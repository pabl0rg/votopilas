package com.crealoya.votopilas.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.crealoya.votopilas.R;
import com.crealoya.votopilas.network.LoginHelper;
import com.crealoya.votopilas.util.DialogUtil;
import com.crealoya.votopilas.util.UserPreferencesHelper;
import com.crealoya.votopilas.util.PreferencesUtil;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;

import java.util.HashSet;

/**
 * Created by Pablo Reyes, @pablopantaleon on 7/18/15.
 */
public class SettingsActivity extends BaseActivity implements View.OnClickListener{
    public static final int REQUEST_RATINGS_TO_APPLY = 99;
    public static final int REQUEST_LISTNAMES = 100;
    public static final int REQUEST_PARTIES = 101;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.create(savedInstanceState, R.layout.activity_settings);
        showNavigationBack();
        findViewById(R.id.choose_valuation).setOnClickListener(this);
        findViewById(R.id.login).setOnClickListener(this);
        findViewById(R.id.restore_parties).setOnClickListener(this);
        if (ParseUser.getCurrentUser() == null) {
            ((TextView)findViewById(R.id.login_text)).setText(getString(R.string.drawer_list_item_login));
        } else {
            ((TextView)findViewById(R.id.login_text)).setText(getString(R.string.drawer_list_item_logout));
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.choose_valuation:
                Intent selectValuesActivity = new Intent(this, SelectValuesActivity.class);
                HashSet<String> usersIds = new HashSet<>(PreferencesUtil.getSetPreference(PreferencesUtil.PREF_RATINGS_TO_APPLY, new HashSet<String>()));
                selectValuesActivity.putExtra(SelectValuesActivity.EXTRA_USERS_IDS, usersIds);
                startActivityForResult(selectValuesActivity, REQUEST_RATINGS_TO_APPLY);
                break;
            case R.id.login:
                if (ParseUser.getCurrentUser() == null) {
                    LoginHelper.showLogInDialog(this, LoginHelper.LOGIN_WITH_CANCEL, new LoginHelper.LoginCallback() {
                        @Override
                        public void onLogin(boolean success) {
                            if (success) {
                                finish();
                            }
                        }

                        @Override
                        public void onLoginCancel() {
                            // do nothing
                        }
                    });
                } else {
                    ParseUser.logOut();
                    com.facebook.Session fbs = com.facebook.Session.getActiveSession();
                    ((TextView)findViewById(R.id.login_text)).setText(getString(R.string.drawer_list_item_login));
                    if (fbs != null) {
                        fbs.closeAndClearTokenInformation();
                    }
                }
                break;
            case R.id.restore_parties:
//                RestoreActivity.launch(this, RestoreActivity.PARTY, REQUEST_PARTIES);
                DialogUtil.simpleDialog(this, DialogUtil.UNHIDE_PARTIES, new DialogUtil.DialogListener() {
                    @Override
                    public void onClose(boolean isAccepted) {
                        if (isAccepted) {
                            UserPreferencesHelper.clearHiddenParties();
                        }
                    }
                });
                break;
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (REQUEST_RATINGS_TO_APPLY == requestCode) {
            if (resultCode == RESULT_OK) {
                HashSet<String> usersIds = (HashSet<String>) data.getSerializableExtra(SelectValuesActivity.EXTRA_USERS_IDS);
                PreferencesUtil.setSetPreference(PreferencesUtil.PREF_RATINGS_TO_APPLY, usersIds);
            }
        } else {
            ParseFacebookUtils.finishAuthentication(requestCode, resultCode, data);
        }
    }
}
