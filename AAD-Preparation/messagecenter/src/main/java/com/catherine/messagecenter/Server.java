package com.catherine.messagecenter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import static com.catherine.messagecenter.Config.messagesList;

@SuppressWarnings("unused")
public class Server {
    private static final String TAG = "MessageCenter";

    private Context ctx;
    private AsyncResponse ar;

    /**
     * Before you use pushTypes(), you should initialize this constructor
     *
     * @param ctx Your Context
     */
    public Server(Context ctx, AsyncResponse ar) {
        this.ctx = ctx;
        this.ar = ar;
    }

    /**
     * Send a message with broadcast
     *
     * @param action   ID
     * @param messages Send bundle with broadcast
     */
    public void pushBundle(String action, Bundle messages) {
        if (action == null || messages == null) {
            ar.onFailure(ErrorMessages.NULL_POINTER);
            return;
        }
        if (messagesList.containsKey(action)) {
            String key = messagesList.get(action);
            if (!"MESSAGES_BUNDLE".equals(key)) {
                ar.onFailure(ErrorMessages.MULTIPLE_VALUE);
                CLog.e(TAG, "MULTIPLE_VALUE:" + action);
                return;
            }
        }
        Intent broadcast = new Intent(action);
        broadcast.putExtra("MESSAGES_BUNDLE", messages);
        messagesList.put(action, "MESSAGES_BUNDLE");
        LocalBroadcastManager.getInstance(ctx).sendBroadcast(broadcast);
    }


    /**
     * Send a message with broadcast
     *
     * @param action   ID
     * @param messages Send boolean with broadcast
     */
    public void pushBoolean(String action, boolean messages) {
        if (action == null) {
            ar.onFailure(ErrorMessages.NULL_POINTER);
            return;
        }
        if (messagesList.containsKey(action)) {
            String key = messagesList.get(action);
            if (!"MESSAGES_BOOLEAN".equals(key)) {
                ar.onFailure(ErrorMessages.MULTIPLE_VALUE);
                CLog.e(TAG, "MULTIPLE_VALUE:" + action);
                return;
            }
        }
        Intent broadcast = new Intent(action);
        broadcast.putExtra("MESSAGES_BOOLEAN", messages);
        messagesList.put(action, "MESSAGES_BOOLEAN");
        LocalBroadcastManager.getInstance(ctx).sendBroadcast(broadcast);
    }


    /**
     * Send a message with broadcast
     *
     * @param action   ID
     * @param messages Send String with broadcast
     */
    public void pushString(String action, String messages) {
        if (action == null || messages == null) {
            ar.onFailure(ErrorMessages.NULL_POINTER);
            return;
        }
        if (messagesList.containsKey(action)) {
            String key = messagesList.get(action);
            if (!"MESSAGES_STRING".equals(key)) {
                ar.onFailure(ErrorMessages.MULTIPLE_VALUE);
                CLog.e(TAG, "MULTIPLE_VALUE:" + action);
                return;
            }
        }
        Intent broadcast = new Intent(action);
        broadcast.putExtra("MESSAGES_STRING", messages);
        messagesList.put(action, "MESSAGES_STRING");
        LocalBroadcastManager.getInstance(ctx).sendBroadcast(broadcast);
    }


    /**
     * Send a message with broadcast
     *
     * @param action   ID
     * @param messages Send int with broadcast
     */
    public void pushInt(String action, int messages) {
        if (action == null) {
            ar.onFailure(ErrorMessages.NULL_POINTER);
            return;
        }
        if (messagesList.containsKey(action)) {
            String key = messagesList.get(action);
            if (!"MESSAGES_INT".equals(key)) {
                ar.onFailure(ErrorMessages.MULTIPLE_VALUE);
                CLog.e(TAG, "MULTIPLE_VALUE:" + action);
                return;
            }
        }
        Intent broadcast = new Intent(action);
        broadcast.putExtra("MESSAGES_INT", messages);
        messagesList.put(action, "MESSAGES_INT");
        LocalBroadcastManager.getInstance(ctx).sendBroadcast(broadcast);
    }


    /**
     * Send a message with broadcast
     *
     * @param action   ID
     * @param messages Send double with broadcast
     */
    public void pushDouble(String action, double messages) {
        if (action == null) {
            ar.onFailure(ErrorMessages.NULL_POINTER);
            return;
        }
        if (messagesList.containsKey(action)) {
            String key = messagesList.get(action);
            if (!"MESSAGES_DOUBLE".equals(key)) {
                ar.onFailure(ErrorMessages.MULTIPLE_VALUE);
                CLog.e(TAG, "MULTIPLE_VALUE:" + action);
                return;
            }
        }
        Intent broadcast = new Intent(action);
        broadcast.putExtra("MESSAGES_DOUBLE", messages);
        messagesList.put(action, "MESSAGES_DOUBLE");
        LocalBroadcastManager.getInstance(ctx).sendBroadcast(broadcast);
    }

    /**
     * Send a message with broadcast
     *
     * @param action   ID
     * @param messages Send byte with broadcast
     */
    public void pushByte(String action, byte messages) {
        if (action == null) {
            ar.onFailure(ErrorMessages.NULL_POINTER);
            return;
        }
        if (messagesList.containsKey(action)) {
            String key = messagesList.get(action);
            if (!"MESSAGES_BYTE".equals(key)) {
                ar.onFailure(ErrorMessages.MULTIPLE_VALUE);
                CLog.e(TAG, "MULTIPLE_VALUE:" + action);
                return;
            }
        }
        Intent broadcast = new Intent(action);
        broadcast.putExtra("MESSAGES_BYTE", messages);
        messagesList.put(action, "MESSAGES_BYTE");
        LocalBroadcastManager.getInstance(ctx).sendBroadcast(broadcast);
    }
}