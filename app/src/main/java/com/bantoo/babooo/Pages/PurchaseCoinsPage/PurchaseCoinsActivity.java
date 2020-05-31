package com.bantoo.babooo.Pages.PurchaseCoinsPage;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.ConsumeResponseListener;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;
import com.bantoo.babooo.R;
import com.bantoo.babooo.Utilities.BaseActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PurchaseCoinsActivity extends BaseActivity implements PurchasesUpdatedListener, InAppPurchaseClickListener {

    private static final String TAG = "PurchaseCoins";
    private BillingClient billingClient;
    private List<String> skuList = new ArrayList<String>();

    private RecyclerView inAppRV;
    private TextView coinsTV;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference userReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_coins);

        initView();
        initFirebase();
        showCurrentCoins();
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

    private void initFirebase() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        SharedPreferences accountData = getSharedPreferences("accountData", MODE_PRIVATE);
        String uid = accountData.getString("uid", "");
        userReference = firebaseDatabase.getReference().child("Users").child(uid);
    }

    private void showCurrentCoins() {
        userReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                coinsTV.setText(dataSnapshot.child("coins").getValue() + " coin");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void initView() {
        inAppRV = findViewById(R.id.in_app_purchase_RV);
        coinsTV = findViewById(R.id.coins_TV_purchase);
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
                    loadProduct();
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

    private void loadProduct() {
        if(billingClient.isReady()) {
            SkuDetailsParams params = new SkuDetailsParams().newBuilder()
                    .setSkusList(skuList)
                    .setType(BillingClient.SkuType.INAPP)
                    .build();
            billingClient.querySkuDetailsAsync(params, new SkuDetailsResponseListener() {
                @Override
                public void onSkuDetailsResponse(int responseCode, List<SkuDetails> skuDetailsList) {
                    if(responseCode == BillingClient.BillingResponse.OK) {
                        Log.d(TAG, "onSkuDetailsResponse: querySkuDetailsAsync, responseCode: "+responseCode);
                        Log.d(TAG, "onSkuDetailsResponse: skudetailList: "+skuDetailsList);
                        configureRecyclerView(skuDetailsList);
                    } else {
                        Log.d(TAG, "onSkuDetailsResponse: Can't querySkuDetailsAsync, responseCode: $responseCode");
                    }
                }
            });
        } else {
            Log.d(TAG, "productClicked: Billing Client not ready");
        }
    }

    private void configureRecyclerView(List<SkuDetails> skuDetailsList) {
        Log.d(TAG, "configureRecyclerView: count skudetail: "+skuDetailsList.size());
        InAppPurchaseAdapter inAppPurchaseAdapter = new InAppPurchaseAdapter(this, skuDetailsList, this);
        inAppRV.setLayoutManager(new LinearLayoutManager(PurchaseCoinsActivity.this,LinearLayoutManager.VERTICAL,false));
        inAppRV.setAdapter(inAppPurchaseAdapter);
    }

    @Override
    public void onPurchasesUpdated(int responseCode, @Nullable List<Purchase> purchases) {
        Log.d(TAG, "onPurchasesUpdated: "+responseCode);
        if (responseCode == 0) {
            int purchasedCoins = 0;
            if (purchases.get(purchases.size() - 1).getSku().equals("coins_100")) {
                purchasedCoins = 100;
            }
            topupCoins(purchasedCoins);
            allowMultiplePurchases(purchases);
        }
    }

    private void topupCoins(int purchaseCoins) {
        userReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int currentCoins = Integer.parseInt(dataSnapshot.child("coins").getValue().toString());
                dataSnapshot.child("coins").getRef().setValue(currentCoins + purchaseCoins);
                showCurrentCoins();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void allowMultiplePurchases(List<Purchase> purchases) {
        Purchase purchase = purchases.get(0);
        if(purchase != null) {
            billingClient.consumeAsync(purchase.getPurchaseToken(), new ConsumeResponseListener() {
                @Override
                public void onConsumeResponse(int responseCode, String purchaseToken) {
                    if(responseCode == BillingClient.BillingResponse.OK  && purchaseToken != null) {
                        Log.d(TAG, "onConsumeResponse: AllowMultiplePurchases success, responseCode:"+responseCode);
                    } else {
                        Log.d(TAG, "onConsumeResponse: Can't allowMultiplePurchases, responseCode"+responseCode);
                    }
                }
            });
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void onItemClick(int position) {
        Log.d(TAG, "onItemClick: Buying = "+skuList.get(position));
        SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
        params.setSkusList(skuList).setType(BillingClient.SkuType.INAPP);
        billingClient.querySkuDetailsAsync(params.build(), new SkuDetailsResponseListener() {
            @Override
            public void onSkuDetailsResponse(int responseCode, List<SkuDetails> skuDetailsList) {
                BillingFlowParams flowParams = BillingFlowParams.newBuilder()
                        .setSkuDetails(skuDetailsList.get(position))
                        .build();
                Log.d(TAG, "onSkuDetailsResponse: skudetaillist: "+skuDetailsList.get(position));
                int itemResponseCode = billingClient.launchBillingFlow(PurchaseCoinsActivity.this ,flowParams);
            }
        });
    }
}
