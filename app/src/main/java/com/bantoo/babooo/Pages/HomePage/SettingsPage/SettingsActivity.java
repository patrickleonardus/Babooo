package com.bantoo.babooo.Pages.HomePage.SettingsPage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;

import com.bantoo.babooo.Pages.LoginPage.LoginActivity;
import com.bantoo.babooo.R;
import com.bantoo.babooo.Utilities.BaseActivity;
import com.google.firebase.auth.FirebaseAuth;

import org.checkerframework.checker.linear.qual.Linear;

import java.util.ArrayList;
import java.util.List;

public class SettingsActivity extends BaseActivity {

    ImageView backBtn;
    Button signoutBtn;
    Switch notifSwitch;
    RelativeLayout faqRL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        backBtn = findViewById(R.id.close_setting_IV);
        signoutBtn = findViewById(R.id.signout_btn);
        notifSwitch = findViewById(R.id.notifSwitch);
        faqRL = findViewById(R.id.faqRL);

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("accountData", MODE_PRIVATE);
        notifSwitch.setChecked(sharedPreferences.getBoolean("notifOn", true));
        handleAction();
    }

    private void handleAction(){
        faqRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(getString(R.string.base_url)+"faq.html"));
                startActivity(intent);
            }
        });
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        signoutBtn.setOnClickListener(v -> {
            SharedPreferences accountData = getApplicationContext().getSharedPreferences("accountData", MODE_PRIVATE);
            accountData.edit().clear().commit();
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(SettingsActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
        notifSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("accountData", MODE_PRIVATE);
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                    SettingsActivity.this);
            if(isChecked) {
                alertDialogBuilder.setTitle("Apakah anda ingin menyalakan notifikasi?");
                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                sharedPreferences.edit().putBoolean("notifOn", true);
                            }
                        })
                        .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
            } else {
                alertDialogBuilder.setTitle("Apakah anda ingin menyalakan notifikasi?");
                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                sharedPreferences.edit().putBoolean("notifOn", false);
                            }
                        })
                        .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
            }
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        });
    }

}
