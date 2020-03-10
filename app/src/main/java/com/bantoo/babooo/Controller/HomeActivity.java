package com.bantoo.babooo.Controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.bantoo.babooo.Controller.HomeFragment.AccountFragment;
import com.bantoo.babooo.Controller.HomeFragment.OrdersFragment;
import com.bantoo.babooo.R;
import com.bantoo.babooo.Controller.HomeFragment.ServiceFragment;
import com.bantoo.babooo.Controller.HomeFragment.SubscriptionFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {
    private BottomNavigationView navbar;
    private FrameLayout mainFrame;

    private ServiceFragment serviceFragment;
    private SubscriptionFragment subscriptionFragment;
    private OrdersFragment ordersFragment;
    private AccountFragment accountFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mainFrame = findViewById(R.id.mainFrame);
        navbar = findViewById(R.id.menuItem);

        serviceFragment = new ServiceFragment();
        subscriptionFragment = new SubscriptionFragment();
        ordersFragment = new OrdersFragment();
        accountFragment = new AccountFragment();
        setFragment(serviceFragment);

        navbar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch(menuItem.getItemId()) {
                    case R.id.serviceItem:
                        setFragment(serviceFragment);
                        return true;
                    case R.id.subsItem:
                        setFragment(subscriptionFragment);
                        return true;
                    case R.id.ordersItem:
                        setFragment(ordersFragment);
                        return true;
                    case R.id.accountItem:
                        setFragment(accountFragment);
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
