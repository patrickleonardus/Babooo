package com.bantoo.babooo.Pages.MonthlyServicePage.FilterPage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.bantoo.babooo.Pages.MonthlyServicePage.CityRegionPage.CityRegionActivity;
import com.bantoo.babooo.R;

public class FilterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        moveToCityPage();

        generalStyling();

    }

    public void generalStyling(){
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(getApplicationContext(),R.color.orangePrimary));
        window.setNavigationBarColor(ContextCompat.getColor(getApplicationContext(),R.color.greenPrimary));
    }

    private void moveToCityPage(){
        Intent intent = new Intent(this, CityRegionActivity.class);
        startActivity(intent);
    }

}
