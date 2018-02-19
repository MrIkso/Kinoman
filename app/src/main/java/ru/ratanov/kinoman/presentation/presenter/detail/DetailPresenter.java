package ru.ratanov.kinoman.presentation.presenter.detail;


import android.app.Activity;

import ru.ratanov.kinoman.model.content.Film;
import ru.ratanov.kinoman.model.parsers.FilmParser;
import ru.ratanov.kinoman.model.parsers.MagnetParser;
import ru.ratanov.kinoman.model.utils.MagnetLinkFetchr;
import ru.ratanov.kinoman.presentation.view.detail.DetailView;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

@InjectViewState
public class DetailPresenter extends MvpPresenter<DetailView> {

    public void loadFilm(String url) {
        getViewState().showProgress(null);
        FilmParser filmParser = new FilmParser(this);
        filmParser.getFilm(url);
    }

    public void updatePage(Film film) {
        getViewState().hideProgress();
        getViewState().updatePage(film);
    }

    public void loadTrailer(String kpUrl) {
        getViewState().showProgress("Поиск трейлера");
        new FilmParser(this).getTrailerUrl(kpUrl);
    }

    public void showTrailer(String trailerUrl) {
        getViewState().hideProgress();
        getViewState().showTrailer(trailerUrl);
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
