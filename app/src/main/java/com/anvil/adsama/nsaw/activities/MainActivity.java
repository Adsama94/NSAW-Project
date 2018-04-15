package com.anvil.adsama.nsaw.activities;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.anvil.adsama.nsaw.R;
import com.anvil.adsama.nsaw.adapters.NewsAdapter;
import com.anvil.adsama.nsaw.adapters.NewsPositionInterface;
import com.anvil.adsama.nsaw.analytics.NsawApp;
import com.anvil.adsama.nsaw.fragments.BookmarksFragment;
import com.anvil.adsama.nsaw.fragments.StockFragment;
import com.anvil.adsama.nsaw.fragments.WeatherFragment;
import com.anvil.adsama.nsaw.model.AlphaVantage;
import com.anvil.adsama.nsaw.model.DarkSkyCurrent;
import com.anvil.adsama.nsaw.model.NewsAPI;
import com.anvil.adsama.nsaw.network.NewsAsyncTask;
import com.anvil.adsama.nsaw.network.NewsListener;
import com.anvil.adsama.nsaw.network.NewsSearchTask;
import com.anvil.adsama.nsaw.network.StockAsyncTask;
import com.anvil.adsama.nsaw.network.StockListener;
import com.anvil.adsama.nsaw.network.WeatherAsyncTask;
import com.anvil.adsama.nsaw.network.WeatherListener;
import com.anvil.adsama.nsaw.services.NotificationReceiver;
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
    @BindView(R.id.cv_news_layout_main)
    ConstraintLayout mPrimaryLayout;
    @BindView(R.id.cl_loading)
    ConstraintLayout mLoadingLayout;
    CircleImageView mProfileImageView;
    TextView mProfileNameView;
    TextView mProfileEmailView;
    GoogleApiClient mGoogleSignInClient;
    LinearLayoutManager linearLayoutManager;
    private ArrayList<NewsAPI> mNewsAPIData;
    private ArrayList<DarkSkyCurrent> mDarkSkyData;
    private ArrayList<AlphaVantage> mStockData;
    private NewsAdapter mNewsAdapter;
    private WeatherFragment mWeatherFragment;
    private BookmarksFragment mBookmarkFragment;
    private StockFragment mStockFragment;
    private String searchText;
    private String mMenuId;
    SearchView mSearchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        if (savedInstanceState != null) {
            searchText = savedInstanceState.getString("Search");
            mMenuId = savedInstanceState.getString("Menu");
        }
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
        newsRequest.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        WeatherAsyncTask weatherAsyncTask = new WeatherAsyncTask(this);
        weatherAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        StockAsyncTask stockAsyncTask = new StockAsyncTask(this);
        stockAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("Search", searchText);
        outState.putString("Menu", mMenuId);
    }

    @Override
    protected void onStart() {
        super.onStart();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        mGoogleSignInClient = new GoogleApiClient.Builder(this).addApi(Auth.GOOGLE_SIGN_IN_API, gso).build();
        mGoogleSignInClient.connect();
    }

    @Override
    protected void onStop() {
        mGoogleSignInClient.disconnect();
        super.onStop();
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
                NewsSearchTask newsSearchTask = new NewsSearchTask(MainActivity.this, MainActivity.this);
                newsSearchTask.execute(makeNewsSearchUrl());
                mSearchView.clearFocus();
                mSearchView.setQueryHint("Search news...");
                mNewsAdapter.notifyDataSetChanged();
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        mMenuId = (String) item.getTitle();
        if (mMenuId.matches(getString(R.string.news))) {
            mToolbar.setTitle(R.string.news);
            getSupportFragmentManager().popBackStack();
            sendNotificationBroadcast();
            hideFragments(mStockFragment);
            hideFragments(mBookmarkFragment);
            hideFragments(mWeatherFragment);
        } else if (mMenuId.matches(getString(R.string.stock))) {
            mToolbar.setTitle(R.string.stock);
            getSupportFragmentManager().popBackStack();
            setStockData();
            hideFragments(mBookmarkFragment);
            hideFragments(mWeatherFragment);
        } else if (mMenuId.matches(getString(R.string.weather))) {
            mToolbar.setTitle(R.string.weather);
            getSupportFragmentManager().popBackStack();
            setWeatherData();
            hideFragments(mStockFragment);
            hideFragments(mBookmarkFragment);
        } else if (mMenuId.matches(getString(R.string.bookmarks))) {
            mToolbar.setTitle(R.string.bookmarks);
            getSupportFragmentManager().popBackStack();
            setBookmarkData();
            hideFragments(mStockFragment);
            hideFragments(mWeatherFragment);
        } else if (mMenuId.matches(getString(R.string.settings))) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
        } else if (mMenuId.matches(getString(R.string.rate_us))) {
            Uri uri = Uri.parse("market://details?id=" + getApplicationContext().getPackageName());
            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
            goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
            try {
                startActivity(goToMarket);
            } catch (ActivityNotFoundException e) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName())));
            }
        } else if (mMenuId.matches(getString(R.string.log_out))) {
            Auth.GoogleSignInApi.signOut(mGoogleSignInClient).setResultCallback(new ResultCallback<Status>() {
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
    protected void onResume() {
        super.onResume();
        NsawApp.getInstance().trackScreenView("MAIN/NEWS SCREEN");
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
        mStockData = new ArrayList<>();
        mStockData = alphaVantageList;
    }

    @Override
    public void getNewsPosition(int position) {
        Intent detailIntent = new Intent(this, DetailActivity.class);
        detailIntent.putExtra("News Position", position);
        detailIntent.putExtra("UID NEWS", "FROM NEWS");
        detailIntent.putParcelableArrayListExtra("News List", mNewsAPIData);
        startActivity(detailIntent);
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
            getSupportFragmentManager().beginTransaction().replace(R.id.weather_fragment_container, mWeatherFragment).addToBackStack(null).commit();
        }
    }

    private void setStockData() {
        Bundle bundleForStock = new Bundle();
        bundleForStock.putParcelableArrayList("AlphaVantage", mStockData);
        if (mStockData != null) {
            mStockFragment = new StockFragment();
            mStockFragment.setArguments(bundleForStock);
            getSupportFragmentManager().beginTransaction().replace(R.id.stock_fragment_container, mStockFragment).addToBackStack(null).commit();
        }
    }

    private void setBookmarkData() {
        mBookmarkFragment = new BookmarksFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.bookmark_fragment_container, mBookmarkFragment).addToBackStack(null).commit();
    }

    private void hideFragments(Fragment fragment) {
        if (fragment != null) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.hide(fragment);
            fragmentTransaction.commit();
        }
    }

    public String makeNewsSearchUrl() {
        return "https://newsapi.org/v2/everything?q=" + searchText + "&language=en&pageSize=30&sortBy=publishedAt&apiKey=f89ab3ddfae84bd8866a8d7d26d961f1";
    }

    private void sendNotificationBroadcast() {
        if (mNewsAPIData != null && mStockData != null && mDarkSkyData != null) {
            String newsExtra = mNewsAPIData.get(0).getTitle();
            String stockExtra = mStockData.get(0).getCompanyName() + " High " + mStockData.get(0).getHigh();
            float weatherExtra = mDarkSkyData.get(0).getTemperature();
            Intent broadcastIntent = new Intent(this, NotificationReceiver.class);
            broadcastIntent.setAction("notification.broadcast");
            broadcastIntent.putExtra("news_extra", newsExtra);
            broadcastIntent.putExtra("stock_extra", stockExtra);
            broadcastIntent.putExtra("weather_extra", weatherExtra);
            sendBroadcast(broadcastIntent);
        }
    }

    public void showProgress() {
        mPrimaryLayout.setVisibility(View.GONE);
        mLoadingLayout.setVisibility(View.VISIBLE);
    }

    public void hideProgress() {
        mPrimaryLayout.setVisibility(View.VISIBLE);
        mLoadingLayout.setVisibility(View.GONE);
    }
}