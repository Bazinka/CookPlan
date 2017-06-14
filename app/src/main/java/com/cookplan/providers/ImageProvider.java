package com.cookplan.providers;

import android.net.Uri;

import io.reactivex.Completable;
import io.reactivex.Single;

/**
 * Created by DariaEfimova on 24.04.17.
 */

public interface ImageProvider {

    //    Observable<List<ShareUserInfo>> getInfoSharedToMe();

    //    Maybe<ShareUserInfo> getDataSharedByMe();

    Single<String> saveImage(Uri uri);

    Completable removeImage(String imageId);
}
