package com.mhandharbeni.e_parking.apis.responses.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class DataKendaraan implements Serializable, Parcelable {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
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
    private Integer price;
    @SerializedName("type")
    @Expose
    private Integer type;
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
    private Integer total;
    @SerializedName("paidOptions")
    @Expose
    private Integer paidOptions;
    @SerializedName("dataQr")
    @Expose
    private String dataQr;
    @SerializedName("paid")
    @Expose
    private Integer paid;
    @SerializedName("isSync")
    @Expose
    private Integer isSync;

    protected DataKendaraan(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readInt();
        }
        createdAt = in.readString();
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
        if (in.readByte() == 0) {
            price = null;
        } else {
            price = in.readInt();
        }
        if (in.readByte() == 0) {
            type = null;
        } else {
            type = in.readInt();
        }
        date = in.readString();
        checkin = in.readString();
        checkout = in.readString();
        image = in.readString();
        if (in.readByte() == 0) {
            total = null;
        } else {
            total = in.readInt();
        }
        if (in.readByte() == 0) {
            paidOptions = null;
        } else {
            paidOptions = in.readInt();
        }
        dataQr = in.readString();
        if (in.readByte() == 0) {
            paid = null;
        } else {
            paid = in.readInt();
        }
        if (in.readByte() == 0) {
            isSync = null;
        } else {
            isSync = in.readInt();
        }
    }

    public static final Creator<DataKendaraan> CREATOR = new Creator<DataKendaraan>() {
        @Override
        public DataKendaraan createFromParcel(Parcel in) {
            return new DataKendaraan(in);
        }

        @Override
        public DataKendaraan[] newArray(int size) {
            return new DataKendaraan[size];
        }
    };

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

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

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
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

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getPaidOptions() {
        return paidOptions;
    }

    public void setPaidOptions(Integer paidOptions) {
        this.paidOptions = paidOptions;
    }

    public String getDataQr() {
        return dataQr;
    }

    public void setDataQr(String dataQr) {
        this.dataQr = dataQr;
    }

    public Integer getPaid() {
        return paid;
    }

    public void setPaid(Integer paid) {
        this.paid = paid;
    }

    public Integer getIsSync() {
        return isSync;
    }

    public void setIsSync(Integer isSync) {
        this.isSync = isSync;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        if (id == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(id);
        }
        parcel.writeString(createdAt);
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
        if (price == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(price);
        }
        if (type == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(type);
        }
        parcel.writeString(date);
        parcel.writeString(checkin);
        parcel.writeString(checkout);
        parcel.writeString(image);
        if (total == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(total);
        }
        if (paidOptions == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(paidOptions);
        }
        parcel.writeString(dataQr);
        if (paid == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(paid);
        }
        if (isSync == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(isSync);
        }
    }
}
