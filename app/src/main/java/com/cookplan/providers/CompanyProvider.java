package com.cookplan.providers;

import com.cookplan.models.Company;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;

/**
 * Created by DariaEfimova on 24.04.17.
 */

public interface CompanyProvider {

    Observable<List<Company>> getUsersCompanyList();

    Single<Company> createCompany(Company company);

    Single<Company> updateCompany(Company company);

    Completable removeCompany(Company company);
}
