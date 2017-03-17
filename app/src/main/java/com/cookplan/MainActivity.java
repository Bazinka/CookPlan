package com.cookplan;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cookplan.auth.FirebaseAuthActivity;
import com.cookplan.recipe_list.RecipeGridFragment;
import com.cookplan.recipe_new.NewRecipeDescActivity;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private int mSelectedNavigationId;
    private View mRootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        mRootView = findViewById(R.id.drawer_layout);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        View headerView = navigationView.getHeaderView(0);
        if (auth != null && auth.getCurrentUser() != null) {
            FirebaseUser user = auth.getCurrentUser();
            if (user.getPhotoUrl() != null) {
                ImageView photoImageView = (ImageView) headerView.findViewById(R.id.user_photo_imageView);
                Picasso.with(this).load(user.getPhotoUrl().getPath()).placeholder(R.drawable.logo).into(photoImageView);
            }
            if (user.getDisplayName() != null) {
                TextView nameTextView = (TextView) headerView.findViewById(R.id.user_name_textView);
                nameTextView.setText(user.getDisplayName());
            }
        }

        navigationView.setNavigationItemSelectedListener(this);

        mSelectedNavigationId = R.id.nav_recipe_list;
        navigationView.setCheckedItem(mSelectedNavigationId);
        setRecipeListFragment();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.add_recipe_fab);
        fab.setOnClickListener(view -> {
            startNewPointActivity();
        });
    }

    void startNewPointActivity() {
        Intent intent = new Intent(this, NewRecipeDescActivity.class);
        startActivityWithLeftAnimation(intent);
    }

    void setRecipeListFragment() {
        setTitle(getString(R.string.recipe_list_menu_title));
        RecipeGridFragment pointListFragment = RecipeGridFragment.newInstance();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, pointListFragment);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        mSelectedNavigationId = item.getItemId();

        if (mSelectedNavigationId == R.id.nav_recipe_list) {
            setRecipeListFragment();
        } else if (mSelectedNavigationId == R.id.nav_sign_out) {
            AuthUI.getInstance()
                    .signOut(this)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Intent in = new Intent();
                                in.setClass(MainActivity.this, FirebaseAuthActivity.class);
                                startActivityWithLeftAnimation(in);
                                finish();
                            } else {
                                Snackbar.make(mRootView, R.string.sign_out_failed, Snackbar.LENGTH_LONG).show();
                            }
                        }
                    });
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
