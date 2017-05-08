package com.cookplan.voice_interaction;

import android.os.Build;
import android.support.annotation.RequiresApi;

import com.cookplan.R;
import com.cookplan.RApplication;
import com.cookplan.models.CookPlanError;
import com.cookplan.models.Ingredient;
import com.cookplan.models.Product;
import com.cookplan.models.ShopListStatus;
import com.cookplan.providers.IngredientProvider;
import com.cookplan.providers.ProductProvider;
import com.cookplan.providers.impl.IngredientProviderImpl;
import com.cookplan.providers.impl.ProductProviderImpl;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by DariaEfimova on 08.05.17.
 */

public class VoiceLauncherPresenterImpl implements VoiceLauncherPresenter {

    private VoiceLauncherView mainView;
    private ProductProvider productDataProvider;
    private IngredientProvider ingredientDataProvider;


    public VoiceLauncherPresenterImpl(VoiceLauncherView mainView) {
        this.mainView = mainView;
        ingredientDataProvider = new IngredientProviderImpl();
        productDataProvider = new ProductProviderImpl();
    }

    @Override
    public void handleText(String text) {
        text = text.toLowerCase();
        String buyStringPattern = RApplication.getAppContext().getString(R.string.buy_pattern).toLowerCase();

        Pattern buyPattern = Pattern.compile(buyStringPattern);

        Matcher buyMatcher = buyPattern.matcher(text);
        if (buyMatcher.find()) {
            text = text.replace(buyStringPattern, "");
            text = text.replace(" ", "");
            productDataProvider.getProductByName(text)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new DisposableObserver<Product>() {
                        @Override
                        public void onNext(Product product) {
                            addIngredientToShopList(product);
                            //                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            //                                    startVoiceTrigger(product);
                            //                                }
                        }

                        @Override
                        public void onError(Throwable e) {
                            if (mainView != null) {
                                if (e instanceof CookPlanError) {
                                    mainView.setErrorString(RApplication.getAppContext().getString(R.string.saving_ingredient_error));
                                } else {
                                    mainView.setErrorString(RApplication.getAppContext().getString(R.string.didnt_find_product_error));
                                }
                            }
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        } else {
            mainView.setErrorString(RApplication.getAppContext().getString(R.string.cant_recognize_command_error));
        }
    }

    private void addIngredientToShopList(Product product) {
        //save ingredient
        Ingredient ingredient = new Ingredient(null,
                                               product.toStringName(),
                                               product,
                                               null,
                                               product.getMainMeasureUnitList().get(0),
                                               0.,
                                               ShopListStatus.NEED_TO_BUY);
        ingredientDataProvider.createIngredient(ingredient)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Ingredient>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(Ingredient ingredient) {
                        if (mainView != null) {
                            mainView.setSuccessSaveIngredient();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (mainView != null && e instanceof CookPlanError) {
                            mainView.setErrorString(RApplication.getAppContext().getString(R.string.saving_ingredient_error));
                        }
                    }
                });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void startVoiceTrigger(Product product) {
        //        VoiceInteractor.PickOptionRequest.Option option = new VoiceInteractor.PickOptionRequest.Option(RApplication.getAppContext().getString(android.R.string.ok), 1);
        //        option.addSynonym(RApplication.getAppContext().getString(android.R.string.yes));
        //        option.addSynonym(RApplication.getAppContext().getString(android.R.string.no));
        //        option.addSynonym("take it");
        //
        //        String question = RApplication.getAppContext().getString(R.string.amount_ingredient_voice_question);
        //        question = question.replace("ingredientName", product.toStringName());
        //        VoiceInteractor.Prompt prompt = new VoiceInteractor.Prompt(question);
        //        mainView.startVoiceTrigger(new VoiceInteractor.PickOptionRequest(prompt, new VoiceInteractor.PickOptionRequest.Option[]{option}, null) {
        //            @Override
        //            public void onPickOptionResult(boolean finished, Option[] selections, Bundle result) {
        //                if (finished && selections.length == 1) {
        //                    Message message = Message.obtain();
        //                    message.obj = result;
        //                } else {
        //                    mainView.setErrorString();
        //                }
        //            }
        //
        //            @Override
        //            public void onCancel() {
        //                mainView.setErrorString();
        //            }
        //        });
    }
}
