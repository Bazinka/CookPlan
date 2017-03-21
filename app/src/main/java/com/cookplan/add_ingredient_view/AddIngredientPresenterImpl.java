package com.cookplan.add_ingredient_view;

import android.util.Log;

import com.cookplan.R;
import com.cookplan.models.Ingredient;
import com.cookplan.models.MeasureUnit;
import com.cookplan.models.Product;
import com.cookplan.utils.DatabaseConstants;
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

public class AddIngredientPresenterImpl implements AddIngredientPresenter {

    private AddIngredientView mainView;
    private DatabaseReference mDatabase;

    public AddIngredientPresenterImpl(AddIngredientView mainView) {
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
                    product.setId(itemSnapshot.getKey());
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
    public void saveIngredient(Product product, double amount, MeasureUnit newMeasureUnit) {
        if (product != null) {
            if (product.getId() == null) {
                DatabaseReference productRef = mDatabase.child(DatabaseConstants.DATABASE_PRODUCT_TABLE);
                //check if we have already had a product with the same name
                productRef.orderByChild(DatabaseConstants.DATABASE_NAME_FIELD).equalTo(product.getName())
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                Product.ProductDBObject productDB = dataSnapshot.getValue(Product.ProductDBObject.class);
                                Product product = Product.getProductFromDB(productDB);
                                if (product != null) {//we have the same product
                                    product.setId(dataSnapshot.getKey());
                                    saveIngredient(product, amount, newMeasureUnit);
                                } else {//we need to save new product
                                    productRef.push().setValue(product.getProductDBObject(), (databaseError, reference) -> {
                                        if (databaseError != null) {
                                            if (mainView != null) {
                                                mainView.setErrorToast(databaseError.getMessage());
                                            }
                                        } else {
                                            product.setId(reference.getKey());
                                            saveIngredient(product, amount, newMeasureUnit);
                                        }
                                    });
                                }
                            }

                            public void onCancelled(DatabaseError databaseError) {
                                if (mainView != null) {
                                    mainView.setErrorToast(databaseError.getMessage());
                                }
                            }
                        });
            } else {
                //update product measure list
                DatabaseReference productRef = mDatabase.child(DatabaseConstants.DATABASE_PRODUCT_TABLE);
                boolean needToUpdate = true;
                for (MeasureUnit productMeasureUnit : product.getMeasureUnitList()) {
                    if (productMeasureUnit == newMeasureUnit) {
                        needToUpdate = false;
                    }
                }
                if (needToUpdate) {
                    product.getMeasureUnitList().add(newMeasureUnit);
                    productRef.child(product.getId()).child(DatabaseConstants.DATABASE_PRODUCT_MEASURE_LIST_FIELD)
                            .setValue(product);
                }

                //save ingredient
                Ingredient ingredient = new Ingredient(product.getName(), product.getId(), newMeasureUnit, amount);
                DatabaseReference ingredRef = mDatabase.child(DatabaseConstants.DATABASE_INRGEDIENT_TABLE);
                ingredRef.push().setValue(ingredient.getIngredientDBObject(), (databaseError, reference) -> {
                    if (databaseError != null) {
                        if (mainView != null) {
                            mainView.setErrorToast(databaseError.getMessage());
                        }
                    } else {
                        if (mainView != null) {
                            mainView.setSuccessSaveIngredient();
                        }
                    }
                });
            }
        } else {
            if (mainView != null) {
                mainView.setErrorToast(R.string.unknown_product_error);
            }
        }
    }
}
