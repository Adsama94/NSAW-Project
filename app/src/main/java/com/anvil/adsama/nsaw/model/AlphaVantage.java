package com.anvil.adsama.nsaw.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AlphaVantage implements Parcelable {

    public static final Parcelable.Creator<AlphaVantage> CREATOR = new Creator<AlphaVantage>() {

        @Override
        public AlphaVantage createFromParcel(Parcel source) {
            AlphaVantage alphaVantage = new AlphaVantage();
            alphaVantage.mCompanyName = ((String) source.readValue((String.class.getClassLoader())));
            alphaVantage.mLastRefreshTime = ((String) source.readValue((String.class.getClassLoader())));
            alphaVantage.mOpen = ((float) source.readValue((float.class.getClassLoader())));
            alphaVantage.mHigh = ((float) source.readValue((float.class.getClassLoader())));
            alphaVantage.mLow = ((float) source.readValue((float.class.getClassLoader())));
            alphaVantage.mClose = ((float) source.readValue((float.class.getClassLoader())));
            alphaVantage.mVolume = ((float) source.readValue((float.class.getClassLoader())));
            return alphaVantage;
        }

        @Override
        public AlphaVantage[] newArray(int size) {
            return new AlphaVantage[size];
        }
    };
    @SerializedName("2. Symbol")
    @Expose
    private String mCompanyName;
    @SerializedName("3. Last Refreshed")
    @Expose
    private String mLastRefreshTime;
    @SerializedName("1. open")
    @Expose
    private float mOpen;
    @SerializedName("2. high")
    @Expose
    private float mHigh;
    @SerializedName("2. low")
    @Expose
    private float mLow;
    @SerializedName("2. close")
    @Expose
    private float mClose;
    @SerializedName("5. volume")
    @Expose
    private float mVolume;

    private AlphaVantage() {
    }

    public AlphaVantage(String companyName, String refreshTime, float open, float high, float low, float close, float volume) {
        mCompanyName = companyName;
        mLastRefreshTime = refreshTime;
        mOpen = open;
        mHigh = high;
        mLow = low;
        mClose = close;
        mVolume = volume;
    }

    public String getCompanyName() {
        return mCompanyName;
    }

    public String getRefreshTime() {
        return mLastRefreshTime;
    }

    public float getOpen() {
        return mOpen;
    }

    public float getHigh() {
        return mHigh;
    }

    public float getLow() {
        return mLow;
    }

    public float getClose() {
        return mClose;
    }

    public float getVolume() {
        return mVolume;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(mCompanyName);
        dest.writeValue(mLastRefreshTime);
        dest.writeValue(mOpen);
        dest.writeValue(mHigh);
        dest.writeValue(mLow);
        dest.writeValue(mClose);
        dest.writeValue(mVolume);
    }
}