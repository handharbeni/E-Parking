package com.mhandharbeni.e_parking.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.fragment.NavHostFragment;

import com.mhandharbeni.e_parking.R;
import com.mhandharbeni.e_parking.apis.responses.DataResponse;
import com.mhandharbeni.e_parking.apis.responses.data.DataMe;
import com.mhandharbeni.e_parking.apis.responses.data.DataQr;
import com.mhandharbeni.e_parking.cores.BaseFragment;
import com.mhandharbeni.e_parking.database.models.Parked;
import com.mhandharbeni.e_parking.databinding.FragmentPaymentOptionsBinding;
import com.mhandharbeni.e_parking.utils.Constant;
import com.skydoves.balloon.Balloon;
import com.skydoves.balloon.BalloonAnimation;
import com.skydoves.balloon.BalloonSizeSpec;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
//            parked.setDate(System.currentTimeMillis());
//            parked.setCheckOut(System.currentTimeMillis());
//            parked.setPaid(true);
//            parked.setPaidOptions(0);
//            appDb.parked().update(parked);
//            showBallonSuccess();
            showBalloonError();
        });
        binding.btnQris.setOnClickListener(v -> {
            showLoading();
            DataMe me = getMe();
            // TODO CHANGE THE PRICE TO String.valueOf(parked.getPrice())
            String price = String.valueOf(parked.getPrice());
//            String price = "1";
            clientInterface.getQr(price, parked.getBillNumber()).enqueue(new Callback<DataResponse<DataQr>>() {
                @Override
                public void onResponse(@NonNull Call<DataResponse<DataQr>> call, @NonNull Response<DataResponse<DataQr>> response) {
                    if (response.isSuccessful()) {
                        if (response.body() != null) {
                            if (!response.body().isError()) {
                                doneLoading();
                                parked.setDataQr(response.body().getData().getQrValue());
                                appDb.parked().update(parked);

                                Bundle args = new Bundle();
                                args.putSerializable(Constant.KEY_DETAIL_TIKET, parked);
                                args.putSerializable(Constant.KEY_DETAIL_QR, response.body().getData());
                                navigate(R.id.action_options_to_detailqr, args);
                            } else {
                                showBalloonError(response.body().getMessage());
                                doneLoading();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<DataResponse<DataQr>> call, @NonNull Throwable t) {
                    showBalloonError("Something went wrong, please try again!");
                    doneLoading();
                }
            });
        });
        binding.btnTapCash.setOnClickListener(v -> showBalloonError());
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
        balloon.setOnBalloonDismissListener(() -> navigate(R.id.action_options_to_main));
    }

    void showBalloonError() {
        if (balloon != null) {
            balloon.dismiss();
            balloon = null;
        }
        balloon = new Balloon.Builder(requireContext())
                .setLayout(R.layout.popup_inprogress)
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

    void showBalloonError(String message) {
        if (balloon != null) {
            balloon.dismiss();
            balloon = null;
        }
        balloon = new Balloon.Builder(requireContext())
                .setText(message)
                .setIsVisibleArrow(false)
                .setWidth(BalloonSizeSpec.WRAP)
                .setHeight(BalloonSizeSpec.WRAP)
                .setPadding(20)
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
