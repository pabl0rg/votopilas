package com.crealoya.votopilas.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.crealoya.votopilas.R;
import com.crealoya.votopilas.model.Education;
import com.crealoya.votopilas.model.Rating;
import com.crealoya.votopilas.transformation.CircleTransformation;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by @pablopantaleon on 7/18/15.
 */
public class PoliticianRatingListAdapter extends ArrayAdapter<Rating> {

    private Context mContext;
    private ArrayList<Rating> mItems;
    static class ViewHolderItem {
        public ImageView photo;
        public TextView name;
        public TextView type;
        public TextView message;
        public RatingBar ratingBar;
    }

    public PoliticianRatingListAdapter(Context context, ArrayList<Rating> items) {
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
            convertView = inflater.inflate(R.layout.list_item_rating, parent, false);

            // well set up the ViewHolder
            viewHolder = new ViewHolderItem();
            viewHolder.photo = (ImageView) convertView.findViewById(R.id.rating_photo1);
            viewHolder.name = (TextView) convertView.findViewById(R.id.name);
            viewHolder.type = (TextView) convertView.findViewById(R.id.type);
            viewHolder.message = (TextView) convertView.findViewById(R.id.message);
            viewHolder.ratingBar = (RatingBar) convertView.findViewById(R.id.rating_politician);

            // store the holder with the view.
            convertView.setTag(viewHolder);

        }else{
            // we've just avoided calling findViewById() on resource everytime
            // just use the viewHolder
            viewHolder = (ViewHolderItem) convertView.getTag();
        }

        Rating rating = mItems.get(position);
        if (rating != null) {
            rating.init();

            if (rating.user.has("fbId")) {
                Glide.with(mContext)
                        .load(String.format(RatingsToApplyAdapter.FB_PROFILE_URL, rating.user.get("fbId")))
                        .transform(new CircleTransformation(mContext))
                        .crossFade()
                        .into(viewHolder.photo);
            }
            if (rating.user != null && rating.user.has("name")) viewHolder.name.setText(rating.user.getString("name"));
            else Log.e("PartyPolitician", "name no esta");
            if (rating.user != null && rating.user.has("vip") && rating.user.getBoolean("vip")) {
                viewHolder.type.setText("VIP");
            } else {
                viewHolder.type.setText("");
            }
            viewHolder.message.setText(rating.message);
            viewHolder.ratingBar.setRating(rating.value);
        }
        return convertView;
    }



}
