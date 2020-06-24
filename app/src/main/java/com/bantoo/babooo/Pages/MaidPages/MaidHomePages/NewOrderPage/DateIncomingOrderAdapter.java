package com.bantoo.babooo.Pages.MaidPages.MaidHomePages.NewOrderPage;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bantoo.babooo.Model.DateOrder;
import com.bantoo.babooo.R;

import java.util.List;

public class DateIncomingOrderAdapter extends RecyclerView.Adapter<DateIncomingOrderAdapter.MyViewHolder> {

    public final String[] MONTH_NAMES = {
            "Januari", "Februari", "Maret", "April", "Mei", "Juni",
            "Juli", "Agustus", "September", "Oktober", "November", "Desember"
    };

    private List<DateOrder> dateOrderList;
    private DateIncomingOrderClickListener dateIncomingOrderClickListener;
    private static int currentPosition = 0;

    public void setCurrentPosition(int position) {
        this.currentPosition = position;
    }

    public DateIncomingOrderAdapter(List<DateOrder> dateOrderList, DateIncomingOrderClickListener dateIncomingOrderClickListener) {
        this.dateOrderList = dateOrderList;
        this.dateIncomingOrderClickListener = dateIncomingOrderClickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.recycler_view_pesanan_datang_tanggal, viewGroup, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if(position == currentPosition) {
            holder.underlineView.setVisibility(View.VISIBLE);
        } else {
            holder.underlineView.setVisibility(View.INVISIBLE);
        }
        holder.tanggalTV.setText(dateOrderList.get(position).getDateOrder());
        int month = Integer.parseInt(dateOrderList.get(position).getMonthOrder());
        holder.bulanTV.setText(MONTH_NAMES[month-1]);
    }

    @Override
    public int getItemCount() {
        return dateOrderList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tanggalTV, bulanTV;
        LinearLayout tanggalLL;
        View underlineView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tanggalTV = itemView.findViewById(R.id.tanggal_TV);
            bulanTV = itemView.findViewById(R.id.bulan_TV);
            tanggalLL = itemView.findViewById(R.id.tanggal_LL);
            underlineView = itemView.findViewById(R.id.underline_view);

            tanggalLL.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    underlineView.setVisibility(View.INVISIBLE);
                    currentPosition = position;
                    dateIncomingOrderClickListener.onDateClick(position);
                    notifyDataSetChanged();
                }
            });
        }
    }
}
