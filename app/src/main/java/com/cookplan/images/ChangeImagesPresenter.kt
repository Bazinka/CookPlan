package com.cookplan.images

import android.net.Uri

/**
 * Created by DariaEfimova on 13.06.17.
 */

interface ChangeImagesPresenter {

    fun getOutputImagePath(): Uri

    fun uploadPhoto()

    fun removePhoto(imageId: String)
}
