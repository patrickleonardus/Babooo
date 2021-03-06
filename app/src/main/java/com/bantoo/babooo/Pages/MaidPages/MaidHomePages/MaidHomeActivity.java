package com.bantoo.babooo.Pages.MaidPages.MaidHomePages;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.bantoo.babooo.Pages.HomePage.HomeActivity;
import com.bantoo.babooo.Pages.MaidPages.MaidHomePages.MaidIncomePage.MaidIncomeFragment;
import com.bantoo.babooo.Pages.MaidPages.MaidHomePages.NewOrderPage.NewOrderFragment;
import com.bantoo.babooo.Pages.MaidPages.MaidHomePages.ProfileMaidPage.ProfileFragment;
import com.bantoo.babooo.R;
import com.bantoo.babooo.Utilities.BaseActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

public class MaidHomeActivity extends BaseActivity {
    private BottomNavigationView navbar;
    private FrameLayout mainFrame;

    private MaidIncomeFragment maidIncomeFragment;
    private NewOrderFragment newOrderFragment;
    private ProfileFragment profileFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maid_home);

        mainFrame = findViewById(R.id.mainFrame);
        navbar = findViewById(R.id.menuItem);

        fragmentSetup();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void fragmentSetup() {
        maidIncomeFragment = new MaidIncomeFragment();
        newOrderFragment = new NewOrderFragment();
        profileFragment = new ProfileFragment();

        setFragment(maidIncomeFragment);
        navbar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.pendapatan_item:
                        setFragment(maidIncomeFragment);
                        return true;
                    case R.id.pesanan_item:
                        setFragment(newOrderFragment);
                        return true;
                    case R.id.profile_item:
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
