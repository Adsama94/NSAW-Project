package com.anvil.adsama.nsaw.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class DarkSkyCurrent implements Parcelable {

    public static final Parcelable.Creator<DarkSkyCurrent> CREATOR = new Creator<DarkSkyCurrent>() {

        @Override
        public DarkSkyCurrent createFromParcel(Parcel source) {
            DarkSkyCurrent darkSky = new DarkSkyCurrent();
            double d = 0.0;
            float f = (float) d;
            DarkSkyDaily darkSkyDaily = new DarkSkyDaily(0, "", f, f, f, f, f, f, f);
            darkSky.mDailyList.add(darkSkyDaily);
            darkSky.mSummary = ((String) source.readValue((String.class.getClassLoader())));
            darkSky.mWeeklySummary = ((String) source.readValue((String.class.getClassLoader())));
            darkSky.mTemperature = ((float) source.readValue((float.class.getClassLoader())));
            darkSky.mIcon = ((String) source.readValue((String.class.getClassLoader())));
            darkSky.mWindSpeed = ((float) source.readValue((float.class.getClassLoader())));
            darkSky.mVisibility = ((float) source.readValue((float.class.getClassLoader())));
            source.readList(darkSky.mDailyList, (com.anvil.adsama.nsaw.model.DarkSkyDaily.class.getClassLoader()));
            return darkSky;
        }

        @Override
        public DarkSkyCurrent[] newArray(int size) {
            return new DarkSkyCurrent[size];
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
    @Expose
    private String mWeeklySummary;
    @SerializedName("daily")
    @Expose
    private ArrayList<DarkSkyDaily> mDailyList = new ArrayList<>();

    private DarkSkyCurrent() {
    }

    public DarkSkyCurrent(String summary, float temperature, String icon, float windSpeed, float visibility, String weeklySummary, ArrayList<DarkSkyDaily> dailyData) {
        mSummary = summary;
        mWeeklySummary = weeklySummary;
        mTemperature = temperature;
        mIcon = icon;
        mWindSpeed = windSpeed;
        mVisibility = visibility;
        mDailyList = dailyData;
    }

    public String getSummary() {
        return mSummary;
    }

    public String getWeeklySummary() {
        return mWeeklySummary;
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

    public ArrayList<DarkSkyDaily> getDailyList() {
        return mDailyList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(mSummary);
        dest.writeValue(mWeeklySummary);
        dest.writeValue(mTemperature);
        dest.writeValue(mIcon);
        dest.writeValue(mWindSpeed);
        dest.writeValue(mVisibility);
        dest.writeList(mDailyList);
    }
}