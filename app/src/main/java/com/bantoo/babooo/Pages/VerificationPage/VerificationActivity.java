package com.bantoo.babooo.Pages.VerificationPage;

import androidx.annotation.NonNull;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bantoo.babooo.Model.FirebaseHelper;
import com.bantoo.babooo.Model.User;
import com.bantoo.babooo.Pages.HomePage.HomeActivity;
import com.bantoo.babooo.Pages.MaidPages.MaidHomePages.MaidHomeActivity;
import com.bantoo.babooo.R;
import com.bantoo.babooo.Utilities.BaseActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.concurrent.TimeUnit;

public class VerificationActivity extends BaseActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private FirebaseHelper firebaseHelper = new FirebaseHelper();

    TextView verificationPhoneTV, validationTV;
    EditText code1ET, code2ET, code3ET, code4ET, code5ET, code6ET;
    LinearLayout validationLayout;
    View verificationBTN;
    VerificationProgressButton progressButton;

    private String codeSent, phoneNumber, numberWithCode;
    private String apprCode, code1, code2, code3, code4, code5, code6;
    private String sender, buttonTitle;
    private String uid;
    private String role, name, email, password, address, phoneNum;

    Animation animationDown, animationUp;
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);

        mAuth = FirebaseAuth.getInstance();
        mAuth.setLanguageCode("id");

        verificationPhoneTV = findViewById(R.id.verificationTextTV);
        code1ET = findViewById(R.id.code1_login_ET);
        code2ET = findViewById(R.id.code2_login_ET);
        code3ET = findViewById(R.id.code3_login_ET);
        code4ET = findViewById(R.id.code4_login_ET);
        code5ET = findViewById(R.id.code5_login_ET);
        code6ET = findViewById(R.id.code6_login_ET);
        verificationBTN = findViewById(R.id.verificationBtn);
        validationTV = findViewById(R.id.validation_login_TV);
        validationLayout = findViewById(R.id.validation_login);
        animationUp = AnimationUtils.loadAnimation(this, R.anim.slide_up);
        animationDown = AnimationUtils.loadAnimation(this, R.anim.slide_down);
        progressButton = new VerificationProgressButton(VerificationActivity.this, verificationBTN);

        //check datang dari page mana
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("verificationPage", Context.MODE_PRIVATE);
        sender = sharedPreferences.getString("from", "N/A");

        initVar();
        handlePhoneNumber();
        sendVerificationCode();
        handleVerificationText();
        handleButton();
        displayButton();
        loadData();
    }

    public void initVar() {
        code1 = "";
        code2 = "";
        code3 = "";
        code4 = "";
        code5 = "";
        code6 = "";
    }

    private void loadData() {
        if (sender.equals("register")) {
            SharedPreferences userPref = getSharedPreferences("userPref", Context.MODE_PRIVATE);

            role = userPref.getString("role", "N/A");
            name = userPref.getString("name", "N/A");
            email = userPref.getString("email", "N/A");
            password = userPref.getString("password", "N/A");
            address = userPref.getString("location", "N/A");
            phoneNum = userPref.getString("phone", "N/A");
        }
    }

    private void createAccount() {
        mUser = mAuth.getInstance().getCurrentUser();
        if (mUser != null) {
            uid = mUser.getUid();

            FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
                @Override
                public void onSuccess(InstanceIdResult instanceIdResult) {
                    String token = "";
                    token = instanceIdResult.getToken();
                    User user = new User(role, name, email, phoneNum, password, address, token);
                    if(role.equals("pengguna")) {
                        firebaseHelper.addUser(user, uid);
                    } else if(role.equals("art")) {

                    } else if(role.equals("artBulanan")) {

                    }
                }
            });
        }
    }

    private void handleVerificationText() {
        code1ET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (code1ET.getText().toString().isEmpty()) {
                    code1ET.setError("Harus diisi");
                } else {
                    if (code1ET.getText().toString().length() == 1) {
                        code1 = code1ET.getText().toString();
                        apprCode = code1 + code2 + code3 + code4 + code5 + code6;
                        code2ET.requestFocus();
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        code2ET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (code2ET.getText().toString().isEmpty()) {
                    code2ET.setError("Harus diisi");
                } else {
                    if (code2ET.getText().toString().length() == 1) {
                        code2 = code2ET.getText().toString();
                        apprCode = code1 + code2 + code3 + code4 + code5 + code6;
                        code3ET.requestFocus();
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        code3ET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (code3ET.getText().toString().isEmpty()) {
                    code3ET.setError("Harus diisi");
                } else {
                    if (code3ET.getText().toString().length() == 1) {
                        code3 = code3ET.getText().toString();
                        apprCode = code1 + code2 + code3 + code4 + code5 + code6;
                        code4ET.requestFocus();
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        code4ET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (code4ET.getText().toString().isEmpty()) {
                    code4ET.setError("Harus diisi");
                } else {
                    if (code4ET.getText().toString().length() == 1) {
                        code4 = code4ET.getText().toString();
                        apprCode = code1 + code2 + code3 + code4 + code5 + code6;
                        code5ET.requestFocus();
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        code5ET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (code5ET.getText().toString().isEmpty()) {
                    code5ET.setError("Harus diisi");
                } else {
                    if (code5ET.getText().toString().length() == 1) {
                        code5 = code5ET.getText().toString();
                        apprCode = code1 + code2 + code3 + code4 + code5 + code6;
                        code6ET.requestFocus();
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        code6ET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (code6ET.getText().toString().isEmpty()) {
                    code6ET.setError("Harus diisi");
                } else {
                    code6 = code6ET.getText().toString();
                    apprCode = code1 + code2 + code3 + code4 + code5 + code6;
                    code6ET.clearFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void handleButton() {
        if (sender.equals("N/A")) {
            displayError("Terjadi kesalahan, silahkan reload ulang page ini");
        } else {
            verificationBTN.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    progressButton.buttonActivated();
                    verification();
                }
            });
        }
    }

    private void handlePhoneNumber() {
        if (sender.equals("login")) {
            phoneNumber = getIntent().getStringExtra("phoneNumber");
            verificationPhoneTV.setText(phoneNumber);
            numberWithCode = "+62" + phoneNumber.substring(1);
        } else if (sender.equals("register")) {
            SharedPreferences phonePref = getSharedPreferences("userPref", Context.MODE_PRIVATE);
            phoneNumber = phonePref.getString("phone", "N/A");
            verificationPhoneTV.setText(phoneNumber);
            numberWithCode = "+62" + phoneNumber.substring(1);
        }
    }

    public void displayButton() {
        if (sender.equals("login")) {
            progressButton.setTextButton("Masuk");
            buttonTitle = "Masuk";
        } else if (sender.equals("register")) {
            progressButton.setTextButton("Verifikasi");
            buttonTitle = "Verifikasi";
        }
    }

    private void resendCode(View v) {
        resendVerificationCode(mResendToken);
        Toast.makeText(getApplicationContext(), "Kode verifikasi telah dikirim", Toast.LENGTH_SHORT).show();
    }

    private void verification() {
        if (code1ET.getText().toString().isEmpty() ||
                code2ET.getText().toString().isEmpty() ||
                code3ET.getText().toString().isEmpty() ||
                code4ET.getText().toString().isEmpty() ||
                code5ET.getText().toString().isEmpty() ||
                code6ET.getText().toString().isEmpty()) {

            displayError("Periksa kembali kode verifikasi anda");
            progressButton.buttonFinished(buttonTitle);
        } else {
            verifySignInCode();
        }
    }

    public void displayError(String message) {
        validationLayout.setVisibility(View.VISIBLE);
        validationLayout.startAnimation(animationDown);
        validationTV.setText(message);
        handler.postDelayed(removeError, 3500);

    }

    public Runnable removeError = new Runnable() {
        @Override
        public void run() {
            if (validationLayout.getVisibility() == View.VISIBLE) {
                validationLayout.startAnimation(animationUp);
                validationLayout.setVisibility(View.GONE);
            }
        }
    };

    private void moveToHome() {
        Intent intent = new Intent(VerificationActivity.this, HomeActivity.class);
        SharedPreferences accountData = getApplicationContext().getSharedPreferences("accountData", MODE_PRIVATE);
        SharedPreferences.Editor editor = accountData.edit();
        editor.putString("phoneNumber", phoneNumber);
        editor.apply();
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void moveToARTBulanan() {
        Intent intent = new Intent(VerificationActivity.this, MaidHomeActivity.class);
        SharedPreferences sharedPreferences = getSharedPreferences("accountData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("phoneNumber", phoneNumber);
        editor.putString("artType", "monthly");
        editor.apply();
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void moveToART() {
        Intent intent = new Intent(VerificationActivity.this, MaidHomeActivity.class);
        SharedPreferences sharedPreferences = getSharedPreferences("accountData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("phoneNumber", phoneNumber).commit();
        editor.putString("artType", "daily").commit();
        editor.apply();
        startActivity(intent);
    }

    //FIREBASE AUTH
    //BELOW CODE HANDLE AUTHENTICATION METHOD

    private void sendVerificationCode() {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(numberWithCode, 60, TimeUnit.SECONDS, this, mCallbacks);
    }

    private void resendVerificationCode(PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(numberWithCode, 60, TimeUnit.SECONDS, this, mCallbacks, token);
    }

    private void verifySignInCode() {
        if (codeSent == null) {
            displayError("Mohon tunggu sampai kode SMS masuk dan coba lagi");
            progressButton.buttonFinished(buttonTitle);
        } else {
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codeSent, apprCode);
            signInWithPhoneAuthCredential(credential);
        }
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            if (sender.equals("register")) {
                                createAccount();
                            }
                            progressButton.buttonFinished(buttonTitle);
                            if(getIntent().getStringExtra("role").equals("art")) {
                                Toast.makeText(VerificationActivity.this, "Ini ART", Toast.LENGTH_SHORT).show();
                                moveToART();
                            } else if(getIntent().getStringExtra("role").equals("artBulanan")) {
                                Toast.makeText(VerificationActivity.this, "ini ART Bulanan", Toast.LENGTH_SHORT).show();
                                moveToARTBulanan();
                            } else {
                                moveToHome();
                            }
                        } else {
                            displayError("Kode verifikasi salah, mohon periksa kembali");
                            progressButton.buttonFinished(buttonTitle);
                        }
                    }
                });
    }

    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            Toast.makeText(getApplicationContext(), "Sukses kirim kode verifikasi", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Log.d("TAG", "onVerificationFailed: "+e);
            displayError("Gagal mengirimkan kode verifikasi, periksa nomor telepon dan koneksi jaringan anda");
        }

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            codeSent = s;
            mResendToken = forceResendingToken;
        }
    };

    //ABOVE CODE HANDLE AUTHENTICATION METHOD
    //FINISH AUTH METHOD

}
