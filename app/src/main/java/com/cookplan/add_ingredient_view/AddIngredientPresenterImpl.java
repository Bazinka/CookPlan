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
import java.util.Map;

/**
 * Created by DariaEfimova on 20.03.17.
 */

public class AddIngredientPresenterImpl implements AddIngredientPresenter {

    private AddIngredientView mainView;
    private DatabaseReference database;
    private Recipe recipe;
    private boolean isNeedToBuy;
    private double lastInputAmount;

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
                lastInputAmount = amount;
                saveProduct(product, newMeasureUnit);
            } else {
                //update product measure map
                boolean needToUpdate = true;
                for (Map.Entry<MeasureUnit, Double> entry : product.getMeasureUnitToAmoutMap().entrySet()) {
                    if (entry.getKey() == newMeasureUnit) {
                        needToUpdate = false;
                    }
                }
                if (newMeasureUnit == product.getMainMeasureUnit()) {
                    needToUpdate = false;
                }
                if (needToUpdate && mainView != null) {
                    lastInputAmount = amount;
                    mainView.needMoreDataAboutProduct(product, newMeasureUnit);
                } else {
                    //save ingredient
                    Ingredient ingredient = new Ingredient(null,
                                                           product.getName(),
                                                           product,
                                                           recipe != null ? recipe.getId() : null,
                                                           newMeasureUnit,
                                                           amount,
                                                           isNeedToBuy ? ShopListStatus.NEED_TO_BUY : ShopListStatus.NONE);
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
            }
        } else {
            if (mainView != null) {
                mainView.setErrorToast(R.string.unknown_product_error);
            }
        }
    }

    public void saveIngredient(Product product, MeasureUnit newMeasureUnit) {
        saveIngredient(product, lastInputAmount, newMeasureUnit);
    }

    @Override
    public void addNewMeasureinfo(Product product, MeasureUnit unit, double amount) {
        if (amount > 1e-8) {
            product.getMeasureUnitToAmoutMap().put(unit, amount);
            DatabaseReference productRef = database.child(DatabaseConstants.DATABASE_PRODUCT_TABLE);
            productRef.child(product.getId()).child(DatabaseConstants.DATABASE_MEASURE_MAP_FIELD)
                    .setValue(product.getMeasureStringToAmoutMap());
            saveIngredient(product, unit);
        }
    }

    private void saveProduct(Product product, MeasureUnit newMeasureUnit) {
        DatabaseReference productRef = database.child(DatabaseConstants.DATABASE_PRODUCT_TABLE);
        //check if we have already had a product with the same name
        productRef.orderByChild(DatabaseConstants.DATABASE_NAME_FIELD).equalTo(product.getName())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getChildrenCount() > 0) {//we have the same product
                            for (DataSnapshot child : dataSnapshot.getChildren()) {
                                saveIngredient(Product.parseProductFromDB(child), newMeasureUnit);
                            }
                        } else {//we need to save new product
                            DatabaseReference productRef = database.child(DatabaseConstants.DATABASE_PRODUCT_TABLE);
                            productRef.push().setValue(product, (databaseError, reference) -> {
                                if (databaseError != null) {
                                    if (mainView != null) {
                                        mainView.setErrorToast(databaseError.getMessage());
                                    }
                                } else {
                                    saveIngredient(product, newMeasureUnit);
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
