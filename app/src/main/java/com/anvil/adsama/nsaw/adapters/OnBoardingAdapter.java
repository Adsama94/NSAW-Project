package com.anvil.adsama.nsaw.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.anvil.adsama.nsaw.R;

public class OnBoardingAdapter extends PagerAdapter {

    private Context mContext;
    private int[] mBackgroundImages = {R.drawable.news_nav, R.drawable.stock_nav, R.drawable.weather_nav};


    public OnBoardingAdapter(Context context) {
        mContext = context;
    }

    @Override
    public int getCount() {
        return mBackgroundImages.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        ImageView imageView = new ImageView(mContext);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setImageResource(mBackgroundImages[position]);
        container.addView(imageView, 0);
        return imageView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ImageView) object);
    }
}