package com.cookplan.models;

/**
 * Created by DariaEfimova on 06.04.17.
 */

/**
 * Class for saving the ratio between 2 measure units
 * <p>
 * rightUnit = ratio * leftUnit
 */

public class RatioMeasure {

    public Double ratio;
    public MeasureUnit rightUnit;
    public MeasureUnit leftUnit;

    public RatioMeasure() {
    }

    public RatioMeasure(MeasureUnit rightUnit, Double ratio, MeasureUnit leftUnit) {
        this();
        this.ratio = ratio;
        this.rightUnit = rightUnit;
        this.leftUnit = leftUnit;
    }

    public Double getRatio() {
        return ratio;
    }

    public void setRatio(Double ratio) {
        this.ratio = ratio;
    }

    public MeasureUnit getRightUnit() {
        return rightUnit;
    }

    public void setRightUnit(MeasureUnit rightUnit) {
        this.rightUnit = rightUnit;
    }

    public MeasureUnit getLeftUnit() {
        return leftUnit;
    }

    public void setLeftUnit(MeasureUnit leftUnit) {
        this.leftUnit = leftUnit;
    }

    public Double getMultiplier(MeasureUnit fromUnit, MeasureUnit toUnit) {
        if (fromUnit == rightUnit && toUnit == leftUnit) {
            return ratio;
        }
        if (fromUnit == leftUnit && toUnit == rightUnit) {
            return 1 / ratio;
        }
        return -1.;
    }
}
