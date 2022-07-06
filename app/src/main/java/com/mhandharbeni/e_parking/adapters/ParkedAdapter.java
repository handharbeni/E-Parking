package com.mhandharbeni.e_parking.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mhandharbeni.e_parking.R;
import com.mhandharbeni.e_parking.database.models.Parked;
import com.mhandharbeni.e_parking.databinding.ItemParkedBinding;

import java.util.List;

public class ParkedAdapter extends RecyclerView.Adapter<ParkedAdapter.ViewHolder> {
    Context context;
    List<Parked> listParked;
    ParkedCallback parkedCallback;

    public ParkedAdapter(Context context, List<Parked> listParked, ParkedCallback parkedCallback) {
        this.context = context;
        this.listParked = listParked;
        this.parkedCallback = parkedCallback;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemParkedBinding binding = ItemParkedBinding.inflate(layoutInflater);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        binding.getRoot().setLayoutParams(lp);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Parked parked = listParked.get(position);
        String vehicle = "Motor";
        switch (parked.getType()) {
            case 0 :
                vehicle = "Motor";
                break;
            case 1 :
                vehicle = "Mobil";
                break;
            case 2 :
                vehicle = "Bus Mini";
                break;
            case 3 :
                vehicle = "Bus Besar";
                break;
        }
        holder.binding.txtPlatNo.setText(String.format("%s: %s", context.getResources().getString(R.string.print_platno), parked.getPlatNumber()));
        holder.binding.txtVehicle.setText(String.format("%s: %s", context.getResources().getString(R.string.print_vehicle), vehicle));
        holder.binding.txtTicketNo.setText(String.format("%s: %s", context.getResources().getString(R.string.print_ticket_number), parked.getTicketNumber()));
        holder.itemView.setOnClickListener(v -> {
            parkedCallback.onItemParkedClick(parked);
        });
    }

    @Override
    public int getItemCount() {
        return listParked.size();
    }

    public void updateData(List<Parked> listParked) {
        this.listParked = listParked;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ItemParkedBinding binding;
        public ViewHolder(@NonNull ItemParkedBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }

    public interface ParkedCallback{
        void onItemParkedClick(Parked parked);
    }
}
