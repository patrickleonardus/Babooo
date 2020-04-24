package com.bantoo.babooo.Model;


import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.InstanceIdResult;

public class FirebaseHelper {

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference reference = database.getReference();

    public void addUser(User user, String uid) {
        DatabaseReference userReferefence = reference.child("Users").child(uid);
        userReferefence.setValue(user);
    }

    public void addDailyOrder(ServiceSchedule order, String orderUniqueKey) {
        DatabaseReference orderReference = reference.child("Order").child(orderUniqueKey);
        orderReference.setValue(order);
    }

    public String addMonthlyOrder(ServiceSchedule order) {
        DatabaseReference monthlyOrderReference = reference.child("Rent");
        String orderUniqueKey = monthlyOrderReference.push().getKey();
        monthlyOrderReference.child(orderUniqueKey).setValue(order);
        return orderUniqueKey;
    }

    public void updateUserData(String uid, String oldPhoneNumber, String noHandphone,
                               String address, String email, String password) {
        DatabaseReference userReference = reference.child("Users").child(uid);
        userReference.child("phoneNumber").setValue(noHandphone);
        userReference.child("address").setValue(address);
        userReference.child("email").setValue(email);
        userReference.child("password").setValue(password);

        //change all phone number on rent and order
        DatabaseReference orderReference = reference.child("Order");
        orderReference.orderByChild("phoneNumber").equalTo(oldPhoneNumber).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    snapshot.getRef().child("phoneNumber").setValue(noHandphone);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        DatabaseReference rentReference = reference.child("Rent");
        rentReference.orderByChild("phoneNumber").equalTo(oldPhoneNumber).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    snapshot.getRef().child("phoneNumber").setValue(noHandphone);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
