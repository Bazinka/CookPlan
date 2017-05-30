package com.cookplan.companies.review.todo_fragment;

import com.cookplan.models.Company;
import com.cookplan.models.ToDoCategory;

import java.util.List;

/**
 * Created by DariaEfimova on 29.05.17.
 */

public interface CompanyToDoListPresenter {

    void getCompanyToDoList(Company company);

    void setCompanyCategoryToDoList(List<ToDoCategory> categoryToDoList);

    ToDoCategory getToDoCategoryById(String categoryId);

    void onStop();
}
