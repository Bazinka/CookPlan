package com.cookplan.share.add_users

import com.cookplan.R
import com.cookplan.models.Contact
import com.cookplan.models.ShareUserInfo
import com.cookplan.models.ShareUserInfoFactory
import com.cookplan.providers.FamilyModeProvider
import com.cookplan.providers.ProviderFactory
import com.google.firebase.auth.FirebaseAuth
import io.reactivex.CompletableObserver
import io.reactivex.MaybeObserver
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

/**
 * Created by DariaEfimova on 04.05.17.
 */

class ShareDataPresenterImpl(private val mainView: ShareDataView?) : ShareDataPresenter {

    private val familyModeProvider: FamilyModeProvider = ProviderFactory.Companion.familyModeProvider

    override fun getSharedUsers() {
        familyModeProvider.getDataSharedByMe()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : MaybeObserver<ShareUserInfo> {

                    override fun onSubscribe(d: Disposable) {}

                    override fun onSuccess(shareUserInfo: ShareUserInfo) {
                        val contactList = mutableListOf<Contact>()
                        shareUserInfo.clientUserEmailList.mapTo(contactList) { Contact(it) }
                        mainView?.setContactList(contactList)
                    }

                    override fun onComplete() {}

                    override fun onError(e: Throwable) {
                        mainView?.setError(e.message ?: String())
                    }
                })
    }

    override fun shareData(emailsList: MutableList<String>) {
        familyModeProvider.getDataSharedByMe()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : MaybeObserver<ShareUserInfo> {

                    override fun onSubscribe(d: Disposable) {}

                    override fun onSuccess(shareUserInfo: ShareUserInfo) {
                        //item was found and we need to update it
                        shareUserInfo.clientUserEmailList.clear()
                        shareUserInfo.clientUserEmailList.addAll(emailsList)
                        familyModeProvider.updateDataSharedItem(shareUserInfo)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(object : SingleObserver<ShareUserInfo> {

                                    override fun onSubscribe(d: Disposable) {}

                                    override fun onSuccess(shareUserInfo: ShareUserInfo) {
                                        mainView?.setShareSuccess(true)
                                    }

                                    override fun onError(e: Throwable) {
                                        mainView?.setShareError(R.string.error_share_title)
                                    }
                                })
                    }

                    override fun onComplete() {
                        //item wasn't found and we need to create a new one
                        val shareInfoItem = ShareUserInfoFactory.createShareUserInfo(
                                FirebaseAuth.getInstance().currentUser,
                                emailsList)
                        if (shareInfoItem != null) {
                            familyModeProvider.createDataSharedItem(shareInfoItem)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(object : SingleObserver<ShareUserInfo> {

                                        override fun onSubscribe(d: Disposable) {}

                                        override fun onSuccess(shareUserInfo: ShareUserInfo) {
                                            mainView?.setShareSuccess(true)
                                        }

                                        override fun onError(e: Throwable) {
                                            mainView?.setShareError(R.string.error_share_title)
                                        }
                                    })
                        }
                    }

                    override fun onError(e: Throwable) {
                        mainView?.setShareError(R.string.shared_data_error)
                    }
                })
    }

    override fun turnOffFamilyMode() {
        familyModeProvider.removeAllSharedData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : CompletableObserver {
                    override fun onSubscribe(d: Disposable) {

                    }

                    override fun onComplete() {
                        mainView?.setShareSuccess(false)
                    }

                    override fun onError(e: Throwable) {
                        mainView?.setShareError(R.string.error_share_title)
                    }
                })
    }
}