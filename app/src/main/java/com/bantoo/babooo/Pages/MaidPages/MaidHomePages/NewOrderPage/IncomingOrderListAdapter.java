package com.bantoo.babooo.Pages.MaidPages.MaidHomePages.NewOrderPage;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bantoo.babooo.Model.ServiceSchedule;
import com.bantoo.babooo.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class IncomingOrderListAdapter extends RecyclerView.Adapter<IncomingOrderListAdapter.MyViewHolder> {

    List<ServiceSchedule> serviceScheduleList;
    List<String> bossName;

    public IncomingOrderListAdapter(List<ServiceSchedule> serviceScheduleList, List<String> bossName) {
        this.serviceScheduleList = serviceScheduleList;
        this.bossName = bossName;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.recycler_view_pesanan_list, viewGroup, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String orderTime = serviceScheduleList.get(position).getOrderTime();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        try {
            Date estimatedDoneOrder = sdf.parse(orderTime);
            Calendar c = Calendar.getInstance();
            c.setTime(estimatedDoneOrder);
            c.add(Calendar.HOUR, 2);
            estimatedDoneOrder = c.getTime();
            String minutesDone = ""+estimatedDoneOrder.getMinutes();
            if(estimatedDoneOrder.getMinutes() < 10) {
                minutesDone = "0"+estimatedDoneOrder.getMinutes();
            }
            holder.jamPesananTV.setText(orderTime + " - " +estimatedDoneOrder.getHours() + ":"+minutesDone);
        } catch (Exception e) {}
        holder.nameTypeTV.setText(bossName.get(position) + " - " +serviceScheduleList.get(position).getServiceType());
        holder.addressTV.setText(serviceScheduleList.get(position).getAddress());
    }

    @Override
    public int getItemCount() {
        return serviceScheduleList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView jamPesananTV, nameTypeTV, addressTV;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            jamPesananTV = itemView.findViewById(R.id.jam_pesanan_TV);
            nameTypeTV = itemView.findViewById(R.id.name_type_TV);
            addressTV = itemView.findViewById(R.id.address_TV);
        }
    }
}
