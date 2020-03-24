package com.bantoo.babooo.Pages.SignUpPage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bantoo.babooo.Pages.SignUpPage.Fragment.ApprCodePage;
import com.bantoo.babooo.Pages.SignUpPage.Fragment.EmailPage;
import com.bantoo.babooo.Pages.SignUpPage.Fragment.LocationPage;
import com.bantoo.babooo.Pages.SignUpPage.Fragment.NamePage;
import com.bantoo.babooo.Pages.SignUpPage.Fragment.PasswordPage;
import com.bantoo.babooo.Pages.SignUpPage.Fragment.PhonePage;
import com.bantoo.babooo.Pages.VerificationPage.VerificationActivity;
import com.bantoo.babooo.R;
import com.layer_net.stepindicator.StepIndicator;

import java.util.ArrayList;
import java.util.List;

public class SignUpFormActivity extends AppCompatActivity {

    public static ViewPager pager;

    private TextView role;
    private PagerAdapter pagerAdapter;
    private StepIndicator stepIndicator;

    private String userRole;
    public List<Fragment> list = new ArrayList<>();
    public Handler handler = new Handler();

    LinearLayout errorValidation;
    ImageView prevBtn, nextBtn, finishBtn;
    TextView errorMessage;
    Animation animationDown, animationUp;

    String name, email, password, alamat, phone, apprCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_form);
        generalStyling();
        setupPage();
        initVar();
        handleUserRole();
        handleButton();
    }

    public void generalStyling() {
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.orangePrimary));
        window.setNavigationBarColor(ContextCompat.getColor(getApplicationContext(), R.color.greenPrimary));
    }

    public void initVar() {
        prevBtn = findViewById(R.id.prevBtn);
        nextBtn = findViewById(R.id.nextBtn);
        finishBtn = findViewById(R.id.finishBtn);
        role = findViewById(R.id.roleTV);
        errorValidation = findViewById(R.id.validation_sign_up);
        errorMessage = findViewById(R.id.validation_sign_up_TV);
        stepIndicator = findViewById(R.id.stepIndicatorSignUp);
        stepIndicator.setClickable(false);
        stepIndicator.setupWithViewPager(pager);
        animationDown = AnimationUtils.loadAnimation(this, R.anim.slide_down);
        animationUp = AnimationUtils.loadAnimation(this, R.anim.slide_up);
    }

    public void handleButton() {
        prevBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int index = pager.getCurrentItem();
                pager.setCurrentItem(index - 1);

                finishBtn.setVisibility(View.INVISIBLE);
                nextBtn.setVisibility(View.VISIBLE);

                if (index == 0) {
                    finish();
                }
            }
        });
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int index = pager.getCurrentItem();
                pager.setCurrentItem(index + 1);

                if (index == list.size() - 2) {
                    finishBtn.setVisibility(View.VISIBLE);
                    nextBtn.setVisibility(View.INVISIBLE);
                }
            }
        });
        finishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleUserData();
            }
        });
    }

    public void handleUserRole() {

        SharedPreferences sharedPreferences = getSharedPreferences("userPref", Context.MODE_PRIVATE);
        userRole = sharedPreferences.getString("role", "Pengguna");

        if (userRole.equals("pengguna")) {
            role.setText("Anda mendaftar sebagai : Pengguna Layanan");
        } else if (userRole.equals("mitra")) {
            role.setText("Anda mendaftar sebagai : Mitra ART");
        } else {
            role.setText("");
        }
    }

    private void setupPage() {

        SharedPreferences sharedPreferences = getSharedPreferences("userPref", Context.MODE_PRIVATE);
        userRole = sharedPreferences.getString("role", "Pengguna");

        if (userRole.equals("pengguna")) {
            list.add(new NamePage());
            list.add(new EmailPage());
            list.add(new PasswordPage());
            list.add(new LocationPage());
            list.add(new PhonePage());
        } else if (userRole.equals("mitra")) {
            list.add(new ApprCodePage());
            list.add(new NamePage());
            list.add(new EmailPage());
            list.add(new PasswordPage());
            list.add(new LocationPage());
            list.add(new PhonePage());
        }

        pager = findViewById(R.id.pager);
        pagerAdapter = new SlidePagerAdapter(getSupportFragmentManager(), list);

        pager.setAdapter(pagerAdapter);
    }

    private void handleUserData() {
        SharedPreferences sharedPreferences = getSharedPreferences("userPref", Context.MODE_PRIVATE);

        name = sharedPreferences.getString("name", "N/A");
        email = sharedPreferences.getString("email", "N/A");
        password = sharedPreferences.getString("password", "N/A");
        alamat = sharedPreferences.getString("location", "N/A");
        phone = sharedPreferences.getString("phone", "N/A");
        apprCode = sharedPreferences.getString("apprCode", "N/A");

        checkUserData();
    }

    private void checkUserData() {

        SharedPreferences userSharePref = getSharedPreferences("userPref", Context.MODE_PRIVATE);
        userRole = userSharePref.getString("role", "N/A");

        SharedPreferences fromSharePref = getSharedPreferences("verificationPage", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = fromSharePref.edit();
        editor.putString("from", "register").commit();

        if (userRole.equals("pengguna")) {
            if (name.equals("N/A")) {
                displayError("Nama belum diisi");
            } else if (email.equals("N/A")) {
                displayError("Email belum diisi");
            } else if (password.equals("N/A")) {
                displayError("Cek kembali password anda");
            } else if (alamat.equals("N/A")) {
                displayError("Alamat belum diisi");
            } else if (phone.equals("N/A")) {
                displayError("Nomor handphone belum diisi");
            } else {
                moveToVerification();
            }
        } else if (userRole.equals("mitra")) {
            if (apprCode.equals("N/A")) {
                displayError("Periksa kembali approval code anda");
            } else if (name.equals("N/A")) {
                displayError("Nama belum diisi");
            } else if (email.equals("N/A")) {
                displayError("Email belum diisi");
            } else if (password.equals("N/A")) {
                displayError("Cek kembali password anda");
            } else if (alamat.equals("N/A")) {
                displayError("Alamat belum diisi");
            } else if (phone.equals("N/A")) {
                displayError("Nomor handphone belum diisi");
            } else if (apprCode.equals("N/A")) {
                displayError("Periksa kembali approval code anda");
            } else {
                moveToVerification();
            }
        }
    }

    private void moveToVerification(){
        Intent intent = new Intent(SignUpFormActivity.this, VerificationActivity.class);
        startActivity(intent);
    }

    public void displayError(String message) {
        errorValidation.setVisibility(View.VISIBLE);
        errorValidation.startAnimation(animationDown);
        errorMessage.setText(message);
        handler.postDelayed(removeError, 3500);
    }

    public Runnable removeError = new Runnable() {
        @Override
        public void run() {
            if (errorValidation.getVisibility() == View.VISIBLE) {
                errorValidation.startAnimation(animationUp);
                errorValidation.setVisibility(View.GONE);
            }
        }
    };

}
