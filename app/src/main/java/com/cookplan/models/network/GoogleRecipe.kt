package com.cookplan.models.network

import com.google.gson.annotations.SerializedName

/**
 * Created by DariaEfimova on 10.06.17.
 */

data class GoogleRecipe(@SerializedName("title") val title: String?,
                        @SerializedName("link") val url: String?,
                        @SerializedName("formattedUrl") val formattedUrl: String?,
                        @SerializedName("snippet") val desc: String?) {


    @SerializedName("pagemap")
    private val pagemap: PageMap? = null

    val imageUrl: String?
        get() = pagemap?.getImage()?.src

    private class PageMap {

        @SerializedName("cse_image")
        private val image: List<CseImage>? = null

        fun getImage(): CseImage? {
            return image?.get(0)
        }
    }


    private class CseImage {
        @SerializedName("src")
        val src: String? = null
    }
}
