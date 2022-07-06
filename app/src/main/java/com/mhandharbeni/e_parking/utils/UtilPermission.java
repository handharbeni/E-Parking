package com.mhandharbeni.e_parking.utils;

import android.content.Context;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;

public class UtilPermission {
    public static boolean checkPermission(Context context) {
        boolean granted = true;
        for (String s : Constant.LIST_PERMISSION) {
            if (ActivityCompat.checkSelfPermission(context, s)!= PackageManager.PERMISSION_GRANTED) {
                granted = false;
            }
        }
        return granted;
    }
}
