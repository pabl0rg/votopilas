package com.crealoya.votopilas.model;

import android.os.Parcel;

import com.parse.ParseClassName;

import java.util.Date;

/**
 * Created by @pablopantaleon on 7/24/15.
 */
@ParseClassName("Political_experience")
public class PoliticalExperience extends BaseParseObject {

    public int abstentionism;
    public int listPosition;

    public String charge;
    public String description;
    public String place;
    public String party;
    public boolean partyChange;

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

        party = getString("party");
        partyChange = getBoolean("party_change");

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
        dest.writeString(this.party);
        dest.writeByte((byte) (partyChange ? 1 : 0));
        dest.writeLong(yearEnd != null ? yearEnd.getTime() : -1);
        dest.writeLong(yearStart != null ? yearStart.getTime() : -1);
    }

    public PoliticalExperience() {
    }

    protected PoliticalExperience(Parcel in) {
        super(in);
        this.abstentionism = in.readInt();
        this.listPosition = in.readInt();
        this.charge = in.readString();
        this.description = in.readString();
        this.place = in.readString();
        this.party = in.readString();
        this.partyChange = in.readByte() != 0;
        long tmpYearEnd = in.readLong();
        this.yearEnd = tmpYearEnd == -1 ? null : new Date(tmpYearEnd);
        long tmpYearStart = in.readLong();
        this.yearStart = tmpYearStart == -1 ? null : new Date(tmpYearStart);
    }

    public static final Creator<PoliticalExperience> CREATOR = new Creator<PoliticalExperience>() {
        public PoliticalExperience createFromParcel(Parcel source) {
            return new PoliticalExperience(source);
        }

        public PoliticalExperience[] newArray(int size) {
            return new PoliticalExperience[size];
        }
    };
}
