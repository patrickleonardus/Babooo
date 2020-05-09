package com.bantoo.babooo.Pages.SignUpPage;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.bantoo.babooo.R;
import com.bantoo.babooo.Utilities.BaseActivity;


public class SignUpRoleActivity extends BaseActivity {

    RelativeLayout penggunaView, mitraView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_role);

        initVar();
        buttonHandler();

    }

    public void initVar() {
        penggunaView = findViewById(R.id.penggunaView);
        mitraView = findViewById(R.id.mitraView);
    }

    public void buttonHandler() {
        penggunaView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                resetSharedPref();

                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("accountData", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("role", "pengguna").commit();
                moveToRegister();
            }
        });
        mitraView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetSharedPref();

                showAlertForART();
            }
        });
    }

    private void showAlertForART() {
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
                .setPositiveButton("Lanjutkan Mendaftar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("accountData", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("role", "mitra").commit();
                        moveToRegister();
                    }
                })
                .setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void resetSharedPref() {
        //clear semua sharedPreferences
        SharedPreferences userPref = getSharedPreferences("accountData", Context.MODE_PRIVATE);
        SharedPreferences verifPref = getSharedPreferences("verificationPage", Context.MODE_PRIVATE);
        userPref.edit().clear().commit();
        verifPref.edit().clear().commit();
    }

    public void moveToRegister() {
        Intent i = new Intent(this, SignUpFormActivity.class);
        startActivity(i);
    }
}
