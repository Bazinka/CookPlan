package com.cookplan.add_ingredient_view

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.cookplan.BaseActivity
import com.cookplan.R
import com.cookplan.models.Recipe

class AddIngredientViewFragment : Fragment() {

    private var mainView: View? = null
    private var recipe: Recipe? = null
    private var isNeedToBuy: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments?.containsKey(RECIPE_OBJECT_KEY) ?: false) {
            recipe = arguments?.getSerializable(RECIPE_OBJECT_KEY) as Recipe
        }
        isNeedToBuy = arguments?.getBoolean(RECIPE_NEED_TO_BUY_KEY, false) ?: false

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        mainView = inflater.inflate(R.layout.fragment_add_ingredient_view, container, false)

        val chooseButton = mainView?.findViewById<Button>(R.id.choose_product_button)
        chooseButton?.setOnClickListener {
            val intent = Intent(activity, ProductForIngredientActivity::class.java)
            intent.putExtra(ProductForIngredientActivity.RECIPE_ID_KEY, recipe?.id)
            intent.putExtra(ProductForIngredientActivity.RECIPE_NEED_TO_BUY_KEY, isNeedToBuy)
            if (activity is BaseActivity) {
                (activity as BaseActivity).startActivityWithLeftAnimation(intent)
            }
        }
        return mainView
    }

    companion object {

        val RECIPE_OBJECT_KEY = "RECIPE_OBJECT_KEY"
        val RECIPE_NEED_TO_BUY_KEY = "is_need_to_buy"

        fun newInstance(isNeedToBuy: Boolean): AddIngredientViewFragment {
            val fragment = AddIngredientViewFragment()
            val args = Bundle()
            args.putBoolean(RECIPE_NEED_TO_BUY_KEY, isNeedToBuy)
            fragment.arguments = args
            return fragment
        }

        fun newInstance(recipe: Recipe?, isNeedToBuy: Boolean): AddIngredientViewFragment {
            val fragment = AddIngredientViewFragment()
            val args = Bundle()
            if (recipe != null) {
                args.putSerializable(RECIPE_OBJECT_KEY, recipe)
            }
            args.putBoolean(RECIPE_NEED_TO_BUY_KEY, isNeedToBuy)
            fragment.arguments = args
            return fragment
        }
    }
}
