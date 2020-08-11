package com.bantoo.babooo.Pages.MaidPages.MaidHomePages.NewOrderPage;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.ImageView;
import android.widget.TextView;

import com.bantoo.babooo.Model.ServiceSchedule;
import com.bantoo.babooo.Model.DateOrder;
import com.bantoo.babooo.Pages.LoginPage.LoginActivity;
import com.bantoo.babooo.Pages.MaidPages.MaidDailyDetailOrderActivity;
import com.bantoo.babooo.Pages.SignUpPage.SignUpRoleActivity;
import com.bantoo.babooo.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;

public class NewOrderFragment extends Fragment implements NewOrderClickListener, DateIncomingOrderClickListener, IncomingOrderClickListener {

    private static final String TAG = "PesananFragment";

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference orderReference, userReference;
    private RecyclerView pesananBaruRV, pesananAkanDatangTanggalRV, pesananAkanDatangListRV;
    private TextView noNewOrderTV, noNewFutureOrderTV;
    private ImageView leftIV, rightIV;
    private String phoneNumber;
    private List<ServiceSchedule> serviceSchedulesList = new ArrayList<>();
    private List<DateOrder> dateOrderList = new ArrayList<>();
    private List<String> bossNameList = new ArrayList<>();
    private List<ServiceSchedule> upcomingScheduleList = new ArrayList<>();
    private List<String> bossNameUpcomingList = new ArrayList<>();
    private NewOrderAdapter newOrderAdapter;
    private DateIncomingOrderAdapter dateIncomingOrderAdapter;
    private IncomingOrderListAdapter incomingOrderListAdapter;
    private LinearLayoutManager tanggalLayoutManager;
    private SharedPreferences accountDataSharedPreferences;
    private String artType;

    int currentDatePosition = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_pesanan, container, false);
        Log.d(TAG, "onCreateView: called");
        pesananBaruRV = rootView.findViewById(R.id.pesanan_baru_rv);
        pesananAkanDatangTanggalRV = rootView.findViewById(R.id.pesanan_datang_tanggal_RV);
        pesananAkanDatangListRV = rootView.findViewById(R.id.pesanan_datang_list_RV);
        noNewOrderTV = rootView.findViewById(R.id.no_new_order_tv);
        noNewFutureOrderTV = rootView.findViewById(R.id.no_future_order_tv);
        noNewFutureOrderTV.setVisibility(View.GONE);
        noNewOrderTV.setVisibility(View.GONE);
        leftIV = rootView.findViewById(R.id.left_tanggal_IV);
        rightIV = rootView.findViewById(R.id.right_tanggal_IV);
        firebaseInit();
        setRecyclerview();
        rightIV.setOnClickListener(v -> {
            if (tanggalLayoutManager.findLastCompletelyVisibleItemPosition() < dateOrderList.size() - 1) {
                int difference = dateOrderList.size() - 1 - tanggalLayoutManager.findLastCompletelyVisibleItemPosition();
                if (difference < 4) {
                    tanggalLayoutManager.scrollToPosition(tanggalLayoutManager.findLastVisibleItemPosition() + difference);
                } else {
                    tanggalLayoutManager.scrollToPosition(tanggalLayoutManager.findLastCompletelyVisibleItemPosition() + 4);
                }
            } else {
                Log.d(TAG, "onClick: no next data, lastvisible:  " + tanggalLayoutManager.findLastCompletelyVisibleItemPosition());
            }
        });
        leftIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tanggalLayoutManager.findFirstCompletelyVisibleItemPosition() > 0) {
                    if (tanggalLayoutManager.findFirstCompletelyVisibleItemPosition() < 4) {
                        tanggalLayoutManager.scrollToPosition(0);
                    } else {
                        tanggalLayoutManager.scrollToPosition(tanggalLayoutManager.findFirstCompletelyVisibleItemPosition() - 4);
                    }
                } else {
                    Log.d(TAG, "onClick: no prev data, first visible: " + tanggalLayoutManager.findFirstCompletelyVisibleItemPosition());
                }
            }
        });
        if(artType.equals("daily")) {
            retrieveNewOrder();
            retrieveDateOrder();
        } else if(artType.equals("monthly")) {
            retrieveMonthlyNewOrder();
            noNewFutureOrderTV.setVisibility(View.VISIBLE);
            pesananAkanDatangListRV.setVisibility(View.GONE);
        }

        return rootView;
    }

    private void retrieveMonthlyNewOrder() {
        bossNameList.clear();
        serviceSchedulesList.clear();
        orderReference.orderByChild("maidPhoneNumber").equalTo(phoneNumber).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        if (snapshot.child("accepted").getValue().toString().equals("none")) {
                            userReference.orderByChild("phoneNumber").equalTo(snapshot.child("phoneNumber").getValue().toString())
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                                                bossNameList.add(userSnapshot.child("name").getValue().toString());
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
                                                serviceSchedulesList.add(serviceSchedule);
                                                newOrderAdapter.notifyDataSetChanged();
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
                        }
                    }
                } else {
                    Log.d(TAG, "onDataChange: no new order");
                    pesananBaruRV.setVisibility(View.GONE);
                    noNewOrderTV.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void retrieveDateOrder() {
        dateOrderList.clear();
        orderReference.orderByChild("maidPhoneNumber").equalTo(phoneNumber).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
                        try {
                            Date orderDate = sdf.parse(snapshot.child("orderYear").getValue().toString()
                                    + "/" + snapshot.child("orderMonth").getValue().toString() + "/"
                                    + snapshot.child("orderDate").getValue().toString());
                            if (orderDate.after(new Date())) {
                                DateOrder dateOrder = new DateOrder(snapshot.child("orderDate").getValue().toString()
                                        , snapshot.child("orderMonth").getValue().toString());
                                dateOrderList.add(dateOrder);
                            }
                        } catch (Exception e) {
                        }
                    }
                    Collections.sort(dateOrderList, new Comparator<DateOrder>() {
                        @Override
                        public int compare(DateOrder o1, DateOrder o2) {
                            Date now = new Date();
                            DateFormat format = new SimpleDateFormat("dd MM yyyy", Locale.ENGLISH);
                            Date date1 = new Date(), date2 = new Date();
                            try {
                                date1 = format.parse(o1.getDateOrder() + " " + o1.getMonthOrder() + " " + (now.getYear() + 1900));
                                date2 = format.parse(o2.getDateOrder() + " " + o2.getMonthOrder() + " " + (now.getYear() + 1900));
                            } catch (Exception e) {}

                            return date1.compareTo(date2);
                        }
                    });
                    dateIncomingOrderAdapter.notifyDataSetChanged();
                    if (!dateOrderList.isEmpty()) {
                        retrieveIncomingOrderList();
                    }
                } else {
                    noNewFutureOrderTV.setVisibility(View.VISIBLE);
                    pesananAkanDatangListRV.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setRecyclerview() {
        //Pesanan baru RV
        newOrderAdapter = new NewOrderAdapter(serviceSchedulesList, NewOrderFragment.this, bossNameList);
        pesananBaruRV.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        pesananBaruRV.setAdapter(newOrderAdapter);

        //Pesanan Tanggal RV
        dateIncomingOrderAdapter = new DateIncomingOrderAdapter(dateOrderList, this);
        tanggalLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        pesananAkanDatangTanggalRV.setLayoutManager(tanggalLayoutManager);
        pesananAkanDatangTanggalRV.setAdapter(dateIncomingOrderAdapter);

        //Pesanan List RV
        incomingOrderListAdapter = new IncomingOrderListAdapter(upcomingScheduleList, bossNameUpcomingList, this);
        pesananAkanDatangListRV.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        pesananAkanDatangListRV.setAdapter(incomingOrderListAdapter);
    }

    private void firebaseInit() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        accountDataSharedPreferences = getActivity().getSharedPreferences("accountData", Context.MODE_PRIVATE);
        artType = accountDataSharedPreferences.getString("artType", "");
        if(artType.equals("daily")) {
            orderReference = firebaseDatabase.getReference().child("Order");
        } else if(artType.equals("monthly")) {
            orderReference = firebaseDatabase.getReference().child("Rent");
        }
        userReference = firebaseDatabase.getReference().child("Users");
        phoneNumber = accountDataSharedPreferences.getString("phoneNumber", "");
    }

    private void retrieveNewOrder() {
        Log.d(TAG, "retrieveNewOrder: called");
        orderReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshotOrder) {
                for (DataSnapshot snapshot : dataSnapshotOrder.getChildren()) {
                    Log.d(TAG, "onDataChange: datasnapshotorder check");
                    snapshot.child("maidList").getRef().orderByChild("maidPhoneNumber")
                            .equalTo(phoneNumber).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                userReference.orderByChild("phoneNumber").equalTo(snapshot.child("phoneNumber")
                                        .getValue().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot userDataSnapshot) {
                                        if(userDataSnapshot.exists()) {
                                            for (DataSnapshot userSnapshot : userDataSnapshot.getChildren()) {
                                                bossNameList.add(userSnapshot.child("name").getValue().toString());
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
                                                serviceSchedulesList.add(serviceSchedule);
                                                newOrderAdapter.notifyDataSetChanged();
                                            }
                                            if(serviceSchedulesList.isEmpty()) {
                                                Log.d(TAG, "onDataChange: no new order");
                                                pesananBaruRV.setVisibility(View.GONE);
                                                noNewOrderTV.setVisibility(View.VISIBLE);
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void retrieveIncomingOrderList() {
        bossNameUpcomingList.clear();
        upcomingScheduleList.clear();
        orderReference.orderByChild("maidPhoneNumber").equalTo(phoneNumber).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (snapshot.child("orderDate").getValue().
                            toString().equals(dateOrderList.get(currentDatePosition).getDateOrder()) &&
                            snapshot.child("orderMonth").getValue().toString().equals(dateOrderList.get(currentDatePosition).getMonthOrder())) {
                        userReference.orderByChild("phoneNumber").equalTo(snapshot.child("phoneNumber").getValue().toString())
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot userDataSnapshot) {
                                        for (DataSnapshot userSnapshot : userDataSnapshot.getChildren()) {
                                            bossNameUpcomingList.add(userSnapshot.child("name").getValue().toString());
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
                                            upcomingScheduleList.add(serviceSchedule);
                                            incomingOrderListAdapter.notifyDataSetChanged();
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

    @Override
    public void onAcceptClick(int position) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                getContext());
        alertDialogBuilder.setTitle("Apakah anda ingin menerima pesanan?");
        alertDialogBuilder
                .setMessage("Pesanan yang sudah diterima tidak bisa dibatalkan")
                .setCancelable(false)
                .setPositiveButton("Ya", (dialog, id) -> {
                    if(artType.equals("monthly")) {
                        orderReference.child(serviceSchedulesList.get(position).getOrderID()).child("accepted").setValue("Accepted");
                        orderReference.orderByChild("maidPhoneNumber").equalTo(phoneNumber).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for(DataSnapshot snapshot: dataSnapshot.getChildren()) {
                                    if(!snapshot.getKey().equals(serviceSchedulesList.get(position).getOrderID())) {
                                        snapshot.child("accepted").getRef().setValue("Rejected");
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    } else {
                        orderReference.child(serviceSchedulesList.get(position).getOrderID()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.child("maid").getValue().toString().equals("maid")) {
                                    SharedPreferences sharedPreferences = getActivity().getSharedPreferences("accountData", MODE_PRIVATE);
                                    String maidName = sharedPreferences.getString("maidName", "");
                                    dataSnapshot.child("maid").getRef().setValue(maidName);
                                    dataSnapshot.child("maidPhoneNumber").getRef().setValue(phoneNumber);
                                    dataSnapshot.child("maidList").getRef().removeValue();
                                    serviceSchedulesList.remove(position);
                                    newOrderAdapter.notifyDataSetChanged();
                                } else {
                                    Log.d("PesananFragment", "onDataChange: order has been taken");
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                })
                .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    public void onRejectClick(int position) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                getContext());
        alertDialogBuilder.setTitle("Apakah anda ingin menolak pesanan?");
        alertDialogBuilder
                .setMessage("Pesanan yang sudah ditolak tidak bisa diterima kembali")
                .setCancelable(false)
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if(artType.equals("monthly")) {
                            orderReference.child(serviceSchedulesList.get(position).getOrderID()).child("accepted").setValue("Rejected");
                        } else {
                            orderReference.child(serviceSchedulesList.get(position).getOrderID())
                                    .child("maidList").orderByChild("maidPhoneNumber").equalTo(phoneNumber)
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                snapshot.getRef().removeValue();
                                                serviceSchedulesList.remove(position);
                                                newOrderAdapter.notifyDataSetChanged();
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
                        }
                    }
                })
                .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

    @Override
    public void onDateClick(int position) {
        currentDatePosition = position;
        Log.d("PesananFragment", "onTanggalClick: tanggalPosition: " + position);
        retrieveIncomingOrderList();
    }

    @Override
    public void onIncomingOrderClick(int position) {
        Intent intent = new Intent(getContext(), MaidDailyDetailOrderActivity.class);
        intent.putExtra("orderKey", upcomingScheduleList.get(position).getOrderID());
        startActivity(intent);
    }
}
