package com.bantoo.babooo.Pages.MonthlyServicePage.MonthlyConfirmationPage;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.bantoo.babooo.Model.FirebaseHelper;
import com.bantoo.babooo.Model.ServiceSchedule;
import com.bantoo.babooo.Pages.DailyServicePage.DailyConfirmationPage.DailyConfirmationActivity;
import com.bantoo.babooo.Pages.LocationPage.DefineLocationActivity;
import com.bantoo.babooo.Pages.MonthlyServicePage.DetailMonthlyConfirmationPage.DetailMonthlyConfirmationActivity;
import com.bantoo.babooo.R;
import com.bantoo.babooo.Utilities.BaseActivity;
import com.bantoo.babooo.Utilities.DatePickerFragment;
import com.bantoo.babooo.Utilities.TimePickerFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MonthlyConfirmationActivity extends BaseActivity implements AdapterView.OnItemSelectedListener, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private static final String DATEPICKER = "datePicker";
    private static final String TIMEPICKER = "timePicker";
    private static final int REQUEST_LOCATION = 1;
    private static final String TAG = "MonthlyConfirmation";

    TextView serviceNameTV, serviceDateTV, serviceTimeTV, estimatedTimeTV, locationTV, serviceDetailNameTV, serviceCostTV, syaratTV;
    LinearLayout serviceDateLayout, serviceTimeLayout, serviceLocationLayout;
    Button setOrderBTN;
    Spinner durationSP;
    Date timeChoosen, dateChoosen;
    ImageView closeBtn;

    private SimpleDateFormat dateFormat;
    private SimpleDateFormat format = new SimpleDateFormat("kk:mm");
    private String estimatedFinishTime, selectedTime;
    private Date today, now;

    //VARIABLE YANG BISA DI PASS KE FIREBASE
    private String duration, maidUniqueKey, orderUniqueKey;
    private int serviceCost;
    private Double userLatitude, userLongitude;
    private String notesLocation;

    SharedPreferences accountDataSharedPreferences;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference userReference, monthlyMaidReference;

    //CLASS INI SAMA SEKALI BELOM DI VALIDASI
    //HARUS VALIDASI TANGGAL,WAKTU MULAI DAN SEMUA KOMPONENNYA

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monthly_confirmation);

        initView();
        initVar();

        handleAction();
        calculateCoins();
    }

    private void termsAndConditionClicked() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(R.string.base_url+"termsAndCondition.html"));
        startActivity(intent);
    }

    private void initView() {
        serviceNameTV = findViewById(R.id.serice_name_monthly_confirmation_TV);
        serviceDateTV = findViewById(R.id.date_service_monthly_confirmation_TV);
        serviceDateLayout = findViewById(R.id.date_service_monthly_confirmation_layout);
        serviceTimeTV = findViewById(R.id.startHour_service_month_confirmation_TV);
        serviceTimeLayout = findViewById(R.id.hour_service_month_confirmation_layout);
        estimatedTimeTV = findViewById(R.id.estimatedHour_service_month_confirmation_TV);
        locationTV = findViewById(R.id.location_month_service_confirmation_TV);
        serviceLocationLayout = findViewById(R.id.location_service_month_confirmation_layout);
        serviceDetailNameTV = findViewById(R.id.serice_name_detail_month_confirmation_TV);
        serviceCostTV = findViewById(R.id.coins_month_confirmation_TV);
        setOrderBTN = findViewById(R.id.order_month_confirmation_BTN);
        durationSP = findViewById(R.id.spinner_duration_confirmation_SP);
        closeBtn = findViewById(R.id.close_monthly_confirmation_IV);
        syaratTV = findViewById(R.id.syaratTV);
        durationSP.setOnItemSelectedListener(this);
    }

    private void initVar() {
        maidUniqueKey = getIntent().getStringExtra("maidUniqueKey");
        dateFormat = new SimpleDateFormat("dd MMMM yyyy");
        Calendar calendar, estimatedTimeCal;
        SimpleDateFormat timeFormat;
        String currDate, currTime;

        //handle default current date and time
        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("dd MMMM yyyy");
        timeFormat = new SimpleDateFormat("HH:mm");
        today = calendar.getTime();
        now = calendar.getTime();
        dateChoosen = calendar.getTime();
        timeChoosen = calendar.getTime();
        currDate = dateFormat.format(calendar.getTime());
        currTime = timeFormat.format(calendar.getTime());

        //handle default estimated time
        estimatedTimeCal = Calendar.getInstance();
        estimatedTimeCal.add(Calendar.HOUR, 4);
        estimatedFinishTime = timeFormat.format(estimatedTimeCal.getTime());

        /*serviceNameTV.setText(serviceType + " | " + serviceName);
        serviceDetailNameTV.setText(serviceName);*/
        serviceDateTV.setText(currDate);
        serviceTimeTV.setText(currTime);
        estimatedTimeTV.setText(estimatedFinishTime);
        //coinsServiceTV.setText(String.valueOf(serviceCost));

        serviceDetailNameTV.setText("Bantoo Bulanan");

        firebaseDatabase = FirebaseDatabase.getInstance();
        accountDataSharedPreferences = getApplicationContext().getSharedPreferences("accountData", MODE_PRIVATE);
        String uid = accountDataSharedPreferences.getString("uid", "");
        userReference = firebaseDatabase.getReference().child("Users").child(uid);
        monthlyMaidReference = firebaseDatabase.getReference().child("ARTBulanan").child(maidUniqueKey);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case (REQUEST_LOCATION) :
                if(resultCode == Activity.RESULT_OK) {
                    locationTV.setText(data.getStringExtra("address"));
                    userLatitude = data.getDoubleExtra("latitude", 0);
                    userLongitude = data.getDoubleExtra("longitude", 0);
                    notesLocation = data.getStringExtra("notes");
                    Log.d(TAG, "onActivityResult: latitude= "+ userLatitude +", longitude = "+ userLongitude);
                }
                break;
        }
    }

    private void handleAction() {
        syaratTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                termsAndConditionClicked();
            }
        });
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        serviceDateLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment fragment = new DatePickerFragment();
                fragment.show(getSupportFragmentManager(), DATEPICKER);
            }
        });

        /*serviceTimeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment fragment = new TimePickerFragment();
                fragment.show(getSupportFragmentManager(), TIMEPICKER);
            }
        });*/

        serviceLocationLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MonthlyConfirmationActivity.this, DefineLocationActivity.class);
                startActivityForResult(intent, REQUEST_LOCATION);
            }
        });

        setOrderBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //SET ORDER
                //BELUM DIVALIDASI DAN BELUM ADA PASSING DATA KE PAGE SELANJUTNYA
                //set order jika semua validasi terpenuhi

                if (dateChoosen.before(today)) { //if the dateChoosen < current date
                    Toast.makeText(getApplicationContext(), "Waktu yang anda pilih sudah terlewat, silahkan periksa kembali", Toast.LENGTH_LONG).show();
                } else if (userLatitude==null) {
                    Toast.makeText(MonthlyConfirmationActivity.this, "Alamat tidak ditemukan, silahkan periksa kembali", Toast.LENGTH_SHORT).show();
                } else {
                    checkCoins();
                }
            }
        });
    }

    private void checkCoins() {
        userReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int coinsAmount = Integer.parseInt(dataSnapshot.child("coins").getValue().toString());
                if(coinsAmount < serviceCost) {
                    new AlertDialog.Builder(MonthlyConfirmationActivity.this)
                            .setTitle("Koin tidak mencukupi")
                            .setMessage("Koin anda tidak mencukupi untuk memesan layanan ini")
                            .setPositiveButton("Tambah Koin", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //move to purchase coins page
                                }
                            })
                            .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .show();
                } else {
                    dataSnapshot.child("coins").getRef().setValue(Integer.parseInt(dataSnapshot.child("coins").getValue().toString()) - serviceCost);
                    processOrder();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void calculateCoins() {
        monthlyMaidReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int maidSalary = Integer.parseInt(dataSnapshot.child("salary").getValue().toString());
                int feePercentage = 0;
                Log.d(TAG, "calculateCoins: duration: "+duration);
                switch (Integer.parseInt(duration)) {
                    case 1:
                    case 3:
                        feePercentage = 20;
                        break;
                    case 6:
                        feePercentage = 15;
                        break;
                    case 12:
                        feePercentage = 12;
                        break;
                    default:
                        feePercentage = 0;
                        break;
                }
                Log.d(TAG, "calculateCoins: salary: "+maidSalary);
                Log.d(TAG, "calculateCoins: feePercent: "+feePercentage);
                float totalSalary = maidSalary * Integer.parseInt(duration);
                float totalFeeInIDR = totalSalary * feePercentage / 100;
                Log.d(TAG, "calculateCoins: totalFeeIDR: "+totalFeeInIDR);
                serviceCost = (int) totalFeeInIDR / 300;
                Log.d(TAG, "calculateCoins: serviceCost"+serviceCost);
                serviceCostTV.setText(""+serviceCost);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    
    private void processOrder() {
        monthlyMaidReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dataSnapshot.getRef().child("working").setValue(true);
                String phoneNumber = accountDataSharedPreferences.getString("phoneNumber", "");
                String date, month, hours, minutes;
                if(timeChoosen.getDate() < 10) { date = "0"+timeChoosen.getDate(); } else { date = ""+timeChoosen.getDate(); }
                if(timeChoosen.getMonth() < 10) { month = "0"+(timeChoosen.getMonth()+1); } else { month = ""+(timeChoosen.getMonth()+1); }
                String maid = dataSnapshot.child("name").getValue().toString();
                String maidPhoneNumber = dataSnapshot.child("phoneNumber").getValue().toString();
                ServiceSchedule order = new ServiceSchedule(""+date, "Bantoo Bulanan", maid, ""+month, "Akan Datang", 0+":"+0, locationTV.getText().toString(), maidPhoneNumber);
                order.setPhoneNumber(phoneNumber);
                order.setServiceCost(serviceCost);
                order.setOrderYear(""+(timeChoosen.getYear()+1900));
                order.setAccepted("none");
                order.setDuration(duration);
                order.setNotesLocation(notesLocation);
                order.setLongitude(userLongitude);
                order.setLatitude(userLatitude);
                FirebaseHelper firebaseHelper = new FirebaseHelper();
                //add order to firebase and assign orderID
                orderUniqueKey = firebaseHelper.addMonthlyOrder(order);
                moveToMonthlyDetailConfirmationPage();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    //HANDLE SPINNER DURATION
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //duration = parent.getSelectedItem().toString();

        //convertToTime();

        duration = "";
        int counter = 0;
        while (durationSP.getSelectedItem().toString().charAt(counter) != ' ') {
            duration += durationSP.getSelectedItem().toString().charAt(counter);
            counter++;
        }
        Calendar c = Calendar.getInstance();
        c.setTime(dateChoosen);
        c.add(Calendar.DATE, 1);
        serviceTimeTV.setText(dateFormat.format(c.getTime()));
        c.add(Calendar.MONTH, Integer.parseInt(duration));
        estimatedTimeTV.setText(dateFormat.format(c.getTime()));
        calculateCoins();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    //HANDLE SAAT DATE DI SET
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        dateChoosen = c.getTime();

        timeChoosen.setDate(dateChoosen.getDate());
        timeChoosen.setMonth(dateChoosen.getMonth());
        timeChoosen.setYear(dateChoosen.getYear());

        String selectedDate;
        selectedDate = dateFormat.format(c.getTime());
        serviceDateTV.setText(selectedDate);

        c.add(Calendar.DATE, 1);
        serviceTimeTV.setText(dateFormat.format(c.getTime()));
        String duration = "";
        int counter = 0;
        while(durationSP.getSelectedItem().toString().charAt(counter) != ' ') {
            duration += durationSP.getSelectedItem().toString().charAt(counter);
            counter++;
        }
        c.add(Calendar.MONTH, Integer.parseInt(duration));
        estimatedTimeTV.setText(dateFormat.format(c.getTime()));
    }

    //HANDLE SAAT TIME DI SET
    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        selectedTime = String.format("%02d:%02d", hourOfDay, minute);

        convertToTime();

        serviceTimeTV.setText(selectedTime);
        estimatedTimeTV.setText(estimatedFinishTime);
    }

    private void moveToMonthlyDetailConfirmationPage(){
        Intent intent = new Intent(this, DetailMonthlyConfirmationActivity.class);
        intent.putExtra("orderUniqueKey", orderUniqueKey);
        intent.putExtra("maidUniqueKey", maidUniqueKey);
        startActivity(intent);
    }

    private void convertToTime() {
        //Func ini buat convert int dari timepicker ke date, jadi mudah bandingin antar datenya

        String time = selectedTime;
        Date selectedTimeToConvert, estimatedTimeToConvert;

        try {
            selectedTimeToConvert = format.parse(time);
            estimatedTimeToConvert = format.parse(time);
            //assign date ke selected time
            timeChoosen = selectedTimeToConvert;
            //to make timechoosen and datechoosen value same. here I set the date,month and year of datechoosen into timeChoosen.
            //actually dateChoosen isn't necessary. But I'm too lazy to edit the code :p
            timeChoosen.setDate(dateChoosen.getDate());
            timeChoosen.setMonth(dateChoosen.getMonth());
            timeChoosen.setYear(dateChoosen.getYear());
            selectedTime = format.format(selectedTimeToConvert);
            //convert selected time ke estimated time
            estimatedTimeToConvert.setTime(estimatedTimeToConvert.getTime() + 60 * 60 * 4000);
            estimatedFinishTime = format.format(estimatedTimeToConvert);

        } catch (ParseException e) { }
    }
}
