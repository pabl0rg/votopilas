package com.crealoya.votopilas.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;

import com.crealoya.votopilas.model.Party;
import com.crealoya.votopilas.ui.fragment.PartyPoliticianListFragment;

import java.util.List;

/**
 * Created by @pablopantaleon on 7/20/15.
 */
public class PartyPoliticianViewPagerAdapter extends FragmentStatePagerAdapter {

    private List<Party> mParties;

    public PartyPoliticianViewPagerAdapter(FragmentManager fm, List<Party> parties) {
        super(fm);
        this.mParties = parties;
    }

    public String makeFragmentName(int viewId, int index) {
        return "android:switcher:" + viewId + ":" + index;
    }

    public Party getParty(final int position) {
        return mParties.get(position);
    }

    @Override
    public Fragment getItem(int position) {
        return PartyPoliticianListFragment.newInstance(mParties.get(position));
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mParties.get(position).shortname;
    }

    @Override
    public int getCount() {
        return mParties.size();
    }

    @Override
    public int getItemPosition(Object object){
        return PagerAdapter.POSITION_NONE;
    }
}
