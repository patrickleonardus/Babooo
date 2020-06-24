package com.bantoo.babooo.Pages.MaidPages.MaidHelpPages;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Database;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bantoo.babooo.Model.Report;
import com.bantoo.babooo.Model.ReportComparator;
import com.bantoo.babooo.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class MaidHelpActivity extends AppCompatActivity  implements MaidHelpListClickedListener{

    private RecyclerView helpReportRV;
    private FloatingActionButton addNewReport;
    private ImageView backIV;

    private MaidHelpListAdapter maidHelpListAdapter;
    private List<Report> reportList = new ArrayList<>();

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference reportReference;
    private String phoneNumber, artType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maid_help);

        initView();
        handleAction();
        initFirebase();
        setRecyclerView();
        loadReportList();
    }



    public void loadReportList(){
        reportList.clear();
        reportReference.orderByChild("phoneNumber").equalTo(phoneNumber).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    SimpleDateFormat sdfDate = new SimpleDateFormat("dd MM yyyy, HH:mm");
                    String date = sdfDate.format(new Date(Long.parseLong(snapshot.child("reportTimeStamp").getValue().toString())));
                    String type = snapshot.child("reportType").getValue().toString();
                    String status = snapshot.child("reportStatus").getValue().toString();
                    String story = snapshot.child("reportStory").getValue().toString();
                    String phone = snapshot.child("phoneNumber").getValue().toString();
                    String key = snapshot.getKey();

                    Report report = new Report(Long.parseLong(snapshot.child("reportTimeStamp").getValue().toString()), type, status, story, phone);
                    report.setReportDate(date);
                    report.setReportKey(key);
                    reportList.add(report);
                }
                Collections.sort(reportList, new ReportComparator());
                maidHelpListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }

    public void initFirebase(){
        firebaseDatabase = FirebaseDatabase.getInstance();
        reportReference = firebaseDatabase.getReference().child("Report");

        SharedPreferences sharedPreferences = getSharedPreferences("accountData", Context.MODE_PRIVATE);
        phoneNumber = sharedPreferences.getString("phoneNumber", "");
        artType = sharedPreferences.getString("artType", "");
    }

    public void setRecyclerView(){
        //configure recycler view
        maidHelpListAdapter = new MaidHelpListAdapter(reportList, this);
        helpReportRV.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        helpReportRV.setAdapter(maidHelpListAdapter);
    }

    public void handleAction(){

        addNewReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addNewReportIntent = new Intent(MaidHelpActivity.this, MaidHelpAddNewActivity.class);
                startActivity(addNewReportIntent);
            }
        });

        backIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void initView(){
        helpReportRV = findViewById(R.id.report_list_RV);
        addNewReport = findViewById(R.id.add_new_report);
        backIV = findViewById(R.id.back_IV);
    }

    @Override
    public void onClickReportList(int position) {
        Intent showDetailReport = new Intent(this, MaidHelpDetailActivity.class);
        showDetailReport.putExtra("reportDate", reportList.get(position).getReportDate());
        showDetailReport.putExtra("reportType", reportList.get(position).getReportType());
        showDetailReport.putExtra("reportStory", reportList.get(position).getReportStory());
        showDetailReport.putExtra("reportStatus", reportList.get(position).getReportStatus());
        showDetailReport.putExtra("reportKey", reportList.get(position).getReportKey());
        startActivity(showDetailReport);

    }
}
