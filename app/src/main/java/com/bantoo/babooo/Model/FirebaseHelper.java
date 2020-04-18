package com.bantoo.babooo.Model;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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

}
