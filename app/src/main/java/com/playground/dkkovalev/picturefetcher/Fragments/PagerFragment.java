package com.playground.dkkovalev.picturefetcher.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.playground.dkkovalev.picturefetcher.Assets.ViewPagerAdapter;
import com.playground.dkkovalev.picturefetcher.Model.FlickrPhotoObject;
import com.playground.dkkovalev.picturefetcher.R;

import java.util.ArrayList;

public class PagerFragment extends Fragment {
    private static final String SAVED_INT = "current_position";
    private ViewPager viewPager;

    private ArrayList<FlickrPhotoObject> photos;
    private int position;

    public PagerFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);

        photos = (ArrayList<FlickrPhotoObject>) getArguments().getSerializable("photos");

        position = getArguments().getInt("position");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_pager, container, false);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getActivity(), photos, position);
        viewPager = (ViewPager) view.findViewById(R.id.view);
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setCurrentItem(position);
        viewPagerAdapter.notifyDataSetChanged();

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if (viewPager != null) {
            outState.putInt(SAVED_INT, viewPager.getCurrentItem());
        }
    }
}
