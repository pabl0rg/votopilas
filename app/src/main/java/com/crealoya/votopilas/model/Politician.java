package com.crealoya.votopilas.model;

import android.os.Parcel;

import com.parse.ParseClassName;
import com.parse.ParseFile;

import java.util.Date;
import java.util.List;

/**
 * Created by @pablopantaleon on 7/18/15.
 */
@ParseClassName("Politician")
public class Politician extends BaseParseObject {
    public static final String PARSE_POLITICIAN_OBJECT = "parse_politician_object";

    public static final String COL_PARTY = "party";
    public static final String COL_LIST = "list";
    public static final String COL_CHARGE = "charge";
    public static final String COL_EDUCATION = "education";
    public static final String COL_LABOR_EXP = "labor_experience";
    public static final String COL_LIST_POS = "list_position";
    public static final String COL_POLITICAL_EXP = "political_experience";

    public Date birthday;

    public String description;
    public String image;
    public String lastname;
    public String name;
    public String fullname;

    public Party party;
    public Charge charge;
    public List<String> links;
    public ListPosition listPosition;

    public Politician() {
        // empty required constructor
    }

    @Override
    public void init() {
        super.init();
        birthday = getDate("birthday");
        description = getString("description");
        name = getString("name");
        lastname = getString("lastname");
        links = getList("links");
        fullname = getString("full_name");

        charge = (Charge) getParseObject(COL_CHARGE);
        listPosition = (ListPosition) getParseObject(COL_LIST_POS);
        party = (Party) getParseObject(COL_PARTY);

        if (charge != null) {
            charge.init();
        }

        if (listPosition != null && listPosition.isDataAvailable()) {
            listPosition.init();
        }

        if (party != null && listPosition.isDataAvailable()) {
            party.init();
        }

        // get parse objects
        final ParseFile parseFile = getParseFile("image");
        if (parseFile != null) {
            image = parseFile.getUrl();
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeLong(birthday != null ? birthday.getTime() : -1);
        dest.writeString(this.description);
        dest.writeString(this.image);
        dest.writeString(this.lastname);
        dest.writeString(this.name);
        dest.writeString(this.objectId);
        dest.writeString(this.fullname);
        dest.writeParcelable(this.party, 0);
        dest.writeParcelable(this.charge, 0);
        dest.writeStringList(this.links);
        dest.writeParcelable(this.listPosition, 0);
    }

    protected Politician(Parcel in) {
        super(in);
        long tmpBirthday = in.readLong();
        this.birthday = tmpBirthday == -1 ? null : new Date(tmpBirthday);
        this.description = in.readString();
        this.image = in.readString();
        this.lastname = in.readString();
        this.name = in.readString();
        this.objectId = in.readString();
        this.fullname = in.readString();
        this.party = in.readParcelable(Party.class.getClassLoader());
        this.charge = in.readParcelable(Charge.class.getClassLoader());
        this.links = in.createStringArrayList();
        this.listPosition = in.readParcelable(ListPosition.class.getClassLoader());
    }

    public static final Creator<Politician> CREATOR = new Creator<Politician>() {
        public Politician createFromParcel(Parcel source) {
            return new Politician(source);
        }

        public Politician[] newArray(int size) {
            return new Politician[size];
        }
    };
}
