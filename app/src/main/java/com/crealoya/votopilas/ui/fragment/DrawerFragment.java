package com.crealoya.votopilas.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;

import com.crealoya.votopilas.R;
import com.crealoya.votopilas.model.ListName;
import com.crealoya.votopilas.network.Callback;
import com.crealoya.votopilas.network.ParseHelper;
import com.crealoya.votopilas.ui.activity.SettingsActivity;
import com.crealoya.votopilas.util.UserPreferencesHelper;

import java.util.ArrayList;
import java.util.List;

public class DrawerFragment extends ListFragment implements View.OnClickListener {


    private OnDrawerInteractionListener mListener;
    private ProgressBar mProgress;
    private ArrayAdapter<ListName> mAdapter;

    public DrawerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_drawer, container, false);
        mProgress = (ProgressBar) root.findViewById(android.R.id.progress);
        root.findViewById(R.id.action_settings).setOnClickListener(this);
        return root;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getListView().setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mListener != null) {
                    getActivity().setTitle(getString(R.string.list) + " " + ((ListName) parent.getItemAtPosition(position)).name);
                    mListener.onListClicked((ListName) parent.getItemAtPosition(position));
                }
            }
        });

        ParseHelper.getListNames(new Callback<List<ListName>>() {
            @Override
            public void done(boolean success, List<ListName> data) {
                for (ListName listName : data) {
                    listName.init();
                }
                mAdapter = new ArrayAdapter(getActivity(), R.layout.simple_list_item_activated, data);
                getListView().setAdapter(mAdapter);

                final List<ListName> listNames = UserPreferencesHelper.getUserLists(new ArrayList(data));
                final int indexOfListName = data.indexOf(listNames.get(0));
                getListView().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getListView().setItemChecked(indexOfListName, true);
                        getActivity().setTitle(getString(R.string.list) + " " + listNames.get(0).name);
                    }
                }, 500);

                mProgress.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnDrawerInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.action_settings:
                startActivity(new Intent(getActivity(), SettingsActivity.class));
                break;
        }
    }

    public interface OnDrawerInteractionListener {
        void onListClicked(ListName list);
    }

}
