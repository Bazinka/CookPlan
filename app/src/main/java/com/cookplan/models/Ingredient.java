package com.cookplan.models;

import com.cookplan.utils.DatabaseConstants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.PropertyName;

import java.io.Serializable;

/**
 * Created by DariaEfimova on 18.03.17.
 */

public class Ingredient implements Serializable {

    private String id;
    private String name;
    private String productId;
    private String recipeId;
    private MeasureUnit measureUnit;
    private Double amount;
    private String amountString;
    private ShopListStatus shopListStatus;

    public Ingredient() {
    }

    public Ingredient(String id, String name, String productId, String recipeId, MeasureUnit measureUnit, Double amount, ShopListStatus shopListStatus) {
        this.id = id;
        this.name = name;
        this.productId = productId;
        this.recipeId = recipeId;
        this.measureUnit = measureUnit;
        this.amount = amount;
        this.shopListStatus = shopListStatus;
    }

    public Ingredient(String name, String amountString, ShopListStatus shopListStatus) {
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

    public IngredientDBObject getIngredientDBObject() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        return new IngredientDBObject(auth.getCurrentUser().getUid(), productId, measureUnit, name,
                recipeId, amount, shopListStatus);
    }

    public static Ingredient getIngredientFromDBObject(DataSnapshot itemSnapshot) {
        Ingredient.IngredientDBObject object = IngredientDBObject.parseIngredientDBObject(itemSnapshot);
        Ingredient ingredient = new Ingredient(itemSnapshot.getKey(), object.getName(),
                object.getProductId(), object.getRecipeId(),
                MeasureUnit.getMeasureUnitById(object.getMeasureUnitId()), object.getAmount(),
                ShopListStatus.getShopListStatusId(object.getShopListStatusId()));
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

    public static class IngredientDBObject {

        public String id;

        @PropertyName(DatabaseConstants.DATABASE_USER_ID_FIELD)
        public String userId;

        @PropertyName(DatabaseConstants.DATABASE_NAME_FIELD)
        public String name;

        @PropertyName(DatabaseConstants.DATABASE_PRODUCT_ID_FIELD)
        public String productId;

        @PropertyName(DatabaseConstants.DATABASE_RECIPE_ID_FIELD)
        public String recipeId;

        @PropertyName(DatabaseConstants.DATABASE_MEASURE_UNIT_ID_FIELD)
        public int measureUnitId;

        @PropertyName(DatabaseConstants.DATABASE_AMOUNT_FIELD)
        public double amount;

        @PropertyName(DatabaseConstants.DATABASE_SHOP_LIST_STATUS_FIELD)
        public int shopListStatusId;

        public IngredientDBObject() {
        }

        public IngredientDBObject(String userId, String productId, MeasureUnit measureUnit, String name, String recipeId, double amount, ShopListStatus shopListStatus) {
            this.userId = userId;
            this.productId = productId;
            this.measureUnitId = measureUnit != null ? measureUnit.getId() : -1;
            this.name = name;
            this.recipeId = recipeId;
            this.amount = amount;
            this.shopListStatusId = shopListStatus != null ? shopListStatus.getId() : -1;
        }

        public int getShopListStatusId() {
            return shopListStatusId;
        }

        public String getId() {
            return id;
        }

        public String getUserId() {
            return userId;
        }

        public String getProductId() {
            return productId;
        }

        public int getMeasureUnitId() {
            return measureUnitId;
        }

        public double getAmount() {
            return amount;
        }

        public String getName() {
            return name;
        }

        public String getRecipeId() {
            return recipeId;
        }

        public static IngredientDBObject parseIngredientDBObject(DataSnapshot dataSnapshot) {
            IngredientDBObject object = new IngredientDBObject();
            object.id = dataSnapshot.getKey();
            for (DataSnapshot child : dataSnapshot.getChildren()) {
                if (child.getKey().equals(DatabaseConstants.DATABASE_NAME_FIELD)) {
                    object.name = child.getValue().toString();
                }
                if (child.getKey().equals(DatabaseConstants.DATABASE_USER_ID_FIELD)) {
                    object.userId = child.getValue().toString();
                }
                if (child.getKey().equals(DatabaseConstants.DATABASE_PRODUCT_ID_FIELD)) {
                    object.productId = child.getValue().toString();
                }
                if (child.getKey().equals(DatabaseConstants.DATABASE_INRGEDIENT_RECIPE_ID_FIELD)) {
                    object.recipeId = child.getValue().toString();
                }
                if (child.getKey().equals(DatabaseConstants.DATABASE_MEASURE_UNIT_ID_FIELD)) {
                    object.measureUnitId = ((Long) child.getValue()).intValue();
                }
                if (child.getKey().equals(DatabaseConstants.DATABASE_AMOUNT_FIELD)) {
                    if (child.getValue() instanceof Double) {
                        object.amount = (Double) child.getValue();
                    } else {
                        object.amount = ((Long) child.getValue()).doubleValue();
                    }
                }
                if (child.getKey().equals(DatabaseConstants.DATABASE_SHOP_LIST_STATUS_FIELD)) {
                    object.shopListStatusId = ((Long) child.getValue()).intValue();
                }
            }
            return object;
        }
    }
}
