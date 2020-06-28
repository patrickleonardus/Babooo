package com.bantoo.babooo.Pages.HomePage.OrderPage;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bantoo.babooo.Model.ServiceSchedule;
import com.bantoo.babooo.Pages.DailyServicePage.DetailDailyConfirmationPage.DetailDailyConfirmationActivity;
import com.bantoo.babooo.Pages.DailyServicePage.SearchingDailyMaidPage.SearchingDailyMaidActivity;
import com.bantoo.babooo.Pages.MonthlyServicePage.DetailMonthlyConfirmationPage.DetailMonthlyConfirmationActivity;
import com.bantoo.babooo.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class OrdersFragment extends Fragment implements OrderItemClickListener {

    private static final String TAG = "OrderFragment";
    private static final int RUNNING_ORDER = 0;
    private static final int PREVIOUS_ORDER = 1;

    List<ServiceSchedule> serviceScheduleList = new ArrayList<ServiceSchedule>();
    List<String> maidIDList = new ArrayList<String>();

    RecyclerView historyRV;
    private HistoryRecyclerViewAdapter historyRecyclerViewAdapter;
    private LinearLayoutManager historyLayoutManager;

    private TextView runningOrderTV, previousOrderTV;
    private View runningOrderLine, previousOrderLine;
    private LinearLayout noDataLayout, runningOrderLayout, previousTransactionLayout;

    SharedPreferences accountDataSharedPreferences;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference orderReference, rentReference, monthlyMaidReference;
    private String phoneNumber;

    private int currentTypeOrder;

    public OrdersFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_home_orders, container, false);
        historyRV = rootView.findViewById(R.id.history_RV);
        runningOrderLine = rootView.findViewById(R.id.running_order_line_order);
        runningOrderLayout = rootView.findViewById(R.id.running_order_layout);
        previousOrderLine = rootView.findViewById(R.id.previous_transaction_line_order);
        previousTransactionLayout = rootView.findViewById(R.id.previous_transaction_layout);
        runningOrderTV = rootView.findViewById(R.id.running_order_TV_order);
        previousOrderTV = rootView.findViewById(R.id.previous_transaction_TV_order);
        noDataLayout = rootView.findViewById(R.id.noDataOrderLayout);
        previousOrderLine.setVisibility(View.GONE);
        currentTypeOrder = RUNNING_ORDER;

        firebaseInit();
        setClickListener();

        retrieveOrderData();
        setupRecyclerView();

        return rootView;
    }

    private void firebaseInit() {
        accountDataSharedPreferences = getActivity().getSharedPreferences("accountData", MODE_PRIVATE);
        phoneNumber = accountDataSharedPreferences.getString("phoneNumber", "");
        firebaseDatabase = FirebaseDatabase.getInstance();
        orderReference = firebaseDatabase.getReference().child("Order");
        rentReference = firebaseDatabase.getReference().child("Rent");
        monthlyMaidReference = firebaseDatabase.getReference().child("ARTBulanan");
    }

    private void setClickListener() {
        runningOrderLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                previousOrderLine.setVisibility(View.GONE);
                runningOrderLine.setVisibility(View.VISIBLE);
                currentTypeOrder = RUNNING_ORDER;
                retrieveOrderData();
            }
        });
        previousTransactionLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runningOrderLine.setVisibility(View.GONE);
                previousOrderLine.setVisibility(View.VISIBLE);
                currentTypeOrder = PREVIOUS_ORDER;
                retrieveOrderData();
            }
        });
    }

    private void retrieveRentData() {
        rentReference.orderByChild("phoneNumber").equalTo(phoneNumber).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    if(currentTypeOrder == RUNNING_ORDER) {
                        if(!snapshot.child("status").getValue().toString().equals("Kontrak Habis")) {
                            String orderDate = snapshot.child("orderDate").getValue().toString();
                            String serviceType = snapshot.child("serviceType").getValue().toString();
                            String maid = snapshot.child("maid").getValue().toString();
                            if (maid.equals("Maid")) {
                                maid = "-";
                            }
                            String orderMonth = snapshot.child("orderMonth").getValue().toString();
                            int monthNumber = Integer.parseInt(orderMonth);
                            String status = snapshot.child("status").getValue().toString();
                            String orderTime = snapshot.child("orderTime").getValue().toString();
                            String address = snapshot.child("address").getValue().toString();
                            String maidPhoneNumber = snapshot.child("maidPhoneNumber").getValue().toString();
                            ServiceSchedule serviceSchedule = new ServiceSchedule(orderDate, serviceType, maid,
                                    new DateFormatSymbols().getMonths()[monthNumber-1], status, orderTime, address, maidPhoneNumber);
                            serviceSchedule.setOrderID(snapshot.getKey());
                            getMaidKey(maidPhoneNumber);
                            serviceScheduleList.add(serviceSchedule);
                        }
                    } else if(currentTypeOrder == PREVIOUS_ORDER) {
                        if(snapshot.child("status").getValue().toString().equals("Kontrak Habis")) {
                            String orderDate = snapshot.child("orderDate").getValue().toString();
                            String serviceType = snapshot.child("serviceType").getValue().toString();
                            String maid = snapshot.child("maid").getValue().toString();
                            if (maid.equals("Maid")) {
                                maid = "-";
                            }
                            String orderMonth = snapshot.child("orderMonth").getValue().toString();
                            int monthNumber = Integer.parseInt(orderMonth);
                            String status = snapshot.child("status").getValue().toString();
                            String orderTime = snapshot.child("orderTime").getValue().toString();
                            String address = snapshot.child("address").getValue().toString();
                            String maidPhoneNumber = snapshot.child("maidPhoneNumber").getValue().toString();
                            ServiceSchedule serviceSchedule = new ServiceSchedule(orderDate, serviceType, maid,
                                    new DateFormatSymbols().getMonths()[monthNumber-1], status, orderTime, address, maidPhoneNumber);
                            serviceSchedule.setOrderID(snapshot.getKey());
                            getMaidKey(maidPhoneNumber);
                            serviceScheduleList.add(serviceSchedule);
                        }
                    }
                }
                checkData();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getMaidKey(String maidPhoneNumber) {
        monthlyMaidReference.orderByChild("phoneNumber").equalTo(maidPhoneNumber).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    maidIDList.add(snapshot.getKey());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void retrieveOrderData() {
        Log.d(TAG, "retrieveOrderData: "+phoneNumber);
        serviceScheduleList.clear();
        orderReference.orderByChild("phoneNumber").equalTo(phoneNumber).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    if (currentTypeOrder == RUNNING_ORDER) {
                        if (!snapshot.child("status").getValue().toString().equals("Pesanan Selesai")) {
                            String orderDate = snapshot.child("orderDate").getValue().toString();
                            String serviceType = snapshot.child("serviceType").getValue().toString();
                            String maid = snapshot.child("maid").getValue().toString();
                            if (maid.equals("Maid")) {
                                maid = "-";
                            }
                            String orderMonth = snapshot.child("orderMonth").getValue().toString();
                            int monthNumber = Integer.parseInt(orderMonth);
                            String status = snapshot.child("status").getValue().toString();
                            String orderTime = snapshot.child("orderTime").getValue().toString();
                            String address = snapshot.child("address").getValue().toString();
                            String maidPhoneNumber = snapshot.child("maidPhoneNumber").getValue().toString();
                            ServiceSchedule serviceSchedule = new ServiceSchedule(orderDate, serviceType, maid,
                                    new DateFormatSymbols().getMonths()[monthNumber-1], status, orderTime, address, maidPhoneNumber);
                            serviceSchedule.setOrderID(snapshot.getKey());
                            serviceScheduleList.add(serviceSchedule);
                        }
                    } else if (currentTypeOrder == PREVIOUS_ORDER) {
                        if(snapshot.child("status").getValue().toString().equals("Pesanan Selesai")) {
                            String orderDate = snapshot.child("orderDate").getValue().toString();
                            String serviceType = snapshot.child("serviceType").getValue().toString();
                            String maid = snapshot.child("maid").getValue().toString();
                            if (maid.equals("Maid")) {
                                maid = "-";
                            }
                            String orderMonth = snapshot.child("orderMonth").getValue().toString();
                            int monthNumber = Integer.parseInt(orderMonth);
                            String status = snapshot.child("status").getValue().toString();
                            String orderTime = snapshot.child("orderTime").getValue().toString();
                            String address = snapshot.child("address").getValue().toString();
                            String maidPhoneNumber = snapshot.child("maidPhoneNumber").getValue().toString();
                            ServiceSchedule serviceSchedule = new ServiceSchedule(orderDate, serviceType, maid,
                                    new DateFormatSymbols().getMonths()[monthNumber-1], status, orderTime, address, maidPhoneNumber);
                            serviceSchedule.setOrderID(snapshot.getKey());
                            serviceScheduleList.add(serviceSchedule);
                        }
                    }
                }
                retrieveRentData();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void checkData(){
        if (serviceScheduleList.isEmpty()){
            noDataLayout.setVisibility(View.VISIBLE);
            historyRV.setVisibility(View.GONE);
        }
        else {
            noDataLayout.setVisibility(View.GONE);
            historyRV.setVisibility(View.VISIBLE);
            Collections.reverse(serviceScheduleList);
            historyRecyclerViewAdapter.notifyDataSetChanged();
        }
    }

    private void addDummyData() {
        ServiceSchedule dummy1 = new ServiceSchedule("10","Cuci Setrika Baju","Ningsih","Jan","Berlangsung","10.00-14.00","Jl Margonda Raya Blok NF9 N0.2", "01239139123");
        ServiceSchedule dummy2 = new ServiceSchedule("12","Membersihkan Kamar Mandi","Inem","Feb","Akan Datang","10.00-14.00","Jl Margonda Raya Blok NF9 N0.2", "10239123");
        ServiceSchedule dummy3 = new ServiceSchedule("9","Setrika Baju","Purwati","Feb","Akan Datang","10.00-14.00","Jl Margonda Raya Blok NF9 N0.2", "10239193");
        ServiceSchedule dummy4 = new ServiceSchedule("2","Menyapu dan Mengepel","Angel","Mar","Akan Datang","10.00-14.00","Jl Margonda Raya Blok NF9 N0.2", "102391239");

        serviceScheduleList.add(dummy1);
        serviceScheduleList.add(dummy2);
        serviceScheduleList.add(dummy3);
        serviceScheduleList.add(dummy4);
    }

    private void setupRecyclerView() {
        historyRecyclerViewAdapter = new HistoryRecyclerViewAdapter(getContext(), serviceScheduleList, this);
        historyLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);

        historyRV.setLayoutManager(historyLayoutManager);
        historyRV.setAdapter(historyRecyclerViewAdapter);
    }

    @Override
    public void onItemClick(int position) {
        Intent moveToDetail;
        if(serviceScheduleList.get(position).getServiceType().equals("Bantoo Bulanan")) {
            moveToDetail = new Intent(getContext(), DetailMonthlyConfirmationActivity.class);
            moveToDetail.putExtra("orderUniqueKey", serviceScheduleList.get(position).getOrderID());
            moveToDetail.putExtra("maidUniqueKey", maidIDList.get(position));
            moveToDetail.putExtra("sender", "orderFragment");
        } else {
            if(serviceScheduleList.get(position).getMaid().equals("maid")) {
                moveToDetail = new Intent(getContext(), SearchingDailyMaidActivity.class);
                moveToDetail.putExtra("sender", "orders");
                moveToDetail.putExtra("orderUniqueKey", serviceScheduleList.get(position).getOrderID());
            } else {
                moveToDetail = new Intent(getContext(), DetailDailyConfirmationActivity.class);
                moveToDetail.putExtra("orderUniqueKey", serviceScheduleList.get(position).getOrderID());
                moveToDetail.putExtra("sender", "orderFragment");
            }
        }
        startActivity(moveToDetail);
    }
}
