package com.cookplan.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.cookplan.RApplication;
import com.cookplan.utils.DatabaseConstants;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by DariaEfimova on 20.03.17.
 */

public class Product implements Parcelable {

    public String id;
    public String userId;
    public List<MeasureUnit> measureUnitList; //list of units in which product can be measured.
    public List<MeasureUnit> mainMeasureUnitList;
    public String rusName;
    public String engName;
    public int countUsing;
    public ProductCategory category;
    public List<String> companyIdList;

    public List<RatioMeasure> ratioMeasureList;

    private Product() {
    }

    public Product(ProductCategory category, String rusName, String engName,
                   List<MeasureUnit> mainMeasureUnitList,
                   List<MeasureUnit> measureUnitList,
                   Map<MeasureUnit, Double> unitToAmoutMap,
                   String userId) {
        this();
        this.category = category;
        this.rusName = rusName;
        this.engName = engName;
        this.mainMeasureUnitList = mainMeasureUnitList;
        this.measureUnitList = measureUnitList;
        this.userId = userId;
        countUsing = 0;
        fillRatioList(unitToAmoutMap);
        companyIdList = new ArrayList<>();
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

    public String toStringName() {
        if (RApplication.isCurrentLocaleRus()) {
            return rusName != null ? rusName : "";
        } else {
            return engName != null ? engName : "";
        }
    }

    public String getRusName() {
        return rusName;
    }

    public void setRusName(String rusName) {
        this.rusName = rusName;
    }

    public String getEngName() {
        return engName;
    }

    public void setEngName(String engName) {
        this.engName = engName;
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

    public List<String> getCompanyIdList() {
        return companyIdList;
    }

    public void addCompanyId(String companyId) {
        if (companyIdList != null) {
            companyIdList.add(companyId);
        } else {
            companyIdList = new ArrayList<>();
            companyIdList.add(companyId);
        }
    }

    public static Product parseProductFromDB(DataSnapshot dataSnapshot) {
        Product product = new Product();
        product.id = dataSnapshot.getKey();
        for (DataSnapshot child : dataSnapshot.getChildren()) {
            if (child.getKey().equals(DatabaseConstants.DATABASE_PRODUCT_RUS_NAME_FIELD)) {
                product.rusName = child.getValue().toString();
            }
            if (child.getKey().equals(DatabaseConstants.DATABASE_PRODUCT_ENG_NAME_FIELD)) {
                product.engName = child.getValue().toString();
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
            if (child.getKey().equals(DatabaseConstants.DATABASE_COMPANY_ID_LIST_FIELD)) {
                product.companyIdList = new ArrayList<>();
                for (DataSnapshot childUnit : child.getChildren()) {
                    if (childUnit.getValue() instanceof String) {
                        product.companyIdList.add(childUnit.getValue(String.class));
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

    // Parcelling part
    public Product(Parcel in) {
        id = in.readString();
        userId = in.readString();
        //        public List<MeasureUnit> measureUnitList = in.readSerializable();
        //        public List<MeasureUnit> mainMeasureUnitList;
        rusName = in.readString();
        engName = in.readString();
        countUsing = in.readInt();
        category = (ProductCategory) in.readSerializable();
        companyIdList = new ArrayList<>();
        in.readStringList(companyIdList);

        ratioMeasureList = new ArrayList<>();
        in.readTypedList(ratioMeasureList, RatioMeasure.CREATOR);

    }

    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(userId);
        //        public List<MeasureUnit> measureUnitList = in.readSerializable();
        //        public List<MeasureUnit> mainMeasureUnitList;
        dest.writeString(rusName);
        dest.writeString(engName);
        dest.writeInt(countUsing);
        dest.writeSerializable(category);
        if (companyIdList == null) {
            companyIdList = new ArrayList<>();
        }
        dest.writeStringList(companyIdList);

        if (ratioMeasureList == null) {
            ratioMeasureList = new ArrayList<>();
        }
        dest.writeTypedList(ratioMeasureList);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        public Product[] newArray(int size) {
            return new Product[size];
        }
    };
}
