package com.cookplan.main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.design.widget.TabLayout
import android.support.v4.view.GravityCompat
import android.support.v4.view.ViewPager
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.cookplan.BaseActivity
import com.cookplan.R
import com.cookplan.companies.MainCompaniesFragment
import com.cookplan.product_list.ProductListFragment
import com.cookplan.recipe.grid.RecipeGridFragment
import com.cookplan.share.add_users.ShareDataFragment
import com.cookplan.shopping_list.list_by_dishes.ShopListByDishesFragment
import com.cookplan.shopping_list.total_list.TotalShoppingListFragment
import com.cookplan.todo_list.ToDoListFragment
import com.google.firebase.auth.FirebaseAuth

class MainActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {

    private var rootView: View? = null

    protected var menu: Menu? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        val toggle = ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer.setDrawerListener(toggle)
        toggle.syncState()

        rootView = findViewById(R.id.main_snackbar_layout)

        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        fillNavHeader()
        navigationView.setNavigationItemSelectedListener(this)
        navigationView.setCheckedItem(R.id.nav_shopping_list)
        menu = navigationView.menu
        setShoppingListFragment()
    }

    private fun fillNavHeader() {
        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        val headerView = navigationView.getHeaderView(0)
        val user = FirebaseAuth.getInstance().currentUser

        val photoImageView = headerView.findViewById<ImageView>(R.id.user_photo_imageView)
        Glide.with(this)
                .load(user?.photoUrl?.path)
                .placeholder(R.drawable.main_drawable)
                .into(photoImageView)

        val nameTextView = headerView.findViewById<TextView>(R.id.user_name_textView)
        nameTextView.text = user?.displayName
    }

    override fun onBackPressed() {
        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        val itemId = item.itemId

        when (itemId) {
            R.id.nav_recipe_list -> setRecipeListFragment()
            R.id.nav_shopping_list -> setShoppingListFragment()
            R.id.nav_vocabulary -> setProductListFragment()
            R.id.nav_todo_list -> setTODOListFragment()
            R.id.nav_companies -> setCompaniesListFragment()
            R.id.nav_share -> setShareDataFragment()
        }

        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    private fun setRecipeListFragment() {
        val tabsLayout = findViewById<TabLayout>(R.id.main_tabs_layout)
        tabsLayout.visibility = View.GONE
        val viewPager = findViewById<ViewPager>(R.id.main_tabs_viewpager)
        viewPager.visibility = View.GONE

        //        FrameLayout mainConteinerView = (FrameLayout) findViewById(R.id.fragment_container);
        //        mainConteinerView.setVisibility(View.VISIBLE);

        setTitle(getString(R.string.recipe_list_menu_title))
        val pointListFragment = RecipeGridFragment.newInstance()
        val transaction = supportFragmentManager.beginTransaction()
        if (supportFragmentManager.findFragmentById(R.id.fragment_container) == null) {
            transaction.add(R.id.fragment_container, pointListFragment)
        } else {
            transaction.replace(R.id.fragment_container, pointListFragment)
        }
        transaction.commit()
    }

    private fun setShoppingListFragment() {

        val tabsLayout = findViewById<TabLayout>(R.id.main_tabs_layout)
        tabsLayout.visibility = View.VISIBLE

        val viewPager = findViewById<ViewPager>(R.id.main_tabs_viewpager)
        viewPager.visibility = View.VISIBLE

        val adapter = ViewPagerTabsAdapter(supportFragmentManager)
        adapter.addFragment(TotalShoppingListFragment.newInstance(), getString(R.string.tab_all_ingredients_title))
        adapter.addFragment(ShopListByDishesFragment.newInstance(), getString(R.string.tab_ingredients_by_dish_title))
        //        adapter.addFragment(new ThreeFragment(), "THREE");
        viewPager.adapter = adapter

        tabsLayout.setupWithViewPager(viewPager)
        setTitle(getString(R.string.shopping_list_title))
    }

    private fun setProductListFragment() {

        val tabsLayout = findViewById<TabLayout>(R.id.main_tabs_layout)
        tabsLayout.visibility = View.GONE
        val viewPager = findViewById<ViewPager>(R.id.main_tabs_viewpager)
        viewPager.visibility = View.GONE

        //        FrameLayout mainConteinerView = (FrameLayout) findViewById(R.id.fragment_container);
        //        mainConteinerView.setVisibility(View.VISIBLE);

        setTitle(getString(R.string.product_vocabulary_title))

        val pointListFragment = ProductListFragment.newInstance()
        val transaction = supportFragmentManager.beginTransaction()
        if (supportFragmentManager.findFragmentById(R.id.fragment_container) == null) {
            transaction.add(R.id.fragment_container, pointListFragment)
        } else {
            transaction.replace(R.id.fragment_container, pointListFragment)
        }
        transaction.commit()
    }

    private fun setTODOListFragment() {

        val tabsLayout = findViewById<TabLayout>(R.id.main_tabs_layout)
        tabsLayout.visibility = View.GONE
        val viewPager = findViewById<ViewPager>(R.id.main_tabs_viewpager)
        viewPager.visibility = View.GONE

        //        FrameLayout mainConteinerView = (FrameLayout) findViewById(R.id.fragment_container);
        //        mainConteinerView.setVisibility(View.VISIBLE);

        setTitle(getString(R.string.todo_list_title))

        val fragment = ToDoListFragment.newInstance()
        val transaction = supportFragmentManager.beginTransaction()
        if (supportFragmentManager.findFragmentById(R.id.fragment_container) == null) {
            transaction.add(R.id.fragment_container, fragment)
        } else {
            transaction.replace(R.id.fragment_container, fragment)
        }
        transaction.commit()
    }

    private fun setCompaniesListFragment() {

        val tabsLayout = findViewById<TabLayout>(R.id.main_tabs_layout)
        tabsLayout.visibility = View.GONE
        val viewPager = findViewById<ViewPager>(R.id.main_tabs_viewpager)
        viewPager.visibility = View.GONE

        setTitle(getString(R.string.companies_list_title))

        val fragment = MainCompaniesFragment.newInstance()
        val transaction = supportFragmentManager.beginTransaction()
        if (supportFragmentManager.findFragmentById(R.id.fragment_container) == null) {
            transaction.add(R.id.fragment_container, fragment)
        } else {
            transaction.replace(R.id.fragment_container, fragment)
        }
        transaction.commit()
    }

    private fun setShareDataFragment() {
        val tabsLayout = findViewById<TabLayout>(R.id.main_tabs_layout)
        tabsLayout.visibility = View.GONE
        val viewPager = findViewById<ViewPager>(R.id.main_tabs_viewpager)
        viewPager.visibility = View.GONE

        setTitle(getString(R.string.share_data_title))
        val fragment = ShareDataFragment.newInstance()
        val transaction = supportFragmentManager.beginTransaction()
        if (supportFragmentManager.findFragmentById(R.id.fragment_container) == null) {
            transaction.add(R.id.fragment_container, fragment)
        } else {
            transaction.replace(R.id.fragment_container, fragment)
        }
        transaction.commit()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && requestCode == OPEN_SHOP_LIST_REQUEST) {
            setShoppingListFragment()
            val navigationView = findViewById<NavigationView>(R.id.nav_view)
            navigationView.setCheckedItem(R.id.nav_shopping_list)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    companion object {
        val OPEN_SHOP_LIST_REQUEST = 10
    }
}
