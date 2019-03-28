package com.cookplan.providers.impl

import com.cookplan.models.CookPlanError
import com.cookplan.models.ToDoCategory
import com.cookplan.models.ToDoItem
import com.cookplan.providers.ToDoListProvider
import com.cookplan.utils.DatabaseConstants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject
import java.util.*

/**
 * Created by DariaEfimova on 24.04.17.
 */

class ToDoListProviderImpl : ToDoListProvider {


    private val database: DatabaseReference

    private val subjectToDoList: BehaviorSubject<List<ToDoItem>>
    private val subjectToDoCategoriesList: BehaviorSubject<List<ToDoCategory>>

    init {
        this.database = FirebaseDatabase.getInstance().reference
        subjectToDoList = BehaviorSubject.create()
        subjectToDoCategoriesList = BehaviorSubject.create()
        getFirebaseUsersToDoList()
        getFirebaseUsersToDoCategoriesList()
    }

    private fun getFirebaseUsersToDoCategoriesList() {
        val items = database.child(DatabaseConstants.DATABASE_TO_DO_CATEGORY_ITEMS_TABLE)
        items.orderByChild(DatabaseConstants.DATABASE_USER_ID_FIELD)
                .equalTo(FirebaseAuth.getInstance().currentUser?.uid)
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        val toDoItemCategoriesList = mutableListOf<ToDoCategory>()
                        for (itemSnapshot in dataSnapshot.children) {
                            val toDoCategory = itemSnapshot.getValue(ToDoCategory::class.java)
                            toDoCategory?.id = itemSnapshot.key
                            toDoItemCategoriesList.add(toDoCategory!!)
                        }
                        subjectToDoCategoriesList.onNext(toDoItemCategoriesList)
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        subjectToDoCategoriesList.onError(CookPlanError(databaseError))
                    }
                })
    }

    private fun getFirebaseUsersToDoList() {
        val items = database.child(DatabaseConstants.DATABASE_TO_DO_ITEMS_TABLE)
        items.orderByChild(DatabaseConstants.DATABASE_USER_ID_FIELD)
                .equalTo(FirebaseAuth.getInstance().currentUser?.uid)
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        val todoList = mutableListOf<ToDoItem>()
                        for (itemSnapshot in dataSnapshot.children) {
                            val toDoItem = itemSnapshot.getValue(ToDoItem::class.java)
                            toDoItem?.id = itemSnapshot.key
                            todoList.add(toDoItem!!)
                        }
                        subjectToDoList.onNext(todoList)
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        subjectToDoList.onError(CookPlanError(databaseError))
                    }
                })
    }

    override fun getUserToDoCategoriesList(): Observable<List<ToDoCategory>> {
        return subjectToDoCategoriesList
    }

    override fun getUserToDoList(): Observable<List<ToDoItem>> {
        return subjectToDoList
    }

    override fun getCompanyToDoList(companyId: String): Observable<List<ToDoItem>> {
        return subjectToDoList.map { toDoItems ->
            val toDoItemList = ArrayList<ToDoItem>()
            for (item in toDoItems) {
                if (item.companyId == companyId) {
                    toDoItemList.add(item)
                }
            }
            toDoItemList
        }
    }

    override fun createToDoItem(item: ToDoItem): Single<ToDoItem> {
        return Single.create { emitter ->
            val todoRef = database.child(DatabaseConstants.DATABASE_TO_DO_ITEMS_TABLE)
            todoRef.push().setValue(item) { databaseError, reference ->
                if (databaseError != null) {
                    emitter.onError(CookPlanError(databaseError))
                } else {
                    item.id = reference.key
                    emitter.onSuccess(item)
                }
            }
        }
    }

    override fun createToDoCategory(category: ToDoCategory): Single<ToDoCategory> {
        return Single.create { emitter ->
            val categoryRef = database.child(DatabaseConstants.DATABASE_TO_DO_CATEGORY_ITEMS_TABLE)
            categoryRef.push().setValue(category) { databaseError, reference ->
                if (databaseError != null) {
                    emitter.onError(CookPlanError(databaseError))
                } else {
                    category.id = reference.key
                    emitter.onSuccess(category)
                }
            }
        }
    }

    override fun updateToDoItem(item: ToDoItem): Single<ToDoItem> {
        return Single.create { emitter ->
            val values = HashMap<String, Any>()
            values.put(DatabaseConstants.DATABASE_NAME_FIELD, item.name)
            values.put(DatabaseConstants.DATABASE_COMMENT_FIELD, item.comment ?: String())
            values.put(DatabaseConstants.DATABASE_CATEGORY_ID_FIELD, item.categoryId ?: String())
            values.put(DatabaseConstants.DATABASE_TO_DO_STATUS_FIELD, item.toDoStatus)
            val todoItemRef = database.child(DatabaseConstants.DATABASE_TO_DO_ITEMS_TABLE)
            todoItemRef.child(item.id!!).updateChildren(values) { databaseError, databaseReference ->
                if (databaseError != null) {
                    emitter?.onError(CookPlanError(databaseError))
                } else {
                    emitter?.onSuccess(item)
                }
            }
        }
    }

    override fun removeToDoItem(item: ToDoItem): Completable {
        return Completable.create { emitter ->
            val todoItemRef = database.child(DatabaseConstants.DATABASE_TO_DO_ITEMS_TABLE)
            val ref = todoItemRef.child(item.id!!)
            ref.removeValue()
                    .addOnFailureListener { exeption -> emitter.onError(CookPlanError(exeption.message)) }
                    .addOnCompleteListener { task ->
                        if (task.isComplete) {
                            emitter.onComplete()
                        }
                    }
        }
    }

    override fun removeToDoCategory(category: ToDoCategory): Completable {
        return Completable.create { emitter ->
            val todoItemRef = database.child(DatabaseConstants.DATABASE_TO_DO_CATEGORY_ITEMS_TABLE)
            val ref = todoItemRef.child(category.id!!)
            ref.removeValue()
                    .addOnFailureListener { exeption -> emitter.onError(CookPlanError(exeption.message)) }
                    .addOnCompleteListener { task ->
                        if (task.isComplete) {
                            emitter.onComplete()
                        }
                    }
        }
    }
}
