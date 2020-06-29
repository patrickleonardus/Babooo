package com.bantoo.babooo.Pages.MaidPages;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bantoo.babooo.R;
import com.bantoo.babooo.Utilities.BaseActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ReceiveSalaryConfirmationActivity extends BaseActivity {

    TextView bossNameTV, incomeStartDateTV, orderNumberTV, monthlyIncomeStatusTV, periodTV,
            baseIncomeTV, totalIncomeTV, thisTotalIncomeTV, currDateTV, maidNameTV;
    Button salaryConfirmBtn;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference rentReference, userReference, maidReference;
    String phoneNumber;
    String bossPhoneNumber;
    int runningMonth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive_salary_confirmation);

        initView();
        firebaseInit();
        showView();
        handleAction();
    }

    private void handleAction() {
        salaryConfirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paymentDone();
            }
        });
    }

    private void paymentDone() {
        rentReference.orderByChild("maidPhoneNumber").equalTo(phoneNumber).limitToLast(1)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot snapshot: dataSnapshot.getChildren()) {
                            Map<String, Object> salaryConfirmMap = new HashMap<String, Object>();
                            salaryConfirmMap.put("ART", "Sudah dikonfirmasi oleh ART");
                            snapshot.getRef().child("gaji Bulan ke "+runningMonth).setValue(salaryConfirmMap);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private void firebaseInit() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        rentReference = firebaseDatabase.getReference().child("Rent");
        userReference = firebaseDatabase.getReference().child("Users");
        maidReference = firebaseDatabase.getReference().child("ARTBulanan");
        phoneNumber = getIntent().getStringExtra("phoneNumber");
        runningMonth = getIntent().getIntExtra("runningMonth", 0);
    }

    private void showView() {
        rentReference.orderByChild("maidPhoneNumber").equalTo(phoneNumber).limitToLast(1)
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    bossPhoneNumber = snapshot.child("phoneNumber").getValue().toString();
                    /*int monthOrder = Integer.parseInt(snapshot.child("orderMonth").getValue().toString());
                    String month = new DateFormatSymbols().getMonths()[monthOrder-1];*/
                    orderNumberTV.setText(snapshot.getKey());
                    String duration = snapshot.child("duration").getValue().toString();
                    monthlyIncomeStatusTV.setText("Bulan ke "+runningMonth+" dari "+duration);
                    String startDate = snapshot.child("orderDate").getValue().toString();
                    String startMonth = snapshot.child("orderMonth").getValue().toString();
                    String month = new DateFormatSymbols().getMonths()[Integer.parseInt(startMonth)-1];
                    String startYear = snapshot.child("orderYear").getValue().toString();
                    String combineStartOrderDate = startDate + "-" + startMonth + "-" + startYear;
                    DateFormat format = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
                    try {
                        Date startOrder = format.parse(combineStartOrderDate);
                        Calendar c = Calendar.getInstance();
                        c.setTime(startOrder);
                        c.add(Calendar.MONTH, Integer.parseInt(duration));
                        Date endOrder = c.getTime();
                        String endMonth = new DateFormatSymbols().getMonths()[endOrder.getMonth()-1];
                        periodTV.setText(startDate + " " + month + " - " + endOrder.getDate() + " " + endMonth + (endOrder.getYear()+1900));
                        incomeStartDateTV.setText(startDate + " " + month + " - " + endOrder.getDate() + " " + endMonth + (endOrder.getYear()+1900));
                    } catch (Exception e) {}
                    getUserData();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getUserData() {
        userReference.orderByChild("phoneNumber").equalTo(bossPhoneNumber).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    bossNameTV.setText(snapshot.child("name").getValue().toString());
                    getMaidData();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getMaidData() {
        maidReference.orderByChild("phoneNumber").equalTo(phoneNumber).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    int salary = Integer.parseInt(snapshot.child("salary").getValue().toString());
                    String formattedSalary = NumberFormat.getNumberInstance(Locale.GERMAN).format(salary);
                    baseIncomeTV.setText("Rp "+formattedSalary);
                    totalIncomeTV.setText("Rp "+formattedSalary);
                    maidNameTV.setText(""+snapshot.child("name").getValue().toString());
                    Date now = new Date();
                    String month = new DateFormatSymbols().getMonths()[now.getMonth()-1];
                    currDateTV.setText(now.getDate() + " " +month + " " + (now.getYear()+1900));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void initView() {
        bossNameTV = findViewById(R.id.boss_TV);
        incomeStartDateTV = findViewById(R.id.tanggal_pemberian_gaji_TV);
        orderNumberTV = findViewById(R.id.nomor_pesanan_TV);
        monthlyIncomeStatusTV = findViewById(R.id.gaji_bulanan_status_TV);
        periodTV = findViewById(R.id.periode_TV);
        baseIncomeTV = findViewById(R.id.gajiPokok_TV);
        totalIncomeTV = findViewById(R.id.gaji_total_TV);
        thisTotalIncomeTV = findViewById(R.id.gaji_total_tersebut_TV);
        currDateTV = findViewById(R.id.tanggal_sekarang_TV);
        maidNameTV = findViewById(R.id.name_maid_TV);
        salaryConfirmBtn = findViewById(R.id.get_salary_confirm_Btn);
    }
}
