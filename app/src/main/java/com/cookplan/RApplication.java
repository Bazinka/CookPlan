package com.cookplan;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

import com.bettervectordrawable.VectorDrawableCompat;
import com.cookplan.models.ProductCategory;
import com.cookplan.utils.FillProductDatabaseProvider;
import com.crashlytics.android.Crashlytics;
import com.google.gson.Gson;

import net.danlew.android.joda.JodaTimeAndroid;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

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
    private static final String IS_ANONYMOUS_POSSIBLE_PREFS_NAME = "IS_ANONYMOUS_POSSIBLE_PREFS_NAME";
    private static final String IS_COOK_PLAN_NOTIFICATION_TURNED_ON = "IS_COOK_PLAN_NOTIFICATION_TURNED_ON";

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
        if (!isAnonymousPossibleSaved()) {
            saveAnonymousPossibility(true);
        }
    }

    private static Locale getCurrentLocale() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return getAppContext().getResources().getConfiguration().getLocales().get(0);
        } else {
            //noinspection deprecation
            return getAppContext().getResources().getConfiguration().locale;
        }
    }

    public static boolean isCurrentLocaleRus() {
        String currentLanguage = RApplication.getCurrentLocale().getDisplayLanguage();
        String russianLanguage = RApplication.getAppContext().getString(R.string.russian_language);
        if (currentLanguage.equals(russianLanguage)) {
            return true;
        } else {
            return false;
        }
    }

    public static void saveAnonymousPossibility(boolean isPossible) {
        SharedPreferences settings;
        SharedPreferences.Editor editor;

        settings = context.getSharedPreferences(PREFS_NAME,
                                                Context.MODE_PRIVATE);
        editor = settings.edit();

        editor.putBoolean(IS_ANONYMOUS_POSSIBLE_PREFS_NAME, isPossible);
        editor.commit();
    }

    private static boolean isAnonymousPossibleSaved() {
        SharedPreferences settings;
        settings = context.getSharedPreferences(PREFS_NAME,
                                                Context.MODE_PRIVATE);

        return settings.contains(IS_ANONYMOUS_POSSIBLE_PREFS_NAME);
    }

    public static boolean isAnonymousPossible() {
        SharedPreferences settings;

        settings = context.getSharedPreferences(PREFS_NAME,
                                                Context.MODE_PRIVATE);

        return settings.getBoolean(IS_ANONYMOUS_POSSIBLE_PREFS_NAME, true);
    }

    public static void saveCookPlanNotificationChanged(boolean isNotificationTurnedOn) {
        SharedPreferences settings;
        SharedPreferences.Editor editor;

        settings = context.getSharedPreferences(PREFS_NAME,
                                                Context.MODE_PRIVATE);
        editor = settings.edit();

        editor.putBoolean(IS_COOK_PLAN_NOTIFICATION_TURNED_ON, isNotificationTurnedOn);
        editor.commit();
    }

    public static boolean isCookPlanNotificationTurnedOn() {
        SharedPreferences settings;

        settings = context.getSharedPreferences(PREFS_NAME,
                                                Context.MODE_PRIVATE);

        return settings.getBoolean(IS_COOK_PLAN_NOTIFICATION_TURNED_ON, false);
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
}
