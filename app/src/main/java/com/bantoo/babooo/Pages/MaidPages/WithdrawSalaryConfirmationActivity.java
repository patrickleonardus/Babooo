package com.bantoo.babooo.Pages.MaidPages;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bantoo.babooo.R;

public class WithdrawSalaryConfirmationActivity extends AppCompatActivity {

    TextView nomorPengajuanTV, tanggalPengajuanTV, coinsTV, nominalRupiahTV;
    EditText code1ET, code2ET, code3ET, code4ET, code5ET, code6ET;
    Button withdrawBtn;

    int coinsWithdraw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw_salary_confirmation);

        initView();
        showData();
        handleAction();
    }

    private void handleAction() {
        withdrawBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String verifCode = code1ET.getText().toString()+
                        code2ET.getText().toString()+
                        code3ET.getText().toString()+
                        code4ET.getText().toString()+
                        code5ET.getText().toString()+
                        code6ET.getText().toString();

            }
        });
    }

    private void showData() {
        coinsWithdraw = getIntent().getIntExtra("coinsWithdraw", 0);
        coinsTV.setText(coinsWithdraw+"");
        nominalRupiahTV.setText((coinsWithdraw*3000)+"");
    }

    private void initView() {
        nomorPengajuanTV = findViewById(R.id.nomor_pengajuan_TV);
        tanggalPengajuanTV = findViewById(R.id.tanggal_pengajuan_TV);
        coinsTV = findViewById(R.id.coins_TV);
        nominalRupiahTV = findViewById(R.id.rupiah_nominal_TV);
        code1ET = findViewById(R.id.code1_withdraw_ET);
        code2ET = findViewById(R.id.code2_withdraw_ET);
        code3ET = findViewById(R.id.code3_withdraw_ET);
        code4ET = findViewById(R.id.code4_withdraw_ET);
        code5ET = findViewById(R.id.code5_withdraw_ET);
        code6ET = findViewById(R.id.code6_withdraw_ET);
        withdrawBtn = findViewById(R.id.withdraw_coins_BTN);
    }
}
