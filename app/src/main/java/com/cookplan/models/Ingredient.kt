package com.cookplan.models

import android.os.Parcel
import android.os.Parcelable
import com.cookplan.utils.DatabaseConstants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import java.util.*

/**
 * Created by DariaEfimova on 18.03.17.
 */
data class Ingredient(var id: String? = null,
                      var name: String? = null,
                      var productId: String? = null,
                      var recipeId: String? = null,
                      var mainMeasureUnit: MeasureUnit = MeasureUnit.UNITS,
                      var mainAmount: Double = -1.toDouble(),
                      var amountString: String? = null,
                      var shopAmountList: MutableList<Double> = mutableListOf(),
                      var shopMeasureList: MutableList<MeasureUnit> = mutableListOf(),
                      var shopListStatus: ShopListStatus? = null,
                      var category: ProductCategory = ProductCategory.WITHOUT_CATEGORY) : Parcelable {
    var userId: String? = FirebaseAuth.getInstance().currentUser?.uid

    var userName: String? = null

    constructor(productName: String,
                shopAmountString: String,
                status: ShopListStatus,
                productCategory: ProductCategory) : this(name = productName, amountString = shopAmountString,
            shopListStatus = status, category = productCategory)

    constructor(source: Parcel) : this(
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readSerializable() as MeasureUnit,
            source.readValue(Double::class.java.classLoader) as Double,
            source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(id)
        writeString(name)
        writeString(productId)
        writeString(recipeId)
        writeSerializable(mainMeasureUnit)
        writeValue(mainAmount)
        writeString(amountString)
    }

    constructor(id: String?, name: String, product: Product?, recipeId: String?,
                localMeasureUnit: MeasureUnit, mainAmount: Double,
                shopListStatus: ShopListStatus) : this() {
        this.id = id
        this.name = name
        this.recipeId = recipeId
        this.mainMeasureUnit = localMeasureUnit
        this.mainAmount = mainAmount
        this.shopListStatus = shopListStatus
        if (product != null) {
            this.category = product.category
            this.productId = product.id
            shopAmountList = ArrayList()
            shopMeasureList = ArrayList()
            var shopListAmount = -1.0
            for (unit in product.mainMeasureUnitList) {
                var multiplier = MeasureUnit.getMultiplier(mainMeasureUnit, unit)
                if (product.ratioMeasureList != null) {
                    for (ratio in product.ratioMeasureList) {
                        if (ratio.getMultiplier(mainMeasureUnit, unit) > 1e-8) {
                            multiplier = ratio.getMultiplier(mainMeasureUnit, unit)
                        }
                    }
                }
                if (multiplier > 1e-8) {
                    shopListAmount = multiplier * mainAmount
                }
                shopAmountList.add(shopListAmount)
                shopMeasureList.add(unit)
            }
        }
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Ingredient> = object : Parcelable.Creator<Ingredient> {
            override fun createFromParcel(source: Parcel): Ingredient = Ingredient(source)
            override fun newArray(size: Int): Array<Ingredient?> = arrayOfNulls(size)
        }

        fun getIngredientFromDBObject(itemSnapshot: DataSnapshot): Ingredient {
            val ingredient = Ingredient()
            ingredient.id = itemSnapshot.key
            for (child in itemSnapshot.children) {
                if (child.key == DatabaseConstants.DATABASE_NAME_FIELD) {
                    ingredient.name = child.value.toString()
                }
                if (child.key == DatabaseConstants.DATABASE_USER_ID_FIELD) {
                    ingredient.userId = child.value.toString()
                }
                if (child.key == DatabaseConstants.DATABASE_PRODUCT_ID_FIELD) {
                    ingredient.productId = child.value.toString()
                }
                if (child.key == DatabaseConstants.DATABASE_INRGEDIENT_RECIPE_ID_FIELD) {
                    ingredient.recipeId = child.value.toString()
                }
                if (child.key == DatabaseConstants.DATABASE_MAIN_MEASURE_UNIT_FIELD) {
                    ingredient.mainMeasureUnit = MeasureUnit.valueOf(child.value as String)
                }
                if (child.key == DatabaseConstants.DATABASE_MAIN_AMOUNT_FIELD) {
                    if (child.value is Double) {
                        ingredient.mainAmount = child.value as Double
                    } else {
                        ingredient.mainAmount = (child.value as Long).toDouble()
                    }
                }
                if (child.key == DatabaseConstants.DATABASE_SHOP_AMOUNT_LIST_FIELD) {
                    ingredient.shopAmountList = ArrayList()
                    for (childUnit in child.children) {
                        val value: Double
                        if (childUnit.value is Long) {
                            value = (childUnit.value as Long).toDouble()
                        } else {
                            value = childUnit.value as Double
                        }
                        ingredient.shopAmountList.add(value)
                    }
                }
                if (child.key == DatabaseConstants.DATABASE_SHOP_MEASURE_LIST_FIELD) {
                    ingredient.shopMeasureList = ArrayList()
                    for (childUnit in child.children) {
                        if (childUnit.value is String) {
                            ingredient.shopMeasureList.add(MeasureUnit.valueOf(childUnit.value as String))
                        }
                    }
                }
                if (child.key == DatabaseConstants.DATABASE_PRODUCT_CATEGORY_FIELD) {
                    ingredient.category = ProductCategory.getProductCategoryByName(child.value as String)
                }
                if (child.key == DatabaseConstants.DATABASE_SHOP_LIST_STATUS_FIELD) {
                    ingredient.shopListStatus = ShopListStatus.getShopListStatusName(child.value as String)
                }
            }
            return ingredient
        }
    }
}
