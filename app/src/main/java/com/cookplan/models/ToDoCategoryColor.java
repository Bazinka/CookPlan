package com.cookplan.models;

import com.cookplan.R;

/**
 * Created by DariaEfimova on 29.03.17.
 */

public enum ToDoCategoryColor {
    BLACK(R.color.black),
    GREY(R.color.category_blue_grey),
    BROWN(R.color.category_brown),
    RED(R.color.category_dark_red),
    ORANGE(R.color.category_dark_orange),
    YELLOW(R.color.category_amber),
    GREEN(R.color.category_green),
    TEAL(R.color.category_teal),
    CYAN(R.color.category_cyan),
    BLUE(R.color.category_blue),
    DARK_BLUE(R.color.category_indigo),
    PURPLE(R.color.category_purple);

    private int colorId;


    ToDoCategoryColor(int colorId) {
        this.colorId = colorId;
    }

    public int getColorId() {
        return colorId;
    }

    public static ToDoCategoryColor getToDoCategoryColorByName(String name) {
        if (name.equals(BLACK.name())) {
            return BLACK;
        }
        if (name.equals(GREY.name())) {
            return GREY;
        }
        if (name.equals(BROWN.name())) {
            return BROWN;
        }
        if (name.equals(RED.name())) {
            return RED;
        }
        if (name.equals(ORANGE.name())) {
            return ORANGE;
        }
        if (name.equals(YELLOW.name())) {
            return YELLOW;
        }
        if (name.equals(GREEN.name())) {
            return GREEN;
        }
        if (name.equals(TEAL.name())) {
            return TEAL;
        }
        if (name.equals(CYAN.name())) {
            return CYAN;
        }
        if (name.equals(BLUE.name())) {
            return BLUE;
        }
        if (name.equals(DARK_BLUE.name())) {
            return DARK_BLUE;
        }
        if (name.equals(PURPLE.name())) {
            return PURPLE;
        }
        return null;
    }
}
