package com.anvil.adsama.nsaw.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.anvil.adsama.nsaw.R;

public class SplashActivity extends AppCompatActivity {

    ConnectivityManager connManager;
    NetworkInfo networkInfo;
    AlertDialog mInternetDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        int SPLASH_TIME_OUT = 2000;
        connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connManager != null)
            networkInfo = connManager.getActiveNetworkInfo();
        if (networkInfo == null) {
            showNoInternetDialog();
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity();
                    finish();
                }
            }, SPLASH_TIME_OUT);
        }
    }

    private void showNoInternetDialog() {
        if (this.mInternetDialog == null || !this.mInternetDialog.isShowing()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Internet Disabled!");
            builder.setMessage("Please enable Internet for latest feeds.");
            builder.setPositiveButton("Enable Wifi", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    SplashActivity.this.startActivityForResult(new Intent("android.settings.WIFI_SETTINGS"), 1);
                }
            });
            builder.setNegativeButton("Offline Mode", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    startActivity();
                }
            });
            this.mInternetDialog = builder.create();
            this.mInternetDialog.setCancelable(false);
            this.mInternetDialog.show();
        }
    }

    private void startActivity() {
        SharedPreferences preferences = getSharedPreferences(LoginActivity.PREFS_NAME, 0);
        boolean isLoginSuccess = preferences.getBoolean("LoginSuccess", false);
        String loginName = preferences.getString("loginName", "");
        String loginAccount = preferences.getString("loginAccount", "");
        String loginUrl = preferences.getString("loginUrl", "");
        if (isLoginSuccess) {
            Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);
            mainIntent.putExtra("NAME_EXTRA", loginName);
            mainIntent.putExtra("EMAIL_EXTRA", loginAccount);
            mainIntent.putExtra("URL_EXTRA", loginUrl);
            startActivity(mainIntent);
            finish();
        } else {
            Intent onBoardIntent = new Intent(getApplicationContext(), OnBoardingActivity.class);
            startActivity(onBoardIntent);
            finish();
        }
    }
}