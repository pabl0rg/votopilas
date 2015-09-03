package com.crealoya.votopilas.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.crealoya.votopilas.R;
import com.crealoya.votopilas.adapter.PoliticianListAdapter;
import com.crealoya.votopilas.adapter.SectionedListAdapter;
import com.crealoya.votopilas.model.ListName;
import com.crealoya.votopilas.model.ListPosition;
import com.crealoya.votopilas.model.Party;
import com.crealoya.votopilas.model.Politician;
import com.crealoya.votopilas.network.Callback;
import com.crealoya.votopilas.network.ParseHelper;
import com.crealoya.votopilas.ui.activity.PartyDetailsActivity;
import com.crealoya.votopilas.util.DialogUtil;
import com.crealoya.votopilas.util.UserPreferencesHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

/**
 * Created by Pablo Reyes, @pablopantaleon on 6/28/15.
 */
public class PartyPoliticianListFragment extends Fragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    private static final String EXTRA_PARTY = "extra.party";

    // UI components
    private ProgressBar mProgress;
    private RecyclerView mRecyclerView;
//    private SwipeRefreshLayout mSwipeRefreshLayout;
    private TextView mRemoveParty;
    private View mEmptyView;

    // models
    private Party mParty;

    // listeners
    private PartyPoliticianListener mListener;

    public static PartyPoliticianListFragment newInstance(Party party) {
        Bundle args = new Bundle();
        args.putParcelable(EXTRA_PARTY, party);

        PartyPoliticianListFragment fragment = new PartyPoliticianListFragment();
        fragment.setArguments(args);

        return fragment;
    }

    public PartyPoliticianListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_party_politician_list, container, false);

        // get arguments
        mParty = getArguments().getParcelable(EXTRA_PARTY);
//        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);

        // find views
        mRecyclerView = (RecyclerView) view.findViewById(android.R.id.list);
        mProgress = (ProgressBar) view.findViewById(android.R.id.progress);
        mRemoveParty = (TextView) view.findViewById(R.id.ic_remove_party);
        mEmptyView = view.findViewById(R.id.empty_view);

        // listeners
        view.findViewById(R.id.party_info).setOnClickListener(this);
//        mSwipeRefreshLayout.setOnRefreshListener(this);
        mRemoveParty.setOnClickListener(this);

        // setup recycler view
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        onRefresh();
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (PartyPoliticianListener) activity;
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
        } else if (v.getId() == R.id.ic_remove_party) {
            DialogUtil.simpleDialog(getActivity(), DialogUtil.HIDE_PARTY, new DialogUtil.DialogListener() {
                @Override
                public void onClose(boolean isAccepted) {
                    if (isAccepted) {
                        mListener.removeParty(mParty);
                    }
                }
            });
        }
    }

    public Party getParty() {
        return mParty;
    }

    @Override
    public void onRefresh() {
        // enable progress bar
        mEmptyView.setVisibility(View.GONE);
        mProgress.setVisibility(View.VISIBLE);

        ParseHelper.getListNames(new Callback<List<ListName>>() {
            @Override
            public void done(boolean success, List<ListName> data) {
                if (success) {
                    final List<ListName> allListNames = UserPreferencesHelper.getUserLists(data);
                    ParseHelper.getListPositions(new Callback<List<ListPosition>>() {
                        @Override
                        public void done(boolean success, List<ListPosition> list) {
                            if (getActivity() == null) {
                                return;
                            } else if (list.isEmpty()) {
                                mEmptyView.setVisibility(View.VISIBLE);
                                mProgress.setVisibility(View.GONE);
                                return;
                            } else {
                                mEmptyView.setVisibility(View.GONE);
                            }

                            if (success) {
                                final TreeMap<String, List<ListPosition>> listHashMap = new TreeMap<>();

                                // add all list names to hashmap
                                for (ListName listName : allListNames) {
                                    listHashMap.put(listName.name, new ArrayList<ListPosition>());
                                }

                                // add all lists
                                for (ListPosition listPosition : list) {
                                    // initialize list values
                                    listPosition.init();
                                    listPosition.list.init();
                                    listPosition.politician.init();

                                    // update list name in the HashMap
                                    final List<ListPosition> politicianList = listHashMap.get(listPosition.list.name);
                                    politicianList.add(listPosition);
                                    listHashMap.put(listPosition.list.name, politicianList);
                                }

                                // Construct sections
                                final List<SectionedListAdapter.Section> sections = new ArrayList<>();
                                final List<ListPosition> allPoliticiansSorted = new ArrayList<>();
                                int sectionCount = 0;

                                // Add sections
                                for (String key : listHashMap.keySet()) {
                                    List<ListPosition> listPositions = listHashMap.get(key);

                                    // create section only if list is not empty
                                    if (!listPositions.isEmpty()) {
                                        sections.add(new SectionedListAdapter.Section(sectionCount, key));
                                        sectionCount += listPositions.size();
                                        allPoliticiansSorted.addAll(listPositions);
                                    }
                                }

                                // setup adapters
                                final PoliticianListAdapter adapter = new PoliticianListAdapter(getActivity(), allPoliticiansSorted, mListener);

                                // add adapter to section adapter
                                SectionedListAdapter mSectionedAdapter = new SectionedListAdapter(getActivity(),
                                        R.layout.view_section,
                                        R.id.section_text,
                                        adapter,
                                        new SectionedListAdapter.SectionListener() {
                                            @Override
                                            public void onSectionRemoved(int start, int end) {
                                                // if the user remove one list fetch data again
                                                mProgress.setVisibility(View.VISIBLE);
                                                onRefresh();
                                            }
                                        });

                                // apply section adapter to the RecyclerView
                                mSectionedAdapter.setSections(sections.toArray(new SectionedListAdapter.Section[sections.size()]));
                                mRecyclerView.setAdapter(mSectionedAdapter);
                                mProgress.setVisibility(View.GONE);
                            } else {
                                mProgress.setVisibility(View.GONE);
                                // TODO: handle error here

                            }
                        }
                    }, data, mParty);
                } else {
                    // TODO: handle error here
                }
            }
        });

//        mSwipeRefreshLayout.setRefreshing(false);
    }

    public interface PartyPoliticianListener {
        void onPoliticianSelected(Politician politician);

        void removeParty(Party party);
    }
}
