package com.anvil.adsama.nsaw.fragments;


import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.anvil.adsama.nsaw.R;
import com.anvil.adsama.nsaw.analytics.NsawApp;
import com.anvil.adsama.nsaw.database.NsawContract;
import com.anvil.adsama.nsaw.model.AlphaVantage;
import com.anvil.adsama.nsaw.model.DarkSkyCurrent;
import com.anvil.adsama.nsaw.model.DarkSkyDaily;
import com.anvil.adsama.nsaw.model.NewsAPI;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DetailFragment extends Fragment {

    private static final String LOG_TAG = DetailFragment.class.getSimpleName();

    TextView mDetailText, mDateText, mDewText, mHumidityText, mVisibilityText, mApparentHigh, mApparentLow, mSummary, mTemp;
    CollapsingToolbarLayout mAppBarLayout;
    ImageView mCollapsingImageView, mIconView;
    ConstraintLayout mNewsLayout, mStockLayout, mWeatherLayout;
    Button mNewsArticleButton;
    ArrayList<NewsAPI> newsData = new ArrayList<>();
    ArrayList<AlphaVantage> stockData = new ArrayList<>();
    ArrayList<DarkSkyCurrent> weatherCurrentData = new ArrayList<>();
    ArrayList<DarkSkyDaily> weatherDailyData = new ArrayList<>();
    FloatingActionButton storingButton, sharingButton, removingButton;
    int newsPosition;
    int stockPosition;
    int weatherPosition;

    public DetailFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        savedInstanceState = getArguments();
        if (savedInstanceState != null) {
            newsPosition = savedInstanceState.getInt("News Position");
            stockPosition = savedInstanceState.getInt("Stock Position");
            weatherPosition = savedInstanceState.getInt("Weather Position");
            newsData = savedInstanceState.getParcelableArrayList("News List");
            stockData = savedInstanceState.getParcelableArrayList("Stock List");
            weatherCurrentData = savedInstanceState.getParcelableArrayList("Weather List");
            if (weatherCurrentData != null) {
                weatherDailyData = weatherCurrentData.get(weatherPosition).getDailyList();
            }
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Activity activity = getActivity();
        if (activity != null) {
            mAppBarLayout = activity.findViewById(R.id.toolbar_layout);
            mCollapsingImageView = activity.findViewById(R.id.iv_backdrop);
            if (mCollapsingImageView != null) {
                if (newsData != null) {
                    Picasso.with(getContext()).load(newsData.get(newsPosition).getImageUrl()).into(mCollapsingImageView);
                }
                if (stockData != null) {
                    mCollapsingImageView.setImageDrawable(getResources().getDrawable(R.drawable.stock_nav));
                }
                if (weatherCurrentData != null) {
                    mCollapsingImageView.setImageDrawable(getResources().getDrawable(R.drawable.weather_nav));
                }
            }
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        mNewsLayout = rootView.findViewById(R.id.news_layout_detail);
        mStockLayout = rootView.findViewById(R.id.stock_layout_detail);
        mWeatherLayout = rootView.findViewById(R.id.weather_layout_detail);
        mDetailText = rootView.findViewById(R.id.tv_detail_text);
        mDateText = rootView.findViewById(R.id.tv_detail_date);
        mNewsArticleButton = rootView.findViewById(R.id.button_article);
        mSummary = rootView.findViewById(R.id.tv_detail_summary);
        mDewText = rootView.findViewById(R.id.tv_dew);
        mHumidityText = rootView.findViewById(R.id.tv_humidity);
        mVisibilityText = rootView.findViewById(R.id.tv_visibility);
        mApparentHigh = rootView.findViewById(R.id.tv_app_high);
        mApparentLow = rootView.findViewById(R.id.tv_app_low);
        mTemp = rootView.findViewById(R.id.tv_detail_temp);
        mIconView = rootView.findViewById(R.id.iv_icon_detail);
        if (newsData != null) {
            displayNewsData();
            mNewsLayout.setVisibility(View.VISIBLE);
            mStockLayout.setVisibility(View.GONE);
            mWeatherLayout.setVisibility(View.GONE);
        }
        if (stockData != null) {
            displayStockData();
            mNewsLayout.setVisibility(View.GONE);
            mStockLayout.setVisibility(View.VISIBLE);
            mWeatherLayout.setVisibility(View.GONE);
        }
        if (weatherCurrentData != null) {
            displayWeatherData();
            mNewsLayout.setVisibility(View.GONE);
            mStockLayout.setVisibility(View.GONE);
            mWeatherLayout.setVisibility(View.VISIBLE);
        }
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getActivity() != null) {
            storingButton = getActivity().findViewById(R.id.storing_fab);
            storingButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    storingButton.setVisibility(View.GONE);
                    addToDatabase();
                    removingButton.setVisibility(View.VISIBLE);
                    Toast.makeText(getContext(), "STORE KARAAA", Toast.LENGTH_SHORT).show();
                }
            });
            sharingButton = getActivity().findViewById(R.id.sharing_fab);
            sharingButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    makeSharingIntent();
                }
            });
            removingButton = getActivity().findViewById(R.id.removing_fab);
            removingButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    removingButton.setVisibility(View.GONE);
                    storingButton.setVisibility(View.VISIBLE);
                    removeFromDatabase();
                    Toast.makeText(getContext(), "REMOVE KARAAA", Toast.LENGTH_SHORT).show();
                }
            });
            BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.bottom_nav_view);
            bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.action_bottom_news:
                            Toast.makeText(getContext(), "NOOJ", Toast.LENGTH_SHORT).show();
                            break;
                        case R.id.action_bottom_stock:
                            Toast.makeText(getContext(), "STOOCK", Toast.LENGTH_SHORT).show();
                            break;
                        case R.id.action_bottom_weather:
                            Toast.makeText(getContext(), "WAAAYTHUR", Toast.LENGTH_SHORT).show();
                            break;
                    }
                    return true;
                }
            });
        }
    }

    private void displayNewsData() {
        final NewsAPI newsAPI = newsData.get(newsPosition);
        if (newsAPI != null) {
            mDateText.setText(newsAPI.getDate());
            mDetailText.setText(newsAPI.getDescription());
            mNewsArticleButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(newsAPI.getArticleUrl()));
                    startActivity(i);
                }
            });
        }
    }

    private void displayStockData() {
        AlphaVantage alphaVantage = stockData.get(stockPosition);
    }

    private void displayWeatherData() {
        DarkSkyCurrent skyCurrent = weatherCurrentData.get(weatherPosition);
        if (skyCurrent != null) {
            DarkSkyDaily skyDaily = weatherDailyData.get(weatherPosition);
            mTemp.setText(String.valueOf(skyDaily.getHighTemp()));
            mApparentLow.setText(String.valueOf(skyDaily.getApparentLow()));
            mApparentHigh.setText(String.valueOf(skyDaily.getApparentHigh()));
            mVisibilityText.setText(String.valueOf(skyDaily.getVisibility()));
            mHumidityText.setText(String.valueOf(skyDaily.getHumidity()));
            mDewText.setText(String.valueOf(skyDaily.getDewPoint()));
            mSummary.setText(skyDaily.getSummary());
            setWeatherIcon();
        }
    }

    private void setWeatherIcon() {
        String weatherIconData = weatherCurrentData.get(weatherPosition).getSummary();
        if (weatherIconData.contains("Rain")) {
            mIconView.setImageResource(R.drawable.ic_rainy);
        } else if (weatherIconData.contains("Clear")) {
            mIconView.setImageResource(R.drawable.ic_sunny);
        } else if (weatherIconData.contains("Cloudy")) {
            mIconView.setImageResource(R.drawable.ic_cloudy);
        } else if (weatherIconData.contains("Snow")) {
            mIconView.setImageResource(R.drawable.ic_snowflake);
        } else if (weatherIconData.contains("Thunder")) {
            mIconView.setImageResource(R.drawable.ic_lightning);
        } else if (weatherIconData.contains("Partly Sunny")) {
            mIconView.setImageResource(R.drawable.ic_partly_sunny);
        } else if (weatherIconData.contains("Showers")) {
            mIconView.setImageResource(R.drawable.ic_showers);
        }
    }

    private void makeSharingIntent() {
        if (newsData != null) {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.setType("text/plain");
            sendIntent.putExtra(Intent.EXTRA_SUBJECT, newsData.get(newsPosition).getTitle());
            sendIntent.putExtra(Intent.EXTRA_TEXT, newsData.get(newsPosition).getDescription() + "\n\n" + newsData.get(newsPosition).getArticleUrl());
            startActivity(sendIntent);
        } else if (stockData != null) {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.setType("text/plain");
            startActivity(sendIntent);
        } else if (weatherCurrentData != null) {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.setType("text/plain");
            sendIntent.putExtra(Intent.EXTRA_TEXT, weatherCurrentData.get(weatherPosition).getTemperature() + "\n\n" + weatherDailyData.get(weatherPosition).getSummary());
            startActivity(sendIntent);
        }
    }

    private void addToDatabase() {
        ContentValues appValues = new ContentValues();
        if (newsData != null) {
            NewsAPI newsAPI = newsData.get(newsPosition);
            appValues.put(NsawContract.NsawEntry.COLUMN_NEWS_AUTHOR, newsAPI.getAuthorName());
            appValues.put(NsawContract.NsawEntry.COLUMN_NEWS_TITLE, newsAPI.getTitle());
            appValues.put(NsawContract.NsawEntry.COLUMN_NEWS_DESCRIPTION, newsAPI.getDescription());
            appValues.put(NsawContract.NsawEntry.COLUMN_NEWS_URL, newsAPI.getArticleUrl());
            appValues.put(NsawContract.NsawEntry.COLUMN_NEWS_DATE, newsAPI.getDate());
            appValues.put(NsawContract.NsawEntry.COLUMN_NEWS_IMAGEURL, newsAPI.getImageUrl());
            if (getContext() != null)
                getContext().getContentResolver().insert(NsawContract.NsawEntry.NEWS_CONTENT_URI, appValues);
        } else if (stockData != null) {
            AlphaVantage alphaVantage = stockData.get(stockPosition);
            appValues.put(NsawContract.NsawEntry.COLUMN_STOCK_NAME, alphaVantage.getCompanyName());
            appValues.put(NsawContract.NsawEntry.COLUMN_STOCK_REFRESH, alphaVantage.getRefreshTime());
            appValues.put(NsawContract.NsawEntry.COLUMN_STOCK_OPEN, alphaVantage.getOpen());
            appValues.put(NsawContract.NsawEntry.COLUMN_STOCK_CLOSE, alphaVantage.getClose());
            appValues.put(NsawContract.NsawEntry.COLUMN_STOCK_HIGH, alphaVantage.getHigh());
            appValues.put(NsawContract.NsawEntry.COLUMN_STOCK_LOW, alphaVantage.getLow());
            appValues.put(NsawContract.NsawEntry.COLUMN_STOCK_VOLUME, alphaVantage.getVolume());
            if (getContext() != null)
                getContext().getContentResolver().insert(NsawContract.NsawEntry.STOCK_CONTENT_URI, appValues);
        } else if (weatherCurrentData != null) {
            DarkSkyCurrent darkSkyCurrent = weatherCurrentData.get(weatherPosition);
            appValues.put(NsawContract.NsawEntry.COLUMN_CURRENT_SUMMARY, darkSkyCurrent.getSummary());
            appValues.put(NsawContract.NsawEntry.COLUMN_CURRENT_WEEKLY, darkSkyCurrent.getWeeklySummary());
            appValues.put(NsawContract.NsawEntry.COLUMN_CURRENT_TEMPERATURE, darkSkyCurrent.getTemperature());
            appValues.put(NsawContract.NsawEntry.COLUMN_CURRENT_ICON, darkSkyCurrent.getIcon());
            appValues.put(NsawContract.NsawEntry.COLUMN_CURRENT_WIND, darkSkyCurrent.getWindSpeed());
            appValues.put(NsawContract.NsawEntry.COLUMN_CURRENT_VISIBILITY, darkSkyCurrent.getVisibility());
            if (getContext() != null)
                getContext().getContentResolver().insert(NsawContract.NsawEntry.WEATHER_CURRENT_CONTENT_URI, appValues);
        } else if (weatherDailyData != null) {
            DarkSkyDaily darkSkyDaily = weatherDailyData.get(weatherPosition);
            appValues.put(NsawContract.NsawEntry.COLUMN_DAILY_TIME, darkSkyDaily.getTime());
            appValues.put(NsawContract.NsawEntry.COLUMN_DAILY_SUMMARY, darkSkyDaily.getSummary());
            appValues.put(NsawContract.NsawEntry.COLUMN_DAILY_LOW, darkSkyDaily.getLowTemp());
            appValues.put(NsawContract.NsawEntry.COLUMN_DAILY_HIGH, darkSkyDaily.getHighTemp());
            appValues.put(NsawContract.NsawEntry.COLUMN_DAILY_APPARENT_HIGH, darkSkyDaily.getApparentHigh());
            appValues.put(NsawContract.NsawEntry.COLUMN_DAILY_APPARENT_LOW, darkSkyDaily.getApparentLow());
            appValues.put(NsawContract.NsawEntry.COLUMN_DAILY_DEW, darkSkyDaily.getDewPoint());
            appValues.put(NsawContract.NsawEntry.COLUMN_DAILY_HUMIDITY, darkSkyDaily.getHumidity());
            appValues.put(NsawContract.NsawEntry.COLUMN_DAILY_VISIBILITY, darkSkyDaily.getVisibility());
            if (getContext() != null)
                getContext().getContentResolver().insert(NsawContract.NsawEntry.WEATHER_DAILY_CONTENT_URI, appValues);
        }
    }

    private void removeFromDatabase() {
        if (newsData != null) {
            NewsAPI newsAPI = newsData.get(newsPosition);
            if (getContext() != null)
                getContext().getContentResolver().delete(NsawContract.NsawEntry.NEWS_CONTENT_URI, NsawContract.NsawEntry.COLUMN_NEWS_TITLE + " = " + newsAPI.getTitle(), null);
        } else if (stockData != null) {
            AlphaVantage alphaVantage = stockData.get(stockPosition);
            if (getContext() != null)
                getContext().getContentResolver().delete(NsawContract.NsawEntry.STOCK_CONTENT_URI, NsawContract.NsawEntry.COLUMN_STOCK_ID + " = " + alphaVantage.getCompanyName(), null);
        } else if (weatherCurrentData != null) {
            DarkSkyCurrent darkSkyCurrent = weatherCurrentData.get(weatherPosition);
            if (getContext() != null)
                getContext().getContentResolver().delete(NsawContract.NsawEntry.WEATHER_CURRENT_CONTENT_URI, NsawContract.NsawEntry.COLUMN_CURRENT_ID + " = " + darkSkyCurrent.getSummary(), null);
        } else if (weatherDailyData != null) {
            DarkSkyDaily darkSkyDaily = weatherDailyData.get(weatherPosition);
            if (getContext() != null)
                getContext().getContentResolver().delete(NsawContract.NsawEntry.WEATHER_DAILY_CONTENT_URI, NsawContract.NsawEntry.COLUMN_DAILY_ID + " = " + darkSkyDaily.getSummary(), null);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        NsawApp.getInstance().trackScreenView("DETAIL FRAGMENT");
    }

}