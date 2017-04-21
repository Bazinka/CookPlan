package com.cookplan.models;

import com.cookplan.utils.DatabaseConstants;
import com.google.firebase.database.DataSnapshot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by DariaEfimova on 20.03.17.
 */

public class Product implements Serializable {

    public String id;
    public String userId;
    public List<MeasureUnit> measureUnitList; //list of units in which product can be measured.
    public List<MeasureUnit> mainMeasureUnitList;
    public String name;
    public int countUsing;
    public ProductCategory category;

    public List<RatioMeasure> ratioMeasureList;

    private Product() {
    }

    public Product(ProductCategory category, String name,
                   List<MeasureUnit> mainMeasureUnitList,
                   List<MeasureUnit> measureUnitList,
                   Map<MeasureUnit, Double> unitToAmoutMap,
                   String userId) {
        this();
        this.category = category;
        this.name = name;
        this.mainMeasureUnitList = mainMeasureUnitList;
        this.measureUnitList = measureUnitList;
        this.userId = userId;
        countUsing = 0;
        fillRatioList(unitToAmoutMap);
    }

    private void fillRatioList(Map<MeasureUnit, Double> unitToAmoutMap) {
        if (unitToAmoutMap != null && !mainMeasureUnitList.isEmpty()) {
            ratioMeasureList = new ArrayList<>();
            for (Map.Entry<MeasureUnit, Double> entry : unitToAmoutMap.entrySet()) {
                ratioMeasureList.add(new RatioMeasure(mainMeasureUnitList.get(0),
                                                      entry.getValue(),
                                                      entry.getKey()));
                if (mainMeasureUnitList.get(0) == MeasureUnit.KILOGRAMM) {
                    ratioMeasureList.add(new RatioMeasure(MeasureUnit.GRAMM,
                                                          entry.getValue() / 1000,
                                                          entry.getKey()));
                }
                if (mainMeasureUnitList.get(0) == MeasureUnit.GRAMM) {
                    ratioMeasureList.add(new RatioMeasure(MeasureUnit.KILOGRAMM,
                                                          1000 * entry.getValue(),
                                                          entry.getKey()));
                }
                if (mainMeasureUnitList.get(0) == MeasureUnit.LITRE) {
                    ratioMeasureList.add(new RatioMeasure(MeasureUnit.MILILITRE,
                                                          entry.getValue() / 1000,
                                                          entry.getKey()));
                }
                if (mainMeasureUnitList.get(0) == MeasureUnit.MILILITRE) {
                    ratioMeasureList.add(new RatioMeasure(MeasureUnit.LITRE,
                                                          1000 * entry.getValue(),
                                                          entry.getKey()));
                }
            }
        }
    }

    public int increasingCount() {
        countUsing = countUsing + 1;
        return countUsing;
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

    public List<MeasureUnit> getMainMeasureUnitList() {
        return mainMeasureUnitList;
    }

    public ProductCategory getCategory() {
        return category;
    }

    public void setCategory(ProductCategory category) {
        this.category = category;
    }

    public List<MeasureUnit> getMeasureUnitList() {
        return measureUnitList;
    }

    public List<RatioMeasure> getRatioMeasureList() {
        return ratioMeasureList;
    }

    public static Product parseProductFromDB(DataSnapshot dataSnapshot) {
        Product product = new Product();
        product.id = dataSnapshot.getKey();
        for (DataSnapshot child : dataSnapshot.getChildren()) {
            if (child.getKey().equals(DatabaseConstants.DATABASE_NAME_FIELD)) {
                product.name = child.getValue().toString();
            }
            if (child.getKey().equals(DatabaseConstants.DATABASE_USER_ID_FIELD)) {
                product.userId = child.getValue().toString();
            }
            if (child.getKey().equals(DatabaseConstants.DATABASE_PRODUCT_CATEGORY_FIELD)) {
                product.category = ProductCategory.getProductCategoryByName((String) child.getValue());
            }
            if (child.getKey().equals(DatabaseConstants.DATABASE_RATIO_MEASURE_LIST_FIELD)) {
                product.ratioMeasureList = new ArrayList<>();
                for (DataSnapshot childUnit : child.getChildren()) {
                    if (childUnit.getValue() instanceof Map) {
                        try {
                            product.ratioMeasureList.add(childUnit.getValue(RatioMeasure.class));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            if (child.getKey().equals(DatabaseConstants.DATABASE_MAIN_MEASURE_UNIT_LIST_FIELD)) {
                product.mainMeasureUnitList = new ArrayList<>();
                for (DataSnapshot childUnit : child.getChildren()) {
                    if (childUnit.getValue() instanceof String) {
                        product.mainMeasureUnitList.add(MeasureUnit.valueOf((String) childUnit.getValue()));
                    }
                }
            }
            if (child.getKey().equals(DatabaseConstants.DATABASE_MEASURE_UNIT_LIST_FIELD)) {
                product.measureUnitList = new ArrayList<>();
                for (DataSnapshot childUnit : child.getChildren()) {
                    if (childUnit.getValue() instanceof String) {
                        product.measureUnitList.add(MeasureUnit.valueOf((String) childUnit.getValue()));
                    }
                }
            }
            if (child.getKey().equals(DatabaseConstants.DATABASE_PRODUCT_COUNT_USING_FIELD)) {
                product.countUsing = ((Long) child.getValue()).intValue();
            }
        }
        return product;
    }

    public String getUserId() {
        return userId;
    }
}
