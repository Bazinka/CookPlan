package com.cookplan.models;

/**
 * Created by DariaEfimova on 29.03.17.
 */

public enum SharedData {
    RECIPE,
    INGREDIENTS;

    public static SharedData getSharedDataByName(String name) {
        if (name.equals(RECIPE.name())) {
            return RECIPE;
        }
        if (name.equals(INGREDIENTS.name())) {
            return INGREDIENTS;
        }
        return null;
    }
}
