package com.bantoo.babooo.Pages.MonthlyServicePage.FilterPage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bantoo.babooo.Model.City;
import com.bantoo.babooo.R;

import java.util.ArrayList;
import java.util.List;

public class CityFilterRecyclerViewAdapter extends RecyclerView.Adapter<CityFilterRecyclerViewAdapter.MyViewHolder> {

    private Context context;
    private List<City> cityList = new ArrayList<City>();
    private CityFilterOnItemClickListener cityOnItemClickListener;

    public CityFilterRecyclerViewAdapter(Context context, List<City> cityList, CityFilterOnItemClickListener cityOnItemClickListener) {
        this.context = context;
        this.cityList = cityList;
        this.cityOnItemClickListener = cityOnItemClickListener;
    }

    @Override
    public CityFilterRecyclerViewAdapter.MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.recycler_view_city_filter, viewGroup, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final CityFilterRecyclerViewAdapter.MyViewHolder viewHolder, int i) {
        final int pos = viewHolder.getAdapterPosition();

        viewHolder.cityName.setText(cityList.get(i).getCityName());
        viewHolder.closeCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cityOnItemClickListener.cityClose(pos);
            }
        });

        if(viewHolder.cityName.getText().toString().equals("Semua Kota")){
            viewHolder.closeCity.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return cityList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView cityName;
        ImageView closeCity;

        public MyViewHolder(View view) {
            super(view);

            cityName = view.findViewById(R.id.city_choosen_TV);
            closeCity = view.findViewById(R.id.close_cityChoosen_IV);
        }
    }
}
