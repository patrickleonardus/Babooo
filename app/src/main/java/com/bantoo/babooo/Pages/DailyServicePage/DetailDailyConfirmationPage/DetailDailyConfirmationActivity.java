package com.bantoo.babooo.Pages.DailyServicePage.DetailDailyConfirmationPage;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bantoo.babooo.R;
import com.bantoo.babooo.Utilities.BaseActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DetailDailyConfirmationActivity extends BaseActivity {

    private static final String TAG = "DetailDailyConfirmation";

    TextView maidNameTV, maidRatingTV, callMaidTV, maidStatusTV,
            orderNumberTV, dateOfWorkTV, timeOfWorkTV, estimatedFinishTimeOfWorkTV,
            orderLocationTV, serviceNameTV, serviceTypeTV, serviceCostTV, serviceFeeTV,
            serviceFeeCostTV, totalCostTV;
    ImageView maidProfilePictureIV, closeIV;
    Button helpBantooBTN;
    private String orderUniqueKey;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference orderReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_daily_confirmation);

        initView();
        handleAction();
        setupFirebase();
        setInformation();
    }

    private void setupFirebase() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        orderReference = firebaseDatabase.getReference().child("Order");
        orderUniqueKey = getIntent().getStringExtra("orderUniqueKey");
    }

    private void setInformation() {
        orderReference.child(orderUniqueKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d(TAG, "setInformation: "+snapshot);
                maidNameTV.setText(snapshot.child("maid").getValue().toString());
                dateOfWorkTV.setText(snapshot.child("orderDate").getValue()+"-"+snapshot.child("orderMonth").getValue()+"-"+snapshot.child("orderYear").getValue());
                timeOfWorkTV.setText(snapshot.child("orderTime").getValue().toString());
                int finishEstimationHours = Integer.parseInt(snapshot.child("orderTime").getValue().toString().substring(0,2)) + 4;
                String finishEstimationMinutes = snapshot.child("orderTime").getValue().toString().substring(3,5);
                if(finishEstimationHours < 10) { estimatedFinishTimeOfWorkTV.setText("0"+finishEstimationMinutes+":"+finishEstimationMinutes); }
                else { estimatedFinishTimeOfWorkTV.setText(finishEstimationHours+":"+finishEstimationMinutes); }
                orderLocationTV.setText(snapshot.child("address").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void initView() {
        maidNameTV = findViewById(R.id.maid_name_detail_daily_confirmation_TV);
        maidRatingTV = findViewById(R.id.maid_rating_detail_daily_confirmation_TV);
        maidStatusTV = findViewById(R.id.maid_status_detail_daily_confirmation_TV);
        callMaidTV = findViewById(R.id.maid_call_detail_daily_confirmation_TV);
        orderNumberTV = findViewById(R.id.maid_order_number_detail_daily_confirmation_TV);
        maidProfilePictureIV = findViewById(R.id.maid_image_detail_daily_confirmation_IV);
        dateOfWorkTV = findViewById(R.id.date_detail_daily_confirmation_TV);
        timeOfWorkTV = findViewById(R.id.time_start_detail_daily_confirmation_TV);
        estimatedFinishTimeOfWorkTV = findViewById(R.id.estimatedTime_detail_daily_confirmation_TV);
        orderLocationTV = findViewById(R.id.location_detail_daily_confirmation_TV);
        serviceTypeTV = findViewById(R.id.type_service_detail_daily_confirmation_TV);
        serviceNameTV = findViewById(R.id.name_service_detail_daily_confirmation_TV);
        serviceCostTV = findViewById(R.id.cost_service_detail_daily_confirmation_TV);
        serviceFeeTV = findViewById(R.id.fee_service_detail_daily_confirmation_TV);
        serviceFeeCostTV = findViewById(R.id.fee_cost_service_detail_daily_confirmation_TV);
        totalCostTV = findViewById(R.id.total_cost_service_detail_daily_confirmation_TV);
        helpBantooBTN = findViewById(R.id.help_bantoo_detail_daily_confirmation_BTN);
        closeIV = findViewById(R.id.close_detail_daily_IV);
    }

    private void handleAction(){
        closeIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        helpBantooBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //call ART
            }
        });
    }
}
