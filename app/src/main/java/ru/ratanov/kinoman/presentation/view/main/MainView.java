package ru.ratanov.kinoman.presentation.view.main;

import com.arellomobile.mvp.MvpView;

import java.util.List;

import ru.ratanov.kinoman.model.content.TopItem;

public interface MainView extends MvpView {
    void setupAdapter(List<TopItem> items);
    void notifyFilmBlocked();
}
