package com.cookplan.companies.edit;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import com.cookplan.R;
import com.cookplan.models.Company;
import com.cookplan.models.CookPlanError;
import com.cookplan.providers.CompanyProvider;
import com.cookplan.providers.ProviderFactory;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.joda.time.LocalDateTime;

import java.util.Locale;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by DariaEfimova on 19.10.16.
 */

public class EditCompanyPresenterImpl implements EditCompanyPresenter {

    private EditCompanyView mainView;
    private Context context;
    private CompanyProvider dataProvider;

    public EditCompanyPresenterImpl(Context contex, EditCompanyView mainView) {
        this.context = contex;
        this.mainView = mainView;
        dataProvider = ProviderFactory.Companion.getCompanyProvider();
    }

    @Override
    public String getDefaultName() {
        LocalDateTime currentDate = new LocalDateTime();
        String defaultName = context.getString(R.string.default_place_name)
                + " " + currentDate.dayOfWeek().getAsText(Locale.getDefault())
                + ", " + currentDate.toString("dd.MM.yyyy HH:mm");
        if (context instanceof AppCompatActivity) {
            String name = ((AppCompatActivity) context).getIntent().getStringExtra(
                    EditCompanyActivity.PLACE_NAME_KEY);
            if (name != null) {
                defaultName = name;
            }
        }
        return defaultName;
    }

    @Override
    public void savePoint(String name, String comments, double latitude, double longitude) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null && name != null && !name.isEmpty()) {
            String userId = user.getUid();
            Company company = new Company(userId, name, comments, latitude, longitude, false);
            dataProvider.createCompany(company)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new SingleObserver<Company>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onSuccess(Company company) {
                            if (mainView != null) {
                                mainView.setSuccessSavePoint();
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            if (mainView != null && e instanceof CookPlanError) {
                                mainView.setErrorToast(e.getMessage());
                            }
                        }
                    });
        }
    }
}
