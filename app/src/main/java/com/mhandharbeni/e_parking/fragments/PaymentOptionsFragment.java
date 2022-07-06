package com.mhandharbeni.e_parking.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.fragment.NavHostFragment;

import com.mhandharbeni.e_parking.R;
import com.mhandharbeni.e_parking.cores.BaseFragment;
import com.mhandharbeni.e_parking.database.models.Parked;
import com.mhandharbeni.e_parking.databinding.FragmentPaymentOptionsBinding;
import com.mhandharbeni.e_parking.utils.Constant;
import com.skydoves.balloon.Balloon;
import com.skydoves.balloon.BalloonAnimation;
import com.skydoves.balloon.BalloonSizeSpec;

public class PaymentOptionsFragment extends BaseFragment {
    FragmentPaymentOptionsBinding binding;
    Parked parked;

    Balloon balloon;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentPaymentOptionsBinding.inflate(inflater, container, false);

        navController = NavHostFragment.findNavController(this);

        setupBundle();

        return binding.getRoot();
    }


    void setupBundle() {
        Bundle args = getArguments();
        if (args != null) {
            parked = (Parked) args.getSerializable(Constant.KEY_DETAIL_TIKET);
            setupTrigger();
        }
    }

    void setupTrigger() {
        binding.btnCash.setOnClickListener(v -> {
            parked.setDate(System.currentTimeMillis());
            parked.setCheckOut(System.currentTimeMillis());
            parked.setPaid(true);
            parked.setPaidOptions(0);
            appDb.parked().update(parked);
            showBallonSuccess();
        });
        binding.btnQris.setOnClickListener(v -> {
            showBalloonError();
        });
        binding.btnTapCash.setOnClickListener(v -> {
            showBalloonError();
        });
    }

    void showBallonSuccess() {
        if (balloon != null) {
            balloon.dismiss();
            balloon = null;
        }
        balloon = new Balloon.Builder(requireContext())
                .setLayout(R.layout.popup_success)
                .setIsVisibleArrow(false)
                .setWidth(BalloonSizeSpec.WRAP)
                .setHeight(BalloonSizeSpec.WRAP)
                .setTextSize(15f)
                .setCornerRadius(4f)
                .setAlpha(0.9f)
                .setBalloonAnimation(BalloonAnimation.ELASTIC)
                .setLifecycleOwner(getViewLifecycleOwner())
                .build();
        balloon.showAlignBottom(binding.btnTapCash);
        balloon.setOnBalloonClickListener(view1 -> balloon.dismiss());
        balloon.setOnBalloonOverlayClickListener(balloon::dismiss);
        balloon.setOnBalloonDismissListener(() -> {
            navigate(R.id.action_options_to_main);
        });
    }

    void showBalloonError() {
        if (balloon != null) {
            balloon.dismiss();
            balloon = null;
        }
        balloon = new Balloon.Builder(requireContext())
                .setLayout(R.layout.popup_error)
                .setIsVisibleArrow(false)
                .setWidth(BalloonSizeSpec.WRAP)
                .setHeight(BalloonSizeSpec.WRAP)
                .setTextSize(15f)
                .setCornerRadius(4f)
                .setAlpha(0.9f)
                .setBalloonAnimation(BalloonAnimation.ELASTIC)
                .setLifecycleOwner(getViewLifecycleOwner())
                .build();

        balloon.showAlignBottom(binding.btnTapCash);
        balloon.setOnBalloonClickListener(view1 -> balloon.dismiss());
        balloon.setOnBalloonOverlayClickListener(balloon::dismiss);
    }
}
