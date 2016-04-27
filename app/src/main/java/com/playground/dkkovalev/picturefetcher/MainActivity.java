package com.playground.dkkovalev.picturefetcher;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;

import com.playground.dkkovalev.picturefetcher.Assets.CustomRecyclerAdapter;
import com.playground.dkkovalev.picturefetcher.Fragments.MainFragment;
import com.playground.dkkovalev.picturefetcher.Fragments.PagerFragment;
import com.playground.dkkovalev.picturefetcher.Model.FlickrPhotoObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    FragmentManager fragmentManager;
  /*
  TODO
  Почитать про проблемы AsyncTask и способы отмены загрузки (метод cancel()).
  Прочитать про AsyncTask подробнее.
  Ограничить кол-во потоков (очередь задач, удобнее использовать executor), добавить управление последовательностью (LIFO).
  Добавить кеширование на карту памяти
  Получить имя от url
  Генерация пути к изображению (передаю url -- получаю файл)
  */

    /*TODO 20.04.2016: Сделать нормальный поворот экрана.
    * TODO 21.04.2016: Реализация паттерна MVP (Model View Presenter)
    * TODO Посмотреть в PagerFragment почему не перелистывается после сворачивания*/

    private Fragment mainFragment;
    private Fragment pagerFragment;

    private static final String MAIN_FRAGMENT_TAG = "MAIN_FRAGMENT";
    private static final String PAGER_FRAGMENT_TAG = "PAGER_FRAGMENT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragmentManager = getSupportFragmentManager();

        createFragment();
/*
        if (savedInstanceState != null) {
            mainFragment = fragmentManager.findFragmentByTag(MAIN_FRAGMENT_TAG);
            pagerFragment = fragmentManager.findFragmentByTag(PAGER_FRAGMENT_TAG);
            if(pagerFragment == null){
                pagerFragment = new PagerFragment();
            }
        } else {
            createFragment();
        }*/
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void createFragment(){
        mainFragment = new MainFragment();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, mainFragment, MAIN_FRAGMENT_TAG);
        fragmentTransaction.commit();
    }
}
