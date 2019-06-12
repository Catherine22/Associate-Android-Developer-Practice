package com.catherine.materialdesignapp.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.catherine.materialdesignapp.R;
import com.catherine.materialdesignapp.components.StepItem;
import com.catherine.materialdesignapp.utils.Storage;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.util.Locale;

public class DynamicDeliveryActivity extends BaseActivity {
    private final static String TAG = DynamicDeliveryActivity.class.getSimpleName();
    private BottomSheetBehavior behavior;
    private ConstraintLayout steps_area;
    private FrameLayout fl_content;
    private StepItem stepItem1, stepItem2, stepItem3, stepItem4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dynamic_delivery);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true); // enable back arrow on the top left area
            getSupportActionBar().setTitle(TAG);
        }

        initView();
    }

    private void initView() {
        fl_content = findViewById(R.id.fl_content);
        steps_area = findViewById(R.id.steps_area);
        stepItem1 = findViewById(R.id.step1);
        stepItem2 = findViewById(R.id.step2);
        stepItem3 = findViewById(R.id.step3);
        stepItem4 = findViewById(R.id.step4);


        View bottomSheet = findViewById(R.id.bottom_sheet);
        behavior = BottomSheetBehavior.from(bottomSheet);
        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                Log.d(TAG, String.format(Locale.US, "bottomSheet: %d", newState));
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            }
        });
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);


        Storage storage = new Storage(this);
        int nightMode = storage.retrieveInt(Storage.NIGHT_MODE);
        if (nightMode == AppCompatDelegate.MODE_NIGHT_YES) {
            bottomSheet.setBackgroundResource(R.drawable.bottom_sheet_dark_bg);
        } else {
            bottomSheet.setBackgroundResource(R.drawable.bottom_sheet_light_bg);
        }

        fl_content.post(() -> {
            int containerHeight = fl_content.getHeight();
            steps_area.post(() -> {
                int stepsAreaHeight = steps_area.getHeight();
                float stepItemHeight =
                        getResources().getDimension(R.dimen.item_line_height) * 2
                                + getResources().getDimension(R.dimen.item_step_circle_diameter);
                int peekHeight = containerHeight - stepsAreaHeight;
                float maxBottomSheetHeight = containerHeight - stepItemHeight;
                bottomSheet.getLayoutParams().height = Math.round(maxBottomSheetHeight);
                bottomSheet.requestLayout();
                behavior.setPeekHeight(peekHeight);
            });
        });
    }
}
