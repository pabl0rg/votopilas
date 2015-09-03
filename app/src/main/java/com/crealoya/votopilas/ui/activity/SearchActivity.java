package com.crealoya.votopilas.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

import com.crealoya.votopilas.R;
import com.crealoya.votopilas.adapter.SearchViewPagerAdapter;
import com.crealoya.votopilas.model.Party;
import com.crealoya.votopilas.model.Politician;
import com.crealoya.votopilas.ui.fragment.PartyPoliticianListFragment;

public class SearchActivity extends BaseActivity implements PartyPoliticianListFragment.PartyPoliticianListener {

    private static final String EXTRA_PARTY = "party";

    private Party mParty;

    // UI Components
    private TabLayout mTabLayout;
    private ViewPager mViewPager;

    // Adapter
    private SearchViewPagerAdapter mAdapter;

    public static void launch(Context context, Party party) {
        Intent intent = new Intent(context, SearchActivity.class);
        intent.putExtra(EXTRA_PARTY, party);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.create(savedInstanceState, R.layout.activity_search);
        showNavigationBack();

        // arguments
        mParty = getIntent().getExtras().getParcelable(EXTRA_PARTY);

        // find views
        mTabLayout = (TabLayout) findViewById(R.id.two_tabs);
        mViewPager = (ViewPager) findViewById(R.id.party_info_viewpager);

        // custom ui
        mAdapter = new SearchViewPagerAdapter(getSupportFragmentManager(), mParty);
        mViewPager.setAdapter(mAdapter);

        // setup view pager
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setTabGravity(TabLayout.GRAVITY_CENTER);
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        final MenuItem exploreMenu = menu.findItem(R.id.search);
        exploreMenu.expandActionView();
//        final SearchView searchView = (SearchView) exploreMenu.getActionView();
//        searchView.clearFocus();
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void removeParty(Party party) {
        // do nothing for now
    }

    @Override
    public void onPoliticianSelected(Politician politician) {
        Intent intent = new Intent(this, PoliticianActivity.class);
        intent.putExtra(Politician.PARSE_POLITICIAN_OBJECT, politician);
        startActivity(intent);
    }
}
