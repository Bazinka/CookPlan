package com.cookplan.providers.impl

import android.app.Activity
import android.net.Uri
import com.cookplan.providers.ImageProvider
import com.google.firebase.storage.FirebaseStorage
import io.reactivex.Completable
import io.reactivex.Single
import java.util.*

/**
 * Created by DariaEfimova on 13.06.17.
 */

class ImageProviderImpl(private val context: Activity) : ImageProvider {

    override fun saveImage(uri: Uri): Single<String> {
        return Single.create { emitter ->
            val uuid = UUID.randomUUID().toString()
            val imageRef = FirebaseStorage.getInstance().getReference(uuid)
            imageRef.putFile(uri)
                    .addOnSuccessListener(context) { taskSnapshot -> emitter.onSuccess(uuid) }
                    .addOnFailureListener(context, { emitter.onError(it) })
        }
    }

    override fun removeImage(imageId: String): Completable {
        return Completable.create { emitter ->
            val imageRef = FirebaseStorage.getInstance().getReference(imageId)
            imageRef.delete()
                    .addOnSuccessListener { aVoid -> emitter.onComplete() }
                    .addOnFailureListener({ emitter.onError(it) })
        }
    }
}
