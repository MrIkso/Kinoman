package ru.ratanov.kinoman.model.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.arellomobile.mvp.MvpPresenter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import ru.ratanov.kinoman.model.net.QBittorrentAPI;
import ru.ratanov.kinoman.model.net.TorrentAPI;
import ru.ratanov.kinoman.model.net.TransmissionAPI;
import ru.ratanov.kinoman.presentation.presenter.detail.DetailPresenter;
import ru.ratanov.kinoman.presentation.presenter.detail.SamePresenter;

/*
Single method of this class getHashLink(Context context, String linkId)
takes 2 parameters: Context and Kinozal.tv film linkId (e.g. '12345678').
Then load into background WebView page (e.g. 'http://kinozal.me/details.php?id=12345678').
Through injection of JavaScript commands trying to:
    - Login on page
    - Show hidden block with magnet link
    - Pass HTML source of page to predefined 'MyJavaInterface'
      and trying to parse magnet link
If success, pass magnet link to 'TorrentAPI' class for add new download
*/
public class MagnetLinkFetchr {

    public static final String TAG = "MagnetLinkFetchr";

    private static String SERVER_TYPE;
    private static String SERVER;
    private static String PORT;
    private static String LOGIN;
    private static String PASSWORD;


    private static Activity mActivity;
    private static MvpPresenter mMvpPresenter;
    private WebView mWebView;
    private String magnetLinkHash;
    private static String mMagnetLink;

    private class MyJavaInterface {
        @JavascriptInterface
        @SuppressWarnings("unused")
        public void processHtml(String html) {
            Log.i(TAG, "processHtml: " + html.length());
            Document doc = Jsoup.parse(html);
            Elements elements = doc.select("div#containerdata").select("li");
            if (elements.size() < 3) {
                parse();
            }
            for (Element li : elements) {
                if (li.text().contains("Инфо хеш")) {
                    magnetLinkHash = li.text().substring(10);
                    Log.i(TAG, "processHtml: " + magnetLinkHash);
                    mMagnetLink = "magnet:?xt=urn:btih:" + magnetLinkHash;

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

                    break;
                } else {
                    Log.i(TAG, "processHtml: not found");

                    showResultMessage("Ссылка не найдена", false);
                }
            }
        }
    }

    public void getHashLink(Activity activity, MvpPresenter mvpPresenter, final String url) {

        mActivity = activity;
        mMvpPresenter = mvpPresenter;

        final String linkId = url.substring(url.indexOf("=") + 1);

        mWebView = new WebView(mActivity);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setDomStorageEnabled(true);
        mWebView.addJavascriptInterface(new MyJavaInterface(), "HTMLOUT");

        // Ignore HTTPS -> HTTP redirect issue
        if (Build.VERSION.SDK_INT >= 21) {
            mWebView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }

        // Block pop-ups
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.i(TAG, "shouldOverrideUrlLoading: ");
                return false;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                if (url.contains("userdetails")) {
                    view.goBack();
                }

                // Login
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    mWebView.evaluateJavascript("javascript: {" +
                                    "document.forms[1].username.value = 'rbaloo';" +
                                    "document.forms[1].password.value = '756530';" +
                                    "document.forms[1].submit(); };"
                            , null);
                } else {
                    mWebView.loadUrl("javascript: {" +
                            "document.forms[1].username.value = 'rbaloo';" +
                            "document.forms[1].password.value = '756530';" +
                            "document.forms[1].submit(); };");
                }
                Log.i(TAG, "onPageFinished: Login");

                // Show hash

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    mWebView.evaluateJavascript("get_torm(" + linkId + ",2,'Список файлов всего 111','')", null);
                } else {
                    mWebView.loadUrl("javascript:get_torm(" + linkId + ",2,'Список файлов всего 111','');");
                }
                Log.i(TAG, "onPageFinished: Show Hash");

                // Parse
                parse();
            }
        });

        mWebView.loadUrl(url);
        Log.i(TAG, "loadUrl" + url);
    }

    private void parse() {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mWebView.loadUrl("javascript:window.HTMLOUT.processHtml(document.getElementsByTagName('html')[0].innerHTML);");
            }
        });
//        mWebView.loadUrl("javascript:window.HTMLOUT.processHtml(document.getElementsByTagName('html')[0].innerHTML);");
        Log.i(TAG, "onPageFinished: Parse");
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
            hideProgress();
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
        hideProgress();

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

