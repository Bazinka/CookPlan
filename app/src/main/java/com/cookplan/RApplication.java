package com.cookplan;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.bettervectordrawable.VectorDrawableCompat;
import com.cookplan.models.ProductCategory;
import com.cookplan.utils.FillProductDatabaseProvider;
import com.crashlytics.android.Crashlytics;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.credentials.CredentialsApi;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;

import net.danlew.android.joda.JodaTimeAndroid;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.fabric.sdk.android.Fabric;


/**
 * Created by DariaEfimova on 17.10.16.
 */

public class RApplication extends Application {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "0l4bGspRK4LNZQIwMx9AaWA1W";
    private static final String TWITTER_SECRET = "NiNrGfXFdhVFMIw6Y1U4nue30TAU321CZWUZ2VWrhokIRNRJpm";

    public static final String PREFS_NAME = "COOK_PLAN_APP";
    private static final String CATEGORY_PRIORITY_PREFS_NAME = "CATEGORY_PRIORITY_PREFS_NAME";

    private static Context context;

    public static Context getAppContext() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        JodaTimeAndroid.init(this);
        findAllVectorResourceIdsSlow();
        context = getApplicationContext();
        if (getPriorityList() == null) {
            FillProductDatabaseProvider.savePriorityList();
        }
    }

    public static void savePriorityList(List<ProductCategory> priorityOfCategories) {
        SharedPreferences settings;
        SharedPreferences.Editor editor;

        settings = context.getSharedPreferences(PREFS_NAME,
                                                Context.MODE_PRIVATE);
        editor = settings.edit();

        Gson gson = new Gson();
        String jsonFavorites = gson.toJson(priorityOfCategories);

        editor.putString(CATEGORY_PRIORITY_PREFS_NAME, jsonFavorites);

        editor.commit();
    }

    public static ArrayList<ProductCategory> getPriorityList() {
        SharedPreferences settings;
        List<ProductCategory> priorityList;

        settings = context.getSharedPreferences(PREFS_NAME,
                                                Context.MODE_PRIVATE);

        if (settings.contains(CATEGORY_PRIORITY_PREFS_NAME)) {
            String json = settings.getString(CATEGORY_PRIORITY_PREFS_NAME, null);
            Gson gson = new Gson();
            ProductCategory[] items = gson.fromJson(json,
                                                    ProductCategory[].class);

            priorityList = Arrays.asList(items);
            priorityList = new ArrayList<>(priorityList);
        } else
            return null;

        return (ArrayList<ProductCategory>) priorityList;
    }

    private void findAllVectorResourceIdsSlow() {
        int[] ids = VectorDrawableCompat.findAllVectorResourceIdsSlow(getResources(), R.drawable.class);
        VectorDrawableCompat.enableResourceInterceptionFor(getResources(), ids);
    }

    public static FirebaseApp getFirebaseApp() {
        return FirebaseApp.getInstance();
    }

    public static FirebaseAuth getFirebaseAuth() {
        return FirebaseAuth.getInstance(getFirebaseApp());
    }

    public static CredentialsApi getCredentialsApi() {
        return Auth.CredentialsApi;
    }

    public static FirebaseUser getCurrentUser() {
        return getFirebaseAuth().getCurrentUser();
    }

}
