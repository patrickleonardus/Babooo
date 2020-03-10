package com.bantoo.babooo.Controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.bantoo.babooo.Model.FirebaseHelper;
import com.bantoo.babooo.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    FirebaseHelper firebaseHelper = new FirebaseHelper();
    EditText phoneNumberET;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference reference = database.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        phoneNumberET = findViewById(R.id.phoneNumberET);
        /*
        User user = new User("Tommy", "tommy@icloud.com", "0812323323", "12345678");
        firebaseHelper.addUser(user);*/
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

}
