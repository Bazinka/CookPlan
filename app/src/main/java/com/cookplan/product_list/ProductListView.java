package com.cookplan.product_list;

import com.cookplan.models.Product;
import com.cookplan.share.ShareView;

import java.util.List;

/**
 * Created by DariaEfimova on 20.03.17.
 */

public interface ProductListView extends ShareView {

    void setErrorToast(String error);

    void setProductList(List<Product> productList);

    void setEmptyView();
}
