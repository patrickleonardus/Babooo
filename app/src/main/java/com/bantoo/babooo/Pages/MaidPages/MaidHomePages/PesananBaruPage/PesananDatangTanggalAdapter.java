package com.bantoo.babooo.Pages.MaidPages.MaidHomePages.PesananBaruPage;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bantoo.babooo.Model.TanggalPesanan;
import com.bantoo.babooo.R;

import java.util.ArrayList;
import java.util.List;

public class PesananDatangTanggalAdapter extends RecyclerView.Adapter<PesananDatangTanggalAdapter.MyViewHolder> {

    private List<TanggalPesanan> tanggalPesananList;
    private PesananDatangTanggalClickListener pesananDatangTanggalClickListener;
    private static int currentPosition = 0;

    public void setCurrentPosition(int position) {
        this.currentPosition = position;
    }

    public PesananDatangTanggalAdapter(List<TanggalPesanan> tanggalPesananList, PesananDatangTanggalClickListener pesananDatangTanggalClickListener) {
        this.tanggalPesananList = tanggalPesananList;
        this.pesananDatangTanggalClickListener = pesananDatangTanggalClickListener;
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
        holder.tanggalTV.setText(tanggalPesananList.get(position).getTanggal());
        holder.bulanTV.setText(tanggalPesananList.get(position).getBulan());
    }

    @Override
    public int getItemCount() {
        return tanggalPesananList.size();
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
                    pesananDatangTanggalClickListener.onTanggalClick(position);
                    notifyDataSetChanged();
                }
            });
        }
    }
}
