package com.cookplan.images

/**
 * Created by DariaEfimova on 13.06.17.
 */

interface ChangeImagesView {

    fun setError(error: String)

    fun setImageSaved(imageId: String)

    fun setImageRemoved(imageId: String)
}
