package com.cookplan.providers.impl

import com.cookplan.models.CookPlanError
import com.cookplan.models.ShareUserInfo
import com.cookplan.providers.FamilyModeProvider
import com.cookplan.utils.DatabaseConstants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import io.reactivex.*
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import java.util.*

/**
 * Created by DariaEfimova on 01.05.17.
 */

class FamilyModeProviderImpl : FamilyModeProvider {

    private val database: DatabaseReference

    private val subjectShareUserList: BehaviorSubject<List<ShareUserInfo>>

    init {
        this.database = FirebaseDatabase.getInstance().reference
        subjectShareUserList = BehaviorSubject.create()
        getFirebaseAllSharedInfo()
    }

    private fun getFirebaseAllSharedInfo() {
        val sharedItems = database.child(DatabaseConstants.DATABASE_SHARE_TO_GOOGLE_USER_TABLE)
        sharedItems.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val sharedInfoList = ArrayList<ShareUserInfo>()
                for (itemSnapshot in dataSnapshot.children) {
                    val userInfo = itemSnapshot.getValue(ShareUserInfo::class.java)
                    userInfo?.id = itemSnapshot.key
                    sharedInfoList.add(userInfo!!)
                }
                subjectShareUserList.onNext(sharedInfoList)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                subjectShareUserList.onError(CookPlanError(databaseError))
            }
        })
    }

    override fun getInfoSharedToMe(): Observable<List<ShareUserInfo>> {
        return subjectShareUserList.map { allSharedInfoList ->
            val sharedInfoList = mutableListOf<ShareUserInfo>()
            for (userInfo in allSharedInfoList) {
                if (userInfo.clientUserEmailList.contains(FirebaseAuth.getInstance().currentUser?.email))
                    sharedInfoList.add(userInfo)
            }
            sharedInfoList
        }
    }

    override fun getDataSharedByMe(): Maybe<ShareUserInfo> {
        return Maybe.create { emitter ->
            if (!(FirebaseAuth.getInstance().currentUser?.isAnonymous ?: true)) {
                val myUid = FirebaseAuth.getInstance().currentUser?.uid
                val sharedItems = database.child(DatabaseConstants.DATABASE_SHARE_TO_GOOGLE_USER_TABLE)
                        .orderByChild(DatabaseConstants.DATABASE_OWNER_USER_ID_FIELD)
                        .equalTo(myUid)
                sharedItems.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        var shareUserInfo: ShareUserInfo? = null
                        if (dataSnapshot.childrenCount == 1L) {
                            for (itemSnapshot in dataSnapshot.children) {
                                shareUserInfo = itemSnapshot.getValue(ShareUserInfo::class.java)
                                shareUserInfo?.id = itemSnapshot.key
                            }
                            emitter.onSuccess(shareUserInfo)
                        } else {
                            emitter.onComplete()
                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        emitter.onError(CookPlanError(databaseError))
                    }
                })
            } else {
                emitter.onComplete()
            }
        }
    }

    override fun createDataSharedItem(dataSharedItem: ShareUserInfo): Single<ShareUserInfo> {
        return Single.create { emitter ->
            val userShareRef = database.child(DatabaseConstants.DATABASE_SHARE_TO_GOOGLE_USER_TABLE)
            userShareRef.push().setValue(dataSharedItem) { databaseError, reference ->
                if (databaseError != null) {
                    emitter.onError(CookPlanError(databaseError))
                } else {
                    dataSharedItem.id = reference.key
                    emitter.onSuccess(dataSharedItem)
                }
            }
        }
    }

    override fun updateDataSharedItem(dataSharedItem: ShareUserInfo): Single<ShareUserInfo> {
        return Single.create { emitter ->
            val userShareRef = database.child(DatabaseConstants.DATABASE_SHARE_TO_GOOGLE_USER_TABLE)
            val values = HashMap<String, Any>()
            values.put(DatabaseConstants.DATABASE_SHARED_OWNER_ID_FIELD, dataSharedItem.ownerUserId)
            values.put(DatabaseConstants.DATABASE_SHARED_OWNER_NAME_FIELD, dataSharedItem.ownerUserName)
            values.put(DatabaseConstants.DATABASE_SHARED_CLIENT_EMAILS_FIELD, dataSharedItem.clientUserEmailList)

            userShareRef
                    .child(dataSharedItem.id!!)
                    .updateChildren(values
                    ) { databaseError, reference ->
                        if (databaseError != null) {
                            emitter.onError(CookPlanError(databaseError))
                        } else {
                            emitter.onSuccess(dataSharedItem)
                        }
                    }
        }
    }

    override fun removeAllSharedData(): Completable {
        return Completable.create { emitter ->
            getDataSharedByMe()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : MaybeObserver<ShareUserInfo> {
                        override fun onSubscribe(d: Disposable) {}

                        override fun onSuccess(shareUserInfo: ShareUserInfo) {
                            val userShareRef = database.child(DatabaseConstants.DATABASE_SHARE_TO_GOOGLE_USER_TABLE)
                            val ref = userShareRef.child(shareUserInfo.id!!)
                            ref.removeValue()
                                    .addOnFailureListener { exeption -> emitter.onError(CookPlanError(exeption.message)) }
                                    .addOnCompleteListener { task ->
                                        if (task.isComplete) {
                                            emitter.onComplete()
                                        }
                                    }
                        }

                        override fun onError(e: Throwable) {

                        }

                        override fun onComplete() {

                        }
                    })
        }
    }
}
