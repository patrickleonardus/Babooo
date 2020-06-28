package com.bantoo.babooo.Pages.MonthlyServicePage.SortPage;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.bantoo.babooo.R;
import com.bantoo.babooo.Utilities.BaseActivity;

import java.util.ArrayList;
import java.util.List;

public class SortMaidActivity extends BaseActivity {

    ListView sortMaidLV;
    ImageView closeActionIV;

    private List<String> sortMenu = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sort_maid);

        sortMaidLV = findViewById(R.id.sort_maid_LV);
        closeActionIV = findViewById(R.id.close_sort_IV);

        sortMenuItem();
        setupSortLV();
        handleListViewClickListener();
        handleButton();
    }

    private void sortMenuItem() {
        sortMenu.add("Popularitas");
        sortMenu.add("Gaji Tinggi ke Rendah");
        sortMenu.add("Gaji Rendah ke Tinggi");
    }

    private void setupSortLV() {
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, sortMenu);
        sortMaidLV.setAdapter(adapter);
    }

    private void handleListViewClickListener() {
        sortMaidLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                sortMaidLV.setSelection(position);
                Intent intent = new Intent();
                if (sortMenu.get(position).equals("Gaji Tinggi ke Rendah")) {
                    intent.putExtra("sortBy", "salaryDescending");
                } else if (sortMenu.get(position).equals("Gaji Rendah ke Tinggi")) {
                    intent.putExtra("sortBy", "salaryAscending");
                }
                setResult(RESULT_OK, intent);
                finish();
                Toast.makeText(getApplicationContext(), sortMenu.get(position), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void handleButton() {
        closeActionIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
