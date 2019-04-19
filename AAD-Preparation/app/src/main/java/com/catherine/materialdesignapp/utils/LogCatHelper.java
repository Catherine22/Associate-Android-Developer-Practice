package com.catherine.materialdesignapp.utils;

import com.catherine.materialdesignapp.listeners.LogCatListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class LogCatHelper {
    private LogCatListener listener;
    private String cache;

    public void setLogCatListener(LogCatListener listener) {
        this.listener = listener;
    }

    public void startRecording() {
        try {
            Process process = Runtime.getRuntime().exec("logcat -d");
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));

            StringBuilder log = new StringBuilder();
            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                log.append(line);
            }
            cache = log.toString();
            if (listener != null)
                listener.onLog(cache);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void clearLog() {
        try {
            Process process = Runtime.getRuntime().exec("logcat -");
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));

            StringBuilder log = new StringBuilder();
            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                log.append(line);
            }
            cache = "";
            if (listener != null)
                listener.onLog(cache);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
