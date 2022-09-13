package com.mhandharbeni.e_parking.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.mhandharbeni.e_parking.R;
import com.mhandharbeni.e_parking.apis.Client;
import com.mhandharbeni.e_parking.apis.responses.DataResponse;
import com.mhandharbeni.e_parking.apis.responses.data.DataLogin;
import com.mhandharbeni.e_parking.cores.BaseFragment;
import com.mhandharbeni.e_parking.databinding.FragmentLoginBinding;
import com.mhandharbeni.e_parking.utils.Constant;
import com.mhandharbeni.e_parking.utils.Util;
import com.skydoves.balloon.Balloon;
import com.skydoves.balloon.BalloonAnimation;
import com.skydoves.balloon.BalloonCenterAlign;
import com.skydoves.balloon.BalloonSizeSpec;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginFragment extends BaseFragment {
    private FragmentLoginBinding binding;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);

        navController = NavHostFragment.findNavController(this);

        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.btnLogin.setOnClickListener(v -> {
            showLoading();
            clientInterface.login(
                    Objects.requireNonNull(binding.edtIdJukir.getEditText()).getText().toString(),
                    Objects.requireNonNull(binding.edtPassword.getEditText()).getText().toString()
            ).enqueue(new Callback<DataResponse<DataLogin>>() {
                @Override
                public void onResponse(@NonNull Call<DataResponse<DataLogin>> call, @NonNull Response<DataResponse<DataLogin>> response) {
                    if (response.isSuccessful()) {
                        if (response.body() != null) {
                            if (!response.body().isError()) {
                                utilDb.putString(Constant.TOKEN, response.body().getData().getToken());
                                navigate(R.id.action_login_to_main);
                            } else {
                                showError(binding.edtPassword, response.body().getMessage());
                            }
                        }
                    } else {
                        showError(binding.edtPassword, "Something went wrong");
                    }
                    doneLoading();
                }

                @Override
                public void onFailure(@NonNull Call<DataResponse<DataLogin>> call, @NonNull Throwable t) {
                    showError(binding.edtPassword, "Wrong username and password");
                    doneLoading();
                }
            });
//            navigate(R.id.action_login_to_main)
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}