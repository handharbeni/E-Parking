package com.mhandharbeni.e_parking.fragments;

import static android.content.Context.WINDOW_SERVICE;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.fragment.NavHostFragment;

import com.mhandharbeni.e_parking.apis.responses.data.DataQr;
import com.mhandharbeni.e_parking.cores.BaseFragment;
import com.mhandharbeni.e_parking.database.models.Parked;
import com.mhandharbeni.e_parking.databinding.FragmentDetailPaymentQrBinding;
import com.mhandharbeni.e_parking.utils.Constant;
import com.skydoves.balloon.Balloon;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class DetailPaymentQR extends BaseFragment {
    FragmentDetailPaymentQrBinding binding;
    Parked parked;
    DataQr dataQr;
    Balloon balloon;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentDetailPaymentQrBinding.inflate(inflater, container, false);

        navController = NavHostFragment.findNavController(this);

        setupBundle();

        return binding.getRoot();
    }

    void setupBundle() {
        Bundle args = getArguments();
        if (args != null) {
            parked = (Parked) args.getSerializable(Constant.KEY_DETAIL_TIKET);
            dataQr = (DataQr) args.getSerializable(Constant.KEY_DETAIL_QR);
            setupTrigger();
            setupData();
        }
    }

    void setupData() {
        WindowManager manager = (WindowManager) requireActivity().getSystemService(WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        int width = point.x;
        int height = point.y;

        int smallerDimension = Math.min(width, height);
        smallerDimension = smallerDimension * 3 / 4;
        QRGEncoder qrgEncoder = new QRGEncoder(dataQr.getQrValue(), null, QRGContents.Type.TEXT, smallerDimension);
        qrgEncoder.setColorBlack(Color.BLACK);
        qrgEncoder.setColorWhite(Color.WHITE);
        try {
            Bitmap bitmap = qrgEncoder.getBitmap();
            glideManager.load(bitmap).into(binding.imgQR);

            parked.setDate(System.currentTimeMillis());
            parked.setCheckOut(System.currentTimeMillis());
            parked.setPaid(true);
            parked.setPaidOptions(0);
            appDb.parked().update(parked);
        } catch (Exception ignored) {
        }
    }

    void setupTrigger() {

    }


}
