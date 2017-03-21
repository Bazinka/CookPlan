package com.cookplan.models;

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

    public Ingredient() {
    }

    public Ingredient(String name, String productId, String recipeId, MeasureUnit measureUnit, Double amount) {
        this.name = name;
        this.productId = productId;
        this.recipeId = recipeId;
        this.measureUnit = measureUnit;
        this.amount = amount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
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

    public IngredientDBObject getIngredientDBObject() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        return new IngredientDBObject(auth.getCurrentUser().getUid(), productId, measureUnit, name, recipeId, amount);
    }

    public static Ingredient getIngredientFromDBObject(DataSnapshot itemSnapshot) {
        Ingredient.IngredientDBObject object = itemSnapshot.getValue(Ingredient.IngredientDBObject.class);
        Ingredient ingredient = new Ingredient(object.getName(), object.getProductId(),
                object.getRecipeId(), MeasureUnit.getMeasureUnitById(object.getMeasureUnitId()),
                object.getAmount());
        ingredient.setId(object.getId());
        return ingredient;
    }

    public static class IngredientDBObject {

        private String id;
        private String userId;
        private String name;
        private String productId;
        private String recipeId;
        private int measureUnitId;
        private double amount;

        public IngredientDBObject() {
        }

        public IngredientDBObject(String userId, String productId, MeasureUnit measureUnit, String name, String recipeId, double amount) {
            this.userId = userId;
            this.productId = productId;
            this.measureUnitId = measureUnit != null ? measureUnit.getId() : -1;
            this.name = name;
            this.recipeId = recipeId;
            this.amount = amount;
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
    }
}
