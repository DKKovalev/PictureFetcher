package com.playground.dkkovalev.picturefetcher;

/**
 * Created by d.kovalev on 25.04.2016.
 */
public interface PresenterFactory<T extends Presenter> {
    T createPresenter();
}
