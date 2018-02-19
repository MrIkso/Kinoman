package ru.ratanov.kinoman.model.net;

import android.app.Activity;
import android.net.Uri;
import android.util.Log;

import com.arellomobile.mvp.MvpPresenter;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.jsoup.Jsoup;

import cz.msebera.android.httpclient.Header;
import ru.ratanov.kinoman.model.utils.QueryPreferences;
import ru.ratanov.kinoman.presentation.presenter.detail.DetailPresenter;
import ru.ratanov.kinoman.presentation.presenter.detail.SamePresenter;

public class TorrentAPI {

    private Activity mActivity;
    private MvpPresenter mMvpPresenter;

    private OnTestConnectionListener mListener;

    public static final String TAG = "TorrentAPI";

    private static String token = null;
    private static String SERVER_URL = null;
    private static String SERVER = null;
    private static String PORT = null;
    private static String LOGIN = null;
    private static String PASSWORD = null;

    public TorrentAPI(Activity activity) {
        this.mActivity = activity;
        mListener = (OnTestConnectionListener) activity;
    }

    public TorrentAPI(Activity activity, MvpPresenter mMvpPresenter) {
        this.mActivity = activity;
        this.mMvpPresenter = mMvpPresenter;
    }

    public interface OnTestConnectionListener {
        void onTestFinish(boolean result);
    }

    public void testConnection() {

        getServerSettings();

        SERVER_URL = "http://" + SERVER + ":" + PORT + "/gui/";
        String url = SERVER_URL + "token.html";

        HttpClient.setBasicAuth(LOGIN, PASSWORD);
        HttpClient.get(url, null, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                mListener.onTestFinish(true);
                Log.d(TAG, "onSuccess: ");
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
               mListener.onTestFinish(false);
                Log.d(TAG, "onFailure: ");
            }
        });
    }

    private void getServerSettings() {
        SERVER = QueryPreferences.getStoredQuery(mActivity, QueryPreferences.PREF_SERVER);
        PORT = QueryPreferences.getStoredQuery(mActivity, QueryPreferences.PREF_PORT);
        LOGIN = QueryPreferences.getStoredQuery(mActivity, QueryPreferences.PREF_LOGIN);
        PASSWORD = QueryPreferences.getStoredQuery(mActivity, QueryPreferences.PREF_PASSWORD);
    }

    public void addTorrent(final String magnetLink) {

        getServerSettings();

        SERVER_URL = "http://" + SERVER + ":" + PORT + "/gui/";

        String url = SERVER_URL + "token.html";

        Log.d(TAG, "addTorrent: " + url);

        HttpClient.setBasicAuth(LOGIN, PASSWORD);
        HttpClient.get(url, null, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String doc = new String(responseBody);
                token = Jsoup.parse(doc).select("div#token").text();
                Log.i(TAG, "Got token " + token);

                String url = Uri.parse(SERVER_URL)
                        .buildUpon()
                        .appendQueryParameter("token", token)
                        .appendQueryParameter("action", "add-url")
                        .appendQueryParameter("s", magnetLink)
                        .build()
                        .toString();

                HttpClient.get(url, null, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        showResultMessage("Торрент добавлен");
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        showResultMessage("Ошибка добавления торрента (" + statusCode + ")");
                    }
                });

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                showResultMessage("Нет связи  с сервером (" + statusCode + ")");
            }
        });
    }

    private void showResultMessage(String message) {
        if (mMvpPresenter instanceof DetailPresenter) {
            ((DetailPresenter) mMvpPresenter).showResult(message, false);
        } else if (mMvpPresenter instanceof SamePresenter) {
            ((SamePresenter) mMvpPresenter).showResult(message, false);
        }
    }
}
