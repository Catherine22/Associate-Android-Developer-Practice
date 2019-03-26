package com.catherine.materialdesignapp.utils;

import com.catherine.materialdesignapp.MyApplication;

import java.util.Locale;

public class LocationHelper {

    public String getPreferredLanguage() {
        Locale primaryLocale;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            primaryLocale = MyApplication.INSTANCE.getApplicationContext().getResources().getConfiguration().getLocales().get(0);
        } else {
            primaryLocale = MyApplication.INSTANCE.getApplicationContext().getResources().getConfiguration().locale;
        }

//        primaryLocale.getDisplayLanguage(); // English(United States)
        return primaryLocale.getLanguage() + "_" + primaryLocale.getCountry(); // en_US
    }
}
