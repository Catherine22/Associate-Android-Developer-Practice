package com.catherine.materialdesignapp.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.ContentLoadingProgressBar;

import com.catherine.materialdesignapp.BuildConfig;
import com.catherine.materialdesignapp.R;
import com.catherine.materialdesignapp.components.StepItem;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.button.MaterialButton;
import com.google.android.play.core.splitcompat.SplitCompat;
import com.google.android.play.core.splitinstall.SplitInstallManager;
import com.google.android.play.core.splitinstall.SplitInstallManagerFactory;
import com.google.android.play.core.splitinstall.SplitInstallRequest;
import com.google.android.play.core.splitinstall.SplitInstallSessionState;
import com.google.android.play.core.splitinstall.model.SplitInstallSessionStatus;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DynamicDeliveryActivity extends BaseActivity {
    public final static String TAG = DynamicDeliveryActivity.class.getSimpleName();
    private BottomSheetBehavior behavior;
    private ConstraintLayout steps_area;
    private FrameLayout fl_content;
    private View bottomSheet;

    // components inside the bottom sheet
    private MaterialButton btn_launch, btn_uninstall;
    private TextView tv_info;
    private ContentLoadingProgressBar progressBar;
    private int currentStepInt;
    private String[] bundleInfo;

    private int containerHeight;
    private int stepsAreaHeight;
    private float stepItemHeight;

    private final DynamicModule[] dynamicModules = {
            new DynamicModule(null, "bbc_news", "com.catherine.materialdesignapp.bbc_news.NewsPageActivity"),
            new DynamicModule(null, "tour_guide", "com.catherine.materialdesignapp.tourguide.LonelyPlanetPageActivity"),
            new DynamicModule(null, "assets", "dictionary.txt"),
            new DynamicModule(null, "open_weather", "com.catherine.materialdesignapp.open_weather.WeatherPageActivity")
    };

    private SplitInstallManager splitInstallManager;
    private final static int CONFIRMATION_REQUEST_CODE = 1;
    private final static int NEW_ACTIVITY_REQUEST_CODE = 2;

    class DynamicModule {
        StepItem stepItem;
        String moduleName;
        String target;
        boolean isInstalled;

        DynamicModule(StepItem stepItem, String moduleName, String target) {
            this.stepItem = stepItem;
            this.moduleName = moduleName;
            this.target = target;
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
        StepItem stepItem1 = findViewById(R.id.step1);
        StepItem stepItem2 = findViewById(R.id.step2);
        StepItem stepItem3 = findViewById(R.id.step3);
        StepItem stepItem4 = findViewById(R.id.step4);
        stepItem1.setOnClickListener(this::onClick);
        stepItem2.setOnClickListener(this::onClick);
        stepItem3.setOnClickListener(this::onClick);
        stepItem4.setOnClickListener(this::onClick);
        dynamicModules[0].stepItem = stepItem1;
        dynamicModules[1].stepItem = stepItem2;
        dynamicModules[2].stepItem = stepItem3;
        dynamicModules[3].stepItem = stepItem4;
        bundleInfo = getResources().getStringArray(R.array.bundle_info);


        bottomSheet = findViewById(R.id.bottom_sheet);
        btn_launch = bottomSheet.findViewById(R.id.btn_launch);
        btn_launch.setOnClickListener(this::onClick);
        btn_uninstall = bottomSheet.findViewById(R.id.btn_uninstall);
        btn_uninstall.setOnClickListener(this::onClick);
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

        currentStepInt = 1;
        updateBottomSheetView(currentStepInt);
    }

    @Override
    protected void onResume() {
        splitInstallManager.registerListener(this::onStateUpdate);
        super.onResume();
    }

    @Override
    protected void onPause() {
        splitInstallManager.unregisterListener(this::onStateUpdate);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        splitInstallManager.unregisterListener(this::onStateUpdate);
        super.onDestroy();
    }

    private void onClick(View v) {
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
                    if (currentStepInt == 3)
                        openAssets();
                    else
                        launchNewModule();
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
        tv_info.setText(bundleInfo[currentStepInt - 1]);
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

    private void onStateUpdate(SplitInstallSessionState state) {
        DynamicModule module = dynamicModules[currentStepInt - 1];
        switch (state.status()) {
            case SplitInstallSessionStatus.DOWNLOADING:
                Log.d(TAG, "displayLoadingState - DOWNLOADING");
                updateDownloadProgress(state);
                break;
            case SplitInstallSessionStatus.REQUIRES_USER_CONFIRMATION:
                /*
                  This may occur when attempting to download a sufficiently large module.
                  To see this, the application has to be uploaded to the Play Store.
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
                    if (currentStepInt == 3)
                        openAssets();
                    else
                        launchNewModule();
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

    // step 3
    private void openAssets() {
        String target = dynamicModules[currentStepInt - 1].target;
        Log.d(TAG, "Open " + target);
        BufferedReader reader = null;
        try {
            AssetManager assetManager = createPackageContext(getPackageName(), 0).getAssets();
            reader = new BufferedReader(new InputStreamReader(assetManager.open(target)));
            String mLine;
            StringBuilder content = new StringBuilder();
            while ((mLine = reader.readLine()) != null) {
                content.append(mLine);
                content.append("\n");
            }

            AlertDialog.Builder builder = new AlertDialog.Builder(DynamicDeliveryActivity.this);
            AlertDialog dialog = builder
                    .setPositiveButton(R.string.ok, (dialog1, which) -> {
                        dialog1.dismiss();
                    })
                    .setTitle("Loaded " + target)
                    .setMessage(content)
                    .setCancelable(false)
                    .create();
            dialog.show();
            nextStep(currentStepInt);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private void launchNewModule() {
        String activity = dynamicModules[currentStepInt - 1].target;
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
            nextStep(currentStepInt);
        }
    }

    private void nextStep(int currentStep) {
        DynamicModule module = dynamicModules[currentStep - 1];
        module.stepItem.isFinished(true);
        module.isInstalled = true;
        if (currentStepInt < dynamicModules.length) {
            currentStepInt += 1;
            updateBottomSheetView(currentStepInt);
        }
    }
}
