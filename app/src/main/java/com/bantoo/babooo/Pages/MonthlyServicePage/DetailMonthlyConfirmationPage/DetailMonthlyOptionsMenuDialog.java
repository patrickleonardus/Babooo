package com.bantoo.babooo.Pages.MonthlyServicePage.DetailMonthlyConfirmationPage;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import com.bantoo.babooo.Pages.MonthlyServicePage.ExtendContractReminderPage.ExtendContractReminderActivity;
import com.bantoo.babooo.Pages.MonthlyServicePage.SalaryConfirmationPage.SalaryConfirmationActivity;
import com.bantoo.babooo.Pages.UserDetailPage.MaidDetailActivity;
import com.bantoo.babooo.R;

public class DetailMonthlyOptionsMenuDialog extends Dialog {

    LinearLayout seeProfileLayout, incomeConfirmationLayout, extendContractLayout;
    String maidUniqueKey, rentUniqueKey;
    boolean showExtendedContract;
    Context context;

    public DetailMonthlyOptionsMenuDialog(@NonNull Context context, String maidUniqueKey, boolean showExtendedContract, String rentUniqueKey) {
        super(context);
        this.context = context;
        this.maidUniqueKey = maidUniqueKey;
        this.showExtendedContract = showExtendedContract;
        this.rentUniqueKey = rentUniqueKey;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.detail_monthly_options_menu_dialog);

        seeProfileLayout = findViewById(R.id.lihat_profile_mitra_monthly_options);
        incomeConfirmationLayout = findViewById(R.id.konfirmasi_gaji_mitra_monthly_options);
        extendContractLayout = findViewById(R.id.perpanjang_kontrak_mitra_monthly_options);
        if(!showExtendedContract) {
            extendContractLayout.setVisibility(View.GONE);
        }
        seeProfileLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent moveToProfile = new Intent(getContext(), MaidDetailActivity.class);
                moveToProfile.putExtra("maidUniqueKey", maidUniqueKey);
                moveToProfile.putExtra("sender", "optionsMenuDialog");
                context.startActivity(moveToProfile);
            }
        });
        incomeConfirmationLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent moveToSalaryConfirmation = new Intent(getContext(), SalaryConfirmationActivity.class);
                moveToSalaryConfirmation.putExtra("rentUniqueKey", rentUniqueKey);
                context.startActivity(moveToSalaryConfirmation);
            }
        });
        extendContractLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent moveToExtendContract = new Intent(getContext(), ExtendContractReminderActivity.class);
                moveToExtendContract.putExtra("rentUniqueKey", rentUniqueKey);
                context.startActivity(moveToExtendContract);
            }
        });
    }
}
