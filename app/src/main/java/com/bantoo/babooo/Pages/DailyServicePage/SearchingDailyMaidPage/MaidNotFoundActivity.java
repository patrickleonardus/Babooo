package com.bantoo.babooo.Pages.DailyServicePage.SearchingDailyMaidPage;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.bantoo.babooo.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MaidNotFoundActivity extends AppCompatActivity {

    Button backToOrderBtn;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference orderReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maid_not_found);

        backToOrderBtn = findViewById(R.id.backToOrderBtn_artNotFound);
        backToOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("result", "back");
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });
        removeOrder();
    }

    private void removeOrder() {
        String orderUniqueKey = getIntent().getStringExtra("orderID");
        firebaseDatabase = FirebaseDatabase.getInstance();
        orderReference = firebaseDatabase.getReference().child("Order");
        orderReference.child(orderUniqueKey).removeValue();
    }
}
