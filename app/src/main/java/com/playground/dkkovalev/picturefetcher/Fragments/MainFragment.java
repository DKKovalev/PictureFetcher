package com.playground.dkkovalev.picturefetcher.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.playground.dkkovalev.picturefetcher.Assets.CustomRecyclerAdapter;
import com.playground.dkkovalev.picturefetcher.MVPOperations;
import com.playground.dkkovalev.picturefetcher.Model.FlickrPhotoObject;
import com.playground.dkkovalev.picturefetcher.Presenter;
import com.playground.dkkovalev.picturefetcher.PresenterCache;
import com.playground.dkkovalev.picturefetcher.PresenterFactory;
import com.playground.dkkovalev.picturefetcher.R;

import java.util.ArrayList;

public class MainFragment extends Fragment implements MVPOperations.MainViewOperations {

    private static final String SAVED_LIST_KEY = "photos";
    private static final String SAVED_FRAGMENT_TAG = "MAIN_FRAGMENT";
    private static final String PAGER_FRAGMENT_TAG = "PAGER_FRAGMENT";

    private PresenterCache presenterCache = PresenterCache.getInstance();
    private boolean isConfigChanged;

    private Presenter presenter;
    private CustomRecyclerAdapter customRecyclerAdapter;
    private ArrayList<FlickrPhotoObject> flickrPhotoObjects = new ArrayList<>();
    private LinearLayoutManager linearLayoutManager;

    private RecyclerView recyclerView;
    private EditText queryText;
    private Button commitSearchButton;

    public MainFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = presenterCache.getPresenter(SAVED_FRAGMENT_TAG, presenterFactory);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main, container, false);
        setupUI(view);

        return view;
    }

    private void setupUI(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.rv_container);

        linearLayoutManager = new LinearLayoutManager(getActivity());
        customRecyclerAdapter = new CustomRecyclerAdapter(flickrPhotoObjects);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(customRecyclerAdapter);

        queryText = (EditText) view.findViewById(R.id.et_query);
        commitSearchButton = (Button) view.findViewById(R.id.btn_search);
        commitSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!queryText.getText().toString().isEmpty()) {
                    presenter.fetchItems(0, "flickr.photos.search", queryText.getText().toString());
                }
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void populateRecyclerView(ArrayList<FlickrPhotoObject> flickrPhotoObjects) {

        customRecyclerAdapter.setFlickrPhotoObjects(flickrPhotoObjects);
        customRecyclerAdapter.notifyDataSetChanged();

        setFlickrPhotoObjects(flickrPhotoObjects);
    }

    @Override
    public void onItemClick(CustomRecyclerAdapter.OnItemClickedListener onItemClickedListener) {
        customRecyclerAdapter.setOnItemClickListener(onItemClickedListener);
    }

    private Presenter getPresenter() {
        if (presenter == null) {
            presenter = new Presenter();
        }
        return presenter;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getPresenter().setMainViewOperations(this);

        presenter.fetchItems(0, "flickr.photos.getRecent", null);
        customRecyclerAdapter.setEndlessScrollListener(presenter.getEndlessScrollListener());

        onItemClick(new CustomRecyclerAdapter.OnItemClickedListener() {
            @Override
            public void onClick(View view, int pos) {
                PagerFragment pagerFragment = new PagerFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("photos", flickrPhotoObjects);
                bundle.putInt("position", pos);
                pagerFragment.setArguments(bundle);
                MainFragment.this.getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, pagerFragment, PAGER_FRAGMENT_TAG)
                        .addToBackStack(PAGER_FRAGMENT_TAG)
                        .commit();
            }
        });

        //customRecyclerAdapter.setOnItemClickListener(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        presenter.setMainViewOperations(null);
        presenter = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        isConfigChanged = false;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        isConfigChanged = true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (!isConfigChanged) {
            presenterCache.removePresenter(SAVED_FRAGMENT_TAG);
        }
    }

    private PresenterFactory<Presenter> presenterFactory = new PresenterFactory<Presenter>() {
        @Override
        public Presenter createPresenter() {
            return new Presenter();
        }
    };

    private void setFlickrPhotoObjects(ArrayList<FlickrPhotoObject> flickrPhotoObjects) {
        this.flickrPhotoObjects = flickrPhotoObjects;
    }
}
