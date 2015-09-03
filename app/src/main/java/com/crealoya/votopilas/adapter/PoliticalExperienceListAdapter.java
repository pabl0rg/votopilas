package com.crealoya.votopilas.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.crealoya.votopilas.R;
import com.crealoya.votopilas.model.Education;
import com.crealoya.votopilas.model.PoliticalExperience;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by @pablopantaleon on 7/18/15.
 */
public class PoliticalExperienceListAdapter extends ArrayAdapter<PoliticalExperience> {

    private Context mContext;
    private ArrayList<PoliticalExperience> mItems;
    static class ViewHolderItem {
        public TextView charge;
        public TextView year;
        public TextView description;
    }

    public PoliticalExperienceListAdapter(Context context, ArrayList<PoliticalExperience> items) {
        super(context, 0, items);
        this.mContext = context;
        this.mItems = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolderItem viewHolder;

        if(convertView==null){
            // inflate the layout
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            convertView = inflater.inflate(R.layout.list_item_political_experience, parent, false);

            // well set up the ViewHolder
            viewHolder = new ViewHolderItem();
            viewHolder.charge = (TextView) convertView.findViewById(R.id.charge);
            viewHolder.year = (TextView) convertView.findViewById(R.id.year_end);
            viewHolder.description = (TextView) convertView.findViewById(R.id.description);

            // store the holder with the view.
            convertView.setTag(viewHolder);

        }else{
            // we've just avoided calling findViewById() on resource everytime
            // just use the viewHolder
            viewHolder = (ViewHolderItem) convertView.getTag();
        }

        PoliticalExperience politicalExperience= mItems.get(position);
        if (politicalExperience != null) {
            politicalExperience.init();

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy");
            viewHolder.charge.setText(politicalExperience.charge);
            if (politicalExperience.yearStart != null && politicalExperience.yearEnd != null) viewHolder.year.setText(simpleDateFormat.format(politicalExperience.yearStart) + " - " + simpleDateFormat.format(politicalExperience.yearEnd));
            viewHolder.description.setText(politicalExperience.description);
        }
        return convertView;
    }



}
