package com.crealoya.votopilas.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.parse.ParseObject;

import java.util.Date;

/**
 * Created by @pablopantaleon on 7/25/15.
 */
public abstract class BaseParseObject extends ParseObject implements Parcelable {

    // TODO: maybe we can remove objectId (use instead setObjectId() and getObjectId())
    public String objectId;

    public Date createdAt;
    public Date updatedAt;

    public void init() {
        objectId = getObjectId();
        createdAt = getCreatedAt();
        updatedAt = getUpdatedAt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.objectId);
        dest.writeLong(createdAt != null ? createdAt.getTime() : -1);
        dest.writeLong(updatedAt != null ? updatedAt.getTime() : -1);
    }

    public BaseParseObject() {
    }

    protected BaseParseObject(Parcel in) {
        this.objectId = in.readString();
        long tmpCreatedAt = in.readLong();
        this.createdAt = tmpCreatedAt == -1 ? null : new Date(tmpCreatedAt);
        long tmpUpdatedAt = in.readLong();
        this.updatedAt = tmpUpdatedAt == -1 ? null : new Date(tmpUpdatedAt);
    }
}
