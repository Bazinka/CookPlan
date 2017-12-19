package com.cookplan.utils

import android.content.Context
import android.util.Log
import android.util.TypedValue
import com.cookplan.models.MeasureUnit
import com.cookplan.models.MeasureUnit.*
import java.util.*
import java.util.regex.Pattern

/**
 * Created by DariaEfimova on 16.03.17.
 */

object Utils {


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

    fun log(tag: String, textLog: String) {
        Log.d(tag, textLog)
    }

    /**
     * Converting dp to pixel
     */
    fun dpToPx(context: Context, dp: Int): Int {
        val r = context.resources
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), r.displayMetrics))
    }

    fun isStringEquals(firstString: String, secondString: String): Boolean {
        var isEqual = false
        if (firstString == secondString) {
            isEqual = true
        } else {
            val regExString = Utils.getRegexAllLowerCaseWords(firstString)
            val matcher = Pattern
                    .compile(regExString)
                    .matcher(secondString.toLowerCase())
            if (matcher.find()) {
                isEqual = true
            }
        }
        return isEqual
    }

    fun getDoubleFromString(string: String): Double {
        var amount = 0.toDouble()
        try {
            if (string == "½") {
                amount = 0.5
            } else if (string == "¾") {
                amount = 3.toDouble() / 4.toDouble()
            } else if (string == "¼") {
                amount = 1.toDouble() / 4.toDouble()
            } else {
                amount = java.lang.Double.valueOf(string)
            }
        } catch (e: Exception) {
            //something went wrong, it means amount = 0;
            e.printStackTrace()
        }

        return amount
    }

    fun isStringUrl(url: String): Boolean {
        return url.contains("http")
    }


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

    fun getRegexAtLeastOneWord(name: String): String {
        val splits = name.split("\\s+".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        var result = ""
        if (splits.size > 1) {
            val splitsLowerCase = name.toLowerCase().split("\\s+".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            result = "("
            for (i in 0 until splits.size - 1) {
                if (splits[i] == splitsLowerCase[i]) {
                    result = result + "\\b" + splits[i] + "\\b|"
                } else {
                    result = result + "\\b" + splits[i] + "\\b|\\b" + splitsLowerCase[i] + "\\b|"
                }
            }
            if (splits[splits.size - 1] == splitsLowerCase[splits.size - 1]) {
                result = result + "\\b" + splits[splits.size - 1] + "\\b)"
            } else {
                result = result + "\\b" + splits[splits.size - 1] + "\\b|" + splitsLowerCase[splits.size - 1] + "\\b)"
            }
        } else {
            result = "(\\b" + name + "\\b|\\b" + name.toLowerCase() + "\\b)"
        }
        return result


//        String[] splits = name.split("\\s+");
//        String result = "";
//        if (splits.length > 1) {
//            String[] splitsLowerCase = name.toLowerCase().split("\\s+");
//            result = "(";
//            for (int i = 0; i < splits.length - 1; i++) {
//                if (splits[i].equals(splitsLowerCase[i])) {
//                    result = result + "\\b" + splits[i] + "\\b|";
//                } else {
//                    result = result + "\\b" + splits[i] + "\\b|\\b" + splitsLowerCase[i] + "\\b|";
//                }
//            }
//            if (splits[splits.length - 1].equals(splitsLowerCase[splits.length - 1])) {
//                result = result + "\\b" + splits[splits.length - 1] + "\\b)";
//            } else {
//                result = result + "\\b" + splits[splits.length - 1] + "\\b|" + splitsLowerCase[splits.length - 1] + "\\b)";
//            }
//        } else {
//            result = "(\\b" + name + "\\b|\\b" + name.toLowerCase() + "\\b)";
//        }
//        return result;

    }

    fun getRegexAllLowerCaseWords(name: String): String {
        val splitsLowerCase = name.toLowerCase().split("\\s+".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        var result = ""
        if (splitsLowerCase.size > 1) {
            for (split in splitsLowerCase) {
                result = "$result(?=.*\\b$split\\b)"
            }
        } else {
            result = "(?=.*\\b" + name.toLowerCase() + "\\b)"
        }
        return result

//        String[] splitsLowerCase = name.toLowerCase().split("\\s+");
//        String result = "";
//        if (splitsLowerCase.length > 1) {
//            for (String split : splitsLowerCase) {
//                result = result + "(?=.*\\b" + split + "\\b)";
//            }
//        } else {
//            result = "(?=.*\\b" + name.toLowerCase() + "\\b)";
//        }
//        return result;
    }
    val SEPARATOR_IN_TEXT = "\n"//[\r\n]+
}
