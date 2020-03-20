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
import android.widget.EditText;
import android.widget.ImageButton;

import com.bantoo.babooo.R;

public class EmailSignUpActivity extends AppCompatActivity {

    ImageButton prevButton, nextButton;
    EditText emailET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_sign_up);

        prevButton = findViewById(R.id.prevButton_email);
        nextButton = findViewById(R.id.nextButton_email);
        emailET = findViewById(R.id.emailET);

        final SharedPreferences sharedPreferences = getSharedPreferences("userPref", Context.MODE_PRIVATE);
        emailET.setText(sharedPreferences.getString("email", ""));

        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("email", emailET.getText().toString());
                editor.apply();
                //move to next activity
                setAnimation(Gravity.LEFT);
                startActivity();
            }
        });
    }

    public void startActivity(){
        Intent i = new Intent(this, PasswordSignUpActivity.class);
        if(Build.VERSION.SDK_INT>20){
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this);
            startActivity(i,options.toBundle());
        }else {
            startActivity(i);
        }
    }

    public void setAnimation(int gravity) {
        if (Build.VERSION.SDK_INT > 20) {
            Slide slide = new Slide();
            slide.setSlideEdge(gravity);
            slide.setDuration(400);
            slide.setInterpolator(new DecelerateInterpolator());
            getWindow().setExitTransition(slide);
            getWindow().setEnterTransition(slide);
        }
    }
}
