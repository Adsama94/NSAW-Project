package com.anvil.adsama.nsaw.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NewsAPI implements Parcelable {

    public static final Parcelable.Creator<NewsAPI> CREATOR = new Creator<NewsAPI>() {

        public NewsAPI createFromParcel(Parcel parcel) {
            NewsAPI newsAPI = new NewsAPI();
            newsAPI.mAuthorName = ((String) parcel.readValue((String.class.getClassLoader())));
            newsAPI.mTitle = ((String) parcel.readValue((String.class.getClassLoader())));
            newsAPI.mDescription = ((String) parcel.readValue((String.class.getClassLoader())));
            newsAPI.mImageUrl = ((String) parcel.readValue((String.class.getClassLoader())));
            newsAPI.mDate = ((String) parcel.readValue((String.class.getClassLoader())));
            return newsAPI;
        }

        public NewsAPI[] newArray(int size) {
            return (new NewsAPI[size]);
        }
    };
    @SerializedName("author")
    @Expose
    private String mAuthorName;
    @SerializedName("title")
    @Expose
    private String mTitle;
    @SerializedName("description")
    @Expose
    private String mDescription;
    @SerializedName("urlToImage")
    @Expose
    private String mImageUrl;
    @SerializedName("publishedAt")
    @Expose
    private String mDate;

    private NewsAPI() {
    }

    public NewsAPI(String authorName, String title, String description, String imageUrl, String date) {
        mAuthorName = authorName;
        mTitle = title;
        mDescription = description;
        mImageUrl = imageUrl;
        mDate = date;
    }

    public String getAuthorName() {
        return mAuthorName;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getDescription() {
        return mDescription;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public String getDate() {
        return mDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(mAuthorName);
        dest.writeValue(mTitle);
        dest.writeValue(mDescription);
        dest.writeValue(mImageUrl);
        dest.writeValue(mDate);
    }
}