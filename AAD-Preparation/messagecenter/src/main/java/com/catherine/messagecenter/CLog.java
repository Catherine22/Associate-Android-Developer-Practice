package com.catherine.messagecenter;

import android.util.Log;

@SuppressWarnings("unused")
class CLog {

    static void v(String TAG, String message) {
        if (Config.showDebugLog) {
            Log.v(TAG, message);
        }
    }

    static void d(String TAG, String message) {
        if (Config.showDebugLog) {
            Log.d(TAG, message);
        }
    }

    static void e(String TAG, String message) {
        if (Config.showDebugLog) {
            Log.e(TAG, message);
        }
    }

    static void i(String TAG, String message) {
        if (Config.showDebugLog) {
            Log.i(TAG, message);
        }
    }

    static void w(String TAG, String message) {
        if (Config.showDebugLog) {
            Log.w(TAG, message);
        }
    }

    static class out {
        static void println(String message) {
            if (Config.showDebugLog) {
                System.out.println(message);
            }
        }
    }
}