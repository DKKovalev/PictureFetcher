package com.playground.dkkovalev.picturefetcher;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by d.kovalev on 12.04.2016.
 */

public class CustomRecyclerAdapter extends RecyclerView.Adapter<CustomRecyclerAdapter.CustomViewHolder> {

    private GalleryItem[] galleryItems;

    private Context context;

    public CustomRecyclerAdapter(GalleryItem[] galleryItems, Context context) {
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

        GalleryItem galleryItem = galleryItems[position];

        new ImageLoader(holder.photoView).execute(galleryItem.getUrl());
    }

    @Override
    public int getItemCount() {
        return galleryItems.length;
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        private ImageView photoView;

        public CustomViewHolder(View itemView) {
            super(itemView);

            photoView = (ImageView) itemView.findViewById(R.id.iv_photo);
        }

        public void bindDrawable(Drawable drawable){
            photoView.setImageDrawable(drawable);
        }
    }

    private class ImageLoader extends AsyncTask<String, Void, Bitmap> {

        ImageView photoView;

        public ImageLoader(ImageView photoView) {
            this.photoView = photoView;
        }

        @Override
        protected Bitmap doInBackground(String... params) {

            HttpURLConnection connection = null;
            Bitmap thumbnail = null;

            try {
                URL url = new URL(params[0]);
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
            photoView.setImageBitmap(bitmap);
        }
    }
}
