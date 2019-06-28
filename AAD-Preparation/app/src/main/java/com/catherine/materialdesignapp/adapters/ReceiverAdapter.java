package com.catherine.materialdesignapp.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.catherine.materialdesignapp.R;
import com.catherine.materialdesignapp.listeners.OnItemClickListener;
import com.catherine.materialdesignapp.models.ReceiverItem;

import java.util.ArrayList;
import java.util.List;

public class ReceiverAdapter extends RecyclerView.Adapter<ReceiverAdapter.MainRvHolder> {
    private final String TAG = ReceiverAdapter.class.getSimpleName();
    private Context ctx;
    private List<ReceiverItem> entities;
    private OnItemClickListener lBListener, rBListener;

    public ReceiverAdapter(Context ctx, List<ReceiverItem> entities, OnItemClickListener rBListener) {
        this.ctx = ctx;
        if (entities == null)
            this.entities = new ArrayList<>();
        else
            this.entities = entities;
        this.rBListener = rBListener;
    }

    public ReceiverAdapter(Context ctx, List<ReceiverItem> entities, OnItemClickListener lBListener, OnItemClickListener rBListener) {
        this.ctx = ctx;
        if (entities == null)
            this.entities = new ArrayList<>();
        else
            this.entities = entities;
        this.lBListener = lBListener;
        this.rBListener = rBListener;
    }

    @NonNull
    @Override
    public MainRvHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new MainRvHolder(LayoutInflater.from(ctx).inflate(R.layout.rv_receiver_item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final MainRvHolder mainRvHolder, final int position) {
        if (entities == null || entities.size() == 0)
            return;

        if (rBListener != null) {
            mainRvHolder.bt_right.setOnClickListener(v -> rBListener.onItemClick(v, position));
            mainRvHolder.bt_right.setOnLongClickListener(v -> {
                rBListener.onItemLongClick(v, position);
                return false;
            });
        }

        if (lBListener != null) {
            mainRvHolder.bt_left.setOnClickListener(v -> lBListener.onItemClick(v, position));
            mainRvHolder.bt_left.setOnLongClickListener(v -> {
                lBListener.onItemLongClick(v, position);
                return false;
            });
        }

        ReceiverItem cardItem = entities.get(position);

        if (!TextUtils.isEmpty(cardItem.title)) {
            mainRvHolder.tv_title.setVisibility(View.VISIBLE);
            mainRvHolder.tv_title.setText(cardItem.title);
        } else
            mainRvHolder.tv_title.setVisibility(View.GONE);

        if (!TextUtils.isEmpty(cardItem.subtitle)) {
            mainRvHolder.tv_subtitle.setVisibility(View.VISIBLE);
            //Enable TextView open url links
            mainRvHolder.tv_subtitle.setMovementMethod(LinkMovementMethod.getInstance());
            mainRvHolder.tv_subtitle.setText(cardItem.subtitle);
        } else {
            mainRvHolder.tv_subtitle.setVisibility(View.GONE);
        }


        if (!TextUtils.isEmpty(cardItem.lButton)) {
            mainRvHolder.bt_left.setVisibility(View.VISIBLE);
            mainRvHolder.bt_left.setText(cardItem.lButton);
            if (lBListener != null) {
                mainRvHolder.bt_left.setOnClickListener(v -> lBListener.onItemClick(mainRvHolder.itemView, position));
                mainRvHolder.bt_left.setOnLongClickListener(v -> {
                    lBListener.onItemLongClick(mainRvHolder.itemView, position);
                    return false;
                });
            }
        } else
            mainRvHolder.bt_left.setVisibility(View.GONE);


        if (!TextUtils.isEmpty(cardItem.rButton)) {
            mainRvHolder.bt_right.setVisibility(View.VISIBLE);
            mainRvHolder.bt_right.setText(cardItem.rButton);
            if (rBListener != null) {
                mainRvHolder.bt_right.setOnClickListener(v -> rBListener.onItemClick(mainRvHolder.itemView, position));
                mainRvHolder.bt_right.setOnLongClickListener(v -> {
                    rBListener.onItemLongClick(mainRvHolder.itemView, position);
                    return false;
                });
            }
        } else
            mainRvHolder.bt_right.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return entities.size();
    }

    public void setEntities(List<ReceiverItem> entities) {
        this.entities = entities;
    }

    public void setOnLeftButtonClickListener(OnItemClickListener listener) {
        lBListener = listener;
    }

    public void setOnRightButtonClickListener(OnItemClickListener listener) {
        rBListener = listener;
    }

    class MainRvHolder extends RecyclerView.ViewHolder {
        TextView tv_title;
        TextView tv_subtitle;
        Button bt_left;
        Button bt_right;
        View itemView;

        MainRvHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_subtitle = itemView.findViewById(R.id.tv_subtitle);
            bt_left = itemView.findViewById(R.id.bt_left);
            bt_right = itemView.findViewById(R.id.bt_right);
        }
    }
}
