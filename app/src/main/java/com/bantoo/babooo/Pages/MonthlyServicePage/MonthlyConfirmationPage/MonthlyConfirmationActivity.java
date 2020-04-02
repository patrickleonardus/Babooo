package com.bantoo.babooo.Pages.MonthlyServicePage.MonthlyConfirmationPage;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import com.bantoo.babooo.Pages.LocationPage.DefineLocationActivity;
import com.bantoo.babooo.Pages.MonthlyServicePage.DetailMonthlyConfirmationPage.DetailMonthlyConfirmationActivity;
import com.bantoo.babooo.R;
import com.bantoo.babooo.Utilities.BaseActivity;
import com.bantoo.babooo.Utilities.DatePickerFragment;
import com.bantoo.babooo.Utilities.TimePickerFragment;

public class MonthlyConfirmationActivity extends BaseActivity implements AdapterView.OnItemSelectedListener, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private static final String DATEPICKER = "datePicker";
    private static final String TIMEPICKER = "timePicker";

    TextView serviceNameTV, serviceDateTV, serviceTimeTV, estimatedTimeTV, locationTV, serviceDetailNameTV, serviceCostTV;
    LinearLayout serviceDateLayout, serviceTimeLayout, serviceLocationLayout;
    Button setOrderBTN;
    Spinner durationSP;

    //VARIABLE YANG BISA DI PASS KE FIREBASE
    private String duration;

    //CLASS INI SAMA SEKALI BELOM DI VALIDASI
    //HARUS VALIDASI TANGGAL,WAKTU MULAI DAN SEMUA KOMPONENNYA

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monthly_confirmation);

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
        durationSP.setOnItemSelectedListener(this);

        handleAction();
    }

    private void handleAction() {
        serviceDateLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment fragment = new DatePickerFragment();
                fragment.show(getSupportFragmentManager(), DATEPICKER);
            }
        });

        serviceTimeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment fragment = new TimePickerFragment();
                fragment.show(getSupportFragmentManager(), TIMEPICKER);
            }
        });

        serviceLocationLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MonthlyConfirmationActivity.this, DefineLocationActivity.class);
                startActivity(intent);
            }
        });

        setOrderBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //SET ORDER
                //BELUM DIVALIDASI DAN BELUM ADA PASSING DATA KE PAGE SELANJUTNYA
                //set order jika semua validasi terpenuhi
                moveToMonthlyDetailConfirmationPage();
            }
        });
    }

    //HANDLE SPINNER DURATION
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        duration = parent.getSelectedItem().toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    //HANDLE SAAT DATE DI SET
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

    }

    //HANDLE SAAT TIME DI SET
    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

    }

    private void moveToMonthlyDetailConfirmationPage(){
        Intent intent = new Intent(this, DetailMonthlyConfirmationActivity.class);
        startActivity(intent);
    }
}
