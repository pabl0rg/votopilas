package com.crealoya.votopilas.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.crealoya.votopilas.R;
import com.crealoya.votopilas.model.Education;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by @pablopantaleon on 7/18/15.
 */
public class EducationListAdapter extends ArrayAdapter<Education> {

    private Context mContext;
    private ArrayList<Education> mItems;
    static class ViewHolderItem {
        public TextView title;
        public TextView year;
        public TextView description;
    }

    public EducationListAdapter(Context context, ArrayList<Education> educations) {
        super(context, 0, educations);
        this.mContext = context;
        this.mItems = educations;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolderItem viewHolder;

        if(convertView==null){
            // inflate the layout
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            convertView = inflater.inflate(R.layout.list_item_education, parent, false);

            // well set up the ViewHolder
            viewHolder = new ViewHolderItem();
            viewHolder.title = (TextView) convertView.findViewById(R.id.title);
            viewHolder.year = (TextView) convertView.findViewById(R.id.year_end);
            viewHolder.description = (TextView) convertView.findViewById(R.id.description);

            // store the holder with the view.
            convertView.setTag(viewHolder);

        }else{
            // we've just avoided calling findViewById() on resource everytime
            // just use the viewHolder
            viewHolder = (ViewHolderItem) convertView.getTag();
        }

        Education item = mItems.get(position);
        if (item != null) {
            item.init();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy");
            viewHolder.title.setText(item.title);
            viewHolder.year.setText(simpleDateFormat.format(item.yearEnd));
            viewHolder.description.setText(item.description);
        }
        return convertView;
    }



}
