package com.catherine.materialdesignapp.adapters;

import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.selection.ItemDetailsLookup;
import androidx.recyclerview.selection.SelectionTracker;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.catherine.materialdesignapp.R;
import com.catherine.materialdesignapp.jetpack.entities.Artist;
import com.catherine.materialdesignapp.utils.DisplayHelper;
import com.facebook.drawee.view.SimpleDraweeView;

public class ArtistAdapter extends PagedListAdapter<Artist, ArtistAdapter.MainRvHolder> {
    private final String TAG = ArtistAdapter.class.getSimpleName();
    public final static int MAX_COLUMNS = 4;
    private SelectionTracker<String> selectionTracker;
    private float cellWidth;

    public ArtistAdapter() {
        super(DIFF_CALLBACK);
    }

    public void setSelectionTracker(SelectionTracker<String> selectionTracker) {
        this.selectionTracker = selectionTracker;
    }

    @NonNull
    @Override
    public MainRvHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        float gridMargin = viewGroup.getContext().getResources().getDimension(R.dimen.grid_margin);
        cellWidth = (DisplayHelper.getScreenWidth() * 1.0f - (MAX_COLUMNS + 1) * gridMargin) / MAX_COLUMNS;
        return new MainRvHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rv_artist_item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final MainRvHolder mainRvHolder, final int position) {
        Artist artist = getItem(position);
        if (artist != null)
            mainRvHolder.bindTo(position, artist);
        else
            mainRvHolder.clear();
    }


    public class MainRvHolder extends RecyclerView.ViewHolder {
        private TextView tv_title;
        private SimpleDraweeView sdv_photo;
        private ConstraintLayout container;
        private ArtistItemDetails artistItemDetails;
        private Artist artist;

        MainRvHolder(View itemView) {
            super(itemView);
            container = itemView.findViewById(R.id.container);
            tv_title = itemView.findViewById(R.id.tv_title);
            sdv_photo = itemView.findViewById(R.id.sdv_photo);
            artistItemDetails = new ArtistItemDetails();
        }

        public Artist getArtist() {
            return artist;
        }

        void bindTo(int position, Artist artist) {
            this.artist = artist;
            boolean isSelected = false;
            if (selectionTracker != null) {
                if (selectionTracker.isSelected(artist.getArtist())) {
                    isSelected = true;
                }
            }

            artistItemDetails.position = position;
            artistItemDetails.identifier = artist.getArtist();

            // other binding stuff
            if (isSelected) {
                container.setBackgroundResource(R.drawable.artist_border);
            } else {
                container.setBackgroundResource(0);
            }

            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) container.getLayoutParams();
            params.width = Math.round(cellWidth);
            params.height = Math.round(cellWidth);
            container.setLayoutParams(params);

            if (!TextUtils.isEmpty(artist.getImage())) {
                sdv_photo.setVisibility(View.VISIBLE);
                Uri uri = Uri.parse(artist.getImage());
                // show raw images
                sdv_photo.setImageURI(uri);
            } else
                sdv_photo.setVisibility(View.GONE);

            if (!TextUtils.isEmpty(artist.getArtist())) {
                tv_title.setVisibility(View.VISIBLE);
                tv_title.setText(artist.getArtist());
            } else
                tv_title.setVisibility(View.GONE);
        }

        void clear() {
            tv_title.setText("");
            sdv_photo.setImageURI("");
        }

        public ItemDetailsLookup.ItemDetails<String> getArtistItemDetails(@NonNull MotionEvent motionEvent) {
            return artistItemDetails;
        }
    }

    public class ArtistItemDetails extends ItemDetailsLookup.ItemDetails<String> {
        public int position;
        public String identifier;

        @Override
        public int getPosition() {
            return position;
        }

        @Nullable
        @Override
        public String getSelectionKey() {
            return identifier;
        }

        @Override
        public boolean inSelectionHotspot(@NonNull MotionEvent e) {
            return true;
        }

        @Override
        public boolean inDragRegion(@NonNull MotionEvent e) {
            return true;
        }
    }

    private static final DiffUtil.ItemCallback<Artist> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<Artist>() {
                @Override
                public boolean areItemsTheSame(@NonNull Artist oldItem, @NonNull Artist newItem) {
                    return oldItem.compareTo(newItem) == 0;
                }

                @Override
                public boolean areContentsTheSame(@NonNull Artist oldItem,
                                                  @NonNull Artist newItem) {
                    return oldItem.getImage().equals(newItem.getImage());
                }
            };
}
