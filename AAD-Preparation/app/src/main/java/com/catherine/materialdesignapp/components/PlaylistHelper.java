package com.catherine.materialdesignapp.components;

import android.app.Activity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.catherine.materialdesignapp.FirebaseDB;
import com.catherine.materialdesignapp.R;
import com.catherine.materialdesignapp.adapters.AddToPlaylistAdapter;
import com.catherine.materialdesignapp.jetpack.entities.Playlist;
import com.catherine.materialdesignapp.jetpack.entities.Song;
import com.catherine.materialdesignapp.listeners.OnItemClickListener;
import com.catherine.materialdesignapp.listeners.PlaylistHelperListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class PlaylistHelper implements View.OnClickListener {
    private String TAG = "PlaylistHelper";
    private AddToPlaylistAdapter addToPlaylistAdapter;
    private BottomSheetDialog playlistsDialog, createPlaylistDialog;
    private TextInputLayout til_title;
    private MaterialButton btn_submit;
    private DatabaseReference myRef;
    private List<Playlist> playlists;
    private PlaylistHelperListener listener;
    private Activity activity;
    private Song song;
    private String songName;

    public PlaylistHelper(Activity activity, PlaylistHelperListener listener) {
        this.activity = activity;
        this.listener = listener;
    }

    public void popUpAddToPlaylist(String songName, Song song) {
        this.songName = songName;
        this.song = song;
        playlistsDialog.show();
    }

    public void addToPlaylist(Playlist playlist, String songName, Song song) {
        Map<String, Song> songs = playlist.getSongs();
        songs.put(songName, song);

        myRef.child(playlist.getName()).child("songs").setValue(songs, (databaseError, databaseReference) -> {
            if (databaseError != null)
                Log.e(TAG, databaseError.getMessage());
            playlistsDialog.dismiss();
        });
    }

    public void prepare() {
        // playlist menu
        playlistsDialog = new BottomSheetDialog(activity);
        View playlistView = activity.getLayoutInflater().inflate(R.layout.bottom_sheet_add_to_playlist, null);
        playlistsDialog.setContentView(playlistView);
        RecyclerView rv_playlists = playlistsDialog.findViewById(R.id.rv_playlists);
        rv_playlists.setLayoutManager(new LinearLayoutManager(activity));
        addToPlaylistAdapter = new AddToPlaylistAdapter(activity, fillInTitles(playlists), new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (position != addToPlaylistAdapter.getItemCount() - 1) {
                    addToPlaylist(playlists.get(position), songName, song);
                } else {
                    //create new playlist
                    createPlaylistDialog.show();
                }
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });
        rv_playlists.setAdapter(addToPlaylistAdapter);


        // create a new playlist
        createPlaylistDialog = new BottomSheetDialog(activity);
        View createPlaylistView = activity.getLayoutInflater().inflate(R.layout.bottom_sheet_create_a_playlist, null);
        createPlaylistDialog.setContentView(createPlaylistView);
        btn_submit = createPlaylistDialog.findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(this);
        til_title = createPlaylistDialog.findViewById(R.id.til_title);
        til_title.getEditText().addTextChangedListener(new MyTextWatcher(til_title.getEditText().getId()));


        String DB_PATH = FirebaseDB.PLAYLIST;
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
                addToPlaylistAdapter.setEntities(fillInTitles(playlists));
                addToPlaylistAdapter.notifyDataSetChanged();
                listener.onDataChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
                listener.onCancelled();
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

    @Override
    public void onClick(View v) {
        // create a new playlist with the chosen song
        if (v.getId() == R.id.btn_submit) {
            String name = til_title.getEditText().getText().toString();
            if (TextUtils.isEmpty(name)) {
                til_title.setErrorEnabled(true);
                til_title.setError(String.format(Locale.US, activity.getString(R.string.cannot_be_empty), activity.getString(R.string.title)));
                return;
            }

            Map<String, Song> songChild = new HashMap<>();
            songChild.put(songName, song);

            Map<String, Object> playlistChild = new HashMap<>();
            Playlist newPlaylist = new Playlist();
            newPlaylist.setIndex(playlists.size());
            newPlaylist.setName(name);
            newPlaylist.setSongs(songChild);
            playlistChild.put(name, newPlaylist);

            myRef.updateChildren(playlistChild, (databaseError, databaseReference) -> {
                if (databaseError != null)
                    Log.e(TAG, databaseError.getMessage());

                playlistsDialog.dismiss();
                createPlaylistDialog.dismiss();
            });
        }
    }

    private class MyTextWatcher implements TextWatcher {
        private int id;

        MyTextWatcher(int id) {
            this.id = id;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            if (id == R.id.tiet_title) {
                til_title.setErrorEnabled(false);
            }
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }
}
