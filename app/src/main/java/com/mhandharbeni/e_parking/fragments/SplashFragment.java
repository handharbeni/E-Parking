package com.mhandharbeni.e_parking.fragments;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.fragment.NavHostFragment;

import com.google.gson.Gson;
import com.mhandharbeni.e_parking.R;
import com.mhandharbeni.e_parking.apis.responses.DataResponse;
import com.mhandharbeni.e_parking.apis.responses.data.DataMe;
import com.mhandharbeni.e_parking.apis.responses.data.DataPrice;
import com.mhandharbeni.e_parking.cores.BaseFragment;
import com.mhandharbeni.e_parking.databinding.FragmentSplashBinding;
import com.mhandharbeni.e_parking.utils.Constant;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashFragment extends BaseFragment {
    FragmentSplashBinding binding;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSplashBinding.inflate(inflater, container, false);

        navController = NavHostFragment.findNavController(this);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (!Objects.equals(utilDb.getString(Constant.TOKEN), "")) {
            fetchMe();
        } else {
            new CountDownTimer(3000, 1000) {
                @Override
                public void onTick(long l) {

                }

                @Override
                public void onFinish() {
                    navController.navigate(R.id.action_splash_to_login);
                }
            }.start();
        }
    }

    void fetchMe() {
        clientInterface.getMe().enqueue(new Callback<DataResponse<DataMe>>() {
            @Override
            public void onResponse(Call<DataResponse<DataMe>> call, Response<DataResponse<DataMe>> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null && !response.body().isError()) {
                        String json = gson.toJson(response.body().getData());
                        utilDb.putString(Constant.ME, json);
                    }
                }
                fetchPrice();
            }

            @Override
            public void onFailure(Call<DataResponse<DataMe>> call, Throwable t) {
                fetchPrice();
            }
        });
    }

    void fetchPrice() {
        clientInterface.getPrice().enqueue(new Callback<DataResponse<DataPrice>>() {
            @Override
            public void onResponse(Call<DataResponse<DataPrice>> call, Response<DataResponse<DataPrice>> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null && !response.body().isError()) {
                        String json = gson.toJson(response.body().getData());
                        utilDb.putString(Constant.PRICE, json);
                    }
                }
                navController.navigate(R.id.action_splash_to_main);
            }

            @Override
            public void onFailure(Call<DataResponse<DataPrice>> call, Throwable t) {
                navController.navigate(R.id.action_splash_to_main);
            }
        });
    }
}
