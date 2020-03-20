package com.bantoo.babooo.Controller.SignUpActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.transition.Slide;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

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
        setAnimation();

    }

    public void generalStyling(){
//        penggunaView.setBackgroundColor(Color.rgb(1,1,1));
    }

    public void initVar(){
        penggunaView = findViewById(R.id.penggunaView);
        mitraView = findViewById(R.id.mitraView);
    }

    public void buttonHandler(){
        final SharedPreferences sharedPreferences = getSharedPreferences("userPref", Context.MODE_PRIVATE);
        penggunaView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("role", "pengguna");
                editor.apply();
                startActivity();
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
                        final SharedPreferences sharedPreferences = getSharedPreferences("userPref", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("role", "mitra");
                        editor.apply();
                        startActivity();
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

    public void setAnimation() {
        if (Build.VERSION.SDK_INT > 20) {
            Slide slide = new Slide();
            slide.setSlideEdge(Gravity.LEFT);
            slide.setDuration(400);
            slide.setInterpolator(new DecelerateInterpolator());
            getWindow().setExitTransition(slide);
            getWindow().setEnterTransition(slide);
        }
    }

    public void startActivity(){
//        Intent i = new Intent(this, NamaSignUpActivity.class);
        Intent i = new Intent(this,SignUpFormActivity.class);
        if(Build.VERSION.SDK_INT>20){
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this);
            startActivity(i,options.toBundle());
        }else {
            startActivity(i);
        }
    }

}
