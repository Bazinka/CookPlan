package com.cookplan.upload_image;

import android.net.Uri;

/**
 * Created by DariaEfimova on 13.06.17.
 */

public interface UploadImagePresenter {
    Uri getOutputImagePath();

    void uploadPhoto(Uri selectedImage);

    void removePhoto(String imageId);
}
