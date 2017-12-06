package com.cookplan.models

import com.cookplan.utils.DatabaseConstants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.PropertyName
import java.io.Serializable
import java.util.*

/**
 * Created by DariaEfimova on 16.03.17.
 */

data class Recipe(var id: String? = null,
                  var name: String? = null,
                  var desc: String = String(),
                  var imageUrls: List<String> = listOf(),
                  val userId: String? = null,
                  var userName: String? = null) : Serializable, Comparable<Recipe> {

    override fun compareTo(other: Recipe): Int {
        return this.name?.compareTo(other.name ?: String()) ?: 0
    }

    constructor(nameRecipe: String? = null,
                descRecipe: String? = String()) : this(name = nameRecipe, desc = descRecipe ?: String())

    val recipeDB: RecipeDB
        get() {
            val auth = FirebaseAuth.getInstance()
            return RecipeDB(auth.currentUser?.uid, name, desc, imageUrls = imageUrls)
        }

    val imageUrlArrayList: ArrayList<String>
        get() = ArrayList(imageUrls)

    data class RecipeDB(var id: String? = null,
                        @PropertyName(DatabaseConstants.DATABASE_NAME_FIELD) var name: String? = null,
                        @PropertyName(DatabaseConstants.DATABASE_DESCRIPTION_FIELD) var desc: String = String(),
                        @PropertyName(DatabaseConstants.DATABASE_USER_ID_FIELD) var userId: String? = null,
                        @PropertyName(DatabaseConstants.DATABASE_IMAGE_URL_LIST_FIELD) var imageUrls: List<String> = listOf()) {
    }

    companion object {

        fun getRecipeFromDBObject(itemSnapshot: DataSnapshot): Recipe {
            val `object` = itemSnapshot.getValue(RecipeDB::class.java)
            return Recipe(itemSnapshot.key, `object`.name, `object`.desc, `object`.imageUrls, `object`.userId)
        }
    }
}
