package com.mhandharbeni.e_parking.fragments;

import static android.content.Context.VIBRATOR_SERVICE;

import android.os.Bundle;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.fragment.NavHostFragment;

import com.mhandharbeni.e_parking.R;
import com.mhandharbeni.e_parking.cores.BaseFragment;
import com.mhandharbeni.e_parking.database.models.Parked;
import com.mhandharbeni.e_parking.databinding.FragmentCheckoutBinding;
import com.mhandharbeni.e_parking.utils.Constant;

import java.util.Objects;

import cn.bingoogolapple.qrcode.core.QRCodeView;

public class CheckoutFragment extends BaseFragment implements QRCodeView.Delegate {
    private final String TAG = CheckoutFragment.class.getSimpleName();

    FragmentCheckoutBinding binding;
    Parked parked;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentCheckoutBinding.inflate(inflater, container, false);

        navController = NavHostFragment.findNavController(this);

        binding.imageKendaraan.setDelegate(this);
        binding.imageKendaraan.startCamera();
        binding.imageKendaraan.startSpotAndShowRect();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupTrigger();
    }

    @Override
    public void onScanQRCodeSuccess(String result) {
        vibrate();
        try {
            String[] aResult = result.split(",_,");
            String platNo = aResult[0];
            String date = aResult[1];
            Objects.requireNonNull(binding.edtPlatNomor.getEditText()).setText(aResult[0]);
            parked = appDb.parked().getParked(platNo, Long.parseLong(date));
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

    void setupTrigger() {
        binding.btnNext.setOnClickListener(v -> {
            if (parked != null) {
                Bundle args = new Bundle();
                args.putSerializable(Constant.KEY_DETAIL_TIKET, parked);
                navigate(R.id.action_checkout_to_detailpayment, args);
            }
        });
    }
}
