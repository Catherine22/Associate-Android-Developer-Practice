package com.catherine.materialdesignapp.listeners;

import android.view.View;

public interface OnActivityEventListener {
    void getPermissions(String[] permissions, OnRequestPermissionsListener listener);

    void showSnackbar(View layout, CharSequence message);
}
