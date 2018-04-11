package com.anvil.adsama.nsaw.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.anvil.adsama.nsaw.R;
import com.anvil.adsama.nsaw.fragments.DetailFragment;
import com.anvil.adsama.nsaw.model.AlphaVantage;
import com.anvil.adsama.nsaw.model.DarkSkyCurrent;
import com.anvil.adsama.nsaw.model.NewsAPI;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity {

    ArrayList<NewsAPI> newsData = new ArrayList<>();
    ArrayList<AlphaVantage> stockData = new ArrayList<>();
    ArrayList<DarkSkyCurrent> weatherCurrentData = new ArrayList<>();
    int newsPosition;
    int stockPosition;
    int weatherPosition;
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
        if (savedInstanceState == null) {
            getDataFromIntent();
        }
    }

    private void getDataFromIntent() {
        Intent data = getIntent();
        if (data != null) {
            newsPosition = data.getIntExtra("News Position", 0);
            stockPosition = data.getIntExtra("Stock Position", 0);
            weatherPosition = data.getIntExtra("Weather Position", 0);
            newsData = data.getParcelableArrayListExtra("News List");
            stockData = data.getParcelableArrayListExtra("Stock List");
            weatherCurrentData = data.getParcelableArrayListExtra("Weather List");
            String uidFromNews = data.getStringExtra("UID NEWS");
            String uidFromStock = data.getStringExtra("UID STOCK");
            String uidFromWeather = data.getStringExtra("UID WEATHER");
            String locationName = data.getStringExtra("LOCATION");
            Bundle arguments = new Bundle();
            arguments.putInt("News Position", newsPosition);
            arguments.putInt("Stock Position", stockPosition);
            arguments.putInt("Weather Position", weatherPosition);
            arguments.putParcelableArrayList("News List", newsData);
            arguments.putParcelableArrayList("Stock List", stockData);
            arguments.putParcelableArrayList("Weather List", weatherCurrentData);
            arguments.putString("NEWS", uidFromNews);
            arguments.putString("STOCK", uidFromStock);
            arguments.putString("WEATHER", uidFromWeather);
            arguments.putString("LOCATION", locationName);
            DetailFragment fragment = new DetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction().add(R.id.detail_container, fragment).commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}