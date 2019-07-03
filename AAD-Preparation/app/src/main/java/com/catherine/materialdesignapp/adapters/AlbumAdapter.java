package com.catherine.materialdesignapp.adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.catherine.materialdesignapp.R;
import com.catherine.materialdesignapp.jetpack.entities.Album;
import com.catherine.materialdesignapp.utils.PrefetchSubscriber;
import com.facebook.binaryresource.BinaryResource;
import com.facebook.binaryresource.FileBinaryResource;
import com.facebook.cache.common.CacheKey;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.cache.DefaultCacheKeyFactory;
import com.facebook.imagepipeline.core.DefaultExecutorSupplier;
import com.facebook.imagepipeline.core.ImagePipelineFactory;
import com.facebook.imagepipeline.request.ImageRequest;

import java.io.File;

public class AlbumAdapter extends PagedListAdapter<Album, AlbumAdapter.MainRvHolder> {
    private final String TAG = AlbumAdapter.class.getSimpleName();

    public AlbumAdapter() {
        super(DIFF_CALLBACK);
    }

    @NonNull
    @Override
    public MainRvHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        return new MainRvHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rv_album_item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final MainRvHolder mainRvHolder, final int position) {

        Album album = getItem(position);
        if (album != null)
            mainRvHolder.bindTo(album);
        else
            mainRvHolder.clear();

    }

    class MainRvHolder extends RecyclerView.ViewHolder {
        private TextView tv_title;
        private TextView tv_subtitle;
        private SimpleDraweeView sdv_photo;
        private Album album;
        private PrefetchSubscriber subscriber;

        MainRvHolder(View itemView) {
            super(itemView);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_subtitle = itemView.findViewById(R.id.tv_subtitle);
            sdv_photo = itemView.findViewById(R.id.sdv_photo);
            subscriber = new PrefetchSubscriber();
        }

        public Album getAlbum() {
            return album;
        }

        void bindTo(Album album) {
            this.album = album;
            if (!TextUtils.isEmpty(album.getImage())) {
                sdv_photo.setVisibility(View.VISIBLE);
                // show raw images
                Bitmap b = getBitmapFromCache(album.getImage());
                if (b != null) {
                    sdv_photo.setImageBitmap(b);
                } else {
                    Uri uri = Uri.parse(album.getImage());
                    sdv_photo.setImageURI(uri);
                    cacheItems(album.getImage());
                }
            } else
                sdv_photo.setVisibility(View.GONE);

            if (!TextUtils.isEmpty(album.getTitle())) {
                tv_title.setVisibility(View.VISIBLE);
                tv_title.setText(album.getTitle());
            } else
                tv_title.setVisibility(View.GONE);

            if (!TextUtils.isEmpty(album.getArtist())) {
                tv_subtitle.setVisibility(View.VISIBLE);
                //Enable TextView open url links
                tv_subtitle.setMovementMethod(LinkMovementMethod.getInstance());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    tv_subtitle.setText(Html.fromHtml(album.getArtist(),
                            Html.FROM_HTML_MODE_COMPACT));
                } else {
                    tv_subtitle.setText(Html.fromHtml(album.getArtist()));
                }
            } else {
                tv_subtitle.setVisibility(View.GONE);
            }
        }


        private void cacheItems(String url) {
            try {
                ImageRequest imageRequest = ImageRequest.fromUri(url);
                if (imageRequest == null)
                    return;
                CacheKey cacheKey = DefaultCacheKeyFactory.getInstance().getEncodedCacheKey(imageRequest, null);
                BinaryResource resource = ImagePipelineFactory.getInstance().getMainFileCache().getResource(cacheKey);
                if (resource == null || resource.size() == 0) {
                    DataSource<Void> ds = Fresco.getImagePipeline().prefetchToDiskCache(ImageRequest.fromUri(url), null);
                    ds.subscribe(subscriber, new DefaultExecutorSupplier(3).forBackgroundTasks());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        private Bitmap getBitmapFromCache(String url) {
            Bitmap bitmap = null;
            try {
                ImageRequest imageRequest = ImageRequest.fromUri(url);
                if (imageRequest == null)
                    return null;
                CacheKey cacheKey = DefaultCacheKeyFactory.getInstance().getEncodedCacheKey(imageRequest, null);
                BinaryResource resource = ImagePipelineFactory.getInstance().getMainFileCache().getResource(cacheKey);
                if (resource == null)
                    return null;
                File file = ((FileBinaryResource) resource).getFile();
                bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        void clear() {
            tv_title.setText("");
            tv_subtitle.setText("");
            sdv_photo.setImageURI("");
        }

    }

    private static final DiffUtil.ItemCallback<Album> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<Album>() {
                @Override
                public boolean areItemsTheSame(@NonNull Album oldItem, @NonNull Album newItem) {
                    return oldItem.compareTo(newItem) == 0;
                }

                @Override
                public boolean areContentsTheSame(@NonNull Album oldItem,
                                                  @NonNull Album newItem) {
                    return oldItem.getTitle().equals(newItem.getTitle());
                }
            };
}
