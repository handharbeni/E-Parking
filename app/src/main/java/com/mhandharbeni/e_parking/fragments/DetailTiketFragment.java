package com.mhandharbeni.e_parking.fragments;

import static android.content.Context.WINDOW_SERVICE;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.mhandharbeni.e_parking.R;
import com.mhandharbeni.e_parking.database.models.Parked;
import com.mhandharbeni.e_parking.databinding.FragmentDetailTiketBinding;
import com.mhandharbeni.e_parking.utils.Constant;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class DetailTiketFragment extends Fragment {
    private final String TAG = DetailTiketFragment.class.getSimpleName();

    NavController navController;
    FragmentDetailTiketBinding binding;

    Parked parked;

    RequestManager glideManager;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentDetailTiketBinding.inflate(inflater, container, false);

        setupBundle();
        setupLibs();

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupData();
        setupTrigger();
    }

    void setupLibs() {
        navController = NavHostFragment.findNavController(this);
        glideManager = Glide.with(this);
    }

    void setupBundle() {
        Bundle args = getArguments();
        if (args != null) {
            parked = (Parked) args.getSerializable(Constant.KEY_DETAIL_TIKET);
        }
    }

    void setupTrigger() {
        binding.btnPrint.setOnClickListener(v -> {});
        binding.btnDone.setOnClickListener(v -> navController.navigate(R.id.action_detail_to_main));
    }

    void setupData() {
        if (parked != null) {
            WindowManager manager = (WindowManager) requireActivity().getSystemService(WINDOW_SERVICE);
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
            try {
                Bitmap bitmap = qrgEncoder.getBitmap();
                glideManager.load(bitmap).into(binding.imgQR);
            } catch (Exception ignored) {}

            String type = "MOTOR";
            switch (parked.getType()) {
                case 0 :
                    type = "MOTOR";
                    break;
                case 1 :
                    type = "MOBIL";
                    break;
                case 2 :
                    type = "BUS MINI";
                    break;
                case 3 :
                    type = "BUS BESAR";
                    break;
            }

            binding.txtDate.setText("Tanggal: "+ parked.getDate());
            binding.txtNoTiket.setText("No. Karcis: "+parked.getTicketNumber());
            binding.txtNoPol.setText("Nomo Polisi"+parked.getPlatNumber());
            binding.txtType.setText("Kendaraan: "+type);
            binding.txtPrice.setText("Tarif: "+String.valueOf(parked.getPrice()));
        } else {
            navController.navigate(R.id.action_detail_to_main);
        }
    }
}