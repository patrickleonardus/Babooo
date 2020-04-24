package com.bantoo.babooo.Pages.PurchaseCoinsPage;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;
import com.bantoo.babooo.R;
import com.bantoo.babooo.Utilities.BaseActivity;

import java.util.ArrayList;
import java.util.List;

public class PurchaseCoinsActivity extends BaseActivity implements PurchasesUpdatedListener {

    Button purchaseBtn;

    private static final String TAG = "PurchaseCoins";
    private BillingClient billingClient;
    List<String> skuList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_coins);
        setupProductName();
        setupBillingClient();
        /*purchaseBtn = findViewById(R.id.purchaseButton);
        purchaseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productClicked();
            }
        });*/
    }

    private void setupProductName() {
        skuList.add("coins_100");
        /*skuList.add("200_coins");
        skuList.add("500_coins");
        skuList.add("800_coins");
        skuList.add("1200_coins");
        skuList.add("1600_coins");
        skuList.add("3000_coins");*/
    }

    private void setupBillingClient() {
        billingClient = BillingClient.newBuilder(this).setListener(this).build();
        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(int responseCode) {
                if (responseCode == BillingClient.BillingResponse.OK) {
                    Log.d(TAG, "onBillingSetupFinished: BILLING | startConnection | RESULT OK");
                } else {
                    Log.d(TAG, "onBillingSetupFinished: BILLING | startConnection | RESULT: "+responseCode);
                }
            }

            @Override
            public void onBillingServiceDisconnected() {
                Log.d(TAG, "onBillingServiceDisconnected: BILLING | startConnection | RESULT: $billingResponseCode");
            }
        });
    }

    private void productClicked() {
        if(billingClient.isReady()) {
            SkuDetailsParams params = new SkuDetailsParams().newBuilder()
                    .setSkusList(skuList)
                    .setType(BillingClient.SkuType.INAPP)
                    .build();
            billingClient.querySkuDetailsAsync(params, new SkuDetailsResponseListener() {
                @Override
                public void onSkuDetailsResponse(int responseCode, List<SkuDetails> skuDetailsList) {
                    if(responseCode == BillingClient.BillingResponse.OK) {
                        Log.d(TAG, "onSkuDetailsResponse: querySkuDetailsAsync, responseCode: $responseCode");
                    } else {
                        Log.d(TAG, "onSkuDetailsResponse: Can't querySkuDetailsAsync, responseCode: $responseCode");
                    }
                }
            });
        } else {
            Log.d(TAG, "productClicked: Billing Client not ready");
        }
    }

    @Override
    public void onPurchasesUpdated(int responseCode, @Nullable List<Purchase> purchases) {
        Log.d(TAG, "onPurchasesUpdated: "+responseCode);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
