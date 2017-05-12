package com.cookplan.todo_list;

import com.cookplan.models.ToDoCategory;
import com.cookplan.models.ToDoItem;

import java.util.List;

/**
 * Created by DariaEfimova on 10.05.17.
 */

public interface ToDoListPresenter {

    void getToDoList();

    void updateToDoItem(ToDoItem item);

    void onStop();

    void deleteToDoItems(List<ToDoItem> items);

    void deleteToDoCategories(List<ToDoCategory> categories);
}
