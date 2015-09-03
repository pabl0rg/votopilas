package com.crealoya.votopilas.ui.activity;

import android.os.Bundle;
import android.view.MenuItem;

import com.crealoya.votopilas.R;

public class AboutActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.create(savedInstanceState, R.layout.activity_about);
        showNavigationBack();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
