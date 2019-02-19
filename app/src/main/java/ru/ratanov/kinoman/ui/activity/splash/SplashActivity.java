package ru.ratanov.kinoman.ui.activity.splash;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import ru.ratanov.kinoman.R;
import ru.ratanov.kinoman.managers.firebase.ForceUpdateChecker;
import ru.ratanov.kinoman.ui.activity.main.MainActivity;

/**
 * Created by ACER on 08.01.2018.
 */

public class SplashActivity extends AppCompatActivity {

    ImageView logo;
    TextView versionNumberTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);

        String versionNumber = ForceUpdateChecker.getAppVersion(this);
        versionNumberTextView = (TextView) findViewById(R.id.splash_version_tv);
        versionNumberTextView.setText(versionNumber);


        logo = (ImageView) findViewById(R.id.logo);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.splash_anim);
        logo.startAnimation(animation);

        Thread thread = new Thread(){
            @Override
            public void run() {
                try {
                    sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        };

        thread.start();
    }
}
