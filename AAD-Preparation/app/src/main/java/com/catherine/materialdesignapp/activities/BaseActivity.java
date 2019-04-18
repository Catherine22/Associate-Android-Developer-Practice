package com.catherine.materialdesignapp.activities;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.catherine.materialdesignapp.R;
import com.catherine.materialdesignapp.listeners.OnActivityEventListener;
import com.catherine.materialdesignapp.listeners.OnRequestPermissionsListener;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;

public class BaseActivity extends AppCompatActivity implements OnActivityEventListener {
    private final static String TAG = BaseActivity.class.getSimpleName();
    private OnRequestPermissionsListener listener;

    //constants
    private final static int OPEN_SETTINGS = 1;
    private final static int ACCESS_PERMISSION = 2;
    private final static int PERMISSION_OVERLAY = 3;
    private final static int PERMISSION_WRITE_SETTINGS = 4;

    private final int GRANTED_SAW = 0x0001;     //同意特殊权限(SYSTEM_ALERT_WINDOW)
    private final int GRANTED_WS = 0x0010;      //同意特殊权限(WRITE_SETTINGS)
    private int requestSpec = 0x0000;           //需要的特殊权限
    private int grantedSpec = 0x0000;           //已取得的特殊权限
    private int confirmedSpec = 0x0000;         //已询问的特殊权限
    private List<String> deniedPermissionsList; //被拒绝的权限

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        // with the app theme is "NoActionBar", this bunch of code help to update statusBar style
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(getResources().getColor(R.color.colorPrimary));
                window.setNavigationBarColor(getResources().getColor(R.color.colorPrimary));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 要求用户打开权限,仅限android 6.0 以上
     * <p/>
     * SYSTEM_ALERT_WINDOW 和 WRITE_SETTINGS, 这两个权限比较特殊，
     * 不能通过代码申请方式获取，必须得用户打开软件设置页手动打开，才能授权。
     *
     * @param permissions 手机权限 e.g. Manifest.permission.ACCESS_FINE_LOCATION
     * @param listener    此变量implements事件的接口,负责传递信息
     */
    @TargetApi(Build.VERSION_CODES.M)
    public void getPermissions(String[] permissions, OnRequestPermissionsListener listener) {
        if (permissions == null || permissions.length == 0 || Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            listener.onGranted();
            return;
        }
        this.listener = listener;
        deniedPermissionsList = new LinkedList<>();
        for (String p : permissions) {
            if (p.equals(android.Manifest.permission.SYSTEM_ALERT_WINDOW)) {
                requestSpec |= GRANTED_SAW;
                if (android.provider.Settings.canDrawOverlays(this))
                    grantedSpec |= GRANTED_SAW;
            } else if (p.equals(android.Manifest.permission.WRITE_SETTINGS)) {
                requestSpec |= GRANTED_WS;
                if (android.provider.Settings.System.canWrite(this))
                    grantedSpec |= GRANTED_WS;
            } else if (ActivityCompat.checkSelfPermission(this, p) != PackageManager.PERMISSION_GRANTED) {
                deniedPermissionsList.add(p);
            }

        }

        if (requestSpec != grantedSpec) {
            getASpecPermission(requestSpec);
        } else {// Granted all of the special permissions
            if (deniedPermissionsList.size() != 0) {
                //Ask for the permissions
                String[] deniedPermissions = new String[deniedPermissionsList.size()];
                for (int i = 0; i < deniedPermissionsList.size(); i++) {
                    deniedPermissions[i] = deniedPermissionsList.get(i);
                }
                ActivityCompat.requestPermissions(this, deniedPermissions, ACCESS_PERMISSION);
            } else {
                listener.onGranted();

                requestSpec = 0x0000;
                grantedSpec = 0x0000;
                confirmedSpec = 0x0000;
                deniedPermissionsList = null;
            }
        }
    }


    @TargetApi(Build.VERSION_CODES.M)
    private void getASpecPermission(int permissions) {
        if ((permissions & GRANTED_SAW) == GRANTED_SAW && (permissions & grantedSpec) != GRANTED_SAW) {
            Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + this.getPackageName()));
            startActivityForResult(intent, PERMISSION_OVERLAY);
        }

        if ((permissions & GRANTED_WS) == GRANTED_WS && (permissions & grantedSpec) != GRANTED_WS) {
            Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_WRITE_SETTINGS, Uri.parse("package:" + this.getPackageName()));
            startActivityForResult(intent, PERMISSION_WRITE_SETTINGS);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //Press home key then click icon to launch while checking permission
        if (permissions.length == 0) {
            requestSpec = 0x0000;
            grantedSpec = 0x0000;
            confirmedSpec = 0x0000;
            deniedPermissionsList = null;
            listener.onRetry();
            return;
        }

        List<String> deniedResults = new ArrayList<>();
        for (int i = 0; i < grantResults.length; i++) {
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                deniedResults.add(permissions[i]);
            }
        }

        if ((requestSpec & GRANTED_WS) == GRANTED_WS && (grantedSpec & GRANTED_WS) != GRANTED_WS)
            deniedResults.add(Manifest.permission.WRITE_SETTINGS);

        if ((requestSpec & GRANTED_SAW) == GRANTED_SAW && (grantedSpec & GRANTED_SAW) != GRANTED_SAW)
            deniedResults.add(Manifest.permission.SYSTEM_ALERT_WINDOW);


        if (deniedResults.size() != 0)
            listener.onDenied(deniedResults);
        else
            listener.onGranted();


        requestSpec = 0x0000;
        grantedSpec = 0x0000;
        confirmedSpec = 0x0000;
        deniedPermissionsList = null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "request:" + requestCode + "/resultCode" + resultCode);
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PERMISSION_OVERLAY:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    confirmedSpec |= GRANTED_SAW;
                    confirmedSpec |= grantedSpec;
                    if (android.provider.Settings.canDrawOverlays(this))
                        grantedSpec |= GRANTED_SAW;
                    if (confirmedSpec == requestSpec) {
                        if (deniedPermissionsList.size() != 0) {
                            //Ask for the permissions
                            String[] deniedPermissions = new String[deniedPermissionsList.size()];
                            for (int i = 0; i < deniedPermissionsList.size(); i++) {
                                deniedPermissions[i] = deniedPermissionsList.get(i);
                            }
                            ActivityCompat.requestPermissions(this, deniedPermissions, ACCESS_PERMISSION);
                        } else {
                            List<String> deniedResults = new ArrayList<>();
                            if ((requestSpec & GRANTED_WS) == GRANTED_WS && (grantedSpec & GRANTED_WS) != GRANTED_WS)
                                deniedResults.add(Manifest.permission.WRITE_SETTINGS);

                            if ((requestSpec & GRANTED_SAW) == GRANTED_SAW && (grantedSpec & GRANTED_SAW) != GRANTED_SAW)
                                deniedResults.add(Manifest.permission.SYSTEM_ALERT_WINDOW);

                            if (deniedResults.size() > 0)
                                listener.onDenied(deniedResults);
                            else
                                listener.onGranted();

                            requestSpec = 0x0000;
                            grantedSpec = 0x0000;
                            confirmedSpec = 0x0000;
                            deniedPermissionsList = null;
                        }
                    }
                }
                break;
            case PERMISSION_WRITE_SETTINGS:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    confirmedSpec |= GRANTED_WS;
                    confirmedSpec |= grantedSpec;
                    if (android.provider.Settings.System.canWrite(this))
                        grantedSpec |= GRANTED_WS;
                    if (confirmedSpec == requestSpec) {
                        if (deniedPermissionsList.size() != 0) {
                            //Ask for the permissions
                            String[] deniedPermissions = new String[deniedPermissionsList.size()];
                            for (int i = 0; i < deniedPermissionsList.size(); i++) {
                                deniedPermissions[i] = deniedPermissionsList.get(i);
                            }
                            ActivityCompat.requestPermissions(this, deniedPermissions, ACCESS_PERMISSION);
                        } else {
                            List<String> deniedResults = new ArrayList<>();
                            if ((requestSpec & GRANTED_WS) == GRANTED_WS && (grantedSpec & GRANTED_WS) != GRANTED_WS)
                                deniedResults.add(Manifest.permission.WRITE_SETTINGS);

                            if ((requestSpec & GRANTED_SAW) == GRANTED_SAW && (grantedSpec & GRANTED_SAW) != GRANTED_SAW)
                                deniedResults.add(Manifest.permission.SYSTEM_ALERT_WINDOW);

                            if (deniedResults.size() > 0)
                                listener.onDenied(deniedResults);
                            else
                                listener.onGranted();

                            requestSpec = 0x0000;
                            grantedSpec = 0x0000;
                            confirmedSpec = 0x0000;
                            deniedPermissionsList = null;
                        }
                    }
                }
                break;
            case OPEN_SETTINGS:
                requestSpec = 0x0000;
                grantedSpec = 0x0000;
                confirmedSpec = 0x0000;
                deniedPermissionsList = null;
                listener.onRetry();
                break;
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            showBasicToast("landscape");
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            showBasicToast("portrait");
        }
    }


    public void showSnackbar(View layout, CharSequence message) {
        final Snackbar snackbar = Snackbar.make(layout, message, Snackbar.LENGTH_LONG);
        snackbar.setActionTextColor(getResources().getColor(R.color.yellow));
        snackbar.setAction(getResources().getString(R.string.undo), v -> {
            snackbar.dismiss();
            // do something
        });
        snackbar.show();
    }

    protected void showBasicToast(CharSequence message) {
        makeBasicToast(message).show();
    }

    protected void showToastOnTopLeft(CharSequence message) {
        Toast toast = makeBasicToast(message);
        toast.setGravity(Gravity.TOP | Gravity.START, 100, 50);
        toast.show();
    }

    protected void showCustomToast(CharSequence message) {
        LayoutInflater layoutInflater = getLayoutInflater();
        View layout = layoutInflater.inflate(R.layout.c_toast, findViewById(R.id.custom_toast_container));

        TextView textView = layout.findViewById(R.id.text);
        textView.setText(message);

        Toast toast = new Toast(this);
        toast.setView(layout);
        toast.setDuration(Toast.LENGTH_SHORT);
//        toast.setGravity(Gravity.TOP | Gravity.START, 100, 50);
        toast.show();
    }

    protected Toast makeBasicToast(CharSequence message) {
        return Toast.makeText(this, message, Toast.LENGTH_SHORT);
    }
}