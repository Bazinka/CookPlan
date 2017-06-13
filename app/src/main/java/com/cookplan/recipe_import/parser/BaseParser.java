package com.cookplan.recipe_import.parser;

import com.cookplan.models.CookPlanError;
import com.cookplan.models.Ingredient;
import com.cookplan.models.Product;
import com.cookplan.models.Recipe;
import com.cookplan.providers.ProductProvider;
import com.cookplan.providers.impl.ProductProviderImpl;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by DariaEfimova on 09.06.17.
 */

public abstract class BaseParser implements Parser {

    private String url;
    private ProductProvider productDataProvider;
    private CompositeDisposable disposables;
    private ParserResultListener resultListener;

    public BaseParser(String url) {
        this.url = url;
        productDataProvider = new ProductProviderImpl();
        disposables = new CompositeDisposable();
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

    private void parseDocument(Document doc) {
        List<String> names = getProductsNames(doc);
        disposables.add(
                productDataProvider.getTheClosestProductsToStrings(names)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableObserver<Map<String, List<Product>>>() {
                            @Override
                            public void onNext(Map<String, List<Product>> namesToProducts) {
                                disposables.clear();
                                if (resultListener != null) {
                                    Map<String, List<Ingredient>> ingredients = parceDocumentToIngredientList(doc, namesToProducts);
                                    Recipe recipe = parceDocumentToRecipe(doc);
                                    resultListener.onSuccess(recipe, ingredients);
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

                            @Override
                            public void onComplete() {

                            }
                        }));
    }

    protected abstract List<String> getProductsNames(Document doc);

    protected abstract Map<String, List<Ingredient>> parceDocumentToIngredientList(Document doc, Map<String, List<Product>> namesToProducts);

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

    private Recipe parceDocumentToRecipe(Document doc) {
        Recipe recipe = new Recipe();
        recipe.setName(parseRecipeTitleFromDoc(doc));

        String description = parseDescriptionFromDoc(doc);

        recipe.setDesc(description);
        return recipe;
    }

    protected abstract String parseDescriptionFromDoc(Document doc);

    protected abstract String parseRecipeTitleFromDoc(Document doc);


}
