package com.bantoo.babooo.Pages.MaidPages;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bantoo.babooo.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DataDiriActivity extends AppCompatActivity {

    private static final String TAG = "DataDiri";

    FirebaseDatabase firebaseDatabase;
    DatabaseReference maidReference;
    private String phoneNumber;

    private TextView nameTV, noKTPTV, ttlTV, alamatTV, emailTV, pengalamanKerjaTV, preferensiKotaTV, gajiTV;
    private ProgressBar cuciKeringPB, setrikaPB, sapuPB, kmrmandiPB;
    private ImageView closeIV;
    SharedPreferences accountDataSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_diri);

        initView();
        handleAction();
        initFirebase();
        showData();
    }

    private void handleAction() {
        closeIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initFirebase() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        accountDataSharedPreferences = getSharedPreferences("accountData", MODE_PRIVATE);
        String artType = accountDataSharedPreferences.getString("artType", "");
        Log.d(TAG, "onCreateView: arttype: "+artType);
        if(artType.equals("daily")) {
            maidReference = firebaseDatabase.getReference().child("ART");
        } else if(artType.equals("monthly")) {
            maidReference = firebaseDatabase.getReference().child("ARTBulanan");
        }
        phoneNumber = accountDataSharedPreferences.getString("phoneNumber", "");
    }

    private void showData() {
        maidReference.orderByChild("phoneNumber").equalTo(phoneNumber).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    nameTV.setText(snapshot.child("name").getValue().toString());
                    noKTPTV.setText(snapshot.child("noKTP").getValue().toString());
                    ttlTV.setText(snapshot.child("ttl").getValue().toString());
                    alamatTV.setText(snapshot.child("address").getValue().toString());
                    emailTV.setText(snapshot.child("email").getValue().toString());
                    if(snapshot.child("experience").getValue() != null) {
                        pengalamanKerjaTV.setText(snapshot.child("experience").getValue().toString());
                    }
                    if (snapshot.child("preferensiKota").getValue() != null) {
                        preferensiKotaTV.setText(snapshot.child("preferensiKota").getValue().toString());
                    }
                    if(snapshot.child("cuciScore").getValue() != null) {
                        try {
                            cuciKeringPB.setProgress(Integer.parseInt(snapshot.child("cuciScore").getValue().toString()));
                        } catch (Exception e) {kmrmandiPB.setProgress(0);}
                    }
                    if(snapshot.child("setrikaScore").getValue() != null) {
                        try {
                            setrikaPB.setProgress(Integer.parseInt(snapshot.child("setrikaScore").getValue().toString()));
                        } catch (Exception e) {kmrmandiPB.setProgress(0);}
                    }
                    if(snapshot.child("sapuScore").getValue() != null) {
                        try {
                            sapuPB.setProgress(Integer.parseInt(snapshot.child("sapuScore").getValue().toString()));
                        } catch (Exception e) { kmrmandiPB.setProgress(0);}
                    }
                    if(snapshot.child("kmrmandiScore").getValue() != null) {
                        try {
                            kmrmandiPB.setProgress(Integer.parseInt(snapshot.child("kmrmandiScore").getValue().toString()));
                        } catch (Exception e) { kmrmandiPB.setProgress(0); }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void initView() {
        nameTV = findViewById(R.id.name_maid_TV);
        noKTPTV = findViewById(R.id.noKTP_TV);
        ttlTV = findViewById(R.id.ttl_TV);
        alamatTV = findViewById(R.id.alamatTV);
        emailTV = findViewById(R.id.email_TV);
        pengalamanKerjaTV = findViewById(R.id.pengalaman_TV);
        preferensiKotaTV = findViewById(R.id.kota_TV);
        gajiTV = findViewById(R.id.gaji_TV);
        cuciKeringPB = findViewById(R.id.progress_bar_cuci_kering);
        setrikaPB = findViewById(R.id.progress_bar_setrika);
        sapuPB = findViewById(R.id.progress_bar_sapu_maid);
        kmrmandiPB = findViewById(R.id.progress_bar_kmrmandi);
        closeIV = findViewById(R.id.close_IV);
    }
}
