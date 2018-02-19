package ru.ratanov.kinoman.model.net;

import android.app.Activity;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import com.arellomobile.mvp.MvpPresenter;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.CookieManager;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

import ru.ratanov.kinoman.model.utils.QueryPreferences;
import ru.ratanov.kinoman.presentation.presenter.detail.DetailPresenter;
import ru.ratanov.kinoman.presentation.presenter.detail.SamePresenter;

/**
 * Created by ACER on 26.12.2016.
 */

public class QBittorrentAPI {

    public static final String TAG = "QBittorrentAPI";

    private static String SERVER_URL = null;
    private static String SERVER = null;
    private static String PORT = null;
    private static String LOGIN = null;
    private static String PASSWORD = null;
    private static String AUTH_PARAMS = null;

    private static String COOKIES_HEADER = "Set-Cookie";
    private static String COOKIE = "Cookie";

    private Activity mActivity;
    private MvpPresenter mMvpPresenter;

    private static CookieManager mCookieManager = new CookieManager();

    public QBittorrentAPI(Activity activity) {
        mActivity = activity;
    }

    public QBittorrentAPI(Activity activity, MvpPresenter mvpPresenter) {
        mActivity = activity;
        mMvpPresenter = mvpPresenter;
    }

    public void addTorrent(String magnetLink) {
        getServerSettings();
        new ConnectTask().execute(magnetLink);
    }

    public void testConnection() {

    }

    private void getServerSettings() {
        SERVER = QueryPreferences.getStoredQuery(mActivity, QueryPreferences.PREF_SERVER);
        PORT = QueryPreferences.getStoredQuery(mActivity, QueryPreferences.PREF_PORT);
        LOGIN = QueryPreferences.getStoredQuery(mActivity, QueryPreferences.PREF_LOGIN);
        PASSWORD = QueryPreferences.getStoredQuery(mActivity, QueryPreferences.PREF_PASSWORD);
        SERVER_URL = "http://" + SERVER + ":" + PORT;
        AUTH_PARAMS = "username=" + LOGIN + "&password=" + PASSWORD;
    }

    private class ConnectTask extends AsyncTask<String, Void, Void> {

        private String mMagnetLink;

        @Override
        protected Void doInBackground(String... strings) {

            mMagnetLink = strings[0];
            String url = SERVER_URL + "/login";

            try {
                URL obj = new URL(url);
                HttpURLConnection connection = (HttpURLConnection) obj.openConnection();

                connection.setRequestMethod("POST");
                connection.setRequestProperty("Host", SERVER_URL);
                connection.setRequestProperty("User-Agent", "Fiddler");
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                connection.setRequestProperty("Length", Integer.toString(AUTH_PARAMS.length()));

                if (mCookieManager.getCookieStore().getCookies().size() > 0) {
                    connection.setRequestProperty(COOKIE,
                            TextUtils.join(";", mCookieManager.getCookieStore().getCookies()));
                }

                connection.setDoOutput(true);
                connection.setDoInput(true);

                OutputStream outputStream = connection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

                writer.write(AUTH_PARAMS);
                writer.flush();
                writer.close();
                outputStream.close();

                Map<String, List<String>> headerFields = connection.getHeaderFields();
                List<String> cookiesHeader = headerFields.get(COOKIES_HEADER);

                if (cookiesHeader != null) {
                    for (String cookie : cookiesHeader) {
                        mCookieManager.getCookieStore().add(null, HttpCookie.parse(cookie).get(0));
                    }
                }

                System.out.println("Headers : " + connection.getHeaderFields().toString());

            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            new AddTorrentTask().execute(mMagnetLink);
        }
    }

    private class AddTorrentTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... strings) {

            String url = SERVER_URL + "/command/download";
            String command = "urls=" + strings[0];

            try {
                URL obj = new URL(url);
                HttpURLConnection connection = (HttpURLConnection) obj.openConnection();

                connection.setRequestMethod("POST");
                connection.setRequestProperty("Host", SERVER_URL);
                connection.setRequestProperty("User-Agent", "Fiddler");
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                connection.setRequestProperty("Length", Integer.toString(command.length()));
//                connection.setRequestProperty("Content-Disposition", "form-data; name=\"urls\"");
                connection.setRequestProperty(COOKIE, "SID=lmsAAMgbAABAQAAAJR0AAD4rAACKRwAA");

                if (mCookieManager.getCookieStore().getCookies().size() > 0) {
                    connection.addRequestProperty(COOKIE,
                            TextUtils.join(";", mCookieManager.getCookieStore().getCookies()));
                }

                connection.setDoOutput(true);
                connection.setDoInput(true);

                OutputStream outputStream = connection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

                writer.write(command);
                writer.flush();
                writer.close();
                outputStream.close();

                Log.d(TAG, "Response Code : " + connection.getResponseCode());
                Log.d(TAG, "Response Message : " + connection.getResponseMessage());
                Log.d(TAG, "doInBackground: " + connection.getHeaderFields().toString());

                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    showResultMessage("Торрент добавлен");
                    Log.d(TAG, "doInBackground: " + connection.getResponseCode());
                } else {
                    showResultMessage("Не удалось добавить торрент");
                    Log.d(TAG, "doInBackground: " + connection.getResponseCode());
                }

            } catch (IOException e) {
                e.printStackTrace();
                showResultMessage("Не удалось добавить торрент");
            }

            return null;
        }
    }

    private void showResultMessage(String message) {
        if (mMvpPresenter instanceof DetailPresenter) {
            ((DetailPresenter) mMvpPresenter).showResult(message, false);
        } else if (mMvpPresenter instanceof SamePresenter) {
            ((SamePresenter) mMvpPresenter).showResult(message, false);
        }
    }
}
