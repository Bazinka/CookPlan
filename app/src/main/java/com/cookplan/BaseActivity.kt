package com.cookplan

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem

/**
 * Created by DariaEfimova on 13.10.17.
 */

open class BaseActivity : AppCompatActivity() {

    fun setTitle(title: String) {
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.title = title
        }
        super.setTitle(title)
    }

    fun setSubTitle(subTitle: String) {
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.subtitle = subTitle
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

    fun startActivityWithUpAnimation(intent: Intent) {
        isUpAnimation = true
        startActivity(intent)
        overridePendingTransition(R.anim.slide_in_up_activity, R.anim.slide_out_up_activity)
    }


    fun startActivityForResultWithUpAnimation(intent: Intent, requestCode: Int) {
        startActivityForResult(intent, requestCode)
        overridePendingTransition(R.anim.slide_in_up_activity, R.anim.slide_out_up_activity)
    }

    fun startActivityWithLeftAnimation(intent: Intent) {
        startActivity(intent)
        overridePendingTransition(R.anim.slide_in_left_activity, R.anim.slide_out_left_activity)
    }

    fun startActivityForResultWithLeftAnimation(intent: Intent, requestCode: Int) {
        startActivityForResult(intent, requestCode)
        overridePendingTransition(R.anim.slide_in_left_activity, R.anim.slide_out_left_activity)
    }

    fun hideNavigationIcon() {
        if (supportActionBar != null) {
            supportActionBar!!.setDisplayHomeAsUpEnabled(false)
            supportActionBar!!.setHomeButtonEnabled(false)
        }
    }

    fun setNavigationArrow() {
        if (supportActionBar != null) {
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setHomeButtonEnabled(true)
        }
    }

    override fun onBackPressed() {
        finish()
        if (isUpAnimation) {
            isUpAnimation = false
            overridePendingTransition(R.anim.slide_in_down_activity, R.anim.slide_out_down_activity)

        } else {
            overridePendingTransition(R.anim.slide_in_right_activity, R.anim.slide_out_right_activity)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    companion object {

        private var isUpAnimation = false
    }
}
