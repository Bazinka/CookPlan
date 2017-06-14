package com.cookplan.upload_image;

import android.app.Activity;
import android.net.Uri;
import android.support.v4.content.FileProvider;

import com.cookplan.R;
import com.cookplan.RApplication;
import com.cookplan.providers.ImageProvider;
import com.cookplan.providers.impl.ImageProviderImpl;

import java.io.File;

import io.reactivex.CompletableObserver;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by DariaEfimova on 13.06.17.
 */

public class UploadImagePresenterImpl implements UploadImagePresenter {
    private UploadImageView mainView;
    private ImageProvider imageProvider;
    private int countPhotos;

    public UploadImagePresenterImpl(UploadImageView mainView, Activity context) {
        this.mainView = mainView;
        countPhotos = 0;
        imageProvider = new ImageProviderImpl(context);
    }

    @Override
    public Uri getOutputImagePath() {
        Uri outputFileUri = null;
        try {
            File file = new File(RApplication.getAppContext().getExternalFilesDir(
                    android.os.Environment.DIRECTORY_PICTURES).getAbsolutePath()
                                         + File.separator
                                         + "photo.jpg");
            outputFileUri = FileProvider.getUriForFile(
                    RApplication.getAppContext(),
                    RApplication.getAppContext().getApplicationContext().getPackageName() + ".provider",
                    file);
        } catch (Exception e) {
            e.printStackTrace();
            if (mainView != null) {
                mainView.setError(RApplication.getAppContext().getString(R.string.text_wasnt_recognize));
            }
        }
        return outputFileUri;
    }

    @Override
    public void uploadPhoto(Uri selectedImage) {
        if (imageProvider != null) {
            imageProvider.saveImage(selectedImage)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new SingleObserver<String>() {

                        @Override
                        public void onSubscribe(Disposable d) {
                        }

                        @Override
                        public void onSuccess(String uuid) {
                            if (mainView != null) {
                                mainView.setImageSaved(uuid);
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            if (mainView != null) {
                                mainView.setError(e.getMessage());
                            }
                        }
                    });
        }
    }

    @Override
    public void removePhoto(String imageId) {
        if (imageId != null && !imageId.contains("http") && imageProvider != null) {
            imageProvider.removeImage(imageId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new CompletableObserver() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onComplete() {
                            if (mainView != null) {
                                mainView.setImageRemoved(imageId);
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            if (mainView != null) {
                                mainView.setError(e.getMessage());
                            }
                        }
                    });
        } else {
            if (mainView != null) {
                mainView.setImageRemoved(imageId);
            }
        }
    }
}
