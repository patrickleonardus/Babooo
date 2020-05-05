package com.bantoo.babooo.Pages.SignUpPage.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bantoo.babooo.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PhonePage extends Fragment {

    private static final String TAG = "PhonePage";

    EditText phoneET;
    private String phone;
    public static boolean correct = false;
    private boolean userExist, maidExist;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference userReference, maidReference, monthlyMaidReference;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_sign_up_phone,container,false);

        firebaseDatabase = FirebaseDatabase.getInstance();
        userReference = firebaseDatabase.getReference().child("Users");
        maidReference = firebaseDatabase.getReference().child("ART");
        monthlyMaidReference = firebaseDatabase.getReference().child("ARTBulanan");
        phoneET = rootView.findViewById(R.id.phone_sign_up_ET);

        phoneET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                phone = phoneET.getText().toString();

                if (!phone.startsWith("08")){
                    correct = false;
                    phoneET.setError("Format nomor salah");
                }
                else if (phone.length()<10){
                    correct = false;
                    phoneET.setError("Nomor handphone terlalu singkat");
                }
                else if(phone.length()>13){
                    correct = false;
                    phoneET.setError("Nomor handphone terlalu panjang");
                }
                else {
                    userReference.orderByChild("phoneNumber").equalTo(phone).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()) {
                                correct = false;
                                userExist = true;
                            } else {
                                userExist = false;
                                checkMaidPhoneNumber();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }

            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        return rootView;
    }

    private void checkMaidPhoneNumber() {
        maidReference.orderByChild("phoneNumber").equalTo(phoneET.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    correct = false;
                    maidExist = true;
                } else {
                    maidExist = false;
                    checkMonthlyMaidPhoneNumber();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void checkMonthlyMaidPhoneNumber() {
        monthlyMaidReference.orderByChild("phoneNumber").equalTo(phoneET.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    correct = false;
                    Toast.makeText(getContext(), "Phone number registered", Toast.LENGTH_SHORT).show();
                } else {
                    if(maidExist && userExist) {
                        correct = false;
                        Log.d(TAG, "onDataChange: data phone number exist");
                    } else {
                        Log.d(TAG, "onTextChanged: correct: " + correct);
                        userExist = false;
                        correct = true;
                        Log.d(TAG, "onDataChange: updating sharedpref phone");
                        SharedPreferences sharedPreferences = getContext().getSharedPreferences("accountData", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("phone", phone);
                        editor.apply();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public boolean getCorrect() {
        return correct;
    }

    public boolean getUserExist() {
        if(userExist || maidExist) {
            return true;
        }
        return false;
    }

}
