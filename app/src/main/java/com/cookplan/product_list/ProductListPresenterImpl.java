package com.cookplan.product_list;


import com.cookplan.models.Product;
import com.cookplan.utils.DatabaseConstants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DariaEfimova on 20.03.17.
 */

public class ProductListPresenterImpl implements ProductListPresenter {

    private ProductListView mainView;
    private DatabaseReference database;

    public ProductListPresenterImpl(ProductListView mainView) {
        this.mainView = mainView;
        this.database = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public void getProductList() {
        Query items = database.child(DatabaseConstants.DATABASE_PRODUCT_TABLE);
        items.orderByChild(DatabaseConstants.DATABASE_PRODUCT_COUNT_USING_FIELD)
                .addValueEventListener(new ValueEventListener() {
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        List<Product> productList = new ArrayList<>();
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()) {
                            Product product = Product.parseProductFromDB(itemSnapshot);
                            if (product != null && user != null) {
                                if (product.getUserId() == null || product.getUserId().equals(user.getUid())) {
                                    productList.add(product);
                                }
                            }
                        }
                        if (mainView != null) {
                            if (productList.size() != 0) {
                                mainView.setProductList(productList);
                            } else {
                                mainView.setEmptyView();
                            }
                        }
                    }

                    public void onCancelled(DatabaseError databaseError) {
                        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                            if (mainView != null) {
                                mainView.setErrorToast(databaseError.getMessage());
                            }
                        }
                    }
                });
    }
}