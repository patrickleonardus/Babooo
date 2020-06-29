package com.bantoo.babooo.Model;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.room.Database;

import com.bantoo.babooo.Pages.HomePage.HomeActivity;
import com.bantoo.babooo.Pages.LoginPage.LoginActivity;
import com.bantoo.babooo.Pages.SignUpPage.SignUpFormActivity;
import com.bantoo.babooo.Pages.VerificationPage.VerificationActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.InstanceIdResult;

public class FirebaseHelper {

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference reference = database.getReference();

    public static String maidUniqueKey = "";

    public void acceptOrder(String type, String orderID, String maidName, String maidPhoneNumber) {
        DatabaseReference orderReference = database.getReference();
        if(type.equals("daily")) {
            orderReference = database.getReference().child("Order");
        } else if(type.equals("monthly")) {
            orderReference = database.getReference().child("Rent");
        }
        orderReference.child(orderID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!dataSnapshot.child("maid").getValue().toString().equals("maid")
                        && !dataSnapshot.child("maidPhoneNuber").getValue().toString().equals("maidPhoneNumber")) {
                    dataSnapshot.child("maid").getRef().setValue(maidName);
                    dataSnapshot.child("maidPhoneNumber").getRef().setValue(maidPhoneNumber);
                } else {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void activateMaid(String type, String apprCode, Context context) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference maidReference = database.getReference();
        if(type.equals("daily")) {
            maidReference = database.getReference().child("ART");
        } else if(type.equals("monthly")) {
            maidReference = database.getReference().child("ARTBulanan");
        }
        maidReference.orderByChild("approvalCode").equalTo(apprCode).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    String maidUniqueKey = snapshot.getKey();
                    snapshot.child("TIMESTAMP").getRef().setValue(ServerValue.TIMESTAMP);
                    snapshot.child("activate").getRef().setValue(true);
                    snapshot.child("approvalCode").getRef().removeValue();
                    Intent moveToVerification = new Intent(context, VerificationActivity.class);
                    SharedPreferences fromSharePref = context.getSharedPreferences("verificationPage", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = fromSharePref.edit();
                    editor.putString("uid", snapshot.getKey());
                    SharedPreferences accountDataPref = context.getSharedPreferences("accountData", Context.MODE_PRIVATE);
                    accountDataPref.edit().putString("logged", "no");
                    context.startActivity(moveToVerification);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public String getMaidUniqueKey() {
        return maidUniqueKey;
    }

    public void addUser(User user, String uid, Context context) {
        DatabaseReference userReferefence = reference.child("Users").child(uid);
        userReferefence.setValue(user);
        SharedPreferences accountData = context.getSharedPreferences("accountData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = accountData.edit();
        editor.putString("phoneNumber", user.phoneNumber).commit();
        context.startActivity(new Intent(context, HomeActivity.class));
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

    public String addSalaryRequest(SalaryRequest salaryRequest) {
        DatabaseReference withdrawReference = reference.child("WithdrawRequest");
        String withdrawUniqueKey = withdrawReference.push().getKey();
        withdrawReference.child(withdrawUniqueKey).setValue(salaryRequest);
        withdrawReference.child(withdrawUniqueKey).child("TIMESTAMP").setValue(ServerValue.TIMESTAMP);
        return withdrawUniqueKey;
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
