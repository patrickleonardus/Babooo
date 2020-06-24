package com.bantoo.babooo.Pages.HomePage.OrderPage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.IntegerRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bantoo.babooo.Model.ServiceSchedule;
import com.bantoo.babooo.R;

import java.util.List;

public class HistoryRecyclerViewAdapter extends RecyclerView.Adapter<HistoryRecyclerViewAdapter.MyViewHolder> {

    public final String[] MONTH_NAMES = {
            "Januari", "Februari", "Maret", "April",  "Mei", "Juni",
            "Juli", "Agustus", "September", "Oktober", "November", "Desember"
    };

    private List<ServiceSchedule> serviceSchedule;
    private Context context;
    private OrderItemClickListener orderItemClickListener;

    public HistoryRecyclerViewAdapter(Context context, List<ServiceSchedule> serviceSchedule, OrderItemClickListener orderItemClickListener) {
        this.context = context;
        this.serviceSchedule = serviceSchedule;
        this.orderItemClickListener = orderItemClickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.cell_history_order, viewGroup, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ServiceSchedule getOrder = serviceSchedule.get(position);
        holder.tanggalTV.setText(getOrder.getOrderDate() + " " + getOrder.getOrderMonth());
        holder.serviceNameTV.setText(getOrder.getServiceType());
        holder.statusOrderTV.setText(getOrder.getStatus());
        holder.timeOrderTV.setText(getOrder.getOrderTime());
        if(getOrder.getServiceType().contains("Bulanan")) {
            holder.typeServiceTV.setText("Layanan Bantoo Bulanan");
            holder.timeOrderTV.setVisibility(View.GONE);
        } else {
            holder.typeServiceTV.setText("Layanan Bantoo Harian");
        }
    }

    @Override
    public int getItemCount() {
        return serviceSchedule.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tanggalTV, serviceNameTV, statusOrderTV, timeOrderTV, typeServiceTV;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tanggalTV = itemView.findViewById(R.id.tanggal_TV_history_order);
            serviceNameTV = itemView.findViewById(R.id.service_name_TV_history_cell);
            statusOrderTV = itemView.findViewById(R.id.status_order_TV_history_cell);
            timeOrderTV = itemView.findViewById(R.id.time_TV_cell_history);
            typeServiceTV = itemView.findViewById(R.id.type_service_tv);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    orderItemClickListener.onItemClick(position);
                }
            });
        }
    }
}
