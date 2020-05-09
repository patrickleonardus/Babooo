package com.bantoo.babooo.Pages.HomePage.SettingsPage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bantoo.babooo.R;
import com.bantoo.babooo.Utilities.BaseActivity;

import org.checkerframework.checker.linear.qual.Linear;

import java.util.ArrayList;
import java.util.List;

public class SettingsActivity extends BaseActivity {

    ImageView backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        backBtn = findViewById(R.id.close_setting_IV);


        handleAction();
    }

    private void handleAction(){
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}
