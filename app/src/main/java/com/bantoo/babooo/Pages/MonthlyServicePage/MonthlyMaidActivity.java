package com.bantoo.babooo.Pages.MonthlyServicePage;

import android.annotation.SuppressLint;
import android.content.Intent;
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

        handleSearchET();
        handleButton();
        receiveMonthlyMaidData();
        //dummyData();
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
        for(int i = 0; i < maidList.size(); i++) {
            //check cost
            if(maidList.get(i).getCost() < Integer.parseInt(filterSearches.get(0).getMinCost())
                    || maidList.get(i).getCost() > Integer.parseInt(filterSearches.get(0).getMaxCost())) {
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
        firebaseDatabase = FirebaseDatabase.getInstance();
        monthlyMaidReference = firebaseDatabase.getReference().child("ARTBulanan");
        monthlyMaidReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    if (snapshot.child("working").getValue().toString() == "false" && snapshot.child("activate").getValue().toString() == "true") {
                        String name = snapshot.child("name").getValue().toString();
                        String email = snapshot.child("email").getValue().toString();
                        String phoneNumber = snapshot.child("phoneNumber").getValue().toString();
                        String address = snapshot.child("address").getValue().toString();
                        int cost = Integer.parseInt(snapshot.child("cost").getValue().toString());
                        int rating = Integer.parseInt(snapshot.child("rating").getValue().toString());
                        int salary = Integer.parseInt(snapshot.child("salary").getValue().toString());
                        Maid maid = new Maid(role, name, email, phoneNumber, "-", address, "-", 1571489529, cost, rating);
                        maid.setMaidUniqueKey(snapshot.getKey());
                        maid.setSalary(salary);
                        maidList.add(maid);
                    }
                }
                setupGridView();
                handleGridViewAction();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void dummyData() {
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
    }

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
                if(!maidList.get(i).name.contains(searchMaidET.getText().toString())) {
                    maidList.remove(i);
                }
                /*for(int j = 0; j < searchMaidET.getText().toString().length(); j++) {

                }*/
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
