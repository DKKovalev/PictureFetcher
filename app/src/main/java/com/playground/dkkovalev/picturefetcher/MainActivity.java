package com.playground.dkkovalev.picturefetcher;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
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
import android.widget.Button;
import android.widget.EditText;

import com.playground.dkkovalev.picturefetcher.Assets.CustomRecyclerAdapter;
import com.playground.dkkovalev.picturefetcher.Tasks.FlickrFetcherTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

  /*
  TODO
  Почитать про проблемы AsyncTask и способы отмены загрузки (метод cancel()).
  Прочитать про AsyncTask подробнее.
  Ограничить кол-во потоков (очередь задач, удобнее использовать executor), добавить управление последовательностью (LIFO).
  Добавить кеширование на карту памяти
  Получить имя от url
  Генерация пути к изображению (передаю url -- получаю файл)
  */

    private RecyclerView recyclerView;
    private EditText queryText;
    private Button commitSearchBtn;

    private LinearLayoutManager linearLayoutManager;
    private int pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupUI();

        new FlickrFetcherTask(MainActivity.this, recyclerView, linearLayoutManager, "flickr.photos.getRecent", "").executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, 1);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void setupUI() {
        recyclerView = (RecyclerView) findViewById(R.id.rv_container);

        queryText = (EditText) findViewById(R.id.et_query);
        commitSearchBtn = (Button) findViewById(R.id.btn_search);

        linearLayoutManager = new LinearLayoutManager(MainActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);

        commitSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new FlickrFetcherTask(MainActivity.this, recyclerView, linearLayoutManager, "flickr.photos.search", queryText.getText().toString()).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, 1);
            }
        });
    }
}
