package com.cookplan.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cookplan.BaseActivity;
import com.cookplan.R;
import com.cookplan.auth.FirebaseAuthActivity;
import com.cookplan.product_list.ProductListFragment;
import com.cookplan.recipe_grid.RecipeGridFragment;
import com.cookplan.recipe_new.add_info.NewRecipeInfoActivity;
import com.cookplan.shopping_list.list_by_dishes.ShopListByDishesFragment;
import com.cookplan.shopping_list.total_list.TotalShoppingListFragment;
import com.firebase.ui.auth.AuthUI;
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
    }

    void setRecipeListFragment() {
        View tabsLayout = findViewById(R.id.main_tabs_layout);
        tabsLayout.setVisibility(View.GONE);
        View viewPager = findViewById(R.id.main_tabs_viewpager);
        viewPager.setVisibility(View.GONE);
        View mainContentView = findViewById(R.id.main_content_layout);
        mainContentView.setVisibility(View.VISIBLE);

        setTitle(getString(R.string.recipe_list_menu_title));
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(RecipeGridFragment.class.getSimpleName());
        if (fragment == null) {
            RecipeGridFragment pointListFragment = RecipeGridFragment.newInstance();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, pointListFragment, RecipeGridFragment.class.getSimpleName());
            transaction.commit();
        }
    }

    void setShoppingListFragment() {
        View mainContentView = findViewById(R.id.main_content_layout);
        mainContentView.setVisibility(View.INVISIBLE);

        View tabsLayout = findViewById(R.id.main_tabs_layout);
        tabsLayout.setVisibility(View.VISIBLE);

        ViewPager viewPager = (ViewPager) findViewById(R.id.main_tabs_viewpager);
        viewPager.setVisibility(View.VISIBLE);

        ViewPagerTabsAdapter adapter = new ViewPagerTabsAdapter(getSupportFragmentManager());
        adapter.addFragment(TotalShoppingListFragment.newInstance(), getString(R.string.tab_all_ingredients_title));
        adapter.addFragment(ShopListByDishesFragment.newInstance(), getString(R.string.tab_ingredients_by_dish_title));
//        adapter.addFragment(new ThreeFragment(), "THREE");
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.main_tabs_layout);
        tabLayout.setupWithViewPager(viewPager);
        setTitle(getString(R.string.shopping_list_title));
    }

    void setProductListFragment() {
        View tabsLayout = findViewById(R.id.main_tabs_layout);
        tabsLayout.setVisibility(View.GONE);
        View viewPager = findViewById(R.id.main_tabs_viewpager);
        viewPager.setVisibility(View.GONE);
        View mainContentView = findViewById(R.id.main_content_layout);
        mainContentView.setVisibility(View.VISIBLE);

        setTitle(getString(R.string.product_vocabulary_title));
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(ProductListFragment.class.getSimpleName());
        if (fragment == null) {
            ProductListFragment pointListFragment = ProductListFragment.newInstance();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, pointListFragment, ProductListFragment.class.getSimpleName());
            transaction.commit();
        }
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
        } else if (mSelectedNavigationId == R.id.nav_shopping_list) {
            setShoppingListFragment();
        } else if (mSelectedNavigationId == R.id.nav_vocabulary) {
            setProductListFragment();
        } else if (mSelectedNavigationId == R.id.nav_sign_out) {
            AuthUI.getInstance()
                    .signOut(this)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Intent in = new Intent();
                            in.setClass(MainActivity.this, FirebaseAuthActivity.class);
                            startActivityWithLeftAnimation(in);
                            finish();
                        } else {
                            Snackbar.make(mRootView, R.string.sign_out_failed, Snackbar.LENGTH_LONG).show();
                        }
                    });
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
