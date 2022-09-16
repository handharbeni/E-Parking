package com.mhandharbeni.e_parking.fragments;

import static android.content.Context.WINDOW_SERVICE;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.fragment.NavHostFragment;

import com.mhandharbeni.e_parking.R;
import com.mhandharbeni.e_parking.apis.responses.DataResponse;
import com.mhandharbeni.e_parking.apis.responses.data.DataQr;
import com.mhandharbeni.e_parking.apis.responses.data.DataStatus;
import com.mhandharbeni.e_parking.cores.BaseFragment;
import com.mhandharbeni.e_parking.database.models.Parked;
import com.mhandharbeni.e_parking.databinding.FragmentDetailPaymentQrBinding;
import com.mhandharbeni.e_parking.utils.Constant;
import com.skydoves.balloon.Balloon;
import com.skydoves.balloon.BalloonAnimation;
import com.skydoves.balloon.BalloonSizeSpec;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailPaymentQR extends BaseFragment {
    FragmentDetailPaymentQrBinding binding;
    Parked parked;
    DataQr dataQr;
    Balloon balloon;

    private int mInterval = 5000; // 5 seconds by default, can be changed later
    private Handler mHandler;

    Runnable mStatusChecker = () -> {
        try {
            // do check status
            checkStatus();
        } catch (Exception ignored) {
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentDetailPaymentQrBinding.inflate(inflater, container, false);
        navController = NavHostFragment.findNavController(this);
        mHandler = new Handler();

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
            parked.setPaid(false);
            parked.setPaidOptions(0);
            appDb.parked().update(parked);

            checkStatus();
        } catch (Exception ignored) {
        }
    }

    void setupTrigger() {
        binding.btnCheckPayment.setOnClickListener(view -> {
            binding.swipeRefresh.setRefreshing(true);
            checkStatus();
        });
    }

    void checkStatus() {
        clientInterface.getStatusPayment(parked.getBillNumber()).enqueue(new Callback<DataResponse<DataStatus>>() {
            @Override
            public void onResponse(@NonNull Call<DataResponse<DataStatus>> call, @NonNull Response<DataResponse<DataStatus>> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (!response.body().isError()) {
                            DataStatus dataStatus = response.body().getData();
                            // 0 false 1 true
                            if (dataStatus.getStatus() == 1) {
                                parked.setPaid(true);
                                appDb.parked().update(parked);

                                binding.ivPaid.setVisibility(View.VISIBLE);

                                binding.txtStatusPayment.setText(getResources().getString(R.string.paid));
                                binding.btnCheckPayment.setEnabled(false);

                                stopRepeatingTask();

                                showSuccess();
                            } else {
                                startRepeatingTask();
                            }
                        } else {
                            startRepeatingTask();
                        }
                    } else {
                        startRepeatingTask();
                    }
                } else {
                    startRepeatingTask();
                }
            }

            @Override
            public void onFailure(@NonNull Call<DataResponse<DataStatus>> call, @NonNull Throwable t) {
                startRepeatingTask();
            }
        });
    }


    void startRepeatingTask() {
        mHandler.postDelayed(mStatusChecker, mInterval);
        binding.swipeRefresh.setRefreshing(true);
    }

    void stopRepeatingTask() {
        mHandler.removeCallbacks(mStatusChecker);
        binding.swipeRefresh.setRefreshing(false);
    }

    public void showSuccess() {
        if (balloon != null) {
            balloon.dismiss();
            balloon = null;
        }
        balloon = new Balloon.Builder(requireContext())
                .setText("Payment Verified!\nYou can close this screen")
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
        balloon.showAlignBottom(binding.logoQris);
        balloon.setOnBalloonClickListener(view1 -> balloon.dismiss());
        balloon.setOnBalloonOverlayClickListener(balloon::dismiss);
    }
}
