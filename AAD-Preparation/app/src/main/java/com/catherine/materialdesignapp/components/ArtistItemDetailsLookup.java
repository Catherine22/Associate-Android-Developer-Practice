package com.catherine.materialdesignapp.components;

import android.view.MotionEvent;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.selection.ItemDetailsLookup;
import androidx.recyclerview.widget.RecyclerView;
import com.catherine.materialdesignapp.adapters.ArtistAdapter;

public class ArtistItemDetailsLookup extends ItemDetailsLookup<String> {
    private RecyclerView recyclerView;

    public ArtistItemDetailsLookup(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
    }

    @Nullable
    @Override
    public ItemDetails<String> getItemDetails(@NonNull MotionEvent e) {
        View view = recyclerView.findChildViewUnder(e.getX(), e.getY());
        if (view != null) {
            RecyclerView.ViewHolder viewHolder = recyclerView.getChildViewHolder(view);
            if (viewHolder instanceof ArtistAdapter.MainRvHolder) {
                return ((ArtistAdapter.MainRvHolder) viewHolder).getArtistItemDetails(e);
            }
        }
        return null;
    }
}