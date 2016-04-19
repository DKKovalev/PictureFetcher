package com.playground.dkkovalev.picturefetcher.Fragments;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.playground.dkkovalev.picturefetcher.Assets.CustomRecyclerAdapter;
import com.playground.dkkovalev.picturefetcher.Model.FlickrPhotoObject;
import com.playground.dkkovalev.picturefetcher.R;
import com.playground.dkkovalev.picturefetcher.Tasks.FlickrFetcherTask;

import java.util.ArrayList;

public class MainFragment extends Fragment implements CustomRecyclerAdapter.OnItemClickedListener {

    private RecyclerView recyclerView;
    private EditText queryText;
    private Button commitSearchBtn;

    private LinearLayoutManager linearLayoutManager;

    private ArrayList<FlickrPhotoObject> flickrPhotoObjects;

    public MainFragment() {
    }


    public void setFlickrPhotoObjects(ArrayList<FlickrPhotoObject> flickrPhotoObjects) {
        this.flickrPhotoObjects = flickrPhotoObjects;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main, container, false);
        setupUI(view);

        final FlickrFetcherTask flickrFetcherTask = new FlickrFetcherTask(
                getActivity()
                , recyclerView
                , linearLayoutManager
                , "flickr.photos.getRecent"
                , ""
                , MainFragment.this);
        flickrFetcherTask.setAsyncCallback(new FlickrFetcherTask.AsyncCallback() {
            @Override
            public void onDownloadComplete(ArrayList<FlickrPhotoObject> flickrPhotoObjects) {
                setFlickrPhotoObjects(flickrPhotoObjects);
            }
        });
        flickrFetcherTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, 1);
        return view;
    }

    @Override
    public void onClick(View view, int pos) {

        PagerFragment pagerFragment = new PagerFragment();

        Bundle bundle = new Bundle();
        bundle.putSerializable("photos", flickrPhotoObjects);
        bundle.putInt("position", pos);
        pagerFragment.setArguments(bundle);

        this.getFragmentManager().beginTransaction().replace(R.id.fragment_container, pagerFragment).addToBackStack(null).commit();

        Log.i("LOIJF", String.valueOf(pos));
    }

    private void setupUI(View view) {

        recyclerView = (RecyclerView) view.findViewById(R.id.rv_container);
        queryText = (EditText) view.findViewById(R.id.et_query);
        commitSearchBtn = (Button) view.findViewById(R.id.btn_search);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);

        commitSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final FlickrFetcherTask flickrFetcherTask = new FlickrFetcherTask(
                        getActivity()
                        , recyclerView
                        , linearLayoutManager
                        , "flickr.photos.search"
                        , queryText.getText().toString()
                        , MainFragment.this);
                flickrFetcherTask.setAsyncCallback(new FlickrFetcherTask.AsyncCallback() {
                    @Override
                    public void onDownloadComplete(ArrayList<FlickrPhotoObject> flickrPhotoObjects) {
                        setFlickrPhotoObjects(flickrPhotoObjects);
                    }
                });
                flickrFetcherTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, 1);
            }
        });
    }
}
