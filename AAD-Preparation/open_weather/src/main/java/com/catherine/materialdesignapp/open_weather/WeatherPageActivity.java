package com.catherine.materialdesignapp.open_weather;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import com.catherine.materialdesignapp.open_weather.models.Rain;
import com.catherine.materialdesignapp.open_weather.models.Snow;
import com.catherine.materialdesignapp.open_weather.models.WeatherResult;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import okhttp3.*;
import okhttp3.logging.HttpLoggingInterceptor;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class WeatherPageActivity extends BaseActivity implements OnMapReadyCallback, GoogleMap.OnMarkerDragListener {
    public final static String TAG = WeatherPageActivity.class.getSimpleName();
    private final static String WEATHER_URL = "api.openweathermap.org";
    private final static String PATH_DATA = "data";
    private final static String PATH_VERSION = "2.5";
    private final static String PATH_FIND = "find";
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
        init();
    }

    private void init() {
        String[] permission = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};
        getPermissions(permission, new OnRequestPermissionsListener() {
            @Override
            public void onGranted() {
                initView();
                initOkHttp();
            }

            @Override
            public void onDenied(@Nullable List<String> deniedPermissions) {
                finish();
            }

            @Override
            public void onRetry() {
                init();
            }
        });
    }

    private void initView() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true); // enable back arrow on the top left area
            getSupportActionBar().setTitle(TAG);
        }
        tv_info = findViewById(R.id.tv_info);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void initOkHttp() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(message -> Log.i(TAG, message));
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

    private void getForecast(final LatLng latLng) {
        Uri uri = uriBuilder
                .appendPath(PATH_FIND)
                .appendQueryParameter("lat", latLng.latitude + "")
                .appendQueryParameter("lon", latLng.longitude + "")
                .appendQueryParameter("cnt", "10")
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
                popUpWarningDialog(e.getMessage(), (dialog, which) -> getForecast(latLng));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful() || response.code() != 200 || response.body() == null) {
                    String sb = "code: " + response.code();
                    popUpWarningDialog(sb, (dialog, which) -> getForecast(latLng));
                    return;
                }

                final String body = response.body().string();
                Gson gson = new Gson();
                WeatherResult result = gson.fromJson(body, WeatherResult.class);
                // convert 1H/3H to oneHour/threeHours
                try {
                    JSONArray ja = new JSONObject(body).getJSONArray("list");
                    for (int i = 0; i < ja.length(); i++) {
                        JSONObject jo = ja.getJSONObject(i);
                        JSONObject rain = jo.optJSONObject("rain");
                        JSONObject snow = jo.optJSONObject("snow");
                        if (rain != null)
                            result.getList()[i].setRain(gson.fromJson(rain.toString(), Rain.class));
                        if (snow != null)
                            result.getList()[i].setSnow(gson.fromJson(snow.toString(), Snow.class));
                    }
                    Log.d(TAG, result.toString());
                    runOnUiThread(() -> tv_info.setText(body));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.setMyLocationEnabled(true);
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        Location location = getLocation();
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        googleMap.addMarker(new MarkerOptions().position(latLng).title(getMyLocation(latLng)));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        getForecast(latLng);
    }

    @SuppressLint("MissingPermission")
    private Location getLocation() {
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
    }

    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        getMyLocation(marker.getPosition());
        getForecast(marker.getPosition());
    }

    public String getMyLocation(LatLng latLng) {
        String unknown = "unknown";
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<android.location.Address> addresses;
        try {
            addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            if (addresses != null && !addresses.isEmpty()) {
                return addresses.get(0).getLocality();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return unknown;
    }
}
