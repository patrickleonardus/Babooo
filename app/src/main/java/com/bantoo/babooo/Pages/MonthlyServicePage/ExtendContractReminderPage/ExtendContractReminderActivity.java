package com.bantoo.babooo.Pages.MonthlyServicePage.ExtendContractReminderPage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bantoo.babooo.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ExtendContractReminderActivity extends AppCompatActivity {

    private static final String TAG = "ExtendContract";

    private TextView coinsFeeTV, endWorkingTV, maidNameTV, orderNumberTV, maidRatingTV, maidSalaryTV, maidStatusTV, serviceNameTV, startWorkingTV;
    private Button renewContractBtn;
    private Spinner durationSpinner;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference rentReference, monthlyMaidReference, userReference;
    private String rentUniqueKey, userPhoneNumber;

    private Date renewStartDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extend_contract_reminder);

        initView();
        loadDatabase();
    }

    private void loadDatabase() {
        rentUniqueKey = getIntent().getStringExtra("rentUniqueKey");
        rentUniqueKey = "-M5Q-7LepvyPbMsoPBCu";
        firebaseDatabase = FirebaseDatabase.getInstance();
        rentReference = firebaseDatabase.getReference().child("Rent").child(rentUniqueKey);
        monthlyMaidReference = firebaseDatabase.getReference().child("ARTBulanan");
        userReference = firebaseDatabase.getReference().child("Users");
        loadOrderData();
    }

    private void loadOrderData() {
        rentReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userPhoneNumber = dataSnapshot.child("phoneNumber").getValue().toString();
                coinsFeeTV.setText(dataSnapshot.child("serviceCost").getValue().toString());
                maidNameTV.setText(dataSnapshot.child("maid").getValue().toString());
                orderNumberTV.setText(dataSnapshot.getKey());
                maidStatusTV.setText(dataSnapshot.child("status").getValue().toString());
                serviceNameTV.setText(dataSnapshot.child("serviceType").getValue().toString());
                String startDate = dataSnapshot.child("orderDate").getValue()+"-"+dataSnapshot.child("orderMonth").getValue()+"-"+dataSnapshot.child("orderYear").getValue();
                DateFormat textFormat = new SimpleDateFormat("dd MMMM yyyy");
                DateFormat format = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
                Date startWorkingDate = new Date(), endWorkingDate = new Date();
                try {
                    startWorkingDate = format.parse(startDate);
                    Calendar c = Calendar.getInstance();
                    c.setTime(startWorkingDate);
                    String duration = dataSnapshot.child("duration").getValue().toString();
                    //updateSpinnerData(duration);
                    c.add(Calendar.MONTH, Integer.parseInt(duration));
                    endWorkingDate = c.getTime();
                    c.add(Calendar.DATE, 1);
                    renewStartDate = c.getTime();
                } catch(Exception e){}
                startWorkingTV.setText(textFormat.format(startWorkingDate));
                endWorkingTV.setText(textFormat.format(endWorkingDate));
                loadMaidData(dataSnapshot.child("maidPhoneNumber").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void loadMaidData(String maidPhoneNumber) {
        monthlyMaidReference.orderByChild("phoneNumber").equalTo(maidPhoneNumber).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    maidRatingTV.setText(snapshot.child("rating").getValue().toString());
                    maidSalaryTV.setText(snapshot.child("salary").getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void updateSpinnerData(String duration) {
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, android.R.id.text1);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        durationSpinner.setAdapter(spinnerAdapter);
        for(int i=1; i<=Integer.parseInt(duration); i++) {
            spinnerAdapter.add("Bulan ke "+i);
        }
        Log.d(TAG, "updateSpinnerData: ");
        spinnerAdapter.notifyDataSetChanged();
    }

    private void initView() {
        coinsFeeTV = findViewById(R.id.coins_month_extend_contract);
        endWorkingTV = findViewById(R.id.endWorking_extend_reminder);
        maidNameTV = findViewById(R.id.maid_name_extend_reminder);
        orderNumberTV = findViewById(R.id.maid_order_number_extend_reminder);
        maidRatingTV = findViewById(R.id.maid_rating_extend_reminder);
        maidSalaryTV = findViewById(R.id.maid_salary_extend_reminder);
        maidStatusTV = findViewById(R.id.maid_status_extend_reminder);
        renewContractBtn = findViewById(R.id.renew_contract_extend_reminder);
        serviceNameTV = findViewById(R.id.serice_name_extend_reminder);
        durationSpinner = findViewById(R.id.spinner_month_extend_reminder);
        startWorkingTV =findViewById(R.id.startWorking_extend_reminder);

        renewContractBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateCoins();
            }
        });
    }

    private void updateCoins() {
        userReference.orderByChild("phoneNumber").equalTo(userPhoneNumber).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    int currentCoins = Integer.parseInt(snapshot.child("coins").getValue().toString());
                    if(currentCoins < Integer.parseInt(coinsFeeTV.getText().toString())) {
                        Toast.makeText(ExtendContractReminderActivity.this, "Not enough Coins", Toast.LENGTH_SHORT).show();
                    } else {
                        snapshot.child("coins").getRef().setValue(currentCoins - Integer.parseInt(coinsFeeTV.getText().toString()));
                        Map<String, Object> salaryConfirmMap = new HashMap<String, Object>();
                        salaryConfirmMap.put("orderDate", "" + renewStartDate.getDate());
                        salaryConfirmMap.put("orderMonth", "" + (1 + renewStartDate.getMonth()));
                        salaryConfirmMap.put("orderYear", "" + (1900 + renewStartDate.getYear()));
                        rentReference.updateChildren(salaryConfirmMap);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}