package ru.ratanov.kinoman.presentation.presenter.main;

import android.content.Context;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import java.util.List;

import ru.ratanov.kinoman.model.content.TopItem;
import ru.ratanov.kinoman.model.parsers.FilmParser;
import ru.ratanov.kinoman.presentation.view.main.MainView;

/**
 * Created by ACER on 15.01.2017.
 */

@InjectViewState
public class TopPresenter extends MvpPresenter<MainView> {

    public TopPresenter() {
    }

    public void loadData(Context context, String pageNumber) {
        FilmParser filmParser = new FilmParser(this);
        filmParser.getTopFilms(context, pageNumber);
    }

    public void onLoadComplete(List<TopItem> topItems) {
        getViewState().setupAdapter(topItems);
    }
}
