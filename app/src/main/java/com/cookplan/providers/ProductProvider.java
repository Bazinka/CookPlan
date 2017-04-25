package com.cookplan.providers;

import com.cookplan.models.Product;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;

/**
 * Created by DariaEfimova on 24.04.17.
 */

public interface ProductProvider {

    Observable<List<Product>> getProductList();

    Single<Product> createProduct(Product product);

    Completable increaseCountUsages(Product product);
}
