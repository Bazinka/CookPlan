package com.cookplan.models;

import com.cookplan.utils.DatabaseConstants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;

import java.io.Serializable;

/**
 * Created by DariaEfimova on 18.03.17.
 */

public class Ingredient implements Serializable {

    public String id;
    public String userId;
    public String name;
    public String productId;
    public String recipeId;
    public MeasureUnit measureUnit;
    public Double amount;
    public String amountString;
    public ShopListStatus shopListStatus;

    public Ingredient() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        userId = auth.getCurrentUser().getUid();
    }

    public Ingredient(String id, String name, String productId, String recipeId, MeasureUnit measureUnit, Double amount, ShopListStatus shopListStatus) {
        this();
        this.id = id;
        this.name = name;
        this.productId = productId;
        this.recipeId = recipeId;
        this.measureUnit = measureUnit;
        this.amount = amount;
        this.shopListStatus = shopListStatus;
    }

    public Ingredient(String name, String amountString, ShopListStatus shopListStatus) {
        this();
        this.name = name;
        this.amountString = amountString;
        this.shopListStatus = shopListStatus;
    }

    public String getId() {
        return id;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(String recipeId) {
        this.recipeId = recipeId;
    }

    public MeasureUnit getMeasureUnit() {
        return measureUnit;
    }

    public void setMeasureUnit(MeasureUnit measureUnit) {
        this.measureUnit = measureUnit;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static Ingredient getIngredientFromDBObject(DataSnapshot itemSnapshot) {
        Ingredient ingredient = new Ingredient();
        ingredient.id = itemSnapshot.getKey();
        for (DataSnapshot child : itemSnapshot.getChildren()) {
            if (child.getKey().equals(DatabaseConstants.DATABASE_NAME_FIELD)) {
                ingredient.name = child.getValue().toString();
            }
            if (child.getKey().equals(DatabaseConstants.DATABASE_USER_ID_FIELD)) {
                ingredient.userId = child.getValue().toString();
            }
            if (child.getKey().equals(DatabaseConstants.DATABASE_PRODUCT_ID_FIELD)) {
                ingredient.productId = child.getValue().toString();
            }
            if (child.getKey().equals(DatabaseConstants.DATABASE_INRGEDIENT_RECIPE_ID_FIELD)) {
                ingredient.recipeId = child.getValue().toString();
            }
            if (child.getKey().equals(DatabaseConstants.DATABASE_MEASURE_UNIT_FIELD)) {
                ingredient.measureUnit = MeasureUnit.getMeasureUnitByName((String) child.getValue());
            }
            if (child.getKey().equals(DatabaseConstants.DATABASE_AMOUNT_FIELD)) {
                if (child.getValue() instanceof Double) {
                    ingredient.amount = (Double) child.getValue();
                } else {
                    ingredient.amount = ((Long) child.getValue()).doubleValue();
                }
            }
            if (child.getKey().equals(DatabaseConstants.DATABASE_SHOP_LIST_STATUS_FIELD)) {
                ingredient.shopListStatus = ShopListStatus.getShopListStatusName((String) child.getValue());
            }
        }
        return ingredient;
    }

    public ShopListStatus getShopListStatus() {
        return shopListStatus;
    }

    public void setShopListStatus(ShopListStatus shopListStatus) {
        this.shopListStatus = shopListStatus;
    }

    public String getAmountString() {
        return amountString;
    }
}
