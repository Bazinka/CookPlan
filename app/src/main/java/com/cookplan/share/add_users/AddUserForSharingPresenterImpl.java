package com.cookplan.share.add_users;

import android.content.Context;

import com.cookplan.models.Contact;
import com.cookplan.models.CookPlanError;
import com.cookplan.models.ShareUserInfo;
import com.cookplan.providers.FamilyModeProvider;
import com.cookplan.providers.impl.FamilyModeProviderImpl;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.MaybeObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by DariaEfimova on 04.05.17.
 */

public class AddUserForSharingPresenterImpl implements AddUserForSharingPresenter {

    private FamilyModeProvider familyModeProvider;
    private AddUserForSharingView mainView;
    private Context context;

    public AddUserForSharingPresenterImpl(AddUserForSharingView mainView, Context context) {
        this.mainView = mainView;
        this.context = context;
        familyModeProvider = new FamilyModeProviderImpl();
    }

    @Override
    public void getSharedUsers() {
        familyModeProvider.getDataSharedByMe()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MaybeObserver<ShareUserInfo>() {


                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onSuccess(ShareUserInfo shareUserInfo) {
                        List<Contact> contactList = new ArrayList<Contact>();
                        for (String email : shareUserInfo.getClientUserEmailList()) {
                            contactList.add(new Contact(email));
                        }
                        if (mainView != null) {
                            mainView.setContactList(contactList);
                        }
                    }

                    @Override
                    public void onComplete() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (mainView != null && e instanceof CookPlanError) {
                            if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                                mainView.setError(e.getMessage());
                            }
                        }
                    }
                });
    }
}
