package com.crealoya.votopilas.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.crealoya.votopilas.R;
import com.crealoya.votopilas.model.Party;

/**
 * Created by @pablopantaleon on 7/31/15.
 */
public class AllPoliticiansListFragment extends Fragment {

    public static final String EXTRA_PARTY = "extra.party";

    public static AllPoliticiansListFragment newInstance(Party party) {
        Bundle args = new Bundle();
        args.putParcelable(EXTRA_PARTY, party);

        AllPoliticiansListFragment fragment = new AllPoliticiansListFragment();
        fragment.setArguments(args);

        return fragment;
    }

    public AllPoliticiansListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_search_politicians_list, container, false);

        // get arguments
//        mParty = getArguments().getParcelable(EXTRA_PARTY);
//        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
//
//        // find views
//        mRecyclerView = (RecyclerView) view.findViewById(android.R.id.list);
//        mProgress = (ProgressBar) view.findViewById(android.R.id.progress);
//
//        // listeners
//        view.findViewById(R.id.party_info).setOnClickListener(this);
//        mSwipeRefreshLayout.setOnRefreshListener(this);
//
//        // setup recycler view
//        mRecyclerView.setHasFixedSize(true);
//        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//
//        onRefresh();
        return view;
    }
}
