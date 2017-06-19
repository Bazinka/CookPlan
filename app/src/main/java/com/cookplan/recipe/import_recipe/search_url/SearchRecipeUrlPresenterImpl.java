package com.cookplan.recipe.import_recipe.search_url;

import com.cookplan.R;
import com.cookplan.models.network.GoogleSearchResponce;
import com.cookplan.network.NetworkServiceFactory;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.HttpException;

import static com.cookplan.utils.Constants.GOOGLE_SEARCH_RESULTS_NUMBER;

/**
 * Created by DariaEfimova on 10.06.17.
 */

public class SearchRecipeUrlPresenterImpl implements SearchRecipeUrlPresenter {

    private SearchRecipeUrlView mainView;
    private CompositeDisposable disposables;
    private String lastQuery;

    public SearchRecipeUrlPresenterImpl(SearchRecipeUrlView mainView) {
        this.mainView = mainView;
        disposables = new CompositeDisposable();
    }

    @Override
    public void searchRecipes(String query) {
        lastQuery = query;
        loadRecipes(1);
    }

    private void loadRecipes(int offset) {
        disposables.add(NetworkServiceFactory.createService().getRecipes(lastQuery, offset, GOOGLE_SEARCH_RESULTS_NUMBER)
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
                                        if (mainView != null) {
                                            if (e instanceof HttpException) {
                                                HttpException httpException = (HttpException) e;
                                                if (httpException.code() == 403) {
                                                    mainView.setError(R.string.google_search_too_many_requests_error);
                                                } else {
                                                    mainView.setError(R.string.google_search_network_error);
                                                }
                                            } else {
                                                mainView.setError(R.string.google_search_network_error);
                                            }
                                        }
                                    }

                                    @Override
                                    public void onComplete() {

                                    }
                                })
        );
    }

    @Override
    public void loadNextPart(int offset) {
        loadRecipes(offset);
    }

    @Override
    public void onStop() {
        if (disposables != null) {
            disposables.clear();
        }
    }
}
