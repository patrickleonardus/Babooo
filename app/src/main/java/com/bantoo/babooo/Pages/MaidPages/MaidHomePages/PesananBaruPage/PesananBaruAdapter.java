package com.bantoo.babooo.Pages.MaidPages.MaidHomePages.PesananBaruPage;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bantoo.babooo.Model.ServiceSchedule;
import com.bantoo.babooo.R;

import java.util.List;

public class PesananBaruAdapter extends RecyclerView.Adapter<PesananBaruAdapter.MyViewHolder> {

    private List<ServiceSchedule> serviceScheduleList;
    private PesananBaruClickListener itemClickListener;
    private List<String> bossName;

    public PesananBaruAdapter(List<ServiceSchedule> serviceSchedules, PesananBaruClickListener pesananBaruClickListener, List<String> bossName) {
        this.serviceScheduleList = serviceSchedules;
        this.itemClickListener = pesananBaruClickListener;
        this.bossName = bossName;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.recycler_view_pesanan_baru, viewGroup, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ServiceSchedule serviceSchedule = serviceScheduleList.get(position);
        holder.tanggalOrderTV.setText(serviceSchedule.getOrderDate() + " " +serviceSchedule.getOrderMonth());
        holder.jamOrderTV.setText(serviceSchedule.getOrderTime());
        holder.nameTypeTV.setText(bossName.get(position) + " - " + serviceSchedule.getServiceType());
        holder.addressTV.setText(serviceSchedule.getAddress());
    }

    @Override
    public int getItemCount() {
        return serviceScheduleList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tanggalOrderTV, jamOrderTV, nameTypeTV, addressTV;
        ImageView rejectButton, acceptButton;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tanggalOrderTV = itemView.findViewById(R.id.tanggal_order_TV);
            jamOrderTV = itemView.findViewById(R.id.jam_order_TV);
            nameTypeTV = itemView.findViewById(R.id.name_type_TV);
            addressTV = itemView.findViewById(R.id.address_TV);
            rejectButton = itemView.findViewById(R.id.reject_button);
            acceptButton = itemView.findViewById(R.id.accept_button);

            rejectButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    itemClickListener.onRejectClick(position);
                }
            });
            acceptButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    itemClickListener.onAcceptClick(position);
                }
            });
        }
    }
}
