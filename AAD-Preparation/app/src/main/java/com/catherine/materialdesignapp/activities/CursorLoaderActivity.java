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
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.catherine.materialdesignapp.R;
import com.catherine.materialdesignapp.adapters.CursorAdapter;
import com.catherine.materialdesignapp.listeners.OnItemClickListener;
import com.catherine.materialdesignapp.listeners.OnRequestPermissionsListener;

import java.util.ArrayList;
import java.util.List;

public class CursorLoaderActivity extends BaseActivity {
    public final static String TAG = CursorLoaderActivity.class.getSimpleName();
    public final static String PROVIDER = "provider";
    public final static int CALL_LOGS = 0;

    /**
     * scheme (content://) + table_name (authorities) + path (defined uriMatcher)
     */
    final static Uri Call_LOGS_URI = Uri.parse("content://call_log/calls");
    final String[] callLogsQuery = new String[]{CallLogs.TYPE, CallLogs.NUMBER_PRESENTATION, CallLogs.NUMBER, CallLogs.FEATURES};

    private SwipeRefreshLayout swipeRefreshLayout;
    private ConstraintLayout empty_page;
    private RecyclerView recyclerView;
    private CursorAdapter adapter;
    private ContentResolver resolver;
    private CallLogsContentObserver callLogsContentObserver;

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
                TextView tv_value = view.findViewById(R.id.tv_value);
                copy(tv_value.getText().toString());
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
        }
    }

    private List<Pair> getDefaultList(int type) {
        List<Pair> pairs = new ArrayList<>();
        if (type == CALL_LOGS) {
            pairs.add(new Pair<String, String>(CallLogs.TYPE, null));
            pairs.add(new Pair<String, String>(CallLogs.NUMBER_PRESENTATION, null));
            pairs.add(new Pair<String, String>(CallLogs.NUMBER, null));
            pairs.add(new Pair<String, String>(CallLogs.FEATURES, null));
        }
        return pairs;
    }

    private void registerCallLogsObserver() {
        String[] permission = {Manifest.permission.READ_CALL_LOG, Manifest.permission.WRITE_CALL_LOG};
        getPermissions(permission, new OnRequestPermissionsListener() {
            @Override
            public void onGranted() {
                // register an observer so that you can do something when the phone rings
                callLogsContentObserver = new CallLogsContentObserver(new Handler());
                resolver.registerContentObserver(Call_LOGS_URI, true, callLogsContentObserver);

                // load data
                Cursor cursor = resolver.query(Call_LOGS_URI, callLogsQuery, null, null, null);
                Log.d(TAG, "cursor items:" + cursor.getCount());
                List<Pair> pairs = new ArrayList<>();
                while (cursor.moveToNext()) {
//                    try {
//                        Thread.sleep(500);// to pretend we have a plenty of data
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
                    for (int i = 0; i < 4; i++) {
                        pairs.add(new Pair<>(callLogsQuery[i], cursor.getString(i)));
                    }
                }
                cursor.close();
                adapter.setEntities(pairs);
                adapter.notifyDataSetChanged();
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
         * Call was WIFI call.
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
        public boolean deliverSelfNotifications() {
            return super.deliverSelfNotifications();
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            Log.d(TAG, "onChange:" + selfChange);
            getContentResolver().unregisterContentObserver(this);
            registerCallLogsObserver(); // update data
        }
    }

    @Override
    protected void onDestroy() {
        try {
            getContentResolver().unregisterContentObserver(callLogsContentObserver);
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }
}