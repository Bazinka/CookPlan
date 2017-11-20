package com.cookplan.models

import android.os.Parcel
import android.os.Parcelable
import com.cookplan.RApplication
import com.cookplan.utils.DatabaseConstants
import com.google.firebase.database.DataSnapshot

/**
 * Created by DariaEfimova on 20.03.17.
 */

data class Product(var id: String? = null,
                   var userId: String? = null,
                   var measureUnitList: MutableList<MeasureUnit> = mutableListOf(), //list of units in which product can be measured.
                   var mainMeasureUnitList: MutableList<MeasureUnit> = mutableListOf(),
                   var rusName: String = "",
                   var engName: String = "",
                   var countUsing: Int = 0,
                   var category: ProductCategory = ProductCategory.WITHOUT_CATEGORY,
                   var companyIdList: MutableList<String> = mutableListOf(),
                   var ratioMeasureList: ArrayList<RatioMeasure> = arrayListOf<RatioMeasure>()) : Parcelable {
    constructor(source: Parcel) : this(
            source.readString(),
            source.readString(),
            ArrayList<MeasureUnit>().apply { source.readList(this, MeasureUnit::class.java.classLoader) },
            ArrayList<MeasureUnit>().apply { source.readList(this, MeasureUnit::class.java.classLoader) },
            source.readString(),
            source.readString(),
            source.readInt(),
            source.readSerializable() as ProductCategory) {
        companyIdList = java.util.ArrayList()
        source.readStringList(companyIdList)

        ratioMeasureList = java.util.ArrayList()
        source.readTypedList<RatioMeasure>(ratioMeasureList, RatioMeasure.CREATOR)
    }

    constructor(productCategory: ProductCategory, russianName: String?, englishName: String?,
                mainMeasUnitList: MutableList<MeasureUnit>,
                measUnitList: MutableList<MeasureUnit>,
                unitToAmoutMap: Map<MeasureUnit, Double>?,
                userId: String) : this(mainMeasureUnitList = mainMeasUnitList,
            measureUnitList = measUnitList,
            rusName = russianName ?: "",
            engName = englishName ?: "",
            category = productCategory,
            userId = userId, countUsing = 0) {
        fillRatioList(unitToAmoutMap)
    }

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(id)
        writeString(userId)
        writeList(measureUnitList)
        writeList(mainMeasureUnitList)
        writeString(rusName)
        writeString(engName)
        writeInt(countUsing)
        writeSerializable(category)
        dest.writeStringList(companyIdList)
        dest.writeTypedList(ratioMeasureList)

    }

    private fun fillRatioList(unitToAmoutMap: Map<MeasureUnit, Double>?) {
        if (unitToAmoutMap != null && !mainMeasureUnitList.isEmpty()) {
            ratioMeasureList = java.util.ArrayList()
            for ((key, value) in unitToAmoutMap) {
                ratioMeasureList.add(RatioMeasure(value, mainMeasureUnitList[0], key))
                if (mainMeasureUnitList[0] === MeasureUnit.KILOGRAMM) {
                    ratioMeasureList.add(RatioMeasure(value / 1000, MeasureUnit.GRAMM, key))
                }
                if (mainMeasureUnitList[0] === MeasureUnit.GRAMM) {
                    ratioMeasureList.add(RatioMeasure(1000 * value, MeasureUnit.KILOGRAMM, key))
                }
                if (mainMeasureUnitList[0] === MeasureUnit.LITRE) {
                    ratioMeasureList.add(RatioMeasure(value / 1000, MeasureUnit.MILILITRE, key))
                }
                if (mainMeasureUnitList[0] === MeasureUnit.MILILITRE) {
                    ratioMeasureList.add(RatioMeasure(1000 * value, MeasureUnit.LITRE, key))
                }
            }
        }
    }

    fun increasingCount(): Int {
        countUsing = countUsing + 1
        return countUsing
    }

    fun toStringName(): String {
        if (RApplication.isCurrentLocaleRus) {
            return rusName
        } else {
            return engName
        }
    }


    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Product> = object : Parcelable.Creator<Product> {
            override fun createFromParcel(source: Parcel): Product = Product(source)
            override fun newArray(size: Int): Array<Product?> = arrayOfNulls(size)
        }

        fun parseProductFromDB(dataSnapshot: DataSnapshot): Product {
            val product = Product()
            product.id = dataSnapshot.key
            for (child in dataSnapshot.children) {
                if (child.key == DatabaseConstants.DATABASE_PRODUCT_RUS_NAME_FIELD) {
                    product.rusName = child.value.toString()
                }
                if (child.key == DatabaseConstants.DATABASE_PRODUCT_ENG_NAME_FIELD) {
                    product.engName = child.value.toString()
                }
                if (child.key == DatabaseConstants.DATABASE_USER_ID_FIELD) {
                    product.userId = child.value.toString()
                }
                if (child.key == DatabaseConstants.DATABASE_PRODUCT_CATEGORY_FIELD) {
                    product.category = ProductCategory.getProductCategoryByName(child.value as String)
                }
                if (child.key == DatabaseConstants.DATABASE_RATIO_MEASURE_LIST_FIELD) {
                    for (childUnit in child.children) {
                        if (childUnit.value is Map<*, *>) {
                            try {
                                product.ratioMeasureList.add(childUnit.getValue(RatioMeasure::class.java))
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }

                        }
                    }
                }
                if (child.key == DatabaseConstants.DATABASE_MAIN_MEASURE_UNIT_LIST_FIELD) {
                    product.mainMeasureUnitList = java.util.ArrayList()
                    for (childUnit in child.children) {
                        if (childUnit.value is String) {
                            product.mainMeasureUnitList.add(MeasureUnit.valueOf(childUnit.value as String))
                        }
                    }
                }
                if (child.key == DatabaseConstants.DATABASE_COMPANY_ID_LIST_FIELD) {
                    product.companyIdList = java.util.ArrayList()
                    for (childUnit in child.children) {
                        if (childUnit.value is String) {
                            product.companyIdList.add(childUnit.getValue(String::class.java))
                        }
                    }
                }
                if (child.key == DatabaseConstants.DATABASE_MEASURE_UNIT_LIST_FIELD) {
                    product.measureUnitList = java.util.ArrayList()
                    for (childUnit in child.children) {
                        if (childUnit.value is String) {
                            product.measureUnitList.add(MeasureUnit.valueOf(childUnit.value as String))
                        }
                    }
                }
                if (child.key == DatabaseConstants.DATABASE_PRODUCT_COUNT_USING_FIELD) {
                    product.countUsing = (child.value as Long).toInt()
                }
            }
            return product
        }
    }
}
