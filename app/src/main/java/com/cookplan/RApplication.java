package com.cookplan;

import android.app.Application;

import com.bettervectordrawable.VectorDrawableCompat;
import com.crashlytics.android.Crashlytics;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;

import net.danlew.android.joda.JodaTimeAndroid;

import io.fabric.sdk.android.Fabric;


/**
 * Created by DariaEfimova on 17.10.16.
 */

public class RApplication extends Application {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "0l4bGspRK4LNZQIwMx9AaWA1W";
    private static final String TWITTER_SECRET = "NiNrGfXFdhVFMIw6Y1U4nue30TAU321CZWUZ2VWrhokIRNRJpm";



    @Override
    public void onCreate() {
        super.onCreate();
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig), new Crashlytics());
        JodaTimeAndroid.init(this);
        findAllVectorResourceIdsSlow();
    }

    private void findAllVectorResourceIdsSlow() {
        int[] ids = VectorDrawableCompat.findAllVectorResourceIdsSlow(getResources(), R.drawable.class);
        VectorDrawableCompat.enableResourceInterceptionFor(getResources(), ids);
    }

}
