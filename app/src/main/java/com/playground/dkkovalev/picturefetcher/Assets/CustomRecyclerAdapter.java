package com.playground.dkkovalev.picturefetcher.Assets;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.playground.dkkovalev.picturefetcher.Model.GalleryItem;
import com.playground.dkkovalev.picturefetcher.R;
import com.playground.dkkovalev.picturefetcher.Tasks.ImageLoaderTask;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by d.kovalev on 12.04.2016.
 */

public class CustomRecyclerAdapter extends RecyclerView.Adapter<CustomRecyclerAdapter.CustomViewHolder> {

    private LruCache<String, Bitmap> bitmapLruCache;

    private ArrayList<GalleryItem> galleryItems;

    private Context context;

    private CacheingHandler cacheingHandler;

    private String path;

    public CustomRecyclerAdapter(ArrayList<GalleryItem> galleryItems, Context context) {
        super();
        this.galleryItems = galleryItems;
        this.context = context;
        cacheingHandler = new CacheingHandler();
        path = Environment.getExternalStorageDirectory().getPath() + "PictureFetcher/25819066324_424b58b7e1_m.jpg";
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, null);
        CustomViewHolder customViewHolder = new CustomViewHolder(view);
        return customViewHolder;
    }

    @Override
    public void onBindViewHolder(final CustomViewHolder holder, int position) {

        //TODO Main fetching goes there

        GalleryItem galleryItem = galleryItems.get(position);
        holder.photoView.setTag(galleryItem.getUrl());

        holder.photoView.setImageResource(R.drawable.placeholder);

        try {

            Bitmap bitmap = cacheingHandler.retrieveBitmapFromExternalStorage(path + galleryItem.getUrl(), holder.photoView);

            if (bitmap != null) {
                holder.photoView.setImageBitmap(bitmap);
            } else {
                new ImageLoaderTask(holder.photoView, context).
                        executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, galleryItem.getUrl());
            }
        } catch (
                IOException e
                )

        {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return galleryItems.size();
    }


    public class CustomViewHolder extends RecyclerView.ViewHolder {

        private ImageView photoView;

        public CustomViewHolder(View itemView) {
            super(itemView);

            photoView = (ImageView) itemView.findViewById(R.id.iv_photo);
        }
    }
}