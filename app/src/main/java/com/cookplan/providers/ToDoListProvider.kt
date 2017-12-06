package com.cookplan.providers

import com.cookplan.models.ToDoCategory
import com.cookplan.models.ToDoItem

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

/**
 * Created by DariaEfimova on 24.04.17.
 */

interface ToDoListProvider {

    fun getUserToDoCategoriesList(): Observable<List<ToDoCategory>>

    fun getUserToDoList(): Observable<List<ToDoItem>>

    fun getCompanyToDoList(companyId: String): Observable<List<ToDoItem>>

    fun createToDoItem(item: ToDoItem): Single<ToDoItem>

    fun createToDoCategory(category: ToDoCategory): Single<ToDoCategory>

    fun updateToDoItem(item: ToDoItem): Single<ToDoItem>

    fun removeToDoItem(item: ToDoItem): Completable

    fun removeToDoCategory(category: ToDoCategory): Completable
}
