package com.anvil.adsama.nsaw.analytics;

import android.app.Application;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

public class NsawApp extends Application {

    private static NsawApp mNsawInstance;

    public static synchronized NsawApp getInstance() {
        return mNsawInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mNsawInstance = this;
        AppTracker.initialize(this);
        AppTracker.getInstance().get(AppTracker.Target.APP);
    }

    public synchronized Tracker getGoogleAnalyticsTracker() {
        AppTracker appTracker = AppTracker.getInstance();
        return appTracker.get(AppTracker.Target.APP);
    }

    public void trackScreenView(String screenName) {
        Tracker t = getGoogleAnalyticsTracker();
        t.setScreenName(screenName);
        t.send(new HitBuilders.ScreenViewBuilder().build());
        GoogleAnalytics.getInstance(this).dispatchLocalHits();
    }
}