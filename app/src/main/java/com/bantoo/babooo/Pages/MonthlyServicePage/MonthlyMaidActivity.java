package com.bantoo.babooo.Pages.MonthlyServicePage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bantoo.babooo.Model.Maid;
import com.bantoo.babooo.Pages.MonthlyServicePage.FilterPage.FilterActivity;
import com.bantoo.babooo.Pages.MonthlyServicePage.SortPage.SortMaidActivity;
import com.bantoo.babooo.R;

import java.util.ArrayList;
import java.util.List;

public class MonthlyMaidActivity extends AppCompatActivity{

    private String role = "mitra";
    private List<Maid> maidList = new ArrayList<Maid>();

    GridView maidGV;
    EditText searchMaidET;
    LinearLayout sortOption,filterOption;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monthly_maid);

        maidGV = findViewById(R.id.maid_GV);
        searchMaidET = findViewById(R.id.search_maid_ET);
        sortOption = findViewById(R.id.sort_maid_layout);
        filterOption = findViewById(R.id.filter_maid_ET);

        generalStyling();
        handleSearchET();
        handleButton();
        dummyData();
        setupGridView();
        handleGridViewAction();
    }

    //GRID VIEW HANDLER
    private void setupGridView(){
        MonthlyMaidGridViewAdapter adapter = new MonthlyMaidGridViewAdapter(MonthlyMaidActivity.this,maidList);
        maidGV.setAdapter(adapter);
    }

    private void handleGridViewAction(){
        maidGV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(),maidList.get(position).name,Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void dummyData(){

        Maid dummy1 = new Maid(role,"Sadikin","sadikin@gmail","08131888888","-","Bogor","-", 1571489529,1000,3);
        Maid dummy2 = new Maid(role,"Munir","sadikin@gmail","08131888888","-","Bogor","-",1571489529,500,2);
        Maid dummy3 = new Maid(role,"Hasan","sadikin@gmail","08131888888","-","Bogor","-",1571489529,1000,5);
        Maid dummy4 = new Maid(role,"Joni","sadikin@gmail","08131888888","-","Bogor","-",1571489529,1000,5);
        Maid dummy5 = new Maid(role,"Beben","sadikin@gmail","08131888888","-","Bogor","-",1571489529,1000,5);
        Maid dummy6 = new Maid(role,"Kiki","sadikin@gmail","08131888888","-","Bogor","-",1571489529,1000,5);
        Maid dummy7 = new Maid(role,"sutopo","sadikin@gmail","08131888888","-","Bogor","-",1571489529,1000,5);

        maidList.add(dummy1);
        maidList.add(dummy2);
        maidList.add(dummy3);
        maidList.add(dummy4);
        maidList.add(dummy5);
        maidList.add(dummy6);
        maidList.add(dummy7);
    }

    public void generalStyling(){
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(getApplicationContext(),R.color.orangePrimary));
        window.setNavigationBarColor(ContextCompat.getColor(getApplicationContext(),R.color.greenPrimary));
    }

    @SuppressLint("ClickableViewAccessibility")
    private void handleSearchET(){
        searchMaidET.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_RIGHT = 2;
                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= (searchMaidET.getRight() - searchMaidET.getLeft() - searchMaidET.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())){

                        Toast.makeText(getApplicationContext(),"Presssss",Toast.LENGTH_SHORT).show();

                        return true;
                    }
                }
                return false;
            }
        });
    }

    private void handleButton(){
        sortOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               moveToSortPage();
            }
        });

        filterOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveToFilterPage();
            }
        });
    }

    private void moveToSortPage(){
        Intent intent = new Intent(this, SortMaidActivity.class);
        startActivity(intent);
    }

    private void moveToFilterPage(){
        Intent intent = new Intent(this, FilterActivity.class);
        startActivity(intent);
    }
}
