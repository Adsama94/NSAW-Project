package com.anvil.adsama.nsaw.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.anvil.adsama.nsaw.model.NewsAPI;

import java.util.ArrayList;

public class BookmarkNewsAdapter extends RecyclerView.Adapter<BookmarkNewsAdapter.NewsHolder> {

    private Context mContext;
    private ArrayList<NewsAPI> mNewsData = new ArrayList<>();

    public BookmarkNewsAdapter(Context context, ArrayList<NewsAPI> newsAPIArrayList) {
        mContext = context;
        mNewsData = newsAPIArrayList;
    }

    @NonNull
    @Override
    public NewsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull NewsHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class NewsHolder extends RecyclerView.ViewHolder {
        public NewsHolder(View itemView) {
            super(itemView);
        }
    }
}