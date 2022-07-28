package com.mhandharbeni.e_parking.apis.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ListResponse<T> {
    @SerializedName("error")
    @Expose
    private boolean error;

    @SerializedName("data")
    @Expose
    private List<T> data;

    @SerializedName("message")
    @Expose
    private String message;

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

