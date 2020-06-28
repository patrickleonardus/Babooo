package com.bantoo.babooo.Pages.HomePage;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.bantoo.babooo.Pages.HomePage.ProfilePage.ProfileFragment;
import com.bantoo.babooo.Pages.HomePage.OrderPage.OrdersFragment;
import com.bantoo.babooo.Pages.HomePage.ServicePage.ServiceFragment;
import com.bantoo.babooo.R;
import com.bantoo.babooo.Utilities.BaseActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends BaseActivity {
    private BottomNavigationView navbar;
    private FrameLayout mainFrame;

    private ServiceFragment serviceFragment;
    private OrdersFragment ordersFragment;
    private ProfileFragment profileFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mainFrame = findViewById(R.id.mainFrame);
        navbar = findViewById(R.id.menuItem);

        fragmentSetup();

    }

    //Fragment Handler
    //Below Code is only for fragment handler and setup

    private void fragmentSetup() {
        serviceFragment = new ServiceFragment();
        ordersFragment = new OrdersFragment();
        profileFragment = new ProfileFragment();
        if(getIntent().getStringExtra("sender") != null) {
            if(getIntent().getStringExtra("sender").equals("orderFragment")) {
                navbar.setSelectedItemId(R.id.ordersItem);
                setFragment(ordersFragment);
            }
        } else {
            setFragment(serviceFragment);
        }

        navbar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.serviceItem:
                        setFragment(serviceFragment);
                        return true;
                    case R.id.ordersItem:
                        setFragment(ordersFragment);
                        return true;
                    case R.id.accountItem:
                        setFragment(profileFragment);
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
