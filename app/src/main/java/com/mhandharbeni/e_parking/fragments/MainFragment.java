package com.mhandharbeni.e_parking.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.mhandharbeni.e_parking.R;
import com.mhandharbeni.e_parking.database.AppDb;
import com.mhandharbeni.e_parking.database.models.Parked;
import com.mhandharbeni.e_parking.databinding.FragmentMainBinding;
import com.mhandharbeni.e_parking.utils.Constant;
import com.mhandharbeni.e_parking.utils.UtilDate;
import com.mhandharbeni.e_parking.utils.UtilNav;
import com.mhandharbeni.e_parking.utils.UtilPermission;
import com.skydoves.balloon.ArrowOrientation;
import com.skydoves.balloon.ArrowPositionRules;
import com.skydoves.balloon.Balloon;
import com.skydoves.balloon.BalloonAnimation;
import com.skydoves.balloon.BalloonSizeSpec;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainFragment extends Fragment {

    private FragmentMainBinding binding;
    NavController navController;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentMainBinding.inflate(inflater, container, false);


        setupNavigation();
        setupTrigger();
        setupDb();
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    void setupDb() {
        AppDb.getInstance(requireContext()).parked().getLive(false, UtilDate.getNow()).observe(
                getViewLifecycleOwner(),
                parkeds -> {
                    int totalKendaraan = 0;
                    int totalPaid = 0;
                    for (Parked parked : parkeds) {
                        totalKendaraan++;
                        totalPaid += parked.getPrice();
                    }
                    binding.edtPendapatan.setText(String.valueOf(totalPaid));
                    binding.edtKarcis.setText(String.valueOf(totalKendaraan));
                });
    }

    void setupNavigation() {
        navController = NavHostFragment.findNavController(this);
    }

    void setupTrigger() {
        binding.header.menu.setOnClickListener(this::showPopupMenu);
        binding.btnCheckin.setOnClickListener(
                v -> {
                    if (!UtilPermission.checkPermission(requireContext())) {
                        new UtilNav<>().setStateHandle(navController, Constant.REQUEST_PERMISSION, CheckinFragment.class.getSimpleName());
                    } else {
                        navController.navigate(R.id.action_main_to_checkin);
                    }
                });
        binding.btnCheckout.setOnClickListener(
                v -> {
                    if (!UtilPermission.checkPermission(requireContext())) {
                        new UtilNav<>().setStateHandle(navController, Constant.REQUEST_PERMISSION, CheckinFragment.class.getSimpleName());
                    } else {
                        navController.navigate(R.id.action_main_to_checkout);
                    }
                }
        );
    }

    void showPopupMenu(View view) {
        Balloon balloon = new Balloon.Builder(requireContext())
                .setLayout(R.layout.popup_menu)
                .setArrowSize(10)
                .setArrowOrientation(ArrowOrientation.TOP)
                .setArrowPositionRules(ArrowPositionRules.ALIGN_ANCHOR)
                .setArrowPosition(0.5f)
                .setWidth(BalloonSizeSpec.WRAP)
                .setHeight(BalloonSizeSpec.WRAP)
                .setTextSize(15f)
                .setCornerRadius(4f)
                .setAlpha(0.9f)
                .setBalloonAnimation(BalloonAnimation.ELASTIC)
                .setLifecycleOwner(getViewLifecycleOwner())
                .build();
        balloon.showAlignBottom(binding.header.menu);
        balloon.getContentView().findViewById(R.id.btnBt).setOnClickListener(
                v -> navController.navigate(R.id.action_main_to_bt));
        balloon.getContentView().findViewById(R.id.btnLogout).setOnClickListener(
                v -> navController.navigate(R.id.action_main_to_login));
        balloon.getContentView().findViewById(R.id.btnAbout).setOnClickListener(v -> {
        });

    }

}