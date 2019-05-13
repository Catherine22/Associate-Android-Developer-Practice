package com.catherine.materialdesignapp.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.catherine.materialdesignapp.R;
import com.catherine.materialdesignapp.listeners.OnItemClickListener;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.MainRvHolder> {
    private final String TAG = SongAdapter.class.getSimpleName();
    private Context ctx;
    private String[] entities;
    private OnItemClickListener listener;

    public SongAdapter(Context ctx, String[] entities, OnItemClickListener listener) {
        this.ctx = ctx;
        this.entities = entities;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MainRvHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new MainRvHolder(LayoutInflater.from(ctx).inflate(R.layout.rv_song_item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final MainRvHolder mainRvHolder, final int position) {
        if (entities == null || entities.length == 0)
            return;

        if (listener != null) {
            mainRvHolder.iv_add.setOnClickListener(v -> listener.onItemClick(mainRvHolder.itemView, position));
        }

        String song = entities[position];
        if (!TextUtils.isEmpty(song)) {
            mainRvHolder.tv_title.setVisibility(View.VISIBLE);
            mainRvHolder.tv_title.setText(song);
        } else
            mainRvHolder.tv_title.setVisibility(View.GONE);

    }

    @Override
    public int getItemCount() {
        return entities.length;
    }

    public void setEntities(String[] entities) {
        this.entities = entities;
    }

    public String[] getEntities() {
        return this.entities;
    }

    class MainRvHolder extends RecyclerView.ViewHolder {
        TextView tv_title;
        ImageView iv_add;

        MainRvHolder(View itemView) {
            super(itemView);
            tv_title = itemView.findViewById(R.id.tv_title);
            iv_add = itemView.findViewById(R.id.iv_add);
        }
    }
}
