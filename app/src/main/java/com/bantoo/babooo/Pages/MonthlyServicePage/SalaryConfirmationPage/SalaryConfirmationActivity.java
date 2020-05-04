package com.bantoo.babooo.Pages.MonthlyServicePage.SalaryConfirmationPage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bantoo.babooo.R;
import com.bantoo.babooo.Utilities.BaseActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class SalaryConfirmationActivity extends BaseActivity {

    private static final String TAG = "SalaryConfirmation";

    private Spinner monthSpinner;
    private TextView nomorPesananTV, salaryMaidTV, finishEstimationTV, nameMaidTV, startDateTV, ratingMaidTV;
    private ImageView tanggalPembayaranIV;
    private String rentUniqueKey;
    private Button confirmSalaryBtn;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference rentReference, monthlyMaidReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salary_confirmation);

        initVar();
        initView();
        initFirebase();
    }

    private void initFirebase() {
        rentUniqueKey = getIntent().getStringExtra("rentUniqueKey");
        firebaseDatabase = FirebaseDatabase.getInstance();
        rentReference = firebaseDatabase.getReference().child("Rent").child(rentUniqueKey);
        monthlyMaidReference = firebaseDatabase.getReference().child("ARTBulanan");
        loadRentData();
    }

    private void loadRentData() {
        rentReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                nomorPesananTV.setText(dataSnapshot.getKey());
                String startDate = dataSnapshot.child("orderDate").getValue()+"-"+dataSnapshot.child("orderMonth").getValue()+"-"+dataSnapshot.child("orderYear").getValue();
                DateFormat textFormat = new SimpleDateFormat("dd MMMM yyyy");
                DateFormat format = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
                Date startWorkingDate = new Date(), endWorkingDate = new Date();
                try {
                    startWorkingDate = format.parse(startDate);
                    Calendar c = Calendar.getInstance();
                    c.setTime(startWorkingDate);
                    String duration = dataSnapshot.child("duration").getValue().toString();
                    updateSpinnerData(duration);
                    c.add(Calendar.MONTH, Integer.parseInt(duration));
                    endWorkingDate = c.getTime();
                } catch (Exception e) {}
                startDateTV.setText(textFormat.format(startWorkingDate));
                finishEstimationTV.setText(textFormat.format(endWorkingDate));
                nameMaidTV.setText(dataSnapshot.child("maid").getValue().toString());
                loadMaidData(dataSnapshot.child("maidPhoneNumber").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void updateSpinnerData(String duration) {
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, android.R.id.text1);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        monthSpinner.setAdapter(spinnerAdapter);
        for(int i=1; i<=Integer.parseInt(duration); i++) {
            spinnerAdapter.add("Bulan ke "+i);
        }
        Log.d(TAG, "updateSpinnerData: ");
        spinnerAdapter.notifyDataSetChanged();
    }

    private void loadMaidData(String maidPhoneNumber) {
        monthlyMaidReference.orderByChild("phoneNumber").equalTo(maidPhoneNumber).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    salaryMaidTV.setText(snapshot.child("salary").getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void initVar() {
        rentUniqueKey = getIntent().getStringExtra("rentUniqueKey");
    }

    private void initView() {
        salaryMaidTV = findViewById(R.id.salary_maid_salary_confirm);
        finishEstimationTV = findViewById(R.id.finish_estimation_TV_salary_confirm);
        nameMaidTV = findViewById(R.id.name_maid_TV_salary_confirm);
        startDateTV = findViewById(R.id.startDate_TV_salary_confirm);
        nomorPesananTV = findViewById(R.id.nomor_pesanan_TV_salary_confirm);
        ratingMaidTV = findViewById(R.id.rating_maid_TV_salary_confirm);
        tanggalPembayaranIV = findViewById(R.id.tanggal_pembayaran_IV_salary_confirm);

        monthSpinner = findViewById(R.id.spinner_month_salary_confirmation);
        confirmSalaryBtn = findViewById(R.id.salary_month_confirmation_BTN);
        confirmSalaryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> salaryConfirmMap = new HashMap<String, Object>();
                salaryConfirmMap.put("user", "Sudah Dibayar");
                rentReference.child("gaji "+monthSpinner.getSelectedItem()).updateChildren(salaryConfirmMap);
                finish();
            }
        });
    }
}
