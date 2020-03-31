package com.bantoo.babooo.Pages.MonthlyServicePage.FilterPage;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bantoo.babooo.Model.City;
import com.bantoo.babooo.Pages.MonthlyServicePage.FilterPage.CityRegionPage.CityRegionActivity;
import com.bantoo.babooo.R;
import com.bantoo.babooo.Utils.BaseActivity;
import com.jaygoo.widget.OnRangeChangedListener;
import com.jaygoo.widget.RangeSeekBar;
import com.takusemba.multisnaprecyclerview.MultiSnapRecyclerView;
import com.tobiasschuerg.prefixsuffix.PrefixSuffixEditText;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FilterActivity extends BaseActivity implements Serializable, CityFilterOnItemClickListener {

    MultiSnapRecyclerView cityRV;
    RangeSeekBar filterAge, filterYears, filterCost;
    PrefixSuffixEditText maxAgeET, minAgeET, maxYearsET, minYearsET;
    EditText maxCostET, minCostET;
    TextView allCityBtn;
    ImageView closeBtn, popularity1, popularity2, popularity3, popularity4, popularity5;
    CityFilterRecyclerViewAdapter adapter;
    LinearLayoutManager manager;
    List<City> cityList = new ArrayList<City>();

    String SERIALIZEPARAMETER = "choosenCity";
    static final int CHOOSENCITYACTIVITYRESULT = 1;
    static final String CHOOSENCITYRESULT = "choosenCityResults";

    int defaultMaxAgeValue = 60;
    int defaultMinAgeValue = 18;
    int defaultMinYearsValue = 0;
    int defaultMaxYearsValue = 20;
    int defaultMinCostValue = 100;
    int defaultMaxCostValue = 1000;

    String maxCost, minCost, maxYears, minYears, minAge, maxAge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        cityRV = findViewById(R.id.cityChoosenRV);
        filterAge = findViewById(R.id.filterAge_search_RSB);
        filterYears = findViewById(R.id.filterYears_search_RSB);
        filterCost = findViewById(R.id.filterCost_search_RSB);
        maxAgeET = findViewById(R.id.maximumAge_filter_ET);
        minAgeET = findViewById(R.id.minimumAge_filter_ET);
        maxYearsET = findViewById(R.id.maximumYears_filter_ET);
        minYearsET = findViewById(R.id.minimumYears_filter_ET);
        maxCostET = findViewById(R.id.maximumCost_filter_ET);
        minCostET = findViewById(R.id.minimumCost_filter_ET);
        allCityBtn = findViewById(R.id.allCity_filter_TV);
        closeBtn = findViewById(R.id.close_filter_IV);
        popularity1 = findViewById(R.id.star_popularity1);
        popularity2 = findViewById(R.id.star_popularity2);
        popularity3 = findViewById(R.id.star_popularity3);
        popularity4 = findViewById(R.id.star_popularity4);
        popularity5 = findViewById(R.id.star_popularity5);

        initVar();
        handleRangeSeekBar();
        handleButton();
        handlePopularity(5);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case CHOOSENCITYACTIVITYRESULT:
                if (resultCode == Activity.RESULT_OK) {

                    if (data != null && data.getSerializableExtra(CHOOSENCITYRESULT) != null) {
                        cityList = (ArrayList<City>) data.getSerializableExtra(CHOOSENCITYRESULT);

                        if (cityList.isEmpty() || cityList.size() == 1) {
                            cityList.clear();
                            City defaultValue = new City(0, "Semua Kota", true);
                            cityList.add(defaultValue);
                        } else if (cityList.size() > 1) {
                            for (int i = 0; i < cityList.size(); i++) {
                                if (cityList.get(i).getCityID() == 0) {
                                    cityList.remove(i);
                                }
                            }
                        }
                        setupCityRecyclerView(cityList);
                    }
                }
                break;
            default:
                break;
        }
    }

    private void initVar() {
        filterAge.setRange(defaultMinAgeValue, defaultMaxAgeValue);
        filterAge.setProgress(defaultMinAgeValue, defaultMaxAgeValue);
        filterAge.setIndicatorTextDecimalFormat("0");

        filterYears.setRange(defaultMinYearsValue, defaultMaxYearsValue);
        filterYears.setProgress(defaultMinYearsValue, defaultMaxYearsValue);
        filterYears.setIndicatorTextDecimalFormat("0");

        filterCost.setRange(defaultMinCostValue, defaultMaxCostValue);
        filterCost.setProgress(defaultMinCostValue, defaultMaxCostValue);
        filterCost.setIndicatorTextDecimalFormat("0");

        minAgeET.setText(String.valueOf(defaultMinAgeValue));
        maxAgeET.setText(String.valueOf(defaultMaxAgeValue));
        minYearsET.setText(String.valueOf(defaultMinYearsValue));
        maxYearsET.setText(String.valueOf(defaultMaxYearsValue));
        minCostET.setText(String.valueOf(defaultMinCostValue));
        maxCostET.setText(String.valueOf(defaultMaxCostValue));

        //add default value to citylist
        City defaultValue = new City(0, "Semua Kota", true);
        cityList.add(defaultValue);
        setupCityRecyclerView(cityList);

    }

    private void handleRangeSeekBar() {
        filterAge.setOnRangeChangedListener(new OnRangeChangedListener() {
            @Override
            public void onRangeChanged(RangeSeekBar view, float leftValue, float rightValue, boolean isFromUser) {

                minAge = String.valueOf((int) leftValue);
                maxAge = String.valueOf((int) rightValue);

                minAgeET.setText(minAge);
                maxAgeET.setText(maxAge);

            }

            @Override
            public void onStartTrackingTouch(RangeSeekBar view, boolean isLeft) {
            }

            @Override
            public void onStopTrackingTouch(RangeSeekBar view, boolean isLeft) {
            }
        });

        filterYears.setOnRangeChangedListener(new OnRangeChangedListener() {
            @Override
            public void onRangeChanged(RangeSeekBar view, float leftValue, float rightValue, boolean isFromUser) {

                maxYears = String.valueOf((int) rightValue);
                minYears = String.valueOf((int) leftValue);

                minYearsET.setText(minYears);
                maxYearsET.setText(maxYears);

            }

            @Override
            public void onStartTrackingTouch(RangeSeekBar view, boolean isLeft) {
            }

            @Override
            public void onStopTrackingTouch(RangeSeekBar view, boolean isLeft) {
            }
        });

        filterCost.setOnRangeChangedListener(new OnRangeChangedListener() {
            @Override
            public void onRangeChanged(RangeSeekBar view, float leftValue, float rightValue, boolean isFromUser) {
                maxCost = String.valueOf((int) rightValue);
                minCost = String.valueOf((int) leftValue);

                minCostET.setText(minCost);
                maxCostET.setText(maxCost);
            }

            @Override
            public void onStartTrackingTouch(RangeSeekBar view, boolean isLeft) {
            }

            @Override
            public void onStopTrackingTouch(RangeSeekBar view, boolean isLeft) {
            }
        });
    }


    private void handlePopularity(int popularity) {
        switch (popularity) {

            case 5:
                popularity1.setImageResource(R.drawable.asset_star_active);
                popularity2.setImageResource(R.drawable.asset_star_active);
                popularity3.setImageResource(R.drawable.asset_star_active);
                popularity4.setImageResource(R.drawable.asset_star_active);
                popularity5.setImageResource(R.drawable.asset_star_active);
                break;
            case 4:
                popularity1.setImageResource(R.drawable.asset_star_active);
                popularity2.setImageResource(R.drawable.asset_star_active);
                popularity3.setImageResource(R.drawable.asset_star_active);
                popularity4.setImageResource(R.drawable.asset_star_active);
                popularity5.setImageResource(R.drawable.asset_star_inactive);
                break;
            case 3:
                popularity1.setImageResource(R.drawable.asset_star_active);
                popularity2.setImageResource(R.drawable.asset_star_active);
                popularity3.setImageResource(R.drawable.asset_star_active);
                popularity4.setImageResource(R.drawable.asset_star_inactive);
                popularity5.setImageResource(R.drawable.asset_star_inactive);
                break;
            case 2:
                popularity1.setImageResource(R.drawable.asset_star_active);
                popularity2.setImageResource(R.drawable.asset_star_active);
                popularity3.setImageResource(R.drawable.asset_star_inactive);
                popularity4.setImageResource(R.drawable.asset_star_inactive);
                popularity5.setImageResource(R.drawable.asset_star_inactive);
                break;
            case 1:
                popularity1.setImageResource(R.drawable.asset_star_active);
                popularity2.setImageResource(R.drawable.asset_star_inactive);
                popularity3.setImageResource(R.drawable.asset_star_inactive);
                popularity4.setImageResource(R.drawable.asset_star_inactive);
                popularity5.setImageResource(R.drawable.asset_star_inactive);
                break;

            default:
                break;
        }
    }

    private void moveToCityPage() {

        Intent intent = new Intent(FilterActivity.this, CityRegionActivity.class);
        intent.putExtra(SERIALIZEPARAMETER, (Serializable) cityList);
        startActivityForResult(intent, CHOOSENCITYACTIVITYRESULT);

    }


    private void handleButton() {
        allCityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveToCityPage();
            }
        });

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        popularity1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handlePopularity(1);
            }
        });

        popularity2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handlePopularity(2);
            }
        });

        popularity3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handlePopularity(3);
            }
        });

        popularity4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handlePopularity(4);
            }
        });

        popularity5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handlePopularity(5);
            }
        });

    }

    private void setupCityRecyclerView(List<City> city) {
        adapter = new CityFilterRecyclerViewAdapter(getApplicationContext(), city, this);
        manager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);

        cityRV.setLayoutManager(manager);
        cityRV.setAdapter(adapter);
    }

    @Override
    public void cityClose(int position) {
        cityList.remove(position);

        if (cityList.isEmpty()) {
            City defaultValue = new City(0, "Semua Kota", true);
            cityList.add(defaultValue);
        }

        setupCityRecyclerView(cityList);
    }

}
