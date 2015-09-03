package com.crealoya.votopilas.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.crealoya.votopilas.R;
import com.crealoya.votopilas.adapter.RestoreListAdapter;
import com.crealoya.votopilas.model.ListName;
import com.crealoya.votopilas.model.Party;
import com.crealoya.votopilas.network.Callback;
import com.crealoya.votopilas.network.ParseHelper;

import java.util.List;

public class RestoreActivity extends BaseActivity {

    public static final String TYPE = "type";
    public static final String EXTRA_LISTNAMES_HIDDEN = "extra_listnames";
    public static final String EXTRA_LISTNAMES = "extra_listnameshidden";

    public static final int PARTY = 100;
    public static final int LIST = 101;

    private ProgressBar mProgress;
    private RecyclerView mRecyclerView;

    private int mType;

    public static void launch(Activity context, int type, int request) {
        Intent intent = new Intent(context, RestoreActivity.class);
        intent.putExtra(TYPE, type);
        context.startActivityForResult(intent, request);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.create(savedInstanceState, R.layout.activity_restore);
        showNavigationBack();

        // get arguments
        mType = getIntent().getExtras().getInt(TYPE);

        // find views
        mRecyclerView = (RecyclerView) findViewById(R.id.restore_recycler);
        mProgress = (ProgressBar) findViewById(android.R.id.progress);

        // setup recycler view
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // setup adapter
        if (mType == PARTY) {
            setTitle("Ocultar Partidos");
            ParseHelper.getParties(new Callback<List<Party>>() {
                @Override
                public void done(boolean success, List<Party> data) {
                    RestoreListAdapter adapter = new RestoreListAdapter(RestoreActivity.this, data);
                    mRecyclerView.setAdapter(adapter);
                    mProgress.setVisibility(View.GONE);
                }
            });
        } else {
            setTitle("Ocultar Listados");
            ParseHelper.getListNames(new Callback<List<ListName>>() {
                @Override
                public void done(boolean success, List<ListName> data) {
                    RestoreListAdapter adapter = new RestoreListAdapter(RestoreActivity.this, data);
                    mRecyclerView.setAdapter(adapter);
                    mProgress.setVisibility(View.GONE);
                }
            });
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            putResults();
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private void putResults() {
        RestoreListAdapter adapter = (RestoreListAdapter) mRecyclerView.getAdapter();
        Intent result = new Intent();
        if (adapter.didSomethingChange()) {
            setResult(RESULT_OK, result);
        } else {
            setResult(RESULT_CANCELED, result);
        }
    }
}
