package com.bantoo.babooo.Pages.HomePage.ServicePage;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bantoo.babooo.Model.ServiceSchedule;
import com.bantoo.babooo.Pages.DailyServicePage.DailyServiceActivity;
import com.bantoo.babooo.Pages.DailyServicePage.DetailDailyConfirmationPage.DetailDailyConfirmationActivity;
import com.bantoo.babooo.Pages.HomePage.HomeActivity;
import com.bantoo.babooo.Pages.MonthlyServicePage.MonthlyMaidActivity;
import com.bantoo.babooo.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.takusemba.multisnaprecyclerview.MultiSnapRecyclerView;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class ServiceFragment extends Fragment implements ServiceItemClickListener{

    private static final String TAG = "ServiceFragment";

    //FIREBASE INIT
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference userReference;
    private Query orderReference;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private String uid;
    private String phoneNumber;

    SharedPreferences accountDataSharedPreferences;

    //Recycler View
    private MultiSnapRecyclerView scheduleRV;
    private LinearLayoutManager scheduleLayoutManager;
    private ServiceRecyclerViewAdapter serviceRecyclerViewAdapter;

    //Array of Service
    private List<ServiceSchedule> serviceScheduleList = new ArrayList<ServiceSchedule>();
    private String username;
    private int coins;

    //Object View
    LinearLayout dailyServiceOption,monthlyServiceOption,topUpOption;
    TextView usernameTV,coinsTV;
    ProgressBar usernamePB,coinsPB;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home_service, container, false);

        scheduleRV = rootView.findViewById(R.id.scheduleRV);
        dailyServiceOption = rootView.findViewById(R.id.dailyServiceLayout);
        monthlyServiceOption = rootView.findViewById(R.id.monthlyServiceLayout);
        usernameTV = rootView.findViewById(R.id.username_home_tv);
        coinsTV = rootView.findViewById(R.id.coins_home_TV);
        topUpOption = rootView.findViewById(R.id.topUp_home_layout);
        usernamePB = rootView.findViewById(R.id.username_home_PB);
        coinsPB = rootView.findViewById(R.id.coins_home_PB);

        showUserProgressBar(true);
        showCoinProgressBar(true);
        retrieveScheduleData();
        //dummyData();
        menuHandler();
        loadUserData();
        checkUserToken();

        return rootView;
    }

    private void checkUserToken() {
        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
            @Override
            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                if (!task.isSuccessful()) {
                    Log.w(TAG, "onComplete: ", task.getException());
                    return;
                }
                String token = task.getResult().getToken();
                Log.d(TAG, "checkUserToken: "+token);
                userReference.child("token").setValue(token);
            }
        });
    }

    private void retrieveScheduleData() {
        serviceScheduleList.clear();
        accountDataSharedPreferences = getActivity().getSharedPreferences("accountData", MODE_PRIVATE);
        phoneNumber = accountDataSharedPreferences.getString("phoneNumber", "");
        firebaseDatabase = FirebaseDatabase.getInstance();
        orderReference = firebaseDatabase.getReference().child("Order").orderByChild("phoneNumber").equalTo(phoneNumber);
        orderReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    String orderDate = snapshot.child("orderDate").getValue().toString();
                    String serviceType = snapshot.child("serviceType").getValue().toString();
                    String maid = snapshot.child("maid").getValue().toString();
                    String orderMonth = snapshot.child("orderMonth").getValue().toString();
                    String status = snapshot.child("status").getValue().toString();
                    String orderTime = snapshot.child("orderTime").getValue().toString();
                    String address = snapshot.child("address").getValue().toString();
                    String maidPhoneNumber = snapshot.child("maidPhoneNumber").getValue().toString();
                    ServiceSchedule serviceSchedule = new ServiceSchedule(orderDate, serviceType, maid, orderMonth, status, orderTime, address, maidPhoneNumber);
                    String orderID = snapshot.getKey();
                    serviceSchedule.setOrderID(orderID);
                    serviceScheduleList.add(serviceSchedule);
                }
                setupScheduleView();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void dummyData(){

        serviceScheduleList.clear();

        /*
        ServiceSchedule dummy1 = new ServiceSchedule("10","Cuci Setrika Baju","Ningsih","Jan","Berlangsung","10.00-14.00","Jl Margonda Raya Blok NF9 N0.2");
        ServiceSchedule dummy2 = new ServiceSchedule("12","Membersihkan Kamar Mandi","Inem","Feb","Akan Datang","10.00-14.00","Jl Margonda Raya Blok NF9 N0.2");
        ServiceSchedule dummy3 = new ServiceSchedule("9","Setrika Baju","Purwati","Feb","Akan Datang","10.00-14.00","Jl Margonda Raya Blok NF9 N0.2");
        ServiceSchedule dummy4 = new ServiceSchedule("2","Menyapu dan Mengepel","Angel","Mar","Akan Datang","10.00-14.00","Jl Margonda Raya Blok NF9 N0.2");


        serviceScheduleList.add(dummy1);
        serviceScheduleList.add(dummy2);
        serviceScheduleList.add(dummy3);
        serviceScheduleList.add(dummy4);*/

    }

    //SETUP RECYCLER VIEW (JADWAL PESANAN)
    //Code below is only for recyclerview handler
    private void setupScheduleView(){
        serviceRecyclerViewAdapter = new ServiceRecyclerViewAdapter(getContext(),serviceScheduleList,this);
        scheduleLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);

        scheduleRV.setLayoutManager(scheduleLayoutManager);
        scheduleRV.setAdapter(serviceRecyclerViewAdapter);
    }

    @Override
    public void onItemClick(int position) {
        Toast.makeText(getContext(),serviceScheduleList.get(position).getServiceType(),Toast.LENGTH_SHORT).show();
        Intent moveToDetailPage = new Intent(getContext(), DetailDailyConfirmationActivity.class);
        moveToDetailPage.putExtra("orderUniqueKey", serviceScheduleList.get(position).getOrderID());
        startActivity(moveToDetailPage);
    }

    //SETUP RECYCLER VIEW (JADWAL PESANAN)
    //Code above is only for recyclerview hanlder

    private void menuHandler(){
        topUpOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"Top Up Menu",Toast.LENGTH_SHORT).show();
            }
        });

        dailyServiceOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveToDailyService();
            }
        });

        monthlyServiceOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               moveToMontlhyMaid();
            }
        });
    }

    private void trimName(String name){
        if (name.contains(" ")){
            name = name.substring(0, name.indexOf(" "));
            usernameTV.setText("Halo, " + name);
        }
        else {
            name = name.substring(0,10);
            usernameTV.setText("Halo, " + name);
        }
    }

    private void showUserProgressBar(boolean show){
        if(show){
            usernamePB.setVisibility(View.VISIBLE);
        }
        else {
            usernamePB.setVisibility(View.INVISIBLE);
        }
    }

    private void showCoinProgressBar(boolean show){
        if(show){
            coinsPB.setVisibility(View.VISIBLE);
            coinsTV.setVisibility(View.GONE);
        }
        else {
            coinsPB.setVisibility(View.INVISIBLE);
            coinsTV.setVisibility(View.VISIBLE);
        }
    }

    private void moveToMontlhyMaid(){
        Intent intent = new Intent(getContext(), MonthlyMaidActivity.class);
        startActivity(intent);
    }

    private void moveToDailyService(){
        Intent intent = new Intent(getContext(), DailyServiceActivity.class);
        startActivity(intent);
    }

    //FIREBASE HANDLER
    //BELOW CODE ARE ONLY FOR DATABASE HANDLER

    private void loadUserData(){

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        accountDataSharedPreferences = getActivity().getSharedPreferences("accountData", MODE_PRIVATE);
        String text = accountDataSharedPreferences.getString("uid", "");

        if(text=="") {
            uid = mUser.getUid();
            SharedPreferences accountData = getActivity().getSharedPreferences("accountData", MODE_PRIVATE);
            SharedPreferences.Editor editor = accountData.edit();
            editor.putString("uid", uid);
            editor.apply();
        } else {
            uid = text;
        }
        firebaseDatabase = FirebaseDatabase.getInstance();
        userReference = firebaseDatabase.getReference("Users").child(uid);

        userReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                username = dataSnapshot.child("name").getValue().toString();
                if (dataSnapshot.child("coins").getValue() != null) {
                    coins = Integer.parseInt(dataSnapshot.child("coins").getValue().toString());
                } else { coins = 0; }
                coinsTV.setText(coins+" Coins");
                showCoinProgressBar(false);
                trimName(username);
                showUserProgressBar(false);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                showUserProgressBar(false);
            }
        });
    }

    //FIREBASE HANDLER
    //ABOVE CODE ARE ONLY FOR DATABASE HANDLER
}
