package com.bantoo.babooo.Pages.MonthlyServicePage.SalaryConfirmationPage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.bantoo.babooo.R;
import com.bantoo.babooo.Utilities.BaseActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

public class SalaryConfirmationActivity extends BaseActivity {

    Spinner monthSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salary_confirmation);

        initView();
    }

    private void initView() {
        monthSpinner = findViewById(R.id.spinner_month_salary_confirmation);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, android.R.id.text1);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        monthSpinner.setAdapter(spinnerAdapter);
        for(int i=1; i<=6; i++) {
            spinnerAdapter.add("Bulan ke "+i);
        }
        spinnerAdapter.notifyDataSetChanged();
    }
}
