package ru.ratanov.kinoman.presentation.view.detail;

import com.arellomobile.mvp.MvpView;

import java.util.List;

import ru.ratanov.kinoman.model.content.SameItem;

public interface SameView extends MvpView {
    void showProgress();
    void hideProgress();

    void setupAdapter(List<SameItem> items);

    void showAddingProgress();
    void showAddingResult(String message, boolean setupServer);
    void hideAddingProgress();
}
