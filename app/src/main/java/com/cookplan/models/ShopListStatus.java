package com.cookplan.models;

/**
 * Created by DariaEfimova on 24.03.17.
 */

public enum ShopListStatus {
    NONE(0), NEED_TO_BUY(1), ALREADY_BOUGHT(2);

    private int id;

    ShopListStatus(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static ShopListStatus getShopListStatusName(String name) {
        if (name.equals(NONE.name())) {
            return NONE;
        }
        if (name.equals(NEED_TO_BUY.name())) {
            return NEED_TO_BUY;
        }
        if (name.equals(ALREADY_BOUGHT.name())) {
            return ALREADY_BOUGHT;
        }
        return null;
    }
}
