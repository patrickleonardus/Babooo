package com.bantoo.babooo.Pages.HomePage;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.bantoo.babooo.Pages.HomePage.ProfilePage.ProfileFragment;
import com.bantoo.babooo.Pages.HomePage.OrderPage.OrdersFragment;
import com.bantoo.babooo.Pages.HomePage.ServicePage.ServiceFragment;
import com.bantoo.babooo.R;
import com.bantoo.babooo.Utilities.BaseActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

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

        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(HomeActivity.this, new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                String token = instanceIdResult.getToken();
                Log.i("FCM Token", token);
                SharedPreferences accountDataSharedPreferences = getApplicationContext().getSharedPreferences("accountData", MODE_PRIVATE);
                String uid = accountDataSharedPreferences.getString("uid", "");
                Log.d("ON NEW TOKEN", "onNewToken: created new token");
                DatabaseReference reference;
                if(accountDataSharedPreferences.getString("role", "").equals("art")) {
                    reference = FirebaseDatabase.getInstance().getReference("ART").child(uid);
                } else if(accountDataSharedPreferences.getString("role", "").equals("artBulanan")) {
                    reference = FirebaseDatabase.getInstance().getReference("ARTBulanan").child(uid);
                } else {
                    reference = FirebaseDatabase.getInstance().getReference("Users").child(uid);
                }
                reference.child("token").setValue(token);
            }
        });

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
