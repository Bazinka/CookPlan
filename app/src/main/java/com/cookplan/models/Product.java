package com.cookplan.models;

import com.cookplan.utils.DatabaseConstants;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.PropertyName;

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

    public Product() {
    }

    public Product(List<MeasureUnit> measureUnitList, String name) {
        this.measureUnitList = measureUnitList;
        this.name = name;
    }

    public Product(MeasureUnit measureUnit, String name) {
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

    public ProductDBObject getProductDBObject() {
        return new ProductDBObject(measureUnitList, name);
    }

    public static Product getProductFromDB(ProductDBObject object) {
        Product product = null;
        if (object != null) {
            product = new Product();
            product.id = object.getId();
            product.name = object.getName();

            if (object.getMeasureUnitIdList() != null && object.getMeasureUnitIdList().size() > 0) {
                product.measureUnitList = new ArrayList<>();
                for (Integer id : object.getMeasureUnitIdList()) {
                    MeasureUnit measureUnit = id != null ? MeasureUnit.getMeasureUnitById(id) : null;
                    if (measureUnit != null) {
                        product.measureUnitList.add(measureUnit);
                    }
                }
            }
        }
        return product;
    }

    public static class ProductDBObject {


        @PropertyName(DatabaseConstants.DATABASE_ID_FIELD)
        public String id;

        @PropertyName(DatabaseConstants.DATABASE_MEASURE_LIST_FIELD)
        public List<Integer> measureUnitIdList;

        @PropertyName(DatabaseConstants.DATABASE_NAME_FIELD)
        public String name;

        public ProductDBObject() {
        }

        public ProductDBObject(List<MeasureUnit> measureUnitList, String name) {
            if (measureUnitList != null && measureUnitList.size() > 0) {
                this.measureUnitIdList = new ArrayList<>();
                for (MeasureUnit unit : measureUnitList) {
                    this.measureUnitIdList.add(unit.getId());
                }
            }
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public List<Integer> getMeasureUnitIdList() {
            return measureUnitIdList;
        }

        public String getName() {
            return name;
        }

        public static ProductDBObject parseProductDBObject(DataSnapshot dataSnapshot) {
            ProductDBObject object = new ProductDBObject();
            object.id = dataSnapshot.getKey();
            for (DataSnapshot child : dataSnapshot.getChildren()) {
                if (child.getKey().equals(DatabaseConstants.DATABASE_NAME_FIELD)) {
                    object.name = child.getValue().toString();
                }
                if (child.getKey().equals(DatabaseConstants.DATABASE_MEASURE_LIST_FIELD)) {
                    if (child.getValue() instanceof List) {
                        List<Long> measDBList = (ArrayList<Long>) child.getValue();
                        object.measureUnitIdList = new ArrayList<>();
                        for (Long mes : measDBList) {
                            object.measureUnitIdList.add(mes.intValue());
                        }
                    } else if (child.getValue() instanceof Map) {
                        Map<Long, Long> measDBMap = (Map<Long, Long>) child.getValue();
                        object.measureUnitIdList = new ArrayList<>();
                        for (Map.Entry<Long, Long> mes : measDBMap.entrySet()) {
                            object.measureUnitIdList.add(mes.getValue().intValue());
                        }
                    }
                }
            }
            return object;
        }
    }
}
