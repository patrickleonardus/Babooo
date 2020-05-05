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

public class EmailPage extends Fragment {

    EditText emailET;
    private String email;
    public static boolean correct = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_sign_up_email,container,false);

        emailET = rootView.findViewById(R.id.email_sign_up_ET);

        emailET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                email = emailET.getText().toString();

                if (email.length() < 3){
                    correct = false;
                    emailET.setError("Masukan format email yang benar");
                }
                else if (!email.contains("@")) {
                    correct = false;
                    emailET.setError("Masukan format email yang benar");
                }
                else if (!email.contains(".com") && !email.contains(".co.id")) {
                    correct = false;
                    emailET.setError("Masukan format email yang benar");
                }
                else {
                    correct = true;
                    SharedPreferences sharedPreferences = getContext().getSharedPreferences("accountData", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("email",email).commit();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        return rootView;
    }
    public boolean getCorrect() {
        return correct;
    }
}
