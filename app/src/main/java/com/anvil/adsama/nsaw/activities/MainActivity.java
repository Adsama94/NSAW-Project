package com.anvil.adsama.nsaw.activities;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.NavigationView;
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
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.anvil.adsama.nsaw.R;
import com.anvil.adsama.nsaw.adapters.NewsAdapter;
import com.anvil.adsama.nsaw.fragments.WeatherFragment;
import com.anvil.adsama.nsaw.model.AlphaVantage;
import com.anvil.adsama.nsaw.model.DarkSky;
import com.anvil.adsama.nsaw.model.NewsAPI;
import com.anvil.adsama.nsaw.network.NewsAsyncTask;
import com.anvil.adsama.nsaw.network.NewsListener;
import com.anvil.adsama.nsaw.network.StockListener;
import com.anvil.adsama.nsaw.network.WeatherAsyncTask;
import com.anvil.adsama.nsaw.network.WeatherListener;
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

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, NewsListener, StockListener, WeatherListener, SearchView.OnQueryTextListener {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final String EMAIL_EXTRA = "EMAIL_EXTRA";
    private static final String URL_EXTRA = "URL_EXTRA";
    private static final String NAME_EXTRA = "NAME_EXTRA";
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
    ArrayList<DarkSky> mDarkSkyData;
    NewsAdapter mNewsAdapter;
    WeatherFragment mWeatherFragment;


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
    }

    @Override
    protected void onStart() {
        super.onStart();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        mGoogleApiClient = new GoogleApiClient.Builder(this).addApi(Auth.GOOGLE_SIGN_IN_API, gso).build();
        mGoogleApiClient.connect();

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
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_location:
                return true;
            case R.id.action_search:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_news) {

            mToolbar.setTitle(R.string.news);

        } else if (id == R.id.nav_stock) {

            mToolbar.setTitle(R.string.stock);

        } else if (id == R.id.nav_weather) {

            mToolbar.setTitle(R.string.weather);
//            mNewsLayout.setVisibility(View.GONE);
            setWeatherData();

        } else if (id == R.id.nav_bookmark) {

            mToolbar.setTitle(R.string.bookmarks);

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
    public void returnWeatherList(ArrayList<DarkSky> darkSkyList) {
        mDarkSkyData = new ArrayList<>();
        mDarkSkyData = darkSkyList;
    }

    @Override
    public void returnStockList(ArrayList<AlphaVantage> alphaVantageList) {

    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
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
        mNewsAdapter = new NewsAdapter(newsAPIArrayList, this);
        mNewsRecyclerView.setAdapter(mNewsAdapter);
        mNewsRecyclerView.setLayoutManager(linearLayoutManager);
    }

    private void setWeatherData() {
        Bundle bundleForWeather = new Bundle();
        bundleForWeather.putParcelableArrayList("DarkSky", mDarkSkyData);
        if (mDarkSkyData != null) {
            mWeatherFragment = new WeatherFragment();
            mWeatherFragment.setArguments(bundleForWeather);
            getSupportFragmentManager().beginTransaction().add(R.id.weather_fragment_container, mWeatherFragment).commit();
        }
    }
}