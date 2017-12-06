package com.cookplan.providers

import android.net.Uri
import io.reactivex.Completable
import io.reactivex.Single

/**
 * Created by DariaEfimova on 24.04.17.
 */

interface ImageProvider {

//    fun getInfoSharedToMe(): Observable<List<ShareUserInfo>>
//
//    fun getDataSharedByMe(): Maybe<ShareUserInfo>

    fun saveImage(uri: Uri): Single<String>

    fun removeImage(imageId: String): Completable
}
