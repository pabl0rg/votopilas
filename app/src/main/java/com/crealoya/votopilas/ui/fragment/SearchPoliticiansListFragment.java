package com.crealoya.votopilas.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.crealoya.votopilas.R;
import com.crealoya.votopilas.adapter.PoliticianListAdapter;
import com.crealoya.votopilas.model.ListPosition;
import com.crealoya.votopilas.model.Party;
import com.crealoya.votopilas.model.Politician;
import com.crealoya.votopilas.ui.activity.PartyDetailsActivity;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

public class SearchPoliticiansListFragment extends Fragment implements View.OnClickListener {

    private static final String ARG_PARTY = "extra.party";
    private static final String ARG_QUERY_TYPE = "extra.query_type";

    private static final int QUERY_LIMIT = 75;

    private RecyclerView mRecyclerView;

    // models
    private Party mParty;

    // query type
    private QueryType mQueryType;

    // adapter
    private PoliticianListAdapter mAdapter;

    // listeners
    private PartyPoliticianListFragment.PartyPoliticianListener mListener;

    public static SearchPoliticiansListFragment newInstance(Party party, QueryType queryType) {
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARTY, party);
        args.putString(ARG_QUERY_TYPE, queryType.name());

        SearchPoliticiansListFragment fragment = new SearchPoliticiansListFragment();
        fragment.setArguments(args);

        return fragment;
    }

    public SearchPoliticiansListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_search_politicians_list, container, false);
        setHasOptionsMenu(true);

        // get arguments
        mParty = getArguments().getParcelable(ARG_PARTY);
        mParty = (Party) ParseObject.createWithoutData("Party", mParty.objectId);
        mQueryType = QueryType.valueOf(getArguments().getString(ARG_QUERY_TYPE));

        // find views
        mRecyclerView = (RecyclerView) view.findViewById(R.id.politician_recycler_view);

        // setup recycler view
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // fetch data if is necessary
        if (mQueryType == QueryType.ONE) {
            fetchPoliticiansByParty(mParty);
        }

        return view;
    }

    private void fetchPoliticians(ParseQuery<Politician> parseQuery) {
        // define Politician Parse Query - fetch all politicians of party
        final ParseQuery<ListPosition> listPositionParseQuery = ParseQuery.getQuery(ListPosition.class);
        listPositionParseQuery.whereMatchesKeyInQuery(ListPosition.COL_POLITICIAN, "objectId", parseQuery);
        listPositionParseQuery.include(ListPosition.COL_POLITICIAN);
        listPositionParseQuery.findInBackground(new FindCallback<ListPosition>() {
            @Override
            public void done(List<ListPosition> list, ParseException e) {
                if (e == null) {
                    for (ListPosition listPosition : list) {
                        listPosition.init();
                        listPosition.politician.init();
                        listPosition.charge.init();
                    }

                    // setup adapters
                    mAdapter = new PoliticianListAdapter(getActivity(), list, mListener);
                    mRecyclerView.setAdapter(mAdapter);
                } else {
                    // TODO: handle error
                }
            }
        });
    }

    private void fetchPoliticiansByParty(Party party) {
        final ParseQuery<Politician> politicianParseQuery = ParseQuery.getQuery(Politician.class);
        politicianParseQuery.whereEqualTo(Politician.COL_PARTY, mParty);
        politicianParseQuery.setLimit(QUERY_LIMIT);
        fetchPoliticians(politicianParseQuery);
    }

    private void fetchPoliticiansByAllParties(String politicianName) {
        final ParseQuery<Politician> politicianParseQuery = ParseQuery.getQuery(Politician.class);
        politicianParseQuery.whereContains("full_name", politicianName);
        fetchPoliticians(politicianParseQuery);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        final SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (mQueryType == QueryType.ALL) {
                    fetchPoliticiansByAllParties(newText);
                    return true;
                } else {
                    if (mAdapter != null) {
                        mAdapter.getFilter().filter(newText);
                    }
                    return true;
                }
            }
        });
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (PartyPoliticianListFragment.PartyPoliticianListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement PartyPoliticianListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.party_info) {
            PartyDetailsActivity.launch(getActivity(), mParty);
        }
    }

    public enum QueryType {
        ONE,
        ALL
    }
}
