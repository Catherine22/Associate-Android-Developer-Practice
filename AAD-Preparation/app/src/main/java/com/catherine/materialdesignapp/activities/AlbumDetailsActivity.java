package com.catherine.materialdesignapp.activities;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.catherine.materialdesignapp.R;
import com.catherine.materialdesignapp.adapters.SongAdapter;
import com.catherine.materialdesignapp.components.PlaylistHelper;
import com.catherine.materialdesignapp.jetpack.entities.Song;
import com.catherine.materialdesignapp.listeners.OnItemClickListener;
import com.catherine.materialdesignapp.listeners.PlaylistHelperListener;
import com.facebook.drawee.view.SimpleDraweeView;

public class AlbumDetailsActivity extends BaseActivity implements PlaylistHelperListener {
    public final static String TAG = AlbumDetailsActivity.class.getSimpleName();
    private String album, artist;
    private NestedScrollView container;
    private Uri coverUri;
    private PlaylistHelper playlistHelper;
    private boolean sentAddToPlaylistEvent;

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
        coverUri = Uri.parse(bundle.getString("cover"));
        album = bundle.getString("album");
        artist = bundle.getString("artist");
        String[] songs = bundle.getStringArray("songs");

        container = findViewById(R.id.container);
        SimpleDraweeView sdv_cover = findViewById(R.id.sdv_cover);
        sdv_cover.setImageURI(coverUri);
        TextView tv_album_name = findViewById(R.id.tv_album_name);
        tv_album_name.setText(album);
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
                Log.d(TAG, "clicked:" + songs[position]);
//                showFragment();
                Song song = new Song();
                song.setAlbum(album);
                song.setArtist(artist);
                song.setUrl(coverUri.toString());
                sentAddToPlaylistEvent = true;
                playlistHelper.popUpAddToPlaylist(songs[position], song);
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });
        recyclerView.setAdapter(adapter);
        playlistHelper = new PlaylistHelper(this, this);
        playlistHelper.prepare();
    }


    @Override
    public void onDataChanged() {
        if (sentAddToPlaylistEvent) {
            showSnackbar(container, "saved");
            sentAddToPlaylistEvent = false;
        }
    }

    @Override
    public void onCancelled() {
        if (sentAddToPlaylistEvent) {
            showSnackbar(container, "failed");
            sentAddToPlaylistEvent = false;
        }
    }
}
