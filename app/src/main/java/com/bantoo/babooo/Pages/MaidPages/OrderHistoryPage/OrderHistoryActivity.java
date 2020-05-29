package com.bantoo.babooo.Pages.MaidPages.OrderHistoryPage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.bantoo.babooo.Model.ServiceSchedule;
import com.bantoo.babooo.Pages.MaidPages.MaidDailyDetailOrderActivity;
import com.bantoo.babooo.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OrderHistoryActivity extends AppCompatActivity implements OrderHistoryClickedRV {

    private static final String TAG = "RiwayatPesanan";

    private RecyclerView orderListRV;
    private ImageView closeIV;
    private OrderHistoryAdapter orderHistoryAdapter;
    private List<ServiceSchedule> serviceScheduleList = new ArrayList<>();
    private List<String> bossServiceName = new ArrayList<>();

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference orderReference, userReference;
    private String phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_riwayat_pesanan);

        initView();
        initFirebase();
        configureRecyclerView();
        loadOrderList();
    }

    private void initFirebase() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        SharedPreferences accountDataSharedPreferences = getSharedPreferences("accountData", Context.MODE_PRIVATE);
        String artType = accountDataSharedPreferences.getString("artType", "");
        phoneNumber = accountDataSharedPreferences.getString("phoneNumber", "");
        if(artType.equals("daily")) {
            orderReference = firebaseDatabase.getReference().child("Order");
        } else if(artType.equals("monthly")) {
            orderReference = firebaseDatabase.getReference().child("Rent");
        }
        userReference = firebaseDatabase.getReference().child("Users");
    }

    private void loadOrderList() {
        serviceScheduleList.clear();
        orderReference.orderByChild("maidPhoneNumber").equalTo(phoneNumber).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Log.d(TAG, "loadOrderList: order(1)");
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd'T'HH:mm");
                    Date orderDate = new Date();
                    try {
                        orderDate = sdf.parse(snapshot.child("orderYear").getValue() + "/" +
                                snapshot.child("orderMonth").getValue() + "/" +
                                snapshot.child("orderDate").getValue() + "T" +
                                snapshot.child("orderTime").getValue());
                    } catch(Exception e) {
                        Log.d(TAG, "loadOrderList: date parsing failed");
                    }
                    if(orderDate.before(new Date())) {
                        userReference.orderByChild("phoneNumber").equalTo(snapshot.child("phoneNumber").getValue().toString())
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot userDataSnapshot) {
                                        for (DataSnapshot userSnapshot : userDataSnapshot.getChildren()) {
                                            bossServiceName.add(userSnapshot.child("name").getValue().toString());
                                            String orderDate = snapshot.child("orderDate").getValue().toString();
                                            String serviceType = snapshot.child("serviceType").getValue().toString();
                                            String maid = snapshot.child("maid").getValue().toString();
                                            String orderMonth = snapshot.child("orderMonth").getValue().toString();
                                            String status = snapshot.child("status").getValue().toString();
                                            String orderTime = snapshot.child("orderTime").getValue().toString();
                                            String address = snapshot.child("address").getValue().toString();
                                            String maidPhoneNumber = snapshot.child("maidPhoneNumber").getValue().toString();
                                            ServiceSchedule serviceSchedule = new ServiceSchedule(orderDate,
                                                    serviceType, maid, orderMonth, status, orderTime, address, maidPhoneNumber);
                                            serviceSchedule.setOrderID(snapshot.getKey());
                                            if(snapshot.child("rating").getValue() != null) {
                                                try {
                                                    serviceSchedule.setRating(Double.parseDouble(snapshot.child("rating").getValue().toString()));
                                                } catch (Exception e) {
                                                    serviceSchedule.setRating(0.0);
                                                }
                                            }
                                            serviceScheduleList.add(serviceSchedule);
                                            orderHistoryAdapter.notifyDataSetChanged();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void configureRecyclerView() {
        orderHistoryAdapter = new OrderHistoryAdapter(serviceScheduleList, bossServiceName, this);
        orderListRV.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        orderListRV.setAdapter(orderHistoryAdapter);
    }

    private void initView() {
        orderListRV = findViewById(R.id.order_list_rv);
        closeIV = findViewById(R.id.close_IV);
        closeIV.setOnClickListener(v -> {
            finish();
        });
    }

    @Override
    public void onClickOrderHistory(int position) {
        Intent intent = new Intent(this, MaidDailyDetailOrderActivity.class);
        intent.putExtra("orderKey", serviceScheduleList.get(position).getOrderID());
        startActivity(intent);
    }
}
