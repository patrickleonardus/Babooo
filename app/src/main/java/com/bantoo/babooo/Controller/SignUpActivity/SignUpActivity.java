package com.bantoo.babooo.Controller.SignUpActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.transition.Slide;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.RelativeLayout;

import com.bantoo.babooo.R;


public class SignUpActivity extends AppCompatActivity {

    RelativeLayout penggunaView, mitraView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        generalStyling();
        initVar();
        buttonHandler();

    }

    public void generalStyling(){
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(getApplicationContext(),R.color.orangePrimary));
        window.setNavigationBarColor(ContextCompat.getColor(getApplicationContext(),R.color.greenPrimary));
    }

    public void initVar(){
        penggunaView = findViewById(R.id.penggunaView);
        mitraView = findViewById(R.id.mitraView);
    }

    public void buttonHandler(){
        penggunaView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("userPref", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("role", "pengguna").commit();
                moveToRegister();
            }
        });
        mitraView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               showAlertForART();
            }
        });
    }

    private void showAlertForART(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);
        alertDialogBuilder.setTitle("Anda akan mendaftar sebagai Mitra ART");
        alertDialogBuilder
                .setMessage("Perlu diketahui, untuk mendaftar sebagai mitra ART, " +
                        "anda perlu datang ke kantor bantoo untuk melakukan registrasi awal, " +
                        "setelah dilakukan proses registrasi, anda akan mendapatkan approval code " +
                        "yang dapat diinput saat melakukan registrasi di aplikasi sebagai mitra, " +
                        "untuk itu, pastikan anda sudah memiliki approval code sebelum melanjutkan " +
                        "registrasi.")
                .setCancelable(false)
                .setPositiveButton("Lanjutkan Mendaftar",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("userPref", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("role", "mitra").commit();
                        moveToRegister();
                    }
                })
                .setNegativeButton("Batal",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void moveToRegister(){
        Intent i = new Intent(this,SignUpFormActivity.class);
        startActivity(i);
    }
}
