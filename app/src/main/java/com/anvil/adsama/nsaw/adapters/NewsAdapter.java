package com.anvil.adsama.nsaw.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.anvil.adsama.nsaw.R;
import com.anvil.adsama.nsaw.model.NewsAPI;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsHolder> {

    private ArrayList<NewsAPI> mNewsList;
    private Context mContext;

    public NewsAdapter(ArrayList<NewsAPI> newsList, Context context) {
        mNewsList = newsList;
        mContext = context;
    }

    @Override
    public NewsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.news_card_layout, parent, false);
        return new NewsHolder(view);
    }

    @Override
    public void onBindViewHolder(NewsHolder holder, int position) {
        NewsAPI newsAPI = mNewsList.get(position);
        holder.mNewsTitle.setText(newsAPI.getTitle());
        Picasso.with(mContext).load(newsAPI.getImageUrl()).into(holder.mNewsImage);
    }

    @Override
    public int getItemCount() {
        if (mNewsList != null) {
            return mNewsList.size();
        } else {
            return 0;
        }
    }

    class NewsHolder extends RecyclerView.ViewHolder {
        ImageView mNewsImage;
        TextView mNewsTitle;

        NewsHolder(View itemView) {
            super(itemView);
            mNewsImage = itemView.findViewById(R.id.news_image);
            mNewsTitle = itemView.findViewById(R.id.tv_news_title);
        }
    }
}
