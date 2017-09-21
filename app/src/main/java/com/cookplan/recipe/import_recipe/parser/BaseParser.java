package com.cookplan.recipe.import_recipe.parser;

import com.cookplan.models.CookPlanError;
import com.cookplan.models.Ingredient;
import com.cookplan.models.Recipe;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by DariaEfimova on 09.06.17.
 */

public abstract class BaseParser implements Parser {

    private String url;
    private ParserResultListener resultListener;

    public BaseParser(String url) {
        this.url = url;
    }

    @Override
    public void parceUrl(ParserResultListener resultListener) {
        this.resultListener = resultListener;
        startParsing();
    }

    private void startParsing() {
        getDocumentFromUrl(url)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Document>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(Document doc) {
                        if (doc != null) {
                            parseDocument(doc);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (resultListener != null) {
                            if (e instanceof CookPlanError) {
                                resultListener.onError(e.getMessage());
                            } else {
                                resultListener.onError("В процессе импорта произошла ошибка.");
                            }
                        }
                    }
                });
    }

    protected abstract void parseDocument(Document doc);

    private Single<Document> getDocumentFromUrl(String url) {
        return Single.create(emitter -> {
            Document doc = null;//Здесь хранится будет разобранный html документ
            try {
                doc = Jsoup.connect(url).get();
                if (emitter != null) {
                    emitter.onSuccess(doc);
                }
            } catch (IOException e) {
                //Если не получилось считать
                e.printStackTrace();
                if (emitter != null) {
                    emitter.onError(e);
                }
            }
        });
    }

    protected void onImportSuccess(Recipe recipe, Map<String, List<Ingredient>> ingredients) {
        if (resultListener != null) {
            resultListener.onSuccess(recipe, ingredients);
        }
    }

    protected void onImportError(Throwable e) {
        if (resultListener != null) {
            if (e instanceof CookPlanError) {
                resultListener.onError(e.getMessage());
            } else {
                resultListener.onError("В процессе импорта произошла ошибка.");
            }
        }
    }
}
