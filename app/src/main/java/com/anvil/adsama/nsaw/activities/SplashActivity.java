package com.anvil.adsama.nsaw.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.anvil.adsama.nsaw.R;
import com.anvil.adsama.nsaw.analytics.NsawApp;

public class SplashActivity extends AppCompatActivity {

    ConnectivityManager connManager;
    NetworkInfo networkInfo;
    AlertDialog mInternetDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
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
            builder.setTitle(getString(R.string.internet_disabled));
            builder.setMessage(getString(R.string.latest_feed));
            builder.setPositiveButton(getString(R.string.enable_wifi), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    startActivityForResult(new Intent("android.settings.WIFI_SETTINGS"), 0);
                }
            });
            builder.setNegativeButton(getString(R.string.exit), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            this.mInternetDialog = builder.create();
            this.mInternetDialog.setCancelable(false);
            this.mInternetDialog.show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 0) {
            if (connManager != null) {
                networkInfo = connManager.getActiveNetworkInfo();
                if (networkInfo != null) {
                    if (networkInfo.isConnectedOrConnecting()) {
                        startActivity();
                    } else {
                        showNoInternetDialog();
                    }
                }
                showNoInternetDialog();
            }
        } else if (networkInfo == null) {
            showNoInternetDialog();
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

    @Override
    protected void onResume() {
        super.onResume();
        NsawApp.getInstance().trackScreenView("SPLASH SCREEN");
    }
}