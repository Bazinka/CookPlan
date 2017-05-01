package com.cookplan.shopping_list;

import com.cookplan.share.ShareView;

/**
 * Created by DariaEfimova on 24.03.17.
 */

public interface ShoppingListView extends ShareView {
    void setErrorToast(String error);

    void setEmptyView();

}
