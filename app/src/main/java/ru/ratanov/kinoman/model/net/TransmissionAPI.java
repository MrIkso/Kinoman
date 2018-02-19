package ru.ratanov.kinoman.model.net;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import com.arellomobile.mvp.MvpPresenter;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import cz.msebera.android.httpclient.Header;
import ru.ratanov.kinoman.model.utils.QueryPreferences;
import ru.ratanov.kinoman.presentation.presenter.detail.DetailPresenter;
import ru.ratanov.kinoman.presentation.presenter.detail.SamePresenter;

/**
 * Created by ACER on 25.12.2016.
 */

public class TransmissionAPI {

    public static final String TAG = "TransmissionAPI";

    private static String SERVER_URL = null;
    private static String SERVER = null;
    private static String PORT = null;
    private static String LOGIN = null;
    private static String PASSWORD = null;
    private static String mSessionId;

    private Activity mActivity;
    private MvpPresenter mMvpPresenter;

    private OnTestConnectionListener mListener;

    public TransmissionAPI(Activity activity) {
        mActivity = activity;
        mListener = (OnTestConnectionListener) activity;
    }

    public TransmissionAPI(Activity activity, MvpPresenter mvpPresenter) {
        mActivity = activity;
        mMvpPresenter = mvpPresenter;
    }

    public interface OnTestConnectionListener {
        void onTestFinish(boolean result);
    }

    public void addTorrent(String magnetLink) {
        getServerSettings();
        connect(magnetLink);
    }

    private void getServerSettings() {
        SERVER = QueryPreferences.getStoredQuery(mActivity, QueryPreferences.PREF_SERVER);
        PORT = QueryPreferences.getStoredQuery(mActivity, QueryPreferences.PREF_PORT);
        LOGIN = QueryPreferences.getStoredQuery(mActivity, QueryPreferences.PREF_LOGIN);
        PASSWORD = QueryPreferences.getStoredQuery(mActivity, QueryPreferences.PREF_PASSWORD);
    }

    private void connect(final String magnetLink) {
        SERVER_URL = "http://" + SERVER + ":" + PORT + "/transmission/rpc";
        HttpClient.setBasicAuth(LOGIN, PASSWORD);

        HttpClient.get(SERVER_URL, null, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.d(TAG, "connect onSuccess: " + statusCode + ":" + new String(responseBody));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                if (statusCode == 409) {
                    Log.d(TAG, "connect onFailure: " + statusCode + ":" + new String(responseBody));

                    Document doc = Jsoup.parse(new String(responseBody));
                    Log.d(TAG, "connect onFailure: " + doc.select("code").text());

                    mSessionId = doc.select("code").text().substring(27);
                    Log.d(TAG, "connect onFailure: " + mSessionId);

                    String addCommand = getAddCommand(magnetLink);
                    new TestTask().execute(addCommand);
                } else if (statusCode == 401) {
                    showResultMessage("Проверьте имя и пароль в настройках");
                }
                
            }
        });
    }

    private String getAddCommand(String magnetLink) {
        return "{\"arguments\":{\"filename\":\"" + magnetLink + "\"},\"method\":\"torrent-add\"}";
    }

    private class TestTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {

            Log.d(TAG, "doInBackground: Start Add Task");

            String command = params[0];
            Log.d(TAG, "doInBackground: command = " + command);

            try {
                URL url = new URL(SERVER_URL);

                String auth = LOGIN + ":" + PASSWORD;
                String encodedAuth = Base64.encodeToString(auth.getBytes(), Base64.NO_WRAP);

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Accept", "application/json");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("X-Transmission-Session-Id", mSessionId);
                connection.setRequestProperty("Authorization", "Basic " + encodedAuth);
                connection.setDoOutput(true);
                connection.connect();

                OutputStream outputStream = null;
                outputStream = new BufferedOutputStream(connection.getOutputStream());
                outputStream.write(command.getBytes());
                outputStream.flush();

                Log.d(TAG, "doInBackground: " + connection.getResponseCode() + connection.getResponseMessage());

                StringBuffer buffer = new StringBuffer();
                InputStream inputStream = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

                Log.d(TAG, "doInBackground: " + connection.getResponseCode() + connection.getResponseMessage());


                String line = null;

                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\r\n");
                }

                Log.d(TAG, "doInBackground: " + buffer);

                if (buffer.toString().contains("torrent-added")) {
                    showResultMessage("Торрент добавлен");
                } else {
                    showResultMessage("Не удалось добавить торрент");
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public void testConnection() {
        getServerSettings();

        SERVER_URL = "http://" + SERVER + ":" + PORT + "/transmission/rpc";
        HttpClient.setBasicAuth(LOGIN, PASSWORD);

        HttpClient.get(SERVER_URL, null, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                mListener.onTestFinish(true);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                if (statusCode == 409) {
                    mListener.onTestFinish(true);
                } else if (statusCode == 401) {
                    showResultMessage("Проверьте имя и пароль в настройках");
                } else {
                    mListener.onTestFinish(false);
                }
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
