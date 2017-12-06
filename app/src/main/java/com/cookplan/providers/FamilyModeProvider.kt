package com.cookplan.providers

import com.cookplan.models.ShareUserInfo

import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single

/**
 * Created by DariaEfimova on 24.04.17.
 */

interface FamilyModeProvider {

    fun getInfoSharedToMe(): Observable<List<ShareUserInfo>>

    fun getDataSharedByMe(): Maybe<ShareUserInfo>

    fun createDataSharedItem(dataSharedItem: ShareUserInfo): Single<ShareUserInfo>

    fun updateDataSharedItem(dataSharedItem: ShareUserInfo): Single<ShareUserInfo>

    fun removeAllSharedData(): Completable
}
