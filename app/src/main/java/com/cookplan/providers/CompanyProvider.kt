package com.cookplan.providers

import com.cookplan.models.Company

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

/**
 * Created by DariaEfimova on 24.04.17.
 */

interface CompanyProvider {

    fun getUsersCompanyList(): Observable<List<Company>>

    fun getCompanyById(companyId: String): Single<Company>

    fun createCompany(company: Company): Single<Company>

    fun updateCompany(company: Company): Single<Company>

    fun removeCompany(company: Company): Completable
}
