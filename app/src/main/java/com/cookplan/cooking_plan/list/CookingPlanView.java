package com.cookplan.cooking_plan.list;

import org.joda.time.LocalDate;

import java.util.List;
import java.util.Map;

/**
 * Created by DariaEfimova on 21.03.17.
 */

public interface CookingPlanView {

    void setErrorToast(String error);

    void setCookingList(Map<LocalDate, List<Object>> dateToObjectMap);

    void setEmptyView();
}
