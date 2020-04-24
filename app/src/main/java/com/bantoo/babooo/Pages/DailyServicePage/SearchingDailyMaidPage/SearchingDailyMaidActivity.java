package com.bantoo.babooo.Pages.DailyServicePage.SearchingDailyMaidPage;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bantoo.babooo.Model.MaidRequest;
import com.bantoo.babooo.Pages.DailyServicePage.DetailDailyConfirmationPage.DetailDailyConfirmationActivity;
import com.bantoo.babooo.Pages.LoginPage.LoginActivity;
import com.bantoo.babooo.Pages.SignUpPage.SignUpRoleActivity;
import com.bantoo.babooo.R;
import com.bantoo.babooo.Utilities.BaseActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class SearchingDailyMaidActivity extends BaseActivity {

    private static final String TAG = "SearchingDailyMaid";
    private String orderUniqueKey, uid, userPhoneNumber;
    private Double userLatitude, userLongitude;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference artReference, userReference;
    private int maidCounter = 0, distanceMax = 2000, serviceCost, coinsAmount;
    private DatabaseReference orderReference;
    private ValueEventListener maidEventListener;
    private Date timeChoosen;
    private SharedPreferences accountDataSharedPreferences;
    private LinearLayout cancelLayout;
    private TextView userCoinsTV, serviceCostTV, serviceTypeTV;
    private Timer timeout;
    private long timerRunning = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searching_daily_maid);

        initView();
        handleAction();
        if(getIntent().getStringExtra("sender") != null &&
                getIntent().getStringExtra("sender").equals("orders")) {
            firebaseDatabase = FirebaseDatabase.getInstance();
            orderReference = firebaseDatabase.getReference().child("Order");
            orderUniqueKey = getIntent().getStringExtra("orderUniqueKey");
            userReference = firebaseDatabase.getReference().child("Users");
            initView();
            handleAction();
            showOrderInfo();
            checkARTAssigned();
        } else {
            configureTimeout();
            initData();
            findMaid();
        }
    }

    private void showOrderInfo() {
        orderReference.child(orderUniqueKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                serviceCost = Integer.parseInt(dataSnapshot.child("serviceCost").getValue().toString());
                serviceCostTV.setText(dataSnapshot.child("serviceCost").getValue().toString());
                serviceTypeTV.setText(dataSnapshot.child("serviceType").getValue().toString());
                userPhoneNumber = dataSnapshot.child("phoneNumber").getValue().toString();
                showUserCoins();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void showUserCoins() {
        userReference.orderByChild("phoneNumber").equalTo(userPhoneNumber).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    userCoinsTV.setText(snapshot.child("coins").getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void configureTimeout() {
        timeout = new Timer();
        timeout.scheduleAtFixedRate(new TimerTask(){
            @Override
            public void run(){
                timerRunning += 60;
                Log.d(TAG, "run timing: "+timerRunning);
                if(timerRunning == 300) {
                    cancelOrder();
                    Intent moveToARTNotFound = new Intent(SearchingDailyMaidActivity.this, MaidNotFoundActivity.class);
                    startActivityForResult(moveToARTNotFound, 1);
                    this.cancel();
                }
            }
        },0,60*1000);
    }

    private void initView() {
        cancelLayout = findViewById(R.id.cancel_layout_searching_daily);
        userCoinsTV = findViewById(R.id.user_coins_TV_searching);
        serviceCostTV = findViewById(R.id.cost_service_TV_searching_daily);
        serviceTypeTV = findViewById(R.id.tipeLayanan_TV_searching);
    }

    private void handleAction() {
        cancelLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        SearchingDailyMaidActivity.this);
                alertDialogBuilder.setTitle("Apakah anda ingin membatalkan pesanan?");
                alertDialogBuilder
                        .setMessage("Pesanan tidak akan dilanjutkan kembali")
                        .setCancelable(false)
                        .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                cancelOrder();
                            }
                        })
                        .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });
    }

    private void initData() {
        orderUniqueKey = getIntent().getStringExtra("orderUniqueKey");
        userLatitude = getIntent().getDoubleExtra("userLatitude", 0);
        userLongitude = getIntent().getDoubleExtra("userLongitude", 0);
        serviceCost = getIntent().getIntExtra("serviceCost", 0);
        serviceCostTV.setText(""+serviceCost);
        coinsAmount = getIntent().getIntExtra("coinsAmount", 0);
        userCoinsTV.setText(""+coinsAmount);
        firebaseDatabase = FirebaseDatabase.getInstance();
        orderReference = firebaseDatabase.getReference().child("Order");
        timeChoosen = (Date) getIntent().getSerializableExtra("timeChoosen");
        accountDataSharedPreferences = getApplicationContext().getSharedPreferences("accountData", MODE_PRIVATE);
        uid = accountDataSharedPreferences.getString("uid", "");
        userReference = firebaseDatabase.getReference().child("Users").child(uid);
    }

    private void findMaid() {
        artReference = firebaseDatabase.getReference().child("ART");
        //assign user's location
        Location userLoc = new Location("");
        userLoc.setLatitude(userLatitude);
        userLoc.setLongitude(userLongitude);
        artReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    Log.d(TAG, "findMaid: checking art working hours");
                    checkARTWorkingHours(snapshot.child("phoneNumber").getValue().toString(), snapshot, userLoc);
                }
                checkARTAssigned();
                if(maidCounter == 0 && distanceMax < 10000) {
                    distanceMax = distanceMax + 2000;
                    findMaid();
                }
                Log.d(TAG, "onDataChange: distanceMax: "+distanceMax);
                if(distanceMax == 10000 && maidCounter == 0) {
                    /*new AlertDialog.Builder(SearchingDailyMaidActivity.this)
                            .setTitle("Maid Not Found!")
                            .setMessage("Seems like there's no maid nearby.")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    cancelOrder();
                                }
                            })
                            .show();*/
                    maidCounter = -1;
                    cancelOrder();
                    timeout.cancel();
                    Intent moveToARTNotFound = new Intent(SearchingDailyMaidActivity.this, MaidNotFoundActivity.class);
                    startActivityForResult(moveToARTNotFound, 1);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1) { //request code for ARTNotFoundActivity
            if (data.getStringExtra("result").equals("back")) {
                finish();
            }
        }
    }

    private void cancelOrder() {
        orderReference.child(orderUniqueKey).removeEventListener(maidEventListener);
        orderReference.child(orderUniqueKey).removeValue();
        //balikin coinnya ya.
        if(getIntent().getStringExtra("sender") == null) {
            userReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    dataSnapshot.getRef().child("coins").setValue(Integer.parseInt(dataSnapshot.child("coins").getValue().toString()) + serviceCost);
                    finish();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        } else {
            userReference.orderByChild("phoneNumber").equalTo(userPhoneNumber).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot snapshot: dataSnapshot.getChildren()) {
                        snapshot.getRef().child("coins").setValue(Integer.parseInt(snapshot.child("coins").getValue().toString()) + serviceCost);
                        finish();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
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
                if(dataSnapshot.child("maid").getValue() == null) {
                    orderReference.child(orderUniqueKey).removeEventListener(maidEventListener);
                } else if (dataSnapshot.child("maid").getValue().toString().equals("maid") == false) {
                    timeout.cancel();
                    orderReference.child(orderUniqueKey).removeEventListener(maidEventListener);
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
