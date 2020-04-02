package com.bantoo.babooo.Pages.UserDetailPage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.bantoo.babooo.Pages.MonthlyServicePage.MonthlyConfirmationPage.MonthlyConfirmationActivity;
import com.bantoo.babooo.R;
import com.bantoo.babooo.Utilities.BaseActivity;

public class MaidDetailActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maid_detail);

        //Buat sementara testing di set ke MonthlyServiceDetail
        Intent intent = new Intent(this, MonthlyConfirmationActivity.class);
        startActivity(intent);
    }
}
