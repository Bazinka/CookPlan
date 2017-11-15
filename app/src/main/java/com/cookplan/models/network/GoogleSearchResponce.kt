package com.cookplan.models.network

import com.google.gson.annotations.SerializedName

/**
 * Created by DariaEfimova on 10.06.17.
 */

data class GoogleSearchResponce(@SerializedName("items") var items: List<GoogleRecipe>?)
