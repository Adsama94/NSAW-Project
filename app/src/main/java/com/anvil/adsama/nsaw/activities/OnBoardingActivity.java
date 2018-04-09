package com.anvil.adsama.nsaw.activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
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
    @BindView(R.id.btn_onBoard)
    Button mOnBoardButton;
    @BindView(R.id.indicator)
    CircleIndicator mCircleIndicator;
    @BindView(R.id.tv_dynamic_text)
    TextView mDynamicText;
    OnBoardingAdapter mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_onboarding);
        ButterKnife.bind(this);
        initPager();
        initButton();
    }

    private void initPager() {
        mPagerAdapter = new OnBoardingAdapter(this);
        mFeaturePager.setAdapter(mPagerAdapter);
        mCircleIndicator.setViewPager(mFeaturePager);
        mFeaturePager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (position == 0) {
                    mDynamicText.setText(R.string.onboard_news);
                } else if (position == 1) {
                    mDynamicText.setText(R.string.onboard_stock);
                } else if (position == 2) {
                    mDynamicText.setText(R.string.onboard_weather);
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initButton() {
        mOnBoardButton.setOnClickListener(new View.OnClickListener() {
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