package com.bantoo.babooo.Pages.PurchaseCoinsPage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.billingclient.api.SkuDetails;
import com.bantoo.babooo.Pages.HomePage.OrderPage.OrderItemClickListener;
import com.bantoo.babooo.R;

import java.util.List;

public class InAppPurchaseAdapter extends RecyclerView.Adapter<InAppPurchaseAdapter.MyViewHolder> {

    private Context context;
    private List<SkuDetails> skuDetailsList;
    private InAppPurchaseClickListener inAppPurchaseClickListener;

    public InAppPurchaseAdapter(Context context, List<SkuDetails> skuDetailsList, InAppPurchaseClickListener inAppPurchaseClickListener) {
        this.context = context;
        this.skuDetailsList = skuDetailsList;
        this.inAppPurchaseClickListener = inAppPurchaseClickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cell_inapp_purchase, viewGroup, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.coinsTV.setText(skuDetailsList.get(position).getTitle());
        String price = skuDetailsList.get(position).getPrice().replace("IDR", "");
        holder.buyCoinsBtn.setText("Rp."+price);

    }

    @Override
    public int getItemCount() {
        return skuDetailsList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView coinsTV;
        Button buyCoinsBtn;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            coinsTV = itemView.findViewById(R.id.coins_TV);
            buyCoinsBtn = itemView.findViewById(R.id.buy_coins_Btn);

            buyCoinsBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    inAppPurchaseClickListener.onItemClick(position);
                }
            });
        }
    }
}
