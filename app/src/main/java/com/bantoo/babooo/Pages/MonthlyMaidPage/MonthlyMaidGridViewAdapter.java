package com.bantoo.babooo.Pages.MonthlyMaidPage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bantoo.babooo.Model.Maid;
import com.bantoo.babooo.R;

import java.util.List;

public class MonthlyMaidGridViewAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater layoutInflater;
    private List<Maid> maidList;

    public MonthlyMaidGridViewAdapter(Context context, List<Maid> maidList){
        this.context = context;
        this.maidList = maidList;
    }

    @Override
    public int getCount() {
        return maidList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ImageView maidImage,star1,star2,star3,star4,star5;
        TextView maidName,maidCoins;

        if(layoutInflater == null){
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        if(convertView == null){
            convertView = layoutInflater.inflate(R.layout.grid_view_monthly_maid,null);
        }

        String name = maidList.get(position).name;
        int coins = maidList.get(position).cost;
        int rating = maidList.get(position).rating;


        maidImage = convertView.findViewById(R.id.image_maid_IV);
        maidName = convertView.findViewById(R.id.name_maid_TV);
        maidCoins = convertView.findViewById(R.id.coins_maid_TV);
        star1 = convertView.findViewById(R.id.star_rating1);
        star2 = convertView.findViewById(R.id.star_rating2);
        star3 = convertView.findViewById(R.id.star_rating3);
        star4 = convertView.findViewById(R.id.star_rating4);
        star5 = convertView.findViewById(R.id.star_rating5);

        maidName.setText(name);
        maidCoins.setText(String.valueOf(coins));

        switch (rating){
            case 5:
                star1.setImageResource(R.drawable.asset_star_active);
                star2.setImageResource(R.drawable.asset_star_active);
                star3.setImageResource(R.drawable.asset_star_active);
                star4.setImageResource(R.drawable.asset_star_active);
                star5.setImageResource(R.drawable.asset_star_active);
                break;
            case 4:
                star1.setImageResource(R.drawable.asset_star_active);
                star2.setImageResource(R.drawable.asset_star_active);
                star3.setImageResource(R.drawable.asset_star_active);
                star4.setImageResource(R.drawable.asset_star_active);
                star5.setImageResource(R.drawable.asset_star_inactive);
                break;
            case 3:
                star1.setImageResource(R.drawable.asset_star_active);
                star2.setImageResource(R.drawable.asset_star_active);
                star3.setImageResource(R.drawable.asset_star_active);
                star4.setImageResource(R.drawable.asset_star_inactive);
                star5.setImageResource(R.drawable.asset_star_inactive);
                break;
            case 2:
                star1.setImageResource(R.drawable.asset_star_active);
                star2.setImageResource(R.drawable.asset_star_active);
                star3.setImageResource(R.drawable.asset_star_inactive);
                star4.setImageResource(R.drawable.asset_star_inactive);
                star5.setImageResource(R.drawable.asset_star_inactive);
                break;
            case 1:
                star1.setImageResource(R.drawable.asset_star_active);
                star2.setImageResource(R.drawable.asset_star_inactive);
                star3.setImageResource(R.drawable.asset_star_inactive);
                star4.setImageResource(R.drawable.asset_star_inactive);
                star5.setImageResource(R.drawable.asset_star_inactive);
                break;
            default: break;
        }

        return convertView;
    }
}
