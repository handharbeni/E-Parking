package com.mhandharbeni.e_parking.apis.responses.data;

import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class DataLostTicket implements Serializable, Parcelable {
    @SerializedName("billNumber")
    @Expose
    private String billNumber;
    @SerializedName("platNumber")
    @Expose
    private String platNumber;
    @SerializedName("fotoKtp")
    @Expose
    private String fotoKtp;
    @SerializedName("fotoKendaraan")
    @Expose
    private String fotoKendaraan;
    @SerializedName("idUser")
    @Expose
    private Integer idUser;
    public final static Creator<DataLostTicket> CREATOR = new Creator<DataLostTicket>() {
        public DataLostTicket createFromParcel(android.os.Parcel in) {
            return new DataLostTicket(in);
        }

        public DataLostTicket[] newArray(int size) {
            return (new DataLostTicket[size]);
        }
    };
    private final static long serialVersionUID = -8008334887522900795L;

    protected DataLostTicket(android.os.Parcel in) {
        this.billNumber = ((String) in.readValue((String.class.getClassLoader())));
        this.platNumber = ((String) in.readValue((String.class.getClassLoader())));
        this.fotoKtp = ((String) in.readValue((String.class.getClassLoader())));
        this.fotoKendaraan = ((String) in.readValue((String.class.getClassLoader())));
        this.idUser = ((Integer) in.readValue((Integer.class.getClassLoader())));
    }

    public DataLostTicket() {
    }

    public String getBillNumber() {
        return billNumber;
    }

    public void setBillNumber(String billNumber) {
        this.billNumber = billNumber;
    }

    public String getPlatNumber() {
        return platNumber;
    }

    public void setPlatNumber(String platNumber) {
        this.platNumber = platNumber;
    }

    public String getFotoKtp() {
        return fotoKtp;
    }

    public void setFotoKtp(String fotoKtp) {
        this.fotoKtp = fotoKtp;
    }

    public String getFotoKendaraan() {
        return fotoKendaraan;
    }

    public void setFotoKendaraan(String fotoKendaraan) {
        this.fotoKendaraan = fotoKendaraan;
    }

    public Integer getIdUser() {
        return idUser;
    }

    public void setIdUser(Integer idUser) {
        this.idUser = idUser;
    }

    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeValue(billNumber);
        dest.writeValue(platNumber);
        dest.writeValue(fotoKtp);
        dest.writeValue(fotoKendaraan);
        dest.writeValue(idUser);
    }

    public int describeContents() {
        return 0;
    }
}
