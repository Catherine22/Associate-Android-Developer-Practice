package com.catherine.materialdesignapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        showCustomToast("hello");

    }


    public void showBasicToast(CharSequence message) {
        makeBasicToast(message).show();
    }

    public void showToastOnTopLeft(CharSequence message) {
        Toast toast = makeBasicToast(message);
        toast.setGravity(Gravity.TOP | Gravity.START, 100, 50);
        toast.show();
    }

    public void showCustomToast(CharSequence message) {
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
