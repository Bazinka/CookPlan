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
    public MeasureUnit mainMeasureUnit;
    public Double mainAmount;
    public MeasureUnit shopListMeasureUnit;
    public Double shopListAmount;
    public String amountString;
    public ShopListStatus shopListStatus;

    public Ingredient() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        userId = auth.getCurrentUser().getUid();
    }

    public Ingredient(String id, String name, Product product, String recipeId,
                      MeasureUnit localMeasureUnit, Double mainAmount, ShopListStatus shopListStatus) {
        this();
        this.id = id;
        this.name = name;
        if (product != null) {
            this.productId = product.getId();
            shopListMeasureUnit = product.getMainMeasureUnit();
        }
        this.recipeId = recipeId;
        this.mainMeasureUnit = localMeasureUnit;
        this.mainAmount = mainAmount;
        shopListAmount = -1.;
        if (shopListMeasureUnit != null) {
            double multiplier = MeasureUnit.getMultiplier(localMeasureUnit, shopListMeasureUnit);
            if (product != null &&
                    product.getMeasureUnitToAmoutMap() != null &&
                    product.getMeasureUnitToAmoutMap().containsKey(localMeasureUnit)) {
                multiplier = 1 / product.getMeasureUnitToAmoutMap().get(localMeasureUnit);
            }
            if (multiplier > 1e-8) {
                shopListAmount = multiplier * mainAmount;
            }
        }
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

    public MeasureUnit getMainMeasureUnit() {
        return mainMeasureUnit;
    }

    public Double getMainAmount() {
        return mainAmount;
    }

    public MeasureUnit getShopListMeasureUnit() {
        return shopListMeasureUnit;
    }

    public Double getShopListAmount() {
        return shopListAmount;
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
            if (child.getKey().equals(DatabaseConstants.DATABASE_MAIN_MEASURE_UNIT_FIELD)) {
                ingredient.mainMeasureUnit = MeasureUnit.valueOf((String) child.getValue());
            }
            if (child.getKey().equals(DatabaseConstants.DATABASE_SHOP_LIST_MEASURE_UNIT_FIELD)) {
                ingredient.shopListMeasureUnit = MeasureUnit.valueOf((String) child.getValue());
            }
            if (child.getKey().equals(DatabaseConstants.DATABASE_MAIN_AMOUNT_FIELD)) {
                if (child.getValue() instanceof Double) {
                    ingredient.mainAmount = (Double) child.getValue();
                } else {
                    ingredient.mainAmount = ((Long) child.getValue()).doubleValue();
                }
            }
            if (child.getKey().equals(DatabaseConstants.DATABASE_SHOP_LIST_AMOUNT_FIELD)) {
                if (child.getValue() instanceof Double) {
                    ingredient.shopListAmount = (Double) child.getValue();
                } else {
                    ingredient.shopListAmount = ((Long) child.getValue()).doubleValue();
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
