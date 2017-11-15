package com.cookplan.models

/**
 * Created by DariaEfimova on 06.04.17.
 */

import android.os.Parcel
import android.os.Parcelable

/**
 * Class for saving the ratio between 2 measure units
 *
 *
 * rightUnit = ratio * leftUnit
 */

data class RatioMeasure(var ratio: Double = 0.toDouble(),
                        var rightUnit: MeasureUnit,
                        var leftUnit: MeasureUnit) : Parcelable {
    constructor() : this(1.toDouble(), MeasureUnit.UNITS, MeasureUnit.UNITS)

    fun getMultiplier(fromUnit: MeasureUnit, toUnit: MeasureUnit): Double {
        if (fromUnit === rightUnit && toUnit === leftUnit) {
            return ratio
        }
        return if (fromUnit === leftUnit && toUnit === rightUnit) {
            1 / ratio
        } else -1.0
    }

    constructor(source: Parcel) : this(
            source.readValue(Double::class.java.classLoader) as Double,
            source.readSerializable() as MeasureUnit,
            source.readSerializable() as MeasureUnit)

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeValue(ratio)
        writeSerializable(rightUnit?.ordinal)
        writeSerializable(leftUnit?.ordinal)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<RatioMeasure> = object : Parcelable.Creator<RatioMeasure> {
            override fun createFromParcel(source: Parcel): RatioMeasure = RatioMeasure(source)
            override fun newArray(size: Int): Array<RatioMeasure?> = arrayOfNulls(size)
        }
    }
}
