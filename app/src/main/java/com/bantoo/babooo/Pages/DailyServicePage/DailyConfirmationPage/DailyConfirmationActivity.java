package com.bantoo.babooo.Pages.DailyServicePage.DailyConfirmationPage;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import com.bantoo.babooo.Pages.LocationPage.DefineLocationActivity;
import com.bantoo.babooo.Pages.MonthlyServicePage.MonthlyConfirmationPage.MonthlyConfirmationActivity;
import com.bantoo.babooo.R;
import com.bantoo.babooo.Utilities.BaseActivity;
import com.bantoo.babooo.Utilities.DatePickerFragment;
import com.bantoo.babooo.Utilities.TimePickerFragment;

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
    private static final String NIGHTHOUR = "nightHour";
    private static final String MORNINGHOUR = "morningHour";

    private Date timeChoosen, dateChoosen, today, now;
    private SimpleDateFormat dateFormat;
    private SimpleDateFormat format = new SimpleDateFormat("kk:mm");

    private String estimatedFinishTime, selectedTime;

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

        getServiceData();
        initVar();
        handleAction();

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
            selectedTime = format.format(selectedTimeToConvert);
            //convert selected time ke estimated time
            estimatedTimeToConvert.setTime(estimatedTimeToConvert.getTime() + 60 * 60 * 4000);
            estimatedFinishTime = format.format(estimatedTimeToConvert);

        } catch (ParseException e) {
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
                startActivity(intent);
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
                //CEK LAGI TOM CEK LAGI YA
                //CEK LAGI TOM CEK LAGI YA

                if (dateChoosen.before(today)) {
                    Toast.makeText(getApplicationContext(), "Tanggal yang anda pilih sudah terlewat, silahkan periksa kembali", Toast.LENGTH_LONG).show();
                } else if (timeChoosen.after(nightHour)) {
                    Toast.makeText(getApplicationContext(), "Anda tidak dapat memesan diatas jam 9 malam dan dibawah jam 5 pagi", Toast.LENGTH_LONG).show();
                } else if(timeChoosen.before(morningHour)){
                    Toast.makeText(getApplicationContext(), "Anda tidak dapat memesan diatas jam 9 malam dan dibawah jam 5 pagi", Toast.LENGTH_LONG).show();
                }
                else {
                    //DISINI FUNGSI BUAT BIKIN ORDERNYA, PARAMETER UNTUK PASSING DATA SUDAH SIAP DIATAS.
                    //PARAMETER buat data ordernya dah siap diatas, tinggal assign ke valuenya sama pake doang
                    Toast.makeText(getApplicationContext(), "Make an order", Toast.LENGTH_LONG).show();
                }

            }
        });
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

            } catch (Exception e) {
            }
        } else if (hour.equals(MORNINGHOUR)) {
            try {
                morningHour = format.parse(morning);
                date = morningHour;
            } catch (Exception e) {
            }
        }
        return date;
    }

}
