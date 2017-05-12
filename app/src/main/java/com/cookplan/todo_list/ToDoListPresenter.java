package com.cookplan.todo_list;

import com.cookplan.models.ToDoItem;

/**
 * Created by DariaEfimova on 10.05.17.
 */

public interface ToDoListPresenter {

    void getToDoList();

    void updateToDoItem(ToDoItem item);

    void onStop();
}
