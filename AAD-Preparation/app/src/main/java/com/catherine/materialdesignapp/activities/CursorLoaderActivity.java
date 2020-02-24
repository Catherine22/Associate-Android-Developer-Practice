package com.catherine.materialdesignapp.activities;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentResolver;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.catherine.materialdesignapp.R;
import com.catherine.materialdesignapp.adapters.CursorAdapter;
import com.catherine.materialdesignapp.listeners.OnItemClickListener;
import com.catherine.materialdesignapp.listeners.OnRequestPermissionsListener;
import com.catherine.materialdesignapp.models.CursorItem;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class CursorLoaderActivity extends BaseActivity {
    public final static String TAG = CursorLoaderActivity.class.getSimpleName();
    public final static String PROVIDER = "provider";
    public final static int CALL_LOGS = 0;
    public final static int CONTACTS = 1;

    /**
     * scheme (content://) + table_name (authorities) + path (defined uriMatcher)
     */
    final static Uri Call_LOGS_URI = Uri.parse("content://call_log/calls");
    final String[] callLogsQuery = new String[]{CallLogs.NUMBER, CallLogs.TYPE, CallLogs.NUMBER_PRESENTATION, CallLogs.FEATURES};


    private SwipeRefreshLayout swipeRefreshLayout;
    private ConstraintLayout empty_page;
    private RecyclerView recyclerView;
    private CursorAdapter adapter;
    private ContentResolver resolver;
    private CallLogsContentObserver callLogsContentObserver;
    private ContactsContentObserver contactsContentObserver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cursor_loader);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true); // enable back arrow on the top left area
            getSupportActionBar().setTitle(TAG);
        }

        Bundle bundle = getIntent().getExtras();
        int type = bundle.getInt(PROVIDER, CALL_LOGS);

        swipeRefreshLayout = findViewById(R.id.srl);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorAccent, R.color.colorPrimaryDark, R.color.colorAccentDark);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            functionHub(type);
            swipeRefreshLayout.setRefreshing(false);
        });

        empty_page = findViewById(R.id.empty_page);
        recyclerView = findViewById(R.id.rv_table);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CursorAdapter(this, getDefaultList(type), new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                copy(adapter.getValue(position));
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });
        recyclerView.setAdapter(adapter);
        resolver = getContentResolver();
        functionHub(type);
    }

    private void copy(String string) {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("MaterialDesignApp", string);
        clipboard.setPrimaryClip(clip);
        showSnackbar(swipeRefreshLayout, "copied");
    }

    private void functionHub(int type) {
        if (type == CALL_LOGS) {
            registerCallLogsObserver();
        } else if (type == CONTACTS)
            registerContactsObserver();
    }

    private List<CursorItem> getDefaultList(int type) {
        List<CursorItem> items = new ArrayList<>();
        switch (type) {
            case CALL_LOGS:
                items.add(new CursorItem(CallLogs.NUMBER, null, CursorItem.TOP));
                items.add(new CursorItem(CallLogs.TYPE, null, CursorItem.BODY));
                items.add(new CursorItem(CallLogs.NUMBER_PRESENTATION, null, CursorItem.BODY));
                items.add(new CursorItem(CallLogs.FEATURES, null, CursorItem.BOTTOM));
                return items;
            case CONTACTS:
                items.add(new CursorItem(ContactsContract.Contacts._ID, null, CursorItem.TOP));
                items.add(new CursorItem(ContactsContract.Contacts.DISPLAY_NAME, null, CursorItem.BODY));
                items.add(new CursorItem(ContactsContract.CommonDataKinds.Phone.NUMBER, null, CursorItem.BOTTOM));
                return items;
            default:
                return items;
        }
    }

    private void registerCallLogsObserver() {
        String[] permission = {Manifest.permission.READ_CALL_LOG};
        getPermissions(permission, new OnRequestPermissionsListener() {
            @Override
            public void onGranted() {
                // register an observer so that you can do something when the phone rings
                callLogsContentObserver = new CallLogsContentObserver(new Handler());
                resolver.registerContentObserver(Call_LOGS_URI, true, callLogsContentObserver);

                // load data
                Cursor cursor = resolver.query(Call_LOGS_URI, callLogsQuery, null, null, null);
                Log.d(TAG, "cursor items:" + cursor.getCount());
                List<CursorItem> items = new ArrayList<>();
                while (cursor.moveToNext()) {
//                    try {
//                        Thread.sleep(500);// to pretend we have a plenty of data
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
                    items.add(new CursorItem(callLogsQuery[0], cursor.getString(0), CursorItem.TOP));
                    for (int i = 1; i < 3; i++) {
                        items.add(new CursorItem(callLogsQuery[i], cursor.getString(i), CursorItem.BODY));
                    }
                    items.add(new CursorItem(callLogsQuery[3], cursor.getString(3), CursorItem.BOTTOM));
                }
                cursor.close();
                adapter.setEntities(items);
                adapter.notifyDataSetChanged();


                if (items.size() == 0) {
                    empty_page.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                } else {
                    empty_page.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onDenied(@Nullable List<String> deniedPermissions) {
                finish();
            }

            @Override
            public void onRetry() {
                registerCallLogsObserver();
            }
        });
    }


    List<Integer> contactIds;
    SparseArray<String> idToName;
    SparseArray<List<String>> idToNumberType;
    SparseArray<List<String>> idToCallNumber;

    private void registerContactsObserver() {
        String[] permission = {Manifest.permission.READ_CONTACTS};
        getPermissions(permission, new OnRequestPermissionsListener() {
            @Override
            public void onGranted() {
                // register an observer so that you can do something when the phone rings
                contactsContentObserver = new ContactsContentObserver(new Handler());
                resolver.registerContentObserver(ContactsContract.Contacts.CONTENT_URI, true, contactsContentObserver);

                // load data
                contactIds = new LinkedList<>();
                idToName = new SparseArray<>();
                idToNumberType = new SparseArray<>();
                idToCallNumber = new SparseArray<>();

                // step1: Get all _IDs and names in the table "contact"
                Cursor cursorForContactID = getContentResolver().query(
                        ContactsContract.Contacts.CONTENT_URI,
                        new String[]{
                                ContactsContract.Contacts._ID,
                                ContactsContract.Contacts.DISPLAY_NAME
                        },
                        null,
                        null,
                        null
                );

                if (cursorForContactID != null) {
                    while (cursorForContactID.moveToNext()) {
                        int id = cursorForContactID.getInt(0);
                        String displayName = cursorForContactID.getString(1);
                        contactIds.add(id);
                        idToName.put(id, displayName);
                    }
                }
                if (cursorForContactID != null) {
                    cursorForContactID.close();
                }

                String[] contactTypes = getResources().getStringArray(R.array.contact_types);
                for (int ID : contactIds) {
                    LinkedList<Integer> listRawContacts = new LinkedList<>();
                    // step2: Get all the _IDs in the table "RawContact", which is equal to the raw_contact_IDs in the table "Data"
                    Cursor cursorForRawContactID = getContentResolver().query(
                            ContactsContract.RawContacts.CONTENT_URI,
                            new String[]{ContactsContract.RawContacts._ID},
                            ContactsContract.RawContacts.CONTACT_ID + " = ?",
                            new String[]{String.valueOf(ID)},
                            null
                    );

                    if (cursorForRawContactID == null) {
                        continue;
                    }

                    while (cursorForRawContactID.moveToNext()) {
                        listRawContacts.add(cursorForRawContactID.getInt(cursorForRawContactID.getColumnIndex(ContactsContract.RawContacts._ID)));
                    }

                    cursorForRawContactID.close();


                    // step3: Get phone numbers via raw_contact_ID
                    LinkedList<String> names = new LinkedList<>();
                    LinkedList<String> numbers = new LinkedList<>();
                    for (Integer rawID : listRawContacts) {
                        Cursor cursorForCallNumbers = getContentResolver().query(
                                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                new String[]{
                                        ContactsContract.CommonDataKinds.Phone.TYPE,
                                        ContactsContract.CommonDataKinds.Phone.NUMBER,
                                        ContactsContract.CommonDataKinds.Phone.LABEL
                                },
                                ContactsContract.CommonDataKinds.Phone.RAW_CONTACT_ID + " = ?",
                                new String[]{String.valueOf(rawID)},
                                ContactsContract.CommonDataKinds.Phone.TYPE
                        );

                        if (cursorForCallNumbers != null) {
                            while (cursorForCallNumbers.moveToNext()) {
                                int type = cursorForCallNumbers.getInt(0);
                                String number = cursorForCallNumbers.getString(1);
                                if (type == 0) { // customType
                                    names.add(cursorForCallNumbers.getString(2));
                                } else
                                    names.add(contactTypes[type]);
                                numbers.add(number);
                            }
                        }

                        if (cursorForCallNumbers != null) {
                            cursorForCallNumbers.close();
                        }
                    }
                    idToNumberType.put(ID, names);
                    idToCallNumber.put(ID, numbers);
                }


                List<CursorItem> items = new ArrayList<>();
                for (int i = 0; i < contactIds.size(); i++) {
                    int id = contactIds.get(i);
                    CursorItem idItem = new CursorItem(ContactsContract.Contacts._ID, id + "", CursorItem.TOP);

                    CursorItem displayNameItem = null;
                    if (!TextUtils.isEmpty(idToName.get(id))) {
                        displayNameItem = new CursorItem(ContactsContract.Contacts.DISPLAY_NAME, idToName.get(id), CursorItem.BODY);
                    }


                    List<String> numberTypes = idToNumberType.get(id);
                    List<String> callNumbers = idToCallNumber.get(id);

                    if (numberTypes != null && !numberTypes.isEmpty()) {
                        items.add(idItem);
                        if (displayNameItem != null) {
                            items.add(displayNameItem);
                        }
                        for (int j = 0; j < numberTypes.size(); j++) {
                            if (j == numberTypes.size() - 1) {
                                items.add(new CursorItem(numberTypes.get(j), callNumbers.get(j), CursorItem.BOTTOM));
                            } else {
                                items.add(new CursorItem(numberTypes.get(j), callNumbers.get(j), CursorItem.BODY));
                            }
                        }
                    } else {
                        // no numbers
                        if (displayNameItem != null) {
                            items.add(idItem);
                            displayNameItem = new CursorItem(ContactsContract.Contacts.DISPLAY_NAME, idToName.get(id), (CursorItem.BOTTOM));
                            items.add(displayNameItem);
                        } else {
                            idItem = new CursorItem(ContactsContract.Contacts._ID, id + "", (CursorItem.TOP | CursorItem.BOTTOM));
                            items.add(idItem);
                        }

                    }
                }

                adapter.setEntities(items);
                adapter.notifyDataSetChanged();


                if (items.size() == 0) {
                    empty_page.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                } else {
                    empty_page.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onDenied(@Nullable List<String> deniedPermissions) {
                finish();
            }

            @Override
            public void onRetry() {
                registerContactsObserver();
            }
        });
    }

    class CallLogs {

        /**
         * The phone number as the user entered it.
         * <P>Type: TEXT</P>
         */
        static final String NUMBER = "number";
        /**
         * The type of the call (incoming, outgoing or missed).
         * <P>Type: INTEGER (int)</P>
         *
         * <p>
         * Allowed values:
         * <ul>
         * <li>{@link #INCOMING_TYPE}</li>
         * <li>{@link #OUTGOING_TYPE}</li>
         * <li>{@link #MISSED_TYPE}</li>
         * <li>{@link #VOICEMAIL_TYPE}</li>
         * <li>{@link #REJECTED_TYPE}</li>
         * <li>{@link #BLOCKED_TYPE}</li>
         * <li>{@link #ANSWERED_EXTERNALLY_TYPE}</li>
         * </ul>
         * </p>
         */
        static final String TYPE = "type";
        /**
         * Call log type for incoming calls.
         */
        static final int INCOMING_TYPE = 1;
        /**
         * Call log type for outgoing calls.
         */
        static final int OUTGOING_TYPE = 2;
        /**
         * Call log type for missed calls.
         */
        static final int MISSED_TYPE = 3;
        /**
         * Call log type for voicemails.
         */
        static final int VOICEMAIL_TYPE = 4;
        /**
         * Call log type for calls rejected by direct user action.
         */
        static final int REJECTED_TYPE = 5;
        /**
         * Call log type for calls blocked automatically.
         */
        static final int BLOCKED_TYPE = 6;
        /**
         * Call log type for a call which was answered on another device.  Used in situations where
         * a call rings on multiple devices simultaneously and it ended up being answered on a
         * device other than the current one.
         */
        static final int ANSWERED_EXTERNALLY_TYPE = 7;

        /**
         * The number presenting rules set by the network.
         *
         * <p>
         * Allowed values:
         * <ul>
         * <li>{@link #PRESENTATION_ALLOWED}</li>
         * <li>{@link #PRESENTATION_RESTRICTED}</li>
         * <li>{@link #PRESENTATION_UNKNOWN}</li>
         * <li>{@link #PRESENTATION_PAYPHONE}</li>
         * </ul>
         * </p>
         *
         * <P>Type: INTEGER</P>
         */
        static final String NUMBER_PRESENTATION = "presentation";
        /**
         * Number is allowed to display for caller id.
         */
        static final int PRESENTATION_ALLOWED = 1;
        /**
         * Number is blocked by user.
         */
        static final int PRESENTATION_RESTRICTED = 2;
        /**
         * Number is not specified or unknown by network.
         */
        static final int PRESENTATION_UNKNOWN = 3;
        /**
         * Number is a pay phone.
         */
        static final int PRESENTATION_PAYPHONE = 4;

        /**
         * Bit-mask describing features of the call (e.g. video).
         *
         * <P>Type: INTEGER (int)</P>
         */
        static final String FEATURES = "features";
        /**
         * Call had video.
         */
        static final int FEATURES_VIDEO = 1;
        /**
         * Call was pulled externally.
         */
        static final int FEATURES_PULLED_EXTERNALLY = 1 << 1;
        /**
         * Call was HD.
         */
        static final int FEATURES_HD_CALL = 1 << 2;
        /**
         * Call was WI-FI call.
         */
        static final int FEATURES_WIFI = 1 << 3;
        /**
         * Indicates the call underwent Assisted Dialing.
         *
         * @hide
         */
        static final int FEATURES_ASSISTED_DIALING_USED = 1 << 4;
        /**
         * Call was on RTT at some point
         */
        static final int FEATURES_RTT = 1 << 5;
    }

    private class CallLogsContentObserver extends ContentObserver {
        /**
         * Creates a content observer.
         *
         * @param handler The handler to run {@link #onChange} on, or null if none.
         */
        CallLogsContentObserver(Handler handler) {
            super(handler);
        }

        @Override
        public void onChange(boolean selfChange, Uri uri) {
            super.onChange(selfChange, uri);
            Log.d(TAG, "onChange:" + selfChange + "/uri:" + uri);

        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            Log.d(TAG, "onChange:" + selfChange);
            getContentResolver().unregisterContentObserver(this);
            registerCallLogsObserver(); // update data
        }
    }

    private class ContactsContentObserver extends ContentObserver {
        /**
         * Creates a content observer.
         *
         * @param handler The handler to run {@link #onChange} on, or null if none.
         */
        ContactsContentObserver(Handler handler) {
            super(handler);
        }

        @Override
        public void onChange(boolean selfChange, Uri uri) {
            super.onChange(selfChange, uri);
            Log.d(TAG, "onChange:" + selfChange + "/uri:" + uri);
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            Log.d(TAG, "onChange:" + selfChange);
            getContentResolver().unregisterContentObserver(this);
            registerContactsObserver(); // update data
        }
    }

    @Override
    protected void onDestroy() {
        try {
            getContentResolver().unregisterContentObserver(callLogsContentObserver);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            getContentResolver().unregisterContentObserver(contactsContentObserver);
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }
}