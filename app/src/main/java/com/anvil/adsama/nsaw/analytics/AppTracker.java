package com.anvil.adsama.nsaw.analytics;

import android.content.Context;

import com.anvil.adsama.nsaw.R;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

import java.util.HashMap;
import java.util.Map;

final class AppTracker {

    private static AppTracker mAppTrackerInstance;
    private final Map<Target, Tracker> mTrackers = new HashMap<>();
    private final Context mContext;

    private AppTracker(Context context) {
        mContext = context.getApplicationContext();
    }

    static synchronized void initialize(Context context) {
        if (mAppTrackerInstance != null) {
            throw new IllegalStateException("Extra call to initialize analytics trackers");
        }
        mAppTrackerInstance = new AppTracker(context);
    }

    static synchronized AppTracker getInstance() {
        if (mAppTrackerInstance == null) {
            throw new IllegalStateException("Call initialize() before getInstance()");
        }
        return mAppTrackerInstance;
    }

    synchronized Tracker get(Target target) {
        if (!mTrackers.containsKey(target)) {
            Tracker tracker;
            switch (target) {
                case APP:
                    tracker = GoogleAnalytics.getInstance(mContext).newTracker(R.xml.global_tracker);
                    break;
                default:
                    throw new IllegalArgumentException("Unhandled analytics target " + target);
            }
            mTrackers.put(target, tracker);
        }
        return mTrackers.get(target);
    }

    public enum Target {
        APP
    }
}