package com.catherine.materialdesignapp.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.RecyclerView;
import com.catherine.materialdesignapp.R;
import com.catherine.materialdesignapp.listeners.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

public class CursorAdapter extends RecyclerView.Adapter<CursorAdapter.MainRvHolder> {
    private final String TAG = CursorAdapter.class.getSimpleName();
    private Context ctx;
    private List<Pair> entities;
    private OnItemClickListener listener;

    public CursorAdapter(Context ctx, List<Pair> entities, OnItemClickListener listener) {
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
        return new MainRvHolder(LayoutInflater.from(ctx).inflate(R.layout.rv_cursor_item, viewGroup, false));
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

        Pair pair = entities.get(position);
        if (pair.first
                != null && !TextUtils.isEmpty(pair.first.toString())) {
            mainRvHolder.tv_title.setVisibility(View.VISIBLE);
            mainRvHolder.tv_title.setText(pair.first.toString());
        } else
            mainRvHolder.tv_title.setVisibility(View.GONE);


        if (pair.second != null && !TextUtils.isEmpty(pair.second.toString())) {
            mainRvHolder.tv_value.setVisibility(View.VISIBLE);
            mainRvHolder.tv_value.setText(pair.second.toString());
        } else
            mainRvHolder.tv_value.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return entities.size();
    }


    public void setEntities(List<Pair> entities) {
        this.entities = entities;
    }

    public List<Pair> getEntities() {
        return entities;
    }


    class MainRvHolder extends RecyclerView.ViewHolder {
        TextView tv_title;
        TextView tv_value;

        MainRvHolder(View itemView) {
            super(itemView);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_value = itemView.findViewById(R.id.tv_value);
        }
    }
}
