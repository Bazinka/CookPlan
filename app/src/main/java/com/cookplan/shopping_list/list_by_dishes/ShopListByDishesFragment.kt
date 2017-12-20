package com.cookplan.shopping_list.list_by_dishes

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cookplan.BaseFragment
import com.cookplan.R
import com.cookplan.models.Ingredient
import com.cookplan.models.Recipe


class ShopListByDishesFragment : BaseFragment(), ShopListByDishesView {


    private var presenter: ShopListByDishPresenter? = null
    private var progressBarLayout: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
        presenter = ShopListByDishesPresenterImpl(this, activity as Context)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        mainView = inflater.inflate(R.layout.fragment_shop_list_by_dish, container, false) as ViewGroup
        progressBarLayout = mainView?.findViewById(R.id.progress_bar_layout)
        return mainView
    }

    override fun onStart() {
        super.onStart()
        presenter?.getShoppingList()
        setEmptyView()
        progressBarLayout?.visibility = View.VISIBLE
    }

    override fun onStop() {
        super.onStop()
        presenter?.onStop()
    }

    override fun setErrorToast(error: String) {
        super.setErrorToast(getString(R.string.error_load_shop_list));
    }

    override fun setEmptyView() {
        progressBarLayout?.visibility = View.GONE
        setEmptyViewVisability(View.VISIBLE)
        setContentVisability(View.GONE)
    }

    private fun setContentVisability(visability: Int) {
        val contentLayout = mainView?.findViewById<ViewGroup>(R.id.main_content_layout)
        contentLayout?.visibility = visability
    }

    override fun setIngredientListToRecipe(newGroupList: MutableList<Recipe>, newChildMap: Map<String, List<Ingredient>>) {
        progressBarLayout?.visibility = View.GONE

        if (newGroupList.isEmpty()) {
            setEmptyView()
        } else {
            setEmptyViewVisability(View.GONE)
            setContentVisability(View.VISIBLE)
            val needToBuyRecyclerView = mainView?.findViewById<RecyclerView>(R.id.shop_list_by_dish_recycler)
            needToBuyRecyclerView?.setHasFixedSize(true)
            needToBuyRecyclerView?.isNestedScrollingEnabled = false
            needToBuyRecyclerView?.layoutManager = LinearLayoutManager(activity)

            needToBuyRecyclerView?.adapter = ShopListByDishesRecyclerAdapter(newGroupList, newChildMap,
                    { recipe, ingredientList ->
                        AlertDialog.Builder(activity as Context, R.style.AppCompatAlertDialogStyle).setTitle(R.string.attention_title)
                                .setMessage(R.string.delete_recipe_from_shop_list_question)
                                .setPositiveButton(android.R.string.ok) { dialog, which ->
                                    progressBarLayout?.visibility = View.VISIBLE
                                    presenter?.recipeIngredBought(recipe, ingredientList)
                                }
                                .setNegativeButton(android.R.string.no, null)
                                .show()
                    })

//            val expandableListView = mainView?.findViewById<View>(R.id.shop_list_by_dish_expListView) as ExpandableListView
//            val needToBuyAdapter = ShopListExpandableListAdapter(
//                    activity as Context,
//                    newGroupList,
//                    newChildMap,
//                    { ingredient ->
//                        val newStatus: ShopListStatus
//                        if (ingredient.shopListStatus === ShopListStatus.NEED_TO_BUY) {
//                            newStatus = ShopListStatus.ALREADY_BOUGHT
//                        } else {
//                            newStatus = ShopListStatus.NEED_TO_BUY
//                        }
//                        presenter?.changeIngredientStatus(ingredient, newStatus)
//
//                    }, )
//            expandableListView.setAdapter(needToBuyAdapter)
//            val count = needToBuyAdapter.groupCount
//            for (position in 0 until count) {
//                expandableListView.expandGroup(position)
//            }
        }
    }

    companion object {

        @JvmStatic
        fun newInstance(): ShopListByDishesFragment {
            val fragment = ShopListByDishesFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }
}