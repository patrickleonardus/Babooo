package com.bantoo.babooo.Pages.LoginPage;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.bantoo.babooo.R;

public class LoginProgressButton {

    CardView cardView;
    ConstraintLayout constraintLayout;
    ProgressBar progressBar;
    TextView textView;

    LoginProgressButton(Context context, View view){
        cardView = view.findViewById(R.id.cardView);
        constraintLayout = view.findViewById(R.id.constraint_layout);
        progressBar = view.findViewById(R.id.progressBar);
        textView = view.findViewById(R.id.textViewButton);
    }

    void setTextButton(String text){
        textView.setText(text);
    }

    void buttonActivated(){
        progressBar.setVisibility(View.VISIBLE);
        textView.setText("Mohon tunggu...");
    }

    void buttonFinished(){
        constraintLayout.setBackgroundColor(cardView.getResources().getColor(R.color.greenPrimary));
        progressBar.setVisibility(View.GONE);
        textView.setText("Masuk");
    }


}
