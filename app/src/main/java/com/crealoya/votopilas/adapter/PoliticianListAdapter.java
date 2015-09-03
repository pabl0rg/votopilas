package com.crealoya.votopilas.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.crealoya.votopilas.R;
import com.crealoya.votopilas.model.ListPosition;
import com.crealoya.votopilas.model.Politician;
import com.crealoya.votopilas.model.Rating;
import com.crealoya.votopilas.network.Callback;
import com.crealoya.votopilas.network.ParseCloudHelper;
import com.crealoya.votopilas.ui.fragment.PartyPoliticianListFragment;
import com.crealoya.votopilas.util.GlideCircleTransform;
import com.crealoya.votopilas.util.StringManager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by @pablopantaleon on 7/18/15.
 */
public class PoliticianListAdapter extends RecyclerView.Adapter<PoliticianListAdapter.PoliticianViewHolder>
    implements Filterable {

    private ConcurrentHashMap<String, RatingBar> mPoliticiansHash = new ConcurrentHashMap<>();

    private Context mContext;
    private List<ListPosition> mItems;
    private List<ListPosition> mFilteredItems;
    private PartyPoliticianListFragment.PartyPoliticianListener mListener;

    public PoliticianListAdapter(Context context, List<ListPosition> listSettings, PartyPoliticianListFragment.PartyPoliticianListener listener) {
        this.mContext = context;
        this.mItems = listSettings;
        this.mFilteredItems = listSettings;
        this.mListener = listener;
    }

    @Override
    public PoliticianViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).
                inflate(R.layout.view_item_politician, viewGroup, false);

        return new PoliticianViewHolder(view, mContext, mListener);
    }

    @Override
    public void onBindViewHolder(final PoliticianViewHolder politicianViewHolder, int position) {
        final ListPosition listPosition = mFilteredItems.get(position);
        politicianViewHolder.setPolitician(listPosition.politician);

        // set politician image
        Glide.with(mContext).load(listPosition.politician.image)
                .error(R.drawable.ic_vp_placeholder_persona)
                .placeholder(R.drawable.ic_vp_placeholder_persona)
                .transform(new GlideCircleTransform(mContext))
                .into(politicianViewHolder.photo);

        if (listPosition.alternate){
            String alternate = mContext.getString(R.string.alternate);
            politicianViewHolder.charge.setText(listPosition.politician.charge.name + " " + alternate);
        } else {
            politicianViewHolder.charge.setText(listPosition.politician.charge.name);
        }

        politicianViewHolder.rating.setRating(0);
        politicianViewHolder.rating.setTag(listPosition.objectId);
        mPoliticiansHash.put(listPosition.objectId, politicianViewHolder.rating);

        politicianViewHolder.politicianListPosition.setText(String.valueOf(listPosition.position) + ".");
        ParseCloudHelper.getRatingByUsersAvg(listPosition.politician, politicianViewHolder.rating, new Callback<List<Rating>>() {
            @Override
            public void done(boolean success, List<Rating> list) {
                final RatingBar tempRatingBar = mPoliticiansHash.get(listPosition.objectId);
                if (tempRatingBar.getTag() == listPosition.objectId) {
                    int sum = 0;
                    for (Rating rating : list) {
                        sum += rating.getInt("value");
                    }
                    int total = sum / list.size();
                    tempRatingBar.setRating(total);
                } else {
                    tempRatingBar.setRating(0);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mFilteredItems.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                if (constraint == null || constraint.length() == 0) {
                    results.values = mItems;
                    results.count = mItems.size();
                }
                else {
                    ArrayList<ListPosition> filtered = new ArrayList<>();
                    for (ListPosition listPosition : mItems) {
                        if (listPosition.politician.fullname.contains(constraint.toString())) {
                            // if `contains` == true then add it
                            // to our filtered list
                            filtered.add(listPosition);
                        }
                    }

                    // Finally set the filtered values and size/count
                    results.values = filtered;
                    results.count = filtered.size();
                }

                // Return our FilterResults object
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                mFilteredItems = (ArrayList<ListPosition>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public static class PoliticianViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener{
        public ImageView photo;
        public TextView name;
        public TextView charge;
        public TextView politicianListPosition;
        public RatingBar rating;
        public Politician politician;
        public Context context;
        public PartyPoliticianListFragment.PartyPoliticianListener listener;

        public PoliticianViewHolder(View itemView, Context context, PartyPoliticianListFragment.PartyPoliticianListener listener) {
            super(itemView);

            this.context = context;
            this.photo = (ImageView) itemView.findViewById(R.id.politician_photo);
            this.name = (TextView) itemView.findViewById(R.id.politician_name);
            this.charge = (TextView) itemView.findViewById(R.id.politician_charge);
            this.politicianListPosition = (TextView) itemView.findViewById(R.id.politician_list_position);
            this.rating = (RatingBar) itemView.findViewById(R.id.rating_politician);

            this.listener = listener;
            itemView.setOnClickListener(this);
        }

        public void setPolitician(Politician politician) {
            this.politician = politician;
            this.name.setText(StringManager.getFullName(politician));
        }
        @Override
        public void onClick(View view) {
            listener.onPoliticianSelected(politician);
        }
    }
}
