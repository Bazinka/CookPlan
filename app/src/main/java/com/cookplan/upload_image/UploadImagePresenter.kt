package com.cookplan.upload_image

import android.net.Uri

/**
 * Created by DariaEfimova on 13.06.17.
 */

interface UploadImagePresenter {

    fun getOutputImagePath(): Uri

    fun uploadPhoto()

    fun removePhoto(imageId: String)
}
