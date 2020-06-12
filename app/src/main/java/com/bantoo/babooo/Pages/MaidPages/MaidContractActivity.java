package com.bantoo.babooo.Pages.MaidPages;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bantoo.babooo.R;

public class MaidContractActivity extends AppCompatActivity {

    private ImageView closeIV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maid_contract);

        initView();
        handleAction();
    }

    private void handleAction(){
        closeIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initView(){
        closeIV = findViewById(R.id.close_contract_IV);
    }

}
