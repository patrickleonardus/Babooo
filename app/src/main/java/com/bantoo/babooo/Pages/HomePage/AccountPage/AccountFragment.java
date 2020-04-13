package com.bantoo.babooo.Pages.HomePage.AccountPage;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.bantoo.babooo.Pages.HomePage.SettingsPage.SettingsActivity;
import com.bantoo.babooo.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class AccountFragment extends Fragment {

    ImageButton editProfileButton, settingsButton;

    public AccountFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_home_account, container, false);
        editProfileButton = rootView.findViewById(R.id.edit_profile_button_home_account);
        settingsButton = rootView.findViewById(R.id.settings_button_home_account);
        buttonHandler();

        return rootView;
    }

    private void buttonHandler() {
        editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent moveToEditPage = new Intent(getContext(), EditAccountActivity.class);
                startActivity(moveToEditPage);
            }
        });
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent moveToSettingsPage = new Intent(getContext(), SettingsActivity.class);
                startActivity(moveToSettingsPage);
            }
        });
    }

    public void signOut() {
        //HAPUS SHAREDPREFERENCES ACCOUNT

    }

}
