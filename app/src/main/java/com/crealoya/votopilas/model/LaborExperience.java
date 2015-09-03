package com.crealoya.votopilas.model;

import android.os.Parcel;

import com.parse.ParseClassName;

import java.util.Date;

/**
 * Created by @pablopantaleon on 7/24/15.
 */
@ParseClassName("Labor_experience")
public class LaborExperience extends BaseParseObject {

    public int abstentionism;
    public int listPosition;

    public String charge;
    public String description;
    public String place;

    public Date yearEnd;
    public Date yearStart;

    @Override
    public void init() {
        super.init();

        listPosition = getNumber("listPosition") != null ? getNumber("listPosition").intValue(): 0;
        abstentionism = getNumber("abstentionism") != null ? getNumber("abstentionism").intValue(): 0;

        charge = getString("charge");
        description = getString("description");
        place = getString("place");

        yearEnd = getDate("yearEnd");
        yearStart = getDate("yearStart");
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(this.abstentionism);
        dest.writeInt(this.listPosition);
        dest.writeString(this.charge);
        dest.writeString(this.description);
        dest.writeString(this.place);
        dest.writeLong(yearEnd != null ? yearEnd.getTime() : -1);
        dest.writeLong(yearStart != null ? yearStart.getTime() : -1);
    }

    public LaborExperience() {
    }

    protected LaborExperience(Parcel in) {
        super(in);
        this.abstentionism = in.readInt();
        this.listPosition = in.readInt();
        this.charge = in.readString();
        this.description = in.readString();
        this.place = in.readString();
        long tmpYearEnd = in.readLong();
        this.yearEnd = tmpYearEnd == -1 ? null : new Date(tmpYearEnd);
        long tmpYearStart = in.readLong();
        this.yearStart = tmpYearStart == -1 ? null : new Date(tmpYearStart);
    }

    public static final Creator<LaborExperience> CREATOR = new Creator<LaborExperience>() {
        public LaborExperience createFromParcel(Parcel source) {
            return new LaborExperience(source);
        }

        public LaborExperience[] newArray(int size) {
            return new LaborExperience[size];
        }
    };
}
