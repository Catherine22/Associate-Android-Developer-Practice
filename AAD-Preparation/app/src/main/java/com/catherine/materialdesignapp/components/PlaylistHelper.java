package com.catherine.materialdesignapp.components;

import android.app.Activity;
import android.util.Log;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.catherine.materialdesignapp.R;
import com.catherine.materialdesignapp.adapters.AddToPlaylistAdapter;
import com.catherine.materialdesignapp.jetpack.entities.Playlist;
import com.catherine.materialdesignapp.jetpack.entities.Song;
import com.catherine.materialdesignapp.listeners.OnItemClickListener;
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

/**
 * Created by catherine_chen on 2019-05-29.
 * Trend Micro
 * catherine_chen@trendmicro.com
 */
public class PlaylistHelper {
    private String TAG = "PlaylistHelper";
    private AddToPlaylistAdapter addToPlaylistAdapter;
    private BottomSheetDialog bottomSheetDialog;
    private DatabaseReference myRef;
    private List<Playlist> playlists;
    private Activity activity;
    private Song song;
    private String songName;

    public PlaylistHelper(Activity activity) {
        this.activity = activity;
    }

    public void popUpAddToPlaylist(String songName, Song song) {
        this.songName = songName;
        this.song = song;
        bottomSheetDialog.show();
    }

    public void prepare() {
        bottomSheetDialog = new BottomSheetDialog(activity);
        View bottomSheet = activity.getLayoutInflater().inflate(R.layout.bottom_sheet_add_to_playlist, null);
        bottomSheetDialog.setContentView(bottomSheet);
        RecyclerView rv_playlists = bottomSheetDialog.findViewById(R.id.rv_playlists);
        rv_playlists.setLayoutManager(new LinearLayoutManager(activity));
        addToPlaylistAdapter = new AddToPlaylistAdapter(activity, fillInTitles(playlists), new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (position != addToPlaylistAdapter.getItemCount() - 1) {
                    addToPlaylist(playlists.get(position), songName, song);
                } else {
                    //create new playlist
                }
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });
        rv_playlists.setAdapter(addToPlaylistAdapter);


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
            }
        };
        myRef.addValueEventListener(firebaseValueEventListener);
    }

    private String[] fillInTitles(List<Playlist> playlists) {
        String defaultString = activity.getResources().getString(R.string.add_new_playlist);
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

    private void addToPlaylist(Playlist playlist, String songName, Song song) {
        Map<String, Song> songs = playlist.getSongs();
        songs.put(songName, song);

        myRef.child(playlist.getName()).child("songs").setValue(songs, (databaseError, databaseReference) -> {
            if (databaseError != null)
                Log.e(TAG, databaseError.getMessage());
            bottomSheetDialog.dismiss();
        });
    }
}
