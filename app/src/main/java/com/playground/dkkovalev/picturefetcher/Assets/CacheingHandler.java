package com.playground.dkkovalev.picturefetcher.Assets;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.util.LruCache;
import android.widget.ImageView;

import com.playground.dkkovalev.picturefetcher.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by d.kovalev on 14.04.2016.
 */
public class CacheingHandler {

    private LruCache<String, Bitmap> bitmapLruCache;

    public CacheingHandler() {
        setupLruCaching();
    }

    public void addBitmapToLruCache(String key, Bitmap value) {
        if (getBitmapFromMemoryCache(key) == null) {
            bitmapLruCache.put(key, value);
        }
    }

    public Bitmap loadBitmapFromLru(String key) {
        Bitmap bitmap = getBitmapFromMemoryCache(key);
        if (bitmap != null){
            return bitmap;
        }
        else return null;
    }

    public String saveTempFile(String url, Bitmap bitmap) throws IOException {
        String fileName = Uri.parse(url).getLastPathSegment();

        File dir = new File(Environment.getExternalStorageDirectory() + "/PictureFetcher/");
        if (!dir.exists()) {
            dir.mkdirs();
        }

        File file = new File(dir, fileName); //File.createTempFile(fileName, null, context.getCacheDir());

        FileOutputStream fileOutputStream = new FileOutputStream(file);

        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
        Log.i("Read", file.getName());

        return file.getAbsolutePath();
    }

    public Bitmap retrieveBitmapFromExternalStorage(String path) throws IOException {

        Bitmap bitmap = null;

        File pathToFile = new File(path);
        if (pathToFile.exists() && !pathToFile.isDirectory()) {
            bitmap = BitmapFactory.decodeFile(pathToFile.getPath());
        }
        return bitmap;
    }

    private Bitmap getBitmapFromMemoryCache(String key) {
        return bitmapLruCache.get(key);
    }

    private void setupLruCaching() {

        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        final int cacheSize = maxMemory / 8;

        bitmapLruCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getByteCount() / 1024;
            }
        };
    }
}
