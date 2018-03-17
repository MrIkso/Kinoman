package ru.ratanov.kinoman.presentation.view.search;

import com.arellomobile.mvp.MvpView;

import java.util.List;

import ru.ratanov.kinoman.model.content.ISearchItem;
import ru.ratanov.kinoman.model.content.SearchItem;

public interface SearchView extends MvpView {
    void showProgress();
    void hideProgress();
    void updatePageI(List<ISearchItem> items);
    void updatePage(List<SearchItem> items);
}
