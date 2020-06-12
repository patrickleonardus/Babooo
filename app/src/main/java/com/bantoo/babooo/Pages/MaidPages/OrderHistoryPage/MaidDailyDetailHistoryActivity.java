package com.bantoo.babooo.Pages.MaidPages.OrderHistoryPage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bantoo.babooo.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MaidDailyDetailHistoryActivity extends AppCompatActivity {

    private TextView noPesanan_TV, userName_TV, orderDate_TV, timeStart_TV, timeEnd_TV, location_TV, noteLoc_TV,
    serviceType_TV, serviceCost_TV, feeCost_TV, total_TV, comment_TV;
    private ImageView closeIV, star1IV, star2IV, star3IV, star4IV, star5IV;;

    private String orderKey, user_name, orderDate, timeStart, timeEnd, location, noteLocation, serviceType, comment, rating;
    private Integer feeCost, totalCost, serviceCost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maid_daily_detail_history);

        initView();
        handleAction();
        getExtra();
        feeCost = 10;
        totalCost = serviceCost-feeCost;
        setView();
        setRating();
    }

    private void handleAction(){
        closeIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setView(){
        noPesanan_TV.setText(orderKey);
        userName_TV.setText(user_name);
        orderDate_TV.setText(orderDate);
        timeStart_TV.setText(timeStart);
        timeEnd_TV.setText(timeEnd);
        location_TV.setText(location);
        noteLoc_TV.setText(noteLocation);
        serviceType_TV.setText(serviceType);
        serviceCost_TV.setText(serviceCost.toString());
        feeCost_TV.setText(feeCost.toString());
        total_TV.setText(totalCost.toString());
        comment_TV.setText(comment);
    }

    private void setRating(){
//        int rate = Integer.parseInt(rating.substring(0,1));
//        for(int i = 0 ; i < rate; i++){
//            switch (i){
//                case 4: star5IV.setImageResource(R.drawable.asset_star_active);
//                case 3: star4IV.setImageResource(R.drawable.asset_star_active);
//                case 2: star3IV.setImageResource(R.drawable.asset_star_active);
//                case 1: star2IV.setImageResource(R.drawable.asset_star_active);
//                case 0: star1IV.setImageResource(R.drawable.asset_star_active);
//            }
//        }
    }

    private void getExtra(){
        orderKey = getIntent().getStringExtra("orderKey");
        user_name = getIntent().getStringExtra("user_name");
        orderDate = getIntent().getStringExtra("orderDate");
        timeStart = getIntent().getStringExtra("timeStart");
        timeEnd = getIntent().getStringExtra("timeEnd");
        location = getIntent().getStringExtra("location");
        noteLocation = getIntent().getStringExtra("noteLocation");
        serviceType = getIntent().getStringExtra("serviceType");
        serviceCost = getIntent().getIntExtra("serviceCost", 100);
        comment = getIntent().getStringExtra("comment");
        rating = getIntent().getStringExtra("rating");
    }

    private void initView(){

        closeIV = findViewById(R.id.close_history_daily_IV);
        noPesanan_TV = findViewById(R.id.order_number_TV);
        userName_TV = findViewById(R.id.user_name_TV);
        orderDate_TV = findViewById(R.id.date_detail_TV);
        timeStart_TV = findViewById(R.id.time_start_detailTV);
        timeEnd_TV = findViewById(R.id.estimated_time_TV);
        location_TV = findViewById(R.id.location_TV);
        noteLoc_TV = findViewById(R.id.notes_location_TV);
        serviceType_TV = findViewById(R.id.name_service_TV);
        serviceCost_TV = findViewById(R.id.cost_service_TV);
        feeCost_TV = findViewById(R.id.fee_cost_TV);
        total_TV = findViewById(R.id.total_income_TV);
        comment_TV = findViewById(R.id.comment_TV);

        star1IV = findViewById(R.id.star1IV_give_dialog);
        star2IV = findViewById(R.id.star2IV_give_dialog);
        star3IV = findViewById(R.id.star3IV_give_dialog);
        star4IV = findViewById(R.id.star4IV_give_dialog);
        star5IV = findViewById(R.id.star5IV_give_dialog);

    }
}
