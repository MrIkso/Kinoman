package ru.ratanov.kinoman.presentation.presenter.detail;

import android.app.Activity;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import java.util.List;

import ru.ratanov.kinoman.model.content.SameItem;
import ru.ratanov.kinoman.model.parsers.FilmParser;
import ru.ratanov.kinoman.model.parsers.MagnetParser;
import ru.ratanov.kinoman.model.utils.MagnetLinkFetchr;
import ru.ratanov.kinoman.presentation.view.detail.SameView;

@InjectViewState
public class SamePresenter extends MvpPresenter<SameView> {

    public void loadSameFilms(String searchUrl) {
        new FilmParser(this).getSameFilms(searchUrl);
    }

    public void showSameFilms(List<SameItem> items) {
        getViewState().setupAdapter(items);
    }

    public void download(Activity activity, String url) {
//        getViewState().showAddingProgress();
//        new MagnetLinkFetchr().getHashLink(activity, this, url);
        new MagnetParser().getMagnetLink(activity, this, url);
    }

    public void showResult (String result, boolean setupServer) {
        getViewState().showAddingResult(result, setupServer);
    }

    public void hideAddingProgress() {
        getViewState().hideAddingProgress();
    }
}
