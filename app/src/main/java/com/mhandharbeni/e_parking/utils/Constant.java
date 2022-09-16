package com.mhandharbeni.e_parking.utils;

import android.Manifest;
import android.os.Build;

import java.util.ArrayList;
import java.util.Arrays;

public class Constant {
    public static final String BASE_URL = "https://e-parkir.batukota.go.id/android/e-parkir/v1/";
//    public static final String BASE_URL = "http://66.96.229.251:84/API/e-parkir/v1/";
    public static final String BASE_IMAGE = "https://via.placeholder.com/100.png?text=";
//    public static final String UUID = "0b000001-0000-0001-1010-000000";
    public static final String UUID = "00001101-0000-1000-8000-00805F9B34FB";
    public static final String DB_NAME = "EParkDB";
    public static final int DB_VERSION = 2;
    public static final boolean DB_EXPORT = false;
    public static final int RC_PERMISSION = 123;
    public static final int REQUEST_CHECK_SETTINGS = 111;
    public static final String BLUETOOTH_SCAN_REQUEST = "BLUETOOTH_SCAN_REQUEST";
    public static final String BLUETOOTH_CONNECT_REQUEST = "BLUETOOTH_CONNECT_REQUEST";
    public static final String BLUETOOTH_CONNECT_STATUS = "BLUETOOTH_CONNECT_STATUS";
    public static final String BLUETOOTH_PRINT = "BLUETOOTH_PRINT";
    public static final String BLUETOOTH_SEND_COMMAND = "BLUETOOTH_SEND_COMMAND";
    public static final String BLUETOOTH_DEVICE = "BLUETOOTH_DEVICE";
    public static final String BLUETOOTH_CONNECTED = "BLUETOOTH_CONNECTED";
    public static final String BLUETOOTH_CONNECTED_STRING = "BLUETOOTH_CONNECTED_STRING";
    public static final String REQUEST_PERMISSION = "REQUEST_PERMISSION";
    public static final ArrayList<String> LIST_PERMISSION = new ArrayList<>(
            Arrays.asList(
                    Manifest.permission.BLUETOOTH,
                    Manifest.permission.BLUETOOTH_ADMIN,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA,
                    Manifest.permission.VIBRATE
            )
    );
    public static final int REQUEST_CODE_ENABLE_BLUETOOTH = 200;
    public static final int REQUEST_CODE_PERMISSION = 300;
    public static int REQUEST_CODE = 0;

    public static String KEY_DETAIL_TIKET = "com.mhandharbeni.e_parking.fragments.DETAIL_TIKET";
    public static String KEY_DETAIL_QR = "com.mhandharbeni.e_parking.fragments.DETAIL_QR";

    public static final String FETCH_DATA = "com.mhandharbeni.e_parking.cores.BaseFragment.FETCH_DATA";

    public static final String TOKEN = "com.mhandharbeni.e_parking.apis.TOKEN";
    public static final String ME = "com.mhandharbeni.e_parking.apis.ME";
    public static final String PRICE = "com.mhandharbeni.e_parking.apis.PRICE";
    public static final String STATS = "com.mhandharbeni.e_parking.apis.STATS";

    public static final String CI_USERNAME = "USERNAME";
    public static final String CI_PASSWORD = "PASSWORD";

    public static final String CI_PLATNUMBER = "platNumber";
    public static final String CI_TICKETNUMBER = "ticketNumber";
    public static final String CI_BILLNUMBER = "billNumber";
    public static final String CI_PRICE = "price";
    public static final String CI_TYPE = "type";
    public static final String CI_DATE = "date";
    public static final String CI_CHECKIN = "checkin";
    public static final String CI_CHECKOUT = "checkout";
    public static final String CI_IMAGE = "image";
    public static final String CI_TOTAL = "total";
    public static final String CI_PAIDOPTIONS = "paidOptions";
    public static final String CI_DATAQR = "dataQr";
    public static final String CI_PAID = "paid";
    public static final String CI_ISSYNC = "isSync";

    static {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            LIST_PERMISSION.add(Manifest.permission.BLUETOOTH_CONNECT);
            LIST_PERMISSION.add(Manifest.permission.BLUETOOTH_SCAN);
        }
    }
}
