package com.bantoo.babooo.Pages.MaidPages;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bantoo.babooo.R;
import com.bantoo.babooo.Utilities.BaseActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class IncomeTargetActivity extends BaseActivity {

    private EditText rupiahTargetET;
    private TextView coinsTV;
    private SeekBar targetSB;
    private Button saveTargetBtn;
    private ImageView closeIV;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference maidReference;
    private String phoneNumber;

    private int coins = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income_target);

        initView();
        handleAction();
    }

    private void handleAction() {
        rupiahTargetET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                coins = Integer.parseInt(rupiahTargetET.getText().toString()) / 3000;
                coinsTV.setText(""+coins);
                targetSB.setProgress(coins);
            }
        });
        targetSB.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                coinsTV.setText(""+progress);
                rupiahTargetET.setText(""+progress*3000);
                /*rupiahTargetET.setText(""+progress);
                coins = Integer.parseInt(rupiahTargetET.getText().toString()) / 3000;
                coinsTV.setText(""+coins);*/
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        saveTargetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveTargetToFirebase();
                finish();
            }
        });
        closeIV.setOnClickListener(v -> {
            finish();
        });
    }

    private void saveTargetToFirebase() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        maidReference = firebaseDatabase.getReference().child("ART");

        SharedPreferences sharedPreferences = getSharedPreferences("accountData", Context.MODE_PRIVATE);
        phoneNumber = sharedPreferences.getString("phoneNumber", "");
        maidReference.orderByChild("phoneNumber").equalTo(phoneNumber).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    snapshot.getRef().child("target").setValue(coins);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void initView() {
        rupiahTargetET = findViewById(R.id.rupiah_target_et);
        coinsTV = findViewById(R.id.coins_TV);
        targetSB = findViewById(R.id.targetSB);
        saveTargetBtn = findViewById(R.id.save_target_btn);
        closeIV = findViewById(R.id.close_IV);
    }
}
