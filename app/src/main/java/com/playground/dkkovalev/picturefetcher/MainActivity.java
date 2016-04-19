package com.playground.dkkovalev.picturefetcher;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

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

    /*TODO 20.04.2016: Сделать нормальный поворот экрана.*/


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        Fragment mainFragment = new MainFragment();
        fragmentTransaction.add(R.id.fragment_container, mainFragment);
        fragmentTransaction.commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (fragmentManager.getBackStackEntryCount() != 0) {
            fragmentManager.popBackStack();
        } else {
            super.onBackPressed();
        }

    }
}
