package com.mhandharbeni.e_parking.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.navigation.fragment.NavHostFragment;

import com.github.douglasjunior.bluetoothclassiclibrary.BluetoothStatus;
import com.mhandharbeni.e_parking.MainActivity;
import com.mhandharbeni.e_parking.R;
import com.mhandharbeni.e_parking.cores.BaseFragment;
import com.mhandharbeni.e_parking.database.models.Parked;
import com.mhandharbeni.e_parking.databinding.FragmentMainBinding;
import com.mhandharbeni.e_parking.events.BluetoothEvent;
import com.mhandharbeni.e_parking.utils.Constant;
import com.mhandharbeni.e_parking.utils.UtilDate;
import com.mhandharbeni.e_parking.utils.UtilPermission;
import com.skydoves.balloon.ArrowOrientation;
import com.skydoves.balloon.ArrowPositionRules;
import com.skydoves.balloon.Balloon;
import com.skydoves.balloon.BalloonAnimation;
import com.skydoves.balloon.BalloonSizeSpec;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class MainFragment extends BaseFragment {

    private FragmentMainBinding binding;

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

        observeMain();
        checkStatus();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    void setupDb() {
        appDb.parked().getLive(false, UtilDate.getNow()).observe(
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

    void observeMain() {
        observe(Constant.BLUETOOTH_CONNECTED, (Observer<Boolean>) aBoolean ->
                binding.txtBtStatus.setText(
                        String.format(
                                "%s: %s",
                                resources.getString(R.string.bluetooth_status),
                                (aBoolean ?
                                        resources.getString(R.string.bluetooth_status_connected)
                                        : resources.getString(R.string.bluetooth_status_disconnected))
                        )
                ));
    }

    void checkStatus() {
        setState(Constant.BLUETOOTH_CONNECT_STATUS, MainFragment.class.getSimpleName());
    }

    void setupTrigger() {
        binding.header.menu.setOnClickListener(this::showPopupMenu);
        binding.btnCheckin.setOnClickListener(
                v -> {
                    if (!UtilPermission.checkPermission(requireContext())) {
                        setState(Constant.REQUEST_PERMISSION, CheckinFragment.class.getSimpleName());
                    } else {
                        if (!MainActivity.bluetoothConnected) {
                            showError(binding.header.menu, "Please Connect to Bluetooh printer!");
                        } else {
                            navigate(R.id.action_main_to_checkin);
                        }
                    }
                });
        binding.btnCheckout.setOnClickListener(
                v -> {
                    if (!UtilPermission.checkPermission(requireContext())) {
                        setState(Constant.REQUEST_PERMISSION, CheckinFragment.class.getSimpleName());
                    } else {
                        navigate(R.id.action_main_to_checkout);
                    }
                }
        );
        binding.btnDetail.setOnClickListener(
                v -> {
                    if (!UtilPermission.checkPermission(requireContext())) {
                        setState(Constant.REQUEST_PERMISSION, CheckinFragment.class.getSimpleName());
                    } else {
                        navigate(R.id.action_main_to_listparked);
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
                v -> navigate(R.id.action_main_to_bt));
        balloon.getContentView().findViewById(R.id.btnLogout).setOnClickListener(
                v -> navigate(R.id.action_main_to_login));
        balloon.getContentView().findViewById(R.id.btnAbout).setOnClickListener(v -> {
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onConnect(BluetoothEvent bluetoothEvent) {
        boolean status = bluetoothEvent.btStatus == BluetoothStatus.CONNECTED;
        binding.txtBtStatus.setText(
                String.format(
                        "%s: %s",
                        resources.getString(R.string.bluetooth_status),
                        (status ?
                                resources.getString(R.string.bluetooth_status_connected)
                                : resources.getString(R.string.bluetooth_status_disconnected))
                )
        );
    }


    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }
}