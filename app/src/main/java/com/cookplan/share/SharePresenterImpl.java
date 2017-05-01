package com.cookplan.share;

import com.cookplan.R;
import com.cookplan.models.CookPlanError;
import com.cookplan.models.ShareUserInfo;
import com.cookplan.providers.FamilyModeProvider;
import com.cookplan.providers.impl.FamilyModeProviderImpl;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DefaultObserver;
import io.reactivex.observers.DisposableObserver;
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
                    .subscribe(new DefaultObserver<List<ShareUserInfo>>() {

                        @Override
                        public void onNext(List<ShareUserInfo> shareUserInfoList) {
                            if (shareUserInfoList.isEmpty()) {
                                String myUid = user.getUid();
                                ShareUserInfo shareInfoItem =
                                        new ShareUserInfo(myUid, user.getDisplayName(), userEmail);
                                familyModeProvider.createDataSharedItem(shareInfoItem)
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(new SingleObserver<ShareUserInfo>() {

                                            @Override
                                            public void onSubscribe(Disposable d) {
                                                d.dispose();
                                            }

                                            @Override
                                            public void onSuccess(ShareUserInfo shareUserInfo) {
                                                if (mainView != null) {
                                                    mainView.setShareIcon();
                                                }
                                            }

                                            @Override
                                            public void onError(Throwable e) {
                                                if (mainView != null) {
                                                    mainView.setShareError(R.string.error_share_title);
                                                }
                                            }
                                        });
                            } else {
                                if (mainView != null) {
                                    mainView.setShareError(R.string.have_already_shared);
                                }
                            }

                        }

                        @Override
                        public void onError(Throwable e) {
                            if (mainView != null && e instanceof CookPlanError) {
                                if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                                    mainView.setShareError(R.string.shared_data_error);
                                }
                            }
                        }

                        @Override
                        public void onComplete() {
                        }
                    });
        } else if (user != null && user.getEmail().equals(userEmail)) {
            mainView.setShareError(R.string.cant_share_to_myself);
        }
    }
}
