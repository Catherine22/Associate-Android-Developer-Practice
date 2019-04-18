package com.catherine.materialdesignapp.activities;

import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CompoundButton;

import com.catherine.materialdesignapp.R;
import com.catherine.materialdesignapp.models.ChannelInfo;
import com.catherine.materialdesignapp.utils.NotificationUtils;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.textfield.TextInputLayout;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class NotificationActivity extends BaseActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    public final static String TAG = NotificationActivity.class.getSimpleName();

    private SwipeRefreshLayout swipeRefreshLayout;
    private TextInputLayout til_title, til_subtitle, til_channel;
    private Set<Integer> selectedChips;
    private Map<String, String> channelTable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true); // enable back arrow on the top left area
        selectedChips = new HashSet<>();
        channelTable = new HashMap<>();
        initComponent();
    }

    private void initComponent() {
        swipeRefreshLayout = findViewById(R.id.srl);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorAccent, R.color.colorPrimaryDark, R.color.colorAccentDark);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            clearData();
            swipeRefreshLayout.setRefreshing(false);
        });
        MaterialButton btn_push = findViewById(R.id.btn_push);
        btn_push.setOnClickListener(this);
        Chip chip_local = findViewById(R.id.chip_local);
        chip_local.setOnCheckedChangeListener(this);
        Chip chip_local_hands_up = findViewById(R.id.chip_local_hands_up);
        chip_local_hands_up.setOnCheckedChangeListener(this);
        Chip chip_local_reply = findViewById(R.id.chip_local_reply);
        chip_local_reply.setOnCheckedChangeListener(this);
        Chip chip_fcm = findViewById(R.id.chip_fcm);
        chip_fcm.setOnCheckedChangeListener(this);
        til_title = findViewById(R.id.til_title);
        til_title.getEditText().addTextChangedListener(new MyTextWatcher(til_title.getEditText().getId()));
        til_subtitle = findViewById(R.id.til_subtitle);
        til_subtitle.getEditText().addTextChangedListener(new MyTextWatcher(til_subtitle.getEditText().getId()));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            til_channel = findViewById(R.id.til_channel);
            til_channel.getEditText().addTextChangedListener(new MyTextWatcher(til_channel.getEditText().getId()));
        }
    }

    private void clearData() {
        selectedChips.clear();
        channelTable.clear();
        til_title.setErrorEnabled(false);
        til_subtitle.setErrorEnabled(false);
        til_channel.setErrorEnabled(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_push:
                if (selectedChips.isEmpty()) {
                    showSnackbar(swipeRefreshLayout, getString(R.string.no_notification_services_selected));
                    return;
                }

                String title = til_title.getEditText().getText().toString();
                if (TextUtils.isEmpty(title)) {
                    til_title.setErrorEnabled(true);
                    til_title.setError(String.format(Locale.US, getString(R.string.cannot_be_empty), getString(R.string.title)));
                    return;
                }

                String subtitle = til_subtitle.getEditText().getText().toString();
                if (TextUtils.isEmpty(subtitle)) {
                    til_subtitle.setErrorEnabled(true);
                    til_subtitle.setError(String.format(Locale.US, getString(R.string.cannot_be_empty), getString(R.string.subtitle)));
                    return;
                }

                String channel = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    channel = til_channel.getEditText().getText().toString();
                    if (TextUtils.isEmpty(channel)) {
                        til_channel.setErrorEnabled(true);
                        til_channel.setError(String.format(Locale.US, getString(R.string.cannot_be_empty), getString(R.string.channel)));
                        return;
                    }
                }

                for (Integer selectedChip : selectedChips) {
                    switch (selectedChip) {
                        case R.id.chip_local:
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                String id;
                                if (channelTable.containsKey(channel)) {
                                    id = channelTable.get(channel);
                                } else {
                                    id = System.currentTimeMillis() / 1000 + "";
                                    channelTable.put(channel, id);
                                }
                                ChannelInfo channelInfo = new ChannelInfo(id, channel);
                                NotificationUtils notificationUtils = new NotificationUtils(this, channelInfo);
                                notificationUtils.sendNotification(title, subtitle, (int) System.currentTimeMillis() / 1000);
                            } else {
                                NotificationUtils notificationUtils = new NotificationUtils(this);
                                notificationUtils.sendNotification(title, subtitle, (int) System.currentTimeMillis() / 1000);
                            }
                            break;
                        case R.id.chip_local_hands_up:
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                String id;
                                if (channelTable.containsKey(channel)) {
                                    id = channelTable.get(channel);
                                } else {
                                    id = System.currentTimeMillis() / 1000 + "";
                                    channelTable.put(channel, id);
                                }
                                ChannelInfo channelInfo = new ChannelInfo(id, channel);
                                NotificationUtils notificationUtils = new NotificationUtils(this, channelInfo);
                                notificationUtils.sendHandsUpNotification(title, subtitle, (int) System.currentTimeMillis() / 1000);
                            } else {
                                NotificationUtils notificationUtils = new NotificationUtils(this);
                                notificationUtils.sendHandsUpNotification(title, subtitle, (int) System.currentTimeMillis() / 1000);
                            }
                            break;
                        case R.id.chip_local_reply:
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                String id;
                                if (channelTable.containsKey(channel)) {
                                    id = channelTable.get(channel);
                                } else {
                                    id = System.currentTimeMillis() / 1000 + "";
                                    channelTable.put(channel, id);
                                }
                                ChannelInfo channelInfo = new ChannelInfo(id, channel);
                                NotificationUtils notificationUtils = new NotificationUtils(this, channelInfo);
                                notificationUtils.sendNotificationWithResponse(title, subtitle, (int) System.currentTimeMillis() / 1000);
                            } else {
                                NotificationUtils notificationUtils = new NotificationUtils(this);
                                notificationUtils.sendNotificationWithResponse(title, subtitle, (int) System.currentTimeMillis() / 1000);
                            }
                            break;
                        case R.id.chip_fcm:

                            break;
                    }
                }
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            selectedChips.add(buttonView.getId());
        } else {
            selectedChips.remove(buttonView.getId());
        }
    }


    private class MyTextWatcher implements TextWatcher {
        private int id;

        MyTextWatcher(int id) {
            this.id = id;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            switch (id) {
                case R.id.tiet_title:
                    til_title.setErrorEnabled(false);
                    break;
            }
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }
}
