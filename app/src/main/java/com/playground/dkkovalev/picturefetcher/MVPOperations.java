package com.playground.dkkovalev.picturefetcher;


import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.widget.ArrayAdapter;

import com.playground.dkkovalev.picturefetcher.Assets.CustomRecyclerAdapter;
import com.playground.dkkovalev.picturefetcher.Assets.ViewPagerAdapter;
import com.playground.dkkovalev.picturefetcher.Model.FlickrPhotoObject;

import java.util.ArrayList;

/**
 * Created by d.kovalev on 25.04.2016.
 */
public interface MVPOperations {

    interface ViewOperations{
        void setData(ArrayList<FlickrPhotoObject> flickrPhotoObjects);
        void onItemClick(CustomRecyclerAdapter.OnItemClickedListener onItemClickedListener);
        void notifyData(ArrayList<FlickrPhotoObject> list);
    }

    interface PresenterOperations{
        void fetchItems(Fragment fragment, Context context, int page, String method, String tags);
        void onItemsFetched(ArrayList<FlickrPhotoObject> flickrPhotoObjects);
    }
}
