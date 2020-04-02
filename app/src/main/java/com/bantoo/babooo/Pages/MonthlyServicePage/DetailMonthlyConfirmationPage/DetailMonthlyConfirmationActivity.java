package com.bantoo.babooo.Pages.MonthlyServicePage.DetailMonthlyConfirmationPage;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bantoo.babooo.R;
import com.bantoo.babooo.Utilities.BaseActivity;

public class DetailMonthlyConfirmationActivity extends BaseActivity {

    TextView maidNameTV, maidRatingTV, callMaidTV, maidStatusTV, durationOfWorkTV,
            orderNumberTV, dateOfWorkTV, timeOfWorkTV, estimatedFinishTimeOfWorkTV,
            orderLocationTV, serviceNameTV, serviceTypeTV, serviceCostTV, serviceFeeTV,
            serviceFeeCostTV, totalCostTV;
    ImageView maidProfilePictureIV, closeIV;
    Button helpBantooBTN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_monthly_confirmation);

        closeIV = findViewById(R.id.close_detail_monthly_IV);
        orderNumberTV = findViewById(R.id.maid_order_number_detail_monthly_confirmation_TV);
        maidProfilePictureIV = findViewById(R.id.maid_image_detail_monthly_confirmation_IV);
        maidNameTV = findViewById(R.id.maid_name_detail_monthly_confirmation_TV);
        maidRatingTV = findViewById(R.id.maid_rating_detail_monthly_confirmation_TV);
        maidStatusTV = findViewById(R.id.maid_status_detail_monthly_confirmation_TV);
        callMaidTV = findViewById(R.id.maid_call_detail_monthly_confirmation_TV);
        durationOfWorkTV = findViewById(R.id.durasi_maid_detail_monthly_confirmation_TV);
        dateOfWorkTV = findViewById(R.id.date_detail_monthly_confirmation_TV);
        timeOfWorkTV = findViewById(R.id.time_start_detail_monthly_confirmation_TV);
        estimatedFinishTimeOfWorkTV = findViewById(R.id.estimatedTime_detail_monthly_confirmation_TV);
        orderLocationTV = findViewById(R.id.location_detail_monthly_confirmation_TV);
        serviceTypeTV = findViewById(R.id.type_service_detail_monthly_confirmation_TV);
        serviceNameTV = findViewById(R.id.name_service_detail_monthly_confirmation_TV);
        serviceFeeTV = findViewById(R.id.fee_service_detail_monthly_confirmation_TV);
        serviceCostTV = findViewById(R.id.cost_service_detail_monthly_confirmation_TV);
        serviceFeeCostTV = findViewById(R.id.fee_cost_service_detail_monthly_confirmation_TV);
        totalCostTV = findViewById(R.id.total_cost_service_detail_monthly_confirmation_TV);
        helpBantooBTN = findViewById(R.id.help_bantoo_detail_monthly_confirmation_BTN);


        handleAction();
    }

    private void handleAction(){
        closeIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}
