package com.mhandharbeni.e_parking.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.fragment.NavHostFragment;

import com.mhandharbeni.e_parking.R;
import com.mhandharbeni.e_parking.cores.BaseFragment;
import com.mhandharbeni.e_parking.database.models.Parked;
import com.mhandharbeni.e_parking.databinding.FragmentDetailBayarBinding;
import com.mhandharbeni.e_parking.utils.Constant;
import com.mhandharbeni.e_parking.utils.UtilImage;

public class DetailPaymentFragment extends BaseFragment {
    FragmentDetailBayarBinding binding;
    Parked parked;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentDetailBayarBinding.inflate(inflater, container, false);

        navController = NavHostFragment.findNavController(this);

        setupBundle();

        return binding.getRoot();
    }

    void setupBundle() {
        Bundle args = getArguments();
        if (args != null) {
            parked = (Parked) args.getSerializable(Constant.KEY_DETAIL_TIKET);
            bindData();

            setupTrigger();
        }
    }

    void bindData() {
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

        binding.txtNoTiket.setText(parked.getTicketNumber());
        binding.txtKendaraan.setText(type);
        binding.txtTimeIn.setText(String.valueOf(parked.getCheckIn()));
        binding.txtTimeOut.setText(String.valueOf(parked.getCheckOut()));
        binding.txtDuration.setText("0");
        binding.txtTarif.setText(String.valueOf(parked.getPrice()));
        binding.txtIdJukir.setText("JK-0012345");
        binding.txtNamaJukir.setText("JUKIR DEV");
        binding.txtLokasiJukir.setText("JL. Diponegoro - Indomart");
        binding.imagePreview.setImageBitmap(UtilImage.base64ToImage(parked.getImage()));
    }

    void setupTrigger() {
        binding.btnBayar.setOnClickListener(v -> {
            Bundle args = new Bundle();
            args.putSerializable(Constant.KEY_DETAIL_TIKET, parked);
            navigate(R.id.action_detailpayment_to_paymentoptions, args);
        });
    }
}
