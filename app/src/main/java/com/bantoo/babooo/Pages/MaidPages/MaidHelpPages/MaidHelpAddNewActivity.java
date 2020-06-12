package com.bantoo.babooo.Pages.MaidPages.MaidHelpPages;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bantoo.babooo.Model.Report;
import com.bantoo.babooo.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

public class MaidHelpAddNewActivity extends AppCompatActivity  {

    final static String callCenterBantoo = "0215512345";

    private Button callHelp, submitReport;
    private Spinner typeHelp;
    private EditText reportStory;
    private ImageView closeIV;

    private String reportStatus, reportType;
    private Integer reportTimeStamp;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference reportReference;
    private String phoneNumber, reportUniqueKey;
    private Report report;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maid_help_add_new);

        initView();
        handleAction();
    }

    private void handleAction(){
        callHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:"+callCenterBantoo.replaceFirst("0", "+62")));
                startActivity(callIntent);
            }
        });

        submitReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (reportStory.getText().toString().equals("") || typeHelp.getSelectedItem().toString().equals("")) {
                    Toast.makeText(MaidHelpAddNewActivity.this, "Pastikan semua bagian terisi", Toast.LENGTH_SHORT).show();
                }
                else {
                    reportType = typeHelp.getSelectedItem().toString();
                    SharedPreferences sharedPreferences = getSharedPreferences("accountData", Context.MODE_PRIVATE);
                    phoneNumber = sharedPreferences.getString("phoneNumber", "");
                    report = new Report(reportTimeStamp, reportType, reportStatus, reportStory.getText().toString(), phoneNumber);
                    report.setReportStatus("Laporan diproses");
                    saveReportToFirebase();

                    Intent moveToHelpList = new Intent(MaidHelpAddNewActivity.this, MaidHelpActivity.class);
                    startActivity(moveToHelpList);
                }
            }
        });

        closeIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void saveReportToFirebase(){
        firebaseDatabase = FirebaseDatabase.getInstance();
        reportReference = firebaseDatabase.getReference().child("Report");

        reportUniqueKey = reportReference.push().getKey();
        reportReference.child(reportUniqueKey).setValue(report);
        reportReference.child(reportUniqueKey).child("reportTimeStamp").setValue(ServerValue.TIMESTAMP);
    }

    private void initView(){
        callHelp = findViewById(R.id.call_help_BTN);
        typeHelp = findViewById(R.id.spinner_type_help);
        reportStory = findViewById(R.id.report_storyET);
        submitReport = findViewById(R.id.submit_new_report_BTN);
        closeIV = findViewById(R.id.close_add_report_IV);
    }
}
