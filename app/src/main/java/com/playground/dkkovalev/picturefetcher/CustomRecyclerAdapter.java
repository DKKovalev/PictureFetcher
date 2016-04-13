package com.playground.dkkovalev.picturefetcher;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by d.kovalev on 12.04.2016.
 */

public class CustomRecyclerAdapter extends RecyclerView.Adapter<CustomRecyclerAdapter.CustomViewHolder> {

    private LruCache<String, Bitmap> bitmapLruCache;

    private ArrayList<GalleryItem> galleryItems;

    private Context context;

    public CustomRecyclerAdapter(ArrayList<GalleryItem> galleryItems, Context context) {
        super();
        this.galleryItems = galleryItems;
        this.context = context;
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

        new ImageLoader(holder.photoView).
                executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, galleryItem.getUrl());
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

    private class ImageLoader extends AsyncTask<String, Void, Bitmap> {

        private WeakReference<ImageView> cache;

        String savedUrl;

        public ImageLoader(ImageView photoView) {
            cache = new WeakReference<>(photoView);
        }

        @Override
        protected Bitmap doInBackground(String... params) {

            HttpURLConnection connection = null;
            Bitmap thumbnail = null;

            try {
                URL url = new URL(params[0]);

                savedUrl = url.toString();

                connection = (HttpURLConnection) url.openConnection();

                InputStream inputStream = new BufferedInputStream(connection.getInputStream());
                thumbnail = BitmapFactory.decodeStream(inputStream);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                connection.disconnect();
            }

            return thumbnail;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);

            if (cache != null) {

                ImageView imageView = cache.get();

                if (imageView != null) {
                    if (bitmap != null) {
                        if (imageView.getTag().toString().equals(savedUrl)) {

                            imageView.setImageBitmap(bitmap);
                        } else {

                            Drawable drawable = imageView.getContext().getResources().getDrawable(R.drawable.placeholder);
                            imageView.setImageDrawable(drawable);
                        }
                    }
                }
            }

            /*if (photoView.getTag().toString().equals(savedUrl)) {
                photoView.setImageBitmap(bitmap);
            }*/
        }
    }
}