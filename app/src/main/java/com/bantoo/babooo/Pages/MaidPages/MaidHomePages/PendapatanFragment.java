package com.bantoo.babooo.Pages.MaidPages.MaidHomePages;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.bantoo.babooo.Pages.MaidPages.WithdrawSalaryFormActivity;
import com.bantoo.babooo.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;

public class PendapatanFragment extends Fragment {

    TextView statusTV, totalKoinTV, setaraRupiahTV, koinHarianTV, percentageHarianTV, ratingMaidTV, targetKoinHarianTV;
    Switch activeSwitch;
    ProgressBar pendapatanPB;
    RelativeLayout penarikanGajiRL;

    String phoneNumber;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference maidReference, orderReference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_pendapatan, container, false);

        statusTV = rootView.findViewById(R.id.status_TV);
        totalKoinTV = rootView.findViewById(R.id.total_koin_TV);
        setaraRupiahTV = rootView.findViewById(R.id.setara_rupiah_koin_TV);
        koinHarianTV = rootView.findViewById(R.id.koin_harian_TV);
        percentageHarianTV = rootView.findViewById(R.id.percentageTarget_TV);
        ratingMaidTV = rootView.findViewById(R.id.rating_maid_TV);
        targetKoinHarianTV = rootView.findViewById(R.id.target_koin_harianTV);
        activeSwitch = rootView.findViewById(R.id.active_switch);
        pendapatanPB = rootView.findViewById(R.id.pendapatan_PB);
        penarikanGajiRL = rootView.findViewById(R.id.penarikan_gaji_RL);
        penarikanGajiRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent withdrawIntent = new Intent(getContext(), WithdrawSalaryFormActivity.class);
                withdrawIntent.putExtra("phoneNumber", phoneNumber);
                startActivity(withdrawIntent);
            }
        });
        initFirebase();
        showData();

        return rootView;
    }

    private void initFirebase() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        maidReference = firebaseDatabase.getReference().child("ART");
        orderReference = firebaseDatabase.getReference().child("Order");
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("accountData", Context.MODE_PRIVATE);
        phoneNumber = sharedPreferences.getString("phoneNumber", "");
    }

    private void showData() {
        maidReference.orderByChild("phoneNumber").equalTo(phoneNumber).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    statusTV.setText(snapshot.child("activate").getValue().toString());
                    String totalKoin = snapshot.child("coins").getValue().toString();
                    if(snapshot.child("coins") != null) {
                        totalKoinTV.setText(totalKoin+" koin");
                    } else { totalKoinTV.setText("0 koin"); }
                    setaraRupiahTV.setText("Setara dengan Rp. "+(Integer.parseInt(totalKoin)*3000));
                    showDailyData();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void showDailyData() {
        orderReference.orderByChild("maidPhoneNumber").equalTo(phoneNumber).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int counter = 0;
                int totalRating = 0;
                int totalFee = 0;
                String currentDate = ""+new Date().getDate();
                String currentMonth = ""+new Date().getMonth();
                String currentYear = ""+new Date().getYear() + 1900;
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    String orderDate = snapshot.child("orderDate").getValue().toString();
                    String orderMonth = snapshot.child("orderMonth").getValue().toString();
                    String orderYear = snapshot.child("orderYear").getValue().toString();
                    totalRating += Integer.parseInt(snapshot.child("rating").getValue().toString());
                    if(currentDate == orderDate && currentMonth == orderMonth && currentYear == orderYear) {
                        totalFee += Integer.parseInt(snapshot.child("serviceCost").getValue().toString());
                        counter++;
                    }
                }
                float averageRating = totalRating / counter;
                koinHarianTV.setText(totalFee+" koin");
                ratingMaidTV.setText(""+averageRating);
                targetKoinHarianTV.setText("target: 300 koin");
                pendapatanPB.setProgress(totalFee);
                pendapatanPB.setMax(300);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
