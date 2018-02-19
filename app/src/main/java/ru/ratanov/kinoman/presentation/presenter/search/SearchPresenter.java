package ru.ratanov.kinoman.presentation.presenter.search;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import java.util.List;

import ru.ratanov.kinoman.model.content.SearchItem;
import ru.ratanov.kinoman.model.parsers.SearchAPI;
import ru.ratanov.kinoman.presentation.view.search.SearchView;

@InjectViewState
public class SearchPresenter extends MvpPresenter<SearchView> {
    public void doSearch(String query) {
        getViewState().showProgress();
        SearchAPI searchAPI = new SearchAPI(this);
        searchAPI.search(query);
    }

    public void updatePage(List<SearchItem> items) {
        getViewState().hideProgress();
        getViewState().updatePage(items);
    }

}
