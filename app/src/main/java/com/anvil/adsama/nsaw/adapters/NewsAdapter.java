package com.anvil.adsama.nsaw.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
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
    private NewsPositionInterface mPositionInterface;

    public NewsAdapter(ArrayList<NewsAPI> newsList, Context context, NewsPositionInterface positionInterface) {
        mNewsList = newsList;
        mContext = context;
        mPositionInterface = positionInterface;
    }

    @NonNull
    @Override
    public NewsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.news_list_item, parent, false);
        return new NewsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final NewsHolder holder, int position) {
        NewsAPI newsAPI = mNewsList.get(position);
        holder.mNewsTitle.setText(newsAPI.getTitle());
        if (newsAPI.getImageUrl().isEmpty()) {
            holder.mNewsImage.setImageResource(R.drawable.news_nav);
        } else {
            Picasso.with(mContext).load(newsAPI.getImageUrl()).error(R.drawable.ic_error_image).into(holder.mNewsImage);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPositionInterface.getNewsPosition(holder.getAdapterPosition());
            }
        });
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
            mNewsImage = itemView.findViewById(R.id.iv_news_image);
            mNewsTitle = itemView.findViewById(R.id.tv_news_title);
        }
    }
}
