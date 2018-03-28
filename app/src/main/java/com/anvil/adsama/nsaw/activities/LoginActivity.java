package com.anvil.adsama.nsaw.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.anvil.adsama.nsaw.R;
import com.anvil.adsama.nsaw.analytics.NsawApp;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {

    public static final String PREFS_NAME = "LoginCheck";
    private static final String LOG_TAG = LoginActivity.class.getSimpleName();
    private static final String EMAIL_EXTRA = "EMAIL_EXTRA";
    private static final String URL_EXTRA = "URL_EXTRA";
    private static final String NAME_EXTRA = "NAME_EXTRA";
    private static final int RC_SIGN_IN = 0;
    private static final int RC_ACCOUNTS = 7010;
    @BindView(R.id.google_button)
    SignInButton mGoogleButton;
    AlertDialog mAccountsDialog;
    GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        mGoogleButton.setOnClickListener(this);
        mGoogleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this, this).addApi(Auth.GOOGLE_SIGN_IN_API, signInOptions).build();
    }

    @Override
    protected void onStart() {
        super.onStart();
        showPopUp();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.i(LOG_TAG, "Connected");
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i(LOG_TAG, "onConnectionFailed " + connectionResult.getErrorCode());
    }

    @Override
    public void onClick(View v) {
        Intent loginIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(loginIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleResult(result);
        }
    }

    private void checkAccountsPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.GET_ACCOUNTS
            }, RC_ACCOUNTS);
        }
    }

    private void showPopUp() {
        if (this.mAccountsDialog == null || !this.mAccountsDialog.isShowing()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Unable to access accounts!");
            builder.setMessage("NSAW needs user data to give personalised information.\nPlease enable contacts permissions.");
            builder.setPositiveButton("ACCEPT", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    checkAccountsPermission();
                }
            });
            builder.setNegativeButton("DENY", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(LoginActivity.this, "PERMISSION NOT GIVEN!", Toast.LENGTH_SHORT).show();
                }
            });
            this.mAccountsDialog = builder.create();
            this.mAccountsDialog.setCancelable(false);
            builder.show();
        }
    }

    private void handleResult(@NonNull GoogleSignInResult result) {
        if (result.isSuccess()) {
            GoogleSignInAccount signInAccount = result.getSignInAccount();
            if (signInAccount != null) {
                String accountName = signInAccount.getDisplayName();
                String accountEmail = signInAccount.getEmail();
                String accountUrl = String.valueOf(signInAccount.getPhotoUrl());
                mGoogleButton.setEnabled(false);
                Toast.makeText(this, "Welcome " + accountName, Toast.LENGTH_SHORT).show();
                Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);
                mainIntent.putExtra(NAME_EXTRA, accountName);
                mainIntent.putExtra(EMAIL_EXTRA, accountEmail);
                mainIntent.putExtra(URL_EXTRA, accountUrl);
                startActivity(mainIntent);
                SharedPreferences settings = getSharedPreferences(LoginActivity.PREFS_NAME, 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putBoolean("LoginSuccess", true);
                editor.putString("loginName", accountName);
                editor.putString("loginAccount", accountEmail);
                editor.putString("loginUrl", accountUrl);
                editor.apply();
                finish();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        NsawApp.getInstance().trackScreenView("LOGIN SCREEN");
    }
}