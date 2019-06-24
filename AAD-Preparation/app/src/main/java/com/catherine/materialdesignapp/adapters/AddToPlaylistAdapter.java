package com.catherine.materialdesignapp.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.catherine.materialdesignapp.R;
import com.catherine.materialdesignapp.listeners.OnItemClickListener;

public class AddToPlaylistAdapter extends RecyclerView.Adapter<AddToPlaylistAdapter.MainRvHolder> {
    private final String TAG = AddToPlaylistAdapter.class.getSimpleName();
    private Context ctx;
    private String[] entities;
    private OnItemClickListener listener;

    public AddToPlaylistAdapter(Context ctx, String[] entities, OnItemClickListener listener) {
        this.ctx = ctx;
        this.listener = listener;
        this.entities = entities;
    }

    @NonNull
    @Override
    public MainRvHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new MainRvHolder(LayoutInflater.from(ctx).inflate(R.layout.rv_add_to_playlist_item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final MainRvHolder mainRvHolder, final int position) {
        if (entities == null || entities.length == 0)
            return;

        if (listener != null) {
            mainRvHolder.itemView.setOnClickListener(v -> listener.onItemClick(mainRvHolder.itemView, position));
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

    public String getEntity(int position) {
        return entities[position];
    }

    class MainRvHolder extends RecyclerView.ViewHolder {
        TextView tv_title;
        View itemView;

        MainRvHolder(View itemView) {
            super(itemView);
            tv_title = itemView.findViewById(R.id.tv_title);
            this.itemView = itemView;
        }
    }
}
