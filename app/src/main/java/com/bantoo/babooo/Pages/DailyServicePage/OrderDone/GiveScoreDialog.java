package com.bantoo.babooo.Pages.DailyServicePage.OrderDone;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;


import com.bantoo.babooo.Pages.HomePage.HomeActivity;
import com.bantoo.babooo.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class GiveScoreDialog extends Dialog {

    Button saveBtn;
    ImageView star1IV, star2IV, star3IV, star4IV, star5IV;
    EditText commentET;
    int starChoosen;
    String orderUniqueKey;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference orderReference;

    public Activity activity;

    public GiveScoreDialog(Activity activity, String orderUniqueKey) {
        super(activity);
        this.activity = activity;
        this.orderUniqueKey = orderUniqueKey;
    }

    private void activateStar() {
        switch (starChoosen) {
            case 5: star5IV.setImageResource(R.drawable.asset_star_active);
            case 4: star4IV.setImageResource(R.drawable.asset_star_active);
            case 3: star3IV.setImageResource(R.drawable.asset_star_active);
            case 2: star2IV.setImageResource(R.drawable.asset_star_active);
            case 1: star1IV.setImageResource(R.drawable.asset_star_active);
        }
    }

    private void deactivateStar() {
        star5IV.setImageResource(R.drawable.asset_star_inactive);
        star4IV.setImageResource(R.drawable.asset_star_inactive);
        star3IV.setImageResource(R.drawable.asset_star_inactive);
        star2IV.setImageResource(R.drawable.asset_star_inactive);
        star1IV.setImageResource(R.drawable.asset_star_inactive);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.give_score_dialog);

        firebaseDatabase = FirebaseDatabase.getInstance();
        orderReference = firebaseDatabase.getReference().child("Order").child(orderUniqueKey);

        commentET = findViewById(R.id.comment_ET_give_dialog);
        star1IV = findViewById(R.id.star1IV_give_dialog);
        star2IV = findViewById(R.id.star2IV_give_dialog);
        star3IV = findViewById(R.id.star3IV_give_dialog);
        star4IV = findViewById(R.id.star4IV_give_dialog);
        star5IV = findViewById(R.id.star5IV_give_dialog);
        saveBtn = findViewById(R.id.save_score_dialog);

        star1IV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deactivateStar();
                starChoosen = 1;
                activateStar();
            }
        });
        star2IV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deactivateStar();
                starChoosen = 2;
                activateStar();
            }
        });
        star3IV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deactivateStar();
                starChoosen = 3;
                activateStar();
            }
        });
        star4IV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deactivateStar();
                starChoosen = 4;
                activateStar();
            }
        });
        star5IV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deactivateStar();
                starChoosen = 5;
                activateStar();
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderReference.child("rating").setValue(starChoosen);
                orderReference.child("comment").setValue(commentET.getText().toString());
                Intent intent = new Intent(getContext(), HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                activity.startActivity(intent);
            }
        });
    }

}
