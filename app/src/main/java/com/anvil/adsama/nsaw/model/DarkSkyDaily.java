package com.anvil.adsama.nsaw.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DarkSkyDaily implements Parcelable {

    public static final Parcelable.Creator<DarkSkyDaily> CREATOR = new Creator<DarkSkyDaily>() {

        @Override
        public DarkSkyDaily createFromParcel(Parcel source) {
            DarkSkyDaily darkSkyDaily = new DarkSkyDaily();
            darkSkyDaily.mTime = ((long) source.readValue((long.class.getClassLoader())));
            darkSkyDaily.mSummary = ((String) source.readValue((String.class.getClassLoader())));
            darkSkyDaily.mHighTemperature = ((float) source.readValue((float.class.getClassLoader())));
            darkSkyDaily.mLowTemperature = ((float) source.readValue((float.class.getClassLoader())));
            darkSkyDaily.mApparentHigh = ((float) source.readValue((float.class.getClassLoader())));
            darkSkyDaily.mApparentLow = ((float) source.readValue((float.class.getClassLoader())));
            darkSkyDaily.mDewPoint = ((float) source.readValue((float.class.getClassLoader())));
            darkSkyDaily.mHumidity = ((float) source.readValue((float.class.getClassLoader())));
            darkSkyDaily.mVisibility = ((float) source.readValue((float.class.getClassLoader())));
            return darkSkyDaily;
        }

        @Override
        public DarkSkyDaily[] newArray(int size) {
            return new DarkSkyDaily[size];
        }
    };

    @SerializedName("time")
    @Expose
    private long mTime;
    @SerializedName("summary")
    @Expose
    private String mSummary;
    @SerializedName("temperatureHigh")
    @Expose
    private float mHighTemperature;
    @SerializedName("temperatureLow")
    @Expose
    private float mLowTemperature;
    @SerializedName("apparentTemperatureHigh")
    @Expose
    private float mApparentHigh;
    @SerializedName("apparentTemperatureLow")
    @Expose
    private float mApparentLow;
    @SerializedName("dewPoint")
    @Expose
    private float mDewPoint;
    @SerializedName("humidity")
    @Expose
    private float mHumidity;
    @SerializedName("visibility")
    @Expose
    private float mVisibility;

    private DarkSkyDaily() {
    }

    public DarkSkyDaily(long time, String summary, float highTemp, float lowTemp, float apparentHigh, float apparentLow, float dewPoint, float humidity, float visibility) {
        mTime = time;
        mSummary = summary;
        mHighTemperature = highTemp;
        mLowTemperature = lowTemp;
        mApparentHigh = apparentHigh;
        mApparentLow = apparentLow;
        mDewPoint = dewPoint;
        mHumidity = humidity;
        mVisibility = visibility;
    }

    public long getTime() {
        return mTime;
    }


    public String getSummary() {
        return mSummary;
    }

    public float getHighTemp() {
        return mHighTemperature;
    }

    public float getLowTemp() {
        return mLowTemperature;
    }

    public float getApparentHigh() {
        return mApparentHigh;
    }

    public float getApparentLow() {
        return mApparentLow;
    }

    public float getDewPoint() {
        return mDewPoint;
    }

    public float getHumidity() {
        return mHumidity;
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
        dest.writeLong(mTime);
        dest.writeString(mSummary);
        dest.writeFloat(mHighTemperature);
        dest.writeFloat(mLowTemperature);
        dest.writeFloat(mApparentHigh);
        dest.writeFloat(mApparentLow);
        dest.writeFloat(mDewPoint);
        dest.writeFloat(mHumidity);
        dest.writeFloat(mVisibility);
    }
}