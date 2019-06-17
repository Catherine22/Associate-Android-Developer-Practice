package com.catherine.materialdesignapp.open_weather;

import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import com.catherine.materialdesignapp.open_weather.models.WeatherResult;
import com.google.gson.Gson;
import okhttp3.*;
import okhttp3.logging.HttpLoggingInterceptor;

import java.io.IOException;

public class WeatherPageActivity extends BaseActivity {
    private final static String TAG = WeatherPageActivity.class.getSimpleName();
    private final static String WEATHER_URL = "api.openweathermap.org";
    private final static String PATH_DATA = "data";
    private final static String PATH_VERSION = "2.5";
    private final static String PATH_WEATHER = "weather";
    private final static String APP_ID = BuildConfig.OPEN_WEATHER_API_KEY;

    private OkHttpClient client;
    private Uri.Builder uriBuilder;
    private Headers.Builder headersBuilder;
    private AlertDialog alertDialog;
    private TextView tv_info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_page);
        initView();
        initOkHttp();
        getLondonForecast();
    }

    private void initView() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true); // enable back arrow on the top left area
            getSupportActionBar().setTitle(TAG);
        }
        tv_info = findViewById(R.id.tv_info);
    }

    private void initOkHttp() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                Log.i(TAG, message);
            }
        });
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        pinSSL();

        client = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build();
        uriBuilder = new Uri.Builder()
                .scheme("http")
                .authority(WEATHER_URL)
                .appendPath(PATH_DATA)
                .appendPath(PATH_VERSION);
        headersBuilder = new Headers.Builder()
                .add("Content-Type", "application/json");
    }

    private void pinSSL() {

    }

    private void getLondonForecast() {
        Uri uri = uriBuilder
                .appendPath(PATH_WEATHER)
                .appendQueryParameter("q", "London")
                .appendQueryParameter("appid", APP_ID)
                .build();
        Headers headers = headersBuilder.build();
        Request request = new Request.Builder()
                .headers(headers)
                .url(uri.toString())
                .get()
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                popUpWarningDialog(e.getMessage(), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getLondonForecast();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful() || response.code() != 200 || response.body() == null) {
                    String sb = "code: " + response.code();
                    popUpWarningDialog(sb, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            getLondonForecast();
                        }
                    });
                    return;
                }

                String body = response.body().string();
                Gson gson = new Gson();
                final WeatherResult result = gson.fromJson(body, WeatherResult.class);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tv_info.setText(result.toString());
                    }
                });
            }
        });
    }

    private void popUpWarningDialog(String message, DialogInterface.OnClickListener onRetry) {
        if (alertDialog != null && alertDialog.isShowing())
            alertDialog.dismiss();
        alertDialog = new AlertDialog.Builder(WeatherPageActivity.this)
                .setTitle(R.string.warnings)
                .setIcon(R.drawable.ic_warning_black_24dp)
                .setTitle(message)
                .setNegativeButton(R.string.retry, onRetry)
                .create();
        alertDialog.show();
    }
}
