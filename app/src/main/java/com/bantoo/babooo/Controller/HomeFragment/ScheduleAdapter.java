package com.bantoo.babooo.Controller.HomeFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bantoo.babooo.Model.ScheduleData;
import com.bantoo.babooo.R;

import java.util.Vector;


public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ViewHolder> {

    private Vector<ScheduleData> mDataset = new Vector<ScheduleData>();

    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView tvDate;
        public TextView tvMonth;
        public TextView tvServiceType;
        public TextView tvMaid;
        public TextView tvTime;
        public TextView tvAddress;
        public TextView tvStatus;
        public ViewHolder(View v) {
            super(v);
            tvDate = v.findViewById(R.id.tv_date);
            tvMonth = v.findViewById(R.id.tv_month);
            tvServiceType = v.findViewById(R.id.tv_serviceType);
            tvMaid = v.findViewById(R.id.tv_maid);
            tvTime = v.findViewById(R.id.tv_time);
            tvAddress = v.findViewById(R.id.tv_address);
            tvStatus = v.findViewById(R.id.tv_status);
        }
    }

    public ScheduleAdapter(Vector<ScheduleData> myDataset) {
        this.mDataset = myDataset;
    }

    @NonNull
    @Override
    public ScheduleAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.schedule_list, null);
        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ScheduleAdapter.ViewHolder holder, int position) {
        ScheduleData data = mDataset.get(position);
        holder.tvDate.setText(data.getOrderDate());
        holder.tvMonth.setText(data.getOrderMonth());
        holder.tvServiceType.setText(data.getServiceType());
        holder.tvMaid.setText(data.getMaid());
        holder.tvTime.setText(data.getOrderTime());
        holder.tvAddress.setText(data.getAddress());
        holder.tvStatus.setText(data.getStatus());
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
