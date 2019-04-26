package com.catherine.materialdesignapp.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.catherine.materialdesignapp.R;
import com.catherine.materialdesignapp.listeners.OnItemMoveListener;
import com.catherine.materialdesignapp.listeners.OnPlaylistItemClickListener;
import com.catherine.materialdesignapp.models.Playlist;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.MainRvHolder> implements OnItemMoveListener {
    private final String TAG = PlaylistAdapter.class.getSimpleName();
    private Context ctx;
    private List<Playlist> entities;
    private OnPlaylistItemClickListener listener;

    public PlaylistAdapter(Context ctx, List<Playlist> entities, OnPlaylistItemClickListener listener) {
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
        return new MainRvHolder(LayoutInflater.from(ctx).inflate(R.layout.rv_playlist_item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final MainRvHolder mainRvHolder, final int position) {
        if (entities == null || entities.size() == 0)
            return;

        if (listener != null) {
            mainRvHolder.itemView.setOnClickListener(v -> listener.onItemClicked(mainRvHolder.itemView, position));
            mainRvHolder.iv_add.setOnClickListener(v -> listener.onAddButtonClicked(mainRvHolder.itemView, position));
        }

        Playlist playlist = entities.get(position);
        if (!TextUtils.isEmpty(playlist.getName())) {
            mainRvHolder.tv_title.setVisibility(View.VISIBLE);
            mainRvHolder.tv_title.setText(playlist.getName());
        } else
            mainRvHolder.tv_title.setVisibility(View.GONE);


        if (playlist.getSongs() != null) {
            String songs = String.format(ctx.getResources().getQuantityString(R.plurals.songs, playlist.getSongs().length), playlist.getSongs().length);
            mainRvHolder.tv_song_numbers.setVisibility(View.VISIBLE);
            mainRvHolder.tv_song_numbers.setText(songs);
        } else
            mainRvHolder.tv_song_numbers.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return entities.size();
    }

    public void setEntities(List<Playlist> entities) {
        this.entities = entities;
    }

    public List<Playlist> getEntities() {
        return entities;
    }

    @Override
    public void onDragged(int oldPosition, int newPosition) {
        Collections.swap(entities, oldPosition, newPosition);

        //非常重要，调用后Adapter才能知道发生了改变。
        notifyItemMoved(oldPosition, newPosition);
        listener.onDragged(oldPosition, newPosition);
    }

    @Override
    public void onSwiped(int position) {
        entities.remove(position);

        //非常重要，调用后Adapter才能知道发生了改变。
        notifyDataSetChanged();
        listener.onSwiped(position);
    }

    class MainRvHolder extends RecyclerView.ViewHolder {
        TextView tv_title;
        TextView tv_song_numbers;
        ImageView iv_add;
        View itemView;

        MainRvHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_song_numbers = itemView.findViewById(R.id.tv_song_numbers);
            iv_add = itemView.findViewById(R.id.iv_add);
        }
    }
}
