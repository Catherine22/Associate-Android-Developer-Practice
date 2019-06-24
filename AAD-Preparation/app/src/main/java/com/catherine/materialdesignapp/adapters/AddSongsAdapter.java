package com.catherine.materialdesignapp.adapters;

import android.content.Context;
import android.os.Build;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.catherine.materialdesignapp.R;
import com.catherine.materialdesignapp.jetpack.entities.Song;
import com.catherine.materialdesignapp.listeners.OnItemClickListener;

import java.util.HashMap;
import java.util.Map;

public class AddSongsAdapter extends RecyclerView.Adapter<AddSongsAdapter.MainRvHolder> {
    private final String TAG = AddSongsAdapter.class.getSimpleName();
    private Context ctx;
    private Map<String, Song> entities;
    private OnItemClickListener listener;

    public AddSongsAdapter(Context ctx, Map<String, Song> entities, OnItemClickListener listener) {
        this.ctx = ctx;
        if (entities == null)
            this.entities = new HashMap<>();
        else
            setEntities(entities);
        this.listener = listener;
    }

    @NonNull
    @Override
    public MainRvHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new MainRvHolder(LayoutInflater.from(ctx).inflate(R.layout.rv_add_songs_item, viewGroup, false));
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

        String title = songTitles[position];
        Song song = songs[position];

        if (!TextUtils.isEmpty(title)) {
            mainRvHolder.tv_title.setVisibility(View.VISIBLE);
            mainRvHolder.tv_title.setText(title);
        } else
            mainRvHolder.tv_title.setVisibility(View.GONE);

        if (!TextUtils.isEmpty(song.getArtist())) {
            mainRvHolder.tv_subtitle.setVisibility(View.VISIBLE);
            mainRvHolder.tv_subtitle.setText(song.getArtist());
        } else
            mainRvHolder.tv_subtitle.setVisibility(View.GONE);

        if (!TextUtils.isEmpty(song.getUrl())) {
            mainRvHolder.tv_link.setVisibility(View.VISIBLE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                mainRvHolder.tv_link.setText(Html.fromHtml(song.getUrl(),
                        Html.FROM_HTML_MODE_COMPACT));
            } else {
                mainRvHolder.tv_link.setText(Html.fromHtml(song.getUrl()));
            }
            //Enable TextView open url links
            mainRvHolder.tv_link.setMovementMethod(LinkMovementMethod.getInstance());
        } else {
            mainRvHolder.tv_link.setVisibility(View.GONE);
        }
    }

    private String[] songTitles;
    private Song[] songs;

    private void formatEntities() {
        if (entities == null || entities.size() == 0)
            return;

        songTitles = new String[entities.size()];
        entities.keySet().toArray(songTitles);

        songs = new Song[entities.size()];
        for (int i = 0; i < songs.length; i++) {
            songs[i] = entities.get(songTitles[i]);
        }
    }

    @Override
    public int getItemCount() {
        return entities.size();
    }

    public void setEntities(Map<String, Song> entities) {
        this.entities = entities;
        formatEntities();
    }

    public String getSongName(int position) {
        return songTitles[position];
    }

    public Song getSong(int position) {
        return songs[position];
    }

    class MainRvHolder extends RecyclerView.ViewHolder {
        TextView tv_title;
        TextView tv_subtitle;
        TextView tv_link;

        MainRvHolder(View itemView) {
            super(itemView);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_subtitle = itemView.findViewById(R.id.tv_subtitle);
            tv_link = itemView.findViewById(R.id.tv_link);
        }
    }
}
