package com.playground.dkkovalev.picturefetcher;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;

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
    private ViewPagerAdapter.EndlessSwipeListener endlessSwipeListener;

    private MVPOperations.ViewOperations mainViewOperations;

    public Presenter() {

    }
    public void setMainViewOperations(MVPOperations.ViewOperations mainViewOperations) {
        this.mainViewOperations = mainViewOperations;
    }

    @Override
    public void fetchItems(Fragment fragment, Context context, int page, String method, String tags) {
        new FlickrFetcherTask(fragment, method, tags).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, page);
    }


    @Override
    public void onItemsFetched(ArrayList<FlickrPhotoObject> flickrPhotoObjects) {
        if (mainViewOperations != null) {
            mainViewOperations.setData(flickrPhotoObjects);
            //mainViewOperations.setData(flickrPhotoObjects);
        }
    }

    public void setEndlessScrollListener(CustomRecyclerAdapter.EndlessScrollListener endlessScrollListener) {
        this.endlessScrollListener = endlessScrollListener;
    }

    public CustomRecyclerAdapter.EndlessScrollListener getEndlessScrollListener() {
        return endlessScrollListener;
    }

    public ViewPagerAdapter.EndlessSwipeListener getEndlessSwipeListener() {
        return endlessSwipeListener;
    }

    public void setEndlessSwipeListener(ViewPagerAdapter.EndlessSwipeListener endlessSwipeListener) {
        this.endlessSwipeListener = endlessSwipeListener;
    }

    private class FlickrFetcherTask extends AsyncTask<Integer, Void, ArrayList<FlickrPhotoObject>> implements CustomRecyclerAdapter.EndlessScrollListener, ViewPagerAdapter.EndlessSwipeListener {

        private ArrayList<FlickrPhotoObject> flickrPhotoObjects;

        private String method;
        private String tags;
        private Fragment fragment;

        public FlickrFetcherTask(Fragment fragment, String method, String tags) {
            setEndlessScrollListener(this);
            setEndlessSwipeListener(this);
            this.method = method;
            this.tags = tags;
            this.fragment = fragment;
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

        private class MoreFetching extends AsyncTask<Integer, Void, ArrayList<FlickrPhotoObject>> {

            DialogFragment dialogFragment;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                dialogFragment = CustomAlertDialogFragment.newInstance("Loading");
                dialogFragment.show(fragment.getFragmentManager(), "Alert");
            }

            @Override
            protected ArrayList<FlickrPhotoObject> doInBackground(Integer... params) {

                return new FlickrFetcher().fetchItems(params[0], method, tags);
            }

            @Override
            protected void onPostExecute(ArrayList<FlickrPhotoObject> galleryItems1) {
                super.onPostExecute(galleryItems1);

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dialogFragment.dismiss();
                    }
                }, 1000);

                flickrPhotoObjects.addAll(galleryItems1);

                if (mainViewOperations != null)
                    mainViewOperations.notifyData(flickrPhotoObjects);
            }
        }
    }
}


