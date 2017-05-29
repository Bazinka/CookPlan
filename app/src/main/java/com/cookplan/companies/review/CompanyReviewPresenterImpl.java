package com.cookplan.companies.review;

import com.cookplan.models.Company;
import com.cookplan.models.CookPlanError;
import com.cookplan.models.ToDoCategory;
import com.cookplan.models.ToDoItem;
import com.cookplan.providers.ToDoListProvider;
import com.cookplan.providers.impl.ToDoListProviderImpl;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by DariaEfimova on 29.05.17.
 */

public class CompanyReviewPresenterImpl implements CompanyReviewPresenter {

    private CompanyReviewView mainView;

    private ToDoListProvider dataProvider;
    private CompositeDisposable disposables;
    private List<ToDoCategory> categoryToDoList;

    public CompanyReviewPresenterImpl(CompanyReviewView mainView) {
        this.mainView = mainView;
        dataProvider = new ToDoListProviderImpl();
        disposables = new CompositeDisposable();
        categoryToDoList = new ArrayList<>();
    }

    @Override
    public void getCompanyToDoList(Company company) {
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
                                loadCompanyToDoList(company);
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

    private void loadCompanyToDoList(Company company) {
        disposables.add(
                dataProvider.getCompanyToDoList(company.getId())
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
    public void setCompanyCategoryToDoList(List<ToDoCategory> categories) {
        categoryToDoList.clear();
        categoryToDoList.addAll(categories);
    }

    @Override
    public ToDoCategory getToDoCategoryById(String categoryId) {
        ToDoCategory category = null;
        if (categoryId != null && !categoryId.isEmpty()) {
            for (ToDoCategory toDoCategory : categoryToDoList) {
                if (toDoCategory.getId().equals(categoryId)) {
                    category = toDoCategory;
                    break;
                }
            }
        }
        return category;
    }

    @Override
    public void onStop() {
        disposables.clear();
    }

}
