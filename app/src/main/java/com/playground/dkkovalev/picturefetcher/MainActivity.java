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

  /*
  TODO Почитать про проблемы AsyncTask и способы отмены загрузки (метод cancel()).
  Прочитать про AsyncTask подробнее.
  Ограничить кол-во потоков (очередь задач, удобнее использовать executor), добавить управление последовательностью (LIFO).
  Добавить кеширование на карту памяти
  */

    private RecyclerView recyclerView;
    private CustomRecyclerAdapter customRecyclerAdapter;

    private ContiniousScroller continiousScroller;

    LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        linearLayoutManager = new LinearLayoutManager(MainActivity.this);
        recyclerView = (RecyclerView) findViewById(R.id.rv_container);
        recyclerView.setLayoutManager(linearLayoutManager);

        new FlickrFetcherTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, 1);
    }

    private class FlickrFetcherTask extends AsyncTask<Integer, Void, ArrayList<GalleryItem>> {

        public ArrayList<GalleryItem> galleryItems;

        @Override
        protected ArrayList<GalleryItem> doInBackground(Integer... params) {

            galleryItems = new FlickrFetcher().fetchItems(params[0]);

            return galleryItems;
        }

        @Override
        protected void onPostExecute(final ArrayList<GalleryItem> galleryItems) {
            super.onPostExecute(galleryItems);

            customRecyclerAdapter = new CustomRecyclerAdapter(galleryItems, MainActivity.this);
            recyclerView.setAdapter(customRecyclerAdapter);

            recyclerView.addOnScrollListener(new ContiniousScroller(linearLayoutManager) {
                @Override
                public void load(int page) {
                    new MoreFetching().execute(page);
                }
            });
        }

        private class MoreFetching extends AsyncTask<Integer, Void, ArrayList<GalleryItem>> {

            @Override
            protected ArrayList<GalleryItem> doInBackground(Integer... params) {

                return new FlickrFetcher().fetchItems(params[0]);
            }

            @Override
            protected void onPostExecute(ArrayList<GalleryItem> galleryItems1) {
                super.onPostExecute(galleryItems1);

                galleryItems.addAll(galleryItems1);
                customRecyclerAdapter.notifyItemRangeChanged(customRecyclerAdapter.getItemCount(), galleryItems.size() - 1);
            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
