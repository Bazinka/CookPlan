package com.cookplan.models;

import com.cookplan.R;
import com.cookplan.RApplication;

/**
 * Created by DariaEfimova on 20.03.17.
 */

public enum MeasureUnit {
    UNITS(0, R.string.unit_title),
    GRAMM(1, R.string.gramm_title),
    KILOGRAMM(2, R.string.kilogramm_title),
    LITRE(3, R.string.litre_title),
    MILILITRE(4, R.string.mililitre_title),
    CUP(5, R.string.cup_title),
    TEASPOON(6, R.string.teaspoon_title),
    TABLESPOON(7, R.string.tablespoon_title);

    private int id;
    private int nameRecourseId;

    MeasureUnit(int id, int nameRecourseId) {
        this.id = id;
        this.nameRecourseId = nameRecourseId;
    }

    @Override
    public String toString() {
        return RApplication.getAppContext().getString(nameRecourseId);
    }

    public int getId() {
        return id;
    }

    public static MeasureUnit getMeasureUnitById(int id) {
        switch (id) {
            case 0:
                return UNITS;
            case 1:
                return GRAMM;
            case 2:
                return KILOGRAMM;
            case 3:
                return LITRE;
            case 4:
                return MILILITRE;
            case 5:
                return CUP;
            case 6:
                return TEASPOON;
            case 7:
                return TABLESPOON;
            default:
                return null;
        }
    }
}
