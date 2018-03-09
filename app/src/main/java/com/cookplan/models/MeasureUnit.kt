package com.cookplan.models

import com.cookplan.R
import com.cookplan.RApplication.Companion.isCurrentLocaleRus
import java.io.Serializable

/**
 * Created by DariaEfimova on 20.03.17.
 */

enum class MeasureUnit constructor(val id: Int, private val nameRecourseId: Int, private val isItIntValue: Boolean) : Serializable {
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

    fun getNameRecourseId(): Int {
        return nameRecourseId
    }

    fun isItIntValue(value: Double): Boolean {
        return if (!isItIntValue) {
            value == Math.floor(value) && !java.lang.Double.isInfinite(value)
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

        private val BY_TASTE_ENG_STRING = "by taste"
        private val BY_TASTE_RUS_STRING = "по вкусу"


        fun getByTasteString(): String {
            return if (isCurrentLocaleRus) {
                BY_TASTE_RUS_STRING
            } else {
                BY_TASTE_ENG_STRING
            }
        }

    }
}
