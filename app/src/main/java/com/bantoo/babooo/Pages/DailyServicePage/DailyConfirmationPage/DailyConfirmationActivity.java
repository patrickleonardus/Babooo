package com.bantoo.babooo.Pages.DailyServicePage.DailyConfirmationPage;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import com.bantoo.babooo.R;
import com.bantoo.babooo.Utils.BaseActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DailyConfirmationActivity extends BaseActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private static final String SERVICETYPE = "serviceType";
    private static final String SERVICENAME = "serviceName";
    private static final String SERVICECOINS = "serviceCoin";
    private static final String DATEPICKER = "datePicker";
    private static final String TIMEPICKER = "timePicker";

    private Date selectedTimeToConvert;
    private SimpleDateFormat dateFormat;
    private SimpleDateFormat format = new SimpleDateFormat("kk:mm");

    private String estimatedFinishTime, selectedDate, selectedTime;

    LinearLayout dateLayout, hourLayout, locationLayout;
    TextView serviceNameTV, detailServiceNameTV, coinsServiceTV, dateServiceTV, startHourTV, estimatedHourTV, locationServiceTV;
    Button setOrder;

    //VARIABLE UNTUK PASSING KE FIREBASE
    private String serviceName, serviceType, serviceDate, serviceHour;
    private int coinsAmount, serviceCost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_confirmation_activity);

        serviceNameTV = findViewById(R.id.serice_name_daily_confirmation_TV);
        detailServiceNameTV = findViewById(R.id.serice_name_detail_daily_confirmation_TV);
        coinsServiceTV = findViewById(R.id.coins_daily_confirmation_TV);
        dateServiceTV = findViewById(R.id.date_service_daily_confirmation_TV);
        startHourTV = findViewById(R.id.startHour_service_daily_confirmation_TV);
        estimatedHourTV = findViewById(R.id.estimatedHour_service_daily_confirmation_TV);
        locationServiceTV = findViewById(R.id.location_service_confirmation_TV);
        dateLayout = findViewById(R.id.date_service_daily_confirmation_layout);
        hourLayout = findViewById(R.id.hour_service_daily_confirmation_layout);
        locationLayout = findViewById(R.id.location_service_daily_confirmation_layout);
        setOrder = findViewById(R.id.order_daily_confirmation_BTN);

        getServiceData();
        initVar();
        handleAction();

    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);

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
        String time = selectedTime;
        Date estimatedTimeToConvert;

        try {
            selectedTimeToConvert = format.parse(time);
            estimatedTimeToConvert = format.parse(time);
            //assign date ke selected time
            selectedTime = format.format(selectedTimeToConvert);
            //convert selected time ke estimated time
            estimatedTimeToConvert.setTime(estimatedTimeToConvert.getTime() + 60 * 60 * 4000);
            estimatedFinishTime = format.format(estimatedTimeToConvert);

        } catch (ParseException e) {
        }
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

        //handle current date and time
        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("dd MMMM yyyy");
        timeFormat = new SimpleDateFormat("HH:mm");
        currDate = dateFormat.format(calendar.getTime());
        currTime = timeFormat.format(calendar.getTime());

        //handle estimated time
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

    private void handleAction() {
        dateLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment fragment = new DailyConfirmationDatePickerFragment();
                fragment.show(getSupportFragmentManager(), DATEPICKER);
            }
        });

        hourLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment fragment = new DailyConfirmationTimePickerFragment();
                fragment.show(getSupportFragmentManager(), TIMEPICKER);
            }
        });

        locationLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Cari lokasi
                Toast.makeText(getApplicationContext(), "Find Location", Toast.LENGTH_SHORT).show();
            }
        });

        setOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });
    }

    private void validateInputDate(){
        String night = "21:00";
        String morning = "05:59";
        Date nightHour, morningHour;

        try {
            nightHour = format.parse(night);
            morningHour = format.parse(morning);

            if (selectedTimeToConvert.after(nightHour) || selectedTimeToConvert.before(morningHour)) {
                Toast.makeText(getApplicationContext(), "Anda tidak bisa memesan diatas jam 9 malam dan dibawah jam 5 pagi", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), "SUKSES", Toast.LENGTH_LONG).show();
            }

        } catch (Exception e) {
        }
    }

}
