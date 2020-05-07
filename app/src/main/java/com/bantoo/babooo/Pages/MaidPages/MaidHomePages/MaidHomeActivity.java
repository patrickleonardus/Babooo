package com.bantoo.babooo.Pages.MaidPages.MaidHomePages;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.bantoo.babooo.R;
import com.bantoo.babooo.Utilities.BaseActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MaidHomeActivity extends BaseActivity {
    private BottomNavigationView navbar;
    private FrameLayout mainFrame;

    private MaidIncomeFragment maidIncomeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maid_home);

        mainFrame = findViewById(R.id.mainFrame);
        navbar = findViewById(R.id.menuItem);

        fragmentSetup();
    }

    private void fragmentSetup() {
        maidIncomeFragment = new MaidIncomeFragment();
        setFragment(maidIncomeFragment);
        navbar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.pendapatan_item:
                        setFragment(maidIncomeFragment);
                        return true;
                    case R.id.pesanan_item:
                        return true;
                    case R.id.profile_item:
                        return true;
                    default:
                        return false;
                }
            }
        });
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.mainFrame, fragment);
        fragmentTransaction.commit();
    }
}
