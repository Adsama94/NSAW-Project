package com.anvil.adsama.nsaw.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.anvil.adsama.nsaw.R;
import com.anvil.adsama.nsaw.analytics.NsawApp;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SettingsActivity extends AppCompatActivity {

    @BindView(R.id.tv_support)
    TextView mSupportText;
    @BindView(R.id.tv_contribute)
    TextView mContributeText;
    @BindView(R.id.iv_contribute)
    ImageView mContributeImage;
    @BindView(R.id.iv_support)
    ImageView mSupportImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);
        mSupportText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                supportIntent();
            }
        });
        mSupportImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                supportIntent();
            }
        });
        mContributeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contributeIntent();
            }
        });
        mContributeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contributeIntent();
            }
        });
    }

    private void supportIntent() {
        Intent supportIntent = new Intent(Intent.ACTION_SENDTO);
        supportIntent.setData(Uri.parse("mailto:paransharma94@gmail.com"));
        supportIntent.putExtra(Intent.EXTRA_SUBJECT, "Feedback/Suggestion");
        startActivity(supportIntent);
    }

    private void contributeIntent() {
        String gitHubUrl = getString(R.string.github_url);
        Intent gitHubIntent = new Intent(Intent.ACTION_VIEW);
        gitHubIntent.setData(Uri.parse(gitHubUrl));
        startActivity(gitHubIntent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        NsawApp.getInstance().trackScreenView("SETTINGS SCREEN");
    }
}