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

public class PasswordPage extends Fragment {

    EditText pass1ET,pass2ET;
    private String pass1,pass2;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_sign_up_password,container,false);

        pass1ET = rootView.findViewById(R.id.pass1_sign_up_ET);
        pass2ET = rootView.findViewById(R.id.pass2_sign_up_ET);

        pass1ET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                pass1 = pass1ET.getText().toString();

                if (pass1.length() < 5){
                    pass1ET.setError("Password terlalu singkat");
                }

            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        pass2ET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                pass2 = pass2ET.getText().toString();

                if (pass1.isEmpty()){
                    pass1ET.setError("Harus diisi");
                    pass2ET.setError("Isi kata sandi diatas terlebih dahulu");
                }
                else if (!pass2.equals(pass1)){
                    pass2ET.setError("Konfirmasi password tidak sama");
                }
                else {
                    SharedPreferences sharedPreferences = getContext().getSharedPreferences("userPref", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("password", pass2).commit();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        return rootView;
    }
}
