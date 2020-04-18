package com.bantoo.babooo.Pages.UserDetailPage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bantoo.babooo.Pages.MonthlyServicePage.MonthlyConfirmationPage.MonthlyConfirmationActivity;
import com.bantoo.babooo.R;
import com.bantoo.babooo.Utilities.BaseActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MaidDetailActivity extends BaseActivity {

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference monthlyMaidReference;

    private String maidUniqueKey;

    ImageView rating1IV, rating2IV, rating3IV, rating4IV, rating5IV;
    TextView maidNameTV, maidAgeTV, maidAddressTV, ratingTV;
    Button recruitButton;
    ProgressBar cuciPB, setrikaPB, sapuPB, kmrmandiPB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maid_detail);

        initView();
        setButtonAction();
        getMaidData();
        //Buat sementara testing di set ke MonthlyServiceDetail
        /*
        Intent intent = new Intent(this, MonthlyConfirmationActivity.class);
        startActivity(intent);*/
    }

    private void getMaidData() {
        maidUniqueKey = getIntent().getStringExtra("maidUniqueKey");
        firebaseDatabase = FirebaseDatabase.getInstance();
        monthlyMaidReference = firebaseDatabase.getReference().child("ARTBulanan");
        monthlyMaidReference.child(maidUniqueKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                maidNameTV.setText(dataSnapshot.child("name").getValue().toString());
                //maidAgeTV.setText(dataSnapshot.child("age").getValue().toString());
                maidAddressTV.setText(dataSnapshot.child("address").getValue().toString());
                int rating = Integer.parseInt(dataSnapshot.child("rating").getValue().toString());
                ratingTV.setText(""+rating);
                switch (rating) {
                    case 5:
                        rating5IV.setVisibility(View.VISIBLE);
                    case 4:
                        rating4IV.setVisibility(View.VISIBLE);
                    case 3:
                        rating3IV.setVisibility(View.VISIBLE);
                    case 2:
                        rating2IV.setVisibility(View.VISIBLE);
                    case 1:
                        rating1IV.setVisibility(View.VISIBLE);
                }
                int cuciScore = Integer.parseInt(dataSnapshot.child("cuciScore").getValue().toString());
                int setrikaScore = Integer.parseInt(dataSnapshot.child("setrikaScore").getValue().toString());
                int sapuScore = Integer.parseInt(dataSnapshot.child("sapuScore").getValue().toString());
                int kmrmandiScore = Integer.parseInt(dataSnapshot.child("kmrmandiScore").getValue().toString());

                cuciPB.setProgress(cuciScore);
                setrikaPB.setProgress(setrikaScore);
                sapuPB.setProgress(sapuScore);
                kmrmandiPB.setProgress(kmrmandiScore);

                /*maidSkillTV1.setText(dataSnapshot.child("skill1").getValue().toString());
                maidSkillTV2.setText(dataSnapshot.child("skill2").getValue().toString());
                maidSkillTV3.setText(dataSnapshot.child("skill3").getValue().toString());*/
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setButtonAction() {
        recruitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent moveToConfirmation = new Intent(MaidDetailActivity.this, MonthlyConfirmationActivity.class);
                moveToConfirmation.putExtra("maidUniqueKey", maidUniqueKey);
                startActivity(moveToConfirmation);
            }
        });
    }

    private void initView() {
        rating1IV = findViewById(R.id.rating_image1_maid_detail);
        rating2IV = findViewById(R.id.rating_image2_maid_detail);
        rating3IV = findViewById(R.id.rating_image3_maid_detail);
        rating4IV = findViewById(R.id.rating_image4_maid_detail);
        rating5IV = findViewById(R.id.rating_image5_maid_detail);
        maidNameTV = findViewById(R.id.maid_name_TV_maid_detail);
        maidAgeTV = findViewById(R.id.maid_age_TV_maid_detail);
        maidAddressTV = findViewById(R.id.maid_address_TV_maid_detail);
        ratingTV = findViewById(R.id.rating_tv_maid_detail);
        recruitButton = findViewById(R.id.order_daily_confirmation_BTN);
        cuciPB = findViewById(R.id.progress_bar_cuci_kering_maid_detail);
        setrikaPB = findViewById(R.id.progress_bar_setrika_maid_detail);
        sapuPB = findViewById(R.id.progress_bar_sapu_maid_detail);
        kmrmandiPB = findViewById(R.id.progress_bar_kmrmandi_maid_detail);

        rating1IV.setVisibility(View.GONE);
        rating2IV.setVisibility(View.GONE);
        rating3IV.setVisibility(View.GONE);
        rating4IV.setVisibility(View.GONE);
        rating5IV.setVisibility(View.GONE);
    }
}
