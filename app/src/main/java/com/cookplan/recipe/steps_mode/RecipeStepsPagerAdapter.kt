package com.cookplan.recipe.steps_mode

import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.cookplan.R

/**
 * Created by DariaEfimova on 13.04.17.
 */

class RecipeStepsPagerAdapter(private val steps: List<String> = listOf()) : PagerAdapter() {

    override fun instantiateItem(collection: ViewGroup, position: Int): Any {
        val step = steps[position]
        val inflater = LayoutInflater.from(collection.context)
        val layout = inflater.inflate(R.layout.recipe_step_pager_item_layout, collection, false) as ViewGroup

        val stepDescTextView = layout.findViewById<TextView>(R.id.step_textview)
        stepDescTextView.text = step

        collection.addView(layout)
        return layout
    }

    override fun destroyItem(collection: ViewGroup, position: Int, view: Any) {
        collection.removeView(view as View)
    }

    override fun getCount(): Int {
        return steps.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }
}