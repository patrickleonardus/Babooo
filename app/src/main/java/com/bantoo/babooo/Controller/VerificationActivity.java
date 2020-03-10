package com.bantoo.babooo.Controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bantoo.babooo.R;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class VerificationActivity extends AppCompatActivity {

    private final String TAG = "VerificationActivity";
    String phoneNumber;
    String verificationCode;
    String numberWithCode;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    LinearLayout layoutCode;
    TextWatcher nextTextWatcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);

        phoneNumber = getIntent().getStringExtra("phoneNumber");
        layoutCode = findViewById(R.id.layoutCode);

        numberWithCode = "+62"+phoneNumber.substring(1);
        verificationCode = "123456";
        //sendSMS();
        configureCodeEditText();
    }

    private void configureCodeEditText() {
        nextTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                TextView textView = (TextView) getCurrentFocus();
                View next = textView.focusSearch(View.FOCUS_RIGHT);
                View prev = textView.focusSearch(View.FOCUS_LEFT);
                if (textView != null && textView.length() > 0) {
                    try {
                        next.requestFocus();
                    } catch (Exception e) {
                        //nothing
                    }
                } else if (textView != null && textView.length() == 0) {
                    try {
                        prev.requestFocus();
                    } catch (Exception e) {
                        //nothing
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        for(int i= 0; i<layoutCode.getChildCount(); i++) {
            if(layoutCode.getChildAt(i) instanceof EditText) {
                ((EditText) layoutCode.getChildAt(i)).addTextChangedListener(nextTextWatcher);
            }
        }
    }

    public void resendCode(View v) {
        sendSMS();
    }

    public void login(View v) {
        String codeEntered = "";
        for(int i = 0; i<layoutCode.getChildCount(); i++) {
            if(layoutCode.getChildAt(i) instanceof EditText) {
                codeEntered += ((EditText) layoutCode.getChildAt(i)).getText().toString();
            }
        }
        Log.d(TAG, "login: "+codeEntered);
    }

    private void sendSMS() {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(numberWithCode, 60, TimeUnit.SECONDS, this, mCallbacks);
        auth.setLanguageCode("id");
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                Log.d(TAG, "onVerificationCompleted: " + phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Log.w(TAG, "onVerificationFailed: ", e);
                if (e instanceof FirebaseAuthInvalidCredentialsException) {

                } else if (e instanceof FirebaseTooManyRequestsException) {

                }
            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                Log.d(TAG, "onCodeSent: " + s);
                verificationCode = s;
            }
        };
    }

}
