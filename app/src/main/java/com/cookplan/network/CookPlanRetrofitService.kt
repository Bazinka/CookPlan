package com.cookplan.network

import com.cookplan.models.network.GoogleSearchResponce

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface CookPlanRetrofitService {

    @GET(NetworkUtils.GET_MAIN_URL)
    fun getRecipes(@Query("q") query: String,
                   @Query("start") start: Int,
                   @Query("number") number: Int): Observable<GoogleSearchResponce>
}
