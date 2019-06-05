package com.catherine.materialdesignapp;

import android.os.Environment;

public class Constants {
    public static String ROOT_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/data/" + MyApplication.INSTANCE.getPackageName() + "/";
    public static String DIR_NAME = "MaterialDesignApp";
    public static final String SD_CARD = "sdCard";
    public static final String EXTERNAL_SD_CARD = "externalSdCard";
    public final static String FRESCO_DIR = "fresco";
    public static final int ROOM_DATABASE_VERSION = 1;
}
