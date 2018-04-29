package com.anvil.adsama.nsaw.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.Loader;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.anvil.adsama.nsaw.R;
import com.anvil.adsama.nsaw.activities.DetailActivity;
import com.anvil.adsama.nsaw.adapters.WeatherAdapter;
import com.anvil.adsama.nsaw.adapters.WeatherPositionInterface;
import com.anvil.adsama.nsaw.analytics.NsawApp;
import com.anvil.adsama.nsaw.model.DarkSkyCurrent;
import com.anvil.adsama.nsaw.model.DarkSkyDaily;
import com.anvil.adsama.nsaw.network.WeatherLoader;
import com.anvil.adsama.nsaw.widget.WeatherWidget;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class WeatherFragment extends Fragment implements WeatherPositionInterface {

    private static final String LOG_TAG = WeatherFragment.class.getSimpleName();
    private static final int WEATHER_LOADER_ID = 5;
    @BindView(R.id.cl_loading)
    ConstraintLayout mLoadingLayout;
    @BindView(R.id.recycler_weather)
    RecyclerView mWeatherRecyclerView;
    ArrayList<DarkSkyCurrent> mWeatherCurrentList;
    ArrayList<DarkSkyDaily> mWeatherDailyList;
    WeatherAdapter mWeatherAdapter;
    DarkSkyCurrent mWeatherData;
    @BindView(R.id.iv_icon)
    ImageView mIcon;
    @BindView(R.id.tv_visibility)
    TextView mVisibility;
    @BindView(R.id.tv_windSpeed)
    TextView mWindSpeed;
    @BindView(R.id.tv_temperature)
    TextView mTemperature;
    @BindView(R.id.tv_summary)
    TextView mSummary;
    @BindView(R.id.tv_weekly_summary)
    TextView mWeeklySummary;
    @BindView(R.id.cv_weather_primary)
    CardView mPrimaryLayout;
    @BindView(R.id.cv_weather_secondary)
    CardView mSecondaryLayout;
    @BindView(R.id.tv_location_name)
    TextView mLocationText;
    private int PLACE_AUTOCOMPLETE_REQUEST_CODE = 2;
    private String mLocationName;
    private double mLatitude;
    private double mLongitude;

    public WeatherFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    private android.support.v4.app.LoaderManager.LoaderCallbacks<ArrayList<DarkSkyCurrent>> weatherLoader = new LoaderManager.LoaderCallbacks<ArrayList<DarkSkyCurrent>>() {
        @NonNull
        @Override
        public Loader<ArrayList<DarkSkyCurrent>> onCreateLoader(int id, @Nullable Bundle args) {
            showProgress();
            if (mLocationName != null) {
                return new WeatherLoader(getContext(), makeWeatherSearchUrl(mLatitude, mLongitude));
            } else {
                return new WeatherLoader(getContext(), null);
            }
        }

        @Override
        public void onLoadFinished(@NonNull Loader<ArrayList<DarkSkyCurrent>> loader, ArrayList<DarkSkyCurrent> data) {
            hideProgress();
            if (data != null && !data.isEmpty()) {
                mWeatherCurrentList = data;
                mWeatherDailyList = mWeatherCurrentList.get(0).getDailyList();
                setWeatherData(mWeatherCurrentList);
                initialiseWeather(mWeatherDailyList);
                setWeatherIcon();
            } else {
                hideProgress();
                showErrorBar(mPrimaryLayout);
            }
        }

        @Override
        public void onLoaderReset(@NonNull Loader<ArrayList<DarkSkyCurrent>> loader) {
            mWeatherAdapter.notifyDataSetChanged();
        }
    };

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.weather_fragment_layout, container, false);
        ButterKnife.bind(this, rootView);
        getLoaderManager().initLoader(WEATHER_LOADER_ID, null, weatherLoader);
        return rootView;
    }

    private void initialiseWeather(ArrayList<DarkSkyDaily> weatherAPIArrayList) {
        if (weatherAPIArrayList != null) {
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
            mWeatherAdapter = new WeatherAdapter(weatherAPIArrayList, getContext(), this);
            mWeatherRecyclerView.setAdapter(mWeatherAdapter);
            mWeatherRecyclerView.setLayoutManager(linearLayoutManager);
            mWeatherRecyclerView.setNestedScrollingEnabled(false);
        }
    }

    private void setWeatherData(ArrayList<DarkSkyCurrent> currentList) {
        if (currentList != null) {
            mWeatherData = currentList.get(0);
            mSummary.setText(mWeatherData.getSummary());
            mTemperature.setText(String.valueOf(mWeatherData.getTemperature() + " \u2103"));
            mWindSpeed.setText(String.valueOf(mWeatherData.getWindSpeed() + " m/s"));
            mVisibility.setText(String.valueOf(mWeatherData.getVisibility() + " Km"));
            mWeeklySummary.setText(mWeatherData.getWeeklySummary());
            sendWeatherBroadcast();
        }
    }

    private void setWeatherIcon() {
        if (mWeatherData != null) {
            String weatherIconData = mWeatherData.getSummary();
            if (weatherIconData.contains("Rain")) {
                mIcon.setImageResource(R.drawable.ic_rainy);
            } else if (weatherIconData.contains("Clear")) {
                mIcon.setImageResource(R.drawable.ic_sunny);
            } else if (weatherIconData.contains("Cloudy")) {
                mIcon.setImageResource(R.drawable.ic_cloudy);
            } else if (weatherIconData.contains("Snow")) {
                mIcon.setImageResource(R.drawable.ic_snowflake);
            } else if (weatherIconData.contains("Thunder")) {
                mIcon.setImageResource(R.drawable.ic_lightning);
            } else if (weatherIconData.contains("Partly Sunny")) {
                mIcon.setImageResource(R.drawable.ic_partly_sunny);
            } else if (weatherIconData.contains("Showers")) {
                mIcon.setImageResource(R.drawable.ic_showers);
            } else if (weatherIconData.contains("Foggy")) {
                mIcon.setImageResource(R.drawable.ic_foggy);
            }
        }
    }

    private void showErrorBar(View errorView) {
        Snackbar snackbar = Snackbar.make(errorView, getString(R.string.unreachable), Snackbar.LENGTH_LONG);
        View snackBarView = snackbar.getView();
        if (getContext() != null)
            snackBarView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
        snackbar.show();
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.action_search).setVisible(false);
        menu.findItem(R.id.action_weather_search).setVisible(true);
        MenuItem searchMenuItem = menu.findItem(R.id.action_weather_search);
        searchMenuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                try {
                    if (getActivity() != null) {
                        Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY).build(getActivity());
                        startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
                    }
                } catch (GooglePlayServicesRepairableException e) {
                    Toast.makeText(getContext(), "SERVICES NOT AVAILABLE " + e, Toast.LENGTH_SHORT).show();
                    Log.e(LOG_TAG, "SERVICES REPAIRABLE " + e.getMessage());
                } catch (GooglePlayServicesNotAvailableException e) {
                    Toast.makeText(getContext(), "SERVICES NOT AVAILABLE " + e, Toast.LENGTH_SHORT).show();
                    Log.e(LOG_TAG, "SERVICES UNAVAILABLE " + e.getMessage());
                }
                return false;
            }
        });
        super.onPrepareOptionsMenu(menu);
    }

    private void refreshAdapter() {
        if (mWeatherAdapter != null) mWeatherAdapter.notifyDataSetChanged();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            switch (resultCode) {
                case RESULT_OK:
                    if (getContext() != null) {
                        Place place = PlaceAutocomplete.getPlace(getContext(), data);
                        mLocationName = (String) place.getAddress();
                        LatLng newLat = place.getLatLng();
                        mLatitude = newLat.latitude;
                        mLongitude = newLat.longitude;
                        mLocationText.setText(mLocationName);
                        getLoaderManager().restartLoader(WEATHER_LOADER_ID, null, weatherLoader);
                    }
                    break;
                case PlaceAutocomplete.RESULT_ERROR:
                    if (getContext() != null) {
                        Status status = PlaceAutocomplete.getStatus(getContext(), data);
                        Log.i(LOG_TAG, status.getStatusMessage());
                    }
                    break;
                case RESULT_CANCELED:
                    Toast.makeText(getContext(), "CANCELLED BY USER", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        NsawApp.getInstance().trackScreenView("WEATHER FRAGMENT");
    }

    public String makeWeatherSearchUrl(double latitude, double longitude) {
        return "https://api.darksky.net/forecast/6baefba9f2a860bd68ecb53fd8024caa/" + latitude + "," + longitude + "?units=si";
    }

    public void showProgress() {
        mPrimaryLayout.setVisibility(View.GONE);
        mSecondaryLayout.setVisibility(View.GONE);
        mWeatherRecyclerView.setVisibility(View.GONE);
        mLoadingLayout.setVisibility(View.VISIBLE);
    }

    public void hideProgress() {
        mPrimaryLayout.setVisibility(View.VISIBLE);
        mSecondaryLayout.setVisibility(View.VISIBLE);
        mWeatherRecyclerView.setVisibility(View.VISIBLE);
        mLoadingLayout.setVisibility(View.GONE);
        refreshAdapter();
    }

    @Override
    public void getWeatherPosition(int position) {
        Intent detailIntent = new Intent(getContext(), DetailActivity.class);
        if (mWeatherCurrentList != null)
            detailIntent.putParcelableArrayListExtra("Weather List", mWeatherCurrentList);
        detailIntent.putExtra("UID WEATHER", "FROM WEATHER");
        detailIntent.putExtra("Weather Position", position);
        detailIntent.putExtra("LOCATION", mLocationName);
        startActivity(detailIntent);
    }

    private void sendWeatherBroadcast() {
        Intent intent = new Intent(getContext(), WeatherWidget.class);
        intent.setAction("android.appwidget.action.APPWIDGET_UPDATE\"");
        if (mLocationName != null) {
            WeatherWidget.setWeatherList(mWeatherCurrentList, mLocationName);
        } else {
            WeatherWidget.setWeatherList(mWeatherCurrentList, "New Delhi, India");
        }
        intent.putParcelableArrayListExtra("weatherList", mWeatherCurrentList);
        if (getActivity() != null)
            getActivity().sendBroadcast(intent);
    }
}