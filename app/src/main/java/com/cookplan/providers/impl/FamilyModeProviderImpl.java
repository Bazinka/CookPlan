package com.cookplan.providers.impl;

import com.cookplan.R;
import com.cookplan.RApplication;
import com.cookplan.models.CookPlanError;
import com.cookplan.models.ShareUserInfo;
import com.cookplan.providers.FamilyModeProvider;
import com.cookplan.utils.DatabaseConstants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.subjects.BehaviorSubject;

/**
 * Created by DariaEfimova on 01.05.17.
 */

public class FamilyModeProviderImpl implements FamilyModeProvider {

    private DatabaseReference database;


    private BehaviorSubject<List<ShareUserInfo>> subjectShareUserList;

    public FamilyModeProviderImpl() {
        this.database = FirebaseDatabase.getInstance().getReference();
        subjectShareUserList = BehaviorSubject.create();
        getFirebaseAllSharedInfo();
    }

    private void getFirebaseAllSharedInfo() {
        Query sharedItems = database.child(DatabaseConstants.DATABASE_SHARE_TO_GOOGLE_USER_TABLE);
        sharedItems.addValueEventListener(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<ShareUserInfo> sharedInfoList = new ArrayList<>();
                if (dataSnapshot.getValue() != null) {
                    for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()) {
                        ShareUserInfo userInfo = itemSnapshot.getValue(ShareUserInfo.class);
                        userInfo.setId(itemSnapshot.getKey());
                        sharedInfoList.add(userInfo);
                    }
                }
                if (subjectShareUserList != null) {
                    subjectShareUserList.onNext(sharedInfoList);
                }
            }

            public void onCancelled(DatabaseError databaseError) {
                if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                    if (subjectShareUserList != null) {
                        subjectShareUserList.onError(new CookPlanError(databaseError));
                    }
                }
            }
        });
    }

    @Override
    public Observable<List<ShareUserInfo>> getInfoSharedToMe() {
        return subjectShareUserList.map(allSharedInfoList -> {
            String mineEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
            List<ShareUserInfo> sharedInfoList = new ArrayList<>();
            for (ShareUserInfo userInfo : allSharedInfoList) {
                if (userInfo.getClientUserEmailList().contains(mineEmail))
                    sharedInfoList.add(userInfo);
            }
            return sharedInfoList;
        });
    }

    @Override
    public Maybe<ShareUserInfo> getDataSharedByMe() {
        return Maybe.create(emitter -> {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null && !user.isAnonymous()) {
                String myUid = user.getUid();
                DatabaseReference database1 = FirebaseDatabase.getInstance().getReference();
                Query sharedItems = database1.child(DatabaseConstants.DATABASE_SHARE_TO_GOOGLE_USER_TABLE)
                        .orderByChild(DatabaseConstants.DATABASE_OWNER_USER_ID_FIELD)
                        .equalTo(myUid);
                sharedItems.addListenerForSingleValueEvent(new ValueEventListener() {
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        ShareUserInfo shareUserInfo = null;
                        if (dataSnapshot.getChildrenCount() == 1) {
                            for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()) {
                                shareUserInfo = itemSnapshot.getValue(ShareUserInfo.class);
                                shareUserInfo.setId(itemSnapshot.getKey());
                            }
                            emitter.onSuccess(shareUserInfo);
                        } else {
                            emitter.onComplete();
                        }
                    }

                    public void onCancelled(DatabaseError databaseError) {
                        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                            emitter.onError(new CookPlanError(databaseError));
                        }
                    }
                });
            } else {
                emitter.onComplete();
            }
        });
    }

    @Override
    public Single<ShareUserInfo> createDataSharedItem(ShareUserInfo dataSharedItem) {
        return Single.create(emitter -> {
            DatabaseReference userShareRef = database.child(DatabaseConstants.DATABASE_SHARE_TO_GOOGLE_USER_TABLE);
            userShareRef.push().setValue(dataSharedItem,
                                         (databaseError, reference) -> {
                                             if (databaseError != null) {
                                                 emitter.onError(new CookPlanError(
                                                         RApplication.getAppContext().getString(R.string.error_share_title)));
                                             } else {
                                                 dataSharedItem.setId(reference.getKey());
                                                 emitter.onSuccess(dataSharedItem);
                                             }
                                         });
        });
    }

    @Override
    public Single<ShareUserInfo> updateDataSharedItem(ShareUserInfo dataSharedItem) {
        return Single.create(emitter -> {
            DatabaseReference userShareRef = database.child(DatabaseConstants.DATABASE_SHARE_TO_GOOGLE_USER_TABLE);
            Map<String, Object> values = new HashMap<>();
            values.put(DatabaseConstants.DATABASE_SHARED_OWNER_ID_FIELD, dataSharedItem.getOwnerUserId());
            values.put(DatabaseConstants.DATABASE_SHARED_OWNER_NAME_FIELD, dataSharedItem.getOwnerUserName());
            values.put(DatabaseConstants.DATABASE_SHARED_CLIENT_EMAILS_FIELD, dataSharedItem.getClientUserEmailList());

            userShareRef
                    .child(dataSharedItem.getId())
                    .updateChildren(values,
                                    (databaseError, reference) -> {
                                        if (databaseError != null) {
                                            emitter.onError(new CookPlanError(
                                                    RApplication.getAppContext().getString(R.string.error_share_title)));
                                        } else {
                                            emitter.onSuccess(dataSharedItem);
                                        }
                                    });
        });
    }

    @Override
    public Completable removeDataSharedItem(ShareUserInfo dataSharedItem) {
        return null;
    }
}
