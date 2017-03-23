package com.cookplan.models;

import com.cookplan.utils.DatabaseConstants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;

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
    private boolean isNeedToBuy;

    public Ingredient() {
    }

    public Ingredient(String id, String name, String productId, String recipeId, MeasureUnit measureUnit, Double amount, boolean isNeedToBuy) {
        this.id = id;
        this.name = name;
        this.productId = productId;
        this.recipeId = recipeId;
        this.measureUnit = measureUnit;
        this.amount = amount;
        this.isNeedToBuy = isNeedToBuy;
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
        return new IngredientDBObject(auth.getCurrentUser().getUid(), productId, measureUnit, name, recipeId, amount, isNeedToBuy);
    }

    public static Ingredient getIngredientFromDBObject(DataSnapshot itemSnapshot) {
        Ingredient.IngredientDBObject object = IngredientDBObject.parseIngredientDBObject(itemSnapshot);
        Ingredient ingredient = new Ingredient(itemSnapshot.getKey(), object.getName(), object.getProductId(),
                object.getRecipeId(), MeasureUnit.getMeasureUnitById(object.getMeasureUnitId()),
                object.getAmount(), object.isNeedToBuy());
        return ingredient;
    }

    public void setNeedToBuy(boolean needToBuy) {
        isNeedToBuy = needToBuy;
    }

    public boolean isNeedToBuy() {
        return isNeedToBuy;
    }

    public static class IngredientDBObject {

        private String id;
        private String userId;
        private String name;
        private String productId;
        private String recipeId;
        private int measureUnitId;
        private double amount;
        private boolean isNeedToBuy;

        public IngredientDBObject() {
        }

        public IngredientDBObject(String userId, String productId, MeasureUnit measureUnit, String name, String recipeId, double amount, boolean isNeedToBuy) {
            this.userId = userId;
            this.productId = productId;
            this.measureUnitId = measureUnit != null ? measureUnit.getId() : -1;
            this.name = name;
            this.recipeId = recipeId;
            this.amount = amount;
            this.isNeedToBuy = isNeedToBuy;
        }

        public boolean isNeedToBuy() {
            return isNeedToBuy;
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
                if (child.getKey().equals(DatabaseConstants.DATABASE_INRGEDIENT_PRODUCT_ID_FIELD)) {
                    object.productId = child.getValue().toString();
                }
                if (child.getKey().equals(DatabaseConstants.DATABASE_INRGEDIENT_RECIPE_ID_FIELD)) {
                    object.recipeId = child.getValue().toString();
                }
                if (child.getKey().equals(DatabaseConstants.DATABASE_INRGEDIENT_MEASURE_UNIT_ID_FIELD)) {
                    object.measureUnitId = ((Long) child.getValue()).intValue();
                }
                if (child.getKey().equals(DatabaseConstants.DATABASE_INRGEDIENT_AMOUNT_FIELD)) {
                    object.amount = ((Long) child.getValue()).doubleValue();
                }
                if (child.getKey().equals(DatabaseConstants.DATABASE_INRGEDIENT_IS_NEEED_TO_BUY_FIELD)) {
                    object.isNeedToBuy = (boolean) child.getValue();
                }
            }
            return object;
        }
    }
}
