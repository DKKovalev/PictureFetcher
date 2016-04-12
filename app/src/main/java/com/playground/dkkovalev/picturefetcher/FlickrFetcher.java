package com.playground.dkkovalev.picturefetcher;

import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by DKKovalev on 11.04.2016.
 */

public class FlickrFetcher {

    public static final String TAG = "FLICKR_FETCHER_LOGTAG";
    private static final String API_KEY = "3187a878484b4918f38130f97b35887b";

    public String getUrlInfo(String urlSpec) throws IOException {

        URL url = new URL(urlSpec);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        StringBuilder resultJSON = new StringBuilder();

        try {
            InputStream inputStream = new BufferedInputStream(connection.getInputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {

                resultJSON.append(line);
            }

        } finally {
            connection.disconnect();
        }

        return resultJSON.toString();

    }

    public String getUrlString(String urlSpec) throws IOException {

        return new String(getUrlInfo(urlSpec));
    }

    public GalleryItem[] fetchItems() {

        GalleryItem[] items = new GalleryItem[100];

        try {

            String url = Uri.parse("https://api.flickr.com/services/rest/")
                    .buildUpon()
                    .appendQueryParameter("method", "flickr.photos.getRecent")
                    .appendQueryParameter("api_key", API_KEY)
                    .appendQueryParameter("format", "json")
                    .appendQueryParameter("nojsoncallback", "1")
                    .appendQueryParameter("extras", "url_s")
                    .appendQueryParameter("per_page", "100")
                    .appendQueryParameter("page", "1")
                    .build().toString();

            String jsonString = getUrlString(url);
            Log.i(TAG, jsonString);

            JSONObject jsonObject = new JSONObject(jsonString);
            parseItems(items, jsonObject);

        } catch (IOException e) {

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return items;
    }

    private void parseItems(GalleryItem[] items, JSONObject jsonObject) throws IOException, JSONException {

        JSONObject photosJsonObject = jsonObject.getJSONObject("photos");
        JSONArray photoJsonArray = photosJsonObject.getJSONArray("photo");

        for (int i = 0; i < photoJsonArray.length(); i++) {
            JSONObject photoJsonObject = photoJsonArray.getJSONObject(i);

            GalleryItem item = new GalleryItem();
            item.setId(photoJsonObject.getString("id"));
            item.setCaption(photoJsonObject.getString("title"));

            if (!photoJsonObject.has("url_s")) {
                continue;
            }

            item.setUrl(photoJsonObject.getString("url_s"));

            if (i == items.length) {
                items = Arrays.copyOf(items, items.length + 500);
            }

            items[i] = item;
        }
    }
}
