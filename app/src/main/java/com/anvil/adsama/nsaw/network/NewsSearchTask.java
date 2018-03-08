package com.anvil.adsama.nsaw.network;

import android.os.AsyncTask;
import android.util.Log;

import com.anvil.adsama.nsaw.model.NewsAPI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class NewsSearchTask extends AsyncTask<String, Void, ArrayList<NewsAPI>> {

    private static final String LOG_TAG = NewsSearchTask.class.getSimpleName();
    private NewsListener mNewsListener;
    private ArrayList<NewsAPI> newsList = new ArrayList<>();

    public NewsSearchTask(NewsListener newsListener) {
        mNewsListener = newsListener;
    }

    @Override
    protected ArrayList<NewsAPI> doInBackground(String... voids) {
        NewsParser newsParser = new NewsParser();
        JSONObject searchData = newsParser.getNewsSearch(voids[0]);
        if (searchData != null) {
            try {
                JSONArray articleArray = searchData.getJSONArray("articles");
                ArrayList<NewsAPI> newsApiList = new ArrayList<>();
                for (int j = 0; j < articleArray.length(); j++) {
                    JSONObject currentNewsItem = articleArray.getJSONObject(j);
                    String authorName = currentNewsItem.optString("author");
                    String title = currentNewsItem.optString("title");
                    String description = currentNewsItem.optString("description");
                    String imageUrl = currentNewsItem.optString("urlToImage");
                    String date = currentNewsItem.optString("publishedAt");
                    NewsAPI newsAPI = new NewsAPI(authorName, title, description, imageUrl, date);
                    newsApiList.add(newsAPI);
                    newsApiList = newsList;
                    Log.d(LOG_TAG, "DATA IS " + newsApiList);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e(LOG_TAG, "Error occurred fetching searched news data " + e.getMessage());
            }
        }
        return newsList;
    }

    @Override
    protected void onPostExecute(ArrayList<NewsAPI> newsAPIArrayList) {
        super.onPostExecute(newsAPIArrayList);
        mNewsListener.returnNewsList(newsAPIArrayList);
    }
}