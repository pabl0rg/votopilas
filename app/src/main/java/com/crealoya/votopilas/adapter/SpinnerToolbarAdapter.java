package com.crealoya.votopilas.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

/**
 * Created by @pablopantaleon on 7/21/15.
 */
public class SpinnerToolbarAdapter extends ArrayAdapter<String> {

    public SpinnerToolbarAdapter(Context context, @LayoutRes int resource) {
        super(context, resource);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return super.getView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return super.getDropDownView(position, convertView, parent);
    }
}
