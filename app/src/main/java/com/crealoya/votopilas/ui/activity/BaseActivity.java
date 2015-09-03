package com.crealoya.votopilas.ui.activity;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.crealoya.votopilas.R;

/**
 * Created by Pablo Reyes, @pablopantaleon on 6/28/15.
 */
public abstract class BaseActivity extends AppCompatActivity {

    public static final String EXTRA_SHOW_BACK = "extra_show_back";
    public static final String EXTRA_DISABLE_BACK = "extra_disable_back";

    private boolean mShowBack;
    private boolean mDisableBack;

    public void create(Bundle savedInstanceState, @LayoutRes int layoutId) {
        super.onCreate(savedInstanceState);

        mShowBack = getIntent().getBooleanExtra(EXTRA_SHOW_BACK, true);
        mDisableBack = getIntent().getBooleanExtra(EXTRA_DISABLE_BACK, false);

        setContentView(layoutId);
        showToolbar(null);
    }

    public void showToolbar(@Nullable View view) {
        if (view == null) {
            setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        } else {
            setSupportActionBar((Toolbar) view.findViewById(R.id.toolbar));
        }

        // TODO: change drawer icon
        final ActionBar actionBar = getSupportActionBar();
        if (mShowBack) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_action_navigation_menu);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public void showNavigationBack() {
        // TODO: change drawer icon
        if (mShowBack) {
            final ActionBar actionBar = getSupportActionBar();
            actionBar.setHomeAsUpIndicator(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public void showFragment(@IdRes int fragmentContainer, Fragment fragment, @NonNull String tag) {
        final FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(fragmentContainer, fragment, tag).commit();
    }

    @Override
    public void onBackPressed() {
        if (!mDisableBack) {
            super.onBackPressed();
        }
    }
}
