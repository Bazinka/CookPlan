package com.cookplan.providers.impl;

import com.cookplan.R;
import com.cookplan.RApplication;
import com.cookplan.models.Company;
import com.cookplan.models.CookPlanError;
import com.cookplan.providers.CompanyProvider;
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
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.subjects.BehaviorSubject;

/**
 * Created by DariaEfimova on 24.04.17.
 */

public class CompanyProviderImpl implements CompanyProvider {

    private DatabaseReference database;

    private BehaviorSubject<List<Company>> subjectCompanyList;

    public CompanyProviderImpl() {
        this.database = FirebaseDatabase.getInstance().getReference();
        subjectCompanyList = BehaviorSubject.create();
        getFirebaseCompanyList();
    }

    private void getFirebaseCompanyList() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String myUid = user.getUid();
            Query items = database.child(DatabaseConstants.DATABASE_COMPANY_TABLE);
            items.orderByChild(DatabaseConstants.DATABASE_USER_ID_FIELD)
                    .equalTo(myUid)
                    .addValueEventListener(new ValueEventListener() {
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            List<Company> companies = new ArrayList<>();
                            for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()) {
                                Company company = itemSnapshot.getValue(Company.class);
                                if (company != null) {
                                    company.setId(itemSnapshot.getKey());
                                    companies.add(company);
                                }
                            }
                            if (subjectCompanyList != null) {
                                subjectCompanyList.onNext(companies);
                            }
                        }

                        public void onCancelled(DatabaseError databaseError) {
                            if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                                if (subjectCompanyList != null) {
                                    subjectCompanyList.onError(new CookPlanError(databaseError));
                                }
                            }
                        }
                    });
        }
    }

    @Override
    public Observable<List<Company>> getUsersCompanyList() {
        return subjectCompanyList.map(companyList -> {
            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            List<Company> usersCompanies = new ArrayList<>();
            for (Company company : companyList) {
                if (company.getUserId().equals(uid)) {
                    usersCompanies.add(company);
                }
            }
            return usersCompanies;
        });
    }

    @Override
    public Single<Company> createCompany(Company company) {
        return Single.create(emitter -> {
            DatabaseReference companyRef = database.child(DatabaseConstants.DATABASE_COMPANY_TABLE);
            companyRef.push().setValue(company, (databaseError, reference) -> {
                if (databaseError != null) {
                    emitter.onError(new CookPlanError(databaseError));
                } else {
                    company.setId(reference.getKey());
                    emitter.onSuccess(company);
                }
            });
        });
    }

    @Override
    public Single<Company> updateCompany(Company company) {
        return Single.create(emitter -> {
            Map<String, Object> values = new HashMap<>();
            values.put(DatabaseConstants.DATABASE_USER_ID_FIELD, company.getUserId());
            values.put(DatabaseConstants.DATABASE_NAME_FIELD, company.getName());
            values.put(DatabaseConstants.DATABASE_COMMENT_FIELD, company.getComment());
            values.put(DatabaseConstants.DATABASE_COMPANY_LATITUDE_FIELD, company.getLatitude());
            values.put(DatabaseConstants.DATABASE_COMPANY_LONGITUDE_FIELD, company.getLongitude());
            values.put(DatabaseConstants.DATABASE_ADDED_TO_GEOFENCE_FIELD, company.isAddedToGeoFence());
            DatabaseReference todoItemRef = database.child(DatabaseConstants.DATABASE_COMPANY_TABLE);
            todoItemRef.child(company.getId()).updateChildren(values, (databaseError, databaseReference) -> {
                if (databaseError != null) {
                    if (emitter != null) {
                        emitter.onError(new CookPlanError(databaseError));
                    }
                } else {
                    if (emitter != null) {
                        emitter.onSuccess(company);
                    }
                }
            });
        });
    }

    @Override
    public Completable removeCompany(Company company) {
        return Completable.create(emitter -> {
            if (company != null && company.getId() != null) {
                DatabaseReference todoItemRef = database.child(DatabaseConstants.DATABASE_TO_DO_ITEMS_TABLE);
                DatabaseReference ref = todoItemRef.child(company.getId());
                ref.removeValue()
                        .addOnFailureListener(exeption -> emitter.onError(new CookPlanError(exeption.getMessage())))
                        .addOnCompleteListener(task -> {
                            if (task.isComplete()) {
                                emitter.onComplete();
                            }
                        });
            } else {
                emitter.onError(new CookPlanError(RApplication.getAppContext().getString(R.string.error_remove_todo_item)));
            }
        });
    }
}
