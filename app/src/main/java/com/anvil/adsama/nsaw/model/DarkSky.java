package com.anvil.adsama.nsaw.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DarkSky implements Parcelable {

    public static final Parcelable.Creator<DarkSky> CREATOR = new Creator<DarkSky>() {

        @Override
        public DarkSky createFromParcel(Parcel source) {
            DarkSky darkSky = new DarkSky();
            darkSky.mSummary = ((String) source.readValue((String.class.getClassLoader())));
            darkSky.mTemperature = ((float) source.readValue((float.class.getClassLoader())));
            darkSky.mIcon = ((String) source.readValue((String.class.getClassLoader())));
            darkSky.mWindSpeed = ((float) source.readValue((float.class.getClassLoader())));
            darkSky.mVisibility = ((float) source.readValue((float.class.getClassLoader())));
            return darkSky;
        }

        @Override
        public DarkSky[] newArray(int size) {
            return new DarkSky[size];
        }
    };
    @SerializedName("summary")
    @Expose
    private String mSummary;
    @SerializedName("temperature")
    @Expose
    private float mTemperature;
    @SerializedName("icon")
    @Expose
    private String mIcon;
    @SerializedName("windSpeed")
    @Expose
    private float mWindSpeed;
    @SerializedName("visibility")
    @Expose
    private float mVisibility;

    private DarkSky() {
    }

    public DarkSky(String summary, float temperature, String icon, float windSpeed, float visibility) {
        mSummary = summary;
        mTemperature = temperature;
        mIcon = icon;
        mWindSpeed = windSpeed;
        mVisibility = visibility;
    }

    public String getSummary() {
        return mSummary;
    }

    public float getTemperature() {
        return mTemperature;
    }

    public String getIcon() {
        return mIcon;
    }

    public float getWindSpeed() {
        return mWindSpeed;
    }

    public float getVisibility() {
        return mVisibility;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(mSummary);
        dest.writeValue(mTemperature);
        dest.writeValue(mIcon);
        dest.writeValue(mWindSpeed);
        dest.writeValue(mVisibility);
    }
}