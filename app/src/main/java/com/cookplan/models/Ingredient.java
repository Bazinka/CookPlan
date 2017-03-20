package com.cookplan.models;

import java.io.Serializable;

/**
 * Created by DariaEfimova on 18.03.17.
 */

public class Ingredient implements Serializable {

    private String id;
    private String userId;
    private Product product;
    private MeasureUnit measureUnit;
    private Double amount;

    public Ingredient() {
    }

    public Ingredient(String userId, Product product, MeasureUnit measureUnit, Double amount) {
        this.userId = userId;
        this.product = product;
        this.measureUnit = measureUnit;
        this.amount = amount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
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

    public IngredientDBObject getIngredientDatabaseObject() {
        return new IngredientDBObject(userId, product, measureUnit, amount);
    }

    public void fillIngredientFromDB(IngredientDBObject object, Product product) {
        id = object.getId();
        userId = object.getUserId();
        measureUnit = MeasureUnit.getMeasureUnitById(object.getMeasureUnitId());
        amount = object.getAmount();
        this.product = product;
    }

    public static class IngredientDBObject {

        private String id;
        private String userId;
        private String productId;
        private int measureUnitId;
        private double amount;

        public IngredientDBObject() {
        }

        public IngredientDBObject(String userId, Product product, MeasureUnit measureUnit, double amount) {
            this.userId = userId;
            this.productId = product != null ? product.getId() : null;
            this.measureUnitId = measureUnit != null ? measureUnit.getId() : -1;
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
    }
}
