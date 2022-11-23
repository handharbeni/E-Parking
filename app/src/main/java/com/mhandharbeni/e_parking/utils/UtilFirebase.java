package com.mhandharbeni.e_parking.utils;

import android.annotation.SuppressLint;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mhandharbeni.e_parking.apis.responses.data.DataMe;

import java.util.HashMap;

public class UtilFirebase {
    @SuppressLint("StaticFieldLeak")
    private static FirebaseFirestore db;

    public static void setOnline(DataMe dataMe, OnCompleteListener onCompleteListener) {
        db = FirebaseFirestore.getInstance();
        if (dataMe != null) {
            HashMap<String, String> hashMapMe = new HashMap<>();
            hashMapMe.put("jenis", dataMe.getNik());
            hashMapMe.put("golongan", dataMe.getNik());
            hashMapMe.put("thn_register", dataMe.getNik());
            hashMapMe.put("kecamatan", dataMe.getNik());
            hashMapMe.put("npwrd", dataMe.getNik());
            hashMapMe.put("nik", dataMe.getNik());
            hashMapMe.put("nama", dataMe.getNik());
            hashMapMe.put("nama_lengkap", dataMe.getNik());
            hashMapMe.put("nm_kelurahan", dataMe.getNik());
            hashMapMe.put("nm_kecamatan", dataMe.getNik());
            hashMapMe.put("no_hp", dataMe.getNik());
            hashMapMe.put("lokasi_parkir", dataMe.getNik());
            hashMapMe.put("nmr_kawasan", dataMe.getNik());
            hashMapMe.put("status", "Online");

            db.collection("EParkUser").document(dataMe.getNik()).set(hashMapMe).addOnCompleteListener(onCompleteListener);
        } else {
            HashMap<String, String> hashMapMe = new HashMap<>();
            hashMapMe.put("TESTING", "TESTING");
            hashMapMe.put("status", "Online");
            db.collection("EParkUser").document("Testing123").set(hashMapMe).addOnCompleteListener(onCompleteListener);
        }
    }

    public static void setOffline(DataMe dataMe, OnCompleteListener onCompleteListener) {
        db = FirebaseFirestore.getInstance();
        if (dataMe != null) {
            HashMap<String, String> hashMapMe = new HashMap<>();
            hashMapMe.put("jenis", dataMe.getNik());
            hashMapMe.put("golongan", dataMe.getNik());
            hashMapMe.put("thn_register", dataMe.getNik());
            hashMapMe.put("kecamatan", dataMe.getNik());
            hashMapMe.put("npwrd", dataMe.getNik());
            hashMapMe.put("nik", dataMe.getNik());
            hashMapMe.put("nama", dataMe.getNik());
            hashMapMe.put("nama_lengkap", dataMe.getNik());
            hashMapMe.put("nm_kelurahan", dataMe.getNik());
            hashMapMe.put("nm_kecamatan", dataMe.getNik());
            hashMapMe.put("no_hp", dataMe.getNik());
            hashMapMe.put("lokasi_parkir", dataMe.getNik());
            hashMapMe.put("nmr_kawasan", dataMe.getNik());
            hashMapMe.put("status", "Offline");

            db.collection("EParkUser").document(dataMe.getNik()).set(hashMapMe).addOnCompleteListener(onCompleteListener);
        } else {
            HashMap<String, String> hashMapMe = new HashMap<>();
            hashMapMe.put("TESTING", "TESTING");
            hashMapMe.put("status", "Online");
            db.collection("EParkUser").document("Testing123").set(hashMapMe).addOnCompleteListener(onCompleteListener);
        }
    }
}
