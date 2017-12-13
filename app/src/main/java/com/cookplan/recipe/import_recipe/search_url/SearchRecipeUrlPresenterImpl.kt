package com.cookplan.recipe.import_recipe.search_url

import com.cookplan.R
import com.cookplan.models.network.GoogleSearchResponce
import com.cookplan.network.NetworkServiceFactory
import com.cookplan.utils.Constants.GOOGLE_SEARCH_RESULTS_NUMBER
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException

/**
 * Created by DariaEfimova on 10.06.17.
 */

class SearchRecipeUrlPresenterImpl(private val mainView: SearchRecipeUrlView?) : SearchRecipeUrlPresenter {

    private val disposables: CompositeDisposable
    private var lastQuery: String = String()

    init {
        disposables = CompositeDisposable()
    }

    override fun searchRecipes(query: String) {
        lastQuery = query
        loadRecipes(1)
    }

    private fun loadRecipes(offset: Int) {
        disposables.add(NetworkServiceFactory.createService().getRecipes(lastQuery, offset, GOOGLE_SEARCH_RESULTS_NUMBER)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableObserver<GoogleSearchResponce>() {
                    override fun onNext(responce: GoogleSearchResponce?) {
                        mainView?.setResultGoogleSearchList(responce?.items ?: listOf())
                    }

                    override fun onError(e: Throwable) {
                        if (e is HttpException) {
                            if (e.code() == 403) {
                                mainView?.setError(R.string.google_search_too_many_requests_error)
                            } else {
                                mainView?.setError(R.string.google_search_network_error)
                            }
                        } else {
                            mainView?.setError(R.string.google_search_network_error)
                        }
                    }

                    override fun onComplete() {

                    }
                })
        )
    }

    override fun loadNextPart(offset: Int) {
        loadRecipes(offset)
    }

    override fun onStop() {
        disposables.clear()
    }
}
