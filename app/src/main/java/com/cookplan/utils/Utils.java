package com.cookplan.utils;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.util.TypedValue;

import com.cookplan.models.MeasureUnit;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static com.cookplan.models.MeasureUnit.BOTTLE;
import static com.cookplan.models.MeasureUnit.CUP;
import static com.cookplan.models.MeasureUnit.GRAMM;
import static com.cookplan.models.MeasureUnit.KILOGRAMM;
import static com.cookplan.models.MeasureUnit.LITRE;
import static com.cookplan.models.MeasureUnit.MILILITRE;
import static com.cookplan.models.MeasureUnit.PACKAGE;
import static com.cookplan.models.MeasureUnit.TABLESPOON;
import static com.cookplan.models.MeasureUnit.TEASPOON;
import static com.cookplan.models.MeasureUnit.UNITS;

/**
 * Created by DariaEfimova on 16.03.17.
 */

public class Utils {

    public static void log(String tag, String textLog) {
        Log.d(tag, textLog);
    }

    /**
     * Converting dp to pixel
     */
    public static int dpToPx(Context context, int dp) {
        Resources r = context.getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }


    public static List<MeasureUnit> getVolumeUnitList() {
        MeasureUnit[] volumeUnitArray = new MeasureUnit[]{LITRE, MILILITRE, CUP, TEASPOON, TABLESPOON, PACKAGE, BOTTLE};
        return Arrays.asList(volumeUnitArray);
    }

    public static Map<MeasureUnit, Double> getLitreUnitMap() {
        Map<MeasureUnit, Double> litreUnitMap = new UnitsMapBuilder()
                .put(MILILITRE, 1000.)
                .put(CUP, 4.) // 250 мл.
                .put(TEASPOON, 202.)// 5 мл.
                .put(TABLESPOON, 67.)// 15 мл.
                .build();
        return litreUnitMap;
    }

    public static List<MeasureUnit> getWeightUnitList() {
        MeasureUnit[] weightUnitArray = new MeasureUnit[]{GRAMM, KILOGRAMM, CUP, TEASPOON, TABLESPOON, PACKAGE, UNITS};
        return Arrays.asList(weightUnitArray);
    }

    public static Map<MeasureUnit, Double> getKilogramUnitMap() {
        Map<MeasureUnit, Double> kilogramUnitMap = new UnitsMapBuilder()
                .put(GRAMM, 1000.)
                .put(CUP, 4.)
                .build();

        return kilogramUnitMap;
    }


    private static class UnitsMapBuilder {
        private final Map<MeasureUnit, Double> measureUnitToAmoutMap;

        public UnitsMapBuilder() {
            measureUnitToAmoutMap = new HashMap<>();
        }

        public UnitsMapBuilder(Map<MeasureUnit, Double> map) {
            measureUnitToAmoutMap = new HashMap<>(map);
        }

        public UnitsMapBuilder put(MeasureUnit unit, Double amount) {
            measureUnitToAmoutMap.put(unit, amount);
            return this;
        }

        public Map<MeasureUnit, Double> build() {
            return measureUnitToAmoutMap;
        }
    }

    private static class UnitsListBuilder {
        private final List<MeasureUnit> measureUnitList;

        public UnitsListBuilder() {
            measureUnitList = new LinkedList<>();
        }

        public UnitsListBuilder add(MeasureUnit unit) {
            measureUnitList.add(unit);
            return this;
        }

        public UnitsListBuilder addAll(MeasureUnit[] unit) {
            measureUnitList.addAll(Arrays.asList(unit));
            return this;
        }

        public List<MeasureUnit> build() {
            return measureUnitList;
        }
    }
}
