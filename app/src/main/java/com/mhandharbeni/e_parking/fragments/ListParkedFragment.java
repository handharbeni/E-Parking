package com.mhandharbeni.e_parking.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.mhandharbeni.e_parking.R;
import com.mhandharbeni.e_parking.adapters.ParkedAdapter;
import com.mhandharbeni.e_parking.cores.BaseFragment;
import com.mhandharbeni.e_parking.database.models.Parked;
import com.mhandharbeni.e_parking.databinding.FragmentListParkedBinding;
import com.mhandharbeni.e_parking.utils.Constant;
import com.mhandharbeni.e_parking.utils.UtilDate;

import java.util.ArrayList;

public class ListParkedFragment extends BaseFragment implements ParkedAdapter.ParkedCallback {
    FragmentListParkedBinding binding;

    ParkedAdapter parkedAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentListParkedBinding.inflate(inflater, container, false);

        navController = NavHostFragment.findNavController(this);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupAdapter();
        setupData();
    }

    void setupAdapter() {
        parkedAdapter = new ParkedAdapter(requireContext(), new ArrayList<>(), this);
        LinearLayoutManager llm = new LinearLayoutManager(requireContext());
        binding.rvParked.setLayoutManager(llm);
        binding.rvParked.setAdapter(parkedAdapter);
    }

    void setupData() {
        appDb.parked().getLive(false, UtilDate.getNow()).observe(getViewLifecycleOwner(), parkeds -> {
            try {
                parkedAdapter.updateData(parkeds);
            } catch (Exception ignored){}
        });
    }

    @Override
    public void onItemParkedClick(Parked parked) {
        Bundle args = new Bundle();
        args.putSerializable(Constant.KEY_DETAIL_TIKET, parked);
        navigate(R.id.action_listparked_to_detailpayment, args);
    }
}
