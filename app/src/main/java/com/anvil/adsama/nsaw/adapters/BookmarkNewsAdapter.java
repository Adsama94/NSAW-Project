package com.anvil.adsama.nsaw.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.anvil.adsama.nsaw.R;
import com.anvil.adsama.nsaw.database.NsawContract;
import com.anvil.adsama.nsaw.model.NewsAPI;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class BookmarkNewsAdapter extends RecyclerView.Adapter<BookmarkNewsAdapter.NewsHolder> {

    private Context mContext;
    private ArrayList<NewsAPI> mNewsData;

    public BookmarkNewsAdapter(Context context, ArrayList<NewsAPI> newsAPIArrayList) {
        mContext = context;
        mNewsData = newsAPIArrayList;
    }

    @NonNull
    @Override
    public NewsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.bookmark_news_item, parent, false);
        return new NewsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsHolder holder, int position) {
        NewsAPI newsAPI = mNewsData.get(position);
        Picasso.with(mContext).load(newsAPI.getImageUrl()).into(holder.newsImage);
        holder.newsTitle.setText(newsAPI.getTitle());
        holder.newsDescription.setText(newsAPI.getDescription());
    }

    @Override
    public int getItemCount() {
        if (mNewsData != null) {
            return mNewsData.size();
        } else return 0;
    }

    public void addNews(Cursor cursor) {
        mNewsData.clear();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                String author = cursor.getString(NsawContract.NsawEntry.COL_NEWS_AUTHOR);
                String title = cursor.getString(NsawContract.NsawEntry.COL_NEWS_TITLE);
                String description = cursor.getString(NsawContract.NsawEntry.COL_NEWS_DESCRIPTION);
                String articleUrl = cursor.getString(NsawContract.NsawEntry.COL_NEWS_URL);
                String date = cursor.getString(NsawContract.NsawEntry.COL_NEWS_DATE);
                String imageUrl = cursor.getString(NsawContract.NsawEntry.COL_NEWS_IMGURL);
                NewsAPI newsAPI = new NewsAPI(author, title, description, articleUrl, imageUrl, date);
                mNewsData.add(newsAPI);
            } while (cursor.moveToNext());
        }
        notifyDataSetChanged();
    }

    class NewsHolder extends RecyclerView.ViewHolder {

        ImageView newsImage;
        TextView newsTitle;
        TextView newsDescription;

        NewsHolder(View itemView) {
            super(itemView);
            newsImage = itemView.findViewById(R.id.iv_bookmark_news);
            newsTitle = itemView.findViewById(R.id.tv_bookmark_title);
            newsDescription = itemView.findViewById(R.id.tv_bookmark_description);
        }
    }
}