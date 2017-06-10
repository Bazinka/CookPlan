package com.cookplan.models.network;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by DariaEfimova on 10.06.17.
 */

public class GoogleSearchResponce {

    @SerializedName("items")
    private List<GoogleRecipe> items;

    public List<GoogleRecipe> getItems() {
        return items;
    }

    public void setItems(List<GoogleRecipe> items) {
        this.items = items;
    }
}
