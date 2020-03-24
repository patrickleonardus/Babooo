package com.bantoo.babooo.Pages.LoginPage;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Activity;
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
import android.widget.Toast;

import com.bantoo.babooo.Pages.HomePage.HomeActivity;
import com.bantoo.babooo.Pages.SignUpPage.SignUpRoleActivity;
import com.bantoo.babooo.Pages.VerificationPage.VerificationActivity;
import com.bantoo.babooo.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference reference = database.getReference();

    EditText phoneNumberET;
    View loginBtn;
    LoginProgressButton progressButton;

    private boolean restrictLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        phoneNumberET = findViewById(R.id.phoneNumber_login_ET);
        loginBtn = findViewById(R.id.login_btn);
        progressButton = new LoginProgressButton(LoginActivity.this,loginBtn);
        progressButton.setTextButton("Masuk");

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        //Check state, klo dah isi uid langsung ke home
        if (mUser != null){
            moveToHome();
        }
        else if (mUser == null){

            generalStyling();
            handleLogin();
            phoneChecker();
        }
        resetSharedPref();


//        setupNotification();

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

    public void resetSharedPref(){
        //clear semua sharedPreferences
        SharedPreferences userPref = getSharedPreferences("userPref", Context.MODE_PRIVATE);
        SharedPreferences verifPref = getSharedPreferences("verificationPage", Context.MODE_PRIVATE);
        userPref.edit().clear().commit();
        verifPref.edit().clear().commit();
    }

    /*
    private void setupGoogleSignIn() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        final GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        if (GoogleSignIn.getLastSignedInAccount(this) != null) {
            //logged in
            Log.d(TAG, "setupGoogleSignIn: "+GoogleSignIn.getLastSignedInAccount(this));
        } else {
            signInButton.setSize(SignInButton.SIZE_STANDARD);
            signInButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                    startActivityForResult(signInIntent, 101);
                }
            });
        }
    }*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case 101:
                    try{
                        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                        GoogleSignInAccount account = task.getResult(ApiException.class);
                        onLoggedIn(account);

                    } catch (ApiException e) {

                    }
                    break;
            }
        }
    }

    private void onLoggedIn(GoogleSignInAccount googleSignInAccount) {
        Toast.makeText(this, googleSignInAccount.getDisplayName(), Toast.LENGTH_SHORT).show();
        //move to another activity
        /*
        * Intent intent = new Intent(this, ProfileActivity.class);
          intent.putExtra(ProfileActivity.GOOGLE_ACCOUNT, googleSignInAccount);

          startActivity(intent);
          finish();
        * */
    }

    private void setupNotification() {
        FirebaseAuth auth = FirebaseAuth.getInstance();

        auth.signInAnonymously().addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    Toast.makeText(LoginActivity.this, "notif success", Toast.LENGTH_LONG).show();
                    final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Notification");
                    FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
                        @Override
                        public void onSuccess(InstanceIdResult instanceIdResult) {
                            reference.child("token").setValue(instanceIdResult.getToken());
                        }
                    });
                } else {
                    Toast.makeText(LoginActivity.this, "notif failed", Toast.LENGTH_LONG).show();
                }
            }
        });
/*
        auth.signInWithEmailAndPassword("tommyryantotomtom@gmail.com", "").addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "notif success", Toast.LENGTH_LONG).show();
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Notification");
                    reference.child("token").setValue(FirebaseInstanceId.getInstance().getToken());
                } else {
                    Toast.makeText(MainActivity.this, "notif failed", Toast.LENGTH_LONG).show();
                    Log.e(TAG, "onComplete: ", task.getException() );
                }
            }
        });*/
    }

    public void handleLogin(){
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

    public void phoneChecker(){
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



    public void signUpButton(View v) {
        Intent moveToSignUp = new Intent(this, SignUpRoleActivity.class);
        startActivity(moveToSignUp);
    }

    public void loginAction(){
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

    public void unregisteredPhoneNumber(){
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
