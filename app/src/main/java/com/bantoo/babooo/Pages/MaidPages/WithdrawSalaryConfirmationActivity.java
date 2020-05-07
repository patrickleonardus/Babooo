package com.bantoo.babooo.Pages.MaidPages;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
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

public class WithdrawSalaryConfirmationActivity extends AppCompatActivity {

    TextView proposeNumberTV, proposeDateTV, coinsTV, inRupiahTV;
    EditText code1ET, code2ET, code3ET, code4ET, code5ET, code6ET;
    Button withdrawBtn;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference maidReference, withdrawReference;

    int coinsWithdraw;
    String requestUniqueKey, phoneNumber, verifCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw_salary_confirmation);

        initView();
        initVar();
        showData();
        handleAction();
    }

    private void initVar() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        maidReference = firebaseDatabase.getReference().child("ART");
        SharedPreferences sharedPreferences = getSharedPreferences("accountData", Context.MODE_PRIVATE);
        phoneNumber = sharedPreferences.getString("phoneNumber", "");
        requestUniqueKey = getIntent().getStringExtra("requestUniqueKey");
        withdrawReference = firebaseDatabase.getReference().child(requestUniqueKey);
    }

    private void handleAction() {
        withdrawBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifCode = code1ET.getText().toString()+
                        code2ET.getText().toString()+
                        code3ET.getText().toString()+
                        code4ET.getText().toString()+
                        code5ET.getText().toString()+
                        code6ET.getText().toString();
                checkVerifCode();
            }
        });
    }

    private void checkVerifCode() {
        withdrawReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child("verifCode").getValue() != null) {
                    if(dataSnapshot.child("verifCode").getValue().toString().equals(verifCode)) {
                        reduceCoins();
                    } else {
                        Toast.makeText(WithdrawSalaryConfirmationActivity.this, "Kode Verifikasi salah", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(WithdrawSalaryConfirmationActivity.this, "Permintaan anda belum dikabulkan", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void reduceCoins() {
        maidReference.orderByChild("phoneNumber").equalTo(phoneNumber).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    int currentCoins = 0;
                    if(snapshot.child("coins").getValue() != null) {
                        currentCoins = Integer.parseInt(snapshot.child("coins").getValue().toString());
                    }
                    snapshot.child("coins").getRef().setValue(currentCoins - coinsWithdraw);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void showData() {
        coinsWithdraw = getIntent().getIntExtra("coinsWithdraw", 0);
        coinsTV.setText(coinsWithdraw+"");
        inRupiahTV.setText((coinsWithdraw*3000)+"");
    }

    private void initView() {
        proposeNumberTV = findViewById(R.id.nomor_pengajuan_TV);
        proposeDateTV = findViewById(R.id.tanggal_pengajuan_TV);
        coinsTV = findViewById(R.id.coins_TV);
        inRupiahTV = findViewById(R.id.rupiah_nominal_TV);
        code1ET = findViewById(R.id.code1_withdraw_ET);
        code2ET = findViewById(R.id.code2_withdraw_ET);
        code3ET = findViewById(R.id.code3_withdraw_ET);
        code4ET = findViewById(R.id.code4_withdraw_ET);
        code5ET = findViewById(R.id.code5_withdraw_ET);
        code6ET = findViewById(R.id.code6_withdraw_ET);
        withdrawBtn = findViewById(R.id.withdraw_coins_BTN);
    }
}
