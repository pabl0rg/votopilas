package com.crealoya.votopilas.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.crealoya.votopilas.R;
import com.crealoya.votopilas.transformation.CircleTransformation;
import com.parse.ParseObject;

import java.util.List;

/**
 * Created by JOSUE on 09-Jul-15.
 */
public class RatingsToApplyAdapter extends ArrayAdapter<ParseObject>
{
    public final static String FB_PROFILE_URL = "https://graph.facebook.com/v2.1/%s/picture?width=270&height=270";
    List<ParseObject> mUsers;
    int resLayout;
    Context context;


    public RatingsToApplyAdapter(Context context, int textViewResourceId, List<ParseObject> users) {
        super(context, textViewResourceId, users);
        this.mUsers = users;
        resLayout = textViewResourceId;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        Holder holder = null;
        if(convertView == null)
        {
            holder = new Holder();
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(resLayout, parent, false);
            holder.mImage = (ImageView) convertView.findViewById(R.id.img);
            holder.mName = (TextView) convertView.findViewById(R.id.label_name);
            holder.mInstitution = (TextView) convertView.findViewById(R.id.label_institution);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        ParseObject user = getItem(position);

        holder.mName.setText(user.getString("name"));

        if (user.has("fbId")) {
            Glide.with(context)
                    .load(String.format(FB_PROFILE_URL, user.get("fbId")))
                    .transform(new CircleTransformation(context))
                    .crossFade()
                    .into(holder.mImage);
        }

        return convertView;
    }

    private class Holder {
        ImageView mImage;
        TextView mName;
        TextView mInstitution;
    }
}