package com.bantoo.babooo.Controller;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.bantoo.babooo.Model.FirebaseHelper;
import com.bantoo.babooo.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
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
    SignInButton signInButton;
    final String TAG = "MainActivity";

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference reference = database.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        phoneNumberET = findViewById(R.id.phoneNumberET);
        signInButton = findViewById(R.id.google_sign_in_button);
        /*
        User user = new User("Tommy", "tommy@icloud.com", "0812323323", "12345678");
        firebaseHelper.addUser(user);*/

        setupGoogleSignIn();
        setupNotification();
    }

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
    }

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

    public void loginButton(View v) {
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

    public void signUpButton(View v) {
        Intent moveToSignUp = new Intent(this, SignUpActivity.class);
        startActivity(moveToSignUp);
    }
}
