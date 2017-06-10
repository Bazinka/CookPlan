package com.cookplan.network;

import com.cookplan.models.network.GoogleRecipe;
import com.cookplan.models.network.GoogleSearchResponce;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface CookPlanRetrofitService {

    @GET(NetworkUtils.GET_MAIN_URL)
    Observable<GoogleSearchResponce> getRecipes(@Query("q") String query);
}
