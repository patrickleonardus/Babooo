package com.bantoo.babooo.Pages.DailyServicePage.SearchingDailyMaidPage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.bantoo.babooo.Model.MaidRequest;
import com.bantoo.babooo.Pages.DailyServicePage.DetailDailyConfirmationPage.DetailDailyConfirmationActivity;
import com.bantoo.babooo.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Locale;

public class SearchingDailyMaidActivity extends AppCompatActivity {

    private static final String TAG = "SearchingDailyMaid";
    private String orderUniqueKey;
    private Double userLatitude, userLongitude;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference artReference;
    private int maidCounter = 0;
    private int distanceMax = 2000;
    private DatabaseReference orderReference;
    private ValueEventListener maidEventListener;
    private Date timeChoosen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searching_daily_maid);

        initData();
        findMaid();
    }

    private void initData() {
        orderUniqueKey = getIntent().getStringExtra("orderUniqueKey");
        userLatitude = getIntent().getDoubleExtra("userLatitude", 0);
        userLongitude = getIntent().getDoubleExtra("userLongitude", 0);
        firebaseDatabase = FirebaseDatabase.getInstance();
        orderReference = firebaseDatabase.getReference().child("Order");
        timeChoosen = (Date) getIntent().getSerializableExtra("timeChoosen");
    }

    private void findMaid() {
        artReference = firebaseDatabase.getReference().child("ART");
        //assign user's location
        Location userLoc = new Location("");
        userLoc.setLatitude(userLatitude);
        userLoc.setLongitude(userLongitude);
        maidCounter = 0;
        artReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    Log.d(TAG, "findMaid: checking art working hours");
                    checkARTWorkingHours(snapshot.child("phoneNumber").getValue().toString(), snapshot, userLoc);
                }
                checkARTAssigned();
                if(maidCounter == 0 && distanceMax <= 10000) {
                    distanceMax = distanceMax * 2;
                    findMaid();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void checkARTWorkingHours(String maidPhoneNumber, DataSnapshot snapshot, Location userLoc) {
        orderReference.orderByChild("maidPhoneNumber").equalTo(maidPhoneNumber).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //check working hours, jangan sampe tabrakan
                boolean workingValid = true;
                for(DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    String date = snapshot.child("orderDate").getValue().toString();
                    String month = snapshot.child("orderMonth").getValue().toString();
                    String year = snapshot.child("orderYear").getValue().toString();
                    String time = snapshot.child("orderTime").getValue().toString();
                    DateFormat format = new SimpleDateFormat("dd-MM-yyyy'T'HH:mm", Locale.ENGLISH);
                    String fullTime = date+"-"+month+"-"+year+"T"+time;
                    try {
                        Date orderDate = format.parse(fullTime);
                        //in minutes
                        long difference = (orderDate.getTime() - timeChoosen.getTime())/60000;
                        //if working time and order time below 4 hours
                        Log.d(TAG, "artworkinghours: timeDifference: "+difference);
                        if(Math.abs(difference) < 240) {
                            workingValid = false;
                        }
                    } catch (ParseException e) {
                        Log.e(TAG, "dateParsing: ", e);
                        e.printStackTrace();
                    }
                }
                if(workingValid) {
                    //get location of each maids
                    if (snapshot.child("latitude").getValue() != null && snapshot.child("longitude").getValue() != null && (boolean) snapshot.child("activate").getValue() == true) {
                        Double maidLatitude = (double) snapshot.child("latitude").getValue();
                        Double maidLongitude = (double) snapshot.child("longitude").getValue();
                        Location artLocation = new Location("");
                        artLocation.setLatitude(maidLatitude);
                        artLocation.setLongitude(maidLongitude);
                        //distance of user and maids in meters
                        //below 2kms
                        if (userLoc.distanceTo(artLocation) < distanceMax) {
                            maidCounter++;
                            Log.d(TAG, "checkLocationART: maid name= "+snapshot.child("name").getValue().toString());
                            MaidRequest maidRequest = new MaidRequest(snapshot.child("name").getValue().toString(), snapshot.child("phoneNumber").getValue().toString());
                            //snapshot.getRef().child("maidLists").push().setValue(maidRequest);
                            orderReference.child(orderUniqueKey).child("maidList").push().setValue(maidRequest);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //wants to check ART has been found or not.
    private void checkARTAssigned() {
        maidEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //if the maid has been found. the data will no longer "maid"
                Log.d(TAG, "onDataChange: "+dataSnapshot);
                if (dataSnapshot.child("maid").getValue().toString().equals("maid") == false) {
                    Log.d(TAG, "onDataChange: "+dataSnapshot.child("maid").getValue().toString());
                    Intent moveToDetail = new Intent(SearchingDailyMaidActivity.this, DetailDailyConfirmationActivity.class);
                    moveToDetail.putExtra("orderUniqueKey", orderUniqueKey);
                    startActivity(moveToDetail);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        orderReference.child(orderUniqueKey).addValueEventListener(maidEventListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        orderReference.removeEventListener(maidEventListener);
    }
}
