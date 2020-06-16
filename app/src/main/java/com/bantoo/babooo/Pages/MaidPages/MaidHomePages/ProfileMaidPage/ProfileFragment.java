package com.bantoo.babooo.Pages.MaidPages.MaidHomePages.ProfileMaidPage;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bantoo.babooo.Pages.HomePage.SettingsPage.SettingsActivity;
import com.bantoo.babooo.Pages.MaidPages.IncomeTargetActivity;
import com.bantoo.babooo.Pages.MaidPages.MaidBiodataActivity;
import com.bantoo.babooo.Pages.MaidPages.MaidContractActivity;
import com.bantoo.babooo.Pages.MaidPages.MaidHelpPages.MaidHelpActivity;
import com.bantoo.babooo.Pages.MaidPages.OrderHistoryPage.OrderHistoryActivity;
import com.bantoo.babooo.R;


public class ProfileFragment extends Fragment {

    ImageView settingsIV;
    RelativeLayout biodataRL, orderHistoryRL, bantuanRL, perjanjianRL, targetIncomeRL;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        settingsIV = view.findViewById(R.id.settings_Btn);
        biodataRL = view.findViewById(R.id.datadiriRL);
        orderHistoryRL = view.findViewById(R.id.riwayat_pesanan_RL);
        bantuanRL = view.findViewById(R.id.bantuanRL);
        perjanjianRL = view.findViewById(R.id.perjanjianRL);
        targetIncomeRL = view.findViewById(R.id.targetIncomeRL);
        handleAction();

        return view;
    }

    private void handleAction() {
        settingsIV.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), SettingsActivity.class);
            startActivity(intent);
        });
        biodataRL.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), MaidBiodataActivity.class);
            startActivity(intent);
        });
        orderHistoryRL.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), OrderHistoryActivity.class);
            startActivity(intent);
        });

        bantuanRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MaidHelpActivity.class);
                startActivity(intent);
            }
        });

        targetIncomeRL.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), IncomeTargetActivity.class);
            startActivity(intent);
        });

        perjanjianRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (getContext(), MaidContractActivity.class);
                startActivity(intent);
            }
        });
    }
}
