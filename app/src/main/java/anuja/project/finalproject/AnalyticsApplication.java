package anuja.project.finalproject;

import android.app.Application;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Logger;
import com.google.android.gms.analytics.Tracker;

/**
 * Created by USER on 08-11-2017.
 */

public class AnalyticsApplication extends Application {
    public Tracker mTracker;

    public void startTracking() {
        // Initialize an Analytics tracker using a Google Analytics property ID.

        // Does the Tracker already exist?
        // If not, create it
        if (mTracker == null) {
            GoogleAnalytics ga = GoogleAnalytics.getInstance(this);
            // Get the config data for the tracker
            mTracker = ga.newTracker(R.xml.global_tracker);
            // Enable tracking of activities
            ga.enableAutoActivityReports(this);
            // Set the log level to verbose.
            ga.getLogger().setLogLevel(Logger.LogLevel.VERBOSE);
        }
    }

}