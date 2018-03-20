package com.anvil.adsama.nsaw.analytics;

import android.app.Application;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.StandardExceptionParser;
import com.google.android.gms.analytics.Tracker;

public class NsawApp extends Application {

    public static final String TAG = NsawApp.class.getSimpleName();
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

    public void trackException(Exception e) {
        if (e != null) {
            Tracker t = getGoogleAnalyticsTracker();
            t.send(new HitBuilders.ExceptionBuilder().setDescription(new StandardExceptionParser(this, null).getDescription(Thread.currentThread().getName(), e)).setFatal(false).build());
        }
    }

    public void trackEvent(String category, String action, String label) {
        Tracker t = getGoogleAnalyticsTracker();
        t.send(new HitBuilders.EventBuilder().setCategory(category).setAction(action).setLabel(label).build());
    }
}