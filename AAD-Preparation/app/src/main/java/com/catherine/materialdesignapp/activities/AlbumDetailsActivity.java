package com.catherine.materialdesignapp.activities;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.catherine.materialdesignapp.R;
import com.catherine.materialdesignapp.adapters.SongAdapter;
import com.catherine.materialdesignapp.listeners.OnItemClickListener;
import com.facebook.drawee.view.SimpleDraweeView;

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
        String[] songs = bundle.getStringArray("songs");

        SimpleDraweeView sdv_cover = findViewById(R.id.sdv_cover);
        sdv_cover.setImageURI(coverUri);
        TextView tv_album_name = findViewById(R.id.tv_album_name);
        tv_album_name.setText(albumName);
        TextView tv_artist = findViewById(R.id.tv_artist);
        tv_artist.setText(artist);

        if (songs == null || songs.length == 0)
            return;
        RecyclerView recyclerView = findViewById(R.id.rv_songs);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        SongAdapter adapter = new SongAdapter(this, songs, new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        ActivityCompat.finishAfterTransition(this);
    }
}
