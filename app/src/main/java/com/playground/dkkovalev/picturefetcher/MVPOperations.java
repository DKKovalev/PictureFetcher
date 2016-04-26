package com.playground.dkkovalev.picturefetcher;


import android.support.v7.widget.RecyclerView;

import com.playground.dkkovalev.picturefetcher.Assets.CustomRecyclerAdapter;
import com.playground.dkkovalev.picturefetcher.Model.FlickrPhotoObject;

import java.util.ArrayList;

/**
 * Created by d.kovalev on 25.04.2016.
 */
public interface MVPOperations {
    interface MainViewOperations{
        void populateRecyclerView(ArrayList<FlickrPhotoObject> flickrPhotoObjects);
        void onItemClick(CustomRecyclerAdapter.OnItemClickedListener onItemClickedListener);
    }

    interface PagerViewOperations{
        void showViewPager(ArrayList<FlickrPhotoObject> flickrPhotoObjects);
    }

    interface PresenterOperations{
        void onConfigurationChanged(MainViewOperations viewOperations);
        void onDestroy(boolean isConfigChanged);
        void fetchItems(int page, String method, String tags);
        void onItemsFetched(ArrayList<FlickrPhotoObject> flickrPhotoObjects);

    }
}
