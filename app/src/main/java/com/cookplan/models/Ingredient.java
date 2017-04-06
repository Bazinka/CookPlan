package com.cookplan.models;

import com.cookplan.utils.DatabaseConstants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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
    public List<Double> shopAmountList;
    public List<MeasureUnit> shopMeasureList;
    public String amountString;
    public ShopListStatus shopListStatus;
    public ProductCategory category;

    public Ingredient() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        userId = auth.getCurrentUser().getUid();
    }

    public Ingredient(String id, String name, Product product, String recipeId,
                      MeasureUnit localMeasureUnit, Double mainAmount,
                      ShopListStatus shopListStatus) {
        this();
        this.id = id;
        this.name = name;
        this.recipeId = recipeId;
        this.mainMeasureUnit = localMeasureUnit;
        this.mainAmount = mainAmount;
        this.shopListStatus = shopListStatus;
        if (product != null) {
            this.category = product.getCategory();
            this.productId = product.getId();
            shopAmountList = new ArrayList<>();
            shopMeasureList = new ArrayList<>();
            double shopListAmount = -1.;
            for (MeasureUnit unit : product.getMainMeasureUnitList()) {
                double multiplier = MeasureUnit.getMultiplier(mainMeasureUnit, unit);
                if (product.getRatioMeasureList() != null) {
                    for (RatioMeasure ratio : product.getRatioMeasureList()) {
                        if (ratio.getMultiplier(mainMeasureUnit, unit) > 1e-8) {
                            multiplier = ratio.getMultiplier(mainMeasureUnit, unit);
                        }
                    }
                }
                if (multiplier > 1e-8) {
                    shopListAmount = multiplier * mainAmount;
                }
                shopAmountList.add(shopListAmount);
                shopMeasureList.add(unit);
            }
        }
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

    public List<Double> getShopAmountList() {
        return shopAmountList;
    }

    public List<MeasureUnit> getShopMeasureList() {
        return shopMeasureList;
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
            if (child.getKey().equals(DatabaseConstants.DATABASE_MAIN_AMOUNT_FIELD)) {
                if (child.getValue() instanceof Double) {
                    ingredient.mainAmount = (Double) child.getValue();
                } else {
                    ingredient.mainAmount = ((Long) child.getValue()).doubleValue();
                }
            }
            if (child.getKey().equals(DatabaseConstants.DATABASE_SHOP_AMOUNT_LIST_FIELD)) {
                ingredient.shopAmountList = new ArrayList<>();
                for (DataSnapshot childUnit : child.getChildren()) {
                    double value;
                    if (childUnit.getValue() instanceof Long) {
                        value = ((Long) childUnit.getValue()).doubleValue();
                    } else {
                        value = (Double) childUnit.getValue();
                    }
                    ingredient.shopAmountList.add(value);
                }
            }
            if (child.getKey().equals(DatabaseConstants.DATABASE_SHOP_MEASURE_LIST_FIELD)) {
                ingredient.shopMeasureList = new ArrayList<>();
                for (DataSnapshot childUnit : child.getChildren()) {
                    if (childUnit.getValue() instanceof String) {
                        ingredient.shopMeasureList.add(MeasureUnit.valueOf((String) childUnit.getValue()));
                    }
                }
            }
            if (child.getKey().equals(DatabaseConstants.DATABASE_PRODUCT_CATEGORY_FIELD)) {
                ingredient.category = ProductCategory.getProductCategoryByName((String) child.getValue());
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

    public ProductCategory getCategory() {
        return category;
    }
}
