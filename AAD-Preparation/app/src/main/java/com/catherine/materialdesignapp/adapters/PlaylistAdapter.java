package com.catherine.materialdesignapp.adapters;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.catherine.materialdesignapp.R;
import com.catherine.materialdesignapp.jetpack.entities.Playlist;

public class PlaylistAdapter extends PagedListAdapter<Playlist, PlaylistAdapter.MainRvHolder> {
    private final String TAG = PlaylistAdapter.class.getSimpleName();
    private ItemClickEvent itemClickEvent;

    public PlaylistAdapter() {
        super(DIFF_CALLBACK);
    }

    public PlaylistAdapter(ItemClickEvent itemClickEvent) {
        this();
        this.itemClickEvent = itemClickEvent;
    }

    @NonNull
    @Override
    public MainRvHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new MainRvHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rv_playlist_item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final MainRvHolder mainRvHolder, final int position) {
        Playlist playlist = getItem(position);
        if (playlist != null)
            mainRvHolder.bindTo(playlist);
        else
            mainRvHolder.clear();
    }

    public class MainRvHolder extends RecyclerView.ViewHolder {
        private TextView tv_title;
        private TextView tv_song_numbers;
        private ImageView iv_add;
        private Playlist playlist;

        MainRvHolder(View itemView) {
            super(itemView);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_song_numbers = itemView.findViewById(R.id.tv_song_numbers);
            iv_add = itemView.findViewById(R.id.iv_add);
        }

        public Playlist getPlaylist() {
            return playlist;
        }

        void bindTo(Playlist playlist) {
            this.playlist = playlist;
            if (itemClickEvent != null)
                iv_add.setOnClickListener(v -> itemClickEvent.onClick(itemView, playlist));

            if (!TextUtils.isEmpty(playlist.getName())) {
                tv_title.setVisibility(View.VISIBLE);
                tv_title.setText(playlist.getName());
            } else
                tv_title.setVisibility(View.GONE);


            // default string: 0 songs
            String songs = String.format(itemView.getContext().getResources().getQuantityString(R.plurals.songs, 0), 0);
            if (playlist.getSongs() != null) {
                songs = String.format(itemView.getContext().getResources().getQuantityString(R.plurals.songs, playlist.getSongs().size()), playlist.getSongs().size());
            }
            tv_song_numbers.setVisibility(View.VISIBLE);
            tv_song_numbers.setText(songs);
        }

        void clear() {
            tv_title.setText("");
            tv_song_numbers.setText("");
        }

    }

    public interface ItemClickEvent {
        void onClick(View view, Playlist playlist);
    }

    private static final DiffUtil.ItemCallback<Playlist> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<Playlist>() {
                @Override
                public boolean areItemsTheSame(@NonNull Playlist oldItem, @NonNull Playlist newItem) {
                    return oldItem.compareTo(newItem) == 0;
                }

                @Override
                public boolean areContentsTheSame(@NonNull Playlist oldItem,
                                                  @NonNull Playlist newItem) {
                    return oldItem.getSongs().equals(newItem.getSongs());
                }
            };
}
