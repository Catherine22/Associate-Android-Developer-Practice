package com.catherine.materialdesignapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.RecyclerView;
import com.catherine.materialdesignapp.R;
import com.catherine.materialdesignapp.listeners.OnItemClickListener;

public class FunctionAdapter extends RecyclerView.Adapter<FunctionAdapter.MainRvHolder> {
    private final String TAG = FunctionAdapter.class.getSimpleName();
    private Context ctx;
    private OnItemClickListener listener;
    private Pair<String, Integer>[] items;

    public FunctionAdapter(Pair<String, Integer>[] items, Context ctx, OnItemClickListener listener) {
        this.ctx = ctx;
        this.items = items;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MainRvHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(ctx).inflate(R.layout.rv_function_item, viewGroup, false);
        return new MainRvHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MainRvHolder mainRvHolder, final int position) {
        if (items == null || items.length == 0)
            return;

        if (listener != null) {
            mainRvHolder.itemView.setOnClickListener(v -> listener.onItemClick(mainRvHolder.itemView, position));
        }

        String title = items[position].first;
        mainRvHolder.tv_title.setText(title);

        int colour = items[position].second;
        mainRvHolder.container.setBackgroundColor(colour);
    }


    @Override
    public int getItemCount() {
        return items.length;
    }

    class MainRvHolder extends RecyclerView.ViewHolder {
        TextView tv_title;
        ConstraintLayout container;

        MainRvHolder(View itemView) {
            super(itemView);
            tv_title = itemView.findViewById(R.id.tv_title);
            container = itemView.findViewById(R.id.container);
        }
    }
}
