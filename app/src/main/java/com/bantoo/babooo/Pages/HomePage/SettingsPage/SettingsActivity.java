package com.bantoo.babooo.Pages.HomePage.SettingsPage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import com.bantoo.babooo.R;

import org.checkerframework.checker.linear.qual.Linear;

import java.util.ArrayList;
import java.util.List;

public class SettingsActivity extends AppCompatActivity implements SettingsItemClickListener {

    RecyclerView settingsRV;
    LinearLayoutManager settingsLayoutManager;
    SettingsRecyclerViewAdapter settingsRecyclerViewAdapter;
    List<String> settingsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        settingsList = new ArrayList<String>();
        settingsList.add("Notifikasi");
        settingsList.add("Ketentuan Layanan");
        settingsList.add("Kebijakan Privasi");
        settingsRV = findViewById(R.id.settings_rv);
        settingsLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        settingsRecyclerViewAdapter = new SettingsRecyclerViewAdapter(this, settingsList, this);
        settingsRV.setLayoutManager(settingsLayoutManager);
        settingsRV.setAdapter(settingsRecyclerViewAdapter);
    }

    @Override
    public void onItemClick(int position) {

    }
}
