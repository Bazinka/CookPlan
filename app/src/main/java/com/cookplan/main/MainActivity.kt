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
import com.cookplan.share.add_users.ShareDataActivity
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

    override fun onStart() {
        super.onStart()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        val itemId = item.itemId

        if (itemId == R.id.nav_recipe_list) {
            setRecipeListFragment()
        } else if (itemId == R.id.nav_shopping_list) {
            setShoppingListFragment()
        } else if (itemId == R.id.nav_vocabulary) {
            setProductListFragment()
        } else if (itemId == R.id.nav_todo_list) {
            setTODOListFragment()
        } else if (itemId == R.id.nav_companies) {
            setCompaniesListFragment()
        }

        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    internal fun setRecipeListFragment() {
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

    internal fun setShoppingListFragment() {

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

    internal fun setProductListFragment() {

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

    internal fun setTODOListFragment() {
        menu?.findItem(R.id.app_bar_share_on)?.isVisible = false
        menu?.findItem(R.id.app_bar_share_off)?.isVisible = false

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

    internal fun setCompaniesListFragment() {
        menu?.findItem(R.id.app_bar_share_on)?.isVisible = false
        menu?.findItem(R.id.app_bar_share_off)?.isVisible = false

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

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == OPEN_SHOP_LIST_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                setShoppingListFragment()
                val navigationView = findViewById<NavigationView>(R.id.nav_view)
                navigationView.setCheckedItem(R.id.nav_shopping_list)
            }
        } else if (requestCode == SHARE_USER_LIST_REQUEST && resultCode == Activity.RESULT_OK) {
            setFamilyModeMenuOptions(data?.getBooleanExtra(FAMILY_TURNED_ON_KEY, false) ?: false)
        }
    }

    override fun onCreateOptionsMenu(_menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, _menu)
        menu = _menu
        setFamilyModeMenuOptions(intent.getBooleanExtra(FAMILY_TURNED_ON_KEY, false))
        return super.onCreateOptionsMenu(_menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.app_bar_share_off || id == R.id.app_bar_share_on) {
            val intent = Intent(this, ShareDataActivity::class.java)
            startActivityForResultWithLeftAnimation(intent, SHARE_USER_LIST_REQUEST)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setFamilyModeMenuOptions(isFamilyModeTurnOn: Boolean) {
        menu?.findItem(R.id.app_bar_share_on)?.isVisible = isFamilyModeTurnOn
        menu?.findItem(R.id.app_bar_share_off)?.isVisible = !isFamilyModeTurnOn
    }

    companion object {
        val OPEN_SHOP_LIST_REQUEST = 10
        val SHARE_USER_LIST_REQUEST = 11

        val FAMILY_TURNED_ON_KEY = "FAMILY_TURNED_ON_REQUEST"
    }
}
