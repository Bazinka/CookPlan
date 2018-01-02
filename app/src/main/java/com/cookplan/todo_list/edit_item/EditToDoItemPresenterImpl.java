package com.cookplan.todo_list.edit_item;

import com.cookplan.models.Company;
import com.cookplan.models.CookPlanError;
import com.cookplan.models.ToDoCategory;
import com.cookplan.models.ToDoItem;
import com.cookplan.providers.ProviderFactory;
import com.cookplan.providers.ToDoListProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by DariaEfimova on 10.05.17.
 */

public class EditToDoItemPresenterImpl implements EditToDoItemPresenter {

    private EditToDoItemView mainView;
    private ToDoListProvider dataProvider;
    private CompositeDisposable disposables;

    public EditToDoItemPresenterImpl(EditToDoItemView mainView) {
        this.mainView = mainView;
        dataProvider = ProviderFactory.Companion.getToDoListProvider();
        disposables = new CompositeDisposable();
    }

    @Override
    public void getToDoCategoriesList() {
        disposables.add(
                dataProvider.getUserToDoCategoriesList()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableObserver<List<ToDoCategory>>() {
                            @Override
                            public void onNext(List<ToDoCategory> items) {
                                if (mainView != null) {
                                    mainView.setToDoCategoriesList(items);
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                if (mainView != null && e instanceof CookPlanError) {
                                    mainView.setError(e.getMessage());
                                }
                            }

                            @Override
                            public void onComplete() {

                            }
                        }));
    }

    @Override
    public void onStop() {

    }

    @Override
    public void saveToDoItem(ToDoItem item, String name, String comment, Company company, ToDoCategory category) {
        if (category != null && category.getId() == null) {
            ToDoItem finalItem = item;
            dataProvider.createToDoCategory(category)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new SingleObserver<ToDoCategory>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onSuccess(ToDoCategory toDoCategory) {
                            saveToDoItem(finalItem, name, comment, company, toDoCategory);
                        }

                        @Override
                        public void onError(Throwable e) {
                            if (mainView != null && e instanceof CookPlanError) {
                                mainView.setError(e.getMessage());
                            }
                        }
                    });
        } else {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (item != null) {
                item.setName(name);
                item.setComment(comment);
            } else {
                String companyId = company == null ? null : company.getId();
                item = new ToDoItem(user.getUid(), name, companyId, comment);
            }
            item.setCategoryId(category != null ? category.getId() : null);
            if (item.getId() == null) {
                dataProvider.createToDoItem(item)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new SingleObserver<ToDoItem>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onSuccess(ToDoItem toDoItem) {
                                if (mainView != null) {
                                    mainView.setSuccessfullSaving();
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                if (mainView != null && e instanceof CookPlanError) {
                                    mainView.setError(e.getMessage());
                                }
                            }
                        });
            } else {
                dataProvider.updateToDoItem(item)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new SingleObserver<ToDoItem>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onSuccess(ToDoItem toDoItem) {
                                if (mainView != null) {
                                    mainView.setSuccessfullSaving();
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                if (mainView != null && e instanceof CookPlanError) {
                                    mainView.setError(e.getMessage());
                                }
                            }
                        });
            }
        }
    }
}
