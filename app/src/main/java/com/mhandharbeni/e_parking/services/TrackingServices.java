package com.mhandharbeni.e_parking.services;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.mhandharbeni.e_parking.apis.responses.data.DataMe;
import com.mhandharbeni.e_parking.utils.Constant;
import com.mhandharbeni.e_parking.utils.UtilDb;
import com.mhandharbeni.e_parking.utils.UtilFirebase;

public class TrackingServices extends Service {

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Gson gson = new Gson();

        UtilDb utilDb = new UtilDb(getApplicationContext());
        DataMe dataMe = gson.fromJson(utilDb.getString(Constant.ME), DataMe.class);
        UtilFirebase.setOnline(dataMe, task -> {});

        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {

        super.onTaskRemoved(rootIntent);

        Gson gson = new Gson();
        UtilDb utilDb = new UtilDb(getApplicationContext());
        DataMe dataMe = gson.fromJson(utilDb.getString(Constant.ME), DataMe.class);
        UtilFirebase.setOffline(dataMe, task -> {});

        this.stopSelf();
    }
}
