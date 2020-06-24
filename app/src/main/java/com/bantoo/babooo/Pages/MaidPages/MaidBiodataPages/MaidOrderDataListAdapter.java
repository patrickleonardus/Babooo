package com.bantoo.babooo.Pages.MaidPages.MaidBiodataPages;

import android.app.Service;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bantoo.babooo.Model.ServiceSchedule;
import com.bantoo.babooo.R;

import java.util.List;

public class MaidOrderDataListAdapter extends RecyclerView.Adapter<MaidOrderDataListAdapter.MyViewHolder> {

    private Context context;
    private List<ServiceSchedule> serviceScheduleList;

    private static final String[] monthNames = {"Januari", "Februari", "Maret", "April",
        "Mei", "Juni", "Juli", "Agustus", "September", "Oktober", "November", "Desember"};

    public MaidOrderDataListAdapter(Context context, List<ServiceSchedule> serviceScheduleList) {
        this.context = context;
        this.serviceScheduleList = serviceScheduleList;
    }

    @NonNull
    @Override
    public MaidOrderDataListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_view_maid_order_experience, viewGroup, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MaidOrderDataListAdapter.MyViewHolder holder, int position) {
        ServiceSchedule serviceSchedule = serviceScheduleList.get(position);
        if(Integer.parseInt(serviceSchedule.getDuration()) % 12 == 0) {
            holder.durationTV.setText(""+(Integer.parseInt(serviceSchedule.getDuration())/12));
            holder.satuanDurationTV.setText("Tahun");
        } else {
            holder.durationTV.setText(serviceSchedule.getDuration());
            holder.satuanDurationTV.setText("Bulan");
        }
        holder.orderTypeTV.setText(serviceSchedule.getServiceType());
        int orderMonth = Integer.parseInt(serviceSchedule.getOrderMonth());
        int totalYear = 0;
        int endMonth = orderMonth;
        for(int i=0; i<Integer.parseInt(serviceSchedule.getDuration()); i++) {
            endMonth++;
            if(endMonth > 12 ) {
                totalYear++;
                endMonth = 1;
            }
        }
        int orderYear = Integer.parseInt(serviceSchedule.getOrderYear());
        holder.periodeOrderTV.setText(monthNames[orderMonth-1]+" "+orderYear+" - "+monthNames[endMonth-1]+" "+(orderYear+totalYear));
        holder.ratingTV.setText(""+serviceSchedule.getRating());
    }

    @Override
    public int getItemCount() {
        return serviceScheduleList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView durationTV, satuanDurationTV, orderTypeTV, periodeOrderTV, ratingTV;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            durationTV = itemView.findViewById(R.id.duration_TV);
            satuanDurationTV = itemView.findViewById(R.id.satuan_duration_TV);
            orderTypeTV = itemView.findViewById(R.id.tipe_layanan_TV);
            periodeOrderTV = itemView.findViewById(R.id.periode_layanan_TV);
            ratingTV = itemView.findViewById(R.id.rating_order_TV);
        }
    }
}
