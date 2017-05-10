package com.cookplan.todo_list.edit_item;

import com.cookplan.models.ToDoCategory;

import java.util.List;

/**
 * Created by DariaEfimova on 10.05.17.
 */

public interface EditToDoItemView {

    void setToDoCategoriesList(List<ToDoCategory> categoriesList);

    void setSuccessfullSaving();

    void setError(String error);
}
