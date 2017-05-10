package com.cookplan.todo_list;

import com.cookplan.models.CookPlanError;
import com.cookplan.models.ToDoItem;
import com.cookplan.providers.ToDoListProvider;
import com.cookplan.providers.impl.ToDoListProviderImpl;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by DariaEfimova on 10.05.17.
 */

public class ToDoListPresenterImpl implements ToDoListPresenter {
    private ToDoListView mainView;
    private ToDoListProvider provider;
    private CompositeDisposable disposables;

    public ToDoListPresenterImpl(ToDoListView mainView) {
        this.mainView = mainView;
        provider = new ToDoListProviderImpl();
        disposables = new CompositeDisposable();
    }

    @Override
    public void getToDoList() {
        disposables.add(
                provider.getUserToDoList()
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
}
