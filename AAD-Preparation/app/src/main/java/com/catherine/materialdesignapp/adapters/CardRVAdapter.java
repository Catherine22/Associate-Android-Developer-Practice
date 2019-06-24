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
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.catherine.materialdesignapp.R;
import com.catherine.materialdesignapp.listeners.OnItemClickListener;
import com.catherine.materialdesignapp.models.CardItem;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.facebook.imagepipeline.request.Postprocessor;
import jp.wasabeef.fresco.processors.BlurPostprocessor;

import java.util.ArrayList;
import java.util.List;

public class CardRVAdapter extends RecyclerView.Adapter<CardRVAdapter.MainRvHolder> {
    private final String TAG = CardRVAdapter.class.getSimpleName();
    private Context ctx;
    private List<CardItem> entities;
    private OnItemClickListener listener;
    private boolean fromHtml = false;
    private OnItemClickListener lBListener, rBListener;

    public CardRVAdapter(Context ctx, List<CardItem> entities, OnItemClickListener listener) {
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
        return new MainRvHolder(LayoutInflater.from(ctx).inflate(R.layout.rv_card_item, viewGroup, false));
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

        CardItem cardItem = entities.get(position);
        if (!TextUtils.isEmpty(cardItem.getImage())) {
            mainRvHolder.sdv_main.setVisibility(View.VISIBLE);
            Uri uri = Uri.parse(cardItem.getImage());
            // show raw images
//            mainRvHolder.sdv_main.setImageURI(uri);

            // show blur images
            Postprocessor postprocessor = new BlurPostprocessor(ctx, 30);
            ImageRequest imageRequest = ImageRequestBuilder.newBuilderWithSource(uri)
                    .setPostprocessor(postprocessor)
                    .build();
            PipelineDraweeController controller = (PipelineDraweeController) Fresco.newDraweeControllerBuilder()
                    .setImageRequest(imageRequest)
                    .setOldController(mainRvHolder.sdv_main.getController())
                    .build();
            mainRvHolder.sdv_main.setController(controller);
        } else
            mainRvHolder.sdv_main.setVisibility(View.GONE);

        if (!TextUtils.isEmpty(cardItem.getTitle())) {
            mainRvHolder.tv_title.setVisibility(View.VISIBLE);
            mainRvHolder.tv_title.setText(cardItem.getTitle());
        } else
            mainRvHolder.tv_title.setVisibility(View.GONE);

        if (!TextUtils.isEmpty(cardItem.getSubtitle())) {
            mainRvHolder.tv_subtitle.setVisibility(View.VISIBLE);
            //Enable TextView open url links
            mainRvHolder.tv_subtitle.setMovementMethod(LinkMovementMethod.getInstance());
            if (fromHtml) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    mainRvHolder.tv_subtitle.setText(Html.fromHtml(cardItem.getSubtitle(),
                            Html.FROM_HTML_MODE_COMPACT));
                } else {
                    mainRvHolder.tv_subtitle.setText(Html.fromHtml(cardItem.getSubtitle()));
                }
            } else
                mainRvHolder.tv_subtitle.setText(cardItem.getSubtitle());
        } else {
            mainRvHolder.tv_subtitle.setVisibility(View.GONE);
        }


        if (!TextUtils.isEmpty(cardItem.getLButton())) {
            mainRvHolder.bt_left.setVisibility(View.VISIBLE);
            mainRvHolder.bt_left.setText(cardItem.getLButton());
            if (lBListener != null) {
                mainRvHolder.bt_left.setOnClickListener(v -> lBListener.onItemClick(mainRvHolder.itemView, position));
                mainRvHolder.bt_left.setOnLongClickListener(v -> {
                    lBListener.onItemLongClick(mainRvHolder.itemView, position);
                    return false;
                });
            }
        } else
            mainRvHolder.bt_left.setVisibility(View.GONE);


        if (!TextUtils.isEmpty(cardItem.getRButton())) {
            mainRvHolder.bt_right.setVisibility(View.VISIBLE);
            mainRvHolder.bt_right.setText(cardItem.getRButton());
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

    public void setFromHtml(boolean fromHtml) {
        this.fromHtml = fromHtml;
    }

    public void setEntities(List<CardItem> entities) {
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
        SimpleDraweeView sdv_main;
        Button bt_left;
        Button bt_right;
        RelativeLayout rv_card_bg;
        View itemView;

        MainRvHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            rv_card_bg = itemView.findViewById(R.id.rv_card_bg);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_subtitle = itemView.findViewById(R.id.tv_subtitle);
            bt_left = itemView.findViewById(R.id.bt_left);
            bt_right = itemView.findViewById(R.id.bt_right);
            sdv_main = itemView.findViewById(R.id.sdv_main);
            //fixed aspect ratio = 1334*750 = 16:9;
            sdv_main.setAspectRatio(1.78f);
        }
    }
}
