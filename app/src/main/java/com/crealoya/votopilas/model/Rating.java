package com.crealoya.votopilas.model;

import android.os.Parcel;

import com.parse.ParseClassName;
import com.parse.ParseRelation;
import com.parse.ParseUser;

/**
 * Created by @pablopantaleon on 7/24/15.
 */
@ParseClassName("Rating")
public class Rating extends BaseParseObject {

    public String message;
    public int value;
    public Politician politician;
    public ParseUser user;

    @Override
    public void init() {
        super.init();
        message = getString("message");
        value = getInt("value");
        politician = (Politician) getParseObject("politician");
        user = getParseUser("user");
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.message);
        dest.writeInt(this.value);
    }

    public Rating() {
    }

    protected Rating(Parcel in) {
        super(in);
        this.message = in.readString();
        this.value = in.readInt();
    }

    public static final Creator<Rating> CREATOR = new Creator<Rating>() {
        public Rating createFromParcel(Parcel source) {
            return new Rating(source);
        }

        public Rating[] newArray(int size) {
            return new Rating[size];
        }
    };
}
