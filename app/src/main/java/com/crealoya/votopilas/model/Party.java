package com.crealoya.votopilas.model;

import android.os.Parcel;

import com.parse.ParseClassName;
import com.parse.ParseFile;

import java.util.Date;

/**
 * Created by @pablopantaleon on 7/20/15.
 */
@ParseClassName("Party")
public class Party extends BaseParseObject{

    public int foundation;
    public int number_of_offices;

    public String objectId;
    public String address;
    public String image;
    public String name;
    public String shortname;
    public String phone;
    public String president_name;
    public String description;
    public String facebook;
    public String twitter;
    public String website;

    public Party() {
    }

    @Override
    public void init() {
        super.init();
        objectId = getObjectId();

        if (isDataAvailable()) {
            address = getString("address");
            foundation = getNumber("foundation").intValue();
            number_of_offices = getNumber("number_of_offices").intValue();
            name = getString("name");
            president_name = getString("president_name");
            description = getString("description");
            shortname = getString("shortname");
            phone = getString("phone");
            facebook = getString("facebook");
            twitter = getString("twitter");
            website = getString("website");

            final ParseFile parseFile = getParseFile("image");

            if (parseFile != null) {
                image = parseFile.getUrl();
            }
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.objectId);
        dest.writeString(this.address);
        dest.writeInt(this.foundation);
        dest.writeInt(this.number_of_offices);
        dest.writeString(this.image);
        dest.writeString(this.name);
        dest.writeString(this.shortname);
        dest.writeString(this.phone);
        dest.writeString(this.president_name);
        dest.writeString(this.description);
        dest.writeString(this.facebook);
        dest.writeString(this.twitter);
        dest.writeString(this.website);
        dest.writeLong(createdAt != null ? createdAt.getTime() : -1);
        dest.writeLong(updatedAt != null ? updatedAt.getTime() : -1);
    }

    protected Party(Parcel in) {
        super(in);
        this.objectId = in.readString();
        this.address = in.readString();
        this.foundation = in.readInt();
        this.number_of_offices = in.readInt();
        this.image = in.readString();
        this.name = in.readString();
        this.shortname = in.readString();
        this.phone = in.readString();
        this.president_name = in.readString();
        this.description = in.readString();
        this.facebook = in.readString();
        this.twitter = in.readString();
        this.website = in.readString();
        long tmpCreatedAt = in.readLong();
        this.createdAt = tmpCreatedAt == -1 ? null : new Date(tmpCreatedAt);
        long tmpUpdatedAt = in.readLong();
        this.updatedAt = tmpUpdatedAt == -1 ? null : new Date(tmpUpdatedAt);
        // setObjectId(this.objectId);
    }

    public static final Creator<Party> CREATOR = new Creator<Party>() {
        public Party createFromParcel(Parcel source) {
            return new Party(source);
        }

        public Party[] newArray(int size) {
            return new Party[size];
        }
    };
}
