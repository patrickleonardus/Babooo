package com.bantoo.babooo.Pages.DailyServicePage.DetailDailyConfirmationPage;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bantoo.babooo.Pages.DailyServicePage.OrderDone.OrderDoneActivity;
import com.bantoo.babooo.Pages.HomePage.HomeActivity;
import com.bantoo.babooo.R;
import com.bantoo.babooo.Utilities.BaseActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DetailDailyConfirmationActivity extends BaseActivity {

    private static final String TAG = "DetailDailyConfirmation";
    private static final int RESULT_DONE_ACTIVITY = 1;

    TextView maidNameTV, maidRatingTV, maidStatusTV,
            orderNumberTV, dateOfWorkTV, timeOfWorkTV, estimatedFinishTimeOfWorkTV,
            orderLocationTV, serviceNameTV, serviceTypeTV, serviceCostTV, serviceFeeTV,
            serviceFeeCostTV, totalCostTV;
    ImageView maidProfilePictureIV, closeIV, messageIV, callIV;
    Button helpBantooBTN;
    private String orderUniqueKey, maidPhoneNumber;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference orderReference;

    @Override
    public void onBackPressed() {
    }

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

    private void checkOrderDone() {
        orderReference.child(orderUniqueKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String status = dataSnapshot.child("status").getValue().toString();
                if (status.equals("Pesanan Selesai")) {
                    orderReference.child(orderUniqueKey).removeEventListener(this);
                    Intent moveToOrderDone = new Intent(DetailDailyConfirmationActivity.this, OrderDoneActivity.class);
                    moveToOrderDone.putExtra("orderUniqueKey", orderUniqueKey);
                    startActivityForResult(moveToOrderDone, RESULT_DONE_ACTIVITY);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RESULT_DONE_ACTIVITY) {
            if(data.getBooleanExtra("back", false)) {
                finish();
            }
        }
    }

    private void setInformation() {
        orderReference.child(orderUniqueKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d(TAG, "setInformation: "+snapshot);
                orderNumberTV.setText(orderUniqueKey);
                maidNameTV.setText(snapshot.child("maid").getValue().toString());
                String status = snapshot.child("status").getValue().toString();
                maidStatusTV.setText(status);
                if (status.equals("Pesanan Selesai")) { //pesanan dah selesai, masuk ke history
                    maidStatusTV.setTextColor(getResources().getColor(R.color.grey));
                } else { //kalo pesanan belum selesai
                    checkOrderDone();
                }
                maidPhoneNumber = snapshot.child("maidPhoneNumber").getValue().toString();
                handleContactAction();
                String serviceCost = "0", serviceFeeCost = "0";
                try {
                    serviceCost = snapshot.child("serviceCostFee").getValue().toString();
                } catch (Exception e) {}
                try {
                    serviceFeeCost = snapshot.child("serviceCostFee").getValue().toString();
                } catch (Exception e) {}
                serviceFeeCostTV.setText(serviceFeeCost);
                serviceCostTV.setText(serviceCost);
                int totalCost = Integer.parseInt(serviceCost) + Integer.parseInt(serviceFeeCost);
                totalCostTV.setText(""+totalCost);
                dateOfWorkTV.setText(snapshot.child("orderDate").getValue()+"-"+snapshot.child("orderMonth").getValue()+"-"+snapshot.child("orderYear").getValue());
                timeOfWorkTV.setText(snapshot.child("orderTime").getValue().toString());
                int finishEstimationHours = Integer.parseInt(snapshot.child("orderTime").getValue().toString().substring(0,2)) + 2;
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
        callIV = findViewById(R.id.call_IV_detail_daily);
        messageIV = findViewById(R.id.message_IV_detail_daily);
    }

    private void handleContactAction() {
        callIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", maidPhoneNumber.replaceFirst("0", "+62"), null)));
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:"+maidPhoneNumber.replaceFirst("0", "+62")));
                startActivity(callIntent);
            }
        });
        messageIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri sms_uri = Uri.parse("smsto:" +maidPhoneNumber.replaceFirst("0", "+62"));
                Intent sms_intent = new Intent(Intent.ACTION_VIEW, sms_uri);
                sms_intent.setData(sms_uri);
                sms_intent.putExtra("sms_body", "Bantoo: ");
                startActivity(sms_intent);
            }
        });
    }

    private void handleAction(){
        closeIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(DetailDailyConfirmationActivity.this, HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        helpBantooBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //call official number
            }
        });
    }
}
