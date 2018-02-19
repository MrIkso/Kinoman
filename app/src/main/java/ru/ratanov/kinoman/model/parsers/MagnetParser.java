package ru.ratanov.kinoman.model.parsers;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;

import com.arellomobile.mvp.MvpPresenter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

import ru.ratanov.kinoman.model.net.QBittorrentAPI;
import ru.ratanov.kinoman.model.net.TorrentAPI;
import ru.ratanov.kinoman.model.net.TransmissionAPI;
import ru.ratanov.kinoman.model.utils.MagnetLinkFetchr;
import ru.ratanov.kinoman.model.utils.QueryPreferences;
import ru.ratanov.kinoman.presentation.presenter.detail.DetailPresenter;
import ru.ratanov.kinoman.presentation.presenter.detail.SamePresenter;

public class MagnetParser {

    public static final String TAG = "MagnetParser";

    private static String SERVER_TYPE;
    private static String SERVER;
    private static String PORT;
    private static String LOGIN;
    private static String PASSWORD;


    private static Activity mActivity;
    private static MvpPresenter mMvpPresenter;
    private static String mMagnetLink;


    public void getMagnetLink(Activity activity, MvpPresenter mvpPresenter, String url) {
        Log.i(TAG, "getMagnetLink: ");
        mMvpPresenter = mvpPresenter;
        mActivity = activity;
        new GetMagnetLinkTask().execute(url);

    }

    private class GetMagnetLinkTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... strings) {
            Log.i(TAG, "doInBackground: " + strings[0]);
            String mMagnetUrl = strings[0].replace("https://kinozal-tv.appspot.com/details.php",
                    "https://s-kinozal-tv.appspot.com/getmagnet?");
            Log.i(TAG, "doInBackground: " + mMagnetUrl);
            try {
                Document doc = Jsoup.connect(mMagnetUrl).get();
                mMagnetLink = doc.select("a").first().attr("href");
                Log.i(TAG, "doInBackground: MAGNET = " + mMagnetLink);


            } catch (IOException e) {
                e.printStackTrace();
                Log.i(TAG, "doInBackground: failed");
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Log.i(TAG, "onPostExecute: ");

            String downloadBehavior = QueryPreferences.getStoredQuery(mActivity, "download_behavior");
            if (downloadBehavior == null) {
                downloadBehavior = "ask";
            }

            switch (downloadBehavior) {
                case "ask":
                    ChooseDialogFragment dialogFragment = new ChooseDialogFragment();
                    FragmentManager fragmentManager = ((AppCompatActivity) mActivity).getSupportFragmentManager();
                    dialogFragment.show(fragmentManager, "choose");
                    break;
                case "send_to_server":
                    sendToServer();
                    break;
                case "send_intent":
                    sendIntent();
                    break;
            }
        }
    }


    // TODO: 31.12.2016 Replace with listener
    private static void showResultMessage(String message, boolean setupServer) {
        if (mMvpPresenter instanceof DetailPresenter) {
            ((DetailPresenter) mMvpPresenter).showResult(message, setupServer);
        } else if (mMvpPresenter instanceof SamePresenter) {
            ((SamePresenter) mMvpPresenter).showResult(message, setupServer);
        }
    }

    private static boolean isServerSettedUp() {
        SERVER_TYPE = QueryPreferences.getStoredQuery(mActivity, QueryPreferences.SERVER_TYPE);
        SERVER = QueryPreferences.getStoredQuery(mActivity, QueryPreferences.PREF_SERVER);
        PORT = QueryPreferences.getStoredQuery(mActivity, QueryPreferences.PREF_PORT);
        LOGIN = QueryPreferences.getStoredQuery(mActivity, QueryPreferences.PREF_LOGIN);
        PASSWORD = QueryPreferences.getStoredQuery(mActivity, QueryPreferences.PREF_PASSWORD);

        return !(TextUtils.isEmpty(SERVER) ||
                TextUtils.isEmpty(PORT) ||
                TextUtils.isEmpty(LOGIN) ||
                TextUtils.isEmpty(PASSWORD));
    }

    public static class ChooseDialogFragment extends DialogFragment {

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            String[] choose = {"Отправить на сервер", "Скачать на устройство"};
            final int[] checkedItem = new int[1];

            AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
            builder.setTitle("Выберите действие")
                    .setSingleChoiceItems(choose, -1, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            checkedItem[0] = i;
                            Log.d(TAG, "onClick: " + i);
                        }
                    })
                    .setPositiveButton("Запомнить выбор", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            QueryPreferences.setStoredQuery(mActivity, "download_behavior", "ask");

                            switch (checkedItem[0]) {
                                case 0:
                                    QueryPreferences.setStoredQuery(mActivity, "download_behavior", "send_to_server");
                                    sendToServer();
                                    break;
                                case 1:
                                    QueryPreferences.setStoredQuery(mActivity, "download_behavior", "send_intent");
                                    sendIntent();
                                    break;
                            }
                            dialogInterface.dismiss();
                        }
                    })
                    .setNegativeButton("Только сейчас", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            switch (checkedItem[0]) {
                                case 0:
                                    sendToServer();
                                    break;
                                case 1:
                                    sendIntent();
                                    break;
                            }
                            dialogInterface.dismiss();
                        }
                    });

            return builder.create();
        }

        @Override
        public void onDismiss(DialogInterface dialog) {
            super.onDismiss(dialog);
//            hideProgress();
        }
    }

    private static void sendToServer() {

        if (!isServerSettedUp()) {
            showResultMessage("Заполните данные сервера в настройках", true);
            return;
        }

        switch (SERVER_TYPE) {
            case "uTorrent":
                TorrentAPI torrentAPI = new TorrentAPI(mActivity, mMvpPresenter);
                torrentAPI.addTorrent(mMagnetLink);
                break;
            case "Transmission":
                TransmissionAPI transmissionAPI = new TransmissionAPI(mActivity, mMvpPresenter);
                transmissionAPI.addTorrent(mMagnetLink);
                break;
            case "QBittorrent":
                QBittorrentAPI qBittorrentAPI = new QBittorrentAPI(mActivity, mMvpPresenter);
                qBittorrentAPI.addTorrent(mMagnetLink);
                break;
        }
    }

    private static void sendIntent() {
//        hideProgress();

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setType("application/x-bittorrent");
        intent.setData(Uri.parse(mMagnetLink));
        mActivity.startActivity(intent);
    }

    // TODO: 31.12.2016 Replace with Listener
    private static void hideProgress() {
        if (mMvpPresenter instanceof DetailPresenter) {
            ((DetailPresenter) mMvpPresenter).hideAddingProgress();
        } else if (mMvpPresenter instanceof SamePresenter) {
            ((SamePresenter) mMvpPresenter).hideAddingProgress();
        }
    }


}
