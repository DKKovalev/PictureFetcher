package com.playground.dkkovalev.picturefetcher;

import android.os.AsyncTask;

import com.playground.dkkovalev.picturefetcher.Assets.CustomRecyclerAdapter;
import com.playground.dkkovalev.picturefetcher.Assets.FlickrFetcher;
import com.playground.dkkovalev.picturefetcher.Assets.ViewPagerAdapter;
import com.playground.dkkovalev.picturefetcher.Model.FlickrPhotoObject;

import java.util.ArrayList;

/**
 * Created by d.kovalev on 25.04.2016.
 */
public class Presenter implements MVPOperations.PresenterOperations {

    private CustomRecyclerAdapter.EndlessScrollListener endlessScrollListener;
    private ViewPagerAdapter.EndlessSwipe endlessSwipe;

    private MVPOperations.MainViewOperations mainViewOperations;
    private MVPOperations.PagerViewOperations pagerViewOperations;

    private boolean isConfigChanged;

    public Presenter() {

    }

    public void setMainViewOperations(MVPOperations.MainViewOperations mainViewOperations) {
        this.mainViewOperations = mainViewOperations;
    }

    public void setPagerViewOperations(MVPOperations.PagerViewOperations pagerViewOperations) {
        this.pagerViewOperations = pagerViewOperations;
    }

    @Override
    public void onConfigurationChanged(MVPOperations.MainViewOperations view) {

    }

    @Override
    public void onDestroy(boolean isConfigChanged) {
        this.isConfigChanged = isConfigChanged;
        if (!isConfigChanged) {
        }
    }

    @Override
    public void fetchItems(int page, String method, String tags) {
        new FlickrFetcherTask(method, tags).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, page);
    }

    @Override
    public void onItemsFetched(ArrayList<FlickrPhotoObject> flickrPhotoObjects) {
        if (mainViewOperations != null) {
            mainViewOperations.populateRecyclerView(flickrPhotoObjects);
        }
        if (pagerViewOperations != null) {
            pagerViewOperations.showViewPager(flickrPhotoObjects);
        }
    }

    public void setEndlessScrollListener(CustomRecyclerAdapter.EndlessScrollListener endlessScrollListener) {
        this.endlessScrollListener = endlessScrollListener;
    }

    public CustomRecyclerAdapter.EndlessScrollListener getEndlessScrollListener() {
        return endlessScrollListener;
    }

    public void setEndlessSwipe(ViewPagerAdapter.EndlessSwipe endlessSwipe) {
        this.endlessSwipe = endlessSwipe;
    }

    private class FlickrFetcherTask extends AsyncTask<Integer, Void, ArrayList<FlickrPhotoObject>> implements CustomRecyclerAdapter.EndlessScrollListener, ViewPagerAdapter.EndlessSwipe {

        private ArrayList<FlickrPhotoObject> flickrPhotoObjects;

        private String method;
        private String tags;

        public FlickrFetcherTask(String method, String tags) {
            setEndlessScrollListener(this);
            setEndlessSwipe(this);
            this.method = method;
            this.tags = tags;
        }

        @Override
        protected ArrayList<FlickrPhotoObject> doInBackground(Integer... params) {

            flickrPhotoObjects = new FlickrFetcher().fetchItems(params[0], method, tags);

            return flickrPhotoObjects;
        }

        @Override
        protected void onPostExecute(ArrayList<FlickrPhotoObject> flickrPhotoObjects) {
            super.onPostExecute(flickrPhotoObjects);
            onItemsFetched(flickrPhotoObjects);
        }

        @Override
        public boolean onLoadMore(int pos) {
            new MoreFetching().execute(pos);
            return false;
        }

        @Override
        public boolean onSwipe(int pos) {
            new MoreFetching().execute(pos);
            return false;
        }

        private class MoreFetching extends AsyncTask<Integer, Void, ArrayList<FlickrPhotoObject>> {

            @Override
            protected ArrayList<FlickrPhotoObject> doInBackground(Integer... params) {

                return new FlickrFetcher().fetchItems(params[0], method, tags);
            }

            @Override
            protected void onPostExecute(ArrayList<FlickrPhotoObject> galleryItems1) {
                super.onPostExecute(galleryItems1);

                flickrPhotoObjects.addAll(galleryItems1);
            }
        }
    }
}


