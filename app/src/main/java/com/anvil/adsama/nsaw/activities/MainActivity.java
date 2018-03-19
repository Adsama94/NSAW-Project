package com.anvil.adsama.nsaw.activities;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.anvil.adsama.nsaw.R;
import com.anvil.adsama.nsaw.adapters.NewsAdapter;
import com.anvil.adsama.nsaw.adapters.NewsPositionInterface;
import com.anvil.adsama.nsaw.fragments.LocationFragment;
import com.anvil.adsama.nsaw.fragments.StockFragment;
import com.anvil.adsama.nsaw.fragments.WeatherFragment;
import com.anvil.adsama.nsaw.model.AlphaVantage;
import com.anvil.adsama.nsaw.model.DarkSkyCurrent;
import com.anvil.adsama.nsaw.model.NewsAPI;
import com.anvil.adsama.nsaw.network.NewsAsyncTask;
import com.anvil.adsama.nsaw.network.NewsListener;
import com.anvil.adsama.nsaw.network.NewsSearchTask;
import com.anvil.adsama.nsaw.network.StockListener;
import com.anvil.adsama.nsaw.network.WeatherAsyncTask;
import com.anvil.adsama.nsaw.network.WeatherListener;
import com.anvil.adsama.nsaw.widget.WeatherWidget;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, NewsListener, StockListener, WeatherListener, NewsPositionInterface {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final String EMAIL_EXTRA = "EMAIL_EXTRA";
    private static final String URL_EXTRA = "URL_EXTRA";
    private static final String NAME_EXTRA = "NAME_EXTRA";
    private static final int REQUEST_CODE = 7171;
    @BindView(R.id.adView)
    AdView mAdView;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.nav_view)
    NavigationView mNavigationView;
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.recycler_news)
    RecyclerView mNewsRecyclerView;
    @BindView(R.id.news_layout_main)
    ConstraintLayout mNewsLayout;
    CircleImageView mProfileImageView;
    TextView mProfileNameView;
    TextView mProfileEmailView;
    GoogleApiClient mGoogleApiClient;
    LinearLayoutManager linearLayoutManager;
    ArrayList<NewsAPI> mNewsAPIData;
    ArrayList<DarkSkyCurrent> mDarkSkyData;
    NewsAdapter mNewsAdapter;
    WeatherFragment mWeatherFragment;
    LocationFragment mLocationFragment;
    StockFragment mStockFragment;
    android.support.v7.widget.SearchView mSearchView;
    private String searchText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build();
        mAdView.loadAd(adRequest);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        mNavigationView.setNavigationItemSelectedListener(this);
        setNavDrawer();
        mNavigationView.setCheckedItem(R.id.nav_news);
        NewsAsyncTask newsRequest = new NewsAsyncTask(this);
        newsRequest.execute();
        WeatherAsyncTask weatherAsyncTask = new WeatherAsyncTask(this);
        weatherAsyncTask.execute();
//        StockAsyncTask stockAsyncTask = new StockAsyncTask(this);
//        stockAsyncTask.execute();
    }

    @Override
    protected void onStart() {
        super.onStart();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        mGoogleApiClient = new GoogleApiClient.Builder(this).addApi(Auth.GOOGLE_SIGN_IN_API, gso).build();
        mGoogleApiClient.connect();
        checkLocationPermission();

    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        mSearchView = (android.support.v7.widget.SearchView) searchItem.getActionView();
        mSearchView.setOnQueryTextListener(new android.support.v7.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchText = query;
                NewsSearchTask newsSearchTask = new NewsSearchTask(MainActivity.this);
                newsSearchTask.execute(makeSearchUrl());
                mNewsAdapter.notifyDataSetChanged();
                LottieAnimationView lottieAnimationView = findViewById(R.id.spider_loader);
                lottieAnimationView.setVisibility(View.VISIBLE);
                lottieAnimationView.playAnimation();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_location:
                mLocationFragment = new LocationFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.location_fragment_container, mLocationFragment).commit();
                return true;
            case R.id.action_search:
                if (mLocationFragment != null) {
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.hide(mLocationFragment);
                    fragmentTransaction.commit();
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_news) {
            mToolbar.setTitle(R.string.news);
            if (mLocationFragment != null) {
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.hide(mLocationFragment);
                fragmentTransaction.commit();
            }
            if (mWeatherFragment != null) {
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.hide(mWeatherFragment);
                fragmentTransaction.commit();
            }
            if (mStockFragment != null) {
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.hide(mStockFragment);
                fragmentTransaction.commit();
            }
        } else if (id == R.id.nav_stock) {
            mToolbar.setTitle(R.string.stock);
            setStockData();
            if (mLocationFragment != null) {
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.hide(mLocationFragment);
                fragmentTransaction.commit();
            }
            if (mWeatherFragment != null) {
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.hide(mWeatherFragment);
                fragmentTransaction.commit();
            }
        } else if (id == R.id.nav_weather) {
            mToolbar.setTitle(R.string.weather);
            setWeatherData();
            if (mLocationFragment != null) {
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.hide(mLocationFragment);
                fragmentTransaction.commit();
            }
            if (mStockFragment != null) {
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.hide(mStockFragment);
                fragmentTransaction.commit();
            }
        } else if (id == R.id.nav_bookmark) {
            mToolbar.setTitle(R.string.bookmarks);
            if (mLocationFragment != null) {
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.hide(mLocationFragment);
                fragmentTransaction.commit();
            }
            if (mStockFragment != null) {
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.hide(mStockFragment);
                fragmentTransaction.commit();
            }
            if (mWeatherFragment != null) {
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.hide(mWeatherFragment);
                fragmentTransaction.commit();
            }
        } else if (id == R.id.nav_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
        } else if (id == R.id.nav_rating) {
            Uri uri = Uri.parse("market://details?id=" + getApplicationContext().getPackageName());
            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
            // To count with Play market backstack, After pressing back button,
            // to taken back to our application, we need to add following flags to intent.
            goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_NEW_DOCUMENT | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
            try {
                startActivity(goToMarket);
            } catch (ActivityNotFoundException e) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName())));
            }
        } else if (id == R.id.nav_logout) {
            Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(new ResultCallback<Status>() {
                @Override
                public void onResult(@NonNull Status status) {
                    Toast.makeText(getApplicationContext(), "Signed out " + status, Toast.LENGTH_LONG).show();
                    Intent onBoardingIntent = new Intent(getApplicationContext(), OnBoardingActivity.class);
                    startActivity(onBoardingIntent);
                }
            });
        }
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void returnNewsList(ArrayList<NewsAPI> newsAPIList) {
        mNewsAPIData = new ArrayList<>();
        mNewsAPIData = newsAPIList;
        initialiseNews(mNewsAPIData);
    }

    @Override
    public void returnWeatherList(ArrayList<DarkSkyCurrent> darkSkyList) {
        mDarkSkyData = new ArrayList<>();
        mDarkSkyData = darkSkyList;
    }

    @Override
    public void returnStockList(ArrayList<AlphaVantage> alphaVantageList) {

    }

    @Override
    public void getNewsPosition(int position) {
        Toast.makeText(this, "passed position is " + position, Toast.LENGTH_SHORT).show();
    }

    private void setNavDrawer() {
        View headerView = mNavigationView.getHeaderView(0);
        mProfileImageView = headerView.findViewById(R.id.cv_profileImage);
        mProfileNameView = headerView.findViewById(R.id.tv_profileName);
        mProfileEmailView = headerView.findViewById(R.id.tv_emailId);
        String profileName = getIntent().getStringExtra(NAME_EXTRA);
        String profileEmail = getIntent().getStringExtra(EMAIL_EXTRA);
        String profileImageURL = getIntent().getStringExtra(URL_EXTRA);
        Picasso.with(getApplicationContext()).load(profileImageURL).into(mProfileImageView);
        mProfileNameView.setText(profileName);
        mProfileEmailView.setText(profileEmail);
    }

    private void initialiseNews(ArrayList<NewsAPI> newsAPIArrayList) {
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mNewsAdapter = new NewsAdapter(newsAPIArrayList, this, this);
        mNewsRecyclerView.setAdapter(mNewsAdapter);
        mNewsRecyclerView.setLayoutManager(linearLayoutManager);
    }

    private void setWeatherData() {
        Bundle bundleForWeather = new Bundle();
        bundleForWeather.putParcelableArrayList("DarkSkyCurrent", mDarkSkyData);
        if (mDarkSkyData != null) {
            mWeatherFragment = new WeatherFragment();
            mWeatherFragment.setArguments(bundleForWeather);
            getSupportFragmentManager().beginTransaction().replace(R.id.weather_fragment_container, mWeatherFragment).commit();
            sendWeatherBroadcast();
        }
    }

    private void setStockData() {
        Bundle bundleForStock = new Bundle();
        mStockFragment = new StockFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.stock_fragment_container, mStockFragment).commit();
    }

    private void checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION
            }, REQUEST_CODE);
        }
    }

    public String makeSearchUrl() {
        return "https://newsapi.org/v2/everything?q=" + searchText + "&language=en&pageSize=30&sortBy=publishedAt&apiKey=f89ab3ddfae84bd8866a8d7d26d961f1";
    }

    private void sendWeatherBroadcast() {
        Intent intent = new Intent(this, WeatherWidget.class);
        intent.setAction("android.appwidget.action.APPWIDGET_UPDATE\"");
        WeatherWidget.setWeatherList(mDarkSkyData);
        intent.putParcelableArrayListExtra("weatherList", mDarkSkyData);
        sendBroadcast(intent);
    }
}