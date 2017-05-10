package com.cookplan.todo_list.edit_item;

import com.cookplan.models.CookPlanError;
import com.cookplan.models.ToDoItem;
import com.cookplan.providers.ToDoListProvider;
import com.cookplan.providers.impl.ToDoListProviderImpl;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by DariaEfimova on 10.05.17.
 */

public class EditToDoItemPresenterImpl implements EditToDoItemPresenter {

    private EditToDoItemView mainView;
    private ToDoListProvider dataProvider;

    public EditToDoItemPresenterImpl(EditToDoItemView mainView) {
        this.mainView = mainView;
        dataProvider = new ToDoListProviderImpl();
    }

    @Override
    public void saveToDoItem(ToDoItem item) {
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
