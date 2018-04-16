package com.anvil.adsama.nsaw.network;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import com.anvil.adsama.nsaw.model.NewsAPI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class NewsLoader extends AsyncTaskLoader<ArrayList<NewsAPI>> {

    private static final String LOG_TAG = NewsLoader.class.getSimpleName();
    private ArrayList<NewsAPI> newsList = new ArrayList<>();
    private String mUrl;

    public NewsLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public ArrayList<NewsAPI> loadInBackground() {
        if (mUrl != null) {
            NewsParser newsParser = new NewsParser();
            JSONObject searchData = newsParser.getNewsSearch(mUrl);
            if (searchData != null) {
                try {
                    JSONArray articleArray = searchData.getJSONArray("articles");
                    ArrayList<NewsAPI> newsApiList = new ArrayList<>();
                    for (int j = 0; j < articleArray.length(); j++) {
                        JSONObject currentNewsItem = articleArray.getJSONObject(j);
                        String authorName = currentNewsItem.optString("author");
                        String title = currentNewsItem.optString("title");
                        String description = currentNewsItem.optString("description");
                        String articleUrl = currentNewsItem.optString("url");
                        String imageUrl = currentNewsItem.optString("urlToImage");
                        String date = currentNewsItem.optString("publishedAt");
                        NewsAPI newsAPI = new NewsAPI(authorName, title, description, articleUrl, imageUrl, date);
                        newsApiList.add(newsAPI);
                        newsApiList = newsList;
                        Log.d(LOG_TAG, "SEARCH DATA IS " + newsApiList);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e(LOG_TAG, "Error occurred fetching news data " + e.getMessage());
                }
            }
            return newsList;
        } else {
            NewsParser newsParser = new NewsParser();
            JSONObject newsData = newsParser.getNewsData();
            if (newsData != null) {
                try {
                    JSONArray articleArray = newsData.getJSONArray("articles");
                    ArrayList<NewsAPI> newsApiList = new ArrayList<>();
                    for (int j = 0; j < articleArray.length(); j++) {
                        JSONObject currentNewsItem = articleArray.getJSONObject(j);
                        String authorName = currentNewsItem.optString("author");
                        String title = currentNewsItem.optString("title");
                        String description = currentNewsItem.optString("description");
                        String articleUrl = currentNewsItem.optString("url");
                        String imageUrl = currentNewsItem.optString("urlToImage");
                        String date = currentNewsItem.optString("publishedAt");
                        NewsAPI newsAPI = new NewsAPI(authorName, title, description, articleUrl, imageUrl, date);
                        newsApiList.add(newsAPI);
                        newsApiList = newsList;
                        Log.d(LOG_TAG, "DATA IS " + newsApiList);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e(LOG_TAG, "Error occurred fetching news data " + e.getMessage());
                }
            }
            return newsList;
        }
    }
}