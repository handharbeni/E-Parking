package com.mhandharbeni.e_parking.fragments;

import static android.content.Context.VIBRATOR_SERVICE;

import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.mhandharbeni.e_parking.databinding.FragmentCheckoutBinding;

import java.util.Objects;

import cn.bingoogolapple.qrcode.core.QRCodeView;

public class CheckoutFragment extends Fragment implements QRCodeView.Delegate {
    private final String TAG = CheckoutFragment.class.getSimpleName();

    FragmentCheckoutBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentCheckoutBinding.inflate(inflater, container, false);
        binding.imageKendaraan.setDelegate(this);

        binding.imageKendaraan.startCamera();
        binding.imageKendaraan.startSpotAndShowRect();
        return binding.getRoot();
    }

    @Override
    public void onScanQRCodeSuccess(String result) {
        vibrate();
        Log.d(TAG, "onScanQRCodeSuccess: " + result);
        try {
            String[] aResult = result.split(",_,");
            Objects.requireNonNull(binding.edtPlatNomor.getEditText()).setText(aResult[0]);
        } catch (Exception ignored) {}
        binding.imageKendaraan.startSpot();
    }

    @Override
    public void onCameraAmbientBrightnessChanged(boolean isDark) {

    }

    @Override
    public void onScanQRCodeOpenCameraError() {

    }

    @Override
    public void onStop() {
        binding.imageKendaraan.stopCamera();
        super.onStop();
    }

    @Override
    public void onDestroy() {
        binding.imageKendaraan.onDestroy();
        super.onDestroy();
    }

    private void vibrate() {
        Vibrator vibrator = (Vibrator) requireActivity().getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(200);
    }
}
