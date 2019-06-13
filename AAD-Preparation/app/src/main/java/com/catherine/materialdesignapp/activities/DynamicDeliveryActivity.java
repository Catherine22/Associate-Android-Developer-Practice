package com.catherine.materialdesignapp.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.ContentLoadingProgressBar;

import com.catherine.materialdesignapp.BuildConfig;
import com.catherine.materialdesignapp.R;
import com.catherine.materialdesignapp.components.StepItem;
import com.catherine.materialdesignapp.utils.Storage;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.button.MaterialButton;
import com.google.android.play.core.splitcompat.SplitCompat;
import com.google.android.play.core.splitinstall.SplitInstallManager;
import com.google.android.play.core.splitinstall.SplitInstallManagerFactory;
import com.google.android.play.core.splitinstall.SplitInstallRequest;
import com.google.android.play.core.splitinstall.SplitInstallSessionState;
import com.google.android.play.core.splitinstall.SplitInstallStateUpdatedListener;
import com.google.android.play.core.splitinstall.model.SplitInstallSessionStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DynamicDeliveryActivity extends BaseActivity implements View.OnClickListener, SplitInstallStateUpdatedListener {
    private final static String TAG = DynamicDeliveryActivity.class.getSimpleName();
    private BottomSheetBehavior behavior;
    private ConstraintLayout steps_area;
    private FrameLayout fl_content;
    private StepItem stepItem1, stepItem2, stepItem3, stepItem4;
    private View bottomSheet;

    // components inside the bottom sheet
    private MaterialButton btn_launch, btn_uninstall;
    private TextView tv_info;
    private ContentLoadingProgressBar progressBar;
    private int currentStepInt;

    private int containerHeight;
    private int stepsAreaHeight;
    private float stepItemHeight;

    private final DynamicModule[] dynamicModules = {
            new DynamicModule(stepItem1, "bbc_news", "com.trendmicro.diamond.bbc_news.NewsPageActivity"),
            new DynamicModule(stepItem2, "tour_guide", "com.trendmicro.diamond.tourguide.LonelyPlanetPageActivity"),
            new DynamicModule(stepItem3, "bbc_news", "com.trendmicro.diamond.bbc_news.NewsPageActivity"),
            new DynamicModule(stepItem4, "tour_guide", "com.trendmicro.diamond.tourguide.LonelyPlanetPageActivity")
    };

    private SplitInstallManager splitInstallManager;
    private final static int CONFIRMATION_REQUEST_CODE = 1;
    private final static int NEW_ACTIVITY_REQUEST_CODE = 2;

    class DynamicModule {
        StepItem stepItem;
        String moduleName;
        String launchActivity;
        boolean isInstalled;

        DynamicModule(StepItem stepItem, String moduleName, String launchActivity) {
            this.stepItem = stepItem;
            this.moduleName = moduleName;
            this.launchActivity = launchActivity;
        }
    }

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
        splitInstallManager = SplitInstallManagerFactory.create(this);
    }

    private void initView() {
        fl_content = findViewById(R.id.fl_content);
        steps_area = findViewById(R.id.steps_area);
        stepItem1 = findViewById(R.id.step1);
        stepItem2 = findViewById(R.id.step2);
        stepItem3 = findViewById(R.id.step3);
        stepItem4 = findViewById(R.id.step4);
        stepItem1.setOnClickListener(this);
        stepItem2.setOnClickListener(this);
        stepItem3.setOnClickListener(this);
        stepItem4.setOnClickListener(this);
        dynamicModules[0].stepItem = stepItem1;
        dynamicModules[1].stepItem = stepItem2;
        dynamicModules[2].stepItem = stepItem3;
        dynamicModules[3].stepItem = stepItem4;


        bottomSheet = findViewById(R.id.bottom_sheet);
        btn_launch = bottomSheet.findViewById(R.id.btn_launch);
        btn_launch.setOnClickListener(this);
        btn_uninstall = bottomSheet.findViewById(R.id.btn_uninstall);
        btn_uninstall.setOnClickListener(this);
        tv_info = bottomSheet.findViewById(R.id.tv_info);
        progressBar = bottomSheet.findViewById(R.id.progress_bar);
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


        Storage storage = new Storage(this);
        int nightMode = storage.retrieveInt(Storage.NIGHT_MODE);
        if (nightMode == AppCompatDelegate.MODE_NIGHT_YES) {
            bottomSheet.setBackgroundResource(R.drawable.bottom_sheet_dark_bg);
        } else {
            bottomSheet.setBackgroundResource(R.drawable.bottom_sheet_light_bg);
        }

        currentStepInt = 1;
        updateBottomSheetView(currentStepInt);
    }

    @Override
    protected void onResume() {
        splitInstallManager.registerListener(this);
        super.onResume();
    }

    @Override
    protected void onPause() {
        splitInstallManager.unregisterListener(this);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        splitInstallManager.unregisterListener(this);
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        DynamicModule module = dynamicModules[currentStepInt - 1];
        switch (v.getId()) {
            case R.id.step1:
                currentStepInt = 1;
                updateBottomSheetView(currentStepInt);
                break;
            case R.id.step2:
                currentStepInt = 2;
                updateBottomSheetView(currentStepInt);
                break;
            case R.id.step3:
                currentStepInt = 3;
                updateBottomSheetView(currentStepInt);
                break;
            case R.id.step4:
                currentStepInt = 4;
                updateBottomSheetView(currentStepInt);
                break;
            case R.id.btn_launch:
                // load and launch module
                if (splitInstallManager.getInstalledModules().contains(module.moduleName)) {
                    Log.d(TAG, "loaded successfully");
                    launchNewModule(module.moduleName);
                    progressBar.hide();
                    return;
                }
                Log.d(TAG, "start to install");
                progressBar.show();
                SplitInstallRequest request = SplitInstallRequest.newBuilder()
                        .addModule(module.moduleName)
                        .build();
                splitInstallManager.startInstall(request);
                break;
            case R.id.btn_uninstall:
                List<String> modules = new ArrayList<>();
                modules.add(module.moduleName);
                splitInstallManager.deferredUninstall(modules)
                        .addOnSuccessListener(aVoid -> {
                            Log.d(TAG, "Uninstalling " + module.moduleName);
                            module.isInstalled = false;
                            module.stepItem.isFinished(false);
                            updateBottomSheetView(currentStepInt);

                            if (currentStepInt > 1) {
                                currentStepInt--;
                                updateBottomSheetView(currentStepInt);
                            }
                        })
                        .addOnFailureListener(e -> Log.e(TAG, "Failed installation of " + module.moduleName));
                break;
        }
    }

    private void updateBottomSheetView(int step) {
        fl_content.post(() -> {
            containerHeight = fl_content.getHeight();
            steps_area.post(() -> {
                stepsAreaHeight = steps_area.getHeight();
                stepItemHeight =
                        getResources().getDimension(R.dimen.item_line_height) * 2 + getResources().getDimension(R.dimen.item_step_circle_diameter);
                int peekHeight = containerHeight - stepsAreaHeight;
                float maxBottomSheetHeight = containerHeight - stepItemHeight * step;
                bottomSheet.getLayoutParams().height = Math.round(maxBottomSheetHeight);
                bottomSheet.requestLayout();
                behavior.setPeekHeight(peekHeight);
                behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            });
        });
        btn_uninstall.setEnabled(dynamicModules[currentStepInt - 1].isInstalled);
    }

    @Override
    protected void attachBaseContext(Context ctx) {
        super.attachBaseContext(ctx);
        SplitCompat.install(this);
    }

    private void updateDownloadProgress(SplitInstallSessionState state) {
        progressBar.show();
        progressBar.setMax(Math.round(state.totalBytesToDownload()));
        progressBar.setProgress(Math.round(state.bytesDownloaded()));
    }

    @Override
    public void onStateUpdate(SplitInstallSessionState state) {
        DynamicModule module = dynamicModules[currentStepInt - 1];
        switch (state.status()) {
            case SplitInstallSessionStatus.DOWNLOADING:
                Log.d(TAG, "displayLoadingState - DOWNLOADING");
                updateDownloadProgress(state);
                break;
            case SplitInstallSessionStatus.REQUIRES_USER_CONFIRMATION:
                /*
                  This may occur when attempting to download a sufficiently large module.
                  In order to see this, the application has to be uploaded to the Play Store.
                  Then features can be requested until the confirmation path is triggered.
                 */
                Log.d(TAG, "startConfirmationDialogForResult");
                try {
                    splitInstallManager.startConfirmationDialogForResult(state, this, CONFIRMATION_REQUEST_CODE);
                } catch (IntentSender.SendIntentException e) {
                    e.printStackTrace();
                }
                break;
            case SplitInstallSessionStatus.INSTALLED:
                Log.d(TAG, "displayLoadingState - INSTALLED");
                if (splitInstallManager.getInstalledModules().contains(module.moduleName)) {
                    Log.d(TAG, "loaded successfully");
                    launchNewModule(module.moduleName);
                    progressBar.hide();
                    return;
                }
                break;
            case SplitInstallSessionStatus.INSTALLING:
                Log.d(TAG, "displayLoadingState - INSTALLING");
                progressBar.hide();
                break;
            case SplitInstallSessionStatus.FAILED:
                Log.e(TAG, "failed:" + state.errorCode());
                break;
        }
    }

    private void launchNewModule(String module) {
        String activity = null;
        for (DynamicModule dynamicModule : dynamicModules) {
            if (module.equals(dynamicModule.moduleName))
                activity = dynamicModule.launchActivity;
        }
        Log.d(TAG, "Launch " + activity);
        Intent intent = new Intent();
        intent.setClassName(BuildConfig.APPLICATION_ID, activity);
        startActivityForResult(intent, NEW_ACTIVITY_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CONFIRMATION_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_CANCELED) {
                Log.e(TAG, "user cancelled");
            }
        } else if (requestCode == NEW_ACTIVITY_REQUEST_CODE) {
            btn_uninstall.setEnabled(true);
            DynamicModule module = dynamicModules[currentStepInt - 1];
            module.stepItem.isFinished(true);
            module.isInstalled = true;
            if (currentStepInt < dynamicModules.length) {
                currentStepInt += 1;
                updateBottomSheetView(currentStepInt);
            }
        }
    }
}
