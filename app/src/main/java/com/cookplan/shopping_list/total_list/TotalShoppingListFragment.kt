package com.cookplan.shopping_list.total_list

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import com.cookplan.BaseFragment
import com.cookplan.R
import com.cookplan.add_ingredient_view.AddIngredientViewFragment
import com.cookplan.models.Ingredient
import com.cookplan.models.ShopListStatus
import java.util.*


class TotalShoppingListFragment : BaseFragment(), TotalShoppingListView {

    private var presenter: TotalShoppingListPresenter? = null
    private var needToBuyAdapter: TotalShopListRecyclerViewAdapter? = null
    private var progressBar: ProgressBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
        presenter = TotalShoppingListPresenterImpl(this)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        mainView = inflater.inflate(R.layout.fragment_total_shop_list, container, false) as ViewGroup

        progressBar = mainView?.findViewById<ProgressBar>(R.id.progress_bar)

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

        val fragment = AddIngredientViewFragment.newInstance(true)
        val transaction = childFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment)
        transaction.commit()

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
                        progressBar?.visibility = View.VISIBLE
                        presenter?.deleteIngredients(ingredients)
                    }
                    .setNeutralButton(android.R.string.cancel, null)
                    .setNegativeButton(R.string.delete_all_items_title) { dialog, which ->
                        progressBar?.visibility = View.VISIBLE
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
        progressBar?.visibility = View.VISIBLE
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
        progressBar?.visibility = View.GONE
        setEmptyViewVisability(View.VISIBLE)
        setContentVisability(View.GONE)
    }

    private fun setContentVisability(visability: Int) {
        val contentLayout = mainView?.findViewById<ViewGroup>(R.id.main_content_layout)
        contentLayout?.visibility = visability
    }

    override fun setIngredientLists(allIngredientList: List<Ingredient>) {
        progressBar?.visibility = View.GONE
        setEmptyViewVisability(View.GONE)
        setContentVisability(View.VISIBLE)
        val needToBuyLayout = mainView?.findViewById<ViewGroup>(R.id.need_to_buy_layout)
        if (!allIngredientList.isEmpty()) {
            setLayoutVisability(needToBuyLayout, View.VISIBLE)
            needToBuyAdapter?.update(allIngredientList)
        } else {
            setLayoutVisability(needToBuyLayout, View.GONE)
            setEmptyView()
        }
    }

    private fun setLayoutVisability(layoutView: ViewGroup?, visability: Int) {
        layoutView?.visibility = visability
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