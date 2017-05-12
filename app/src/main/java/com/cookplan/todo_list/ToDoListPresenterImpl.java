package com.cookplan.todo_list;

import com.cookplan.models.CookPlanError;
import com.cookplan.models.ToDoCategory;
import com.cookplan.models.ToDoItem;
import com.cookplan.providers.ToDoListProvider;
import com.cookplan.providers.impl.ToDoListProviderImpl;

import java.util.List;

import io.reactivex.CompletableObserver;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by DariaEfimova on 10.05.17.
 */

public class ToDoListPresenterImpl implements ToDoListPresenter {
    private ToDoListView mainView;
    private ToDoListProvider dataProvider;
    private CompositeDisposable disposables;

    public ToDoListPresenterImpl(ToDoListView mainView) {
        this.mainView = mainView;
        dataProvider = new ToDoListProviderImpl();
        disposables = new CompositeDisposable();
    }

    @Override
    public void getToDoList() {
        disposables.add(
                dataProvider.getUserToDoCategoriesList()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableObserver<List<ToDoCategory>>() {
                            @Override
                            public void onNext(List<ToDoCategory> categories) {
                                if (mainView != null) {
                                    mainView.setToDoCategoryList(categories);
                                }
                                loadToDoList();
                            }

                            @Override
                            public void onError(Throwable e) {
                                if (mainView != null && e instanceof CookPlanError) {
                                    mainView.setErrorToast(e.getMessage());
                                }
                            }

                            @Override
                            public void onComplete() {

                            }
                        }));

    }

    @Override
    public void updateToDoItem(ToDoItem item) {
        dataProvider.updateToDoItem(item)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<ToDoItem>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(ToDoItem toDoItem) {
                        //                        if (mainView != null) {
                        //                            mainView.setSuccessfullSaving();
                        //                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (mainView != null && e instanceof CookPlanError) {
                            mainView.setErrorToast(e.getMessage());
                        }
                    }
                });
    }

    private void loadToDoList() {
        disposables.add(
                dataProvider.getUserToDoList()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableObserver<List<ToDoItem>>() {
                            @Override
                            public void onNext(List<ToDoItem> items) {
                                if (mainView != null) {
                                    if (items.isEmpty()) {
                                        mainView.setEmptyView();
                                    } else {
                                        mainView.setToDoList(items);
                                    }
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                if (mainView != null && e instanceof CookPlanError) {
                                    mainView.setErrorToast(e.getMessage());
                                }
                            }

                            @Override
                            public void onComplete() {

                            }
                        }));
    }

    @Override
    public void onStop() {
        disposables.clear();
    }

    @Override
    public void deleteToDoItems(List<ToDoItem> items) {
        for (ToDoItem item : items) {
            dataProvider.removeToDoItem(item)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new CompletableObserver() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onComplete() {
                        }

                        @Override
                        public void onError(Throwable e) {
                            if (mainView != null && e instanceof CookPlanError) {
                                mainView.setErrorToast(e.getMessage());
                            }
                        }
                    });
        }
    }

    @Override
    public void deleteToDoCategories(List<ToDoCategory> categories) {
        for (ToDoCategory category : categories) {
            dataProvider.removeToDoCategory(category)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new CompletableObserver() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onComplete() {
                        }

                        @Override
                        public void onError(Throwable e) {
                            if (mainView != null && e instanceof CookPlanError) {
                                mainView.setErrorToast(e.getMessage());
                            }
                        }
                    });
        }
    }
}
