package com.playground.dkkovalev.picturefetcher.Assets;

import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;

import com.playground.dkkovalev.picturefetcher.Model.FlickrPhotoObject;
import com.playground.dkkovalev.picturefetcher.R;
import com.playground.dkkovalev.picturefetcher.Tasks.ImageLoaderTask;

import java.util.ArrayList;

/**
 * Created by d.kovalev on 12.04.2016.
 */

public class CustomRecyclerAdapter extends RecyclerView.Adapter<CustomRecyclerAdapter.CustomViewHolder> {

    private static final String LOGTAG = "CustomAdapter TAG";
    private static final int THRESHOLD = 5;
    private ArrayList<FlickrPhotoObject> galleryItems;
    private CacheingHandler cacheingHandler;

    private OnItemClickedListener onItemClickListener;

    private EndlessScrollListener endlessScrollListener;

    public void setEndlessScrollListener(EndlessScrollListener endlessScrollListener) {
        this.endlessScrollListener = endlessScrollListener;
    }

    public void setGalleryItems(ArrayList<FlickrPhotoObject> galleryItems) {
        this.galleryItems = galleryItems;
    }

    public CustomRecyclerAdapter(Context context, ArrayList<FlickrPhotoObject> galleryItems) {
        super();

        this.galleryItems = galleryItems;
        cacheingHandler = new CacheingHandler();
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

        FlickrPhotoObject galleryItem = galleryItems.get(position);
        holder.photoView.setTag(galleryItem.getUrl());

        holder.photoView.setImageResource(R.drawable.placeholder);

        Bitmap photo = cacheingHandler.loadBitmapFromLru(galleryItems.get(position).getUrl());

        if (photo != null) {
            holder.photoView.setImageBitmap(photo);
        } else {
            new ImageLoaderTask(holder.photoView).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, galleryItem.getUrl());
        }

        if (position == getItemCount() - THRESHOLD) {
            if (endlessScrollListener != null) {
                endlessScrollListener.onLoadMore(position);
            }
        }
    }

    @Override
    public int getItemCount() {
        return galleryItems.size();
    }

    public void setOnItemClickListener(OnItemClickedListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView photoView;

        public CustomViewHolder(View itemView) {
            super(itemView);

            photoView = (ImageView) itemView.findViewById(R.id.iv_photo);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            if (onItemClickListener != null) {
                onItemClickListener.onClick(v, getAdapterPosition());
            }

        }
    }

    public interface OnItemClickedListener {
        void onClick(View view, int pos);
    }

    public interface EndlessScrollListener {
        boolean onLoadMore(int pos);
    }
}
