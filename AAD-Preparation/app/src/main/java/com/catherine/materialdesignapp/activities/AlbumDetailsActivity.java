package com.catherine.materialdesignapp.activities;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.catherine.materialdesignapp.R;
import com.catherine.materialdesignapp.adapters.SongAdapter;
import com.catherine.materialdesignapp.fragments.PlaylistFragment;
import com.catherine.materialdesignapp.listeners.OnItemClickListener;
import com.facebook.drawee.view.SimpleDraweeView;

public class AlbumDetailsActivity extends BaseActivity {
    private final static String TAG = AlbumDetailsActivity.class.getSimpleName();
    private ConstraintLayout container;

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

        // song list
        RecyclerView recyclerView = findViewById(R.id.rv_songs);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        SongAdapter adapter = new SongAdapter(this, songs, new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Log.d(TAG, "clicked:" + position);
                showFragment();
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });
        recyclerView.setAdapter(adapter);
        container = findViewById(R.id.container);
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 0)
            ActivityCompat.finishAfterTransition(this);
        else {
            getSupportFragmentManager().popBackStack();
            container.setVisibility(View.VISIBLE);
        }
    }

    private void showFragment() {
        container.setVisibility(View.INVISIBLE);
        getSupportFragmentManager().beginTransaction()
                .addToBackStack("bottom_sheet")
                .replace(R.id.fl_bottom_fragment, new PlaylistFragment(), "bottom_sheet")
                .commit();
    }
}
