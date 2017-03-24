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

    public static ShopListStatus getShopListStatusId(int id) {
        switch (id) {
            case 0:
                return NONE;
            case 1:
                return NEED_TO_BUY;
            case 2:
                return ALREADY_BOUGHT;
            default:
                return null;
        }
    }
}
