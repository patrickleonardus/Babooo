package com.bantoo.babooo.Pages.HomePage.ServicePage;

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

public class ServiceRecyclerViewAdapter extends RecyclerView.Adapter<ServiceRecyclerViewAdapter.MyViewHolder> {

    private Context context;
    private List<ServiceSchedule> serviceScheduleList;

    public ServiceRecyclerViewAdapter(Context context, List<ServiceSchedule> serviceScheduleList){
        this.context = context;
        this.serviceScheduleList = serviceScheduleList;
    }

    @Override
    public ServiceRecyclerViewAdapter.MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i){
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.recycler_view_service, viewGroup, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ServiceRecyclerViewAdapter.MyViewHolder viewHolder, int i){
        viewHolder.servName.setText(serviceScheduleList.get(i).getServiceType());
        viewHolder.maidName.setText(serviceScheduleList.get(i).getMaid());
        viewHolder.hourServ.setText(serviceScheduleList.get(i).getOrderTime());
        viewHolder.addressServ.setText(serviceScheduleList.get(i).getAddress());
        viewHolder.dayServ.setText(serviceScheduleList.get(i).getOrderDate());
        viewHolder.monthServ.setText(serviceScheduleList.get(i).getOrderMonth());
        viewHolder.status.setText(serviceScheduleList.get(i).getStatus());
    }

    @Override
    public int getItemCount() {
        return serviceScheduleList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView servName,maidName,hourServ,addressServ,dayServ,monthServ,status;

        public MyViewHolder (View view){
            super(view);

            servName = view.findViewById(R.id.serviceName_services_TV);
            maidName = view.findViewById(R.id.maidName_services_TV);
            hourServ = view.findViewById(R.id.hourService_services_TV);
            addressServ = view.findViewById(R.id.address_services_TV);
            dayServ = view.findViewById(R.id.dayService_services_TV);
            monthServ = view.findViewById(R.id.monthService_services_TV);
            status = view.findViewById(R.id.status_services_TV);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                }
            });
        }
    }
}
