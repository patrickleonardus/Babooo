package com.bantoo.babooo.Pages.MaidPages.MaidHomePages.NewOrderPage;

import android.content.Context;
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

import com.bantoo.babooo.Model.ServiceSchedule;
import com.bantoo.babooo.Model.TanggalPesanan;
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

import static android.content.Context.MODE_PRIVATE;

public class NewOrderFragment extends Fragment implements NewOrderClickListener, DateIncomingOrderClickListener {

    private static final String TAG = "PesananFragment";

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference orderReference, userReference;
    private RecyclerView pesananBaruRV, pesananAkanDatangTanggalRV, pesananAkanDatangListRV;
    private ImageView leftIV, rightIV;
    private String phoneNumber;
    private List<ServiceSchedule> serviceSchedulesList = new ArrayList<>();
    private List<TanggalPesanan> tanggalPesananList = new ArrayList<>();
    private List<String> bossNameList = new ArrayList<>();
    private List<ServiceSchedule> upcomingScheduleList = new ArrayList<>();
    private List<String> bossNameUpcomingList = new ArrayList<>();
    private NewOrderAdapter newOrderAdapter;
    private DateIncomingOrderAdapter dateIncomingOrderAdapter;
    private IncomingOrderListAdapter incomingOrderListAdapter;
    private LinearLayoutManager tanggalLayoutManager;

    int currentDatePosition = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_pesanan, container, false);
        pesananBaruRV = rootView.findViewById(R.id.pesanan_baru_rv);
        pesananAkanDatangTanggalRV = rootView.findViewById(R.id.pesanan_datang_tanggal_RV);
        pesananAkanDatangListRV = rootView.findViewById(R.id.pesanan_datang_list_RV);
        leftIV = rootView.findViewById(R.id.left_tanggal_IV);
        rightIV = rootView.findViewById(R.id.right_tanggal_IV);
        firebaseInit();
        setRecyclerview();
        rightIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tanggalLayoutManager.findLastCompletelyVisibleItemPosition() < tanggalPesananList.size() - 1) {
                    int difference = tanggalPesananList.size() - 1 - tanggalLayoutManager.findLastCompletelyVisibleItemPosition();
                    if (difference < 4) {
                        tanggalLayoutManager.scrollToPosition(tanggalLayoutManager.findLastVisibleItemPosition() + difference);
                    } else {
                        tanggalLayoutManager.scrollToPosition(tanggalLayoutManager.findLastCompletelyVisibleItemPosition() + 4);
                    }
                } else {
                    Log.d(TAG, "onClick: no next data, lastvisible:  "+tanggalLayoutManager.findLastCompletelyVisibleItemPosition());
                }
            }
        });
        leftIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tanggalLayoutManager.findFirstCompletelyVisibleItemPosition() > 0) {
                    if(tanggalLayoutManager.findFirstCompletelyVisibleItemPosition() < 4) {
                        tanggalLayoutManager.scrollToPosition(0);
                    } else {
                        tanggalLayoutManager.scrollToPosition(tanggalLayoutManager.findFirstCompletelyVisibleItemPosition() - 4);
                    }
                } else {
                    Log.d(TAG, "onClick: no prev data, first visible: "+ tanggalLayoutManager.findFirstCompletelyVisibleItemPosition());
                }
            }
        });
        retrieveNewOrder();
        retrieveTanggalOrder();

        return rootView;
    }

    private void retrieveTanggalOrder() {
        tanggalPesananList.clear();
        bossNameList.clear();
        orderReference.orderByChild("maidPhoneNumber").equalTo(phoneNumber).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
                    try {
                        Date orderDate = sdf.parse(snapshot.child("orderYear").getValue().toString()
                                + "/" + snapshot.child("orderMonth").getValue().toString() + "/"
                                + snapshot.child("orderDate").getValue().toString());
                        if (orderDate.after(new Date())) {
                            TanggalPesanan tanggalPesanan = new TanggalPesanan(snapshot.child("orderDate").getValue().toString()
                                    , snapshot.child("orderMonth").getValue().toString());
                            tanggalPesananList.add(tanggalPesanan);
                            dateIncomingOrderAdapter.notifyDataSetChanged();
                        }
                    } catch (Exception e) {}
                }
                if(!tanggalPesananList.isEmpty()) {
                    retrieveIncomingOrderList();
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
        pesananBaruRV.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
        pesananBaruRV.setAdapter(newOrderAdapter);

        //Pesanan Tanggal RV
        dateIncomingOrderAdapter = new DateIncomingOrderAdapter(tanggalPesananList, this);
        tanggalLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        pesananAkanDatangTanggalRV.setLayoutManager(tanggalLayoutManager);
        pesananAkanDatangTanggalRV.setAdapter(dateIncomingOrderAdapter);

        //Pesanan List RV
        incomingOrderListAdapter = new IncomingOrderListAdapter(upcomingScheduleList, bossNameUpcomingList);
        pesananAkanDatangListRV.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        pesananAkanDatangListRV.setAdapter(incomingOrderListAdapter);
    }

    private void firebaseInit() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        orderReference = firebaseDatabase.getReference().child("Order");
        userReference = firebaseDatabase.getReference().child("Users");
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("accountData", Context.MODE_PRIVATE);
        phoneNumber = sharedPreferences.getString("phoneNumber", "");
    }

    private void retrieveNewOrder() {
        orderReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshotOrder) {
                for(DataSnapshot snapshot: dataSnapshotOrder.getChildren()) {
                    snapshot.child("maidList").getRef().orderByChild("maidPhoneNumber")
                            .equalTo(phoneNumber).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()) {
                                userReference.orderByChild("phoneNumber").equalTo(snapshot.child("phoneNumber")
                                        .getValue().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot userDataSnapshot) {
                                        for(DataSnapshot userSnapshot: userDataSnapshot.getChildren()) {
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
                for(DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    if(snapshot.child("orderDate").getValue().
                            toString().equals(tanggalPesananList.get(currentDatePosition).getTanggal()) &&
                    snapshot.child("orderMonth").getValue().toString().equals(tanggalPesananList.get(currentDatePosition).getBulan())) {
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
        orderReference.child(serviceSchedulesList.get(position).getOrderID()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child("maid").getValue().toString().equals("maid")) {
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

    @Override
    public void onRejectClick(int position) {
        orderReference.child(serviceSchedulesList.get(position).getOrderID())
                .child("maidList").orderByChild("maidPhoneNumber").equalTo(phoneNumber)
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot: dataSnapshot.getChildren()) {
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

    @Override
    public void onTanggalClick(int position) {
        currentDatePosition = position;
        Log.d("PesananFragment", "onTanggalClick: tanggalPosition: "+position);
        retrieveIncomingOrderList();
    }
}
