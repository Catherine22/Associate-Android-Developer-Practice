package com.catherine.materialdesignapp.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.RecyclerView;
import com.catherine.materialdesignapp.R;
import com.catherine.materialdesignapp.listeners.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

public class LifecycleAdapter extends RecyclerView.Adapter<LifecycleAdapter.MainRvHolder> {
    private final String TAG = LifecycleAdapter.class.getSimpleName();
    private Context ctx;
    private List<Pair<String, String>> entities;
    private boolean[] unfoldedItems;
    private OnItemClickListener onItemClickListener;

    public LifecycleAdapter(Context ctx, List<Pair<String, String>> entities, OnItemClickListener onItemClickListener) {
        this.ctx = ctx;
        if (entities == null)
            this.entities = new ArrayList<>();
        else
            this.entities = entities;
        this.onItemClickListener = onItemClickListener;
        unfoldedItems = new boolean[this.entities.size()];
    }

    @NonNull
    @Override
    public MainRvHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new MainRvHolder(LayoutInflater.from(ctx).inflate(R.layout.rv_lifecycle_item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final MainRvHolder mainRvHolder, final int position) {
        if (entities == null || entities.size() == 0)
            return;

        if (onItemClickListener != null)
            mainRvHolder.bt_show_body.setOnClickListener(v -> onItemClickListener.onItemClick(mainRvHolder.itemView, position));

        Pair<String, String> pair = entities.get(position);

        if (!TextUtils.isEmpty(pair.first)) {
            mainRvHolder.tv_title.setVisibility(View.VISIBLE);
            mainRvHolder.tv_title.setText(pair.first);
        } else
            mainRvHolder.tv_title.setVisibility(View.GONE);

        if (!TextUtils.isEmpty(pair.second)) {
            mainRvHolder.tv_body.setVisibility(View.VISIBLE);
            mainRvHolder.tv_body.setText(pair.second);
        } else
            mainRvHolder.tv_body.setVisibility(View.GONE);
        dropDownOrUp(mainRvHolder.itemView, position);
    }

    @Override
    public int getItemCount() {
        return entities.size();
    }

    public void updateContent(int pos, String content) {
        Pair<String, String> p = new Pair<>(entities.get(pos).first, content);
        entities.set(pos, p);
        unfoldedItems[pos] = true;
        notifyDataSetChanged();
    }

    public List<Pair<String, String>> getEntities() {
        return this.entities;
    }

    public boolean isUnfolded(int pos) {
        return unfoldedItems[pos];
    }

    public void unfold(int pos, boolean unfold) {
        unfoldedItems[pos] = unfold;
    }

    public void dropDownOrUp(View view, int pos) {
        TextView tv_body = view.findViewById(R.id.tv_body);
        ImageButton bt_show_body = view.findViewById(R.id.bt_show_body);
        if (isUnfolded(pos)) {
            tv_body.setVisibility(View.VISIBLE);
            tv_body.setText(entities.get(pos).second);
            bt_show_body.setImageDrawable(ctx.getResources().getDrawable(R.drawable.ic_arrow_drop_up_black_24dp));
        } else {
            tv_body.setVisibility(View.GONE);
            bt_show_body.setImageDrawable(ctx.getResources().getDrawable(R.drawable.ic_arrow_drop_down_black_24dp));
        }
    }

    class MainRvHolder extends RecyclerView.ViewHolder {
        TextView tv_title;
        TextView tv_body;
        ImageButton bt_show_body;
        View itemView;

        MainRvHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_body = itemView.findViewById(R.id.tv_body);
            bt_show_body = itemView.findViewById(R.id.bt_show_body);
        }
    }
}
