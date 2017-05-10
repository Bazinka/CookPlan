package com.cookplan.providers;

import com.cookplan.models.ToDoItem;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;

/**
 * Created by DariaEfimova on 24.04.17.
 */

public interface ToDoListProvider {

    Observable<List<ToDoItem>> getUserToDoList();

    Single<ToDoItem> createToDoItem(ToDoItem item);

    Single<ToDoItem> updateToDoItem(ToDoItem item);

    Completable removeToDoItem(ToDoItem item);
}
