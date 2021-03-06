package com.bantoo.babooo.Utilities;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.TaskStackBuilder;

import com.bantoo.babooo.Pages.DailyServicePage.DetailDailyConfirmationPage.DetailDailyConfirmationActivity;
import com.bantoo.babooo.Pages.DailyServicePage.OrderDone.OrderDoneActivity;
import com.bantoo.babooo.Pages.HomePage.HomeActivity;
import com.bantoo.babooo.Pages.LoginPage.LoginActivity;
import com.bantoo.babooo.Pages.MaidPages.MaidHomePages.MaidHomeActivity;
import com.bantoo.babooo.Pages.MonthlyServicePage.ExtendContractReminderPage.ExtendContractReminderActivity;
import com.bantoo.babooo.Pages.MonthlyServicePage.SalaryConfirmationPage.SalaryConfirmationActivity;
import com.bantoo.babooo.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("accountData", MODE_PRIVATE);
        Log.d("TAG", "onMessageReceived: getNotif");
        if(sharedPreferences.getBoolean("notifOn", true)) {
            Log.d("ON MESSAGE", "onMessageReceived: called");
            if (remoteMessage.getData().size() > 0) {
                Map<String, String> payload = remoteMessage.getData();
                showNotification(payload);
            }
        }
    }

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);

        SharedPreferences accountDataSharedPreferences = getApplicationContext().getSharedPreferences("accountData", MODE_PRIVATE);
        String uid = accountDataSharedPreferences.getString("uid", "");
        Log.d("ON NEW TOKEN", "onNewToken: created new token");
        DatabaseReference reference;
        if(accountDataSharedPreferences.getString("role", "").equals("art")) {
            reference = FirebaseDatabase.getInstance().getReference("ART").child(uid);
        } else if(accountDataSharedPreferences.getString("role", "").equals("artBulanan")) {
            reference = FirebaseDatabase.getInstance().getReference("ARTBulanan").child(uid);
        } else {
            reference = FirebaseDatabase.getInstance().getReference("Users").child(uid);
        }
        reference.child("token").setValue(s);
    }

    private void showNotification(Map<String, String> payload) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.drawable.group_2);
        builder.setContentTitle(payload.get("username"));
        builder.setContentText(payload.get("text"));


        String notificationType = payload.get("notificationType");
        Intent resultIntent = new Intent(this, HomeActivity.class);
        /*if(notificationType.equals("maidFound")) {
            String orderUniqueKey = payload.get("orderUniqueKey");
            resultIntent = new Intent(this, DetailDailyConfirmationActivity.class);
            resultIntent.putExtra("orderUniqueKey", orderUniqueKey);
        } else if (notificationType.equals("payMonthlyMaid")) {
            resultIntent = new Intent(this, SalaryConfirmationActivity.class);
        }*/
        if(notificationType != null) {
            if (notificationType.equals("orderDone")) {
                String orderUniqueKey = payload.get("orderID");
                resultIntent = new Intent(this, OrderDoneActivity.class);
                resultIntent.putExtra("orderUniqueKey", orderUniqueKey);
            } else if (notificationType.equals("artFound")) {
                String orderUniqueKey = payload.get("orderID");
                resultIntent = new Intent(this, DetailDailyConfirmationActivity.class);
                resultIntent.putExtra("orderUniqueKey", orderUniqueKey);
            } else if (notificationType.equals("extendMonthlyMaidReminder")) {
                String rentUniqueKey = payload.get("rentID");
                resultIntent = new Intent(this, ExtendContractReminderActivity.class);
                resultIntent.putExtra("rentUniqueKey", rentUniqueKey);
            } else if (notificationType.equals("payMonthlyMaidSalary")) {
                String rentUniqueKey = payload.get("rentID");
                resultIntent = new Intent(this, SalaryConfirmationActivity.class);
                resultIntent.putExtra("rentUniqueKey", rentUniqueKey);
            } else if (notificationType.equals("requestDailyOrder")) {
                //app ART kalo ada yang order daily
                String orderUniqueKey = payload.get("orderID");
                resultIntent = new Intent(this, MaidHomeActivity.class);
                //resultIntent.putExtra("orderID")
            } else if (notificationType.equals("requestMonthlyOrder")) {
                //app ART kalo ada yg order monthly
                String orderUniqueKey = payload.get("orderID");
                resultIntent = new Intent(this, MaidHomeActivity.class);
            }
        }

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(resultPendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            String channelId = "Your_channel_id";
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
            builder.setChannelId(channelId);
        }
        notificationManager.notify(0, builder.build());
    }
}
