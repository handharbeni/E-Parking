package com.mhandharbeni.e_parking.cores;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.navigation.NavController;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.google.android.datatransport.runtime.dagger.Component;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.gson.Gson;
import com.mhandharbeni.e_parking.R;
import com.mhandharbeni.e_parking.apis.Client;
import com.mhandharbeni.e_parking.apis.ClientInterface;
import com.mhandharbeni.e_parking.apis.responses.DataResponse;
import com.mhandharbeni.e_parking.apis.responses.data.DataMe;
import com.mhandharbeni.e_parking.apis.responses.data.DataPrice;
import com.mhandharbeni.e_parking.apis.responses.data.DataStats;
import com.mhandharbeni.e_parking.database.AppDb;
import com.mhandharbeni.e_parking.fragments.LoginFragment;
import com.mhandharbeni.e_parking.services.TrackingServices;
import com.mhandharbeni.e_parking.utils.Constant;
import com.mhandharbeni.e_parking.utils.UtilDb;
import com.mhandharbeni.e_parking.utils.UtilFirebase;
import com.skydoves.balloon.ArrowOrientation;
import com.skydoves.balloon.ArrowPositionRules;
import com.skydoves.balloon.Balloon;
import com.skydoves.balloon.BalloonAnimation;
import com.skydoves.balloon.BalloonSizeSpec;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BaseFragment extends Fragment {
    public final String TAG = BaseFragment.class.getSimpleName();
    public NavController navController;
    public AppDb appDb;
    public UtilDb utilDb;
    public Balloon balloon;
    public Resources resources;
    public RequestManager glideManager;
    public ProgressDialog progressDialog;
    public Gson gson;

    public ClientInterface clientInterface;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        clientInterface = Client.getInstance(requireContext(), ClientInterface.class);
        appDb = AppDb.getInstance(requireContext());
        utilDb = new UtilDb(requireContext());
        resources = requireContext().getResources();
        glideManager = Glide.with(this);
        gson = new Gson();


        Intent intent = new Intent(requireActivity(), TrackingServices.class);
        requireActivity().startService(intent);

//        UtilFirebase.setOnline(getMe(), task -> {});
    }

    @Override
    public void onStart() {
        super.onStart();
        UtilFirebase.setOnline(getMe(), task -> {});
    }

    @Override
    public void onResume() {
        super.onResume();
        UtilFirebase.setOnline(getMe(), task -> {});
    }

    @Override
    public void onDestroy() {
        UtilFirebase.setOffline(getMe(), task -> {});
        super.onDestroy();
    }

    public <T> void observe(String key, Observer<T> observer) {
        Objects.requireNonNull(
                navController
                        .getCurrentBackStackEntry()
        ).getSavedStateHandle().getLiveData(key).observe(
                getViewLifecycleOwner(),
                (Observer<? super Object>) t -> observer.onChanged((T) t)
        );
    }

    public <T> void setState(String key, T value) {
        Objects.requireNonNull(
                navController
                        .getCurrentBackStackEntry()
        ).getSavedStateHandle().set(key, value);
    }

    public void navigate(int id) {
        try {
            navController.navigate(id);
        } catch (Exception ignored) {}
    }

    public void navigate(int id, Bundle bundle) {
        try {
            navController.navigate(id, bundle);
        } catch (Exception ignored) {}
    }

    public void showLoading() {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
        progressDialog = new ProgressDialog(requireActivity());
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Tunggu Sebentar");
        progressDialog.show();
    }

    public void doneLoading() {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    public void showError(View view, String message) {
        if (balloon != null) {
            balloon.dismiss();
            balloon = null;
        }
        balloon = new Balloon.Builder(requireContext())
                .setArrowSize(10)
                .setArrowOrientation(ArrowOrientation.TOP)
                .setArrowPositionRules(ArrowPositionRules.ALIGN_ANCHOR)
                .setArrowPosition(0.5f)
                .setIsVisibleArrow(true)
                .setText(message)
                .setPadding(9)
                .setWidth(BalloonSizeSpec.WRAP)
                .setHeight(BalloonSizeSpec.WRAP)
                .setTextSize(15f)
                .setCornerRadius(4f)
                .setAlpha(0.9f)
                .setBalloonAnimation(BalloonAnimation.ELASTIC)
                .setLifecycleOwner(getViewLifecycleOwner())
                .build();
        balloon.showAlignBottom(view);
        balloon.setOnBalloonClickListener(view1 -> balloon.dismiss());
        balloon.setOnBalloonOverlayClickListener(balloon::dismiss);
    }

    public void showDialogOneButton(String title, String message, String button) {
        new MaterialAlertDialogBuilder(requireActivity())
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(button, (dialogInterface, i) -> {

                })
                .show();
    }

    public void fetchMe() {
        clientInterface.getMe().enqueue(new Callback<DataResponse<DataMe>>() {
            @Override
            public void onResponse(@NonNull Call<DataResponse<DataMe>> call, @NonNull Response<DataResponse<DataMe>> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null && !response.body().isError()) {
                        String json = gson.toJson(response.body().getData());
                        utilDb.putString(Constant.ME, json);
                    }
                }
                fetchPrice();
            }

            @Override
            public void onFailure(@NonNull Call<DataResponse<DataMe>> call, @NonNull Throwable t) {
                fetchPrice();
            }
        });
    }

    public void fetchPrice() {
        clientInterface.getPrice().enqueue(new Callback<DataResponse<DataPrice>>() {
            @Override
            public void onResponse(@NonNull Call<DataResponse<DataPrice>> call, @NonNull Response<DataResponse<DataPrice>> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null && !response.body().isError()) {
                        String json = gson.toJson(response.body().getData());
                        utilDb.putString(Constant.PRICE, json);
                    }
                }
                fetchStats();
            }

            @Override
            public void onFailure(@NonNull Call<DataResponse<DataPrice>> call, @NonNull Throwable t) {
                fetchStats();
            }
        });
    }

    public void fetchStats() {
        clientInterface.getStats().enqueue(new Callback<DataResponse<DataStats>>() {
            @Override
            public void onResponse(@NonNull Call<DataResponse<DataStats>> call, @NonNull Response<DataResponse<DataStats>> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null && !response.body().isError()) {
                        String json = gson.toJson(response.body().getData());
                        utilDb.putString(Constant.STATS, json);
                    }
                }
                setState(Constant.FETCH_DATA, "DONE");
            }

            @Override
            public void onFailure(@NonNull Call<DataResponse<DataStats>> call, @NonNull Throwable t) {
                setState(Constant.FETCH_DATA, "DONE");
            }
        });
    }

    public DataMe getMe() {
        try {
            return gson.fromJson(utilDb.getString(Constant.ME), DataMe.class);
        } catch (Exception ignored) {
            return null;
        }
    }

    public DataPrice getPrice() {
        try {
            return gson.fromJson(utilDb.getString(Constant.PRICE), DataPrice.class);
        } catch (Exception ignored) {
            return null;
        }
    }

    public DataStats getStats() {
        try {
            return gson.fromJson(utilDb.getString(Constant.STATS), DataStats.class);
        } catch (Exception ignored) {
            return null;
        }
    }
}
