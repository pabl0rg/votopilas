package com.crealoya.votopilas.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.crealoya.votopilas.R;
import com.crealoya.votopilas.adapter.RatingsToApplyAdapter;
import com.crealoya.votopilas.network.ParseCloudHelper;
import com.parse.ParseObject;

import java.util.HashSet;

public class SelectValuesActivity extends BaseActivity implements View.OnClickListener {

    public static final String EXTRA_USERS_IDS = "extra_user_ids";

    // UI components
    private TextView mContinue;
    private ListView mList;
    private ProgressBar mProgress;
    private HashSet<String> mCheckedValues;

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.create(savedInstanceState, R.layout.activity_select_values);
        super.showNavigationBack();

        if (getIntent().hasExtra(EXTRA_USERS_IDS)) {
            mCheckedValues = (HashSet<String>) getIntent().getSerializableExtra(EXTRA_USERS_IDS);
        }

        // find views
        mContinue = (TextView) findViewById(R.id.values_continue);
        mList = (ListView) findViewById(android.R.id.list);
        mProgress = (ProgressBar) findViewById(android.R.id.progress);

        // set listeners
        mContinue.setOnClickListener(this);

        //initializing
        mList.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        ParseCloudHelper.getVipUsers(this, mList, mProgress, mCheckedValues);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.values_continue) {

            SparseBooleanArray checkItems = mList.getCheckedItemPositions();
            RatingsToApplyAdapter adapter = (RatingsToApplyAdapter) mList.getAdapter();
            HashSet<String>  usersIds = new HashSet<>();
            boolean atLeastOneValue = false;
            for (int i = 0; i < checkItems.size() ; i++) {
                if (checkItems.valueAt(i)) {
                    atLeastOneValue = true;
                    ParseObject user = adapter.getItem(checkItems.keyAt(i));
                    usersIds.add(user.getObjectId());
                }
            }
            if (atLeastOneValue) {
                Intent result = new Intent();
                result.putExtra(EXTRA_USERS_IDS, usersIds);
                setResult(RESULT_OK, result);
                finish();
            } else {
                Toast.makeText(this, R.string.select_at_least_one_value, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
