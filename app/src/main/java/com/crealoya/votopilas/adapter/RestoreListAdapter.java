package com.crealoya.votopilas.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.crealoya.votopilas.R;
import com.crealoya.votopilas.model.ListName;
import com.crealoya.votopilas.model.Party;
import com.crealoya.votopilas.util.UserPreferencesHelper;

import java.util.List;

/**
 * Created by @pablopantaleon on 7/30/15.
 */
public class RestoreListAdapter<T> extends RecyclerView.Adapter<RestoreListAdapter.RestoreViewHolder> {

    private Context mContext;
    public List<T> mItems;
    private boolean mIsParty;

    private SparseArray<Boolean> enableSwitch;
    private boolean mDidSomethingChange;

    public RestoreListAdapter(Context context, List<T> items) {
        this.mContext = context;
        this.mItems = items;
        this.enableSwitch = new SparseArray<>();
        mDidSomethingChange = false;
    }

    @Override
    public RestoreViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.list_item_restore, parent, false);

        return new RestoreViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RestoreViewHolder holder, final int position) {
        Object object = mItems.get(position);
        String data = "";
        mIsParty = false;

        if (object instanceof ListName) {
            ((ListName) object).init();
            data = ((ListName) object).name;
        } else if (object instanceof Party) {
            mIsParty = true;
            ((Party) object).init();
            data = ((Party) object).shortname + " - " + ((Party) object).name;
        }

        enableSwitch.put(position, false);

        holder.name.setText(data);
        final String finalData = data;
        final boolean finalIsParty = mIsParty;
//        holder.aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (enableSwitch.get(position)) {
//                    mDidSomethingChange = true;
//                    if (isChecked) {
//                        if (finalIsParty) {
//                            UserPreferencesHelper.addToHiddenParties(finalData);
//                        } else {
//                            UserPreferencesHelper.addToShowLists(finalData);
//                        }
//                    } else {
//                        if (finalIsParty) {
//                            UserPreferencesHelper.removeFromHiddenParties(finalData);
//                        } else {
//                            UserPreferencesHelper.removeFromShowLists(finalData);
//                        }
//                    }
//                }
//            }
//        });

//        if (mIsParty) {
//            holder.aSwitch.setChecked(UserPreferencesHelper.contains((Party) object));
//        } else {
//            holder.aSwitch.setChecked(UserPreferencesHelper.contains((ListName) object));
//        }
        enableSwitch.put(position, true);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public static class RestoreViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public SwitchCompat aSwitch;

        public RestoreViewHolder(View itemView) {
            super(itemView);
            this.name = (TextView) itemView.findViewById(R.id.restore_txt);
            this.aSwitch = (SwitchCompat) itemView.findViewById(R.id.restore_switch);
        }
    }

    public boolean isParty() {
        return mIsParty;
    }

    public boolean didSomethingChange() {
        return mDidSomethingChange;
    }
}
