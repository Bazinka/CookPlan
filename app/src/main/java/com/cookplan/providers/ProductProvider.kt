package com.cookplan.providers

import com.cookplan.models.Product

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

/**
 * Created by DariaEfimova on 24.04.17.
 */

interface ProductProvider {

    fun getProductList(): Observable<List<Product>>

    fun getCompanyProductList(companyId: String): Observable<List<Product>>

    fun getTheClosestProductsToStrings(names: List<String>): Observable<MutableMap<String, List<Product>>>

    fun createProduct(product: Product): Single<Product>

    fun updateProductNames(product: Product): Single<Product>

    fun updateProductCompanies(product: Product): Completable

    fun getProductByName(name: String): Observable<Product>

    fun increaseCountUsages(product: Product): Completable
}
