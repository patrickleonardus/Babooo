package com.bantoo.babooo.Pages.MaidPages.OrderHistoryPage;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bantoo.babooo.R;

public class MaidMonthlyDetailHistoryActivity extends AppCompatActivity {

    private TextView noPesanan_TV, userName_TV, duration_TV, arrivalDate_TV, dateStart_TV, dateFinish_TV, location_TV, noteLoc_TV,
            serviceType_TV, serviceCost_TV, multiply_TV, total_TV, comment_TV;
    private ImageView closeIV, star1IV, star2IV, star3IV, star4IV, star5IV;;

    private String orderKey, user_name, arrivalDate, dateStart, dateFinish, location, noteLocation, serviceType, comment, rating, duration;
    private Integer totalCost, serviceCost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maid_monthly_detail_history);

        initView();
        handleAction();
        getExtra();
        totalCost = Integer.parseInt(duration)*serviceCost;
        setView();
        setRating();
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
        duration_TV.setText(duration);
        arrivalDate_TV.setText(arrivalDate);
        dateStart_TV.setText(dateStart);
        dateFinish_TV.setText(dateFinish);
        location_TV.setText(location);
        noteLoc_TV.setText(noteLocation);
        multiply_TV.setText(duration+" x");
        total_TV.setText(totalCost);
        if (!comment.equals(null)){
            comment_TV.setText(comment);
        }
        else {
            comment_TV.setText("-");
        }

    }

    private void getExtra(){
        orderKey = getIntent().getStringExtra("orderKey");
        user_name = getIntent().getStringExtra("user_name");
        duration = getIntent().getStringExtra("duration");
        arrivalDate = getIntent().getStringExtra("orderDate");
        dateStart = getIntent().getStringExtra("dateStart");
        dateFinish = getIntent().getStringExtra("dateFinish");
        location = getIntent().getStringExtra("location");
        noteLocation = getIntent().getStringExtra("noteLocation");
        serviceCost = getIntent().getIntExtra("serviceCost", 0);
        comment = getIntent().getStringExtra("comment");
        rating = getIntent().getStringExtra("rating");
    }

    private void initView(){

        closeIV = findViewById(R.id.close_history_monthly_IV);
        noPesanan_TV = findViewById(R.id.order_number_TV);
        userName_TV = findViewById(R.id.user_name_TV);
        duration_TV = findViewById(R.id.durasi_monthly_history_TV);
        arrivalDate_TV = findViewById(R.id.date_arrival_TV);
        dateStart_TV = findViewById(R.id.date_start_TV);
        dateFinish_TV = findViewById(R.id.date_finish_TV);
        location_TV = findViewById(R.id.location_TV);
        noteLoc_TV = findViewById(R.id.notes_location_TV);
        serviceCost_TV = findViewById(R.id.cost_service_TV);
        multiply_TV = findViewById(R.id.multiply_durasi_TV);
        total_TV = findViewById(R.id.total_income_TV);
        comment_TV = findViewById(R.id.comment_TV);

        star1IV = findViewById(R.id.star1IV_give_dialog);
        star2IV = findViewById(R.id.star2IV_give_dialog);
        star3IV = findViewById(R.id.star3IV_give_dialog);
        star4IV = findViewById(R.id.star4IV_give_dialog);
        star5IV = findViewById(R.id.star5IV_give_dialog);

    }
}
