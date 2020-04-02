package com.bantoo.babooo.Pages.DailyServicePage;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bantoo.babooo.Pages.DailyServicePage.DailyConfirmationPage.DailyConfirmationActivity;
import com.bantoo.babooo.R;
import com.bantoo.babooo.Utils.BaseActivity;

public class DailyServiceActivity extends BaseActivity {

    LinearLayout serviceOpt1, serviceOpt2, serviceOpt3, serviceOpt4;
    TextView serviceName1, serviceName2, serviceName3, serviceName4;
    TextView detailService1, detailService2, detailService3, detailService4;

    static final String SERVICETYPE = "serviceType";
    static final String SERVICENAME = "serviceName";
    static final String SERVICECOINS = "serviceCoin";
    static final String thisServiceType = "Bantoo Harian";

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

        initVar();
        handleMenu();

    }

    private void initVar() {
        serviceName1.setText("Cuci Kering");
        detailService1.setText("Layanan mencuci dan menjemur pakaian maksimal 15kg selama pengerjaan");
        serviceName2.setText("Setrika Baju");
        detailService2.setText("Layanan Menyetrika baju bersih");
        serviceName3.setText("Pembersihan Umum");
        detailService3.setText("Layanan pembersihan umum meliputi menyapu, mengepel dan mengelap");
        serviceName4.setText("Pembersihan Kamar Mandi");
        detailService4.setText("Layanan pembersihan kamar mandi");
    }

    private void handleMenu() {

        serviceOpt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveToConfirmationPage(thisServiceType, "Cuci Kering",100);
            }
        });

        serviceOpt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveToConfirmationPage(thisServiceType, "Setrika Baju",100);
            }
        });

        serviceOpt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveToConfirmationPage(thisServiceType, "Pembersihan Umum",100);
            }
        });

        serviceOpt4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveToConfirmationPage(thisServiceType, "Pembersihan Kamar Mandi",100);
            }
        });

    }

    private void moveToConfirmationPage(String valueServiceType, String valueServiceName, int coins) {
        Intent intent = new Intent(DailyServiceActivity.this, DailyConfirmationActivity.class);
        intent.putExtra(SERVICETYPE, valueServiceType);
        intent.putExtra(SERVICENAME, valueServiceName);
        intent.putExtra(SERVICECOINS, coins);
        startActivity(intent);
    }

}
