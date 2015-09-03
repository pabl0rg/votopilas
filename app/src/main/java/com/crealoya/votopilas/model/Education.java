package com.crealoya.votopilas.model;

import android.os.Parcel;

import com.parse.ParseClassName;

import java.util.Date;

/**
 * Created by @pablopantaleon on 7/24/15.
 */
@ParseClassName("Education")
public class Education extends BaseParseObject {

    public String description;
    public String place;
    public String title;

    public Date yearEnd;
    public Date yearStart;

    @Override
    public void init() {
        super.init();
        description = getString("description");
        place = getString("place");
        title = getString("title");
        yearEnd = getDate("year_end");
        yearStart = getDate("year_start");
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.description);
        dest.writeString(this.place);
        dest.writeString(this.title);
        dest.writeLong(yearEnd != null ? yearEnd.getTime() : -1);
        dest.writeLong(yearStart != null ? yearStart.getTime() : -1);
    }

    public Education() {
    }

    protected Education(Parcel in) {
        super(in);
        this.description = in.readString();
        this.place = in.readString();
        this.title = in.readString();
        long tmpYearEnd = in.readLong();
        this.yearEnd = tmpYearEnd == -1 ? null : new Date(tmpYearEnd);
        long tmpYearStart = in.readLong();
        this.yearStart = tmpYearStart == -1 ? null : new Date(tmpYearStart);
    }

    public static final Creator<Education> CREATOR = new Creator<Education>() {
        public Education createFromParcel(Parcel source) {
            return new Education(source);
        }

        public Education[] newArray(int size) {
            return new Education[size];
        }
    };
}
