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

    private String id;
    private List<MeasureUnit> measureUnitList;
    private String name;
    private ProductCategory category;

    private Product() {
    }

    public Product(List<MeasureUnit> measureUnitList, String name, ProductCategory category) {
        this.category = category;
        this.measureUnitList = measureUnitList;
        this.name = name;
    }

    public Product(MeasureUnit measureUnit, String name, ProductCategory category) {
        this.category = category;
        this.measureUnitList = new ArrayList<>();
        measureUnitList.add(measureUnit);
        this.name = name;
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

    public List<MeasureUnit> getMeasureUnitList() {
        return measureUnitList;
    }

    public void setMeasureUnitList(List<MeasureUnit> measureUnitList) {
        this.measureUnitList = measureUnitList;
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
            if (child.getKey().equals(DatabaseConstants.DATABASE_MEASURE_LIST_FIELD)) {
                product.measureUnitList = new ArrayList<>();
                if (child.getValue() instanceof List) {
                    List<String> measName = (List<String>) child.getValue();
                    if (!measName.isEmpty()) {
                        MeasureUnit measureUnit = MeasureUnit.getMeasureUnitByName(measName.get(0));
                        if (measureUnit != null) {
                            product.measureUnitList.add(measureUnit);
                        }
                    }
                } else if (child.getValue() instanceof Map) {
                    Map<Long, String> measDBMap = (Map<Long, String>) child.getValue();
                    for (Map.Entry<Long, String> mes : measDBMap.entrySet()) {
                        MeasureUnit measureUnit = MeasureUnit.getMeasureUnitByName(mes.getValue());
                        if (measureUnit != null) {
                            product.measureUnitList.add(measureUnit);
                        }
                    }
                }
            }
        }
        return product;
    }
}
