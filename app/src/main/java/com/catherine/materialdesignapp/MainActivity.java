package com.catherine.materialdesignapp;

import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private final static String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        showSnackbar("dddd");
    }


    private void showSnackbar(CharSequence message) {
        ConstraintLayout layout = findViewById(R.id.cl_background);
        final Snackbar snackbar = Snackbar.make(layout, message, Snackbar.LENGTH_LONG);
        snackbar.setActionTextColor(getResources().getColor(R.color.yellow));
        snackbar.setAction(getResources().getString(R.string.undo), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
                // do something
            }
        });
        snackbar.show();
    }

    private void showBasicToast(CharSequence message) {
        makeBasicToast(message).show();
    }

    private void showToastOnTopLeft(CharSequence message) {
        Toast toast = makeBasicToast(message);
        toast.setGravity(Gravity.TOP | Gravity.START, 100, 50);
        toast.show();
    }

    private void showCustomToast(CharSequence message) {
        LayoutInflater layoutInflater = getLayoutInflater();
        View layout = layoutInflater.inflate(R.layout.c_toast, (ViewGroup) findViewById(R.id.custom_toast_container));

        TextView textView = layout.findViewById(R.id.text);
        textView.setText(message);

        Toast toast = new Toast(this);
        toast.setView(layout);
        toast.setDuration(Toast.LENGTH_SHORT);
//        toast.setGravity(Gravity.TOP | Gravity.START, 100, 50);
        toast.show();
    }

    private Toast makeBasicToast(CharSequence message) {
        return Toast.makeText(this, message, Toast.LENGTH_SHORT);
    }
}
