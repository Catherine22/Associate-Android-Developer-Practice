package com.catherine.messagecenter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

@SuppressWarnings("unused")
public class Client {
    private static final String TAG = "MessageCenter";
    private Context ctx;
    private Result result;
    private CustomReceiver cr;
    private BroadcastReceiver broadcastReceiver;
    private LocalBroadcastManager broadcastManager;


    /**
     * Before you use gotMessages(), you should initialize this constructor
     *
     * @param ctx Context
     * @param cr  Implements
     */
    public Client(Context ctx, CustomReceiver cr) {
        this.ctx = ctx;
        this.cr = cr;
        this.result = new Result();
    }

    /**
     * Register to receive messages.
     *
     * @param action ID
     */
    public void gotMessages(final String action) {
        broadcastManager = LocalBroadcastManager.getInstance(ctx);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(action);
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (Config.messagesList.get(action) != null) {
                    CLog.d(TAG, "(" + action + ")You got a message");
                    if (Config.messagesList.containsKey(action)) {
                        switch (Config.messagesList.get(action)) {
                            case "MESSAGES_BUNDLE":
                                result.setBundle(intent.getBundleExtra("MESSAGES_BUNDLE"));
                                cr.onBroadcastReceive(result);
                                break;
                            case "MESSAGES_BOOLEAN":
                                result.setBoolean(intent.getBooleanExtra("MESSAGES_BOOLEAN", false));
                                cr.onBroadcastReceive(result);
                                break;
                            case "MESSAGES_STRING":
                                result.setString(intent.getStringExtra("MESSAGES_STRING"));
                                cr.onBroadcastReceive(result);
                                break;
                            case "MESSAGES_INT":
                                result.setInt(intent.getIntExtra("MESSAGES_INT", -1));
                                cr.onBroadcastReceive(result);
                                break;
                            case "MESSAGES_DOUBLE":
                                result.setDouble(intent.getDoubleExtra("MESSAGES_DOUBLE", 0));
                                cr.onBroadcastReceive(result);
                                break;
                            default:
                                Log.e(TAG, "Unknown action");
                                break;
                        }
                    } else {
                        Log.e(TAG, "Unknown action");
                    }
                }
            }
        };
        broadcastManager.registerReceiver(broadcastReceiver, intentFilter);
    }


    public void unRegister(String actionName) {
        Config.messagesList.remove(actionName);
    }

    /**
     * You should unregister receiver when destroy app
     */
    public void release() {
        if (broadcastReceiver != null)
            broadcastManager.unregisterReceiver(broadcastReceiver);
    }
}
