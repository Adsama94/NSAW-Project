package com.anvil.adsama.nsaw.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

public class ConnectivityReceiver extends BroadcastReceiver {

    private static final String LOG_TAG = ConnectivityReceiver.class.getSimpleName();
    ConnectivityManager connManager;
    NetworkInfo networkInfo;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(LOG_TAG, "onReceive Triggered");
        intent.getAction();
        if (intent.getExtras() != null) {
            connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connManager != null)
                networkInfo = connManager.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnectedOrConnecting()) {
                Log.i(LOG_TAG, "Network " + networkInfo.getTypeName() + " connected");
                Toast.makeText(context, "INTERNET CONNECTED", Toast.LENGTH_SHORT).show();
            } else if (intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, Boolean.FALSE)) {
                Log.d(LOG_TAG, "No connectivity!");
                Toast.makeText(context, "INTERNET DISCONNECTED", Toast.LENGTH_SHORT).show();
            }
        }
    }
}