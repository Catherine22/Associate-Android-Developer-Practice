package com.catherine.materialdesignapp.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import com.catherine.materialdesignapp.R;
import com.catherine.materialdesignapp.listeners.OnItemClickListener;
import com.catherine.materialdesignapp.models.CursorItem;

import java.util.ArrayList;
import java.util.List;

public class CursorAdapter extends RecyclerView.Adapter<CursorAdapter.MainRvHolder> {
    private final String TAG = CursorAdapter.class.getSimpleName();
    private Context ctx;
    private List<CursorItem> entities;
    private OnItemClickListener listener;

    public CursorAdapter(Context ctx, List<CursorItem> entities, OnItemClickListener listener) {
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

        CursorItem item = entities.get(position);
        if ((item.getType() & CursorItem.TOP) == CursorItem.TOP) {
            mainRvHolder.c_top.setVisibility(View.VISIBLE);
            mainRvHolder.c_body.setVisibility(View.GONE);
            mainRvHolder.c_bottom.setVisibility(View.GONE);

            if (!TextUtils.isEmpty(item.getTitle())) {
                mainRvHolder.tv_header_title.setVisibility(View.VISIBLE);
                mainRvHolder.tv_header_title.setText(item.getTitle());
            } else
                mainRvHolder.tv_header_title.setVisibility(View.GONE);

            if (!TextUtils.isEmpty(item.getSubtitle())) {
                mainRvHolder.tv_header_value.setVisibility(View.VISIBLE);
                mainRvHolder.tv_header_value.setText(item.getSubtitle());
            } else
                mainRvHolder.tv_header_value.setVisibility(View.GONE);

        } else if ((item.getType() & CursorItem.BODY) == CursorItem.BODY) {
            mainRvHolder.c_top.setVisibility(View.GONE);
            mainRvHolder.c_body.setVisibility(View.VISIBLE);
            mainRvHolder.c_bottom.setVisibility(View.GONE);

            if (!TextUtils.isEmpty(item.getTitle())) {
                mainRvHolder.tv_body_title.setVisibility(View.VISIBLE);
                mainRvHolder.tv_body_title.setText(item.getTitle());
            } else
                mainRvHolder.tv_body_title.setVisibility(View.GONE);

            if (!TextUtils.isEmpty(item.getSubtitle())) {
                mainRvHolder.tv_body_value.setVisibility(View.VISIBLE);
                mainRvHolder.tv_body_value.setText(item.getSubtitle());
            } else
                mainRvHolder.tv_body_value.setVisibility(View.GONE);
        } else {
            mainRvHolder.c_top.setVisibility(View.GONE);
            mainRvHolder.c_body.setVisibility(View.GONE);
            mainRvHolder.c_bottom.setVisibility(View.VISIBLE);

            if (!TextUtils.isEmpty(item.getTitle())) {
                mainRvHolder.tv_bottom_title.setVisibility(View.VISIBLE);
                mainRvHolder.tv_bottom_title.setText(item.getTitle());
            } else
                mainRvHolder.tv_bottom_title.setVisibility(View.GONE);

            if (!TextUtils.isEmpty(item.getSubtitle())) {
                mainRvHolder.tv_bottom_value.setVisibility(View.VISIBLE);
                mainRvHolder.tv_bottom_value.setText(item.getSubtitle());
            } else
                mainRvHolder.tv_bottom_value.setVisibility(View.GONE);
        }


    }

    @Override
    public int getItemCount() {
        return entities.size();
    }

    public void setEntities(List<CursorItem> entities) {
        this.entities = entities;
    }

    public List<CursorItem> getEntities() {
        return entities;
    }

    public String getTitle(int pos) {
        return getEntities().get(pos).getTitle();
    }

    public String getValue(int pos) {
        return getEntities().get(pos).getSubtitle();
    }


    class MainRvHolder extends RecyclerView.ViewHolder {
        TextView tv_header_title, tv_body_title, tv_bottom_title;
        TextView tv_header_value, tv_body_value, tv_bottom_value;
        ConstraintLayout c_top, c_body, c_bottom;

        MainRvHolder(View itemView) {
            super(itemView);
            tv_header_title = itemView.findViewById(R.id.tv_header_title);
            tv_body_title = itemView.findViewById(R.id.tv_body_title);
            tv_bottom_title = itemView.findViewById(R.id.tv_bottom_title);
            tv_header_value = itemView.findViewById(R.id.tv_header_value);
            tv_body_value = itemView.findViewById(R.id.tv_body_value);
            tv_bottom_value = itemView.findViewById(R.id.tv_bottom_value);
            c_top = itemView.findViewById(R.id.c_top);
            c_body = itemView.findViewById(R.id.c_body);
            c_bottom = itemView.findViewById(R.id.c_bottom);
        }
    }
}
