package com.mhandharbeni.e_parking.apis.responses.data;

import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class DataQr implements Serializable, Parcelable {
    @SerializedName("totalAmount")
    @Expose
    private Integer totalAmount;
    @SerializedName("qrValue")
    @Expose
    private String qrValue;
    @SerializedName("billNumber")
    @Expose
    private String billNumber;
    @SerializedName("invoiceNumber")
    @Expose
    private String invoiceNumber;
    public final static Creator<DataQr> CREATOR = new Creator<DataQr>() {

        public DataQr createFromParcel(android.os.Parcel in) {
            return new DataQr(in);
        }

        public DataQr[] newArray(int size) {
            return (new DataQr[size]);
        }

    }
            ;
    private final static long serialVersionUID = -2081681847379655208L;

    protected DataQr(android.os.Parcel in) {
        this.totalAmount = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.qrValue = ((String) in.readValue((String.class.getClassLoader())));
        this.billNumber = ((String) in.readValue((String.class.getClassLoader())));
        this.invoiceNumber = ((String) in.readValue((String.class.getClassLoader())));
    }

    public DataQr() {
    }

    public Integer getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Integer totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getQrValue() {
        return qrValue;
    }

    public void setQrValue(String qrValue) {
        this.qrValue = qrValue;
    }

    public String getBillNumber() {
        return billNumber;
    }

    public void setBillNumber(String billNumber) {
        this.billNumber = billNumber;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeValue(totalAmount);
        dest.writeValue(qrValue);
        dest.writeValue(billNumber);
        dest.writeValue(invoiceNumber);
    }

    public int describeContents() {
        return 0;
    }
}
