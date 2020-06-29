package com.bantoo.babooo.Pages.MaidPages;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bantoo.babooo.Pages.MaidPages.MaidHomePages.MaidHomeActivity;
import com.bantoo.babooo.R;
import com.bantoo.babooo.Utilities.BaseActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormatSymbols;
import java.util.Date;

public class WithdrawSalaryConfirmationActivity extends BaseActivity {

    private static final String TAG = "WithdrawSalaryConfirm";

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
        firebaseDatabase = FirebaseDatabase.getInstance();
        maidReference = firebaseDatabase.getReference().child("ART");
        SharedPreferences sharedPreferences = getSharedPreferences("accountData", Context.MODE_PRIVATE);
        phoneNumber = sharedPreferences.getString("phoneNumber", "");
        requestUniqueKey = getIntent().getStringExtra("requestUniqueKey");
        Log.d(TAG, "initVar: request: "+requestUniqueKey);
        withdrawReference = firebaseDatabase.getReference().child("WithdrawRequest").child(requestUniqueKey);
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
                if(dataSnapshot.child("approvalCode").getValue() != null) {
                    if(dataSnapshot.child("approvalCode").getValue().toString().equals(verifCode)) {
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
                    Intent moveToHome = new Intent(WithdrawSalaryConfirmationActivity.this, MaidHomeActivity.class);
                    moveToHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(moveToHome);
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

        Date now = new Date();

        String month = new DateFormatSymbols().getMonths()[now.getMonth()-1];
        proposeDateTV.setText(""+now.getDate()+" "+month+ (now.getYear()+1900));
        proposeNumberTV.setText(requestUniqueKey);
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
