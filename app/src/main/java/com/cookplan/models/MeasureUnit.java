package com.cookplan.models;

import com.cookplan.R;
import com.cookplan.RApplication;

import java.io.Serializable;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by DariaEfimova on 20.03.17.
 */

public enum MeasureUnit implements Serializable {
    UNITS(0, R.string.unit_title, false),
    GRAMM(1, R.string.gramm_title, true),
    KILOGRAMM(2, R.string.kilogramm_title, false),
    LITRE(3, R.string.litre_title, false),
    MILILITRE(4, R.string.mililitre_title, true),
    CUP(5, R.string.cup_title, false),
    TEASPOON(6, R.string.teaspoon_title, false),
    TABLESPOON(7, R.string.tablespoon_title, true),
    BOTTLE(8, R.string.bottle_title, false),
    PACKAGE(9, R.string.package_title, false);

    private int id;
    private int nameRecourseId;
    private boolean isItIntValue;

    MeasureUnit(int id, int nameRecourseId, boolean isItIntValue) {
        this.id = id;
        this.nameRecourseId = nameRecourseId;
        this.isItIntValue = isItIntValue;
    }

    @Override
    public String toString() {
        return RApplication.Companion.getAppContext().getString(nameRecourseId);
    }

    public String toValueString(double value) {
        String valueString;
        if (value < 1e-8 && value > -1e-8) {//value==0.0
            return RApplication.Companion.getAppContext().getString(R.string.by_the_taste);
        }

        if (this == KILOGRAMM && value < 1.) {
            valueString = getIntOrDoubleValueString(value * 1000) + " " + GRAMM.toString();
        } else if (this == GRAMM && value > 1000.) {
            valueString = getIntOrDoubleValueString(value / 1000) + " " + KILOGRAMM.toString();
        } else if (this == LITRE && value < 1.) {
            valueString = getIntOrDoubleValueString(value * 1000) + " " + MILILITRE.toString();
        } else if (this == MILILITRE && value > 1000.) {
            valueString = getIntOrDoubleValueString(value / 1000) + " " + LITRE.toString();
        } else {
            valueString = getIntOrDoubleValueString(value) + " " + toString();
        }
        return valueString;
    }

    public String toStringForShopList(double value) {
        String valueString;
        if (value < 1e-8 && value > -1e-8) {//value==0.0
            return "";
        }

        if (this == KILOGRAMM && value < 1.) {
            valueString = getIntOrDoubleValueString(value * 1000) + " " + GRAMM.toString();
        } else if (this == GRAMM && value > 1000.) {
            valueString = getIntOrDoubleValueString(value / 1000) + " " + KILOGRAMM.toString();
        } else if (this == LITRE && value < 1.) {
            valueString = getIntOrDoubleValueString(value * 1000) + " " + MILILITRE.toString();
        } else if (this == MILILITRE && value > 1000.) {
            valueString = getIntOrDoubleValueString(value / 1000) + " " + LITRE.toString();
        } else {
            valueString = getIntOrDoubleValueString(value) + " " + toString();
        }
        return valueString;
    }

    private String getIntOrDoubleValueString(double value) {
        String valueString;
        if (isItIntValue(value)) {
            valueString = String.valueOf(Math.round(value));
        } else {
            if (value > 10.) {
                valueString = String.valueOf(Math.round(value));
            } else {
                valueString = String.format(Locale.getDefault(), "%.1f", value);
            }
        }
        return valueString;
    }

    public int getId() {
        return id;
    }

    public static double getMultiplier(MeasureUnit from, MeasureUnit to) {
        if (from == to) {
            return 1;
        }

        switch (from) {
            case UNITS: {
                switch (to) {
                    case PACKAGE:
                        return 1;
                    case BOTTLE:
                        return 1;
                    default:
                        return -1;
                }
            }
            case PACKAGE: {
                switch (to) {
                    case UNITS:
                        return 1;
                    case BOTTLE:
                        return 1;
                    default:
                        return -1;
                }

            }
            case BOTTLE: {
                switch (to) {
                    case UNITS:
                        return 1;
                    case PACKAGE:
                        return 1;
                    default:
                        return -1;
                }

            }
            case GRAMM: {
                switch (to) {
                    case GRAMM:
                        return 1;
                    case KILOGRAMM:
                        return 0.001;
                    case LITRE:
                        return 0.001;
                    case MILILITRE:
                        return 1;
                    case CUP:
                        return 0.004;
                    case TEASPOON:
                        return 0.2;
                    case TABLESPOON:
                        return 0.06;
                    default:
                        return -1;
                }
            }
            case KILOGRAMM: {
                switch (to) {
                    case KILOGRAMM:
                        return 1;
                    case GRAMM:
                        return 1000;
                    case LITRE:
                        return 1;
                    case MILILITRE:
                        return 1000;
                    case CUP:
                        return 4;
                    case TEASPOON:
                        return 200;
                    case TABLESPOON:
                        return 66.6;
                    default:
                        return -1;
                }
            }
            case LITRE: {
                switch (to) {
                    case LITRE:
                        return 1;
                    case GRAMM:
                        return 1000;
                    case KILOGRAMM:
                        return 1;
                    case MILILITRE:
                        return 1000;
                    case CUP:
                        return 4;
                    case TEASPOON:
                        return 200;
                    case TABLESPOON:
                        return 66.6;
                    default:
                        return -1;
                }
            }
            case MILILITRE: {
                switch (to) {
                    case MILILITRE:
                        return 1;
                    case KILOGRAMM:
                        return 0.001;
                    case LITRE:
                        return 0.001;
                    case GRAMM:
                        return 1;
                    case CUP:
                        return 0.004;
                    case TEASPOON:
                        return 0.2;
                    case TABLESPOON:
                        return 0.06;
                    default:
                        return -1;
                }
            }
            case CUP: {
                switch (to) {
                    case CUP:
                        return 1;
                    case KILOGRAMM:
                        return 0.25;
                    case LITRE:
                        return 0.25;
                    case GRAMM:
                        return 250;
                    case MILILITRE:
                        return 250;
                    case TEASPOON:
                        return 50;
                    case TABLESPOON:
                        return 15;
                    default:
                        return -1;
                }
            }
            case TEASPOON: {
                switch (to) {
                    case TEASPOON:
                        return 1;
                    case KILOGRAMM:
                        return 0.005;
                    case LITRE:
                        return 0.005;
                    case MILILITRE:
                        return 5;
                    case CUP:
                        return 0.02;
                    case GRAMM:
                        return 5;
                    case TABLESPOON:
                        return 16;
                    default:
                        return -1;
                }
            }
            case TABLESPOON: {
                switch (to) {
                    case TABLESPOON:
                        return 1;
                    case KILOGRAMM:
                        return 0.015;
                    case LITRE:
                        return 0.015;
                    case MILILITRE:
                        return 15;
                    case CUP:
                        return 0.06;
                    case GRAMM:
                        return 15;
                    case TEASPOON:
                        return 3;
                    default:
                        return -1;
                }
            }
            default:
                return -1;
        }
    }

    private boolean isItIntValue(double value) {
        if (!isItIntValue) {
            if ((value == Math.floor(value)) && !Double.isInfinite(value)) {
                return true;
            }
            return false;
        } else {
            return true;
        }
    }

    /*
    This method works only for russian language
     */
    public static MeasureUnit parseUnit(String unitString) {
        MeasureUnit unit = UNITS;

        //try to find UNIT
        String unitTitle = RApplication.Companion.getAppContext().getString(R.string.unit_title);
        unitTitle = unitTitle.replace(".", "");
        String regExString = unitTitle + "\\.*";
        Matcher matcher = Pattern.compile(regExString).matcher(unitString);
        if (matcher.find()) {
            unit = UNITS;
        }
        //try to find GRAMM
        String grammTitle = RApplication.Companion.getAppContext().getString(R.string.gramm_title);
        grammTitle = "(\\b" + RApplication.Companion.getAppContext().getString(R.string.short_gramm_title)
                + ".*\\b|\\b" + grammTitle + ")";
        matcher = Pattern.compile(grammTitle).matcher(unitString);
        if (matcher.find()) {
            unit = GRAMM;
        }

        //try to find KILOGRAMM
        String kilogrammTitle = RApplication.Companion.getAppContext().getString(R.string.kilogramm_title);
        kilogrammTitle = "(\\b" + kilogrammTitle + "*\\b)";
        matcher = Pattern.compile(kilogrammTitle).matcher(unitString);
        if (matcher.find()) {
            unit = KILOGRAMM;
        }

        //try to find LITRE
        String litreTitle = RApplication.Companion.getAppContext().getString(R.string.litre_title);
        litreTitle = "(\\b" + litreTitle + "*\\b)";
        matcher = Pattern.compile(litreTitle).matcher(unitString);
        if (matcher.find()) {
            unit = LITRE;
        }

        //try to find MILILITRE
        String mililitreTitle = RApplication.Companion.getAppContext().getString(R.string.mililitre_title);
        mililitreTitle = "(\\b" + mililitreTitle + "*\\b)";
        matcher = Pattern.compile(mililitreTitle).matcher(unitString);
        if (matcher.find()) {
            unit = MILILITRE;
        }

        //try to find CUP
        String cupTitle = RApplication.Companion.getAppContext().getString(R.string.cup_title);
        cupTitle = "(\\b" + cupTitle + "*\\b)";
        matcher = Pattern.compile(cupTitle).matcher(unitString);
        if (matcher.find()) {
            unit = CUP;
        }

        //try to find TEASPOON
        String teaspoonTitle = RApplication.Companion.getAppContext().getString(R.string.teaspoon_title);
        teaspoonTitle = "(\\b" + teaspoonTitle.replace(".", ".*") + "\\b)";
        matcher = Pattern.compile(teaspoonTitle).matcher(unitString);
        if (matcher.find()) {
            unit = TEASPOON;
        }

        //try to find TABLESPOON
        String tablespoonTitle = RApplication.Companion.getAppContext().getString(R.string.tablespoon_title);
        tablespoonTitle = "(\\b" + tablespoonTitle.replace(".", ".*") + "\\b)";
        matcher = Pattern.compile(tablespoonTitle).matcher(unitString);
        if (matcher.find()) {
            unit = TABLESPOON;
        }

        //try to find BOTTLE
        String bottleTitle = RApplication.Companion.getAppContext().getString(R.string.bottle_title);
        bottleTitle = "(\\b" + bottleTitle + ".*\\b)";
        matcher = Pattern.compile(bottleTitle).matcher(unitString);
        if (matcher.find()) {
            unit = BOTTLE;
        }

        //try to find PACKAGE
        String packageTitle = RApplication.Companion.getAppContext().getString(R.string.package_title);
        packageTitle = "(\\b" + packageTitle + "*\\b)";
        matcher = Pattern.compile(packageTitle).matcher(unitString);
        if (matcher.find()) {
            unit = PACKAGE;
        }
        return unit;
    }
}
