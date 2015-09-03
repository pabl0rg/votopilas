package com.crealoya.votopilas.model;

import android.os.Parcel;

import com.parse.ParseClassName;

/**
 * Created by @pablopantaleon on 7/24/15.
 */
@ParseClassName("List")
public class ListName extends BaseParseObject {

    public String name;

    @Override
    public void init() {
        super.init();
        name = getString("name");
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.name);
    }

    public ListName() {
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof String) {
            if (o.equals(name) && name == null) {
                return true;
            } else {
                return false;
            }
        } else {
            return super.equals(o);
        }
    }

    protected ListName(Parcel in) {
        super(in);
        this.name = in.readString();
    }

    public static final Creator<ListName> CREATOR = new Creator<ListName>() {
        public ListName createFromParcel(Parcel source) {
            return new ListName(source);
        }

        public ListName[] newArray(int size) {
            return new ListName[size];
        }
    };

    @Override
    public String toString() {
        return name;
    }
}
