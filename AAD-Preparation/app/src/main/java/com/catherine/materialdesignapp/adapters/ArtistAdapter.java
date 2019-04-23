package com.catherine.materialdesignapp.adapters;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.catherine.materialdesignapp.R;
import com.catherine.materialdesignapp.listeners.OnItemClickListener;
import com.catherine.materialdesignapp.models.Artist;
import com.catherine.materialdesignapp.utils.DisplayHelper;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class ArtistAdapter extends RecyclerView.Adapter<ArtistAdapter.MainRvHolder> {
    private final String TAG = ArtistAdapter.class.getSimpleName();
    private Context ctx;
    private List<Artist> entities;
    private OnItemClickListener listener;

    public final static int MAX_COLUMNS = 4;
    private float gridMargin;
    private float cellWidth;

    public ArtistAdapter(Context ctx, List<Artist> entities, OnItemClickListener listener) {
        this.ctx = ctx;
        if (entities == null)
            this.entities = new ArrayList<>();
        else
            this.entities = entities;
        this.listener = listener;


        gridMargin = ctx.getResources().getDimension(R.dimen.grid_margin);
        cellWidth = (DisplayHelper.getScreenWidth() * 1.0f - (MAX_COLUMNS + 1) * gridMargin) / MAX_COLUMNS;
    }

    @NonNull
    @Override
    public MainRvHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new MainRvHolder(LayoutInflater.from(ctx).inflate(R.layout.rv_artist_item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final MainRvHolder mainRvHolder, final int position) {
        if (entities == null || entities.size() == 0)
            return;

        if (listener != null) {
            mainRvHolder.itemView.setOnClickListener(v -> listener.onItemClick(mainRvHolder.itemView, position));
            mainRvHolder.itemView.setOnLongClickListener(v -> {
                listener.onItemLongClick(mainRvHolder.itemView, position);
                return false;
            });
        }

        RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) mainRvHolder.container.getLayoutParams();
        params.width = Math.round(cellWidth);
        params.height = Math.round(cellWidth);
        mainRvHolder.container.setLayoutParams(params);

        Artist artist = entities.get(position);
        if (!TextUtils.isEmpty(artist.getImage())) {
            mainRvHolder.sdv_photo.setVisibility(View.VISIBLE);
            Uri uri = Uri.parse(artist.getImage());
            // show raw images
            mainRvHolder.sdv_photo.setImageURI(uri);
        } else
            mainRvHolder.sdv_photo.setVisibility(View.GONE);

        if (!TextUtils.isEmpty(artist.getArtist())) {
            mainRvHolder.tv_title.setVisibility(View.VISIBLE);
            mainRvHolder.tv_title.setText(artist.getArtist());
        } else
            mainRvHolder.tv_title.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return entities.size();
    }

    public void setEntities(List<Artist> entities) {
        this.entities = entities;
    }

    class MainRvHolder extends RecyclerView.ViewHolder {
        TextView tv_title;
        SimpleDraweeView sdv_photo;
        ConstraintLayout container;
        View itemView;

        MainRvHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            container = itemView.findViewById(R.id.container);
            tv_title = itemView.findViewById(R.id.tv_title);
            sdv_photo = itemView.findViewById(R.id.sdv_photo);
        }
    }
}
