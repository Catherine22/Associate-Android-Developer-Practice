package com.catherine.materialdesignapp.utils;

import com.catherine.materialdesignapp.MyApplication;

public class OccupiedActions {
    public final static String ACTION_NETWORK_STATE = "ACTION_NETWORK_STATE";
    public final static String IS_CONNECTED = "IS_CONNECTED";
    public final static String TYPE_NAME = "TYPE_NAME";

    public final static String ACTION_UPDATE_LOGGER = "ACTION_UPDATE_LOGGER";


    // actions for broadcast
    public final static String ACTION_UPDATE_NOTIFICATION = String.format("%s.updateNotification", MyApplication.INSTANCE.getPackageName());
    public final static String ACTION_POSITIVE_CLICK = "ACTION_POSITIVE_CLICK";
    public final static String ACTION_NEGATIVE_CLICK = "ACTION_NEGATIVE_CLICK";
    public final static String ACTION_REPLAY = "ACTION_REPLAY";

    public final static String NOTIFICATION_TITLE = "TITLE";
    public final static String NOTIFICATION_BODY = "BODY";
    public final static String NOTIFICATION_ID = "NOTIFICATION_ID";
    public final static String NOTIFICATION_REPLY_KEY = "NOTIFICATION_REPLY_KEY";

}
