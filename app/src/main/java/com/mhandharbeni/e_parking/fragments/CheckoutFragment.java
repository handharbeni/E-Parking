package com.mhandharbeni.e_parking.fragments;

import static android.content.Context.VIBRATOR_SERVICE;

import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.fragment.NavHostFragment;

import com.mhandharbeni.e_parking.R;
import com.mhandharbeni.e_parking.apis.responses.DataResponse;
import com.mhandharbeni.e_parking.apis.responses.data.DataKendaraan;
import com.mhandharbeni.e_parking.cores.BaseFragment;
import com.mhandharbeni.e_parking.database.models.Parked;
import com.mhandharbeni.e_parking.databinding.FragmentCheckoutBinding;
import com.mhandharbeni.e_parking.utils.Constant;
import com.skydoves.balloon.Balloon;
import com.skydoves.balloon.BalloonAnimation;
import com.skydoves.balloon.BalloonSizeSpec;

import java.util.Objects;

import cn.bingoogolapple.qrcode.core.QRCodeView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckoutFragment extends BaseFragment implements QRCodeView.Delegate {
    private final String TAG = CheckoutFragment.class.getSimpleName();

    FragmentCheckoutBinding binding;
    Parked parked;

    Balloon balloon;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentCheckoutBinding.inflate(inflater, container, false);

        navController = NavHostFragment.findNavController(this);
        binding.imageKendaraan.setDelegate(this);


        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupTrigger();
    }

    @Override
    public void onStart() {
        super.onStart();
        new Handler().postDelayed(() -> binding.imageKendaraan.startSpotAndShowRect(), 2000);
        binding.imageKendaraan.startCamera();
    }

    @Override
    public void onScanQRCodeSuccess(String result) {
        binding.imageKendaraan.stopSpot();

        showLoading();
        vibrate();
        try {
            String[] aResult = result.split(",_,");
            String billNumber = aResult[0];
            String date = aResult[1];
            Objects.requireNonNull(binding.edtPlatNomor.getEditText()).setText(aResult[0]);

            clientInterface.getKendaraan(billNumber).enqueue(new Callback<DataResponse<DataKendaraan>>() {
                @Override
                public void onResponse(@NonNull Call<DataResponse<DataKendaraan>> call, @NonNull Response<DataResponse<DataKendaraan>> response) {
                    doneLoading();
                    if (response.isSuccessful()) {
                        if (response.body() != null) {
                            if (!response.body().isError()) {
                                if (response.body().getData() != null) {
                                    DataKendaraan dataKendaraan = response.body().getData();

                                    Bundle args = new Bundle();
                                    parked = new Parked();
                                    parked.setPlatNumber(dataKendaraan.getPlatNumber());
                                    parked.setTicketNumber(dataKendaraan.getTicketNumber());
                                    parked.setBillNumber(dataKendaraan.getBillNumber());
                                    parked.setPrice(dataKendaraan.getPrice());
                                    parked.setType(dataKendaraan.getType());
                                    parked.setDate(Long.parseLong(dataKendaraan.getDate()));
                                    parked.setCheckIn(Long.parseLong(dataKendaraan.getCheckin()));
                                    parked.setCheckOut(Long.parseLong(dataKendaraan.getCheckout()));
                                    parked.setImage(dataKendaraan.getImage());
                                    parked.setTotal(dataKendaraan.getTotal());
                                    parked.setPaidOptions(dataKendaraan.getPaidOptions());
                                    parked.setDataQr(dataKendaraan.getDataQr());
                                    parked.setPaid(dataKendaraan.getPaid()==0);
                                    parked.setSync(dataKendaraan.getIsSync()==0);
                                    args.putBoolean(Constant.KEY_DETAIL_PURPOSE, false);
                                    args.putSerializable(Constant.KEY_DETAIL_TIKET, parked);
                                    navigate(R.id.action_checkout_to_detailpayment, args);
                                } else {
                                    showBaloonError();
                                }
                            } else {
                                showBaloonError();
                            }
                        }
                    } else {
                        showBaloonError();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<DataResponse<DataKendaraan>> call, @NonNull Throwable t) {
                    doneLoading();
                    showBaloonError();
                }
            });
        } catch (Exception ignored) {
            showBaloonError();
        } finally {
            new Handler().postDelayed(() -> binding.imageKendaraan.startSpotAndShowRect(), 2000);
        }
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
                args.putBoolean(Constant.KEY_DETAIL_PURPOSE, false);
                args.putSerializable(Constant.KEY_DETAIL_TIKET, parked);
                navigate(R.id.action_checkout_to_detailpayment, args);
            } else {
                parked = appDb.parked().getParked(Objects.requireNonNull(binding.edtPlatNomor.getEditText()).getText().toString());
                if (parked != null) {
                    Bundle args = new Bundle();
                    args.putBoolean(Constant.KEY_DETAIL_PURPOSE, false);
                    args.putSerializable(Constant.KEY_DETAIL_TIKET, parked);
                    navigate(R.id.action_checkout_to_detailpayment, args);
                } else {
                    showBaloonError();
                }
            }
        });
    }

    void showBaloonError() {
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

        balloon.showAlignBottom(binding.panelCamera);
        balloon.setOnBalloonClickListener(view1 -> balloon.dismiss());
        balloon.setOnBalloonOverlayClickListener(balloon::dismiss);
    }
}
