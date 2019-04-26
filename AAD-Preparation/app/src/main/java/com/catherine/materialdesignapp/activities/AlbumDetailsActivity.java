package com.catherine.materialdesignapp.activities;

import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;

import com.catherine.materialdesignapp.R;
import com.facebook.drawee.view.SimpleDraweeView;

import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

public class AlbumDetailsActivity extends BaseActivity {
    private final static String TAG = AlbumDetailsActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_details);
        initComponent();
    }

    private void initComponent() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true); // enable back arrow on the top left area
            getSupportActionBar().setTitle(TAG);
        }

        Bundle bundle = getIntent().getExtras();
        Uri coverUri = Uri.parse(bundle.getString("cover"));
        String albumName = bundle.getString("album");
        String artist = bundle.getString("artist");

        SimpleDraweeView sdv_cover = findViewById(R.id.sdv_cover);
        sdv_cover.setImageURI(coverUri);
        TextView tv_album_name = findViewById(R.id.tv_album_name);
        tv_album_name.setText(albumName);
        TextView tv_artist = findViewById(R.id.tv_artist);
        tv_artist.setText(artist);
    }

    @Override
    public void onBackPressed() {
        ActivityCompat.finishAfterTransition(this);
    }
}
