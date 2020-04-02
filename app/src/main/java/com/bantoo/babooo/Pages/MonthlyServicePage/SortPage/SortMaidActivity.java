package com.bantoo.babooo.Pages.MonthlyServicePage.SortPage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.bantoo.babooo.R;
import com.bantoo.babooo.Utils.BaseActivity;

import java.util.ArrayList;
import java.util.List;

public class SortMaidActivity extends BaseActivity {

    ListView sortMaidLV;
    ImageView closeAction;

    List<String> sortMenu = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sort_maid);

        sortMaidLV = findViewById(R.id.sort_maid_LV);
        closeAction = findViewById(R.id.close_sort_IV);

        sortMenuItem();
        setupSortLV();
        handleListViewClickListener();
        handleButton();
    }

    private void sortMenuItem() {
        sortMenu.add("Popularitas");
        sortMenu.add("Coin Tinggi ke Rendah");
        sortMenu.add("Coin Rendah ke Tinggi");
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
                finish();
                Toast.makeText(getApplicationContext(), sortMenu.get(position), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void handleButton() {
        closeAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
