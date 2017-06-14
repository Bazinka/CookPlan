package com.cookplan.upload_image;

/**
 * Created by DariaEfimova on 13.06.17.
 */

public interface UploadImageView {
    void setError(String error);

    void setImageSaved(String url);

    void setImageRemoved(String imageId);
}
