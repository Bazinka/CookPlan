package com.cookplan.shopping_list.total_list

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.RelativeLayout
import com.cookplan.BaseActivity
import com.cookplan.BaseFragment
import com.cookplan.R
import com.cookplan.add_ingredient_view.ProductForIngredientActivity
import com.cookplan.models.Ingredient
import com.cookplan.models.ShopListStatus
import java.util.*


class TotalShoppingListFragment : BaseFragment(), TotalShoppingListView {

    private var presenter: TotalShoppingListPresenter? = null
    private var needToBuyAdapter: TotalShopListRecyclerViewAdapter? = null
    private var progressBarLayout: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
        presenter = TotalShoppingListPresenterImpl(this)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        mainView = inflater.inflate(R.layout.fragment_total_shop_list, container, false) as ViewGroup

        progressBarLayout = mainView?.findViewById<View>(R.id.progress_bar_layout)

        val needToBuyRecyclerView = mainView?.findViewById<RecyclerView>(R.id.total_need_to_buy_recycler)
        needToBuyRecyclerView?.setHasFixedSize(true)
        needToBuyRecyclerView?.isNestedScrollingEnabled = false
        needToBuyRecyclerView?.layoutManager = LinearLayoutManager(activity)


        needToBuyAdapter = TotalShopListRecyclerViewAdapter { ingredient ->
            if (ingredient.shopListStatus === ShopListStatus.NEED_TO_BUY) {
                ingredient.shopListStatus = ShopListStatus.ALREADY_BOUGHT
                presenter?.changeShopListStatus(ingredient, ShopListStatus.ALREADY_BOUGHT)
            } else if (ingredient.shopListStatus === ShopListStatus.ALREADY_BOUGHT) {
                ingredient.shopListStatus = ShopListStatus.NEED_TO_BUY
                presenter?.changeShopListStatus(ingredient, ShopListStatus.NEED_TO_BUY)
            }
            needToBuyAdapter?.notifyDataSetChanged()
        }
        needToBuyRecyclerView?.adapter = needToBuyAdapter

        val chooseButton = mainView?.findViewById<Button>(R.id.add_shop_list_items_button)
        chooseButton?.setOnClickListener {
            val intent = Intent(activity, ProductForIngredientActivity::class.java)
            intent.putExtra(ProductForIngredientActivity.RECIPE_NEED_TO_BUY_KEY, true)
            intent.putExtra(ProductForIngredientActivity.RECIPE_ID_KEY, String())
            if (activity is BaseActivity) {
                (activity as BaseActivity).startActivityWithLeftAnimation(intent)
            }
        }

        val deleteImageButton = mainView?.findViewById<ImageView>(R.id.delete_image_view)
        deleteImageButton?.setOnClickListener { v ->
            AlertDialog.Builder(activity as Context, R.style.AppCompatAlertDialogStyle)
                    .setTitle(R.string.delete_shop_list_title)
                    .setMessage(R.string.choose_right_delete_mode)
                    .setPositiveButton(R.string.delete_already_bought) { dialog, which ->
                        val ingredients = ArrayList<Ingredient>()
                        for (ingredient in needToBuyAdapter?.getIngredients() ?: listOf()) {
                            if (ingredient.shopListStatus === ShopListStatus.ALREADY_BOUGHT) {
                                ingredients.add(ingredient)
                            }
                        }
                        progressBarLayout?.visibility = VISIBLE
                        presenter?.deleteIngredients(ingredients)
                    }
                    .setNeutralButton(android.R.string.cancel, null)
                    .setNegativeButton(R.string.delete_all_items_title) { dialog, which ->
                        progressBarLayout?.visibility = VISIBLE
                        presenter?.deleteIngredients(needToBuyAdapter?.getIngredients() ?: listOf())
                    }
                    .show()
        }
        return mainView
    }

    override fun onStart() {
        super.onStart()
        presenter?.getShoppingList()
        setEmptyView()
        progressBarLayout?.visibility = VISIBLE
    }

    override fun onStop() {
        super.onStop()
        presenter?.onStop()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun setErrorToast(error: String) {
        super.setErrorToast(getString(R.string.error_load_shop_list));
    }

    override fun setEmptyView() {
        setEmptyViewVisability(VISIBLE)
        setContentVisability(GONE)
    }

    private fun setContentVisability(visability: Int) {
        val contentLayout = mainView?.findViewById<ViewGroup>(R.id.main_content_layout)
        contentLayout?.visibility = visability
    }

    override fun setIngredientLists(allIngredientsList: List<Ingredient>) {
        progressBarLayout?.visibility = GONE
        if (!allIngredientsList.isEmpty()) {
            needToBuyAdapter?.update(allIngredientsList)
            val layoutparams = RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            layoutparams.addRule(RelativeLayout.CENTER_HORIZONTAL)
            mainView?.findViewById<View>(R.id.add_product_card_view)?.layoutParams = layoutparams
            setEmptyViewVisability(GONE)
            setContentVisability(VISIBLE)
        } else {
            val layoutparams = RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            layoutparams.addRule(RelativeLayout.CENTER_HORIZONTAL)
            mainView?.findViewById<View>(R.id.add_product_card_view)?.layoutParams = layoutparams
            setEmptyView()
        }
        mainView?.requestLayout();
    }

    companion object {

        @JvmStatic
        fun newInstance(): TotalShoppingListFragment {
            val fragment = TotalShoppingListFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }
}