package com.bantoo.babooo.Pages.HomePage.AccountPage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bantoo.babooo.Model.FirebaseHelper;
import com.bantoo.babooo.R;
import com.bantoo.babooo.Utilities.BaseActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditAccountActivity extends BaseActivity {

    private TextView usernameTV;
    private EditText noHandphoneET, addressET, emailET, passwordET;
    private Button saveBtn;
    private ImageView closeIV;

    /*private FirebaseDatabase firebaseDatabase;
    private DatabaseReference userReference;*/
    
    private String username, noHandphone, address, email, password, uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_account);

        initView();
        initVar();
        handleAction();
    }

    private void handleAction() {
        closeIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isDataValid()) {
                    FirebaseHelper firebaseHelper = new FirebaseHelper();
                    firebaseHelper.updateUserData(uid, noHandphone, noHandphoneET.getText().toString(),
                            addressET.getText().toString(), emailET.getText().toString(),
                            passwordET.getText().toString());
                    SharedPreferences accountData = getApplicationContext().getSharedPreferences("accountData", MODE_PRIVATE);
                    SharedPreferences.Editor editor = accountData.edit();
                    editor.putString("phoneNumber", noHandphoneET.getText().toString());
                    editor.apply();
                    finish();
                }
            }
        });
    }

    private boolean isDataValid() {
        if(!noHandphoneET.getText().toString().startsWith("08") || noHandphoneET.getText().toString().length() <=10) {
            Toast.makeText(this, "No Handphone tidak benar", Toast.LENGTH_SHORT).show();
            return false;
        } else if(!emailET.getText().toString().contains("@") || (!emailET.getText().toString().endsWith(".com") &&
            !emailET.getText().toString().endsWith(".co.id")) ) {
            Toast.makeText(this, "Email tidak benar", Toast.LENGTH_SHORT).show();
            return false;
        } else if(passwordET.getText().toString().length() < 6) {
            Toast.makeText(this, "Panjang password harus lebih dari 6 karakter", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void initVar() {
        username = getIntent().getStringExtra("username");
        noHandphone = getIntent().getStringExtra("phoneNumber");
        address = getIntent().getStringExtra("address");
        email = getIntent().getStringExtra("email");
        password = getIntent().getStringExtra("password");
        uid = getIntent().getStringExtra("uid");

        /*firebaseDatabase = FirebaseDatabase.getInstance();
        userReference = firebaseDatabase.getReference().child("Users");*/

        usernameTV.setText(username);
        noHandphoneET.setText(noHandphone);
        addressET.setText(address);
        emailET.setText(email);
        passwordET.setText(password);
    }

    private void initView() {
        usernameTV = findViewById(R.id.user_name_TV_edit);
        noHandphoneET = findViewById(R.id.phoneNumber_ET_edit_account);
        addressET = findViewById(R.id.address_ET_edit_account);
        emailET = findViewById(R.id.email_ET_edit_account);
        passwordET = findViewById(R.id.password_ET_edit_account);
        saveBtn = findViewById(R.id.saveButton_edit_account);
        closeIV = findViewById(R.id.close_edit_IV);
    }
}