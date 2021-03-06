package com.bantoo.babooo.Pages.MonthlyServicePage.FilterPage;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bantoo.babooo.Model.City;
import com.bantoo.babooo.Model.FilterSearch;
import com.bantoo.babooo.Pages.MonthlyServicePage.FilterPage.CityRegionPage.CityRegionActivity;
import com.bantoo.babooo.R;
import com.bantoo.babooo.Utilities.BaseActivity;
import com.jaygoo.widget.OnRangeChangedListener;
import com.jaygoo.widget.RangeSeekBar;
import com.takusemba.multisnaprecyclerview.MultiSnapRecyclerView;
import com.tobiasschuerg.prefixsuffix.PrefixSuffixEditText;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FilterActivity extends BaseActivity implements Serializable, CityFilterOnItemClickListener {

    private MultiSnapRecyclerView cityRV;
    private RangeSeekBar filterAge, filterYears, filterCost;
    private PrefixSuffixEditText maxAgeET, minAgeET, maxYearsET, minYearsET;
    private EditText maxCostET, minCostET;
    private TextView allCityBTN;
    private ImageView closeIV, popularity1IV, popularity2IV, popularity3IV, popularity4IV, popularity5IV;
    private Button setFilterBTN;
    private CityFilterRecyclerViewAdapter adapter;
    private LinearLayoutManager manager;
    private List<City> cityList = new ArrayList<City>();
    private List<FilterSearch> filterSearches = new ArrayList<FilterSearch>();

    private static final String SERIALIZEPARAMETER = "choosenCity";
    private static final String CHOOSENCITYRESULT = "choosenCityResults";
    private static final int CHOOSENCITYACTIVITYRESULT = 1;
    private static final String FILTERINTENT = "filterIntent";

    private int defaultMaxAgeValue = 60;
    private int defaultMinAgeValue = 18;
    private int defaultMinYearsValue = 0;
    private int defaultMaxYearsValue = 20;
    private int defaultMinCostValue = 1500000;
    private int defaultMaxCostValue = 2500000;
    private int maidPopularity = 5;

    private String maxCost, minCost, maxYears, minYears, minAge, maxAge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        initView();
        initVar();
        handleRangeSeekBar();
        handleButton();
        handlePopularity(5);

        //prevent keyboard opened
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    private void initView() {
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
        allCityBTN = findViewById(R.id.allCity_filter_TV);
        closeIV = findViewById(R.id.close_filter_IV);
        popularity1IV = findViewById(R.id.star_popularity1);
        popularity2IV = findViewById(R.id.star_popularity2);
        popularity3IV = findViewById(R.id.star_popularity3);
        popularity4IV = findViewById(R.id.star_popularity4);
        popularity5IV = findViewById(R.id.star_popularity5);
        setFilterBTN = findViewById(R.id.setFilter_BTN);
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

        //add default value to all variable
        minAge = String.valueOf(18);
        maxAge = String.valueOf(60);
        minYears = String.valueOf(0);
        maxYears = String.valueOf(20);
        minCost = String.valueOf(1500000);
        maxCost = String.valueOf(2500000);
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
                maidPopularity = 5;
                popularity1IV.setImageResource(R.drawable.asset_star_active);
                popularity2IV.setImageResource(R.drawable.asset_star_active);
                popularity3IV.setImageResource(R.drawable.asset_star_active);
                popularity4IV.setImageResource(R.drawable.asset_star_active);
                popularity5IV.setImageResource(R.drawable.asset_star_active);
                break;
            case 4:
                maidPopularity = 4;
                popularity1IV.setImageResource(R.drawable.asset_star_active);
                popularity2IV.setImageResource(R.drawable.asset_star_active);
                popularity3IV.setImageResource(R.drawable.asset_star_active);
                popularity4IV.setImageResource(R.drawable.asset_star_active);
                popularity5IV.setImageResource(R.drawable.asset_star_inactive);
                break;
            case 3:
                maidPopularity = 3;
                popularity1IV.setImageResource(R.drawable.asset_star_active);
                popularity2IV.setImageResource(R.drawable.asset_star_active);
                popularity3IV.setImageResource(R.drawable.asset_star_active);
                popularity4IV.setImageResource(R.drawable.asset_star_inactive);
                popularity5IV.setImageResource(R.drawable.asset_star_inactive);
                break;
            case 2:
                maidPopularity = 2;
                popularity1IV.setImageResource(R.drawable.asset_star_active);
                popularity2IV.setImageResource(R.drawable.asset_star_active);
                popularity3IV.setImageResource(R.drawable.asset_star_inactive);
                popularity4IV.setImageResource(R.drawable.asset_star_inactive);
                popularity5IV.setImageResource(R.drawable.asset_star_inactive);
                break;
            case 1:
                maidPopularity = 1;
                popularity1IV.setImageResource(R.drawable.asset_star_active);
                popularity2IV.setImageResource(R.drawable.asset_star_inactive);
                popularity3IV.setImageResource(R.drawable.asset_star_inactive);
                popularity4IV.setImageResource(R.drawable.asset_star_inactive);
                popularity5IV.setImageResource(R.drawable.asset_star_inactive);
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
        allCityBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveToCityPage();
            }
        });

        closeIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        popularity1IV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handlePopularity(1);
            }
        });

        popularity2IV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handlePopularity(2);
            }
        });

        popularity3IV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handlePopularity(3);
            }
        });

        popularity4IV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handlePopularity(4);
            }
        });

        popularity5IV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handlePopularity(5);
            }
        });

        setFilterBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FilterSearch filter = new FilterSearch(maxCost, minCost, maxYears, minYears, minAge, maxAge, maidPopularity,cityList);
                filter.setPopularity(maidPopularity);
                filterSearches.clear();
                filterSearches.add(filter);
                Intent intent = new Intent();
                intent.putExtra(FILTERINTENT, (ArrayList<FilterSearch>) filterSearches);
                setResult(RESULT_OK, intent);
                finish();
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
