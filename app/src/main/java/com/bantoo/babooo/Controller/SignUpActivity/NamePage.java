package com.bantoo.babooo.Controller.SignUpActivity;

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

public class NamePage extends Fragment {

    EditText nameET;
    String name;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_sign_up_name,container,false);

        nameET = rootView.findViewById(R.id.name_sign_up_ET);

        nameET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                name = nameET.getText().toString();

                if (name.length() < 4){
                    nameET.setError("Nama anda terlalu singkat");
                }
                else {
                    SharedPreferences sharedPreferences = getContext().getSharedPreferences("userPref", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("name", name).commit();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        return rootView;
    }
}