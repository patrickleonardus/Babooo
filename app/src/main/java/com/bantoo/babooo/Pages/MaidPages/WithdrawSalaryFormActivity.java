package com.bantoo.babooo.Pages.MaidPages;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bantoo.babooo.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;

public class WithdrawSalaryFormActivity extends AppCompatActivity {

    TextView currentCoinsTV, rupiahNominalTV, currentDateTV;
    EditText coinsWithdrawET;
    Button withdrawSalaryBtn;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference maidRefence;
    String phoneNumber;
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
                int coinsValue = Integer.parseInt(coinsWithdrawET.getText().toString());
                rupiahNominalTV.setText("Rp "+(coinsValue*3000));
            }
        });
        withdrawSalaryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Integer.parseInt(currentCoins) < Integer.parseInt(coinsWithdrawET.getText().toString()) ) {
                    Toast.makeText(WithdrawSalaryFormActivity.this, "Not Enough Coins", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(WithdrawSalaryFormActivity.this, WithdrawSalaryConfirmationActivity.class);
                    intent.putExtra("coinsWithdraw", Integer.parseInt(coinsWithdrawET.getText().toString()));
                    startActivity(intent);
                }
            }
        });
    }

    private void showData() {
        Date now = new Date();

        maidRefence.orderByChild("phoneNumber").equalTo(phoneNumber).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    if(snapshot.child("coins").getValue() != null) {
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
        maidRefence = firebaseDatabase.getReference().child("ART");
        SharedPreferences sharedPreferences = getSharedPreferences("accountData", Context.MODE_PRIVATE);
        phoneNumber = sharedPreferences.getString("phoneNumber", "");
    }

    private void initView() {
        currentCoinsTV = findViewById(R.id.current_coins_TV);
        rupiahNominalTV = findViewById(R.id.rupiah_nominal_TV);
        currentDateTV = findViewById(R.id.current_date_TV);
        coinsWithdrawET = findViewById(R.id.coins_withdraw_ET);
        withdrawSalaryBtn = findViewById(R.id.withdraw_coins_BTN);
    }
}
