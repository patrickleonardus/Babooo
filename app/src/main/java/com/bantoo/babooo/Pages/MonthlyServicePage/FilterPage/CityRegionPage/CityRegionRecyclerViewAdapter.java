package com.bantoo.babooo.Pages.MonthlyServicePage.FilterPage.CityRegionPage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bantoo.babooo.Model.City;
import com.bantoo.babooo.R;

import java.util.List;

public class CityRegionRecyclerViewAdapter extends RecyclerView.Adapter<CityRegionRecyclerViewAdapter.MyViewHolder> {

    Context context;
    List<City> cityList;
    CityRegionOnClickListener onClickListener;

    public CityRegionRecyclerViewAdapter(Context context, List<City> cityList, CityRegionOnClickListener onClickListener) {
        this.context = context;
        this.cityList = cityList;
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_city_region,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        boolean checked;
        checked = cityList.get(position).isChecked();

        holder.cityName.setText(cityList.get(position).getCityName());

        if(checked){
            holder.cityCheckbox.setChecked(true);
        }
        else {
            holder.cityCheckbox.setChecked(false);
        }
    }

    @Override
    public int getItemCount() {return cityList.size();}


    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView cityName;
        CheckBox cityCheckbox;

        public MyViewHolder(@NonNull final View itemView) {
            super(itemView);
            cityName = itemView.findViewById(R.id.cityName_recyclerView_TV);
            cityCheckbox = itemView.findViewById(R.id.city_recyclerView_CB);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    onClickListener.onCityClick(position);

                    if(cityCheckbox.isChecked()){
                        cityCheckbox.setChecked(false);
                    }
                    else{
                        cityCheckbox.setChecked(true);
                    }

                }
            });
        }
    }

}
