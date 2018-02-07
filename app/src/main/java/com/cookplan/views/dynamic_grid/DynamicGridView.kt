package com.cookplan.views.dynamic_grid

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import com.cookplan.R


/**
 * Created by DariaEfimova on 10.05.17.
 */

class DynamicGridView : RelativeLayout {

    private var adapter: DynamicGridAdapter? = null

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        init()
    }

    private fun init() {
        View.inflate(context, R.layout.dynamic_grid_view_main_layout, this)
    }

    fun setAdapter(gridAdapter: DynamicGridAdapter) {
        adapter = gridAdapter
        reloadView()
    }

    private fun reloadView() {
        val mainLayout = findViewById<ViewGroup>(R.id.main_view)

        var i = 0
        while (i < adapter?.getItemsCount() ?: 0) {
            val listViews = adapter?.getLineFromItemPosition(i) ?: listOf()
            val linearlayout = LinearLayout(context)

            linearlayout.orientation = LinearLayout.HORIZONTAL

            val linearlayoutlayoutparams = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT)
            linearlayout.layoutParams = linearlayoutlayoutparams

            for (view in listViews) {
                linearlayout.addView(view)
            }
            mainLayout.addView(linearlayout)
            i += if (listViews.isNotEmpty()) listViews.size else adapter?.getItemsCount()?:0
        }
        requestLayout()
    }
}
