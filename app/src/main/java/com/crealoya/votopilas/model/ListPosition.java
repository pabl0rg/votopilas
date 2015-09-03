package com.crealoya.votopilas.model;

import android.os.Parcel;

import com.parse.ParseClassName;

/**
 * Created by @pablopantaleon on 7/24/15.
 */
@ParseClassName("List_position")
public class ListPosition extends BaseParseObject {

    public static final String COL_LIST = "list";
    public static final String COL_POLITICIAN = "politician";
    public static final String COL_CHARGE = "charge";
    public static final String COL_ALTERNATE = "alternate";

    public int position;
    public Charge charge;
    public ListName list;
    public boolean alternate;
    public Politician politician;

    @Override
    public void init() {
        super.init();
        position = getNumber("position").intValue();

        charge = (Charge) getParseObject(COL_CHARGE);
        list = (ListName) getParseObject(COL_LIST);
        politician = (Politician) getParseObject(COL_POLITICIAN);
        alternate = getBoolean(COL_ALTERNATE);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(this.position);
        dest.writeParcelable(this.charge, 0);
        dest.writeParcelable(this.list, 0);
        dest.writeInt(this.alternate? 1 : 0);
        //dest.writeParcelable(this.politician, 0);
    }

    public ListPosition() {
    }

    protected ListPosition(Parcel in) {
        super(in);
        this.position = in.readInt();
        this.charge = in.readParcelable(Charge.class.getClassLoader());
        this.list = in.readParcelable(ListName.class.getClassLoader());
        this.alternate = in.readInt() == 1? true : false;
        //this.politician = in.readParcelable(Politician.class.getClassLoader());
    }

    public static final Creator<ListPosition> CREATOR = new Creator<ListPosition>() {
        public ListPosition createFromParcel(Parcel source) {
            return new ListPosition(source);
        }

        public ListPosition[] newArray(int size) {
            return new ListPosition[size];
        }
    };
}
