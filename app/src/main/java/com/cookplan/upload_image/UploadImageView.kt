package com.cookplan.upload_image

/**
 * Created by DariaEfimova on 13.06.17.
 */

interface UploadImageView {
    
    fun setError(error: String)

    fun setImageSaved(url: String)

    fun setImageRemoved(imageId: String)
}
