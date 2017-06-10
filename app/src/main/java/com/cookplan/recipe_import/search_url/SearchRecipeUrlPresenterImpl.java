package com.cookplan.recipe_import.search_url;

import com.cookplan.models.network.GoogleSearchResponce;
import com.cookplan.network.NetworkServiceFactory;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by DariaEfimova on 10.06.17.
 */

public class SearchRecipeUrlPresenterImpl implements SearchRecipeUrlPresenter {

    private SearchRecipeUrlView mainView;
    private CompositeDisposable disposables;

    public SearchRecipeUrlPresenterImpl(SearchRecipeUrlView mainView) {
        this.mainView = mainView;
        disposables = new CompositeDisposable();
    }

    @Override
    public void searchRecipes(String query) {
        disposables.add(NetworkServiceFactory.createService().getRecipes(query)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribeWith(new DisposableObserver<GoogleSearchResponce>() {
                                    @Override
                                    public void onNext(GoogleSearchResponce responce) {
                                        if (mainView != null && responce != null) {
                                            mainView.setResultGoogleSearchList(responce.getItems());
                                        }
                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                    }

                                    @Override
                                    public void onComplete() {

                                    }
                                })
        );
    }

    @Override
    public void onStop() {
        if (disposables != null) {
            disposables.clear();
        }
    }
}
