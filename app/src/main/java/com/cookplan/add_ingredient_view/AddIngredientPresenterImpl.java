package com.cookplan.add_ingredient_view;

import com.cookplan.R;
import com.cookplan.models.Ingredient;
import com.cookplan.models.MeasureUnit;
import com.cookplan.models.Product;
import com.cookplan.models.Recipe;
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
    private DatabaseReference database;
    private Recipe recipe;

    public AddIngredientPresenterImpl(AddIngredientView mainView) {
        this.mainView = mainView;
        this.database = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public void getAsyncProductList() {
        Query items = database.child(DatabaseConstants.DATABASE_PRODUCT_TABLE);
        items.addValueEventListener(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {

                List<Product> products = new ArrayList<>();
                for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()) {
                    Product.ProductDBObject productDB = Product.ProductDBObject.parseProductDBObject(itemSnapshot);
                    Product product = Product.getProductFromDB(productDB);
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
                saveProduct(product, amount, newMeasureUnit);
            } else {
                //update product measure list
                DatabaseReference productRef = database.child(DatabaseConstants.DATABASE_PRODUCT_TABLE);
                boolean needToUpdate = true;
                for (MeasureUnit productMeasureUnit : product.getMeasureUnitList()) {
                    if (productMeasureUnit == newMeasureUnit) {
                        needToUpdate = false;
                    }
                }
                if (needToUpdate) {
                    product.getMeasureUnitList().add(newMeasureUnit);
                    productRef.child(product.getId()).child(DatabaseConstants.DATABASE_PRODUCT_MEASURE_LIST_FIELD)
                            .setValue(product.getProductDBObject().getMeasureUnitIdList());
                }

                //save ingredient
                Ingredient ingredient = new Ingredient(product.getName(),
                        product.getId(),
                        recipe != null ? recipe.getId() : null,
                        newMeasureUnit,
                        amount);
                DatabaseReference ingredRef = database.child(DatabaseConstants.DATABASE_INRGEDIENT_TABLE);
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

    private void saveProduct(Product product, double amount, MeasureUnit newMeasureUnit) {
        DatabaseReference productRef = database.child(DatabaseConstants.DATABASE_PRODUCT_TABLE);
        //check if we have already had a product with the same name
        productRef.orderByChild(DatabaseConstants.DATABASE_NAME_FIELD).equalTo(product.getName())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getChildrenCount() > 0) {//we have the same product
                            for (DataSnapshot child : dataSnapshot.getChildren()) {
                                Product.ProductDBObject productDB = Product.ProductDBObject.parseProductDBObject(child);
                                saveIngredient(Product.getProductFromDB(productDB), amount, newMeasureUnit);
                            }
                        } else {//we need to save new product
                            DatabaseReference productRef = database.child(DatabaseConstants.DATABASE_PRODUCT_TABLE);
                            productRef.push().setValue(product.getProductDBObject(), (databaseError, reference) -> {
                                if (databaseError != null) {
                                    if (mainView != null) {
                                        mainView.setErrorToast(databaseError.getMessage());
                                    }
                                } else {
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
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }
}
