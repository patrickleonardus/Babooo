package com.bantoo.babooo.Pages.HomePage.AccountPage;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bantoo.babooo.Pages.HomePage.SettingsPage.SettingsActivity;
import com.bantoo.babooo.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class AccountFragment extends Fragment {

    private ImageButton editProfileButton, settingsButton;
    private TextView usernameTV, phoneNumberTV, addressTV, emailTV, passwordTV;
    private String uid;
    private SharedPreferences accountDataSharedPreferences;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference userReference;

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
        usernameTV = rootView.findViewById(R.id.user_name_TV_account);
        phoneNumberTV = rootView.findViewById(R.id.phoneNumber_TV_account);
        addressTV = rootView.findViewById(R.id.address_TV_account);
        emailTV = rootView.findViewById(R.id.email_TV_account);
        passwordTV = rootView.findViewById(R.id.password_TV_account);

        initData();
        buttonHandler();
        showUserInfo();

        return rootView;
    }

    private void showUserInfo() {
        userReference.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                usernameTV.setText(dataSnapshot.child("name").getValue().toString());
                phoneNumberTV.setText(dataSnapshot.child("phoneNumber").getValue().toString());
                addressTV.setText(dataSnapshot.child("address").getValue().toString());
                emailTV.setText(dataSnapshot.child("email").getValue().toString());
                String password = "";
                for(int i = 0; i < dataSnapshot.child("password").getValue().toString().length(); i++) {
                    password += "*";
                }
                passwordTV.setText(password);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void initData() {
        accountDataSharedPreferences = getActivity().getSharedPreferences("accountData", MODE_PRIVATE);
        uid = accountDataSharedPreferences.getString("uid", "");
        firebaseDatabase = FirebaseDatabase.getInstance();
        userReference = firebaseDatabase.getReference().child("Users");
    }

    private void buttonHandler() {
        editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent moveToEditPage = new Intent(getContext(), EditAccountActivity.class);
                moveToEditPage.putExtra("uid", uid);
                moveToEditPage.putExtra("username", usernameTV.getText().toString());
                moveToEditPage.putExtra("phoneNumber", phoneNumberTV.getText().toString());
                moveToEditPage.putExtra("address", addressTV.getText().toString());
                moveToEditPage.putExtra("email", emailTV.getText().toString());
                moveToEditPage.putExtra("password", passwordTV.getText().toString());
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
