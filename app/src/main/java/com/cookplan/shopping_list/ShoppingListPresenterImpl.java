package com.cookplan.shopping_list;

import android.util.Log;

import com.cookplan.models.Product;
import com.cookplan.utils.DatabaseConstants;
import com.google.firebase.auth.FirebaseAuth;
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

public class ShoppingListPresenterImpl implements ShoppingListPresenter {

    private ShoppingListView mainView;
    private DatabaseReference mDatabase;

    public ShoppingListPresenterImpl(ShoppingListView mainView) {
        this.mainView = mainView;
        this.mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public void getAsyncProductList() {
        Query items = mDatabase.child(DatabaseConstants.DATABASE_PRODUCT_TABLE);
        items.addValueEventListener(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Product> products = new ArrayList<>();
                for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()) {
                    Product.ProductDBObject productDB = itemSnapshot.getValue(Product.ProductDBObject.class);
                    Product product = Product.getProductFromDB(productDB);
                    Log.i("getAsyncProductList", itemSnapshot.child("name").getValue(String.class));
                    if (product != null) {
                        products.add(product);
                    }
                }
                if (mainView != null) {
                    mainView.setProductsList(products);
                }
            }

            public void onCancelled(DatabaseError databaseError) {
                if (mainView != null) {
                    mainView.setErrorToast(databaseError.getMessage());
                }
            }
        });
    }

    @Override
    public void getAsyncIngredientList() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String uid = null;
        if (auth != null && auth.getCurrentUser() != null) {
            uid = auth.getCurrentUser().getUid();
        }
        if (uid != null) {
            Query items = mDatabase.child(DatabaseConstants.DATABASE_INRGEDIENT_TABLE).
                    orderByChild(DatabaseConstants.DATABASE_USER_ID_FIELD).equalTo(uid);
            items.addValueEventListener(new ValueEventListener() {
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()) {
                        Log.i("getAsyncIngredientList", itemSnapshot.child("name").getValue(String.class));
                    }
                }

                public void onCancelled(DatabaseError databaseError) {
                    if (mainView != null) {
                        mainView.setErrorToast(databaseError.getMessage());
                    }
                }
            });
        }
    }
}
