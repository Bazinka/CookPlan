package com.cookplan.providers.impl

import com.cookplan.models.CookPlanError
import com.cookplan.models.Product
import com.cookplan.providers.ProductProvider
import com.cookplan.utils.DatabaseConstants
import com.cookplan.utils.Utils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject
import java.util.regex.Pattern

/**
 * Created by DariaEfimova on 24.04.17.
 */

class ProductProviderImpl : ProductProvider {

    private val database: DatabaseReference

    private val subjectProductList: BehaviorSubject<List<Product>>

    init {
        this.database = FirebaseDatabase.getInstance().reference
        subjectProductList = BehaviorSubject.create()
        getFirebaseAllProductList()
    }

    private fun getFirebaseAllProductList() {
        val items = database.child(DatabaseConstants.DATABASE_PRODUCT_TABLE)
        items.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val products = dataSnapshot.children
                        .map { Product.parseProductFromDB(it) }
                        .filter {
                            (it.userId == null
                                    || it.userId == FirebaseAuth.getInstance().currentUser?.uid)
                        }
                        .sortedByDescending { it.countUsing }
                subjectProductList.onNext(products)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                subjectProductList.onError(CookPlanError(databaseError))
            }
        })
    }

    override fun getProductList(): Observable<List<Product>> {
        return subjectProductList
    }

    override fun getCompanyProductList(companyId: String): Observable<List<Product>> {
        return subjectProductList.map { productList ->
            val products = mutableListOf<Product>()
            for (product in productList) {
                if (product.companyIdList.contains(companyId)) {
                    products.add(product)
                }
            }
            products
        }
    }

    override fun getTheClosestProductsToStrings(names: List<String>): Observable<MutableMap<String, List<Product>>> {
        return subjectProductList.map { allProducts ->
            val result = mutableMapOf<String, List<Product>>()
            for (productName in names) {
                val products = mutableListOf<Product>()
                var foundProduct = false
                for (product in allProducts) {
                    if (Utils.isStringEquals(productName, product.toStringName())) {
                        products.add(product)
                        foundProduct = true
                        if (productName.split("\\s+".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray().size != 1) {
                            break
                        }
                    }
                }
                if (!foundProduct) {//if didn't found special product, looking for suppose products
                    val regExString = Utils.getRegexAtLeastOneWord(productName)
                    val p = Pattern.compile(regExString)
                    for (product in allProducts) {
                        val matcher = p.matcher(product.toStringName().toLowerCase())
                        if (matcher.find()) {
                            products.add(product)
                        }
                    }
                }
                result.put(productName, products)
            }
            result
        }
    }

    //emitter.onError(new CookPlanError(RApplication.getAppContext().getString(R.string.recipe_doesnt_exist)));
    override fun createProduct(product: Product): Single<Product> {
        return Single.create { emitter ->
            val productRef = database.child(DatabaseConstants.DATABASE_PRODUCT_TABLE)
            productRef.push().setValue(product) { databaseError, reference ->
                if (databaseError != null) {
                    emitter.onError(CookPlanError(databaseError))
                } else {
                    product.id = reference.key
                    emitter.onSuccess(product)
                }
            }
        }
    }

    override fun updateProductNames(product: Product): Single<Product> {
        return Single.create { emitter ->
            val values = mutableMapOf<String, Any>()
            values.put(DatabaseConstants.DATABASE_PRODUCT_RUS_NAME_FIELD, product.rusName)
            values.put(DatabaseConstants.DATABASE_PRODUCT_ENG_NAME_FIELD, product.engName)
            val productRef = database.child(DatabaseConstants.DATABASE_PRODUCT_TABLE)
            productRef.child(product.id).updateChildren(values) { databaseError, databaseReference ->
                if (databaseError != null) {
                    emitter?.onError(CookPlanError(databaseError))
                } else {
                    emitter?.onSuccess(product)
                }
            }
        }
    }

    override fun updateProductCompanies(product: Product): Completable {
        return Completable.create { emitter ->
            val values = mutableMapOf<String, Any>()
            values.put(DatabaseConstants.DATABASE_COMPANY_ID_LIST_FIELD, product.companyIdList)
            val productRef = database.child(DatabaseConstants.DATABASE_PRODUCT_TABLE)
            productRef.child(product.id).updateChildren(values) { databaseError, databaseReference ->
                if (databaseError != null) {
                    emitter?.onError(CookPlanError(databaseError))
                } else {
                    emitter?.onComplete()
                }
            }
        }
    }

    override fun getProductByName(name: String): Observable<Product> {
        return subjectProductList.map { productList ->
            var productRes: Product? = null
            for (product in productList) {
                if (product.toStringName().toLowerCase() == name) {
                    productRes = product
                }
            }
            productRes
        }
    }

    override fun increaseCountUsages(product: Product): Completable {
        return Completable.create { emitter ->
            val productRef = database.child(DatabaseConstants.DATABASE_PRODUCT_TABLE)
            productRef.child(product.id)
                    .child(DatabaseConstants.DATABASE_PRODUCT_COUNT_USING_FIELD)
                    .setValue(product.increasingCount()) { databaseError, reference ->
                        if (emitter != null) {
                            if (databaseError != null) {
                                emitter.onError(CookPlanError(databaseError))
                            } else {
                                emitter.onComplete()
                            }
                        }
                    }
        }
    }
}