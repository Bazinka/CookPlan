package com.cookplan.providers.impl

import com.cookplan.models.Company
import com.cookplan.models.CookPlanError
import com.cookplan.providers.CompanyProvider
import com.cookplan.utils.DatabaseConstants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject
import java.util.*

/**
 * Created by DariaEfimova on 24.04.17.
 */

class CompanyProviderImpl : CompanyProvider {

    private val database: DatabaseReference

    private val subjectCompanyList: BehaviorSubject<List<Company>>

    init {
        this.database = FirebaseDatabase.getInstance().reference
        subjectCompanyList = BehaviorSubject.create()
        getFirebaseCompanyList()
    }

    private fun getFirebaseCompanyList() {
        val items = database.child(DatabaseConstants.DATABASE_COMPANY_TABLE)
        items.orderByChild(DatabaseConstants.DATABASE_USER_ID_FIELD)
                .equalTo(FirebaseAuth.getInstance().currentUser?.uid)
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        val companies = ArrayList<Company>()
                        for (itemSnapshot in dataSnapshot.children) {
                            val company = itemSnapshot.getValue(Company::class.java)
                            if (company != null) {
                                company.id = itemSnapshot.key
                                companies.add(company)
                            }
                        }
                        subjectCompanyList.onNext(companies)
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        subjectCompanyList.onError(CookPlanError(databaseError))
                    }
                })
    }

    override fun getUsersCompanyList(): Observable<List<Company>> {
        return subjectCompanyList.map { companyList ->
            val usersCompanies = mutableListOf<Company>()
            for (company in companyList) {
                if (company.userId == FirebaseAuth.getInstance().currentUser?.uid) {
                    usersCompanies.add(company)
                }
            }
            usersCompanies
        }
    }

    override fun getCompanyById(companyId: String): Single<Company> {
        return Single.create { emitter ->
            val items = database.child(DatabaseConstants.DATABASE_COMPANY_TABLE).child(companyId)
            items.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    var company = dataSnapshot.getValue(Company::class.java)
                    company.id = dataSnapshot.key
                    emitter?.onSuccess(company)
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    emitter?.onError(CookPlanError(databaseError))
                }
            })
        }
    }

    override fun createCompany(company: Company): Single<Company> {
        return Single.create { emitter ->
            val companyRef = database.child(DatabaseConstants.DATABASE_COMPANY_TABLE)
            companyRef.push().setValue(company) { databaseError, reference ->
                if (databaseError != null) {
                    emitter.onError(CookPlanError(databaseError))
                } else {
                    company.id = reference.key
                    emitter.onSuccess(company)
                }
            }
        }
    }

    override fun updateCompany(company: Company): Single<Company> {
        return Single.create { emitter ->
            val values = HashMap<String, Any>()
            values.put(DatabaseConstants.DATABASE_USER_ID_FIELD, company.userId)
            values.put(DatabaseConstants.DATABASE_NAME_FIELD, company.name)
            values.put(DatabaseConstants.DATABASE_COMMENT_FIELD, company.comment ?: String())
            values.put(DatabaseConstants.DATABASE_COMPANY_LATITUDE_FIELD, company.latitude)
            values.put(DatabaseConstants.DATABASE_COMPANY_LONGITUDE_FIELD, company.longitude)
            values.put(DatabaseConstants.DATABASE_ADDED_TO_GEOFENCE_FIELD, company.isAddedToGeoFence)
            val companyItemRef = database.child(DatabaseConstants.DATABASE_COMPANY_TABLE)
            companyItemRef.child(company.id).updateChildren(values) { databaseError, databaseReference ->
                if (databaseError != null) {
                    emitter?.onError(CookPlanError(databaseError))
                } else {
                    emitter?.onSuccess(company)
                }
            }
        }
    }

    override fun removeCompany(company: Company): Completable {
        return Completable.create { emitter ->
            if (company.id != null) {
                val companyItemRef = database.child(DatabaseConstants.DATABASE_COMPANY_TABLE)
                val ref = companyItemRef.child(company.id)
                ref.removeValue()
                        .addOnFailureListener { exeption -> emitter.onError(CookPlanError(exeption.message)) }
                        .addOnCompleteListener { task ->
                            if (task.isComplete) {
                                emitter.onComplete()
                            }
                        }
            }
        }
    }
}
