package com.bantoo.babooo.Pages.MonthlyServicePage;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bantoo.babooo.Model.FilterSearch;
import com.bantoo.babooo.Model.Maid;
import com.bantoo.babooo.Pages.MonthlyServicePage.FilterPage.FilterActivity;
import com.bantoo.babooo.Pages.MonthlyServicePage.SortPage.SortMaidActivity;
import com.bantoo.babooo.Pages.UserDetailPage.MaidDetailActivity;
import com.bantoo.babooo.R;
import com.bantoo.babooo.Utilities.BaseActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MonthlyMaidActivity extends BaseActivity implements Serializable {

    //FIREBASE
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference monthlyMaidReference;

    static final int REQUEST_FILTER = 1;
    static final int REQUEST_SORT = 2;
    static final String FILTERINTENT = "filterIntent";

    private String role = "mitra";
    private String currentCity = "";
    private List<Maid> maidList = new ArrayList<Maid>();

    //LIST DIBAWAH INI DIJADIKAN DASAR UNTUK FILTER PENCARIAN MAID
    private List<FilterSearch> filterSearches = new ArrayList<FilterSearch>();

    GridView maidGV;
    private MonthlyMaidGridViewAdapter adapter;
    EditText searchMaidET;
    LinearLayout sortOptionLayout, filterOptionLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monthly_maid);

        maidGV = findViewById(R.id.maid_GV);
        searchMaidET = findViewById(R.id.search_maid_ET);
        sortOptionLayout = findViewById(R.id.sort_maid_layout);
        filterOptionLayout = findViewById(R.id.filter_maid_ET);

        getCurrentCity();
        //dummyData();
    }

    private void getCurrentCity() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        SharedPreferences accountDataSharedPreferences = getSharedPreferences("accountData", MODE_PRIVATE);
        String uid = accountDataSharedPreferences.getString("uid", "");
        DatabaseReference userReference = firebaseDatabase.getReference().child("Users").child(uid);
        userReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child("address").getValue() != null) {
                    currentCity = dataSnapshot.child("address").getValue().toString();
                    if(currentCity.equalsIgnoreCase("jakarta") ||
                        currentCity.equalsIgnoreCase("bogor") || currentCity.equalsIgnoreCase("depok") ||
                        currentCity.equalsIgnoreCase("tangerang") ||
                        currentCity.equalsIgnoreCase("bekasi")) {
                        currentCity = "jabodetabek";
                    }
                }
                handleSearchET();
                handleButton();
                receiveMonthlyMaidData();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_FILTER){
            if(resultCode == RESULT_OK){
                filterSearches = (ArrayList<FilterSearch>) data.getSerializableExtra(FILTERINTENT);
                applyFilter();
            }
        } else if(requestCode == REQUEST_SORT) {
            if(resultCode == RESULT_OK) {
                String sortBy = data.getStringExtra("sortBy");
                sortMaid(sortBy);
            }
        }
    }

    private void applyFilter() {
        Log.d("MonthlyMaidActivity", "applyFilter: minCost: "+filterSearches.get(0).getMinCost()+", maxCost: "+filterSearches.get(0).getMaxCost());
        Log.d("MonthlyMaidActivity", "applyFilter: maidlistsize: "+maidList.size());
        for(int i = maidList.size() - 1; i >= 0; i--) {
            Log.d("MonthlyMaidActivity", "applyFilter: "+i);
            Log.d("MonthlyMaidActivity", "applyFilter: maid: "+maidList.get(i).name+"\n maidRating: "+maidList.get(i).getPopularity()+", filterRating: "+filterSearches.get(0).getPopularity());
            String dateOfBirth = maidList.get(i).getDateOfBirth();
            int bornYears = 0;
            try {
                bornYears = Integer.parseInt(dateOfBirth.substring(dateOfBirth.length() - 4));
            } catch (Exception e) {
                bornYears = 0;
                Log.d("MonthlyMaidActivity", "applyFilter: parsing int failed, maid: "+maidList.get(i).name);
            }
            //check cost
            if(maidList.get(i).getSalary() < Integer.parseInt(filterSearches.get(0).getMinCost())
                    || maidList.get(i).getSalary() > Integer.parseInt(filterSearches.get(0).getMaxCost())) {
                Log.d("MonthlyMaidActivity", "applyFilter: maid: "+maidList.get(i).name+" cost remove, cost: "+maidList.get(i).getSalary());
                maidList.remove(i);
            } else if(maidList.get(i).getExperience() < Integer.parseInt(filterSearches.get(0).getMinYears())
                    || maidList.get(i).getExperience() > Integer.parseInt(filterSearches.get(0).getMaxYears())) {
                //check experience
                Log.d("MonthlyMaidActivity", "applyFilter: maid: "+maidList.get(i).name+" exp remove");
                maidList.remove(i);
            } else if(bornYears < Integer.parseInt(filterSearches.get(0).getMinAge())
                    || bornYears > Integer.parseInt(filterSearches.get(0).getMaxAge())) {
                //check age
                Log.d("MonthlyMaidActivity", "applyFilter: maid: "+maidList.get(i).name+" age remove");
                maidList.remove(i);
            } else if(maidList.get(i).getPopularity() != filterSearches.get(0).getPopularity()) {
                //check popularity
                Log.d("MonthlyMaidActivity", "applyFilter: maid: "+maidList.get(i).name+" rating remove");
                maidList.remove(i);
            }
        }

        adapter.notifyDataSetChanged();
    }

    private void sortMaid(String by) {
        if(by.equals("salaryAscending")) {
            Collections.sort(maidList, Maid.salaryAscending);
        } else if (by.equals("salaryDescending")) {
            Collections.sort(maidList, Maid.salaryDescending);
        }
        adapter.notifyDataSetChanged();
    }

    //GRID VIEW HANDLER
    private void setupGridView() {
        adapter = new MonthlyMaidGridViewAdapter(MonthlyMaidActivity.this, maidList);
        maidGV.setAdapter(adapter);
    }

    private void handleGridViewAction() {
        maidGV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                moveToUserPage(maidList.get(position).getMaidUniqueKey());
            }
        });
    }

    private void receiveMonthlyMaidData() {
        maidList.clear();
        monthlyMaidReference = firebaseDatabase.getReference().child("ARTBulanan");
        monthlyMaidReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    if (snapshot.child("working").getValue() != null && snapshot.child("activate").getValue() != null) {
                        if (snapshot.child("working").getValue().toString() == "false" && snapshot.child("activate").getValue().toString() == "true") {
                            String name = snapshot.child("name").getValue().toString();
                            String email = snapshot.child("email").getValue().toString();
                            String phoneNumber = snapshot.child("phoneNumber").getValue().toString();
                            String address = snapshot.child("address").getValue().toString();
                            int cost = Integer.parseInt(snapshot.child("cost").getValue().toString());
                            String bornYears = "";
                            if (snapshot.child("ttl").getValue() != null) {
                                bornYears = snapshot.child("ttl").getValue().toString();
                            }
                            int rating = Integer.parseInt(snapshot.child("rating").getValue().toString());
                            int salary = Integer.parseInt(snapshot.child("salary").getValue().toString());
                            Maid maid = new Maid(role, name, email, phoneNumber, "-", address, "-", bornYears, cost, rating);
                            maid.setMaidUniqueKey(snapshot.getKey());
                            if (snapshot.child("photoUrl").getValue() != null) {
                                maid.setPhotoUrl(snapshot.child("photoUrl").getValue().toString());
                            }
                            if (snapshot.child("experience").getValue() != null) {
                                try {
                                    maid.setExperience(Integer.parseInt(snapshot.child("experience").getValue().toString()));
                                } catch (Exception e) {
                                    maid.setExperience(0);
                                }
                            } else {
                                maid.setExperience(0);
                            }
                            maid.setSalary(salary);
                            if (snapshot.child("rating").getValue() != null) {
                                maid.setPopularity(Integer.parseInt(snapshot.child("rating").getValue().toString()));
                            } else {
                                maid.setPopularity(0);
                            }
                            if(snapshot.child("cityPreference").getValue() != null) {
                                if(snapshot.child("cityPreference").getValue().toString().equals(currentCity)) {
                                    maidList.add(maid);
                                }
                            }
                        }
                    }
                }
                Collections.reverse(maidList);
                setupGridView();
                handleGridViewAction();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    /*public void dummyData() {
        Maid dummy1 = new Maid(role, "Sadikin", "sadikin@gmail", "08131888888", "-", "Bogor", "-", 1571489529, 1000, 3);
        Maid dummy2 = new Maid(role, "Munir", "sadikin@gmail", "08131888888", "-", "Bogor", "-", 1571489529, 500, 2);
        Maid dummy3 = new Maid(role, "Hasan", "sadikin@gmail", "08131888888", "-", "Bogor", "-", 1571489529, 1000, 5);
        Maid dummy4 = new Maid(role, "Joni", "sadikin@gmail", "08131888888", "-", "Bogor", "-", 1571489529, 1000, 5);
        Maid dummy5 = new Maid(role, "Beben", "sadikin@gmail", "08131888888", "-", "Bogor", "-", 1571489529, 1000, 5);
        Maid dummy6 = new Maid(role, "Kiki", "sadikin@gmail", "08131888888", "-", "Bogor", "-", 1571489529, 1000, 5);
        Maid dummy7 = new Maid(role, "sutopo", "sadikin@gmail", "08131888888", "-", "Bogor", "-", 1571489529, 1000, 5);

        maidList.add(dummy1);
        maidList.add(dummy2);
        maidList.add(dummy3);
        maidList.add(dummy4);
        maidList.add(dummy5);
        maidList.add(dummy6);
        maidList.add(dummy7);
    }*/

    @SuppressLint("ClickableViewAccessibility")
    private void handleSearchET() {
        searchMaidET.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_RIGHT = 2;
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (searchMaidET.getRight() - searchMaidET.getLeft() - searchMaidET.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {

                        Toast.makeText(getApplicationContext(), "Mencari" + searchMaidET.getText().toString(), Toast.LENGTH_SHORT).show();

                        return true;
                    }
                }
                return false;
            }
        });
        searchMaidET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {searchMaid();}

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void searchMaid() {
        if(searchMaidET.getText().toString().equals("")){
            receiveMonthlyMaidData();
        } else {
            for (int i = 0; i < maidList.size(); i++) {
                if(!maidList.get(i).name.toLowerCase().contains(searchMaidET.getText().toString().toLowerCase())) {
                    maidList.remove(i);
                }

            }
            adapter.notifyDataSetChanged();
        }
    }

    private void handleButton() {
        sortOptionLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveToSortPage();
            }
        });

        filterOptionLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveToFilterPage();
            }
        });
    }

    private void moveToSortPage() {
        Intent intent = new Intent(this, SortMaidActivity.class);
        startActivityForResult(intent, REQUEST_SORT);
    }

    private void moveToFilterPage() {
        Intent intent = new Intent(this, FilterActivity.class);
        startActivityForResult(intent, REQUEST_FILTER);
    }

    private void moveToUserPage(String uniqueKey){
        Intent intent = new Intent(this, MaidDetailActivity.class);
        intent.putExtra("maidUniqueKey", uniqueKey);
        startActivity(intent);
    }
}
