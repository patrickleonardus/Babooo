package com.bantoo.babooo.Controller.SignUpActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.transition.Slide;
import android.view.Gravity;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.bantoo.babooo.R;


public class SignUpActivity extends AppCompatActivity {

    ImageButton nextButton;
    View penggunaView, mitraView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        final SharedPreferences sharedPreferences = getSharedPreferences("userPref", Context.MODE_PRIVATE);
        penggunaView = findViewById(R.id.penggunaView);
        mitraView = findViewById(R.id.mitraView);

        setAnimation();
        nextButton = findViewById(R.id.nextButton_nama);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity();
            }
        });
        penggunaView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("role", "pengguna");
                editor.apply();
            }
        });
        mitraView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("role", "mitra");
                editor.apply();
            }
        });
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
        Intent i = new Intent(this, NamaSignUpActivity.class);
        if(Build.VERSION.SDK_INT>20){
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this);
            startActivity(i,options.toBundle());
        }else {
            startActivity(i);
        }
    }

}
