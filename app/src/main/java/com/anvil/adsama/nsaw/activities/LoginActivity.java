package com.anvil.adsama.nsaw.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.anvil.adsama.nsaw.R;
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

    private static final String LOG_TAG = LoginActivity.class.getSimpleName();
    private static final String EMAIL_EXTRA = "EMAIL_EXTRA";
    private static final String URL_EXTRA = "URL_EXTRA";
    private static final String NAME_EXTRA = "NAME_EXTRA";
    private static final int RC_SIGN_IN = 0;

    @BindView(R.id.google_button)
    SignInButton mGoogleButton;

    GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        mGoogleButton.setOnClickListener(this);
        mGoogleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this, this).addApi(Auth.GOOGLE_SIGN_IN_API, signInOptions).build();
    }

    @Override
    protected void onStart() {
        super.onStart();
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

    private void handleResult(@NonNull GoogleSignInResult result) {
        if (result.isSuccess()) {
            GoogleSignInAccount signInAccount = result.getSignInAccount();
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
            finish();
        }
    }
}