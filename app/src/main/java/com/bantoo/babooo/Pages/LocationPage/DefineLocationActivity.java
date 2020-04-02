package com.bantoo.babooo.Pages.LocationPage;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bantoo.babooo.R;
import com.bantoo.babooo.Utilities.BaseActivity;

public class DefineLocationActivity extends BaseActivity {

    ImageView closeLocationIV;
    EditText searchLocationET;
    Button searchLocationBTN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_define_location);

        closeLocationIV = findViewById(R.id.close_location_IV);
        searchLocationET = findViewById(R.id.search_location_ET);
        searchLocationBTN = findViewById(R.id.search_loaction_BTN);

        handleAction();
    }

    private void handleAction(){
        closeLocationIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        searchLocationBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Search location
            }
        });
    }

}
