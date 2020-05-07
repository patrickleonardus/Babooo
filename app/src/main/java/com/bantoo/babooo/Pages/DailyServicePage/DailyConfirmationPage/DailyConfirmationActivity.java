package com.bantoo.babooo.Pages.DailyServicePage.DailyConfirmationPage;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.bantoo.babooo.Model.ServiceSchedule;
import com.bantoo.babooo.Pages.DailyServicePage.SearchingDailyMaidPage.SearchingDailyMaidActivity;
import com.bantoo.babooo.Pages.LocationPage.DefineLocationActivity;
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

public class DailyConfirmationActivity extends BaseActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private static final String TAG = "DailyConfirmation";

    //FIREBASE INIT
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference orderReference, userReference;

    private SharedPreferences accountDataSharedPreferences;

    private static final String SERVICETYPE = "serviceType";
    private static final String SERVICENAME = "serviceName";
    private static final String SERVICECOINS = "serviceCoin";
    private static final String DATEPICKER = "datePicker";
    private static final String TIMEPICKER = "timePicker";
    private static final String NIGHTHOUR = "nightHour";
    private static final String MORNINGHOUR = "morningHour";

    private static final int REQUEST_LOCATION = 1;

    private Date timeChoosen, dateChoosen, today, now;
    private SimpleDateFormat dateFormat;
    private SimpleDateFormat format = new SimpleDateFormat("kk:mm");

    private String estimatedFinishTime, selectedTime;
    private Double userLatitude, userLongitude;
    private String notesLocation;

    LinearLayout dateLayout, hourLayout, locationLayout;
    TextView serviceNameTV, detailServiceNameTV, coinsServiceTV, dateServiceTV, startHourTV, estimatedHourTV, locationServiceTV;
    Button setOrder;

    //VARIABLE UNTUK PASSING KE FIREBASE
    private String serviceName, serviceType, serviceDate, serviceHour;
    private int coinsAmount, serviceCost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_confirmation);

        initView();
        getServiceData();
        initVar();
        initFirebase();
        handleAction();
    }

    private void initFirebase() {
        firebaseDatabase = FirebaseDatabase.getInstance();

        accountDataSharedPreferences = getApplicationContext().getSharedPreferences("accountData", MODE_PRIVATE);
        String uid = accountDataSharedPreferences.getString("uid", "");

        orderReference = firebaseDatabase.getReference().child("Order");
        userReference = firebaseDatabase.getReference().child("Users").child(uid);
    }

    private void initView() {
        serviceNameTV = findViewById(R.id.service_name_daily_confirmation_TV);
        detailServiceNameTV = findViewById(R.id.service_name_detail_daily_confirmation_TV);
        coinsServiceTV = findViewById(R.id.coins_daily_confirmation_TV);
        dateServiceTV = findViewById(R.id.date_service_daily_confirmation_TV);
        startHourTV = findViewById(R.id.startHour_service_daily_confirmation_TV);
        estimatedHourTV = findViewById(R.id.estimatedHour_service_daily_confirmation_TV);
        locationServiceTV = findViewById(R.id.location_service_confirmation_TV);
        dateLayout = findViewById(R.id.date_service_daily_confirmation_layout);
        hourLayout = findViewById(R.id.hour_service_daily_confirmation_layout);
        locationLayout = findViewById(R.id.location_service_daily_confirmation_layout);
        setOrder = findViewById(R.id.order_daily_confirmation_BTN);
    }

    private void getServiceData() {
        serviceName = getIntent().getStringExtra(SERVICENAME);
        serviceType = getIntent().getStringExtra(SERVICETYPE);
        serviceCost = getIntent().getIntExtra(SERVICECOINS, 50);
    }

    private void initVar() {

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

        serviceNameTV.setText(serviceType + " | " + serviceName);
        detailServiceNameTV.setText(serviceName);
        dateServiceTV.setText(currDate);
        startHourTV.setText(currTime);
        estimatedHourTV.setText(estimatedFinishTime);
        coinsServiceTV.setText(String.valueOf(serviceCost));
    }

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
        dateServiceTV.setText(selectedDate);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        selectedTime = String.format("%02d:%02d", hourOfDay, minute);

        convertToTime();

        startHourTV.setText(selectedTime);
        estimatedHourTV.setText(estimatedFinishTime);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case (REQUEST_LOCATION) :
                if(resultCode == Activity.RESULT_OK) {
                    locationServiceTV.setText(data.getStringExtra("address"));
                    userLatitude = data.getDoubleExtra("latitude", 0);
                    userLongitude = data.getDoubleExtra("longitude", 0);
                    notesLocation = data.getStringExtra("notes");
                    Log.d(TAG, "onActivityResult: latitude= "+ userLatitude +", longitude = "+ userLongitude);
                }
                break;
        }
    }

    private void handleAction() {
        dateLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment fragment = new DatePickerFragment();
                fragment.show(getSupportFragmentManager(), DATEPICKER);
            }
        });

        hourLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment fragment = new TimePickerFragment();
                fragment.show(getSupportFragmentManager(), TIMEPICKER);
            }
        });

        locationLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DailyConfirmationActivity.this, DefineLocationActivity.class);
                startActivityForResult(intent, REQUEST_LOCATION);
            }
        });

        setOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Date nightHour = validateInputTime(NIGHTHOUR);
                Date morningHour = validateInputTime(MORNINGHOUR);

                //TOMMY TOLONG CEK LAGI INI LOGICNYA MASIH BARBAR, di else if kedua dan ketiga salah harusnya ga gitu
                //kan gamungkin  dia pesen diatas jam 9 malem sama sebelum jam 6 pagi gitu,
                //intinya di else if kedua dan ketiga masih salah, lu ganti aja pake logic lu
                //CEK LAGI TOM CEK LAGI YA
                //CEK LAGI TOM CEK LAGI YA

                //SIAP BOSQ NTR GW BENERIN LOGIC LU
                Log.d("DailyConfirmation", "onClick: date= "+new Date());
                Log.d("DailyConfirmation", "onClick: datechoosen= "+dateChoosen);
                Log.d("DailyConfirmation", "onClick: timeChoosen = "+timeChoosen);
                if (dateChoosen.before(today)) { //if the dateChoosen < current date
                    Toast.makeText(getApplicationContext(), "Waktu yang anda pilih sudah terlewat, silahkan periksa kembali", Toast.LENGTH_LONG).show();
                } else if (timeChoosen.getHours() >= 21 || timeChoosen.getHours() < 6) { //if the time hours >=21 or <6
                    Toast.makeText(DailyConfirmationActivity.this, "Layanan tidak tersedia pada pukul 21.00 sampai 05.59, silahkan pilih waktu lain", Toast.LENGTH_SHORT).show();
                } else {
                    if(timeChoosen.before(new Date())) { //if the timeChoosen before current time. new Date() is current time.
                        Toast.makeText(DailyConfirmationActivity.this, "Waktu sudah terlewat, silahkan periksa kembali", Toast.LENGTH_SHORT).show();
                    } else {
                        if (userLatitude==null) {
                            Toast.makeText(DailyConfirmationActivity.this, "Please input your address", Toast.LENGTH_SHORT).show();
                        } else {
                            checkCoins();
                        }
                    }
                }
            }
        });
    }

    private void checkCoins() {
        userReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d(TAG, "coinsCheck: snapshot= "+dataSnapshot);
                coinsAmount = Integer.parseInt(dataSnapshot.child("coins").getValue().toString());
                if(coinsAmount < serviceCost) {
                   new AlertDialog.Builder(DailyConfirmationActivity.this)
                           .setTitle("Coins not Enough")
                           .setMessage("Your coins aren't enough to use this service")
                           .setPositiveButton("Buy Coins", new DialogInterface.OnClickListener() {
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
                    Toast.makeText(DailyConfirmationActivity.this, "make order", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void processOrder() {
        SharedPreferences accountDataSharedPreferences = getSharedPreferences("accountData", MODE_PRIVATE);
        String phoneNumber = accountDataSharedPreferences.getString("phoneNumber", "");
        String year, date, month, hours, minutes;
        if(timeChoosen.getDate() < 10) { date = "0"+timeChoosen.getDate(); } else { date = ""+timeChoosen.getDate(); }
        if(timeChoosen.getMonth() < 10) { month = "0"+(timeChoosen.getMonth()+1); } else { month = ""+(timeChoosen.getMonth()+1); }
        if(timeChoosen.getHours() < 10) { hours = "0"+timeChoosen.getHours(); } else { hours = ""+timeChoosen.getHours(); }
        if(timeChoosen.getMinutes() < 10) { minutes = "0"+timeChoosen.getMinutes(); } else { minutes = ""+timeChoosen.getMinutes(); }
        ServiceSchedule order = new ServiceSchedule(""+date, serviceName, "maid", ""+month, "Akan Datang", hours+":"+minutes, locationServiceTV.getText().toString(), "maidPhoneNumber");
        order.setPhoneNumber(phoneNumber);
        order.setLatitude(userLatitude);
        order.setLongitude(userLongitude);
        order.setNotesLocation(notesLocation);
        order.setServiceCost(serviceCost);
        order.setOrderYear(""+(timeChoosen.getYear()+1900));
        String orderUniqueKey = orderReference.push().getKey();
        orderReference.child(orderUniqueKey).setValue(order);
        Intent moveToSearchingPage = new Intent(this, SearchingDailyMaidActivity.class);
        moveToSearchingPage.putExtra("orderUniqueKey", orderUniqueKey);
        moveToSearchingPage.putExtra("userLatitude", userLatitude);
        moveToSearchingPage.putExtra("userLongitude", userLongitude);
        moveToSearchingPage.putExtra("timeChoosen", timeChoosen);
        moveToSearchingPage.putExtra("serviceCost", serviceCost);
        moveToSearchingPage.putExtra("coinsAmount", coinsAmount);
        startActivity(moveToSearchingPage);
    }

    private Date validateInputTime(String hour) {
        String night = "21:00";
        String morning = "05:59";
        Date nightHour, morningHour;
        Date date = Calendar.getInstance().getTime();

        if (hour.equals(NIGHTHOUR)) {
            try {
                nightHour = format.parse(night);
                date = nightHour;

            } catch (Exception e) { Log.e("DailyConfirmation", "validateInputTime: "+e, new Throwable()); }
        } else if (hour.equals(MORNINGHOUR)) {
            try {
                morningHour = format.parse(morning);
                date = morningHour;
            } catch (Exception e) { Log.e("DailyConfirmation", "validateInputTime: "+e, new Throwable()); }
        }
        return date;
    }

}
