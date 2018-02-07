package com.cookplan.views.dynamic_grid

import android.content.res.Resources
import android.view.View
import android.widget.RelativeLayout


/**
 * Created by DariaEfimova on 07.02.2018.
 */
abstract class DynamicGridAdapter {

    fun getLineFromItemPosition(fromPosition: Int): List<View> {

        val screenWidth = Resources.getSystem().displayMetrics.widthPixels

        val list = mutableListOf<View>()

        var pos = fromPosition
        var viewWidth = 0
        while (pos < getItemsCount()) {
            val view = getViewAtPosition(pos)
            view.measure(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            viewWidth += view.measuredWidth
            if (viewWidth < screenWidth) {
                list.add(view)
            }else{
                break
            }
            pos++
        }
        return list
    }

    abstract fun getViewAtPosition(position: Int): View
    abstract fun getItemsCount(): Int

}