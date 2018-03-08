package com.cookplan

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import com.cookplan.models.ProductCategory
import com.cookplan.utils.FillProductDatabaseProvider
import com.crashlytics.android.Crashlytics
import com.google.gson.Gson
import io.fabric.sdk.android.Fabric
import net.danlew.android.joda.JodaTimeAndroid
import java.util.*


/**
 * Created by DariaEfimova on 17.10.16.
 */

class RApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Fabric.with(this, Crashlytics())
        JodaTimeAndroid.init(this)
        appContext = applicationContext
        if (getPriorityList().isEmpty()) {
            FillProductDatabaseProvider.savePriorityList()
        }
    }

    companion object {

        // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
        private val TWITTER_KEY = "0l4bGspRK4LNZQIwMx9AaWA1W"
        private val TWITTER_SECRET = "NiNrGfXFdhVFMIw6Y1U4nue30TAU321CZWUZ2VWrhokIRNRJpm"

        val PREFS_NAME = "COOK_PLAN_APP"
        private val CATEGORY_PRIORITY_PREFS_NAME = "CATEGORY_PRIORITY_PREFS_NAME"
        private val USER_SAW_VOICE_RECOGNITION_ALERT_PREFS_NAME = "USER_SAW_VOICE_RECOGNITION_ALERT_PREFS_NAME"

        var appContext: Context? = null
            private set


        private val currentLocale: Locale
            get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                appContext!!.resources.configuration.locales.get(0)
            } else {
                appContext!!.resources.configuration.locale
            }

        val isCurrentLocaleRus: Boolean
            get() {
                val currentLanguage = RApplication.currentLocale.displayLanguage
                val russianLanguage = RApplication.appContext!!.getString(R.string.russian_language)
                return currentLanguage == russianLanguage
            }

        fun savePriorityList(priorityOfCategories: List<ProductCategory>) {
            val settings: SharedPreferences? = appContext?.getSharedPreferences(PREFS_NAME,
                    Context.MODE_PRIVATE)
            val editor: SharedPreferences.Editor?

            editor = settings?.edit()

            val jsonFavorites = Gson().toJson(priorityOfCategories)

            editor?.putString(CATEGORY_PRIORITY_PREFS_NAME, jsonFavorites)

            editor?.commit()
        }

        fun getPriorityList(): List<ProductCategory> {
            val settings: SharedPreferences? = appContext?.getSharedPreferences(PREFS_NAME,
                    Context.MODE_PRIVATE)
            var priorityList: List<ProductCategory> = arrayListOf()

            if (settings?.contains(CATEGORY_PRIORITY_PREFS_NAME) == true) {
                val json = settings.getString(CATEGORY_PRIORITY_PREFS_NAME, null)
                val items = Gson().fromJson(json,
                        Array<ProductCategory>::class.java)

                priorityList = Arrays.asList(*items)
            }

            return priorityList
        }

        fun saveUserSawVoiceAlert(isUserSawVoiceAlert: Boolean) {
            val settings: SharedPreferences? = appContext?.getSharedPreferences(PREFS_NAME,
                    Context.MODE_PRIVATE)
            val editor: SharedPreferences.Editor?

            editor = settings?.edit()

            editor?.putBoolean(USER_SAW_VOICE_RECOGNITION_ALERT_PREFS_NAME, isUserSawVoiceAlert)

            editor?.commit()
        }

        fun isUserSawVoiceAlert(): Boolean {
            val settings: SharedPreferences? = appContext?.getSharedPreferences(PREFS_NAME,
                    Context.MODE_PRIVATE)

            return settings?.getBoolean(USER_SAW_VOICE_RECOGNITION_ALERT_PREFS_NAME, false) ?: false
        }
    }
}
