package com.cookplan.providers.impl;

import com.cookplan.R;
import com.cookplan.RApplication;
import com.cookplan.models.CookPlanError;
import com.cookplan.models.ToDoCategory;
import com.cookplan.models.ToDoItem;
import com.cookplan.providers.ToDoListProvider;
import com.cookplan.utils.DatabaseConstants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.subjects.BehaviorSubject;

/**
 * Created by DariaEfimova on 24.04.17.
 */

public class ToDoListProviderImpl implements ToDoListProvider {


    private DatabaseReference database;

    private BehaviorSubject<List<ToDoItem>> subjectToDoList;
    private BehaviorSubject<List<ToDoCategory>> subjectToDoCategoriesList;

    public ToDoListProviderImpl() {
        this.database = FirebaseDatabase.getInstance().getReference();
        subjectToDoList = BehaviorSubject.create();
        subjectToDoCategoriesList = BehaviorSubject.create();
        getFirebaseUsersToDoList();
        getFirebaseUsersToDoCategoriesList();
    }

    private void getFirebaseUsersToDoCategoriesList() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String myUid = user.getUid();
            Query items = database.child(DatabaseConstants.DATABASE_TO_DO_CATEGORY_ITEMS_TABLE);
            items.orderByChild(DatabaseConstants.DATABASE_USER_ID_FIELD)
                    .equalTo(myUid)
                    .addValueEventListener(new ValueEventListener() {
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            List<ToDoCategory> toDoItemCategoriesList = new ArrayList<>();
                            for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()) {
                                ToDoCategory toDoCategory = itemSnapshot.getValue(ToDoCategory.class);
                                if (toDoCategory != null) {
                                    toDoCategory.setId(itemSnapshot.getKey());
                                    toDoItemCategoriesList.add(toDoCategory);
                                }
                            }
                            if (subjectToDoCategoriesList != null) {
                                subjectToDoCategoriesList.onNext(toDoItemCategoriesList);
                            }
                        }

                        public void onCancelled(DatabaseError databaseError) {
                            if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                                if (subjectToDoCategoriesList != null) {
                                    subjectToDoCategoriesList.onError(new CookPlanError(databaseError));
                                }
                            }
                        }
                    });
        }
    }

    private void getFirebaseUsersToDoList() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String myUid = user.getUid();
            Query items = database.child(DatabaseConstants.DATABASE_TO_DO_ITEMS_TABLE);
            items.orderByChild(DatabaseConstants.DATABASE_USER_ID_FIELD)
                    .equalTo(myUid)
                    .addValueEventListener(new ValueEventListener() {
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            List<ToDoItem> todoList = new ArrayList<>();
                            for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()) {
                                ToDoItem toDoItem = itemSnapshot.getValue(ToDoItem.class);
                                if (toDoItem != null) {
                                    toDoItem.setId(itemSnapshot.getKey());
                                    todoList.add(toDoItem);
                                }
                            }
                            if (subjectToDoList != null) {
                                subjectToDoList.onNext(todoList);
                            }
                        }

                        public void onCancelled(DatabaseError databaseError) {
                            if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                                if (subjectToDoList != null) {
                                    subjectToDoList.onError(new CookPlanError(databaseError));
                                }
                            }
                        }
                    });
        }
    }

    @Override
    public Observable<List<ToDoCategory>> getUserToDoCategoriesList() {
        return subjectToDoCategoriesList;
    }

    @Override
    public Observable<List<ToDoItem>> getUserToDoList() {
        return subjectToDoList;
    }

    @Override
    public Single<ToDoItem> createToDoItem(ToDoItem item) {
        return Single.create(emitter -> {
            DatabaseReference todoRef = database.child(DatabaseConstants.DATABASE_TO_DO_ITEMS_TABLE);
            todoRef.push().setValue(item, (databaseError, reference) -> {
                if (databaseError != null) {
                    emitter.onError(new CookPlanError(databaseError));
                } else {
                    item.setId(reference.getKey());
                    emitter.onSuccess(item);
                }
            });
        });
    }

    @Override
    public Single<ToDoCategory> createToDoCategory(ToDoCategory category) {
        return Single.create(emitter -> {
            DatabaseReference categoryRef = database.child(DatabaseConstants.DATABASE_TO_DO_CATEGORY_ITEMS_TABLE);
            categoryRef.push().setValue(category, (databaseError, reference) -> {
                if (databaseError != null) {
                    emitter.onError(new CookPlanError(databaseError));
                } else {
                    category.setId(reference.getKey());
                    emitter.onSuccess(category);
                }
            });
        });
    }

    @Override
    public Single<ToDoItem> updateToDoItem(ToDoItem item) {
        return Single.create(emitter -> {
            Map<String, Object> values = new HashMap<>();
            values.put(DatabaseConstants.DATABASE_NAME_FIELD, item.getName());
            values.put(DatabaseConstants.DATABASE_COMMENT_FIELD, item.getComment());
            values.put(DatabaseConstants.DATABASE_CATEGORY_ID_FIELD, item.getCategoryId());
            values.put(DatabaseConstants.DATABASE_TO_DO_STATUS_FIELD, item.getToDoStatus());
            DatabaseReference todoItemRef = database.child(DatabaseConstants.DATABASE_TO_DO_ITEMS_TABLE);
            todoItemRef.child(item.getId()).updateChildren(values, (databaseError, databaseReference) -> {
                if (databaseError != null) {
                    if (emitter != null) {
                        emitter.onError(new CookPlanError(databaseError));
                    }
                } else {
                    if (emitter != null) {
                        emitter.onSuccess(item);
                    }
                }
            });
        });
    }

    @Override
    public Completable removeToDoItem(ToDoItem item) {
        return Completable.create(emitter -> {
            if (item != null && item.getId() != null) {
                DatabaseReference todoItemRef = database.child(DatabaseConstants.DATABASE_TO_DO_ITEMS_TABLE);
                DatabaseReference ref = todoItemRef.child(item.getId());
                ref.removeValue()
                        .addOnFailureListener(exeption -> emitter.onError(new CookPlanError(exeption.getMessage())))
                        .addOnCompleteListener(task -> {
                            if (task.isComplete()) {
                                emitter.onComplete();
                            }
                        });
            } else {
                emitter.onError(new CookPlanError(RApplication.getAppContext().getString(R.string.error_remove_todo_item)));
            }
        });
    }
}
