package com.anvil.adsama.nsaw.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.Toast;

import com.anvil.adsama.nsaw.R;
import com.anvil.adsama.nsaw.analytics.NsawApp;
import com.anvil.adsama.nsaw.model.AlphaVantage;
import com.anvil.adsama.nsaw.model.DarkSkyCurrent;
import com.anvil.adsama.nsaw.model.DarkSkyDaily;
import com.anvil.adsama.nsaw.model.NewsAPI;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity {

    ArrayList<NewsAPI> newsData = new ArrayList<>();
    ArrayList<AlphaVantage> stockData = new ArrayList<>();
    ArrayList<DarkSkyCurrent> weatherCurrentData = new ArrayList<>();
    ArrayList<DarkSkyDaily> weatherDailyData = new ArrayList<>();
    int newsPosition;
    int stockPosition;
    int weatherPosition;
    @BindView(R.id.iv_backdrop)
    ImageView mBackdropView;
    @BindView(R.id.detail_toolbar)
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("");
        }
        getDataFromIntent();
    }

    private void getDataFromIntent() {
        Intent data = getIntent();
        if (data != null) {
            newsPosition = data.getIntExtra("News Position", 0);
            stockPosition = data.getIntExtra("Stock Position", 0);
            weatherPosition = data.getIntExtra("Weather Position", 0);
            newsData = data.getParcelableArrayListExtra("News List");
            if (newsData != null) {
                loadNewsData();
            }
            stockData = data.getParcelableArrayListExtra("Stock List");
            if (stockData != null) {
                loadStockData();
            }
            weatherCurrentData = data.getParcelableArrayListExtra("Weather List");
            if (weatherCurrentData != null) {
                weatherDailyData = weatherCurrentData.get(weatherPosition).getDailyList();
                loadWeatherData();
            }
        }
    }

    private void loadNewsData() {
        Picasso.with(getApplicationContext()).load(newsData.get(newsPosition).getImageUrl()).into(mBackdropView);
    }

    private void loadStockData() {
        Toast.makeText(this, "STOCK", Toast.LENGTH_SHORT).show();
    }

    private void loadWeatherData() {
        Toast.makeText(this, "WEATHER", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Toast.makeText(this, "lel " + newsPosition + " " + stockPosition + " " + weatherPosition, Toast.LENGTH_SHORT).show();
        NsawApp.getInstance().trackScreenView("DETAIL SCREEN");
    }
}