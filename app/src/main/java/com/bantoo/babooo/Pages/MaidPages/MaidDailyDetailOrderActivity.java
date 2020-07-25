package com.bantoo.babooo.Pages.MaidPages;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bantoo.babooo.R;
import com.bantoo.babooo.Utilities.BaseActivity;
import com.ebanx.swipebtn.OnStateChangeListener;
import com.ebanx.swipebtn.SwipeButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MaidDailyDetailOrderActivity extends BaseActivity {

    private TextView orderNumberTV, userNameTV, statusTV, dateTV,
            timeStartTV, estimatedTimeTV, locationTV, serviceNameTV, costServiceTV,
            feeCostTV, totalIncomeTV, langkahBerikutTV;

    private ImageView progressBar1, progressBar2, progressBar3, progressBar4, progressBar5, callIV, messageIV,
            closeIV;
    private SwipeButton swipeButton;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference orderReference, userReference;
    private String orderUniqueKey, orderStatus;

    private int slideCounter = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maid_daily_detail_order);

        initView();
        initData();
        handleAction();
        if(orderUniqueKey != null) {
            showData();
        }
    }

    private void setProgressStep() {
        int step = 1;
        String nextStatus = "";
        if(orderStatus.equals("Akan Datang")) {
            step = 1;
        } else if(orderStatus.equals("Menuju Lokasi")) {
            step = 2;
        } else if(orderStatus.equals("Sudah Sampai")) {
            step = 3;
        } else if(orderStatus.equals("Dalam Pengerjaan")) {
            step = 4;
        } else if (orderStatus.equals("Pesanan Selesai")) {
            step = 5;
        }
        activateProgressStep(step);
        setNextStepLabel();
    }

    private void activateProgressStep(int step) {
        switch (step) {
            case 5:
                progressBar5.setImageResource(R.drawable.step_active_monthly);
            case 4:
                progressBar4.setImageResource(R.drawable.step_active_monthly);
            case 3:
                progressBar3.setImageResource(R.drawable.step_active_monthly);
            case 2:
                progressBar2.setImageResource(R.drawable.step_active_monthly);
            case 1:
                progressBar1.setImageResource(R.drawable.step_active_monthly);
        }
    }

    private void handleAction(){
        swipeButton.setOnStateChangeListener(new OnStateChangeListener() {
            @Override
            public void onStateChange(boolean active) {
                if(slideCounter % 2 == 0) {
                    slideCounter++;
                    return;
                }
                slideCounter++;
                int step = 1;
                String nextStatus = "";
                if(orderStatus.equals("Akan Datang")) {
                    nextStatus = "Menuju Lokasi";
                    step = 2;
                } else if(orderStatus.equals("Menuju Lokasi")) {
                    nextStatus = "Sudah Sampai";
                    step = 3;
                } else if(orderStatus.equals("Sudah Sampai")) {
                    nextStatus = "Dalam Pengerjaan";
                    step = 4;
                } else if(orderStatus.equals("Dalam Pengerjaan")) {
                    nextStatus = "Pesanan Selesai";
                    step = 5;
                } else if (orderStatus.equals("Pesanan Selesai")) {
                    swipeButton.setVisibility(View.GONE);
                    langkahBerikutTV.setVisibility(View.GONE);
                }
                statusTV.setText(nextStatus);
                updateStatusToFirebase(nextStatus);
                orderStatus = nextStatus;
                statusTV.setText(orderStatus);
                activateProgressStep(step);
                setNextStepLabel();
            }
        });
    }

    private void setNextStepLabel() {
        String nextStatus = "";
        if(orderStatus.equals("Akan Datang")) {
            nextStatus = "Menuju Lokasi";
        } else if(orderStatus.equals("Menuju Lokasi")) {
            nextStatus = "Sudah Sampai";
        } else if(orderStatus.equals("Sudah Sampai")) {
            nextStatus = "Dalam Pengerjaan";
        } else if(orderStatus.equals("Dalam Pengerjaan")) {
            nextStatus = "Pesanan Selesai";
        } else if (orderStatus.equals("Pesanan Selesai")) {
            swipeButton.setVisibility(View.GONE);
            langkahBerikutTV.setVisibility(View.GONE);
        }
        langkahBerikutTV.setText(""+nextStatus);
    }

    private void updateStatusToFirebase(String status) {
        orderReference.child("status").setValue(status);
    }

    private void initData() {
        if(getIntent().getStringExtra("orderKey") == null) {
            orderUniqueKey = "";
        } else {
            orderUniqueKey = getIntent().getStringExtra("orderKey");
        }
        firebaseDatabase = FirebaseDatabase.getInstance();
        orderReference = firebaseDatabase.getReference().child("Order").child(orderUniqueKey);
        userReference = firebaseDatabase.getReference().child("Users");
    }

    /*private void progress (int step) {
        switch (step) {
            case 5: progressBar5.setImageResource(R.drawable.asset_star_active);
            case 4: progressBar4.setImageResource(R.drawable.asset_star_active);
            case 3: progressBar3.setImageResource(R.drawable.asset_star_active);
            case 2: progressBar2.setImageResource(R.drawable.asset_star_active);
            case 1: progressBar1.setImageResource(R.drawable.asset_star_active);
        }
    }*/

    private void showUserData(String userPhoneNumber) {
        userReference.orderByChild("phoneNumber").equalTo(userPhoneNumber).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    userNameTV.setText(snapshot.child("name").getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void showData() {
        orderReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                orderNumberTV.setText(dataSnapshot.getKey());
                callIV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(Intent.ACTION_DIAL,
                                Uri.fromParts("tel", dataSnapshot.child("phoneNumber").getValue().toString(),
                                        null)));
                    }
                });
                messageIV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                        sendIntent.putExtra("sms_body", "Bantoo: ");
                        sendIntent.setType("vnd.android-dir/mms-sms");
                        sendIntent.putExtra("address", dataSnapshot.child("phoneNumber").getValue().toString());
                        startActivity(sendIntent);
                    }
                });
                orderStatus = dataSnapshot.child("status").getValue().toString();
                statusTV.setText(orderStatus);
                setProgressStep();
                setNextStepLabel();
                String orderTime = dataSnapshot.child("orderTime").getValue().toString();
                int finishEstimationHours = Integer.parseInt(orderTime.substring(0,2)) + 2;
                String finishEstimationMinutes = orderTime.substring(3,5);
                if(finishEstimationHours < 10) { estimatedTimeTV.setText("0"+finishEstimationMinutes+":"+finishEstimationMinutes); }
                else { estimatedTimeTV.setText(finishEstimationHours+":"+finishEstimationMinutes); }
                timeStartTV.setText(orderTime);
                String orderDate = dataSnapshot.child("orderDate").getValue().toString();
                String orderMonth = dataSnapshot.child("orderMonth").getValue().toString();
                String orderYear = dataSnapshot.child("orderYear").getValue().toString();
                dateTV.setText(orderDate + " - " + orderMonth + " - " + orderYear);
                locationTV.setText(dataSnapshot.child("address").getValue().toString());
                serviceNameTV.setText(dataSnapshot.child("serviceType").getValue().toString());
                costServiceTV.setText(dataSnapshot.child("serviceCost").getValue().toString());
                feeCostTV.setText("10");
                totalIncomeTV.setText(""+ (Integer.parseInt(dataSnapshot.child("serviceCost").getValue().toString()) - 10 ));
                showUserData(dataSnapshot.child("phoneNumber").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void initView() {
        closeIV = findViewById(R.id.closeIV);
        progressBar1 = findViewById(R.id.progressbar_1);
        progressBar2 = findViewById(R.id.progressbar_2);
        progressBar3 = findViewById(R.id.progressbar_3);
        progressBar4 = findViewById(R.id.progressbar_4);
        progressBar5 = findViewById(R.id.progressbar_5);
        callIV = findViewById(R.id.call_IV);
        messageIV = findViewById(R.id.message_IV);
        orderNumberTV = findViewById(R.id.order_number_TV);
        userNameTV = findViewById(R.id.user_name_TV);
        statusTV = findViewById(R.id.order_status_TV);
        dateTV = findViewById(R.id.date_detail_TV);
        timeStartTV = findViewById(R.id.time_start_detailTV);
        estimatedTimeTV = findViewById(R.id.estimated_time_TV);
        locationTV = findViewById(R.id.location_TV);
        serviceNameTV = findViewById(R.id.name_service_TV);
        costServiceTV = findViewById(R.id.cost_service_TV);
        feeCostTV = findViewById(R.id.fee_cost_TV);
        totalIncomeTV = findViewById(R.id.total_income_TV);
        swipeButton = findViewById(R.id.swipe_button_btn);
        langkahBerikutTV = findViewById(R.id.langkah_berikut_tv);

        closeIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
