package com.cookplan.todo_list;

import com.cookplan.models.ToDoCategory;
import com.cookplan.models.ToDoItem;

import java.util.List;

/**
 * Created by DariaEfimova on 10.05.17.
 */

public interface ToDoListView {


    void setErrorToast(String error);

    void setToDoList(List<ToDoItem> todoList);

    void setToDoCategoryList(List<ToDoCategory> toDoCategoryList);

    void setEmptyView();
}
