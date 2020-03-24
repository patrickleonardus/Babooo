package com.bantoo.babooo.Pages.SignUpPage.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bantoo.babooo.R;

public class PhonePage extends Fragment {

    EditText phoneET;
    String phone;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_sign_up_phone,container,false);

        phoneET = rootView.findViewById(R.id.phone_sign_up_ET);

        phoneET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                phone = phoneET.getText().toString();

                if (!phone.startsWith("08")){
                    phoneET.setError("Format nomor salah");
                }
                else if (phone.length()<10){
                    phoneET.setError("Nomor handphone terlalu singkat");
                }
                else if(phone.length()>13){
                    phoneET.setError("Nomor handphone terlalu panjang");
                }
                else {
                    SharedPreferences sharedPreferences = getContext().getSharedPreferences("userPref", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("phone", phone).commit();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        return rootView;
    }

}
