package com.playground.dkkovalev.picturefetcher;

import android.support.v4.util.SimpleArrayMap;

/**
 * Created by d.kovalev on 25.04.2016.
 */
public class PresenterCache {
    private static PresenterCache instance = null;
    private SimpleArrayMap<String, Presenter> presenterMap;

    private PresenterCache() {

    }

    public static PresenterCache getInstance() {
        if (instance == null) {
            instance = new PresenterCache();
        }

        return instance;
    }

    public final <T extends Presenter> T getPresenter(String key, PresenterFactory<T> presenterFactory) {
        if (presenterMap == null) {
            presenterMap = new SimpleArrayMap<>();
        }

        T t = null;
        try {
            t = (T) presenterMap.get(key);
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
        if (t == null) {
            presenterFactory.createPresenter();
            presenterMap.put(key, t);
        }

        return t;
    }

    public final void removePresenter(String key) {
        if (presenterMap != null) {
            presenterMap.remove(key);
        }
    }
}
