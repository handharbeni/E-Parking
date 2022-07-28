package com.mhandharbeni.e_parking.apis.responses.data;

import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class DataPrice implements Serializable, Parcelable {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("kategori_tarif")
    @Expose
    private String kategoriTarif;
    @SerializedName("motor")
    @Expose
    private Integer motor;
    @SerializedName("mobil")
    @Expose
    private Integer mobil;
    @SerializedName("bus_mini")
    @Expose
    private Integer busMini;
    @SerializedName("bus_besar")
    @Expose
    private Integer busBesar;
    public final static Creator<DataPrice> CREATOR = new Creator<DataPrice>() {

        public DataPrice createFromParcel(android.os.Parcel in) {
            return new DataPrice(in);
        }

        public DataPrice[] newArray(int size) {
            return (new DataPrice[size]);
        }

    };

    protected DataPrice(android.os.Parcel in) {
        this.id = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.kategoriTarif = ((String) in.readValue((String.class.getClassLoader())));
        this.motor = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.mobil = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.busMini = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.busBesar = ((Integer) in.readValue((Integer.class.getClassLoader())));
    }

    public DataPrice() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getKategoriTarif() {
        return kategoriTarif;
    }

    public void setKategoriTarif(String kategoriTarif) {
        this.kategoriTarif = kategoriTarif;
    }

    public Integer getMotor() {
        return motor;
    }

    public void setMotor(Integer motor) {
        this.motor = motor;
    }

    public Integer getMobil() {
        return mobil;
    }

    public void setMobil(Integer mobil) {
        this.mobil = mobil;
    }

    public Integer getBusMini() {
        return busMini;
    }

    public void setBusMini(Integer busMini) {
        this.busMini = busMini;
    }

    public Integer getBusBesar() {
        return busBesar;
    }

    public void setBusBesar(Integer busBesar) {
        this.busBesar = busBesar;
    }

    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(kategoriTarif);
        dest.writeValue(motor);
        dest.writeValue(mobil);
        dest.writeValue(busMini);
        dest.writeValue(busBesar);
    }

    public int describeContents() {
        return 0;
    }

}