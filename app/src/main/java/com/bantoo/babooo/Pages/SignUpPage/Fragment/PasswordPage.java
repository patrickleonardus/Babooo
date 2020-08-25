package com.bantoo.babooo.Pages.SignUpPage.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bantoo.babooo.R;

public class PasswordPage extends Fragment {

    EditText pass1ET,pass2ET;
    private String pass1,pass2;
    public static boolean correct;

    public boolean getCorrect() {
        return correct;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_sign_up_password,container,false);

        TextView flagTV;
        pass1ET = rootView.findViewById(R.id.pass1_sign_up_ET);
        pass2ET = rootView.findViewById(R.id.pass2_sign_up_ET);
        flagTV = rootView.findViewById(R.id.flag_tv);

        pass1ET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                pass1 = pass1ET.getText().toString();
                boolean containsNumeric = false;
                boolean containsUpper = false;

                if (pass1.length() < 5){
                    correct = false;
                    pass1ET.setError("Password terlalu singkat");
                    flagTV.setText("Password: Weak");
                    flagTV.setTextColor(Color.RED);
                } else {
                    for(char c: pass1.toCharArray()) {
                        if(Character.isDigit(c)) {
                            containsNumeric = true;
                        }
                        if(Character.isUpperCase(c)) {
                            containsUpper = true;
                        }
                    }
                    correct = containsNumeric && containsUpper;
                    if(!correct) {
                        flagTV.setText("Kata Sandi: Lemah");
                        flagTV.setTextColor(Color.RED);
                        pass1ET.setError("Password terdiri dari kombinasi huruf besar dan angka");
                    } else {
                        flagTV.setText("Kata Sandi: Kuat");
                        flagTV.setTextColor(Color.GREEN);
                    }
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
                    correct = false;
                    pass1ET.setError("Harus diisi");
                    pass2ET.setError("Isi kata sandi diatas terlebih dahulu");
                } else if(pass1.length() < 5) {
                    correct = false;
                    pass2ET.setError("Password terlalu singkat");
                }
                else if (!pass2.equals(pass1)){
                    correct = false;
                    pass2ET.setError("Konfirmasi password tidak sama");
                }
                else {
                    boolean containsNumeric = false;
                    for(char c: pass1.toCharArray()) {
                        if(Character.isDigit(c)) {
                            containsNumeric = true;
                        }
                    }
                    correct = containsNumeric;
                    if(correct) {
                        SharedPreferences sharedPreferences = getContext().getSharedPreferences("accountData", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("password", pass2).commit();
                    } else {
                        pass2ET.setError("Password terdiri dari kombinasi huruf dan angka");
                    }
                }

            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        return rootView;
    }
}
