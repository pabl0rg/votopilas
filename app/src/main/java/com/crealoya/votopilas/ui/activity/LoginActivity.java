package com.crealoya.votopilas.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.crealoya.votopilas.R;
import com.crealoya.votopilas.network.LoginHelper;
import com.crealoya.votopilas.util.PreferencesUtil;
import com.parse.ParseFacebookUtils;

import java.util.HashSet;

public class LoginActivity extends AppCompatActivity {

    public static final int REQUEST_REVIEWS_TO_APPLY = 99;
    // UI components
    private ImageView mBackground;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // find views
        mBackground = (ImageView) findViewById(R.id.login_background_img);

        final LoginHelper.LoginCallback callback = new LoginHelper.LoginCallback() {
            @Override
            public void onLogin(boolean success) {
                if (success) {
                    if (PreferencesUtil.getBooleanPreference(PreferencesUtil.PREF_FIRST_RUN, true)) {
                        Intent valuesActivity = new Intent(LoginActivity.this, SelectValuesActivity.class);
                        valuesActivity.putExtra(BaseActivity.EXTRA_SHOW_BACK, false);
                        valuesActivity.putExtra(BaseActivity.EXTRA_DISABLE_BACK, true);
                        startActivityForResult(valuesActivity, REQUEST_REVIEWS_TO_APPLY);
                    } else {
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    }
                } else {
                    LoginHelper.showLogInDialog(LoginActivity.this, LoginHelper.LOGIN_WITH_OMIT, (ProgressBar) findViewById(android.R.id.progress), this);
                }
            }

            @Override
            public void onLoginCancel() {
                if (PreferencesUtil.getBooleanPreference(PreferencesUtil.PREF_FIRST_RUN, true)) {
                    Intent valuesActivity = new Intent(LoginActivity.this, SelectValuesActivity.class);
                    valuesActivity.putExtra(BaseActivity.EXTRA_SHOW_BACK, false);
                    valuesActivity.putExtra(BaseActivity.EXTRA_DISABLE_BACK, true);
                    startActivityForResult(valuesActivity, REQUEST_REVIEWS_TO_APPLY);
                } else {
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                }
            }
        };

        LoginHelper.showLogInDialog(this, LoginHelper.LOGIN_WITH_OMIT, (ProgressBar) findViewById(android.R.id.progress), callback);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (REQUEST_REVIEWS_TO_APPLY == requestCode) {
            if (resultCode == RESULT_OK) {
                PreferencesUtil.setBooleanPreference(PreferencesUtil.PREF_FIRST_RUN, false);
                PreferencesUtil.setBooleanPreference(PreferencesUtil.PREF_LOGIN_DIALOG_SHOWED, true);
                HashSet<String> usersIds = (HashSet<String>) data.getSerializableExtra(SelectValuesActivity.EXTRA_USERS_IDS);
                PreferencesUtil.setSetPreference(PreferencesUtil.PREF_RATINGS_TO_APPLY, usersIds);
            }
            startActivity(new Intent(this, MainActivity.class));
            finish();
        } else {
            ParseFacebookUtils.finishAuthentication(requestCode, resultCode, data);
        }
    }
}
