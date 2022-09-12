package com.mhandharbeni.e_parking.apis.responses.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class DataParkirKeluar implements Serializable, Parcelable {
    protected DataParkirKeluar(Parcel in) {
    }

    public static final Creator<DataParkirKeluar> CREATOR = new Creator<DataParkirKeluar>() {
        @Override
        public DataParkirKeluar createFromParcel(Parcel in) {
            return new DataParkirKeluar(in);
        }

        @Override
        public DataParkirKeluar[] newArray(int size) {
            return new DataParkirKeluar[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
    }
}
