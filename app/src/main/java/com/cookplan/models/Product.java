package com.cookplan.models;

import com.cookplan.utils.DatabaseConstants;
import com.google.firebase.database.DataSnapshot;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by DariaEfimova on 20.03.17.
 */

public class Product implements Serializable {

    public String id;
    public Map<String, Double> measureUnitToAmoutMap;//map from measureUnitList to amount
    public List<MeasureUnit> measureUnitList; //list of units in which product can be measured.
    public MeasureUnit mainMeasureUnit;
    public String name;
    public ProductCategory category;

    private Product() {
    }

    public Product(ProductCategory category, String name,
                   MeasureUnit measureUnit, List<MeasureUnit> measureUnitList) {
        this.category = category;
        measureUnitToAmoutMap = new HashMap<>();
        this.name = name;
        this.mainMeasureUnit = measureUnit;
        this.measureUnitList = measureUnitList;
    }

    public void fillTheMap(Map<MeasureUnit, Double> unitToAmoutMap) {
        if (unitToAmoutMap != null) {
            measureUnitToAmoutMap = new HashMap<>();
            for (Map.Entry<MeasureUnit, Double> entry : unitToAmoutMap.entrySet()) {
                measureUnitToAmoutMap.put(entry.getKey().name(), entry.getValue());
            }
        }
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setItemToMap(MeasureUnit unit, double amount) {
        getMeasureUnitToAmoutMap().put(unit, amount);
    }

    public Map<MeasureUnit, Double> getMeasureUnitToAmoutMap() {
        Map<MeasureUnit, Double> unitToAmoutMap = new HashMap<>();
        for (Map.Entry<String, Double> entry : measureUnitToAmoutMap.entrySet()) {
            unitToAmoutMap.put(MeasureUnit.valueOf(entry.getKey()), entry.getValue());
        }
        return unitToAmoutMap;
    }

    public Map<String, Double> getMeasureStringToAmoutMap() {
        return measureUnitToAmoutMap;
    }

    public MeasureUnit getMainMeasureUnit() {
        return mainMeasureUnit;
    }

    public ProductCategory getCategory() {
        return category;
    }

    public void setCategory(ProductCategory category) {
        this.category = category;
    }

    public static Product parseProductFromDB(DataSnapshot dataSnapshot) {
        Product product = new Product();
        product.id = dataSnapshot.getKey();
        for (DataSnapshot child : dataSnapshot.getChildren()) {
            if (child.getKey().equals(DatabaseConstants.DATABASE_NAME_FIELD)) {
                product.name = child.getValue().toString();
            }
            if (child.getKey().equals(DatabaseConstants.DATABASE_PRODUCT_CATEGORY_FIELD)) {
                product.category = ProductCategory.getProductCategoryByName((String) child.getValue());
            }
            if (child.getKey().equals(DatabaseConstants.DATABASE_MEASURE_MAP_FIELD)) {
                product.measureUnitToAmoutMap = new HashMap<>();
                for (DataSnapshot childUnit : child.getChildren()) {
                    double value;
                    if (childUnit.getValue() instanceof Long) {
                        value = ((Long) childUnit.getValue()).doubleValue();
                    } else {
                        value = (Double) childUnit.getValue();
                    }
                    product.measureUnitToAmoutMap.put(childUnit.getKey(), value);
                }
            }
            if (child.getKey().equals(DatabaseConstants.DATABASE_MAIN_MEASURE_UNIT_FIELD)) {
                product.mainMeasureUnit = MeasureUnit.valueOf((String) child.getValue());
            }
        }
        return product;
    }
}
