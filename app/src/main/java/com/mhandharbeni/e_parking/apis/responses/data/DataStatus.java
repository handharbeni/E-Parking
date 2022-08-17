package com.mhandharbeni.e_parking.apis.responses.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class DataStatus implements Serializable, Parcelable {
    int status;

    public DataStatus() {
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    protected DataStatus(Parcel in) {
        status = in.readInt();
    }

    public static final Creator<DataStatus> CREATOR = new Creator<DataStatus>() {
        @Override
        public DataStatus createFromParcel(Parcel in) {
            return new DataStatus(in);
        }

        @Override
        public DataStatus[] newArray(int size) {
            return new DataStatus[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(status);
    }
}
