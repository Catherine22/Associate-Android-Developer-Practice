package com.catherine.materialdesignapp.adapters;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.catherine.materialdesignapp.R;
import com.catherine.materialdesignapp.listeners.OnItemClickListener;
import com.catherine.materialdesignapp.models.Album;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.MainRvHolder> {
    private final String TAG = AlbumAdapter.class.getSimpleName();
    private Context ctx;
    private List<Album> entities;
    private OnItemClickListener listener;
    private boolean fromHtml = false;

    public AlbumAdapter(Context ctx, List<Album> entities, OnItemClickListener listener) {
        this.ctx = ctx;
        if (entities == null)
            this.entities = new ArrayList<>();
        else
            this.entities = entities;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MainRvHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new MainRvHolder(LayoutInflater.from(ctx).inflate(R.layout.rv_album_item, viewGroup, false));
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

        Album album = entities.get(position);
        if (!TextUtils.isEmpty(album.getImage())) {
            mainRvHolder.sdv_photo.setVisibility(View.VISIBLE);
            Uri uri = Uri.parse(album.getImage());
            // show raw images
            mainRvHolder.sdv_photo.setImageURI(uri);
        } else
            mainRvHolder.sdv_photo.setVisibility(View.GONE);

        if (!TextUtils.isEmpty(album.getTitle())) {
            mainRvHolder.tv_title.setVisibility(View.VISIBLE);
            mainRvHolder.tv_title.setText(album.getTitle());
        } else
            mainRvHolder.tv_title.setVisibility(View.GONE);

        if (!TextUtils.isEmpty(album.getArtist())) {
            mainRvHolder.tv_subtitle.setVisibility(View.VISIBLE);
            //Enable TextView open url links
            mainRvHolder.tv_subtitle.setMovementMethod(LinkMovementMethod.getInstance());
            if (fromHtml) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    mainRvHolder.tv_subtitle.setText(Html.fromHtml(album.getArtist(),
                            Html.FROM_HTML_MODE_COMPACT));
                } else {
                    mainRvHolder.tv_subtitle.setText(Html.fromHtml(album.getArtist()));
                }
            } else
                mainRvHolder.tv_subtitle.setText(album.getArtist());
        } else {
            mainRvHolder.tv_subtitle.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return entities.size();
    }

    public void setFromHtml(boolean fromHtml) {
        this.fromHtml = fromHtml;
    }

    public void setEntities(List<Album> entities) {
        this.entities = entities;
    }

    public String getImageUrl(int position) {
        return entities.get(position).getImage();
    }

    class MainRvHolder extends RecyclerView.ViewHolder {
        TextView tv_title;
        TextView tv_subtitle;
        SimpleDraweeView sdv_photo;
        View itemView;

        MainRvHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_subtitle = itemView.findViewById(R.id.tv_subtitle);
            sdv_photo = itemView.findViewById(R.id.sdv_photo);
        }
    }
}
