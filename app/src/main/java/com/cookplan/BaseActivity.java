package com.cookplan;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.cookplan.auth.IdpResponse;
import com.cookplan.auth.ResultCodes;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by DariaEfimova on 17.10.16.
 */

public class BaseActivity extends AppCompatActivity {

    private static boolean isUpAnimation = false;

    public void setTitle(String title) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(title);
        }
        super.setTitle(title);
    }

    public void setSubTitle(String subTitle) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setSubtitle(subTitle);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    public void startActivityWithUpAnimation(Intent intent) {
        isUpAnimation = true;
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_up_activity, R.anim.slide_out_up_activity);
    }


    public void startActivityForResultWithUpAnimation(Intent intent, int requestCode) {
        startActivityForResult(intent, requestCode);
        overridePendingTransition(R.anim.slide_in_up_activity, R.anim.slide_out_up_activity);
    }

    public void startActivityWithLeftAnimation(Intent intent) {
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left_activity, R.anim.slide_out_left_activity);
    }

    public void startActivityForResultWithLeftAnimation(Intent intent, int requestCode) {
        startActivityForResult(intent, requestCode);
        overridePendingTransition(R.anim.slide_in_left_activity, R.anim.slide_out_left_activity);
    }

    public void hideNavigationIcon() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setHomeButtonEnabled(false);
        }
    }

    public void setNavigationArrow() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        if (isUpAnimation) {
            isUpAnimation = false;
            overridePendingTransition(R.anim.slide_in_down_activity, R.anim.slide_out_down_activity);

        } else {
            overridePendingTransition(R.anim.slide_in_right_activity, R.anim.slide_out_right_activity);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
