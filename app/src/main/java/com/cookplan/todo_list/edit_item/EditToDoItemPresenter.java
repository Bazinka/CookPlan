package com.cookplan.todo_list.edit_item;

import com.cookplan.models.ToDoCategory;
import com.cookplan.models.ToDoItem;

/**
 * Created by DariaEfimova on 10.05.17.
 */

public interface EditToDoItemPresenter {
    void getToDoCategoriesList();

    void onStop();

    void saveToDoItem(ToDoItem item, String name, String comment, ToDoCategory category);
}
