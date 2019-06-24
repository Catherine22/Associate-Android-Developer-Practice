package com.catherine.materialdesignapp.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import com.catherine.materialdesignapp.MyApplication;

public class NetworkHelper {
    public static boolean getNetworkHealth() {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) MyApplication.INSTANCE.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivityManager == null)
                return false;
            NetworkInfo ni = connectivityManager.getActiveNetworkInfo();
            return ni != null && ni.isConnected();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
