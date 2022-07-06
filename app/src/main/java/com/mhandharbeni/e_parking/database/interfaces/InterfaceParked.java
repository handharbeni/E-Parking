package com.mhandharbeni.e_parking.database.interfaces;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.mhandharbeni.e_parking.database.models.Parked;

import java.util.List;

@Dao
public interface InterfaceParked {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Parked parked);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<Parked> parkeds);

    @Update
    void update(Parked parked);

    @Update
    void update(List<Parked> listParked);

    @Delete
    void delete(Parked parked);

    @Delete
    void delete(List<Parked> listParked);

    @Query("SELECT * FROM parked")
    List<Parked> getList();

    @Query("SELECT * FROM parked")
    LiveData<List<Parked>> getLive();

    @Query("SELECT * FROM parked WHERE isSync = :isSync AND date >= :date AND checkOut != 0")
    LiveData<List<Parked>> getLive(boolean isSync, long date);

    @Query("SELECT * FROM parked WHERE id = :value")
    Parked getParkedById(int value);

    @Query("SELECT * FROM parked WHERE platNumber = :value AND isSync = :isSync")
    Parked getParkedByPlatNumber(String value, boolean isSync);

    @Query("SELECT * FROM parked WHERE platNumber = :platNo AND date = :date")
    Parked getParked(String platNo, long date);

    @Query("SELECT * FROM parked WHERE platNumber = :platNo AND checkOut = 0")
    Parked getParked(String platNo);

    @Query("SELECT * FROM parked WHERE isSync = :isSync")
    List<Parked> getParkedByMode(boolean isSync);
}
