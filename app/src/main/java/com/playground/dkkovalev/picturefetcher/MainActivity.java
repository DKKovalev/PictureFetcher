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

import com.playground.dkkovalev.picturefetcher.Tasks.FlickrFetcherTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

  /*
  TODO Почитать про проблемы AsyncTask и способы отмены загрузки (метод cancel()).
  Прочитать про AsyncTask подробнее.
  Ограничить кол-во потоков (очередь задач, удобнее использовать executor), добавить управление последовательностью (LIFO).
  Добавить кеширование на карту памяти
  Получить имя от url
  Генерация пути к изображению (передаю url -- получаю файл)
  */

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.rv_container);

        new FlickrFetcherTask(MainActivity.this, recyclerView).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, 1);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
