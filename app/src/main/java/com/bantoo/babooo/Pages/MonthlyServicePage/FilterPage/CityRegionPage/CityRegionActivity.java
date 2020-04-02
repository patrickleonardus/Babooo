package com.bantoo.babooo.Pages.MonthlyServicePage.FilterPage.CityRegionPage;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bantoo.babooo.Model.City;
import com.bantoo.babooo.R;
import com.bantoo.babooo.Utilities.BaseActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CityRegionActivity extends BaseActivity implements Serializable, CityRegionOnClickListener {

    LinearLayoutManager manager;
    CityRegionRecyclerViewAdapter adapter;
    List<City> defaultCityList = new ArrayList<City>();
    List<City> filterCityList = new ArrayList<City>();
    List<City> cityListToFilterActivity = new ArrayList<City>();

    static final String CHOOSENCITYRESULTS = "choosenCityResults";
    static final String SERIALIZEPARAMETER = "choosenCity";
    boolean searching, allCityChecked, spesifiedCityChecked, cityIsCheckedExceeded;
    int cityIsCheckedCount;

    RecyclerView cityRV;
    ImageView closeAction;
    EditText cityET;
    LinearLayout errorLayout;
    Button setCity;
    RelativeLayout contentWrapper;

    int textLength = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_region);

        cityRV = findViewById(R.id.city_RV);
        closeAction = findViewById(R.id.close_city_IV);
        cityET = findViewById(R.id.city_ET);
        errorLayout = findViewById(R.id.city_error_layout);
        setCity = findViewById(R.id.set_city_BTN);
        contentWrapper = findViewById(R.id.city_contentWrapper);

        defaultCityData();
        prepareCityData();
        initializeRV(defaultCityList);
        handleAction();
        handleSearch();
    }

    private void defaultCityData() {
        City data1 = new City(0, "Semua Kota", false);
        City data2 = new City(1, "Jakarta", false);
        City data3 = new City(2, "Bogor", false);
        City data4 = new City(3, "Tangerang", false);
        City data5 = new City(4, "Depok", false);
        City data6 = new City(5, "Bekasi", false);
        City data7 = new City(6, "Bandung", false);
        City data8 = new City(7, "Semarang", false);
        City data9 = new City(8, "Surabaya", false);

        defaultCityList.add(data1);
        defaultCityList.add(data2);
        defaultCityList.add(data3);
        defaultCityList.add(data4);
        defaultCityList.add(data5);
        defaultCityList.add(data6);
        defaultCityList.add(data7);
        defaultCityList.add(data8);
        defaultCityList.add(data9);
    }

    private void prepareCityData() {
        List<City> cityListFromFilterActivity = (ArrayList<City>) getIntent().getSerializableExtra(SERIALIZEPARAMETER);

        for (int i = 0; i < cityListFromFilterActivity.size(); i++) {
            for (int j = 0; j < defaultCityList.size(); j++) {
                if (cityListFromFilterActivity.get(i).getCityID() == defaultCityList.get(j).getCityID()) {
                    defaultCityList.get(j).setChecked(cityListFromFilterActivity.get(i).isChecked());
                }
            }
        }
    }

    private void getCitySelected() {

        for (int i = 0; i < defaultCityList.size(); i++) {
            if (defaultCityList.get(i).isChecked()) {
                cityListToFilterActivity.add(defaultCityList.get(i));
            }
        }
    }

    private void validateCity() {
        cityIsCheckedCount = 0;
        allCityChecked = false;
        spesifiedCityChecked = false;
        for (int i = 0; i < defaultCityList.size(); i++) {
            if (defaultCityList.get(i).getCityID() == 0 && defaultCityList.get(i).isChecked()) {
                //Kondisi semua kota dicheck
                allCityChecked = true;
                cityIsCheckedCount = cityIsCheckedCount + 1;
            }
            if (defaultCityList.get(i).getCityID() > 0 && defaultCityList.get(i).isChecked()) {
                //kondisi kota lain dicek
                spesifiedCityChecked = true;
                cityIsCheckedCount = cityIsCheckedCount + 1;
            }
        }
        if (cityIsCheckedCount > 4) {
            cityIsCheckedExceeded = true;
        }
    }

    @Override
    public void onCityClick(int position) {

        if (searching) {
            for (int i = 0; i < filterCityList.size(); i++) {
                for (int j = 0; j < defaultCityList.size(); j++) {
                    if (filterCityList.get(i).getCityID() == defaultCityList.get(j).getCityID()) {
                        defaultCityList.get(j).setChecked(filterCityList.get(i).isChecked());
                    }
                }
            }
        } else {
            if (defaultCityList.get(position).isChecked()) {
                defaultCityList.get(position).setChecked(false);
            } else if (!defaultCityList.get(position).isChecked()) {
                defaultCityList.get(position).setChecked(true);
            }
        }
    }

    private void handleAction() {
        closeAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        setCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getCitySelected();
//                validateCity();

                Intent cityResult = new Intent();
                cityResult.putExtra(CHOOSENCITYRESULTS, (Serializable) cityListToFilterActivity);
                setResult(Activity.RESULT_OK, cityResult);
                finish();


            }
        });
    }

    private void initializeRV(List<City> cities) {

        adapter = new CityRegionRecyclerViewAdapter(getApplicationContext(), cities, this);
        manager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);

        cityRV.setLayoutManager(manager);
        cityRV.setAdapter(adapter);

    }

    private void handleSearch() {
        cityET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                searching = true;
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                //ambil panjang kata dari kota
                textLength = cityET.getText().length();
                //clear array sortnya
                filterCityList.clear();

                if (cityET.getText().toString().isEmpty()) {
                    searching = false;
                    filterCityList.addAll(defaultCityList);
                    showError(false);
                } else {
                    //loop sebanyak data kota
                    for (int i = 0; i < defaultCityList.size(); i++) {
                        if (textLength <= defaultCityList.get(i).getCityName().length()) {
                            String citySearch = cityET.getText().toString().toLowerCase();
                            String city = defaultCityList.get(i).getCityName().toLowerCase();

                            if (city.contains(citySearch)) {
                                filterCityList.add(defaultCityList.get(i));
                                showError(false);
                            } else {
                                if (filterCityList.isEmpty()) {
                                    showError(true);
                                }
                            }
                        } else {
                            if (filterCityList.isEmpty()) {
                                showError(true);
                            }
                        }
                    }
                }
                initializeRV(filterCityList);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void showError(boolean error) {
        if (error) {
            contentWrapper.setVisibility(View.GONE);
            errorLayout.setVisibility(View.VISIBLE);
        } else if (!error) {
            contentWrapper.setVisibility(View.VISIBLE);
            errorLayout.setVisibility(View.GONE);
        }
    }
}
