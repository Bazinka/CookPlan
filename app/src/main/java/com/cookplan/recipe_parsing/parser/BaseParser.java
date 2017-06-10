package com.cookplan.recipe_parsing.parser;

import com.cookplan.models.CookPlanError;
import com.cookplan.models.Ingredient;
import com.cookplan.models.Product;
import com.cookplan.models.Recipe;
import com.cookplan.providers.ProductProvider;
import com.cookplan.providers.impl.ProductProviderImpl;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by DariaEfimova on 09.06.17.
 */

public abstract class BaseParser implements Parser {

    private String url;
    private ProductProvider productDataProvider;
    private ParserResultListener resultListener;

    public BaseParser(String url) {
        this.url = url;
        productDataProvider = new ProductProviderImpl();
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
        productDataProvider.findProductsByNames(names).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<List<Product>>() {
                    @Override
                    public void onNext(List<Product> products) {
                        if (resultListener != null) {
                            Map<String, List<Ingredient>> ingredients = parceDocumentToIngredientList(doc, products);
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
                });
    }

    protected abstract List<String> getProductsNames(Document doc);

    protected abstract Map<String, List<Ingredient>> parceDocumentToIngredientList(Document doc, List<Product> products);

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
        recipe.setName(doc.title());

        String description = "";

        //parsing description of eda.ru
        Elements descriptionList = doc.select(getDescTagName());
        for (Element desc : descriptionList) {
            description = description + desc.text() + "\n";
        }
        //end parsing eda.ru


        recipe.setDesc(description);
        return recipe;
    }

    protected abstract String getDescTagName();


}
