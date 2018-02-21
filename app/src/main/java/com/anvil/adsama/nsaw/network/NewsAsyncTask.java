package com.anvil.adsama.nsaw.network;

import android.os.AsyncTask;
import android.util.Log;

import com.anvil.adsama.nsaw.model.NewsAPI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class NewsAsyncTask extends AsyncTask<Void, Void, ArrayList<NewsAPI>> {

    private static final String LOG_TAG = NewsAsyncTask.class.getSimpleName();
    private NewsListener mNewsListener;

    public NewsAsyncTask(NewsListener newsListener) {
        mNewsListener = newsListener;
    }

    @Override
    protected ArrayList<NewsAPI> doInBackground(Void... voids) {
        ArrayList<NewsAPI> newsList = new ArrayList<>();
        JSONObject newsData = NewsParser.getNewsData();
        try {
            if (newsData != null)
                for (int i = 0; i < newsData.length(); i++) {
                    JSONArray articleArray = newsData.getJSONArray("articles");
                    ArrayList<NewsAPI> newsApiList = new ArrayList<>();
                    for (int j = 0; j < articleArray.length(); j++) {
                        JSONObject currentNewsItem = articleArray.getJSONObject(j);
                        String authorName = currentNewsItem.getString("author");
                        String title = currentNewsItem.getString("title");
                        String description = currentNewsItem.getString("description");
                        String imageUrl = currentNewsItem.getString("urlToImage");
                        String date = currentNewsItem.getString("publishedAt");
                        NewsAPI newsAPI = new NewsAPI(authorName, title, description, imageUrl, date);
                        newsApiList.add(newsAPI);
                        Log.d(LOG_TAG, "DATA IS " + newsApiList);
                    }
                }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(LOG_TAG, "Error occurred fetching news data " + e.getMessage());
        }
        return newsList;
    }

    @Override
    protected void onPostExecute(ArrayList<NewsAPI> newsAPIArrayList) {
        super.onPostExecute(newsAPIArrayList);
        mNewsListener.returnNewsList(newsAPIArrayList);
    }
}