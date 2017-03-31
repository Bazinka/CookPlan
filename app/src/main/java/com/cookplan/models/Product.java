package com.cookplan.models;

import com.cookplan.utils.DatabaseConstants;
import com.google.firebase.database.DataSnapshot;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by DariaEfimova on 20.03.17.
 */

public class Product implements Serializable {

    public String id;
    public Map<MeasureUnit, Double> measureUnitToAmoutMap;//amount is how many measureUnit contains in 1 mainMeasureUnit
    public MeasureUnit mainMeasureUnit;
    public String name;
    public ProductCategory category;

    private Product() {
    }

    //    public Product(List<MeasureUnit> measureUnitList, String name, ProductCategory category) {
    //        this.category = category;
    //        this.measureUnitList = measureUnitList;
    //        this.name = name;
    //    }

    public Product(MeasureUnit measureUnit, Double amountMesUnitInTheMain, String name, ProductCategory category) {
        this.category = category;
        if (measureUnitToAmoutMap == null) {
            measureUnitToAmoutMap = new HashMap<>();
            mainMeasureUnit = measureUnit;
        } else {
            measureUnitToAmoutMap.put(measureUnit, amountMesUnitInTheMain);
        }
        this.name = name;
    }

    public Product(MeasureUnit measureUnit, String name, ProductCategory category) {
        this(measureUnit, 0., name, category);
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

    //    public List<MeasureUnit> getMeasureUnitList() {
    //        return new ArrayList<>(measureUnitToAmoutMap.keySet());
    //    }

    public Map<MeasureUnit, Double> getMeasureUnitToAmoutMap() {
        if (measureUnitToAmoutMap == null) {
            measureUnitToAmoutMap = new HashMap<>();
        }
        return measureUnitToAmoutMap;
    }

    public Map<String, Double> getMeasureStringToAmoutMap() {
        Map<String, Double> measureStringToAmoutMap = new HashMap<>();
        for (Map.Entry<MeasureUnit, Double> entry : measureUnitToAmoutMap.entrySet()) {
            measureStringToAmoutMap.put(entry.getKey().name(), entry.getValue());
        }
        return measureStringToAmoutMap;
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
                    MeasureUnit measureUnit = MeasureUnit.getMeasureUnitByName(childUnit.getKey());
                    if (measureUnit != null) {
                        double value;
                        if (childUnit.getValue() instanceof Long) {
                            value = ((Long) childUnit.getValue()).doubleValue();
                        } else {
                            value = (Double) childUnit.getValue();
                        }
                        product.measureUnitToAmoutMap.put(measureUnit, value);
                    }

                }
            }
            if (child.getKey().equals(DatabaseConstants.DATABASE_MAIN_MEASURE_UNIT_FIELD)) {
                product.mainMeasureUnit = MeasureUnit.getMeasureUnitByName((String) child.getValue());
            }
        }
        return product;
    }
}
