package com.cookplan.providers.impl

import com.cookplan.R
import com.cookplan.RApplication
import com.cookplan.models.CookPlanError
import com.cookplan.models.Recipe
import com.cookplan.models.ShareUserInfo
import com.cookplan.providers.RecipeProvider
import com.cookplan.utils.DatabaseConstants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject
import java.util.*

/**
 * Created by DariaEfimova on 24.04.17.
 */

class RecipeProviderImpl : RecipeProvider {


    private val database: DatabaseReference

    private val subjectRecipeList: BehaviorSubject<List<Recipe>>

    init {
        this.database = FirebaseDatabase.getInstance().reference
        subjectRecipeList = BehaviorSubject.create()
        getFirebaseAllRecipeList()
    }

    private fun getFirebaseAllRecipeList() {
        val items = database.child(DatabaseConstants.DATABASE_RECIPE_TABLE)
        items.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val recipes = ArrayList<Recipe>()
                for (itemSnapshot in dataSnapshot.children) {
                    val recipe = Recipe.getRecipeFromDBObject(itemSnapshot)
                    recipes.add(recipe)
                }

                subjectRecipeList.onNext(recipes)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                if (FirebaseAuth.getInstance().currentUser != null) {
                    subjectRecipeList.onError(CookPlanError(databaseError))
                }
            }
        })
    }

    override fun getSharedToMeRecipeList(sharedInfoList: List<ShareUserInfo>): Observable<List<Recipe>> {
        return subjectRecipeList.map { allRecipes ->
            val uid = FirebaseAuth.getInstance().currentUser?.uid
            val resultRecipes = ArrayList<Recipe>()
            for (recipe in allRecipes) {
                if (recipe.userId == uid) {
                    resultRecipes.add(recipe)
                } else if (recipe.userId != null) {
                    for ((_, ownerUserId, ownerUserName) in sharedInfoList) {
                        if (ownerUserId?.contains(recipe.userId) ?: false) {
                            recipe.userName = ownerUserName
                            resultRecipes.add(recipe)
                        }
                    }
                }
            }
            resultRecipes
        }
    }


    override fun createRecipe(recipe: Recipe): Single<Recipe> {
        return Single.create { emitter ->
            val recipeRef = database.child(DatabaseConstants.DATABASE_RECIPE_TABLE)
            recipeRef.push().setValue(recipe.recipeDB) { databaseError, reference ->
                if (databaseError != null) {
                    emitter?.onError(CookPlanError(databaseError))
                } else {
                    recipe.id = reference.key
                    emitter?.onSuccess(recipe)
                }
            }
        }
    }

    override fun update(recipe: Recipe): Single<Recipe> {
        return Single.create { emitter ->
            val values = HashMap<String, Any>()
            values.put(DatabaseConstants.DATABASE_NAME_FIELD, recipe.name ?: String())
            values.put(DatabaseConstants.DATABASE_DESCRIPTION_FIELD, recipe.desc)
            values.put(DatabaseConstants.DATABASE_IMAGE_URL_LIST_FIELD, recipe.imageUrls)
            val recipeRef = database.child(DatabaseConstants.DATABASE_RECIPE_TABLE)
            recipeRef.child(recipe.id).updateChildren(values) { databaseError, databaseReference ->
                if (databaseError != null) {
                    emitter?.onError(CookPlanError(databaseError))
                } else {
                    emitter?.onSuccess(recipe)
                }
            }
        }
    }

    override fun getRecipeById(recipeId: String): Single<Recipe> {
        return Single.create { emitter ->
            val items = database.child(DatabaseConstants.DATABASE_RECIPE_TABLE).child(recipeId)
            items.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.value != null) {
                        val recipe = Recipe.getRecipeFromDBObject(dataSnapshot)
                        emitter?.onSuccess(recipe)
                    } else {
                        emitter?.onError(CookPlanError())
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    emitter?.onError(CookPlanError(databaseError))
                }
            })
        }
    }

    override fun removeRecipe(recipe: Recipe): Completable {
        return Completable.create { emitter ->
            if (recipe.id != null) {
                val recipeRef = database.child(DatabaseConstants.DATABASE_RECIPE_TABLE)
                val ref = recipeRef.child(recipe.id)
                ref.removeValue()
                        .addOnFailureListener { exeption -> emitter.onError(CookPlanError(exeption.message)) }
                        .addOnCompleteListener { task ->
                            if (task.isComplete) {
                                emitter.onComplete()
                            }
                        }
            } else {
                emitter.onError(CookPlanError(RApplication.appContext!!.getString(R.string.recipe_doesnt_exist)))
            }
        }
    }
}
