package com.cookplan.upload_image

import android.app.Activity
import android.net.Uri
import android.support.v4.content.FileProvider
import com.cookplan.providers.ImageProvider
import com.cookplan.providers.ProviderFactory
import com.cookplan.utils.Utils
import io.reactivex.CompletableObserver
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.io.File

/**
 * Created by DariaEfimova on 13.06.17.
 */

class UploadImagePresenterImpl(private val mainView: UploadImageView?, private val context: Activity) : UploadImagePresenter {
    private val imageProvider: ImageProvider? = ProviderFactory.Companion.imageProvider
    private val countPhotos: Int = 0

    override fun getOutputImagePath(): Uri {
        val file = File(context.getExternalFilesDir(
                android.os.Environment.DIRECTORY_PICTURES).absolutePath
                + File.separator
                + "photo.jpg")
        return FileProvider.getUriForFile(
                context,
                context.applicationContext.packageName + ".provider",
                file)
    }

    override fun uploadPhoto() {
        imageProvider?.saveImage(getOutputImagePath(), context)
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe(object : SingleObserver<String> {

                    override fun onSubscribe(d: Disposable) {}

                    override fun onSuccess(uuid: String) {
                        mainView?.setImageSaved(uuid)
                    }

                    override fun onError(e: Throwable) {
                        mainView?.setError(e.message)
                    }
                })
    }

    override fun removePhoto(imageId: String) {
        if (!Utils.isStringUrl(imageId)) {
            imageProvider?.removeImage(imageId)
                    ?.subscribeOn(Schedulers.io())
                    ?.observeOn(AndroidSchedulers.mainThread())
                    ?.subscribe(object : CompletableObserver {
                        override fun onSubscribe(d: Disposable) {

                        }

                        override fun onComplete() {
                            mainView?.setImageRemoved(imageId)
                        }

                        override fun onError(e: Throwable) {
                            mainView?.setError(e.message)
                        }
                    })
        } else {
            mainView?.setImageRemoved(imageId)
        }
    }
}
