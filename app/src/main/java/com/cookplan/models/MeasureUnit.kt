package com.cookplan.models

import com.cookplan.R
import com.cookplan.RApplication
import java.io.Serializable
import java.util.*
import java.util.regex.Pattern

/**
 * Created by DariaEfimova on 20.03.17.
 */

enum class MeasureUnit private constructor(val id: Int, private val nameRecourseId: Int, private val isItIntValue: Boolean) : Serializable {
    UNITS(0, R.string.unit_title_main, false),
    GRAMM(1, R.string.gramm_title_main, true),
    KILOGRAMM(2, R.string.kilogramm_title_main, false),
    LITRE(3, R.string.litre_title_main, false),
    MILILITRE(4, R.string.mililitre_title_main, true),
    CUP(5, R.string.cup_title_main, false),
    TEASPOON(6, R.string.teaspoon_title_main, false),
    TABLESPOON(7, R.string.tablespoon_title_main, true),
    BOTTLE(8, R.string.bottle_title_main, false),
    PACKAGE(9, R.string.package_title_main, false);

    override fun toString(): String {
        return RApplication.appContext?.getString(nameRecourseId) ?: ""
    }

    fun toValueString(value: Double): String {
        val valueString: String
        if (value < 1e-8 && value > -1e-8) {//value==0.0
            return RApplication.appContext?.getString(R.string.by_the_taste) ?: ""
        }

        if (this == KILOGRAMM && value < 1.0) {
            valueString = getStringOfValue(value * 1000) + " " + GRAMM.toString()
        } else if (this == GRAMM && value > 1000.0) {
            valueString = getStringOfValue(value / 1000) + " " + KILOGRAMM.toString()
        } else if (this == LITRE && value < 1.0) {
            valueString = getStringOfValue(value * 1000) + " " + MILILITRE.toString()
        } else if (this == MILILITRE && value > 1000.0) {
            valueString = getStringOfValue(value / 1000) + " " + LITRE.toString()
        } else {
            valueString = getStringOfValue(value) + " " + toString()
        }
        return valueString
    }

    fun toStringForShopList(value: Double): String {
        val valueString: String
        if (value < 1e-8 && value > -1e-8) {//value==0.0
            return ""
        }

        if (this == KILOGRAMM && value < 1.0) {
            valueString = getStringOfValue(value * 1000) + " " + GRAMM.toString()
        } else if (this == GRAMM && value > 1000.0) {
            valueString = getStringOfValue(value / 1000) + " " + KILOGRAMM.toString()
        } else if (this == LITRE && value < 1.0) {
            valueString = getStringOfValue(value * 1000) + " " + MILILITRE.toString()
        } else if (this == MILILITRE && value > 1000.0) {
            valueString = getStringOfValue(value / 1000) + " " + LITRE.toString()
        } else {
            valueString = getStringOfValue(value) + " " + toString()
        }
        return valueString
    }

    fun getStringOfValue(value: Double): String {
        val valueString: String
        if (isItIntValue(value)) {
            valueString = Math.round(value).toString()
        } else {
            if (value > 10.0) {
                valueString = Math.round(value).toString()
            } else {
                valueString = String.format(Locale.getDefault(), "%.1f", value)
            }
        }
        return valueString
    }

    private fun isItIntValue(value: Double): Boolean {
        return if (!isItIntValue) {
            if (value == Math.floor(value) && !java.lang.Double.isInfinite(value)) {
                true
            } else false
        } else {
            true
        }
    }

    companion object {

        fun getMultiplier(from: MeasureUnit, to: MeasureUnit): Double {
            if (from == to) {
                return 1.0
            }

            when (from) {
                UNITS -> {
                    when (to) {
                        PACKAGE -> return 1.0
                        BOTTLE -> return 1.0
                        else -> return -1.0
                    }
                }
                PACKAGE -> {
                    when (to) {
                        UNITS -> return 1.0
                        BOTTLE -> return 1.0
                        else -> return -1.0
                    }

                }
                BOTTLE -> {
                    when (to) {
                        UNITS -> return 1.0
                        PACKAGE -> return 1.0
                        else -> return -1.0
                    }

                }
                GRAMM -> {
                    when (to) {
                        GRAMM -> return 1.0
                        KILOGRAMM -> return 0.001
                        LITRE -> return 0.001
                        MILILITRE -> return 1.0
                        CUP -> return 0.004
                        TEASPOON -> return 0.2
                        TABLESPOON -> return 0.06
                        else -> return -1.0
                    }
                }
                KILOGRAMM -> {
                    when (to) {
                        KILOGRAMM -> return 1.0
                        GRAMM -> return 1000.0
                        LITRE -> return 1.0
                        MILILITRE -> return 1000.0
                        CUP -> return 4.0
                        TEASPOON -> return 200.0
                        TABLESPOON -> return 66.6
                        else -> return -1.0
                    }
                }
                LITRE -> {
                    when (to) {
                        LITRE -> return 1.0
                        GRAMM -> return 1000.0
                        KILOGRAMM -> return 1.0
                        MILILITRE -> return 1000.0
                        CUP -> return 4.0
                        TEASPOON -> return 200.0
                        TABLESPOON -> return 66.6
                        else -> return -1.0
                    }
                }
                MILILITRE -> {
                    when (to) {
                        MILILITRE -> return 1.0
                        KILOGRAMM -> return 0.001
                        LITRE -> return 0.001
                        GRAMM -> return 1.0
                        CUP -> return 0.004
                        TEASPOON -> return 0.2
                        TABLESPOON -> return 0.06
                        else -> return -1.0
                    }
                }
                CUP -> {
                    when (to) {
                        CUP -> return 1.0
                        KILOGRAMM -> return 0.25
                        LITRE -> return 0.25
                        GRAMM -> return 250.0
                        MILILITRE -> return 250.0
                        TEASPOON -> return 50.0
                        TABLESPOON -> return 15.0
                        else -> return -1.0
                    }
                }
                TEASPOON -> {
                    when (to) {
                        TEASPOON -> return 1.0
                        KILOGRAMM -> return 0.005
                        LITRE -> return 0.005
                        MILILITRE -> return 5.0
                        CUP -> return 0.02
                        GRAMM -> return 5.0
                        TABLESPOON -> return 16.0
                        else -> return -1.0
                    }
                }
                TABLESPOON -> {
                    when (to) {
                        TABLESPOON -> return 1.0
                        KILOGRAMM -> return 0.015
                        LITRE -> return 0.015
                        MILILITRE -> return 15.0
                        CUP -> return 0.06
                        GRAMM -> return 15.0
                        TEASPOON -> return 3.0
                        else -> return -1.0
                    }
                }
                else -> return -1.0
            }
        }

        /*
    This method works only for russian language
     */
        fun parseUnit(unitString: String): MeasureUnit {
            var unit = UNITS

            //try to find UNIT
            val unitsArray = RApplication.appContext?.resources?.getStringArray(R.array.unit_title_array)
            var matcher = Pattern.compile(getUnitRegex(unitsArray)).matcher(unitString)
            if (matcher.find()) {
                unit = UNITS
            }

            //try to find GRAMM
            val grammArray = RApplication.appContext?.resources?.getStringArray(R.array.gramm_title_array)
            matcher = Pattern.compile(getUnitRegex(grammArray)).matcher(unitString)
            if (matcher.find()) {
                unit = GRAMM
            }

            //try to find KILOGRAMM
            val kilogrammTitle = RApplication.appContext?.resources?.getStringArray(R.array.kilogramm_title_array)
            matcher = Pattern.compile(getUnitRegex(kilogrammTitle)).matcher(unitString)
            if (matcher.find()) {
                unit = KILOGRAMM
            }

            //try to find LITRE
            val litreTitle = RApplication.appContext?.resources?.getStringArray(R.array.litre_title_array)
            matcher = Pattern.compile(getUnitRegex(litreTitle)).matcher(unitString)
            if (matcher.find()) {
                unit = LITRE
            }

            //try to find MILILITRE
            val mililitreTitle = RApplication.appContext?.resources?.getStringArray(R.array.mililitre_title_array)
            matcher = Pattern.compile(getUnitRegex(mililitreTitle)).matcher(unitString)
            if (matcher.find()) {
                unit = MILILITRE
            }

            //try to find CUP
            val cupTitle = RApplication.appContext?.resources?.getStringArray(R.array.cup_title_array)
            matcher = Pattern.compile(getUnitRegex(cupTitle)).matcher(unitString)
            if (matcher.find()) {
                unit = CUP
            }

            //try to find PACKAGE
            val packageTitle = RApplication.appContext?.resources?.getStringArray(R.array.package_title_array)
            matcher = Pattern.compile(getUnitRegex(packageTitle)).matcher(unitString)
            if (matcher.find()) {
                unit = PACKAGE
            }

            //try to find TEASPOON
            val teaspoonTitle = RApplication.appContext?.resources?.getStringArray(R.array.teaspoon_title_array)
            matcher = Pattern.compile(getUnitRegex(teaspoonTitle)).matcher(unitString)
            if (matcher.find()) {
                unit = TEASPOON
            }

            //try to find TABLESPOON
            val tablespoonTitle = RApplication.appContext?.resources?.getStringArray(R.array.tablespoon_title_array)
            matcher = Pattern.compile(getUnitRegex(tablespoonTitle)).matcher(unitString)
            if (matcher.find()) {
                unit = TABLESPOON
            }

            //try to find BOTTLE
            val bottleTitle = RApplication.appContext?.resources?.getStringArray(R.array.bottle_title_array)
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
}
