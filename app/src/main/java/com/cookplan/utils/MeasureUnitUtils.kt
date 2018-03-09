package com.cookplan.utils

import android.content.Context
import com.cookplan.R
import com.cookplan.models.MeasureUnit
import com.cookplan.models.MeasureUnit.*
import java.util.*
import java.util.regex.Pattern

/**
 * Created by DariaEfimova on 16.03.17.
 */

object MeasureUnitUtils {


    val volumeUnitList: MutableList<MeasureUnit>
        get() {
            val volumeUnitArray = arrayOf(LITRE, MILILITRE, CUP, TEASPOON, TABLESPOON, PACKAGE, BOTTLE)
            return volumeUnitArray.toMutableList()
        }

    // 250 мл.
    // 5 мл.
    // 15 мл.
    val litreUnitMap: Map<MeasureUnit, Double>
        get() = UnitsMapBuilder()
                .put(MILILITRE, 1000.0)
                .put(CUP, 4.0)
                .put(TEASPOON, 202.0)
                .put(TABLESPOON, 67.0)
                .build()

    val weightUnitList: MutableList<MeasureUnit>
        get() {
            val weightUnitArray = arrayOf(GRAMM, KILOGRAMM, CUP, TEASPOON, TABLESPOON, PACKAGE, UNITS)
            return weightUnitArray.toMutableList()
        }

    val kilogramUnitMap: Map<MeasureUnit, Double>
        get() = UnitsMapBuilder()
                .put(GRAMM, 1000.0)
                .put(CUP, 4.0)
                .build()

    private class UnitsMapBuilder {
        private val measureUnitToAmoutMap: MutableMap<MeasureUnit, Double>

        constructor() {
            measureUnitToAmoutMap = HashMap()
        }

        constructor(map: Map<MeasureUnit, Double>) {
            measureUnitToAmoutMap = HashMap(map)
        }

        fun put(unit: MeasureUnit, amount: Double): UnitsMapBuilder {
            measureUnitToAmoutMap.put(unit, amount)
            return this
        }

        fun build(): Map<MeasureUnit, Double> {
            return measureUnitToAmoutMap
        }
    }

    private class UnitsListBuilder {
        private val measureUnitList: MutableList<MeasureUnit>

        init {
            measureUnitList = LinkedList()
        }

        fun add(unit: MeasureUnit): UnitsListBuilder {
            measureUnitList.add(unit)
            return this
        }

        fun addAll(unit: Array<MeasureUnit>): UnitsListBuilder {
            measureUnitList.addAll(Arrays.asList(*unit))
            return this
        }

        fun build(): List<MeasureUnit> {
            return measureUnitList
        }
    }

    fun valueToString(unit: MeasureUnit, value: Double, getContext: () -> Context?): String {
        if (value < 1e-8 && value > -1e-8) {//value==0.0
            return MeasureUnit.getByTasteString()
        }

        return if (unit == KILOGRAMM && value < 1.0) {
            getStringOfValueInUnit(unit, value * 1000) + " " + getContext()?.getString(GRAMM.getNameRecourseId())
        } else if (unit == GRAMM && value > 1000.0) {
            getStringOfValueInUnit(unit, value / 1000) + " " + getContext()?.getString(KILOGRAMM.getNameRecourseId())
        } else if (unit == LITRE && value < 1.0) {
            getStringOfValueInUnit(unit, value * 1000) + " " + getContext()?.getString(MILILITRE.getNameRecourseId())
        } else if (unit == MILILITRE && value > 1000.0) {
            getStringOfValueInUnit(unit, value / 1000) + " " + getContext()?.getString(LITRE.getNameRecourseId())
        } else {
            getStringOfValueInUnit(unit, value) + " " + getContext()?.getString(unit.getNameRecourseId())
        }
    }

    fun valueToStringForShopList(unit: MeasureUnit, value: Double, getContext: () -> Context?): String {
        val string = valueToString(unit, value, getContext)
        return if (string == MeasureUnit.getByTasteString()) {
            String()
        } else {
            string
        }
    }

    fun getStringOfValueInUnit(unit: MeasureUnit, value: Double): String {
        return if (unit.isItIntValue(value)) {
            Math.round(value).toString()
        } else {
            if (value > 10.0) {
                Math.round(value).toString()
            } else {
                String.format(Locale.getDefault(), "%.1f", value)
            }
        }
    }

    /*
   This method works only for russian language
    */
    fun parseUnit(unitString: String, getContext: () -> Context?): MeasureUnit {
        var unit = UNITS

        //try to find UNIT
        val unitsArray = getContext()?.resources?.getStringArray(R.array.unit_title_array)
        var matcher = Pattern.compile(getUnitRegex(unitsArray)).matcher(unitString)
        if (matcher.find()) {
            unit = UNITS
        }

        //try to find GRAMM
        val grammArray = getContext()?.resources?.getStringArray(R.array.gramm_title_array)
        matcher = Pattern.compile(getUnitRegex(grammArray)).matcher(unitString)
        if (matcher.find()) {
            unit = GRAMM
        }

        //try to find KILOGRAMM
        val kilogrammTitle = getContext()?.resources?.getStringArray(R.array.kilogramm_title_array)
        matcher = Pattern.compile(getUnitRegex(kilogrammTitle)).matcher(unitString)
        if (matcher.find()) {
            unit = KILOGRAMM
        }

        //try to find LITRE
        val litreTitle = getContext()?.resources?.getStringArray(R.array.litre_title_array)
        matcher = Pattern.compile(getUnitRegex(litreTitle)).matcher(unitString)
        if (matcher.find()) {
            unit = LITRE
        }

        //try to find MILILITRE
        val mililitreTitle = getContext()?.resources?.getStringArray(R.array.mililitre_title_array)
        matcher = Pattern.compile(getUnitRegex(mililitreTitle)).matcher(unitString)
        if (matcher.find()) {
            unit = MILILITRE
        }

        //try to find CUP
        val cupTitle = getContext()?.resources?.getStringArray(R.array.cup_title_array)
        matcher = Pattern.compile(getUnitRegex(cupTitle)).matcher(unitString)
        if (matcher.find()) {
            unit = CUP
        }

        //try to find PACKAGE
        val packageTitle = getContext()?.resources?.getStringArray(R.array.package_title_array)
        matcher = Pattern.compile(getUnitRegex(packageTitle)).matcher(unitString)
        if (matcher.find()) {
            unit = PACKAGE
        }

        //try to find TEASPOON
        val teaspoonTitle = getContext()?.resources?.getStringArray(R.array.teaspoon_title_array)
        matcher = Pattern.compile(getUnitRegex(teaspoonTitle)).matcher(unitString)
        if (matcher.find()) {
            unit = TEASPOON
        }

        //try to find TABLESPOON
        val tablespoonTitle = getContext()?.resources?.getStringArray(R.array.tablespoon_title_array)
        matcher = Pattern.compile(getUnitRegex(tablespoonTitle)).matcher(unitString)
        if (matcher.find()) {
            unit = TABLESPOON
        }

        //try to find BOTTLE
        val bottleTitle = getContext()?.resources?.getStringArray(R.array.bottle_title_array)
        matcher = Pattern.compile(getUnitRegex(bottleTitle)).matcher(unitString)
        if (matcher.find()) {
            unit = BOTTLE
        }
        return unit
    }

    private fun getUnitRegex(unitsArray: Array<String>?): String {
        val unitsArrayString = unitsArray?.joinToString("|", "(", ")") { it }

        return "" + unitsArrayString + "\\.*"
    }


}