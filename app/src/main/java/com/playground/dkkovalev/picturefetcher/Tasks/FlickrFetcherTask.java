package com.playground.dkkovalev.picturefetcher.Tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.playground.dkkovalev.picturefetcher.Assets.ContiniousScroller;
import com.playground.dkkovalev.picturefetcher.Assets.CustomRecyclerAdapter;
import com.playground.dkkovalev.picturefetcher.Assets.FlickrFetcher;
import com.playground.dkkovalev.picturefetcher.Model.GalleryItem;

import java.util.ArrayList;

/**
 * Created by d.kovalev on 14.04.2016.
 */
public class FlickrFetcherTask extends AsyncTask<Integer, Void, ArrayList<GalleryItem>> {

    private ArrayList<GalleryItem> galleryItems;
    private Context context;
    private LinearLayoutManager linearLayoutManager;
    private RecyclerView recyclerView;

    private CustomRecyclerAdapter customRecyclerAdapter;

    public FlickrFetcherTask(Context context, RecyclerView recyclerView) {
        this.context = context;
        this.recyclerView = recyclerView;

        linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    @Override
    protected ArrayList<GalleryItem> doInBackground(Integer... params) {

        galleryItems = new FlickrFetcher().fetchItems(params[0]);

        return galleryItems;
    }

    @Override
    protected void onPostExecute(final ArrayList<GalleryItem> galleryItems) {
        super.onPostExecute(galleryItems);

        customRecyclerAdapter = new CustomRecyclerAdapter(galleryItems, context);
        recyclerView.setAdapter(customRecyclerAdapter);

        recyclerView.addOnScrollListener(new ContiniousScroller(linearLayoutManager) {
            @Override
            public void load(int page) {
                new MoreFetching().execute(page);
            }
        });
    }

    private class MoreFetching extends AsyncTask<Integer, Void, ArrayList<GalleryItem>> {

        @Override
        protected ArrayList<GalleryItem> doInBackground(Integer... params) {

            return new FlickrFetcher().fetchItems(params[0]);
        }

        @Override
        protected void onPostExecute(ArrayList<GalleryItem> galleryItems1) {
            super.onPostExecute(galleryItems1);

            galleryItems.addAll(galleryItems1);
            customRecyclerAdapter.notifyItemRangeChanged(customRecyclerAdapter.getItemCount(), galleryItems.size() - 1);
        }
    }
}