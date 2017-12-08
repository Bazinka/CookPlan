package com.cookplan.add_ingredient_view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.SpinnerAdapter
import android.widget.TextView
import com.cookplan.R
import com.cookplan.models.MeasureUnit

/**
 * Created by DariaEfimova on 20.03.17.
 */

class MeasureUnitsSpinnerAdapter : ArrayAdapter<MeasureUnit>, SpinnerAdapter {
    private var selectedItems: List<MeasureUnit> = listOf<MeasureUnit>()
    private var itemsAll: List<MeasureUnit> = listOf<MeasureUnit>()

    constructor(context: Context, itemsAll: List<MeasureUnit>, selectedItems: List<MeasureUnit>) : super(context, R.layout.measure_spinner_item_layout, itemsAll) {
        this.selectedItems = selectedItems
        this.itemsAll = itemsAll
    }

    constructor(context: Context, itemsAll: List<MeasureUnit>, selectedItem: MeasureUnit?) : super(context, R.layout.measure_spinner_item_layout, itemsAll) {
        this.selectedItems = if (selectedItem != null) listOf(selectedItem) else listOf()
        this.itemsAll = itemsAll
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View

        if (convertView == null) {
            val vi = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            view = vi.inflate(R.layout.measure_spinner_item_layout, null)
        } else {
            view = convertView
        }

        val unit = itemsAll[position]
        val customerNameLabel = view?.findViewById<TextView>(R.id.item_name)
        customerNameLabel?.text = unit.toString()
        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View

        val unit = itemsAll[position]
        val vi = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        if (selectedItems.contains(unit)) {
            if (convertView == null) {
                view = vi.inflate(R.layout.measure_selected_spinner_item_layout, null)
            } else {
                view = convertView
            }
        } else {
            if (convertView == null) {
                view = vi.inflate(R.layout.measure_spinner_item_layout, null)
            } else {
                view = convertView
            }
        }

        val customerNameLabel = view?.findViewById<TextView>(R.id.item_name)
        customerNameLabel?.text = unit.toString()
        return view
    }
}