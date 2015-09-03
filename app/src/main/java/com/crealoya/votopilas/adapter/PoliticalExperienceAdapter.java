package com.crealoya.votopilas.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.crealoya.votopilas.R;
import com.crealoya.votopilas.model.PoliticalExperience;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by @pablopantaleon on 7/18/15.
 */
public class PoliticalExperienceAdapter extends RecyclerView.Adapter<PoliticalExperienceAdapter.PoliticalExperienceViewHolder> {

    private Context mContext;
    private List<PoliticalExperience> mItems;

    public PoliticalExperienceAdapter(Context context, List<PoliticalExperience> listSettings) {
        this.mContext = context;
        this.mItems = listSettings;
    }

    @Override
    public PoliticalExperienceViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).
                inflate(R.layout.list_item_political_experience, viewGroup, false);

        return new PoliticalExperienceViewHolder(view, mContext);
    }

    @Override
    public void onBindViewHolder(final PoliticalExperienceViewHolder politicianViewHolder, int position) {
        final PoliticalExperience item = mItems.get(position);
        politicianViewHolder.setItem(item);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public static class PoliticalExperienceViewHolder extends RecyclerView.ViewHolder {
        public TextView charge;
        public TextView year;
        public TextView description;

        public PoliticalExperienceViewHolder(View itemView, Context context) {
            super(itemView);
            charge = (TextView) itemView.findViewById(R.id.charge);
            year = (TextView) itemView.findViewById(R.id.year_end);
            description = (TextView) itemView.findViewById(R.id.description);
        }

        public void setItem(PoliticalExperience politicalExperience) {
            if (politicalExperience == null) return;
            politicalExperience.init();

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy");
            charge.setText(politicalExperience.charge);
            if (politicalExperience.yearStart != null && politicalExperience.yearEnd != null) year.setText(simpleDateFormat.format(politicalExperience.yearStart) + " - " + simpleDateFormat.format(politicalExperience.yearEnd));
            description.setText(politicalExperience.description);
        }
    }
}
