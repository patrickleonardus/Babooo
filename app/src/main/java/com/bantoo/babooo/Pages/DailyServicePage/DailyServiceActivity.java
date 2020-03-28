package com.bantoo.babooo.Pages.DailyServicePage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bantoo.babooo.R;

public class DailyServiceActivity extends AppCompatActivity {

    LinearLayout serviceOpt1,serviceOpt2,serviceOpt3,serviceOpt4;
    TextView serviceName1,serviceName2,serviceName3,serviceName4;
    TextView detailService1,detailService2,detailService3,detailService4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_service);

        serviceOpt1 = findViewById(R.id.first_daily_service_layout);
        serviceOpt2 = findViewById(R.id.second_daily_service_layout);
        serviceOpt3 = findViewById(R.id.third_daily_service_layout);
        serviceOpt4 = findViewById(R.id.fourth_daily_service_layout);

        serviceName1 = findViewById(R.id.service_name_daily_TV_1);
        serviceName2 = findViewById(R.id.service_name_daily_TV_2);
        serviceName3 = findViewById(R.id.service_name_daily_TV_3);
        serviceName4 = findViewById(R.id.service_name_daily_TV_4);

        detailService1 = findViewById(R.id.service_detail_daily_TV_1);
        detailService2 = findViewById(R.id.service_detail_daily_TV_2);
        detailService3 = findViewById(R.id.service_detail_daily_TV_3);
        detailService4 = findViewById(R.id.service_detail_daily_TV_4);

        generalStyling();
        initVar();
        handleMenu();

    }

    public void generalStyling(){
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(getApplicationContext(),R.color.orangePrimary));
        window.setNavigationBarColor(ContextCompat.getColor(getApplicationContext(),R.color.greenPrimary));
    }

    private void initVar(){
        serviceName1.setText("Cuci Kering");
        detailService1.setText("Layanan mencuci dan menjemur pakaian maksimal 15kg selama pengerjaan");
        serviceName2.setText("Setrika Baju");
        detailService2.setText("Layanan Menyetrika baju bersih");
        serviceName3.setText("Pembersihan Umum");
        detailService3.setText("Layanan pembersihan umum meliputi menyapu, mengepel dan mengelap");
        serviceName4.setText("Pembersihan Kamar Mandi");
        detailService4.setText("Layanan pembersihan kamar mandi");
    }

    private void handleMenu(){

        serviceOpt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DailyServiceActivity.this,serviceName1.getText().toString(),Toast.LENGTH_SHORT).show();
            }
        });

        serviceOpt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DailyServiceActivity.this,serviceName2.getText().toString(),Toast.LENGTH_SHORT).show();
            }
        });

        serviceOpt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DailyServiceActivity.this,serviceName3.getText().toString(),Toast.LENGTH_SHORT).show();
            }
        });

        serviceOpt4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DailyServiceActivity.this,serviceName4.getText().toString(),Toast.LENGTH_SHORT).show();
            }
        });

    }

}
