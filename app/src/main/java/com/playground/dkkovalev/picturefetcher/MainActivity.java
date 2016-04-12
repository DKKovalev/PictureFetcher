package com.playground.dkkovalev.picturefetcher;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CustomRecyclerAdapter customRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this);
        recyclerView = (RecyclerView) findViewById(R.id.rv_container);
        recyclerView.setLayoutManager(linearLayoutManager);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new FlickrFetcherTask().execute();
            }
        });
    }

    private class FlickrFetcherTask extends AsyncTask<Void, Void, GalleryItem[]> {

        @Override
        protected GalleryItem[] doInBackground(Void... params) {

            return new FlickrFetcher().fetchItems();
        }

        @Override
        protected void onPostExecute(GalleryItem[] galleryItems) {
            super.onPostExecute(galleryItems);

            customRecyclerAdapter = new CustomRecyclerAdapter(galleryItems, MainActivity.this);
            recyclerView.setAdapter(customRecyclerAdapter);
            customRecyclerAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
