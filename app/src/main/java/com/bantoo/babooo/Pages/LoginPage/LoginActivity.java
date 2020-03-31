package com.bantoo.babooo.Pages.LoginPage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bantoo.babooo.Pages.HomePage.HomeActivity;
import com.bantoo.babooo.Pages.MonthlyServicePage.FilterPage.CityRegionPage.CityRegionActivity;
import com.bantoo.babooo.Pages.MonthlyServicePage.FilterPage.FilterActivity;
import com.bantoo.babooo.Pages.SignUpPage.SignUpRoleActivity;
import com.bantoo.babooo.Pages.VerificationPage.VerificationActivity;
import com.bantoo.babooo.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference reference = database.getReference();

    EditText phoneNumberET;
    TextView signUpBtn;
    View loginBtn;
    LoginProgressButton progressButton;

    private boolean restrictLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        phoneNumberET = findViewById(R.id.phoneNumber_login_ET);
        loginBtn = findViewById(R.id.login_btn);
        signUpBtn = findViewById(R.id.signUp_btn);
        progressButton = new LoginProgressButton(LoginActivity.this,loginBtn);
        progressButton.setTextButton("Masuk");

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        Intent intent = new Intent(this, FilterActivity.class);
        startActivity(intent);

        //Check state, klo dah isi uid langsung ke home
        if (mUser != null){
//            moveToHome();
        }
        else if (mUser == null){
            generalStyling();
            handleLogin();
            phoneChecker();
            signUpAction();
        }
        resetSharedPref();
    }

    public void generalStyling(){
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(getApplicationContext(),R.color.orangePrimary));
        window.setNavigationBarColor(ContextCompat.getColor(getApplicationContext(),R.color.greenPrimary));
    }

    private void moveToHome(){
        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
        startActivity(intent);
        LoginActivity.this.finish();
    }

    private void resetSharedPref(){
        //clear semua sharedPreferences
        SharedPreferences userPref = getSharedPreferences("userPref", Context.MODE_PRIVATE);
        SharedPreferences verifPref = getSharedPreferences("verificationPage", Context.MODE_PRIVATE);
        userPref.edit().clear().commit();
        verifPref.edit().clear().commit();
    }

    private void handleLogin(){
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (phoneNumberET.getText().toString().isEmpty()){
                    phoneNumberET.setError("Nomor telepon harus diisi");
                }
                else if (!restrictLogin){
                    progressButton.buttonActivated();

                    SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("verificationPage", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("from", "login").commit();

                    loginAction();
                }
            }
        });
    }

    private void phoneChecker(){
        phoneNumberET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().startsWith("08")){
                    phoneNumberET.setError("Format nomor handphone salah");
                    restrictLogin = true;
                }
                else if (s.length() < 10){
                    phoneNumberET.setError("Nomor handphone terlalu pendek");
                    restrictLogin = true;
                }
                else if (s.length() > 13){
                    phoneNumberET.setError("Nomor handphone terlalu panjang");
                    restrictLogin = true;
                }
                else {
                    restrictLogin = false;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }



    private void signUpAction() {
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent moveToSignUp = new Intent(LoginActivity.this, SignUpRoleActivity.class);
                startActivity(moveToSignUp);
            }
        });
    }

    private void loginAction(){
        DatabaseReference userRef = reference.child("Users");
        Query queryRef = userRef.orderByChild("phoneNumber").equalTo(phoneNumberET.getText().toString());
        queryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    progressButton.buttonFinished();
                    Intent moveToVerification = new Intent(LoginActivity.this, VerificationActivity.class);
                    moveToVerification.putExtra("phoneNumber", phoneNumberET.getText().toString());
                    startActivity(moveToVerification);
                }
                else if (!dataSnapshot.exists()){
                    progressButton.buttonFinished();
                    unregisteredPhoneNumber();
                }
                else {
                    progressButton.buttonFinished();
                    Toast.makeText(getApplicationContext(),"Terjadi kesalahan, periksa koneksi jaringan anda",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void unregisteredPhoneNumber(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);
        alertDialogBuilder.setTitle("Nomor anda belum terdaftar");
        alertDialogBuilder
                .setMessage("Silahkan lakukan registrasi akun terlebih dahulu sebelum melakukan login")
                .setCancelable(false)
                .setPositiveButton("Registrasi Sekarang",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                       Intent intent = new Intent(LoginActivity.this,SignUpRoleActivity.class);
                       startActivity(intent);
                    }
                })
                .setNegativeButton("Nanti Dulu",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

}