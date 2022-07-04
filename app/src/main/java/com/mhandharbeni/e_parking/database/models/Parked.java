package com.mhandharbeni.e_parking.database.models;

import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(indices = {@Index(value = {"id"}, unique = true)})
public class Parked {
    @PrimaryKey(autoGenerate = true)
    long id;

    String platNumber;
    String ticketNumber;
    int price;
    int type;
    long date;
    long checkIn;
    long checkOut;
    String image;
    boolean isSync;



    public Parked() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public void setTicketNumber(String ticket) {
        this.ticketNumber = ticket;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public long getCheckIn() {
        return checkIn;
    }

    public void setCheckIn(long checkIn) {
        this.checkIn = checkIn;
    }

    public long getCheckOut() {
        return checkOut;
    }

    public void setCheckOut(long checkOut) {
        this.checkOut = checkOut;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean isSync() {
        return isSync;
    }

    public void setSync(boolean sync) {
        isSync = sync;
    }
}
