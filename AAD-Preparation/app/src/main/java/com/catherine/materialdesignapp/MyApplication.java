package com.catherine.materialdesignapp;

import android.app.Application;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Build;
import android.os.Environment;
import android.os.HandlerThread;
import android.util.Log;

import com.catherine.materialdesignapp.services.NetworkHealthJobScheduler;
import com.catherine.materialdesignapp.services.NetworkHealthService;
import com.catherine.materialdesignapp.utils.FileUtils;
import com.facebook.cache.disk.DiskCacheConfig;
import com.facebook.common.util.ByteConstants;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;

import java.io.File;
import java.util.Map;

import static com.catherine.materialdesignapp.services.BusyJobs.JOB_NETWORK_STATE;

public class MyApplication extends Application {
    private static String TAG = MyApplication.class.getSimpleName();
    public static MyApplication INSTANCE;
    public HandlerThread musicPlayerThread;


    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE = this;

        //Invoke the largest storage to save data.
        Map<String, File> externalLocations = FileUtils.getAllStorageLocations();
        File sdCard = externalLocations.get(Constants.SD_CARD);
        File externalSdCard = externalLocations.get(Constants.EXTERNAL_SD_CARD);
        long sdCardSize = 0;
        long extSdCardSize = 0;
        if (sdCard != null) {
            sdCardSize = sdCard.getFreeSpace();
            Log.i(TAG, String.format("free storage: (sd card) %s", sdCard.getAbsolutePath()));
            Log.i(TAG, String.format("free storage: (sd card) %s MB", sdCard.getFreeSpace() / ByteConstants.MB));
        }
        if (externalSdCard != null) {
            extSdCardSize = externalSdCard.getFreeSpace();
            Log.i(TAG, String.format("free storage: (external sd card) %s", externalSdCard.getAbsolutePath()));
            Log.i(TAG, String.format("free storage: (external sd card) %s MB", externalSdCard.getFreeSpace() / ByteConstants.MB));
        }

        try {
            if (sdCardSize > extSdCardSize) {
                Constants.ROOT_PATH = String.format("%s/%s", sdCard.getAbsolutePath(), Constants.DIR_NAME);
            } else {
                Constants.ROOT_PATH = String.format("%s/%s", externalSdCard.getAbsolutePath(), Constants.DIR_NAME);
            }
        } catch (NullPointerException e) {
            Constants.ROOT_PATH = String.format("%s/%s", Environment.getExternalStorageDirectory().getAbsolutePath(), Constants.DIR_NAME);

        }
        Log.i(TAG, String.format("Your data will save in %s", Constants.ROOT_PATH));


        // start network monitoring service
        Intent intent = new Intent(INSTANCE, NetworkHealthService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            try {
                JobScheduler jobScheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
                ComponentName componentName = new ComponentName(this, NetworkHealthJobScheduler.class);
                JobInfo jobInfo = new JobInfo.Builder(JOB_NETWORK_STATE, componentName)
                        .setRequiresStorageNotLow(false)
                        .setRequiresBatteryNotLow(true)
                        .setRequiresCharging(false)
                        .build();
                jobScheduler.schedule(jobInfo);
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        } else {
            startService(intent);
        }
    }

    /**
     * Initialise Fresco
     */
    public void init(boolean isCacheable) {
        File rootDir = new File(Constants.ROOT_PATH);
        if (!rootDir.exists())
            rootDir.mkdirs();

        //fresco
        if (isCacheable) {
            //check free memory (MB)
            File externalStorageDir = Environment.getExternalStorageDirectory();
            long temp = externalStorageDir.getFreeSpace();
            long free = (temp > 10 * ByteConstants.MB) ? 20 * ByteConstants.MB : temp;
            Log.i(TAG, String.format("free memory = %s MB, use %s MB to cache images.", temp / ByteConstants.MB, free / (2 * ByteConstants.MB)));
            DiskCacheConfig diskCacheConfig = DiskCacheConfig.newBuilder(MyApplication.this)
                    .setCacheErrorLogger((category, clazz, message, throwable) -> Log.e("Fresco CacheError", String.format("%s: %s", category, message)))
                    .setVersion(1)
                    .setMaxCacheSize(free / 2)
                    .setBaseDirectoryName(Constants.FRESCO_DIR)
                    .setBaseDirectoryPath(rootDir)
                    .build();
            ImagePipelineConfig config = ImagePipelineConfig.newBuilder(MyApplication.this)
                    .setResizeAndRotateEnabledForNetwork(true)
                    .setMainDiskCacheConfig(diskCacheConfig)
                    .build();
            Fresco.initialize(this, config);
        } else
            Fresco.initialize(this);
    }
}
