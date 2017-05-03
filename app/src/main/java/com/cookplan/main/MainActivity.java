package com.cookplan.main;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cookplan.BaseActivity;
import com.cookplan.R;
import com.cookplan.RApplication;
import com.cookplan.auth.ui.FirebaseAuthActivity;
import com.cookplan.product_list.ProductListFragment;
import com.cookplan.recipe_grid.RecipeGridFragment;
import com.cookplan.share.SharePresenter;
import com.cookplan.share.SharePresenterImpl;
import com.cookplan.share.ShareView;
import com.cookplan.share.add_users.AddUserForSharingActivity;
import com.cookplan.shopping_list.list_by_dishes.ShopListByDishesFragment;
import com.cookplan.shopping_list.total_list.TotalShoppingListFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, MainView, ShareView {

    public static final int OPEN_SHOP_LIST_REQUEST = 10;
    public static final int SHARE_USER_LIST_REQUEST = 11;
    public static final String SHARE_USER_EMAIL_LIST_KEY = "SHARE_USER_EMAIL_LIST_KEY";

    private ProgressDialog mProgressDialog;

    private int mSelectedNavigationId;
    private View rootView;

    private MainPresenter presenter;

    protected SharePresenter sharePresenter;

    protected Menu menu;

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

        rootView = findViewById(R.id.main_snackbar_layout);

        sharePresenter = new SharePresenterImpl(this);
        presenter = new MainPresenterImpl(this, this);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        fillNavHeader();
        navigationView.setNavigationItemSelectedListener(this);
        mSelectedNavigationId = R.id.nav_recipe_list;
        navigationView.setCheckedItem(mSelectedNavigationId);
        setRecipeListFragment();
    }

    private void fillNavHeader() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        FirebaseUser user = presenter.getCurrentUser();
        if (user != null) {
            if (user.isAnonymous()) {
                navigationView.getMenu().findItem(R.id.nav_sign_in).setVisible(true);
                navigationView.getMenu().findItem(R.id.nav_sign_out).setVisible(false);
            } else {
                navigationView.getMenu().findItem(R.id.nav_sign_in).setVisible(false);
                navigationView.getMenu().findItem(R.id.nav_sign_out).setVisible(true);
            }
            if (user.getPhotoUrl() != null) {
                ImageView photoImageView = (ImageView) headerView.findViewById(R.id.user_photo_imageView);
                Glide.with(this)
                        .load(user.getPhotoUrl().getPath())
                        .placeholder(R.drawable.main_drawable)
                        .into(photoImageView);
            }
            if (user.getDisplayName() != null) {
                TextView nameTextView = (TextView) headerView.findViewById(R.id.user_name_textView);
                nameTextView.setText(user.getDisplayName());
            }
        } else {
            signedOut();
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

    @Override
    protected void onStart() {
        if (sharePresenter != null) {
            sharePresenter.isFamilyModeTurnOnRequest();
        }
        super.onStart();
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
            if (presenter != null) {
                presenter.signOut();
            }
        } else if (mSelectedNavigationId == R.id.nav_sign_in) {
            if (presenter != null) {
                presenter.signIn();
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    void setRecipeListFragment() {
        View tabsLayout = findViewById(R.id.main_tabs_layout);
        tabsLayout.setVisibility(View.GONE);
        View viewPager = findViewById(R.id.main_tabs_viewpager);
        viewPager.setVisibility(View.GONE);

        //        FrameLayout mainConteinerView = (FrameLayout) findViewById(R.id.fragment_container);
        //        mainConteinerView.setVisibility(View.VISIBLE);

        setTitle(getString(R.string.recipe_list_menu_title));
        RecipeGridFragment pointListFragment = RecipeGridFragment.newInstance();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (getSupportFragmentManager().findFragmentById(R.id.fragment_container) == null) {
            transaction.add(R.id.fragment_container, pointListFragment);
        } else {
            transaction.replace(R.id.fragment_container, pointListFragment);
        }
        transaction.commit();
    }

    void setShoppingListFragment() {

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

        //        FrameLayout mainConteinerView = (FrameLayout) findViewById(R.id.fragment_container);
        //        mainConteinerView.setVisibility(View.VISIBLE);

        setTitle(getString(R.string.product_vocabulary_title));

        ProductListFragment pointListFragment = ProductListFragment.newInstance();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (getSupportFragmentManager().findFragmentById(R.id.fragment_container) == null) {
            transaction.add(R.id.fragment_container, pointListFragment);
        } else {
            transaction.replace(R.id.fragment_container, pointListFragment);
        }
        transaction.commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
        dismissDialog();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == OPEN_SHOP_LIST_REQUEST) {
            if (resultCode == RESULT_OK) {
                mSelectedNavigationId = R.id.nav_shopping_list;
                setShoppingListFragment();
                NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
                navigationView.setCheckedItem(mSelectedNavigationId);
            }
        } else if (requestCode == SHARE_USER_LIST_REQUEST) {
            ArrayList<String> emailList = data.getStringArrayListExtra(SHARE_USER_EMAIL_LIST_KEY);
            if (sharePresenter != null && emailList != null && !emailList.isEmpty()) {
                sharePresenter.shareData(emailList);
            }
        } else if (presenter != null) {
            presenter.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void showSnackbar(int messageRes) {
        Snackbar.make(rootView, messageRes, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showLoadingDialog(String message) {
        dismissDialog();
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setTitle("");
        }

        mProgressDialog.setMessage(message);
        mProgressDialog.show();
    }

    @Override
    public void signedInWithAnonymous() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.getMenu().findItem(R.id.nav_sign_in).setVisible(true);
        navigationView.getMenu().findItem(R.id.nav_sign_out).setVisible(false);
    }

    @Override
    public void signedInWithGoogle() {
        fillNavHeader();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.getMenu().findItem(R.id.nav_sign_in).setVisible(false);
        navigationView.getMenu().findItem(R.id.nav_sign_out).setVisible(true);
    }

    @Override
    public void signedInFailed() {
        dismissDialog();
        showSnackbar(R.string.unknown_sign_in_response);
    }

    @Override
    public void signedOut() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.getMenu().findItem(R.id.nav_sign_in).setVisible(false);
        navigationView.getMenu().findItem(R.id.nav_sign_out).setVisible(false);
        Intent intent = new Intent();
        RApplication.saveAnonymousPossibility(false);
        intent.setClass(this, FirebaseAuthActivity.class);
        startActivityWithLeftAnimation(intent);
        finish();
    }

    @Override
    public void showLoadingDialog(@StringRes int stringResource) {
        showLoadingDialog(getString(stringResource));
    }

    @Override
    public void dismissDialog() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu _menu) {
        getMenuInflater().inflate(R.menu.main_menu, _menu);
        menu = _menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.app_bar_share_off) {

            if (!FirebaseAuth.getInstance().getCurrentUser().isAnonymous()) {

                Intent intent = new Intent(this, AddUserForSharingActivity.class);
                startActivityForResultWithLeftAnimation(intent, SHARE_USER_LIST_REQUEST);

            } else if (FirebaseAuth.getInstance().getCurrentUser() != null &&
                    FirebaseAuth.getInstance().getCurrentUser().isAnonymous()) {
                new AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle)
                        .setTitle(R.string.attention_title)
                        .setMessage(R.string.need_to_auth)
                        .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                            dialog.dismiss();
                        })
                        .show();
            } else {
                new AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle)
                        .setTitle(R.string.attention_title)
                        .setMessage(R.string.cant_share_data)
                        .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                            dialog.dismiss();
                        })
                        .show();
            }
            return true;
        }
        if (id == R.id.app_bar_share_on) {
            if (!FirebaseAuth.getInstance().getCurrentUser().isAnonymous()) {
                new AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle)
                        .setTitle(R.string.attention_title)
                        .setMessage(R.string.turn_off_family_mode_question)
                        .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                            if (sharePresenter != null) {
                                sharePresenter.turnOffFamilyMode();
                            }
                            dialog.dismiss();
                        })
                        .setNegativeButton(android.R.string.cancel, (dialog, which) -> {
                            dialog.dismiss();
                        })
                        .show();
            } else if (FirebaseAuth.getInstance().getCurrentUser() != null &&
                    FirebaseAuth.getInstance().getCurrentUser().isAnonymous()) {
                new AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle)
                        .setTitle(R.string.attention_title)
                        .setMessage(R.string.need_to_auth)
                        .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                            dialog.dismiss();
                        })
                        .show();
            } else {
                new AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle)
                        .setTitle(R.string.attention_title)
                        .setMessage(R.string.cant_share_data)
                        .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                            dialog.dismiss();
                        })
                        .show();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setShareIcon(boolean isFamilyModeTurnOn) {
        if (menu != null) {
            if (isFamilyModeTurnOn) {
                menu.findItem(R.id.app_bar_share_on).setVisible(true);
                menu.findItem(R.id.app_bar_share_off).setVisible(false);
            } else {
                menu.findItem(R.id.app_bar_share_on).setVisible(false);
                menu.findItem(R.id.app_bar_share_off).setVisible(true);
            }
        }
    }

    @Override
    public void setShareError(int errorResourceId) {
        Toast.makeText(this, errorResourceId, Toast.LENGTH_LONG).show();
    }
}
