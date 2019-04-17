package com.catherine.materialdesignapp.models;

import android.util.SparseArray;

public class NotificationChannelsGroup {
    public final static int FIREBASE = 0;
    public final static int FOREGROUND_SERVICE = 1;

    public static SparseArray<ChannelInfo> CHANNELS = new SparseArray<>();

    static {
        CHANNELS.put(FIREBASE, new ChannelInfo(FIREBASE + "", "My Firebase Channel"));
        CHANNELS.put(FOREGROUND_SERVICE, new ChannelInfo(FOREGROUND_SERVICE + "", "My Foreground Service"));
    }
}