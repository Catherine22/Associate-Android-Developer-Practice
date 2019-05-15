package com.catherine.materialdesignapp.activities;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.catherine.materialdesignapp.R;
import com.catherine.materialdesignapp.adapters.AddToPlaylistAdapter;
import com.catherine.materialdesignapp.adapters.SongAdapter;
import com.catherine.materialdesignapp.listeners.OnItemClickListener;
import com.catherine.materialdesignapp.models.Playlist;
import com.catherine.materialdesignapp.models.Song;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class AlbumDetailsActivity extends BaseActivity {
    private final static String TAG = AlbumDetailsActivity.class.getSimpleName();
    private BottomSheetDialog bottomSheetDialog;
    private AddToPlaylistAdapter addToPlaylistAdapter;
    private String albumName, artist;
    private DatabaseReference myRef;
    private List<Playlist> playlists;

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
        albumName = bundle.getString("album");
        artist = bundle.getString("artist");
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
//                showFragment();
                bottomSheetDialog.show();

            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });
        recyclerView.setAdapter(adapter);

        // bottom sheet
        bottomSheetDialog = new BottomSheetDialog(this);
        View bottomSheet = getLayoutInflater().inflate(R.layout.bottom_sheet_add_to_playlist, null);
        bottomSheetDialog.setContentView(bottomSheet);

        RecyclerView rv_playlists = bottomSheetDialog.findViewById(R.id.rv_playlists);
        rv_playlists.setLayoutManager(new LinearLayoutManager(this));
        addToPlaylistAdapter = new AddToPlaylistAdapter(this, null, new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                if (position != addToPlaylistAdapter.getItemCount() - 1) {
                    addToPlaylist(playlists.get(position), addToPlaylistAdapter.getEntity(position));
                } else {
                    //create new playlist
                }
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });
        rv_playlists.setAdapter(addToPlaylistAdapter);
        getPlaylists();
    }

    private void getPlaylists() {
        String DB_PATH = "test_playlist"; // TODO playlists
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference(DB_PATH);
        ValueEventListener firebaseValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                Log.d(TAG, String.format("size: %d", dataSnapshot.getChildrenCount()));
                playlists = new ArrayList<>();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    Playlist playlist = child.getValue(Playlist.class);
                    Log.i(TAG, String.format("%s: %s", child.getKey(), playlist));
                    playlists.add(playlist);
                }
                Collections.sort(playlists);
                addToPlaylistAdapter.setEntities(fillInTitles(playlists));
                addToPlaylistAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
                addToPlaylistAdapter.setEntities(fillInTitles(null));
                addToPlaylistAdapter.notifyDataSetChanged();
            }
        };
        myRef.addValueEventListener(firebaseValueEventListener);
    }

    private String[] fillInTitles(List<Playlist> playlists) {
        String defaultString = getResources().getString(R.string.add_new_playlist);
        if (playlists == null || playlists.size() == 0) {
            return new String[]{defaultString};
        } else {
            String[] titles = new String[playlists.size() + 1];
            for (int i = 0; i < playlists.size(); i++) {
                titles[i] = playlists.get(i).getName();
            }
            titles[titles.length - 1] = defaultString;
            return titles;
        }
    }

    private void addToPlaylist(Playlist playlist, String songName) {
        Song song = new Song();
        song.setAlbum(albumName);
        song.setArtist(artist);
        Map<String, Song> songs = playlist.getSongs();
        songs.put(songName, song);

        myRef.child(playlist.getName()).child("songs").setValue(songs, (databaseError, databaseReference) -> {
            if (databaseError != null)
                Log.e(TAG, databaseError.getMessage());
        });
    }
}
