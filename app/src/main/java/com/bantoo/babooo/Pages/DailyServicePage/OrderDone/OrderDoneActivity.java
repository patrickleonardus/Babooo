package com.bantoo.babooo.Pages.DailyServicePage.OrderDone;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.bantoo.babooo.R;

public class OrderDoneActivity extends AppCompatActivity {

    private Button backButton;
    private String orderUniquekey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_daily_done);

        orderUniquekey = getIntent().getStringExtra("orderUniqueKey");
        backButton = findViewById(R.id.give_score_done_daily_activity);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GiveScoreDialog giveScoreDialog = new GiveScoreDialog(OrderDoneActivity.this, orderUniquekey);
                giveScoreDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                giveScoreDialog.show();
            }
        });
    }

    public void backToHome() {

    }
}