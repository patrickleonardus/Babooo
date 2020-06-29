package com.bantoo.babooo.Pages.MaidPages.MaidHelpPages;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bantoo.babooo.R;
import com.bantoo.babooo.Utilities.BaseActivity;

public class MaidHelpDetailActivity extends BaseActivity {

    private TextView noPengajuanTV, tglPengajuanTV, reportTypeTV, reportStoryTV, reportStatusTV;
    private String reportKey, reportDate, reportType, reportStory, reportStatus;
    private ImageView closeIV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maid_help_detail);

        initView();
        handleAction();
        getExtra();
        setView();
    }

    private void setView(){
        noPengajuanTV.setText(reportKey);
        tglPengajuanTV.setText(reportDate);
        reportTypeTV.setText(reportType);
        reportStoryTV.setText(reportStory);
    }

    private void getExtra(){
        reportKey = getIntent().getStringExtra("reportKey");
        reportDate = getIntent().getStringExtra("reportDate");
        reportType = getIntent().getStringExtra("reportType");
        reportStory = getIntent().getStringExtra("reportStory");
        reportStatus = getIntent().getStringExtra("reportStatus");

    }

    private void handleAction(){
        closeIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initView(){

        noPengajuanTV = findViewById(R.id.nomor_pengajuan_TV);
        tglPengajuanTV = findViewById(R.id.tanggal_pengajuan_TV);
        reportTypeTV = findViewById(R.id.report_typeTV);
        reportStoryTV = findViewById(R.id.report_storyTV);
        reportStatusTV = findViewById(R.id.report_statusTV);
        closeIV = findViewById(R.id.close_report_det_IV);
    }
}
