package com.cookplan.add_ingredient_view;

import com.cookplan.R;
import com.cookplan.models.Ingredient;
import com.cookplan.models.MeasureUnit;
import com.cookplan.models.Product;
import com.cookplan.models.Recipe;
import com.cookplan.models.ShopListStatus;
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
    private boolean isNeedToBuy;

    public AddIngredientPresenterImpl(AddIngredientView mainView) {
        this.mainView = mainView;
        this.database = FirebaseDatabase.getInstance().getReference();
        isNeedToBuy = false;
    }

    @Override
    public void getAsyncProductList() {
        Query items = database.child(DatabaseConstants.DATABASE_PRODUCT_TABLE);
        items.addValueEventListener(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Product> products = new ArrayList<>();
                for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()) {
                    Product product = Product.parseProductFromDB(itemSnapshot);
                    if (product != null) {
                        products.add(product);
                    }
                }
                if (mainView != null && mainView.isAddedToActivity()) {
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
                    productRef.child(product.getId()).child(DatabaseConstants.DATABASE_MEASURE_LIST_FIELD)
                            .setValue(product.getMeasureUnitList());
                }

                //save ingredient
                Ingredient ingredient = new Ingredient();
                ingredient.setName(product.getName());
                ingredient.setProductId(product.getId());
                ingredient.setRecipeId(recipe != null ? recipe.getId() : null);
                ingredient.setMeasureUnit(newMeasureUnit);
                ingredient.setAmount(amount);
                ingredient.setShopListStatus(isNeedToBuy ? ShopListStatus.NEED_TO_BUY : ShopListStatus.NONE);
                DatabaseReference ingredRef = database.child(DatabaseConstants.DATABASE_INRGEDIENT_TABLE);
                ingredRef.push().setValue(ingredient, (databaseError, reference) -> {
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
                                saveIngredient(Product.parseProductFromDB(child), amount, newMeasureUnit);
                            }
                        } else {//we need to save new product
                            DatabaseReference productRef = database.child(DatabaseConstants.DATABASE_PRODUCT_TABLE);
                            productRef.push().setValue(product, (databaseError, reference) -> {
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

    @Override
    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    @Override
    public void setIsNeedToBuy(boolean isNeedToBuy) {
        this.isNeedToBuy = isNeedToBuy;
    }
}
