package com.bantoo.babooo.Pages.MonthlyServicePage.DetailMonthlyConfirmationPage;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.service.autofill.Dataset;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bantoo.babooo.Pages.DailyServicePage.DetailDailyConfirmationPage.DetailDailyConfirmationActivity;
import com.bantoo.babooo.Pages.DailyServicePage.SearchingDailyMaidPage.MaidNotFoundActivity;
import com.bantoo.babooo.Pages.DailyServicePage.SearchingDailyMaidPage.SearchingDailyMaidActivity;
import com.bantoo.babooo.Pages.HomePage.HomeActivity;
import com.bantoo.babooo.R;
import com.bantoo.babooo.Utilities.BaseActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class DetailMonthlyConfirmationActivity extends BaseActivity {

    private static final String TAG = "DeatilMonthlyConfirm";

    TextView maidNameTV, maidRatingTV, maidStatusTV, durationOfWorkTV,
            orderNumberTV, dateOfWorkTV, timeOfWorkTV, estimatedFinishTimeOfWorkTV,
            orderLocationTV, serviceNameTV, serviceTypeTV, serviceCostTV,
            totalCostTV;
    ImageView maidProfilePictureIV, closeIV, optionsMenuIV, callMaidIV, messageMaidIV;
    Button helpBantooBTN;
    Timer timeout;
    private long timerRunning;
    private String orderUniqueKey, maidUniqueKey, maidPhoneNumber;
    private boolean showExtendedContract;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference monthlyRentReference, monthlyMaidReference;
    private ValueEventListener statusEventListener;

    @Override
    public void onBackPressed() { }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_monthly_confirmation);

        configureTimeout();
        initView();
        handleAction();
        initFirebase();
        configureView();
        listenMaidAccepted();
    }

    private void configureTimeout() {
        timeout = new Timer();
        timeout.scheduleAtFixedRate(new TimerTask(){
            @Override
            public void run(){
                timerRunning += 1;
                Log.d(TAG, "run timing: "+timerRunning);
                if(timerRunning == 300) {
                    cancelOrder();
                    Intent moveToARTNotFound = new Intent(DetailMonthlyConfirmationActivity.this, MaidNotFoundActivity.class);
                    startActivityForResult(moveToARTNotFound, 1);
                    this.cancel();
                }
            }
        },0,3600*1000);
    }

    private void initView() {
        closeIV = findViewById(R.id.close_detail_monthly_IV);
        optionsMenuIV = findViewById(R.id.options_menu_detail_monthly);
        callMaidIV = findViewById(R.id.call_IV_detail_monthly);
        messageMaidIV = findViewById(R.id.message_IV_detail_monthly);
        orderNumberTV = findViewById(R.id.maid_order_number_detail_monthly_confirmation_TV);
        maidProfilePictureIV = findViewById(R.id.maid_image_detail_monthly_confirmation_IV);
        maidNameTV = findViewById(R.id.maid_name_detail_monthly_confirmation_TV);
        maidRatingTV = findViewById(R.id.maid_rating_detail_monthly_confirmation_TV);
        maidStatusTV = findViewById(R.id.maid_status_detail_monthly_confirmation_TV);

        durationOfWorkTV = findViewById(R.id.durasi_maid_detail_monthly_confirmation_TV);
        dateOfWorkTV = findViewById(R.id.date_detail_monthly_confirmation_TV);
        timeOfWorkTV = findViewById(R.id.time_start_detail_monthly_confirmation_TV);
        estimatedFinishTimeOfWorkTV = findViewById(R.id.estimatedTime_detail_monthly_confirmation_TV);
        orderLocationTV = findViewById(R.id.location_detail_monthly_confirmation_TV);
        serviceTypeTV = findViewById(R.id.type_service_detail_monthly_confirmation_TV);
        serviceNameTV = findViewById(R.id.name_service_detail_monthly_confirmation_TV);
        serviceCostTV = findViewById(R.id.cost_service_detail_monthly_confirmation_TV);
        totalCostTV = findViewById(R.id.total_cost_service_detail_monthly_confirmation_TV);
        helpBantooBTN = findViewById(R.id.help_bantoo_detail_monthly_confirmation_BTN);
    }

    private void updateStatus() {
        monthlyRentReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                maidStatusTV.setText(dataSnapshot.child("status").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void configureView() {
        monthlyRentReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                maidPhoneNumber = dataSnapshot.child("maidPhoneNumber").getValue().toString();
                orderNumberTV.setText(dataSnapshot.getKey());
                maidStatusTV.setText(dataSnapshot.child("status").getValue().toString());
                durationOfWorkTV.setText(dataSnapshot.child("duration").getValue().toString() + " Bulan");
                String tanggalKedatangan = dataSnapshot.child("orderDate").getValue() + "-" + dataSnapshot.child("orderMonth").getValue()+"-"+dataSnapshot.child("orderYear").getValue();
                DateFormat format = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
                Date tanggalKedatanganDate = new Date();
                Date startWorkingDate = new Date(), endWorkingDate = new Date();
                try {
                    tanggalKedatanganDate = format.parse(tanggalKedatangan);
                    System.out.println(tanggalKedatanganDate); // Sat Jan 02 00:00:00 GMT 2010
                    Calendar c = Calendar.getInstance();
                    c.setTime(tanggalKedatanganDate);
                    c.add(Calendar.DATE, 1);
                    startWorkingDate = c.getTime();
                    c.add(Calendar.MONTH, Integer.parseInt(dataSnapshot.child("duration").getValue().toString()));
                    endWorkingDate = c.getTime();
                } catch (Exception e) {}
                Calendar endWorkingCalendar = Calendar.getInstance();
                endWorkingCalendar.setTime(endWorkingDate);
                endWorkingCalendar.add(Calendar.DATE, -14);
                if(Calendar.getInstance().before(endWorkingCalendar)) {
                    showExtendedContract = false;
                } else {
                    showExtendedContract = true;
                }

                DateFormat textFormat = new SimpleDateFormat("dd MMMM yyyy");
                dateOfWorkTV.setText(textFormat.format(tanggalKedatanganDate));
                timeOfWorkTV.setText(textFormat.format(startWorkingDate));
                estimatedFinishTimeOfWorkTV.setText(textFormat.format(endWorkingDate));
                orderLocationTV.setText(dataSnapshot.child("address").getValue().toString());
                serviceNameTV.setText(dataSnapshot.child("serviceType").getValue().toString());
                serviceCostTV.setText(dataSnapshot.child("serviceCost").getValue().toString());
                totalCostTV.setText(dataSnapshot.child("serviceCost").getValue().toString());
                updateStatus();
                getMaidData();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getMaidData() {
        monthlyMaidReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                maidNameTV.setText(dataSnapshot.child("name").getValue().toString());
                maidRatingTV.setText(dataSnapshot.child("rating").getValue().toString());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        monthlyRentReference.removeEventListener(statusEventListener);
    }

    private void listenMaidAccepted() {
        statusEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String status = dataSnapshot.child("accepted").getValue().toString();
                if(status.equals("Accepted")) {
                    timeout.cancel();
                } else if (status.equals("Rejected")) {
                    timeout.cancel();
                    cancelOrder();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        monthlyRentReference.addValueEventListener(statusEventListener);
    }

    private void initFirebase() {
        orderUniqueKey = getIntent().getStringExtra("orderUniqueKey");
        maidUniqueKey = getIntent().getStringExtra("maidUniqueKey");
        firebaseDatabase = FirebaseDatabase.getInstance();
        monthlyRentReference = firebaseDatabase.getReference().child("Rent").child(orderUniqueKey);
        monthlyMaidReference = firebaseDatabase.getReference().child("ARTBulanan").child(maidUniqueKey);
    }

    private void cancelOrder() {
        monthlyRentReference.removeEventListener(statusEventListener);
        monthlyRentReference.child(orderUniqueKey).removeValue();
    }

    private void handleAction() {
        helpBantooBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        DetailMonthlyConfirmationActivity.this);
                alertDialogBuilder.setTitle("Apakah anda ingin menelepon ART?");
                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                                callIntent.setData(Uri.parse("tel:"+"+6254123456"));
                                startActivity(callIntent);
                            }
                        })
                        .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });
        callMaidIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", maidPhoneNumber, null)));

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        DetailMonthlyConfirmationActivity.this);
                alertDialogBuilder.setTitle("Apakah anda ingin menelepon ART?");
                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                                callIntent.setData(Uri.parse("tel:"+maidPhoneNumber.replaceFirst("0", "+62")));
                                startActivity(callIntent);
                            }
                        })
                        .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });
        messageMaidIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        DetailMonthlyConfirmationActivity.this);
                alertDialogBuilder.setTitle("Apakah anda ingin mengirim SMS ke ART?");
                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Uri sms_uri = Uri.parse("smsto:" +maidPhoneNumber.replaceFirst("0", "+62"));
                                Intent sms_intent = new Intent(Intent.ACTION_VIEW, sms_uri);
                                sms_intent.setData(sms_uri);
                                sms_intent.putExtra("sms_body", "Bantoo: ");
                                startActivity(sms_intent);
                            }
                        })
                        .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });
        closeIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailMonthlyConfirmationActivity.this, HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                if(getIntent().getStringExtra("sender") != null) {
                    if(getIntent().getStringExtra("sender").equals("orderFragment")) {
                        intent.putExtra("sender", "orderFragment");
                    }
                }
                startActivity(intent);
            }
        });
        optionsMenuIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DetailMonthlyOptionsMenuDialog dialog = new DetailMonthlyOptionsMenuDialog(DetailMonthlyConfirmationActivity.this, maidUniqueKey, showExtendedContract, orderUniqueKey);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//                dialog.getWindow().setGravity(Gravity.TOP | Gravity.RIGHT);
                WindowManager.LayoutParams wmlp = dialog.getWindow().getAttributes();
                wmlp.gravity = Gravity.TOP | Gravity.RIGHT;
                wmlp.x = 0;
                wmlp.y = 100;
                dialog.show();
            }
        });
    }

}
