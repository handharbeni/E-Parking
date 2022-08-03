package com.mhandharbeni.e_parking.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.fragment.NavHostFragment;

import com.mhandharbeni.e_parking.R;
import com.mhandharbeni.e_parking.apis.responses.data.DataMe;
import com.mhandharbeni.e_parking.cores.BaseFragment;
import com.mhandharbeni.e_parking.database.models.Parked;
import com.mhandharbeni.e_parking.databinding.FragmentDetailBayarBinding;
import com.mhandharbeni.e_parking.utils.Constant;
import com.mhandharbeni.e_parking.utils.UtilDate;
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
        binding.txtTimeIn.setText(UtilDate.longToDate(parked.getCheckIn(), "HH:mm:ss"));
        binding.txtTimeOut.setText(UtilDate.longToDate(parked.getCheckOut(), "HH:mm:ss"));
        binding.txtDuration.setText("0");
        binding.txtTarif.setText(String.valueOf(parked.getPrice()));
        binding.imagePreview.setImageBitmap(UtilImage.base64ToImage(parked.getImage()));

        try {
            binding.txtIdJukir.setText(getMe()!=null?getMe().getNpwrd().replaceAll("([\\r\\n])", "").trim():"");
            binding.txtNamaJukir.setText(getMe()!=null?getMe().getNamaLengkap().replaceAll("([\\r\\n])", "").trim():"");
            binding.txtLokasiJukir.setText(getMe()!=null?getMe().getLokasiParkir().replaceAll("([\\r\\n])", "").trim():"");
        } catch (Exception ignored) {}
    }

    void setupTrigger() {
        binding.btnBayar.setOnClickListener(v -> {
            Bundle args = new Bundle();
            args.putSerializable(Constant.KEY_DETAIL_TIKET, parked);
            navigate(R.id.action_detailpayment_to_paymentoptions, args);
        });
        binding.btnPrintTiket.setOnClickListener(v -> setState(Constant.BLUETOOTH_PRINT, parked));
    }
}
