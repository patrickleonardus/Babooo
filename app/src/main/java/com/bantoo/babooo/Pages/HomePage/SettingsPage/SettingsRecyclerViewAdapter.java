package com.bantoo.babooo.Pages.HomePage.SettingsPage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bantoo.babooo.Pages.HomePage.ServicePage.ServiceRecyclerViewAdapter;
import com.bantoo.babooo.R;

import java.util.List;

public class SettingsRecyclerViewAdapter extends RecyclerView.Adapter<SettingsRecyclerViewAdapter.MyViewHolder> {

    private Context context;
    private List<String> settingsList;
    private SettingsItemClickListener itemClickListener;

    public SettingsRecyclerViewAdapter(Context context, List<String> settingsList, SettingsItemClickListener itemClickListener) {
        this.context = context;
        this.settingsList = settingsList;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public SettingsRecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.recycler_view_settings, viewGroup, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.textLabel.setText(settingsList.get(position));
    }

    @Override
    public int getItemCount() {
        return settingsList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView textLabel;

        public MyViewHolder (View view){
            super(view);

            textLabel = view.findViewById(R.id.textLabel);
        }
    }
}
