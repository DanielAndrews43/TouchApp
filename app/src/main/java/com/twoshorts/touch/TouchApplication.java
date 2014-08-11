package com.twoshorts.touch;

//copied mostly from http://www.javacodegeeks.com/2014/04/working-with-google-analytics-api-v4-for-android.html

import android.app.Application;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

import java.util.HashMap;

public class TouchApplication extends Application {
    private static final String PROPERTY_ID = "UA-53642745-2";
    public final static String TAG = "TouchApp";
    public static int GENERAL_TRACKER = 0;

    public enum TrackerName {
        APP_TRACKER
    }

    HashMap<TrackerName, Tracker> mTrackers = new HashMap<TrackerName, Tracker>();

    public TouchApplication(){
        super();
    }

    public synchronized Tracker getTracker(TrackerName trackerId) {
        if (!mTrackers.containsKey(trackerId)) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            Tracker t = analytics.newTracker(R.xml.app_tracker);
            mTrackers.put(trackerId, t);
        }
        return mTrackers.get(trackerId);
    }
}
