package com.mhandharbeni.e_parking.fragments;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.navigation.fragment.NavHostFragment;

import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.mhandharbeni.e_parking.R;
import com.mhandharbeni.e_parking.apis.responses.DataResponse;
import com.mhandharbeni.e_parking.apis.responses.data.DataKendaraan;
import com.mhandharbeni.e_parking.apis.responses.data.DataLostTicket;
import com.mhandharbeni.e_parking.cores.BaseFragment;
import com.mhandharbeni.e_parking.database.models.Parked;
import com.mhandharbeni.e_parking.databinding.FragmentSearchPlatBinding;
import com.mhandharbeni.e_parking.utils.Constant;
import com.mhandharbeni.e_parking.utils.UtilPermission;
import com.priyankvasa.android.cameraviewex.CameraView;
import com.priyankvasa.android.cameraviewex.Image;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;

import kotlin.Unit;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentLostTicket extends BaseFragment {
    private final String TAG = FragmentLostTicket.class.getSimpleName();
    FragmentSearchPlatBinding binding;
    CameraView cameraViewKtp;
    CameraView cameraViewKendaraan;

    private final Matrix matrix = new Matrix();

    Parked parked;

    String imageKtp;
    String imageKendaraan;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSearchPlatBinding.inflate(inflater, container, false);

        navController = NavHostFragment.findNavController(this);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initTrigger();
    }

    private void initTrigger() {
        binding.svPlatNumber.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchPlat(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        binding.btnNext.setOnClickListener(v -> {
            sentLostTicket();
        });
        binding.cameraView.imageKtp.setOnClickListener(v -> {
            CameraFragment.newInstance(new CameraFragment.CameraCallback() {
                @Override
                public void onApply(String image) {
                    imageKtp = image;
                    final RequestOptions requestOptions = new RequestOptions()
                            .override((int) (binding.cameraView.imageKtp.getWidth()), (int) (binding.cameraView.imageKtp.getHeight()));
                    glideManager.load(fromBase64(image))
                            .apply(requestOptions)
                            .into(new CustomTarget<Drawable>() {
                                @Override
                                public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                    binding.cameraView.imageKtp.setImageDrawable(resource);
                                    binding.cameraView.imageKtp.buildDrawingCache();
                                }

                                @Override
                                public void onLoadCleared(@Nullable Drawable placeholder) {

                                }
                            });

                }

                @Override
                public void onCancel() {

                }

                @Override
                public void requestPermission() {
                    setState(Constant.REQUEST_PERMISSION, FragmentLostTicket.this);
                }
            }).show(getChildFragmentManager(), "CameraKTP");
        });
        binding.cameraView.imageKendaraan.setOnClickListener(v -> {
            CameraFragment.newInstance(new CameraFragment.CameraCallback() {
                @Override
                public void onApply(String image) {
                    imageKendaraan = image;
                    final RequestOptions requestOptions = new RequestOptions()
                            .override((int) (binding.cameraView.imageKendaraan.getWidth()), (int) (binding.cameraView.imageKendaraan.getHeight()));
                    glideManager.load(fromBase64(image))
                            .apply(requestOptions)
                            .into(new CustomTarget<Drawable>() {
                                @Override
                                public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                    binding.cameraView.imageKendaraan.setImageDrawable(resource);
                                    binding.cameraView.imageKendaraan.buildDrawingCache();
                                }

                                @Override
                                public void onLoadCleared(@Nullable Drawable placeholder) {

                                }
                            });

                }

                @Override
                public void onCancel() {

                }

                @Override
                public void requestPermission() {
                    setState(Constant.REQUEST_PERMISSION, FragmentLostTicket.this);
                }
            }).show(getChildFragmentManager(), "CameraKendaraan");
        });
    }

    private void searchPlat(String platNomor) {
        showLoading();
        clientInterface.getKendaraanByPlatNumber(platNomor).enqueue(new Callback<DataResponse<DataKendaraan>>() {
            @Override
            public void onResponse(@NonNull Call<DataResponse<DataKendaraan>> call, @NonNull Response<DataResponse<DataKendaraan>> response) {
                doneLoading();
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (!response.body().isError()) {
                            if (response.body().getData() != null) {
                                DataKendaraan dataKendaraan = response.body().getData();

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

                                binding.txtPlatNomor.setText(String.format("Plat Nomor: %s", dataKendaraan.getPlatNumber()));
                                binding.txtCheckin.setText(String.format("Waktu Checkin: %s", dataKendaraan.getCheckin()));

                                switchLayout(true);
                            } else {
                                switchLayout(false);
                            }
                        } else {
                            switchLayout(false);
                        }
                    }
                } else {
                    switchLayout(false);
                }
            }

            @Override
            public void onFailure(@NonNull Call<DataResponse<DataKendaraan>> call, @NonNull Throwable t) {
                doneLoading();
                switchLayout(false);
            }
        });
    }

    private void sentLostTicket() {
        if (parked == null) {
            showError(binding.svPlatNumber, "Search Vehicle first!");
            return;
        }

        if (imageKtp == null) {
            showError(binding.cameraView.cvKtp, "Please Take a Picture first");
            return;
        }

        if (imageKendaraan == null) {
            showError(binding.cameraView.cvKendaraan, "Please Take a Picture first");
            return;
        }

        showLoading();
        clientInterface.sendLostTicket(parked.getPlatNumber(), parked.getBillNumber(), imageKtp, imageKendaraan)
                .enqueue(new Callback<DataResponse<DataLostTicket>>() {
                    @Override
                    public void onResponse(Call<DataResponse<DataLostTicket>> call, Response<DataResponse<DataLostTicket>> response) {
                        doneLoading();
                        if (response.isSuccessful()) {
                            if (response.body() != null) {
                                if (!response.body().isError()) {
                                    // success
                                    // go to detail parkir
                                    Bundle args = new Bundle();
                                    args.putBoolean(Constant.KEY_DETAIL_PURPOSE, true);
                                    args.putSerializable(Constant.KEY_DETAIL_TIKET, parked);
                                    navigate(R.id.action_fragmentLostTicket_to_DetailPaymentFragment, args);
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<DataResponse<DataLostTicket>> call, Throwable t) {
                        doneLoading();
                    }
                });
    }

    private void switchLayout(boolean found) {
        if (found) {
            binding.cameraView.mainCameraView.setVisibility(View.VISIBLE);
            binding.llNotFound.setVisibility(View.GONE);
            binding.btnNext.setVisibility(View.VISIBLE);
            binding.cvDataKendaraan.setVisibility(View.VISIBLE);
        } else {
            binding.cameraView.mainCameraView.setVisibility(View.GONE);
            binding.llNotFound.setVisibility(View.VISIBLE);
            binding.btnNext.setVisibility(View.GONE);
            binding.cvDataKendaraan.setVisibility(View.GONE);
        }
    }

    private Bitmap fromBase64(String encodedImage) {
        byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }
}
