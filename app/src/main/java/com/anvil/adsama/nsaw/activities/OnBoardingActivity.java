package com.anvil.adsama.nsaw.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.anvil.adsama.nsaw.R;
import com.anvil.adsama.nsaw.adapters.OnBoardingAdapter;
import com.anvil.adsama.nsaw.analytics.NsawApp;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.relex.circleindicator.CircleIndicator;

public class OnBoardingActivity extends AppCompatActivity {

    @BindView(R.id.feature_pics)
    ViewPager mFeaturePager;
    @BindView(R.id.tv_onBoard)
    TextView mOnBoardTextView;
    @BindView(R.id.indicator)
    CircleIndicator mCircleIndicator;
    OnBoardingAdapter mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);
        ButterKnife.bind(this);
        initPager();
        initButton();
    }

    private void initPager() {
        mPagerAdapter = new OnBoardingAdapter(this);
        mFeaturePager.setAdapter(mPagerAdapter);
        mCircleIndicator.setViewPager(mFeaturePager);
    }

    private void initButton() {
        mOnBoardTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        NsawApp.getInstance().trackScreenView("VIEWPAGER SCREEN");
    }
}