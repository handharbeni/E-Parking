package com.mhandharbeni.e_parking.apis.responses.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class DataParkirMasuk implements Serializable, Parcelable {
    @SerializedName("platNumber")
    @Expose
    private String platNumber;
    @SerializedName("ticketNumber")
    @Expose
    private String ticketNumber;
    @SerializedName("idUser")
    @Expose
    private Integer idUser;
    @SerializedName("nmrKawasan")
    @Expose
    private Integer nmrKawasan;
    @SerializedName("billNumber")
    @Expose
    private String billNumber;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("checkin")
    @Expose
    private String checkin;
    @SerializedName("checkout")
    @Expose
    private String checkout;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("total")
    @Expose
    private String total;
    @SerializedName("paidOptions")
    @Expose
    private String paidOptions;
    @SerializedName("dataQr")
    @Expose
    private String dataQr;
    @SerializedName("paid")
    @Expose
    private String paid;
    @SerializedName("isSync")
    @Expose
    private String isSync;

    protected DataParkirMasuk(Parcel in) {
        platNumber = in.readString();
        ticketNumber = in.readString();
        if (in.readByte() == 0) {
            idUser = null;
        } else {
            idUser = in.readInt();
        }
        if (in.readByte() == 0) {
            nmrKawasan = null;
        } else {
            nmrKawasan = in.readInt();
        }
        billNumber = in.readString();
        price = in.readString();
        type = in.readString();
        date = in.readString();
        checkin = in.readString();
        checkout = in.readString();
        image = in.readString();
        total = in.readString();
        paidOptions = in.readString();
        dataQr = in.readString();
        paid = in.readString();
        isSync = in.readString();
    }

    public static final Creator<DataParkirMasuk> CREATOR = new Creator<DataParkirMasuk>() {
        @Override
        public DataParkirMasuk createFromParcel(Parcel in) {
            return new DataParkirMasuk(in);
        }

        @Override
        public DataParkirMasuk[] newArray(int size) {
            return new DataParkirMasuk[size];
        }
    };

    public String getPlatNumber() {
        return platNumber;
    }

    public void setPlatNumber(String platNumber) {
        this.platNumber = platNumber;
    }

    public String getTicketNumber() {
        return ticketNumber;
    }

    public void setTicketNumber(String ticketNumber) {
        this.ticketNumber = ticketNumber;
    }

    public Integer getIdUser() {
        return idUser;
    }

    public void setIdUser(Integer idUser) {
        this.idUser = idUser;
    }

    public Integer getNmrKawasan() {
        return nmrKawasan;
    }

    public void setNmrKawasan(Integer nmrKawasan) {
        this.nmrKawasan = nmrKawasan;
    }

    public String getBillNumber() {
        return billNumber;
    }

    public void setBillNumber(String billNumber) {
        this.billNumber = billNumber;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCheckin() {
        return checkin;
    }

    public void setCheckin(String checkin) {
        this.checkin = checkin;
    }

    public String getCheckout() {
        return checkout;
    }

    public void setCheckout(String checkout) {
        this.checkout = checkout;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getPaidOptions() {
        return paidOptions;
    }

    public void setPaidOptions(String paidOptions) {
        this.paidOptions = paidOptions;
    }

    public String getDataQr() {
        return dataQr;
    }

    public void setDataQr(String dataQr) {
        this.dataQr = dataQr;
    }

    public String getPaid() {
        return paid;
    }

    public void setPaid(String paid) {
        this.paid = paid;
    }

    public String getIsSync() {
        return isSync;
    }

    public void setIsSync(String isSync) {
        this.isSync = isSync;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(platNumber);
        parcel.writeString(ticketNumber);
        if (idUser == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(idUser);
        }
        if (nmrKawasan == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(nmrKawasan);
        }
        parcel.writeString(billNumber);
        parcel.writeString(price);
        parcel.writeString(type);
        parcel.writeString(date);
        parcel.writeString(checkin);
        parcel.writeString(checkout);
        parcel.writeString(image);
        parcel.writeString(total);
        parcel.writeString(paidOptions);
        parcel.writeString(dataQr);
        parcel.writeString(paid);
        parcel.writeString(isSync);
    }
}
