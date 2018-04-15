package com.anvil.adsama.nsaw.services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.anvil.adsama.nsaw.R;
import com.anvil.adsama.nsaw.activities.MainActivity;

public class NotificationReceiver extends BroadcastReceiver {

    private static final String LOG_TAG = NotificationReceiver.class.getSimpleName();
    private static final String CHANNEL_ID = "com.anvil.adsama.nsaw";
    NotificationCompat.Builder mBuilder;

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Log.i(LOG_TAG, "Broadcast received: " + action);
        if (action != null) {
            if (action.equals("notification.broadcast")) {
                if (intent.getExtras() != null) {
                    String news = intent.getExtras().getString("news_extra");
                    String stock = intent.getExtras().getString("stock_extra");
                    float weather = intent.getExtras().getFloat("weather_extra");
                    checkOreo(context);
                    mBuilder = new NotificationCompat.Builder(context, CHANNEL_ID)
                            .setSmallIcon(R.drawable.ic_lightning)
                            .setContentTitle(news)
                            .setContentText(stock + "\n" + weather + " \u2103")
                            .setStyle(new NotificationCompat.BigTextStyle())
                            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                            .setContentIntent(sendOpenIntent(context))
                            .setAutoCancel(true);
                    NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
                    notificationManager.notify(3, mBuilder.build());
                }
            }
        }
    }

    private void checkOreo(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "NSAW";
            String description = "Default";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            if (notificationManager != null)
                notificationManager.createNotificationChannel(channel);
        }
    }

    private PendingIntent sendOpenIntent(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        return PendingIntent.getActivity(context, 0, intent, 0);
    }
}