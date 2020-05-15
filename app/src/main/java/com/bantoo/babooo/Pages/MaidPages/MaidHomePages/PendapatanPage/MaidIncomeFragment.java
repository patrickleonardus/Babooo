package com.bantoo.babooo.Pages.MaidPages.MaidHomePages.PendapatanPage;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.bantoo.babooo.Pages.MaidPages.ReceiveSalaryConfirmationActivity;
import com.bantoo.babooo.Pages.MaidPages.WithdrawSalaryFormActivity;
import com.bantoo.babooo.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class MaidIncomeFragment extends Fragment implements LocationListener {

    private static final String TAG = "PendapatanFragment";

    TextView statusTV, totalCoinsTV, inRupiahTV, dailyCoinsTV, dailyPercentageTV, ratingMaidTV, dailyCoinsTargetTV, usernameTV;
    Switch activeSwitch;
    ProgressBar incomePB;
    RelativeLayout withdrawIncomeLayout;

    String phoneNumber;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference maidReference, orderReference;

    SharedPreferences accountDataSharedPreferences;
    SharedPreferences.Editor editor;
    LocationManager locationManager;

    int diffInMonth, targetKoin = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_maid_income, container, false);

        statusTV = rootView.findViewById(R.id.status_TV);
        totalCoinsTV = rootView.findViewById(R.id.total_koin_TV);
        inRupiahTV = rootView.findViewById(R.id.setara_rupiah_koin_TV);
        dailyCoinsTV = rootView.findViewById(R.id.koin_harian_TV);
        dailyPercentageTV = rootView.findViewById(R.id.percentageTarget_TV);
        ratingMaidTV = rootView.findViewById(R.id.rating_maid_TV);
        dailyCoinsTargetTV = rootView.findViewById(R.id.target_koin_harianTV);
        activeSwitch = rootView.findViewById(R.id.active_switch);
        incomePB = rootView.findViewById(R.id.pendapatan_PB);
        usernameTV = rootView.findViewById(R.id.username_income_maid_tv);
        withdrawIncomeLayout = rootView.findViewById(R.id.penarikan_gaji_RL);

        accountDataSharedPreferences = getActivity().getSharedPreferences("accountData", Context.MODE_PRIVATE);
        editor = accountDataSharedPreferences.edit();
        String artType = accountDataSharedPreferences.getString("artType", "");
        Log.d(TAG, "onCreateView: arttype: "+artType);
        if(artType.equals("daily")) {
            withdrawIncomeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent withdrawIntent = new Intent(getContext(), WithdrawSalaryFormActivity.class);
                    withdrawIntent.putExtra("phoneNumber", phoneNumber);
                    startActivity(withdrawIntent);
                }
            });
            initFirebase();
            showData();
        } else if(artType.equals("monthly")) {
            TextView typeMaidTV = rootView.findViewById(R.id.type_maid_TV);
            TextView kontrakBerlangsung = rootView.findViewById(R.id.pendapatan_TV);
            ImageView coinsPendapatanIV = rootView.findViewById(R.id.coins_logo_pendapatan_IV);
            ImageView coinsPendapatanHariIniIV = rootView.findViewById(R.id.coins_logo_hari_ini_IV);
            TextView konfirmasiGajiTV = rootView.findViewById(R.id.cairkan_koin_TV);
            coinsPendapatanIV.setVisibility(View.GONE);
            coinsPendapatanHariIniIV.setVisibility(View.GONE);
            typeMaidTV.setText("Mitra Bantoo Bulanan");
            kontrakBerlangsung.setText("Kontrak Berlangsung");
            konfirmasiGajiTV.setText("Konfirmasi Terima Gaji");
            withdrawIncomeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent receiveSalaryIntent = new Intent(getContext(), ReceiveSalaryConfirmationActivity.class);
                    receiveSalaryIntent.putExtra("phoneNumber", phoneNumber);
                    receiveSalaryIntent.putExtra("runningMonth", diffInMonth);
                    startActivity(receiveSalaryIntent);
                }
            });
            initFirebaseMonthly();
            showDataMonthly();
        }
        getCurrentLocation();

        return rootView;
    }

    private void getCurrentLocation() {
        try {
            locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5, this);
        }
        catch(SecurityException e) {
            e.printStackTrace();
        }
    }

    private void uploadLocationData(Double latitude, Double longitude) {
        maidReference.orderByChild("phoneNumber").equalTo(phoneNumber).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    snapshot.child("latitude").getRef().setValue(latitude);
                    snapshot.child("longitude").getRef().setValue(longitude);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void initFirebaseMonthly() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        maidReference = firebaseDatabase.getReference().child("ARTBulanan");
        orderReference = firebaseDatabase.getReference().child("Rent");
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("accountData", Context.MODE_PRIVATE);
        phoneNumber = sharedPreferences.getString("phoneNumber", "");
        Log.d(TAG, "initFirebaseMonthly: phoneNumber "+phoneNumber);
    }

    private void showDataMonthly() {
        maidReference.orderByChild("phoneNumber").equalTo(phoneNumber).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    usernameTV.setText(snapshot.child("name").getValue().toString());
                    String salary = snapshot.child("salary").getValue().toString();
                    String tidySalary = NumberFormat.getNumberInstance(Locale.GERMAN).format(Integer.parseInt(salary));
                    totalCoinsTV.setText("Rp "+tidySalary);
                    showRentData(salary);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void showRentData(String salary) {
        orderReference.orderByChild("maidPhoneNumber").equalTo(phoneNumber).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    int rating = 0;
                    int totalOrder = 0;
                    if(!snapshot.child("status").getValue().toString().equals("Sudah Selesai")) {
                        if(snapshot.child("rating").getValue() != null) {
                            rating += Integer.parseInt(snapshot.child("rating").getValue().toString());
                            totalOrder++;
                        }
                        int duration = Integer.parseInt(snapshot.child("duration").getValue().toString());
                        int totalSalary = duration * Integer.parseInt(salary);
                        String tidyTotalSalary = NumberFormat.getNumberInstance(Locale.GERMAN).format(totalSalary);
                        inRupiahTV.setText("dari total kontrak Rp. " + tidyTotalSalary);
                        Date now = new Date();
                        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
                        String orderDate = snapshot.child("orderDate").getValue().toString();
                        String orderMonth = snapshot.child("orderMonth").getValue().toString();
                        String orderYear = snapshot.child("orderYear").getValue().toString();
                        try {
                            Date current = sdf.parse("" + now.getMonth() + "/" + now.getDate() + "/" + (now.getYear() + 1900));
                            Date firstOrder = sdf.parse("" + orderMonth + "/" + orderDate + "/" + orderYear);
                            long diffInMillies = Math.abs(current.getTime() - firstOrder.getTime());
                            long diffInDays = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
                            Log.d(TAG, "onDataChange: diffinDays: "+diffInDays);
                            diffInMonth = ((int) diffInDays / 30) + 1;
                            Log.d(TAG, "onDataChange: diffinMonth: "+diffInMonth);
                            String periodeBulan = "Bulan ke "+diffInMonth;
                            dailyCoinsTV.setText(periodeBulan);
                            incomePB.setProgress(diffInMonth);
                            incomePB.setMax(duration);
                            float percentageDuration = (diffInMonth / duration) * 100;
                            dailyPercentageTV.setText((int) percentageDuration + "%");
                            dailyCoinsTargetTV.setText("duration: " + duration + " bulan");
                        } catch (Exception e) {}
                        if(rating != 0 && totalOrder != 0) {
                            float averageRating = rating / totalOrder;
                            ratingMaidTV.setText(averageRating + "");
                        } else {
                            ratingMaidTV.setText("0");
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void initFirebase() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        maidReference = firebaseDatabase.getReference().child("ART");
        orderReference = firebaseDatabase.getReference().child("Order");
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("accountData", Context.MODE_PRIVATE);
        phoneNumber = sharedPreferences.getString("phoneNumber", "");
    }

    private void showData() {
        maidReference.orderByChild("phoneNumber").equalTo(phoneNumber).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    editor.putString("maidName", snapshot.child("name").getValue().toString());
                    editor.apply();
                    usernameTV.setText(snapshot.child("name").getValue().toString());
                    if(snapshot.child("activate").getValue().toString().equals("true")) {
                        statusTV.setText("Aktif");
                    } else if (snapshot.child("activate").getValue().toString().equals("false")) {
                        statusTV.setText("Tidak Aktif");
                    }
                    String totalKoin = "0";
                    if(snapshot.child("coins").getValue() != null) {
                        totalKoin = snapshot.child("coins").getValue().toString();
                        totalCoinsTV.setText(totalKoin+" koin");
                    } else { totalCoinsTV.setText("0 koin"); }
                    inRupiahTV.setText("Setara dengan Rp. "+(Integer.parseInt(totalKoin)*3000));
                    if(snapshot.child("target").getValue() != null) {
                        targetKoin = Integer.parseInt(snapshot.child("target").getValue().toString());
                    }
                    showDailyData();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void showDailyData() {
        orderReference.orderByChild("maidPhoneNumber").equalTo(phoneNumber).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int counter = 0;
                int totalRating = 0;
                int totalFee = 0;
                String currentDate = ""+new Date().getDate();
                String currentMonth = ""+(new Date().getMonth() + 1);
                String currentYear = ""+(new Date().getYear() + 1900);
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    String orderDate = snapshot.child("orderDate").getValue().toString();
                    String orderMonth = snapshot.child("orderMonth").getValue().toString();
                    String orderYear = snapshot.child("orderYear").getValue().toString();
                    if(snapshot.child("rating").getValue() != null) {
                        totalRating += Integer.parseInt(snapshot.child("rating").getValue().toString());
                    }
                    Log.d(TAG, "showDailyData: now: "+currentDate+currentMonth+currentYear);
                    Log.d(TAG, "showDailyData: order: "+orderDate+orderMonth+orderYear);
                    if(Integer.parseInt(currentDate) == Integer.parseInt(orderDate) &&
                            Integer.parseInt(currentMonth) == Integer.parseInt(orderMonth) &&
                            Integer.parseInt(currentYear) == Integer.parseInt(orderYear)) {
                        totalFee += Integer.parseInt(snapshot.child("serviceCost").getValue().toString());
                        counter++;
                    }
                }
                if(counter == 0) counter = 1;
                float averageRating = totalRating / counter;
                dailyCoinsTV.setText(totalFee+" koin");
                ratingMaidTV.setText(""+averageRating);
                dailyCoinsTargetTV.setText("target: "+ targetKoin +" koin");
                incomePB.setProgress(totalFee);
                if (targetKoin == 0) {
                    dailyPercentageTV.setText("0%");
                } else {
                    dailyPercentageTV.setText("" + totalFee / targetKoin * 100);
                }
                incomePB.setMax(targetKoin);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onLocationChanged(Location location) {
        Double latitudeLocation = location.getLatitude();
        Double longitudeLocation = location.getLongitude();
        uploadLocationData(latitudeLocation, longitudeLocation);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
