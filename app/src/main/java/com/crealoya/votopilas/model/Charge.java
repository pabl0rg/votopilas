package com.crealoya.votopilas.model;

import android.os.Parcel;

import com.parse.GetCallback;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;

/**
 * Created by @pablopantaleon on 7/24/15.
 */
@ParseClassName("Charge")
public class Charge extends BaseParseObject {

    public String name;

    @Override
    public void init() {
        super.init();

        if (has("name")) {
            name = getString("name");
        } else {
            fetchInBackground(new GetCallback<ParseObject>() {
                @Override
                public void done(ParseObject parseObject, ParseException e) {
                    name = parseObject.getString("name");
                }
            });
        }
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

    public Charge() {
    }

    protected Charge(Parcel in) {
        super(in);
        this.name = in.readString();
    }

    public static final Creator<Charge> CREATOR = new Creator<Charge>() {
        public Charge createFromParcel(Parcel source) {
            return new Charge(source);
        }

        public Charge[] newArray(int size) {
            return new Charge[size];
        }
    };
}
