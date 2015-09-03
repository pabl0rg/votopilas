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
import com.crealoya.votopilas.model.LaborExperience;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by @pablopantaleon on 7/18/15.
 */
public class LaborExperienceListAdapter extends ArrayAdapter<LaborExperience> {

    private Context mContext;
    private ArrayList<LaborExperience> mItems;
    static class ViewHolderItem {
        public TextView charge;
        public TextView year;
        public TextView place;
        public TextView description;
    }

    public LaborExperienceListAdapter(Context context, ArrayList<LaborExperience> items) {
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
            convertView = inflater.inflate(R.layout.list_item_laboral_experience, parent, false);

            // well set up the ViewHolder
            viewHolder = new ViewHolderItem();
            viewHolder.charge = (TextView) convertView.findViewById(R.id.charge);
            viewHolder.year = (TextView) convertView.findViewById(R.id.year_end);
            viewHolder.place = (TextView) convertView.findViewById(R.id.place);
            viewHolder.description = (TextView) convertView.findViewById(R.id.description);

            // store the holder with the view.
            convertView.setTag(viewHolder);

        }else{
            // we've just avoided calling findViewById() on resource everytime
            // just use the viewHolder
            viewHolder = (ViewHolderItem) convertView.getTag();
        }

        LaborExperience item = mItems.get(position);
        if (item != null) {
            item.init();

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy");
            viewHolder.charge.setText(item.charge);
            viewHolder.year.setText(simpleDateFormat.format(item.yearStart) + " - " + simpleDateFormat.format(item.yearEnd));
            viewHolder.place.setText(item.place);
            viewHolder.description.setText(item.description);
        }
        return convertView;
    }



}
