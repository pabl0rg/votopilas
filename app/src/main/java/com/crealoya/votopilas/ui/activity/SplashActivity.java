package com.crealoya.votopilas.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.crealoya.votopilas.R;
import com.crealoya.votopilas.network.ParseHelper;
import com.crealoya.votopilas.util.PreferencesUtil;
import com.parse.ParseUser;

/**
 * Created by Pablo Reyes, @pablopantaleon on 7/18/15.
 */
public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_TIME_OUT = 1500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // fetch data and store it locally
        ParseHelper.getListNames(null);
        ParseHelper.getParties(null);
        ParseHelper.getCharges(null);

        // start activity
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent;

                if (PreferencesUtil.getBooleanPreference(PreferencesUtil.PREF_FIRST_RUN, true)) {
                    intent = new Intent(SplashActivity.this, LoginActivity.class);
                } else if (PreferencesUtil.getBooleanPreference(PreferencesUtil.PREF_LOGIN_DIALOG_SHOWED, false)) {
                    intent = new Intent(SplashActivity.this, MainActivity.class);
                } else if (ParseUser.getCurrentUser() == null) {
                    intent = new Intent(SplashActivity.this, LoginActivity.class);
                } else {
                    intent = new Intent(SplashActivity.this, MainActivity.class);
                }

                startActivity(intent);
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}
