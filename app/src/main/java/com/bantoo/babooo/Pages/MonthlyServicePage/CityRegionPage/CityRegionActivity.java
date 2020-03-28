package com.bantoo.babooo.Pages.MonthlyServicePage.CityRegionPage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.bantoo.babooo.R;

import java.util.ArrayList;
import java.util.List;

public class CityRegionActivity extends AppCompatActivity {

    ListView cityLV;
    ImageView closeAction;

    List<String> cityList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_region);

        cityLV = findViewById(R.id.city_LV);
        closeAction = findViewById(R.id.close_city_IV);

        generalStyling();
        addCity();
        setupLV();
        handleListViewClickListener();
        handleAction();
    }

    public void generalStyling(){
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(getApplicationContext(),R.color.orangePrimary));
        window.setNavigationBarColor(ContextCompat.getColor(getApplicationContext(),R.color.greenPrimary));
    }

    private void addCity(){
        cityList.add("Jakarta");
        cityList.add("Bogor");
        cityList.add("Tangerang");
        cityList.add("Depok");
        cityList.add("Bekasi");
        cityList.add("Bandung");
        cityList.add("Semarang");
        cityList.add("Surabaya");
    }

    private void setupLV(){
        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,cityList);
        cityLV.setAdapter(adapter);
    }

    private void handleListViewClickListener(){
        cityLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                cityLV.setSelection(position);
                finish();
                Toast.makeText(getApplicationContext(),cityList.get(position),Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void handleAction(){
        closeAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
