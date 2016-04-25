package com.playground.dkkovalev.picturefetcher.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.playground.dkkovalev.picturefetcher.Assets.ViewPagerAdapter;
import com.playground.dkkovalev.picturefetcher.MVPOperations;
import com.playground.dkkovalev.picturefetcher.Model.FlickrPhotoObject;
import com.playground.dkkovalev.picturefetcher.Presenter;
import com.playground.dkkovalev.picturefetcher.PresenterCache;
import com.playground.dkkovalev.picturefetcher.PresenterFactory;
import com.playground.dkkovalev.picturefetcher.R;

import java.util.ArrayList;
import java.util.ListIterator;

public class PagerFragment extends Fragment implements MVPOperations.PagerViewOperations {
    private static final String SAVED_INT = "current_position";
    private static final String SAVED_FRAGMENT_TAG = "PAGER_FRAGMENT";
    private ViewPager viewPager;

    private ArrayList<FlickrPhotoObject> flickrPhotoObjects;
    private int position;

    private PresenterCache presenterCache = PresenterCache.getInstance();
    private boolean isConfigChanged;

    private Presenter presenter;

    public PagerFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        presenter = presenterCache.getPresenter(SAVED_FRAGMENT_TAG, presenterFactory);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_pager, container, false);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getActivity(), flickrPhotoObjects, position);
        viewPager = (ViewPager) view.findViewById(R.id.view);
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setCurrentItem(position);
        viewPagerAdapter.notifyDataSetChanged();

        return view;
    }

    @Override
    public void showViewPager(ArrayList<FlickrPhotoObject> flickrPhotoObjects) {

    }

    private PresenterFactory<Presenter> presenterFactory = new PresenterFactory<Presenter>() {
        @Override
        public Presenter createPresenter() {
            return new Presenter();
        }
    };

    private Presenter getPresenter() {
        if (presenter == null) {
            presenter = new Presenter();
        }
        return presenter;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
            //TODO Продумать логику показа изображений. Сравнить size и position

        getPresenter().setPagerViewOperations(this);
        ListIterator<FlickrPhotoObject> iterator = flickrPhotoObjects.listIterator();
        if (!iterator.hasNext()) {
            //TODO Дошли до конца, грузим новые. Отловить событие свайпа
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        presenter.setPagerViewOperations(null);
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
}
