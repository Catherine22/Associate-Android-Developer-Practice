package com.catherine.materialdesignapp.components;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.selection.ItemKeyProvider;

import com.catherine.materialdesignapp.jetpack.entities.Artist;

import java.util.List;

public class ArtistItemKeyProvider extends ItemKeyProvider<String> {
    private List<Artist> artistList;

    public ArtistItemKeyProvider(List<Artist> artistList) {
        super(SCOPE_CACHED);
        this.artistList = artistList;
    }

    public void updateList(List<Artist> artistList) {
        this.artistList = artistList;
    }

    @Nullable
    @Override
    public String getKey(int position) {
        return artistList.get(position).getArtist();
    }

    @Override
    public int getPosition(@NonNull String key) {
        if (artistList == null || artistList.size() == 0)
            return -1;

        for (int i = 0; i < artistList.size(); i++) {
            if (key.equals(artistList.get(i).getArtist())) {
                return i;
            }
        }
        return -1;
    }
}