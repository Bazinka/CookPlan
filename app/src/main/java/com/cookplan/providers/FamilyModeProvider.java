package com.cookplan.providers;

import com.cookplan.models.ShareUserInfo;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;

/**
 * Created by DariaEfimova on 24.04.17.
 */

public interface FamilyModeProvider {

    Observable<List<ShareUserInfo>> getDataSharedByMe();

    Single<ShareUserInfo> createDataSharedItem(ShareUserInfo dataSharedItem);

    Completable removeDataSharedItem(ShareUserInfo dataSharedItem);
}