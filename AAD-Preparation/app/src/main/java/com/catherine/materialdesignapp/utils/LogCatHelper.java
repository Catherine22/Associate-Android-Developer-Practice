package com.catherine.materialdesignapp.utils;

import android.util.Log;
import com.catherine.materialdesignapp.MyApplication;
import com.catherine.messagecenter.Server;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import static com.catherine.materialdesignapp.utils.OccupiedActions.ACTION_UPDATE_LOGGER;


public class LogCatHelper {
    public final static String TAG = LogCatHelper.class.getSimpleName();

    public void startRecording() {
        try {
            Server sv = new Server(MyApplication.INSTANCE, errorCode -> Log.e(TAG, "onFailure:" + errorCode));
            // -d: Dump the log to the screen and exits.

            Process process = Runtime.getRuntime().exec(String.format("logcat -d %s:v -v brief", MyApplication.INSTANCE.getPackageName()));
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sv.pushString(ACTION_UPDATE_LOGGER, line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void clearAll() {
        try {
            Runtime.getRuntime().exec("logcat -c");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
