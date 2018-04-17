package com.anvil.adsama.nsaw.activities;

import android.app.LoaderManager;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.Loader;
import android.net.Uri;
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
import android.text.TextUtils;
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
import com.anvil.adsama.nsaw.model.NewsAPI;
import com.anvil.adsama.nsaw.network.NewsLoader;
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

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, NewsPositionInterface {

    private static final String EMAIL_EXTRA = "EMAIL_EXTRA";
    private static final String URL_EXTRA = "URL_EXTRA";
    private static final String NAME_EXTRA = "NAME_EXTRA";
    private static final int NEWS_LOADER_ID = 3;
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
    private NewsAdapter mNewsAdapter;
    private WeatherFragment mWeatherFragment;
    private BookmarksFragment mBookmarkFragment;
    private StockFragment mStockFragment;
    private String searchText;
    private String mMenuId;
    private String updatedShit;
    private SearchView mSearchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        if (savedInstanceState != null) {
            if (mMenuId != null) {
                mMenuId = savedInstanceState.getString("Menu");
                if (mMenuId.matches(getString(R.string.news))) {
                    mToolbar.setTitle(R.string.news);
                } else if (mMenuId.matches(getString(R.string.stock))) {
                    mToolbar.setTitle(R.string.stock);
                } else if (mMenuId.matches(getString(R.string.weather))) {
                    mToolbar.setTitle(R.string.weather);
                } else if (mMenuId.matches(getString(R.string.bookmarks))) {
                    mToolbar.setTitle(R.string.bookmarks);
                }
            }
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
        getLoaderManager().initLoader(NEWS_LOADER_ID, null, newsLoader);
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

    private LoaderManager.LoaderCallbacks<ArrayList<NewsAPI>> newsLoader = new LoaderManager.LoaderCallbacks<ArrayList<NewsAPI>>() {
        @Override
        public Loader<ArrayList<NewsAPI>> onCreateLoader(int id, Bundle args) {
            showProgress();
            if (searchText != null) {
                return new NewsLoader(getApplicationContext(), makeNewsSearchUrl());
            } else {
                return new NewsLoader(getApplicationContext(), null);
            }
        }

        @Override
        public void onLoadFinished(Loader<ArrayList<NewsAPI>> loader, ArrayList<NewsAPI> data) {
            if (data != null && !data.isEmpty()) {
                mNewsAPIData = data;
                initialiseNews(mNewsAPIData);
                hideProgress();
            }
        }

        @Override
        public void onLoaderReset(Loader<ArrayList<NewsAPI>> loader) {
            mNewsAdapter.notifyDataSetChanged();
        }
    };

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("Search", mSearchView.getQuery().toString());
        outState.putString("Menu", mMenuId);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        updatedShit = savedInstanceState.getString("Search");
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        mSearchView = (android.support.v7.widget.SearchView) searchItem.getActionView();
        if (!TextUtils.isEmpty(updatedShit)) {
            searchItem.expandActionView();
            mSearchView.setQuery(updatedShit, false);
            mSearchView.clearFocus();
        }
        mSearchView.setOnQueryTextListener(new android.support.v7.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchText = query;
                getLoaderManager().restartLoader(NEWS_LOADER_ID, null, newsLoader);
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

    private void setWeatherData() {
        mWeatherFragment = new WeatherFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.weather_fragment_container, mWeatherFragment).addToBackStack(null).commit();

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

    private void showProgress() {
        mPrimaryLayout.setVisibility(View.GONE);
        mLoadingLayout.setVisibility(View.VISIBLE);
    }

    private void hideProgress() {
        mPrimaryLayout.setVisibility(View.VISIBLE);
        mLoadingLayout.setVisibility(View.GONE);
    }

    private void setStockData() {
        mStockFragment = new StockFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.stock_fragment_container, mStockFragment).addToBackStack(null).commit();
    }
}