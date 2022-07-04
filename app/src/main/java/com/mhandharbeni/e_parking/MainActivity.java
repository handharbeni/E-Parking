package com.mhandharbeni.e_parking;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.github.douglasjunior.bluetoothclassiclibrary.BluetoothClassicService;
import com.github.douglasjunior.bluetoothclassiclibrary.BluetoothConfiguration;
import com.github.douglasjunior.bluetoothclassiclibrary.BluetoothService;
import com.github.douglasjunior.bluetoothclassiclibrary.BluetoothStatus;
import com.google.android.material.snackbar.Snackbar;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.core.app.ActivityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.mhandharbeni.e_parking.database.AppDb;
import com.mhandharbeni.e_parking.databinding.ActivityMainBinding;
import com.mhandharbeni.e_parking.events.MessageEvent;
import com.mhandharbeni.e_parking.utils.Constant;
import com.mhandharbeni.e_parking.utils.Util;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

public class MainActivity extends AppCompatActivity implements BluetoothService.OnBluetoothEventCallback, BluetoothService.OnBluetoothScanCallback {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    private BluetoothAdapter bluetoothAdapter;
    private BluetoothConfiguration config;
    private BluetoothService service;
    private AppDb appDb;

    List<BluetoothDevice> listDevice = new ArrayList<>();

    private final ActivityResultLauncher<String[]> requestPermissionLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.RequestMultiplePermissions(),
                    this::responsePermissions
            );

    ActivityResultLauncher<Intent> mainActivityLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            this::resultActivity);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        binding.toolbar.setVisibility(View.GONE);

        requestPermission();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {

    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    void requestPermission() {
        Util.enableGps(this);
        String[] listPermission = new String[Constant.LIST_PERMISSION.size()];
        for (int i = 0; i < Constant.LIST_PERMISSION.size(); i++) {
            listPermission[i] = Constant.LIST_PERMISSION.get(i);
        }
        requestPermissionLauncher.launch(listPermission);
    }

    void responsePermissions(Map<String, Boolean> result) {
        boolean readStoragePermission = Boolean.TRUE.equals(result.get(Manifest.permission.READ_EXTERNAL_STORAGE));
        boolean writeStoragePermission = Boolean.TRUE.equals(result.get(Manifest.permission.WRITE_EXTERNAL_STORAGE));
        boolean fineLocationPermission = Boolean.TRUE.equals(result.get(Manifest.permission.ACCESS_FINE_LOCATION));
        boolean coarseLocationPermission = Boolean.TRUE.equals(result.get(Manifest.permission.ACCESS_COARSE_LOCATION));
        if (fineLocationPermission || coarseLocationPermission) {
            // coarse location accepted
            Util.enableGps(this);
        }

        if (writeStoragePermission) {
            initDb();
        }
    }

    void initDb() {
        appDb = AppDb.getInstance(getApplicationContext());
    }

    void resultActivity(ActivityResult result) {
        if (result.getResultCode() == Activity.RESULT_OK) {
            if (Constant.REQUEST_CODE == Constant.REQUEST_CODE_ENABLE_BLUETOOTH) {
                initBluetooth();
            }
        }
    }

    void initBluetooth() {
        config = new BluetoothConfiguration();
        config.context = getApplicationContext();
        config.bluetoothServiceClass = BluetoothClassicService.class;
        config.bufferSize = 1024;
        config.characterDelimiter = '\n';
        config.deviceName = getResources().getString(R.string.app_name);
        config.callListenersInMainThread = true;

        config.uuid = UUID.fromString(Constant.UUID);

        BluetoothService.init(config);
        service = BluetoothService.getDefaultInstance();

        service.setOnEventCallback(this);
        service.setOnScanCallback(this);

        try {
            service.disconnect();
        } catch (Exception ignored) {

        } finally {
            service.startScan();
        }
    }

    void checkBluetoothDevice() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter != null) {
            turnOnBT();
        }
    }

    void turnOnBT() {
        if (!bluetoothAdapter.isEnabled()) {
            Constant.REQUEST_CODE = Constant.REQUEST_CODE_ENABLE_BLUETOOTH;
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            mainActivityLauncher.launch(enableBtIntent);
        }
        initBluetooth();
    }

    @Override
    public void onDataRead(byte[] buffer, int length) {

    }

    @Override
    public void onStatusChange(BluetoothStatus status) {

    }

    @Override
    public void onDeviceName(String deviceName) {

    }

    @Override
    public void onToast(String message) {

    }

    @Override
    public void onDataWrite(byte[] buffer) {

    }

    @Override
    public void onDeviceDiscovered(BluetoothDevice device, int rssi) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            requestPermission();
        }

        if (device.getName() != null) {
            if (!listDevice.contains(device)) {
                listDevice.add(device);
            }
        }

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_content_main);
        AtomicReference<BluetoothFragment> bluetoothFragment = new AtomicReference<>();

        try {
            if (navHostFragment != null) {
                bluetoothFragment.set((BluetoothFragment) navHostFragment.getChildFragmentManager().getFragments().get(0));
            }
            if (bluetoothFragment.get() != null) {
                bluetoothFragment.get().updateDevice(listDevice);
            }
        } catch (Exception ignored) {
        }
    }

    @Override
    public void onStartScan() {

    }

    @Override
    public void onStopScan() {

    }
}