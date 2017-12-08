package com.cookplan.providers.impl

import android.os.Looper
import android.util.Log
import com.cookplan.R
import com.cookplan.RApplication
import com.cookplan.models.CookPlanError
import com.cookplan.models.Ingredient
import com.cookplan.models.ShareUserInfo
import com.cookplan.providers.IngredientProvider
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

class IngredientProviderImpl : IngredientProvider {

    private val database: DatabaseReference

    private val subjectAllIngredients: BehaviorSubject<List<Ingredient>>

    init {
        this.database = FirebaseDatabase.getInstance().reference
        subjectAllIngredients = BehaviorSubject.create()
        getFirebaseAllIngredientList()
    }

    private fun getFirebaseAllIngredientList() {
        val items = database.child(DatabaseConstants.DATABASE_INRGEDIENT_TABLE)
        items.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val allIngredients = ArrayList<Ingredient>()
                for (itemSnapshot in dataSnapshot.children) {
                    val ingredient = Ingredient.getIngredientFromDBObject(itemSnapshot)
                    allIngredients.add(ingredient)
                }
                subjectAllIngredients.onNext(allIngredients)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                if (FirebaseAuth.getInstance().currentUser != null) {
                    subjectAllIngredients.onError(CookPlanError(databaseError))
                }
            }
        })
    }


    override fun getAllIngredientsSharedToUser(shareUserInfos: List<ShareUserInfo>): Observable<List<Ingredient>> {
        return subjectAllIngredients.map { ingredientList ->
            val uid = FirebaseAuth.getInstance().currentUser?.uid
            val resultIngredient = ArrayList<Ingredient>()
            for (ingredient in ingredientList) {
                if (ingredient.userId == uid) {
                    resultIngredient.add(ingredient)
                } else if (ingredient.userId != null && !shareUserInfos.isEmpty()) {
                    for ((_, ownerUserId, ownerUserName) in shareUserInfos) {
                        if (ownerUserId?.contains(ingredient.userId) ?: false) {
                            ingredient.userName = ownerUserName
                            resultIngredient.add(ingredient)
                        }
                    }
                }
            }
            resultIngredient
        }
    }

    override fun getIngredientListByRecipeId(recipeId: String): Observable<List<Ingredient>> {
        return subjectAllIngredients.map { allIngredients ->
            val recipeIngredients = ArrayList<Ingredient>()
            for (ingredient in allIngredients) {
                if (ingredient.recipeId == recipeId) {
                    recipeIngredients.add(ingredient)
                }
            }
            recipeIngredients
        }
    }

    override fun createIngredient(ingredient: Ingredient): Single<Ingredient> {
        return Single.create { emitter ->
            val ingredRef = database.child(DatabaseConstants.DATABASE_INRGEDIENT_TABLE)
            ingredRef.push().setValue(ingredient) { databaseError, reference ->
                if (emitter != null) {
                    if (databaseError != null) {
                        emitter.onError(CookPlanError(databaseError))
                    } else {
                        emitter.onSuccess(ingredient)
                    }
                }
            }
        }
    }

    override fun removeIngredient(ingredient: Ingredient): Completable {
        return Completable.create { emitter ->
            if (ingredient.id != null) {
                val ingredRef = database.child(DatabaseConstants.DATABASE_INRGEDIENT_TABLE)
                val ref = ingredRef.child(ingredient.id)
                ref.removeValue()
                        .addOnFailureListener { exeption -> emitter.onError(CookPlanError(exeption.message)) }
                        .addOnCompleteListener { task ->
                            if (task.isComplete) {
                                emitter.onComplete()
                            }
                        }
            } else {
                emitter.onError(CookPlanError(RApplication.appContext?.getString(R.string.ingred_remove_error)))
            }
        }
    }

    override fun removeIngredientList(ingredients: List<Ingredient>): Completable {
        return Completable.create { emitter ->
            val ingredRef = database.child(DatabaseConstants.DATABASE_INRGEDIENT_TABLE)
            ingredRef.runTransaction(object : Transaction.Handler {
                override fun doTransaction(mutableData: MutableData): Transaction.Result {
                    for ((id) in ingredients) {
                        if (id != null) {
                            val ref = mutableData.child(id)
                            ref.value = null
                        } else {
                            emitter.onError(CookPlanError(RApplication.appContext?.getString(R.string.ingred_remove_error)))
                        }
                    }
                    emitter.onComplete()
                    return Transaction.success(mutableData)
                }

                override fun onComplete(databaseError: DatabaseError?, b: Boolean,
                                        dataSnapshot: DataSnapshot?) {
                    emitter.onComplete()
                }
            })
        }
    }

    override fun updateShopStatus(ingredient: Ingredient): Completable {
        return Completable.create { emitter ->
            val ingredientRef = database.child(DatabaseConstants.DATABASE_INRGEDIENT_TABLE)
            ingredientRef.runTransaction(object : Transaction.Handler {
                override fun doTransaction(mutableData: MutableData): Transaction.Result {
                    ingredientRef
                            .child(ingredient.id)
                            .child(DatabaseConstants.DATABASE_SHOP_LIST_STATUS_FIELD)
                            .setValue(ingredient.shopListStatus)
                    return Transaction.success(mutableData)
                }

                override fun onComplete(databaseError: DatabaseError?, b: Boolean,
                                        dataSnapshot: DataSnapshot?) {
                    emitter.onComplete()
                }
            })
        }
    }

    override fun updateShopStatusList(ingredients: List<Ingredient>): Completable {
        return Completable.create { emitter ->
            val ingredientRef = database.child(DatabaseConstants.DATABASE_INRGEDIENT_TABLE)
            ingredientRef.runTransaction(object : Transaction.Handler {
                override fun doTransaction(mutableData: MutableData): Transaction.Result {
                    for (ingred in ingredients) {
                        mutableData
                                .child(ingred.id)
                                .child(DatabaseConstants.DATABASE_SHOP_LIST_STATUS_FIELD)
                                .value = ingred.shopListStatus
                    }
                    if (Looper.myLooper() == Looper.getMainLooper()) {
                        Log.d("updateShopStatusList", "Main Thread")
                    } else {
                        Log.d("updateShopStatusList", "мы не в основном потоке")
                    }
                    return Transaction.success(mutableData)
                }

                override fun onComplete(databaseError: DatabaseError?, b: Boolean,
                                        dataSnapshot: DataSnapshot?) {
                    emitter.onComplete()
                }
            })
        }
    }

    //    @Override
    //    public Single<Recipe> createRecipe(Recipe recipe) {
    //        return Single.create(emitter -> {
    //            DatabaseReference recipeRef = database.child(DatabaseConstants.DATABASE_RECIPE_TABLE);
    //            recipeRef.push().setValue(recipe.getRecipeDB(), (databaseError, reference) -> {
    //                if (databaseError != null) {
    //                    if (emitter != null) {
    //                        emitter.onError(new CookPlanError(databaseError));
    //                    }
    //                } else {
    //                    recipe.setId(reference.getKey());
    //                    if (emitter != null) {
    //                        emitter.onSuccess(recipe);
    //                    }
    //                }
    //            });
    //        });
    //    }
    //
    //    @Override
    //    public Single<Recipe> update(Recipe recipe) {
    //        return Single.create(emitter -> {
    //            Map<String, Object> values = new HashMap<>();
    //            values.put(DatabaseConstants.DATABASE_NAME_FIELD, recipe.getStringName());
    //            values.put(DatabaseConstants.DATABASE_DESCRIPTION_FIELD, recipe.getDesc());
    //            DatabaseReference recipeRef = database.child(DatabaseConstants.DATABASE_RECIPE_TABLE);
    //            recipeRef.child(recipe.getId()).updateChildren(values, (databaseError, databaseReference) -> {
    //                if (databaseError != null) {
    //                    if (emitter != null) {
    //                        emitter.onError(new CookPlanError(databaseError));
    //                    }
    //                } else {
    //                    if (emitter != null) {
    //                        emitter.onSuccess(recipe);
    //                    }
    //                }
    //            });
    //        });
    //    }
    //
    //    @Override
    //    public Single<Recipe> getRecipeById(String recipeId) {
    //        return Single.create(emitter -> {
    //            Query items = database.child(DatabaseConstants.DATABASE_RECIPE_TABLE).child(recipeId);
    //            items.addListenerForSingleValueEvent(new ValueEventListener() {
    //                public void onDataChange(DataSnapshot dataSnapshot) {
    //                    if (dataSnapshot.getValue() != null) {
    //                        Recipe recipe = Recipe.getRecipeFromDBObject(dataSnapshot);
    //                        if (emitter != null) {
    //                            emitter.onSuccess(recipe);
    //                        }
    //                    } else {
    //                        if (emitter != null) {
    //                            emitter.onSuccess(null);
    //                        }
    //                    }
    //                }
    //
    //                public void onCancelled(DatabaseError databaseError) {
    //                    if (emitter != null) {
    //                        emitter.onError(new CookPlanError(databaseError));
    //                    }
    //                }
    //            });
    //        });
    //    }
    //
}
