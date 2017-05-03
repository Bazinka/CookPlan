package com.cookplan.share;

import com.cookplan.R;
import com.cookplan.models.CookPlanError;
import com.cookplan.models.ShareUserInfo;
import com.cookplan.providers.FamilyModeProvider;
import com.cookplan.providers.impl.FamilyModeProviderImpl;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Collections;

import io.reactivex.CompletableObserver;
import io.reactivex.MaybeObserver;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by DariaEfimova on 01.05.17.
 */

public class SharePresenterImpl implements SharePresenter {

    private FamilyModeProvider familyModeProvider;

    private ShareView mainView;

    public SharePresenterImpl(ShareView mainView) {
        this.mainView = mainView;
        familyModeProvider = new FamilyModeProviderImpl();
    }

    @Override
    public void shareData(String userEmail) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null && !user.getEmail().equals(userEmail)) {
            familyModeProvider.getDataSharedByMe()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new MaybeObserver<ShareUserInfo>() {


                        @Override
                        public void onSubscribe(Disposable d) {
                        }

                        @Override
                        public void onSuccess(ShareUserInfo shareUserInfo) {
                            //item was found and we need to update it
                            boolean emailExist = false;
                            for (String clientEmail : shareUserInfo.getClientUserEmailList()) {
                                if (clientEmail.equals(userEmail)) {
                                    emailExist = true;
                                    break;
                                }
                            }
                            if (!emailExist) {
                                shareUserInfo.getClientUserEmailList().add(userEmail);
                            }
                            familyModeProvider.updateDataSharedItem(shareUserInfo)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new SingleObserver<ShareUserInfo>() {

                                        @Override
                                        public void onSubscribe(Disposable d) {
                                        }

                                        @Override
                                        public void onSuccess(ShareUserInfo shareUserInfo) {
                                            if (mainView != null) {
                                                mainView.setShareIcon(true);
                                            }
                                        }

                                        @Override
                                        public void onError(Throwable e) {
                                            if (mainView != null) {
                                                mainView.setShareError(R.string.error_share_title);
                                            }
                                        }
                                    });
                        }

                        @Override
                        public void onComplete() {
                            //item wasn't found and we need to create a new one
                            String myUid = user.getUid();
                            ShareUserInfo shareInfoItem =
                                    new ShareUserInfo(myUid, user.getDisplayName(),
                                                      Collections.singletonList(userEmail));
                            familyModeProvider.createDataSharedItem(shareInfoItem)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new SingleObserver<ShareUserInfo>() {

                                        @Override
                                        public void onSubscribe(Disposable d) {
                                        }

                                        @Override
                                        public void onSuccess(ShareUserInfo shareUserInfo) {
                                            if (mainView != null) {
                                                mainView.setShareIcon(true);
                                            }
                                        }

                                        @Override
                                        public void onError(Throwable e) {
                                            if (mainView != null) {
                                                mainView.setShareError(R.string.error_share_title);
                                            }
                                        }
                                    });
                        }

                        @Override
                        public void onError(Throwable e) {
                            if (mainView != null && e instanceof CookPlanError) {
                                if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                                    mainView.setShareError(R.string.shared_data_error);
                                }
                            }
                        }
                    });
        } else if (user != null && user.getEmail().equals(userEmail)) {
            mainView.setShareError(R.string.cant_share_to_myself);
        }
    }

    @Override
    public void turnOffFamilyMode() {
        familyModeProvider.removeAllSharedData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        if (mainView != null) {
                            mainView.setShareIcon(false);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (mainView != null) {
                            mainView.setShareError(R.string.error_share_title);
                        }
                    }
                });
    }

    @Override
    public void isFamilyModeTurnOnRequest() {
        familyModeProvider.getDataSharedByMe()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new MaybeObserver<ShareUserInfo>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(ShareUserInfo shareUserInfo) {
                        if (mainView != null) {
                            mainView.setShareIcon(true);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (mainView != null && e instanceof CookPlanError) {
                            mainView.setShareError(R.string.error_share_data_loading);
                        }
                    }

                    @Override
                    public void onComplete() {
                        if (mainView != null) {
                            mainView.setShareIcon(false);
                        }
                    }
                });

    }
}
