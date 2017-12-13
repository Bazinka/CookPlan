package com.cookplan.recipe.import_recipe.parser

import com.cookplan.models.CookPlanError
import com.cookplan.models.Ingredient
import com.cookplan.models.Recipe
import io.reactivex.Single
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.io.IOException

/**
 * Created by DariaEfimova on 09.06.17.
 */

abstract class BaseParser(private val url: String) : Parser {
    private var resultListener: ParserResultListener? = null

    override fun parceUrl(resultListener: ParserResultListener) {
        this.resultListener = resultListener
        startParsing()
    }

    private fun startParsing() {
        getDocumentFromUrl(url)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : SingleObserver<Document> {
                    override fun onSubscribe(d: Disposable) {

                    }

                    override fun onSuccess(doc: Document) {
                        parseDocument(doc)
                    }

                    override fun onError(e: Throwable) {
                        if (e is CookPlanError) {
                            resultListener?.onError(e.message ?: String())
                        } else {
                            resultListener?.onError("В процессе импорта произошла ошибка.")
                        }
                    }
                })
    }

    protected abstract fun parseDocument(doc: Document)

    private fun getDocumentFromUrl(url: String): Single<Document> {
        return Single.create { emitter ->
            try {
                val doc = Jsoup.connect(url).get()
                emitter?.onSuccess(doc)
            } catch (e: IOException) {
                //Если не получилось считать
                e.printStackTrace()
                emitter?.onError(e)
            }
        }
    }

    protected fun onImportSuccess(recipe: Recipe, ingredients: MutableMap<String, List<Ingredient>>) {
        resultListener?.onSuccess(recipe, ingredients)
    }

    protected fun onImportError(e: Throwable) {
        if (e is CookPlanError) {
            resultListener?.onError(e.message ?: String())
        } else {
            resultListener?.onError("В процессе импорта произошла ошибка.")
        }
    }
}
