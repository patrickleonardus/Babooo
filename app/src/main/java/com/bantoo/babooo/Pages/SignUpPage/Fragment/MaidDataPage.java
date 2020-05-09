package com.bantoo.babooo.Pages.SignUpPage.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bantoo.babooo.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MaidDataPage extends Fragment {

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference maidReference;
    private String apprCode;

    TextView nameTV, ttlTV, noKTPTV, addressTV, noHPTV, experienceTV, salaryTV, cityPreferenceTV;
    LinearLayout experienceLayout, incomeLayout, workPreferenceLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_sign_up_maid_data, container, false);
        nameTV = rootView.findViewById(R.id.name_TV);
        ttlTV = rootView.findViewById(R.id.ttl_TV);
        noKTPTV = rootView.findViewById(R.id.noKTP_TV);
        addressTV = rootView.findViewById(R.id.address_TV);
        noHPTV = rootView.findViewById(R.id.noHP_TV);
        experienceTV = rootView.findViewById(R.id.experience_TV);
        salaryTV = rootView.findViewById(R.id.gaji_bulanan_TV);
        cityPreferenceTV = rootView.findViewById(R.id.preferensi_kota_kerja_TV);
        experienceLayout = rootView.findViewById(R.id.experienceLayout);
        incomeLayout = rootView.findViewById(R.id.incomeLayout);
        workPreferenceLayout = rootView.findViewById(R.id.workPreferenceLayout);

        firebaseDatabase = FirebaseDatabase.getInstance();

        SharedPreferences userSharePref = getContext().getSharedPreferences("accountData", Context.MODE_PRIVATE);
        apprCode = userSharePref.getString("apprCode", "");
        String artType = userSharePref.getString("artType", "N/A");
        if (artType.equals("daily")) {
            maidReference = firebaseDatabase.getReference().child("ART");
            experienceLayout.setVisibility(View.GONE);
            incomeLayout.setVisibility(View.GONE);
            workPreferenceLayout.setVisibility(View.GONE);
        } else if (artType.equals("monthly")) {
            maidReference = firebaseDatabase.getReference().child("ARTBulanan");
            experienceLayout.setVisibility(View.VISIBLE);
            incomeLayout.setVisibility(View.VISIBLE);
            workPreferenceLayout.setVisibility(View.VISIBLE);
        }
        checkArt();

        return rootView;
    }

    private void checkArt() {

        if (!apprCode.isEmpty()) {
            if(!apprCode.equals("N/A")){
                maidReference.orderByChild("approvalCode").equalTo(apprCode).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            nameTV.setText(snapshot.child("name").getValue().toString());
                            ttlTV.setText(snapshot.child("ttl").getValue().toString());
                            addressTV.setText(snapshot.child("address").getValue().toString());
                            noKTPTV.setText(snapshot.child("noKTP").getValue().toString());
                            noHPTV.setText(snapshot.child("phoneNumber").getValue().toString());
                            if (snapshot.child("experience").getValue() != null) {
                                experienceTV.setText(dataSnapshot.child("phoneNumber").getValue().toString());
                            }
                            if (snapshot.child("cityPreference").getValue() != null) {
                                cityPreferenceTV.setText(dataSnapshot.child("cityPreference").getValue().toString());
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        }
    }
}
