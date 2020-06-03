package com.bantoo.babooo.Pages.MaidPages;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bantoo.babooo.Model.FirebaseHelper;
import com.bantoo.babooo.Model.SalaryRequest;
import com.bantoo.babooo.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormatSymbols;
import java.util.Date;

public class WithdrawSalaryFormActivity extends AppCompatActivity {

    TextView currentCoinsTV, inRupiahTV, currentDateTV;
    EditText coinsWithdrawET;
    Button withdrawSalaryBtn;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference maidReference, withdrawReference;
    String phoneNumber, maidName, requestUniqueKey;
    String currentCoins = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw_salary_form);

        initView();
        initFirebase();
        showData();
        handleAction();
    }

    private void handleAction() {
        coinsWithdrawET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int coinsValue = 0;
                if(!coinsWithdrawET.getText().toString().isEmpty()) {
                    coinsValue = Integer.parseInt(coinsWithdrawET.getText().toString());
                }

                inRupiahTV.setText("Rp "+(coinsValue*3000));
            }
        });
        withdrawSalaryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Integer.parseInt(currentCoins) < Integer.parseInt(coinsWithdrawET.getText().toString()) ) {
                    Toast.makeText(WithdrawSalaryFormActivity.this, "Not Enough Coins", Toast.LENGTH_SHORT).show();
                } else {
                    addRequestToFirebase();
                }
            }
        });
    }

    private void addRequestToFirebase() {
        withdrawReference.orderByChild("phoneNumber").equalTo(phoneNumber).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for(DataSnapshot snapshot: dataSnapshot.getChildren()) {
                        snapshot.child("coinsRequest").getRef().setValue(coinsWithdrawET.getText().toString());
                        requestUniqueKey = snapshot.getKey();
                        if(snapshot.child("status").getValue() != null) {
                            snapshot.child("status").getRef().removeValue();
                        }
                        if(snapshot.child("approvalCode").getValue() != null) {
                            snapshot.child("approvalCode").getRef().removeValue();
                        }
                    }
                } else {
                    SalaryRequest salaryRequest = new SalaryRequest(maidName, phoneNumber, coinsWithdrawET.getText().toString());
                    FirebaseHelper firebaseHelper = new FirebaseHelper();
                    requestUniqueKey = firebaseHelper.addSalaryRequest(salaryRequest);
                    Log.d("WithdrawSalaryForm", "onDataChange: requestUniqueKey: "+requestUniqueKey);
                }
                Intent intent = new Intent(WithdrawSalaryFormActivity.this, WithdrawSalaryConfirmationActivity.class);
                intent.putExtra("coinsWithdraw", Integer.parseInt(coinsWithdrawET.getText().toString()));
                intent.putExtra("requestUniqueKey", requestUniqueKey);
                startActivity(intent);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void showData() {
        Date now = new Date();

        String month = new DateFormatSymbols().getMonths()[now.getMonth()-1];
        currentDateTV.setText(""+now.getDate()+" "+month+ (now.getYear()+1900));
        maidReference.orderByChild("phoneNumber").equalTo(phoneNumber).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    if(snapshot.child("coins").getValue() != null) {
                        maidName = snapshot.child("name").getValue().toString();
                        currentCoins = snapshot.child("coins").getValue().toString();
                        currentCoinsTV.setText(currentCoins);
                    } else {
                        currentCoinsTV.setText("0");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void initFirebase() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        maidReference = firebaseDatabase.getReference().child("ART");
        withdrawReference = firebaseDatabase.getReference().child("WithdrawRequest");
        SharedPreferences sharedPreferences = getSharedPreferences("accountData", Context.MODE_PRIVATE);
        phoneNumber = sharedPreferences.getString("phoneNumber", "");
    }

    private void initView() {
        currentCoinsTV = findViewById(R.id.current_coins_TV);
        inRupiahTV = findViewById(R.id.rupiah_nominal_TV);
        currentDateTV = findViewById(R.id.current_date_TV);
        coinsWithdrawET = findViewById(R.id.coins_withdraw_ET);
        withdrawSalaryBtn = findViewById(R.id.withdraw_coins_BTN);
    }
}
