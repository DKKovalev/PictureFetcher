package com.playground.dkkovalev.picturefetcher.Assets;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.playground.dkkovalev.picturefetcher.Model.FlickrPhotoObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by d.kovalev on 19.04.2016.
 */
public class ViewPagerAdapter extends PagerAdapter {

    private static final int THRESHOLD = 13;
    private ArrayList<FlickrPhotoObject> flickrPhotoObjects;
    private Context context;

    private EndlessSwipeListener endlessSwipeListener;


    public ViewPagerAdapter(Context context, ArrayList<FlickrPhotoObject> flickrPhotoObjects) {
        this.context = context;
        this.flickrPhotoObjects = flickrPhotoObjects;
    }



    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        ImageView imageView = new ImageView(context);
        new ImageLoaderTask1(imageView).execute(flickrPhotoObjects.get(position).getUrl());

        container.addView(imageView);

        if (position == flickrPhotoObjects.size() - THRESHOLD) {
            if (endlessSwipeListener != null) {
                endlessSwipeListener.onLoadMore(position);
            }
        }

        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View)object);
    }

    @Override
    public int getCount() {
        return flickrPhotoObjects.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {

        return view == object;
    }

    public void setFlickrPhotoObjects(ArrayList<FlickrPhotoObject> flickrPhotoObjects) {
        this.flickrPhotoObjects = flickrPhotoObjects;
    }

    public void setEndlessSwipeListener(EndlessSwipeListener endlessSwipeListener) {
        this.endlessSwipeListener = endlessSwipeListener;
    }

    public class ImageLoaderTask1 extends AsyncTask<String, Void, Bitmap> {

        ImageView imageView;

        public ImageLoaderTask1(ImageView photoView) {
            this.imageView = photoView;
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

            imageView.setImageBitmap(bitmap);
        }
    }

    public interface EndlessSwipeListener {
        boolean onLoadMore(int pos);
    }
}
