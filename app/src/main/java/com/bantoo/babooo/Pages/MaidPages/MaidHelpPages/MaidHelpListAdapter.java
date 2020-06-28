package com.bantoo.babooo.Pages.MaidPages.MaidHelpPages;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bantoo.babooo.Model.Report;
import com.bantoo.babooo.R;

import java.util.List;


public class MaidHelpListAdapter extends RecyclerView.Adapter<MaidHelpListAdapter.MyViewHolder> {

    private List<Report> reportList;
    private MaidHelpListClickedListener helpListClickedListener;

    public MaidHelpListAdapter(List<Report> reportList, MaidHelpListClickedListener helpListClickedListener) {
        this.reportList = reportList;
        this.helpListClickedListener = helpListClickedListener;
    }

    @NonNull
    @Override
    public MaidHelpListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_view_help_list, viewGroup, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MaidHelpListAdapter.MyViewHolder holder, int position) {
        Report report = reportList.get(position);
        holder.reportDateTV.setText(report.getReportDate());
        holder.reportTypeTV.setText(report.getReportType());
        holder.reportStatusTV.setText(report.getReportStatus());
    }

    @Override
    public int getItemCount() {
        return reportList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView reportDateTV, reportTypeTV, reportStatusTV;
        LinearLayout linearLayout_reportList;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            reportDateTV = itemView.findViewById(R.id.report_dateTV);
            reportTypeTV = itemView.findViewById(R.id.report_typeTV);
            reportStatusTV = itemView.findViewById(R.id.report_statusTV);
            linearLayout_reportList = itemView.findViewById(R.id.linearLayout_reportList);
            linearLayout_reportList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    helpListClickedListener.onClickReportList(position);
                }
            });
        }
    }
}
