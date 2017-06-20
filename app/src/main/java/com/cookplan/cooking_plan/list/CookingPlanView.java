package com.cookplan.cooking_plan.list;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

/**
 * Created by DariaEfimova on 21.03.17.
 */

public interface CookingPlanView {

    void setErrorToast(String error);

    void setCookingList(Map<Long, List<Object>> dateToObjectMap);

    void setEmptyView();
}
