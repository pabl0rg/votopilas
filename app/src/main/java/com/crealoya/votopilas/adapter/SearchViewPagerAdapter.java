package com.crealoya.votopilas.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.crealoya.votopilas.model.Party;
import com.crealoya.votopilas.ui.fragment.SearchPoliticiansListFragment;

/**
 * Created by @pablopantaleon on 7/31/15.
 */
public class SearchViewPagerAdapter extends FragmentStatePagerAdapter {

    public static final int PAGE_PARTY = 0;
    public static final int PAGE_ALL_PARTIES = 1;

    private static final int PAGES = 1;
    private static final String ALL_PARTIES = "Buscar en todos los partidos";

    private Party mParty;

    public SearchViewPagerAdapter(FragmentManager fm, Party party) {
        super(fm);
        this.mParty = party;
    }

    @Override
    public Fragment getItem(int position) {
        return SearchPoliticiansListFragment.newInstance(mParty, SearchPoliticiansListFragment.QueryType.ALL);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return ALL_PARTIES;
    }

    @Override
    public int getCount() {
        return PAGES;
    }
}
