package com.bantoo.babooo.Model;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseHelper {

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference reference = database.getReference();

    public void addUser(User user, String uid) {
        DatabaseReference userReferefence = reference.child("Users").child(uid);
        userReferefence.setValue(user);
    }
}
