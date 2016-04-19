package com.playground.dkkovalev.picturefetcher.Tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.util.Log;

import com.playground.dkkovalev.picturefetcher.Assets.ContiniousScroller;
import com.playground.dkkovalev.picturefetcher.Assets.CustomRecyclerAdapter;
import com.playground.dkkovalev.picturefetcher.Assets.FlickrFetcher;
import com.playground.dkkovalev.picturefetcher.Fragments.MainFragment;
import com.playground.dkkovalev.picturefetcher.MainActivity;
import com.playground.dkkovalev.picturefetcher.Model.FlickrPhotoObject;


import java.util.ArrayList;

/**
 * Created by d.kovalev on 14.04.2016.
 */
public class FlickrFetcherTask extends AsyncTask<Integer, Void, ArrayList<FlickrPhotoObject>> {

    private ArrayList<FlickrPhotoObject> galleryItems;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private Context context;

    private CustomRecyclerAdapter customRecyclerAdapter;

    private String method;
    private String tags;

    private AsyncCallback asyncCallback;

    private CustomRecyclerAdapter.OnItemClickedListener onItemClickedListener;

    public FlickrFetcherTask(Context context, RecyclerView recyclerView, LinearLayoutManager linearLayoutManager, String method, String tags, CustomRecyclerAdapter.OnItemClickedListener onItemClickedListener) {
        this.context = context;
        this.recyclerView = recyclerView;
        this.method = method;
        this.tags = tags;
        this.linearLayoutManager = linearLayoutManager;
        this.onItemClickedListener = onItemClickedListener;
    }

    public void setAsyncCallback(AsyncCallback asyncCallback) {
        this.asyncCallback = asyncCallback;
    }

    @Override
    protected ArrayList<FlickrPhotoObject> doInBackground(Integer... params) {

        galleryItems = new FlickrFetcher().fetchItems(params[0], method, tags);

        return galleryItems;
    }

    @Override
    protected void onPostExecute(ArrayList<FlickrPhotoObject> galleryItems) {
        super.onPostExecute(galleryItems);



        customRecyclerAdapter = new CustomRecyclerAdapter(context, galleryItems);

        asyncCallback.onDownloadComplete(galleryItems);

        recyclerView.setAdapter(customRecyclerAdapter);

        customRecyclerAdapter.setOnItemClickListener(onItemClickedListener);

        customRecyclerAdapter.notifyDataSetChanged();

        recyclerView.addOnScrollListener(new ContiniousScroller(linearLayoutManager) {
            @Override
            public void load(int page) {
                new MoreFetching().execute(page);
            }
        });
    }

    private class MoreFetching extends AsyncTask<Integer, Void, ArrayList<FlickrPhotoObject>> {

        @Override
        protected ArrayList<FlickrPhotoObject> doInBackground(Integer... params) {

            return new FlickrFetcher().fetchItems(params[0], method, tags);
        }

        @Override
        protected void onPostExecute(ArrayList<FlickrPhotoObject> galleryItems1) {
            super.onPostExecute(galleryItems1);

            galleryItems.addAll(galleryItems1);
            customRecyclerAdapter.notifyItemRangeChanged(customRecyclerAdapter.getItemCount(), galleryItems.size() - 1);
        }
    }

    public interface AsyncCallback{
        void onDownloadComplete(ArrayList<FlickrPhotoObject> flickrPhotoObjects);
    }
}