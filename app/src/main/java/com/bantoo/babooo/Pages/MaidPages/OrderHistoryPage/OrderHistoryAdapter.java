package com.bantoo.babooo.Pages.MaidPages.OrderHistoryPage;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bantoo.babooo.Model.ServiceSchedule;
import com.bantoo.babooo.R;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.MyViewHolder> {

    private List<ServiceSchedule> serviceScheduleList;
    private List<String> bossNameList;
    private OrderHistoryClickedRV orderHistoryClickedRV;

    public OrderHistoryAdapter(List<ServiceSchedule> serviceScheduleList, List<String> bossNameList, OrderHistoryClickedRV orderHistoryClickedRV) {
        this.serviceScheduleList = serviceScheduleList;
        this.bossNameList = bossNameList;
        this.orderHistoryClickedRV = orderHistoryClickedRV;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.recycler_view_riwayat_pesanan, viewGroup, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        int monthNumber = Integer.parseInt(serviceScheduleList.get(position).getOrderMonth());
        String bulan = new DateFormatSymbols().getMonths()[monthNumber-1];
        holder.tanggalTV.setText(serviceScheduleList.get(position).getOrderDate());
        holder.bulanTV.setText(bulan);
        if(position == 0) {
            holder.tanggalLL.setVisibility(View.VISIBLE);
            Log.d("RiwayatPesanan", "onBindViewHolder: position 0");
        } else if (!serviceScheduleList.get(position).getOrderDate().equals(serviceScheduleList.get(position - 1).getOrderDate())
                || !serviceScheduleList.get(position).getOrderMonth().equals(serviceScheduleList.get(position - 1).getOrderMonth())) {
            holder.tanggalLL.setVisibility(View.VISIBLE);
            Log.d("RiwayatPesanan", "onBindViewHolder: date and month diff");
        } else if (serviceScheduleList.get(position).getOrderDate().equals(serviceScheduleList.get(position - 1).getOrderDate())
                && serviceScheduleList.get(position).getOrderMonth().equals(serviceScheduleList.get(position - 1).getOrderMonth())) {
            holder.tanggalLL.setVisibility(View.INVISIBLE);
            Log.d("RiwayatPesanan", "onBindViewHolder: date and month same");
        }

        if(position != serviceScheduleList.size() - 1) {
            if(serviceScheduleList.get(position).getOrderDate().equals(serviceScheduleList.get(position + 1).getOrderDate())
                && serviceScheduleList.get(position).getOrderMonth().equals(serviceScheduleList.get(position + 1).getOrderMonth())) {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                params.setMargins(10, 0, 10, 0);
                holder.tanggalLL.setLayoutParams(params);
            }
        }


        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        try {
            Date estimatedDoneOrder = sdf.parse(serviceScheduleList.get(position).getOrderTime());
            Calendar c = Calendar.getInstance();
            c.setTime(estimatedDoneOrder);
            c.add(Calendar.HOUR, 2);
            estimatedDoneOrder = c.getTime();
            String timeDone = estimatedDoneOrder.getHours()+":"+estimatedDoneOrder.getMinutes();
            holder.jamOrderTV.setText(serviceScheduleList.get(position).getOrderTime() + " - " + timeDone);
        } catch (Exception e) {
            holder.jamOrderTV.setText(serviceScheduleList.get(position).getOrderTime());
        }
        holder.nameTypeTV.setText(bossNameList.get(position) + " - " + serviceScheduleList.get(position).getServiceType());
        holder.addressTV.setText(serviceScheduleList.get(position).getAddress());
        if (serviceScheduleList.get(position).getRating() != null) {
            holder.ratingTV.setText(serviceScheduleList.get(position).getRating().toString());
        } else {
            holder.ratingTV.setText("-");
        }
    }

    @Override
    public int getItemCount() {
        return serviceScheduleList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        LinearLayout tanggalLL, fullLL;
        TextView tanggalTV, bulanTV, jamOrderTV, nameTypeTV, addressTV, ratingTV;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            fullLL = itemView.findViewById(R.id.fullLL);
            tanggalLL = itemView.findViewById(R.id.tanggal_LL);
            tanggalTV = itemView.findViewById(R.id.tanggal_TV);
            bulanTV = itemView.findViewById(R.id.bulan_TV);
            jamOrderTV = itemView.findViewById(R.id.jam_order_TV);
            nameTypeTV = itemView.findViewById(R.id.name_type_TV);
            addressTV = itemView.findViewById(R.id.address_TV);
            ratingTV = itemView.findViewById(R.id.rating_TV);

            fullLL.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    orderHistoryClickedRV.onClickOrderHistory(position);
                }
            });
        }
    }
}
