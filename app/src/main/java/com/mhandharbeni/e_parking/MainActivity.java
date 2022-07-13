package com.mhandharbeni.e_parking;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.dantsu.escposprinter.EscPosPrinter;
import com.dantsu.escposprinter.connection.bluetooth.BluetoothPrintersConnections;
import com.dantsu.escposprinter.exceptions.EscPosBarcodeException;
import com.dantsu.escposprinter.exceptions.EscPosConnectionException;
import com.dantsu.escposprinter.exceptions.EscPosEncodingException;
import com.dantsu.escposprinter.exceptions.EscPosParserException;
import com.dantsu.escposprinter.textparser.PrinterTextParserImg;
import com.github.douglasjunior.bluetoothclassiclibrary.BluetoothClassicService;
import com.github.douglasjunior.bluetoothclassiclibrary.BluetoothConfiguration;
import com.github.douglasjunior.bluetoothclassiclibrary.BluetoothService;
import com.github.douglasjunior.bluetoothclassiclibrary.BluetoothStatus;
import com.mhandharbeni.e_parking.database.AppDb;
import com.mhandharbeni.e_parking.database.models.Parked;
import com.mhandharbeni.e_parking.databinding.ActivityMainBinding;
import com.mhandharbeni.e_parking.events.BluetoothEvent;
import com.mhandharbeni.e_parking.events.MessageEvent;
import com.mhandharbeni.e_parking.fragments.BluetoothFragment;
import com.mhandharbeni.e_parking.utils.Constant;
import com.mhandharbeni.e_parking.utils.Util;
import com.mhandharbeni.e_parking.utils.UtilDate;
import com.mhandharbeni.e_parking.utils.UtilNav;
import com.mhandharbeni.e_parking.utils.UtilPermission;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import io.reactivex.rxjava3.core.Observable;

public class MainActivity extends AppCompatActivity implements BluetoothService.OnBluetoothEventCallback, BluetoothService.OnBluetoothScanCallback {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    private BluetoothAdapter bluetoothAdapter;
    private BluetoothConfiguration config;
    private BluetoothService service;
    private AppDb appDb;

    NavController navController;

    List<BluetoothDevice> listDevice = new ArrayList<>();
    public static boolean bluetoothConnected = true;

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

        navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        navController.addOnDestinationChangedListener(this::observeChild);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
    }

    void observeChild(NavController navController, NavDestination navDestination, Bundle arguments) {
        new UtilNav<>()
                .observeValue(
                        navController,
                        this,
                        Constant.REQUEST_PERMISSION,
                        o -> requestPermission());

        new UtilNav<>()
                .observeValue(
                        navController,
                        this,
                        Constant.BLUETOOTH_SCAN_REQUEST,
                        o -> checkBluetoothDevice());

        new UtilNav<BluetoothDevice>()
                .observeValue(
                        navController,
                        this,
                        Constant.BLUETOOTH_CONNECT_REQUEST,
                        o -> {
                            try {
                                service.connect(o);
                            } catch (Exception ignored) {
                            }
                        });
        new UtilNav<Parked>()
                .observeValue(
                        navController,
                        this,
                        Constant.BLUETOOTH_PRINT,
                        this::print);
        new UtilNav<>()
                .observeValue(
                        navController,
                        this,
                        Constant.BLUETOOTH_CONNECT_STATUS,
                        o -> checkStatusBt());
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

    void checkStatusBt() {
        try {
            new UtilNav<Boolean>().setStateHandle(navController, Constant.BLUETOOTH_CONNECTED, service.getStatus() == BluetoothStatus.CONNECTED);
        } catch (Exception ignored) {
            new UtilNav<Boolean>().setStateHandle(navController, Constant.BLUETOOTH_CONNECTED, false);
        }
    }

    void initBluetooth() {
        config = new BluetoothConfiguration();
        config.context = getApplicationContext();
        config.bluetoothServiceClass = BluetoothClassicService.class;
        config.bufferSize = 1024;
        config.characterDelimiter = '\n';
        config.deviceName = getResources().getString(R.string.app_name);
        config.callListenersInMainThread = false;

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

    void print(Parked parked) {
        new PrintAsyncTask(this, getApplicationContext()).execute(parked);
    }

    @Override
    public void onDataRead(byte[] buffer, int length) {

    }

    @Override
    public void onStatusChange(BluetoothStatus status) {
        if (status == BluetoothStatus.CONNECTED) {
            bluetoothConnected = true;
            EventBus.getDefault().post(new BluetoothEvent(BluetoothEvent.BTEvent.BLUETOOTH_CONNECTED, BluetoothStatus.CONNECTED));
        } else if (status == BluetoothStatus.CONNECTING) {
            bluetoothConnected = false;
            EventBus.getDefault().post(new BluetoothEvent(BluetoothEvent.BTEvent.BLUETOOTH_CONNECTED, BluetoothStatus.CONNECTING));
        } else if (status == BluetoothStatus.NONE) {
            bluetoothConnected = false;
            EventBus.getDefault().post(new BluetoothEvent(BluetoothEvent.BTEvent.BLUETOOTH_CONNECTED, BluetoothStatus.NONE));
        }

        new Thread(() -> {
            new UtilNav<Boolean>().setStateHandle(navController, Constant.BLUETOOTH_CONNECTED, bluetoothConnected);
            new UtilNav<BluetoothStatus>().setStateHandle(navController, Constant.BLUETOOTH_CONNECTED_STRING, status);
        });
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
        if (!UtilPermission.checkPermission(getApplicationContext())) {
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

    @SuppressLint("StaticFieldLeak")
    private static class PrintAsyncTask extends AsyncTask<Parked, Boolean, Boolean> {
        private Context context;
        private ProgressDialog dialog;

        public PrintAsyncTask(Activity activity, Context context) {
            this.context = context;
            dialog = new ProgressDialog(activity);
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }

        @Override
        protected void onPreExecute() {
            dialog.setCancelable(false);
            dialog.setIndeterminate(true);
            dialog.setMessage("Printing");
            dialog.show();
        }

        @Override
        protected Boolean doInBackground(Parked... parkeds) {
            EscPosPrinter printer;
            try {
                Parked parked = parkeds[0];
                WindowManager manager = (WindowManager) context.getSystemService(WINDOW_SERVICE);
                Display display = manager.getDefaultDisplay();
                Point point = new Point();
                display.getSize(point);
                int width = point.x;
                int height = point.y;
                int smallerDimension = Math.min(width, height);
                smallerDimension = smallerDimension * 3 / 4;

                String inputValue = "";
                inputValue += parked.getPlatNumber();
                inputValue += ",_,";
                inputValue += parked.getDate();

                QRGEncoder qrgEncoder = new QRGEncoder(inputValue, null, QRGContents.Type.TEXT, smallerDimension);
                qrgEncoder.setColorBlack(Color.BLACK);
                qrgEncoder.setColorWhite(Color.WHITE);

                Bitmap bitmap = qrgEncoder.getBitmap();

                String vehicle = "Motor";
                switch (parked.getType()) {
                    case 0:
                        vehicle = "Motor";
                        break;
                    case 1:
                        vehicle = "Mobil";
                        break;
                    case 2:
                        vehicle = "Bus Mini";
                        break;
                    case 3:
                        vehicle = "Bus Besar";
                        break;
                }

                printer = new EscPosPrinter(BluetoothPrintersConnections.selectFirstPaired(), 203, 48f, 32);
                printer.printFormattedText(
                        "[C]<b><font size='big'>" + context.getResources().getString(R.string.app_name) + "</font></b>\n" +
                        "[C]<u><font size='normal'>" + context.getResources().getString(R.string.long_name_first) + "</font></u>\n" +
                        "[C]<u><font size='normal'>" + context.getResources().getString(R.string.long_name_second) + "</font></u>\n" +
                        "[C]<u><font size='normal'>" + context.getResources().getString(R.string.long_name_third) + "</font></u>\n" +
                        "[C]================================\n" +
                        "[L]" + context.getResources().getString(R.string.print_date) + "[R]" + UtilDate.longToDate(parked.getDate(), "MM/dd/yyyy") + "\n" +
                        "[L]" + context.getResources().getString(R.string.print_time_in) + "[R]" + UtilDate.longToDate(parked.getCheckIn(), "HH:mm:ss") + "\n" +
                        "[L]" + context.getResources().getString(R.string.print_vehicle) + "[R]" + vehicle + "\n" +
                        "[L]" + context.getResources().getString(R.string.print_ticket_number) + "[R]" + parked.getTicketNumber() + "\n" +
                        "[L]" + context.getResources().getString(R.string.print_platno) + "[R]" + parked.getPlatNumber() + "\n" +
                        "[L]" + context.getResources().getString(R.string.print_price) + "[R]" + parked.getPrice() + "\n" +
                        "[C]================================\n" +
                        "[C]<img>" + PrinterTextParserImg.bitmapToHexadecimalString(printer, bitmap) + "</img>\n" +
                        "[C]================================\n" +
                        "[C]\n" +
                        "[C]\n" +
                        "[C]\n" +
                        "[C]\n"
                );
            } catch (EscPosConnectionException | EscPosEncodingException | EscPosBarcodeException | EscPosParserException ignored) {
            }
            return false;
        }
    }
}