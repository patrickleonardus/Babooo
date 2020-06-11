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
    private String phoneNumber, artType, estimatedMinute, estimatedTime;
    private Integer estimatedHour;

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
        artType = accountDataSharedPreferences.getString("artType", "");
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
                                            String orderMonth = snapshot.child("orderMonth").getValue().toString();
                                            String orderYear = snapshot.child("orderYear").getValue().toString();
                                            String serviceType = snapshot.child("serviceType").getValue().toString();
                                            String maid = snapshot.child("maid").getValue().toString();
                                            String status = snapshot.child("status").getValue().toString();
                                            String orderTime = snapshot.child("orderTime").getValue().toString();
                                            estimatedHour = Integer.parseInt(orderTime.substring(0,2)) + 2;
                                            estimatedMinute = orderTime.substring(3,5);
                                            if(estimatedHour < 10) {
                                                estimatedTime = "0"+estimatedHour+":"+estimatedMinute; }
                                            else { estimatedTime = estimatedHour+":"+estimatedMinute; }
                                            String address = snapshot.child("address").getValue().toString();
                                            String noteLocation = snapshot.child("notesLocation").getValue().toString();
                                            String maidPhoneNumber = snapshot.child("maidPhoneNumber").getValue().toString();
                                            Integer serviceCost = Integer.parseInt(snapshot.child("serviceCost").getValue().toString());
                                            ServiceSchedule serviceSchedule = new ServiceSchedule(orderDate,
                                                    serviceType, maid, orderMonth, status, orderTime, address, maidPhoneNumber);
                                            serviceSchedule.setOrderID(snapshot.getKey());
                                            serviceSchedule.setOrderYear(orderYear);
                                            serviceSchedule.setNotesLocation(noteLocation);
                                            serviceSchedule.setServiceCost(serviceCost);
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
        if (artType.equals("daily")) {
            Intent intent = new Intent(this, MaidDailyDetailHistoryActivity.class);
            intent.putExtra("orderKey", serviceScheduleList.get(position).getOrderID());
            intent.putExtra("user_name", bossServiceName.get(position));
            intent.putExtra("orderDate", serviceScheduleList.get(position).getOrderDate()+" "
                    +serviceScheduleList.get(position).getOrderMonth()+" "+serviceScheduleList.get(position).getOrderYear());
            intent.putExtra("timeStart", serviceScheduleList.get(position).getOrderTime());
            intent.putExtra("timeEnd", estimatedTime);
            intent.putExtra("location", serviceScheduleList.get(position).getAddress());
            intent.putExtra("noteLocation", serviceScheduleList.get(position).getNotesLocation());
            intent.putExtra("serviceType", serviceScheduleList.get(position).getServiceType());
            intent.putExtra("serviceCost", serviceScheduleList.get(position).getServiceCost());
            intent.putExtra("rating", serviceScheduleList.get(position).getRating());
            intent.putExtra("comment", serviceScheduleList.get(position).getComment());
            startActivity(intent);
        }
        else if (artType.equals("monthly")){
            Intent intent = new Intent(this, MaidMonthlyDetailHistoryActivity.class);
            intent.putExtra("orderKey", serviceScheduleList.get(position).getOrderID());
            startActivity(intent);
        }
    }
}
