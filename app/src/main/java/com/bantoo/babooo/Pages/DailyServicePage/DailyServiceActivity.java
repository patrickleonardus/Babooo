package com.bantoo.babooo.Pages.DailyServicePage;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bantoo.babooo.Pages.DailyServicePage.DailyConfirmationPage.DailyConfirmationActivity;
import com.bantoo.babooo.R;
import com.bantoo.babooo.Utilities.BaseActivity;

public class DailyServiceActivity extends BaseActivity {

    LinearLayout serviceOpt1Layout, serviceOpt2Layout, serviceOpt3Layout, serviceOpt4Layout;
    TextView serviceName1TV, serviceName2TV, serviceName3TV, serviceName4TV;
    TextView detailService1TV, detailService2TV, detailService3TV, detailService4TV;

    static final String SERVICETYPE = "serviceType";
    static final String SERVICENAME = "serviceName";
    static final String SERVICECOINS = "serviceCoin";
    static final String thisServiceType = "Bantoo Harian";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_service);

        serviceOpt1Layout = findViewById(R.id.first_daily_service_layout);
        serviceOpt2Layout = findViewById(R.id.second_daily_service_layout);
        serviceOpt3Layout = findViewById(R.id.third_daily_service_layout);
        serviceOpt4Layout = findViewById(R.id.fourth_daily_service_layout);

        serviceName1TV = findViewById(R.id.service_name_daily_TV_1);
        serviceName2TV = findViewById(R.id.service_name_daily_TV_2);
        serviceName3TV = findViewById(R.id.service_name_daily_TV_3);
        serviceName4TV = findViewById(R.id.service_name_daily_TV_4);

        detailService1TV = findViewById(R.id.service_detail_daily_TV_1);
        detailService2TV = findViewById(R.id.service_detail_daily_TV_2);
        detailService3TV = findViewById(R.id.service_detail_daily_TV_3);
        detailService4TV = findViewById(R.id.service_detail_daily_TV_4);

        initVar();
        handleMenu();

    }

    private void initVar() {
        serviceName1TV.setText("Cuci Kering");
        detailService1TV.setText("Layanan mencuci dan menjemur pakaian maksimal 15kg selama pengerjaan");
        serviceName2TV.setText("Setrika Baju");
        detailService2TV.setText("Layanan Menyetrika baju bersih");
        serviceName3TV.setText("Pembersihan Umum");
        detailService3TV.setText("Layanan pembersihan umum meliputi menyapu, mengepel dan mengelap");
        serviceName4TV.setText("Pembersihan Kamar Mandi");
        detailService4TV.setText("Layanan pembersihan kamar mandi");
    }

    private void handleMenu() {

        serviceOpt1Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveToConfirmationPage(thisServiceType, "Cuci Kering",100);
            }
        });

        serviceOpt2Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveToConfirmationPage(thisServiceType, "Setrika Baju",100);
            }
        });

        serviceOpt3Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveToConfirmationPage(thisServiceType, "Pembersihan Umum",100);
            }
        });

        serviceOpt4Layout.setOnClickListener(new View.OnClickListener() {
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
