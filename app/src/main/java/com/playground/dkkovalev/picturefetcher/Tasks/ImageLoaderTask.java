package com.playground.dkkovalev.picturefetcher.Tasks;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import com.playground.dkkovalev.picturefetcher.Assets.CacheingHandler;
import com.playground.dkkovalev.picturefetcher.R;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by d.kovalev on 14.04.2016.
 */
public class ImageLoaderTask extends AsyncTask<String, Void, Bitmap> {

    private WeakReference<ImageView> imageViewWeakReference;
    private String savedUrl;

    private CacheingHandler cacheingHandler;

    public ImageLoaderTask(ImageView photoView) {
        imageViewWeakReference = new WeakReference<>(photoView);
        cacheingHandler = new CacheingHandler();
    }

    @Override
    protected Bitmap doInBackground(String... params) {

        HttpURLConnection connection = null;
        Bitmap thumbnail = null;

        try {
            URL url = new URL(params[0]);

            savedUrl = params[0];

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

        Log.d("Downloaded bitmap ", bitmap.toString());

        cacheingHandler.addBitmapToLruCache(savedUrl, bitmap);
        if (imageViewWeakReference != null) {

            ImageView imageView = imageViewWeakReference.get();

            if (imageView != null) {
                if (bitmap != null) {
                    if (imageView.getTag().toString().equals(savedUrl)) {
                        imageView.setImageBitmap(bitmap);
                    } else {
                        imageView.setImageResource(R.drawable.placeholder);
                    }
                }
            }

        /*try {
            cacheingHandler.saveTempFile(savedUrl, bitmap);

            if (imageViewWeakReference != null) {

                ImageView imageView = imageViewWeakReference.get();

                if (imageView != null) {
                    if (bitmap != null) {
                        if (imageView.getTag().toString().equals(savedUrl)) {
                            imageView.setImageBitmap(bitmap);
                        } else {
                            imageView.setImageResource(R.drawable.placeholder);
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        }
    }
}