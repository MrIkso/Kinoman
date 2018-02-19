package ru.ratanov.kinoman.ui.activity.pref;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.OnClick;
import ru.ratanov.kinoman.R;
import ru.ratanov.kinoman.model.net.TorrentAPI;
import ru.ratanov.kinoman.model.net.TransmissionAPI;
import ru.ratanov.kinoman.model.utils.QueryPreferences;

public class OldPreferenceActivity extends android.preference.PreferenceActivity
        implements TorrentAPI.OnTestConnectionListener, TransmissionAPI.OnTestConnectionListener {

    private View mView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
        setContentView(R.layout.pref_button_test);

        ButterKnife.bind(this);

        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            String versionName = "Версия " + packageInfo.versionName;
            TextView versionLabel = (TextView) findViewById(R.id.version_label);
            versionLabel.setText(versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.test_button)
    void test(View view) {
        mView = view;

        String serverType = QueryPreferences.getStoredQuery(this, QueryPreferences.SERVER_TYPE);

        switch (serverType) {
            case "uTorrent":
                TorrentAPI torrentAPI = new TorrentAPI(this);
                torrentAPI.testConnection();
                break;
            case "Transmission":
                TransmissionAPI transmissionAPI = new TransmissionAPI(this);
                transmissionAPI.testConnection();
                break;
        }

        showTestingProgress();
    }

    public void showTestingProgress() {
        Snackbar snackbar = Snackbar.make(mView, "Проверка связи...", Snackbar.LENGTH_INDEFINITE);
        Snackbar.SnackbarLayout snackbarLayout = (Snackbar.SnackbarLayout) snackbar.getView();
        snackbarLayout.addView(new ProgressBar(this));
        snackbar.show();
    }

    @Override
    public void onTestFinish(boolean result) {
        if (result) {
            Snackbar.make(mView, "Связь с сервером установлена", Snackbar.LENGTH_SHORT).show();
        } else {
            Snackbar.make(mView, "Нет связи с сервером", Snackbar.LENGTH_SHORT).show();
        }
    }
}
