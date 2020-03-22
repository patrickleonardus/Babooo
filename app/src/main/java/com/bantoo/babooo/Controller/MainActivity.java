package com.bantoo.babooo.Controller;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bantoo.babooo.Controller.SignUpActivity.SignUpActivity;
import com.bantoo.babooo.Model.FirebaseHelper;
import com.bantoo.babooo.Model.User;
import com.bantoo.babooo.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

public class MainActivity extends AppCompatActivity {

    FirebaseHelper firebaseHelper = new FirebaseHelper();

    EditText phoneNumberET;
    Button loginBtn;

    final String TAG = "MainActivity";
    boolean restrictLogin;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference reference = database.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        phoneNumberET = findViewById(R.id.phoneNumber_login_ET);
        loginBtn = findViewById(R.id.login_btn);

        User user = new User("Tommy", "tommy@icloud.com", "0812323323", "1234567");
        firebaseHelper.addUser(user);

        setupNotification();

        generalStyling();
        handleLogin();
        phoneChecker();

    }

    public void generalStyling(){
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(getApplicationContext(),R.color.orangePrimary));
        window.setNavigationBarColor(ContextCompat.getColor(getApplicationContext(),R.color.greenPrimary));
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
                        Log.d(TAG, "onActivityResult: "+account);
                    } catch (ApiException e) {
                        Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
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
                    Log.d(TAG, "onComplete token: "+FirebaseInstanceId.getInstance().getToken());
                    Toast.makeText(MainActivity.this, "notif success", Toast.LENGTH_LONG).show();
                    final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Notification");
                    FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
                        @Override
                        public void onSuccess(InstanceIdResult instanceIdResult) {
                            reference.child("token").setValue(instanceIdResult.getToken());
                        }
                    });
                } else {
                    Toast.makeText(MainActivity.this, "notif failed", Toast.LENGTH_LONG).show();
                    Log.e(TAG, "onComplete: ", task.getException() );
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
            public void afterTextChanged(Editable s) { }
        });
    }

    public void signUpButton(View v) {

        SharedPreferences sharedPreferences = getSharedPreferences("userPref", Context.MODE_PRIVATE);
        sharedPreferences.edit().clear().commit();

        Intent moveToSignUp = new Intent(this, SignUpActivity.class);
        startActivity(moveToSignUp);
    }

    public void loginAction(){
        boolean found;
        DatabaseReference userRef = reference.child("Users");
        Query queryRef = userRef.orderByChild("phoneNumber").equalTo(phoneNumberET.getText().toString());
        queryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    Intent moveToVerification = new Intent(MainActivity.this, VerificationActivity.class);
                    moveToVerification.putExtra("phoneNumber", phoneNumberET.getText().toString());
                    startActivity(moveToVerification);
                } else {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
