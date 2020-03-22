package com.bantoo.babooo.Controller.SignUpActivity;

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

public class LocationPage extends Fragment {

    EditText locationET;
    String location;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_sign_up_location,container,false);

        locationET = rootView.findViewById(R.id.location_sign_up_ET);

        locationET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                location = locationET.getText().toString();

                locationET.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                        if (locationET.getText().toString().isEmpty()){
                            locationET.setError("Harus diisi");
                        }
                        else {
                            SharedPreferences sharedPreferences = getContext().getSharedPreferences("userPref", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("location", location).commit();
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {}
                });
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        return rootView;
    }

}
