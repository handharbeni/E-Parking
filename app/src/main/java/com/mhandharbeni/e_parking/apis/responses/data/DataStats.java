package com.mhandharbeni.e_parking.apis.responses.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class DataStats implements Serializable, Parcelable {
    @SerializedName("jumlahTransaksi")
    @Expose
    int jumlahTransaksi;
    @SerializedName("totalTransaksi")
    @Expose
    String totalTransaksi;

    public DataStats() {
    }

    protected DataStats(Parcel in) {
        jumlahTransaksi = in.readInt();
        totalTransaksi = in.readString();
    }

    public static final Creator<DataStats> CREATOR = new Creator<DataStats>() {
        @Override
        public DataStats createFromParcel(Parcel in) {
            return new DataStats(in);
        }

        @Override
        public DataStats[] newArray(int size) {
            return new DataStats[size];
        }
    };

    public int getJumlahTransaksi() {
        return jumlahTransaksi;
    }

    public void setJumlahTransaksi(int jumlahTransaksi) {
        this.jumlahTransaksi = jumlahTransaksi;
    }

    public String getTotalTransaksi() {
        return totalTransaksi;
    }

    public void setTotalTransaksi(String totalTransaksi) {
        this.totalTransaksi = totalTransaksi;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(jumlahTransaksi);
        parcel.writeString(totalTransaksi);
    }
}
