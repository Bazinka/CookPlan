package com.cookplan.providers.impl;

import android.app.Activity;
import android.net.Uri;

import com.cookplan.providers.ImageProvider;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.UUID;

import io.reactivex.Completable;
import io.reactivex.Single;

/**
 * Created by DariaEfimova on 13.06.17.
 */

public class ImageProviderImpl implements ImageProvider {

    private Activity context;

    public ImageProviderImpl(Activity context) {
        this.context = context;
    }

    @Override
    public Single<String> saveImage(Uri uri) {
        return Single.create(emitter -> {
            String uuid = UUID.randomUUID().toString();
            StorageReference imageRef = FirebaseStorage.getInstance().getReference(uuid);
            imageRef.putFile(uri)
                    .addOnSuccessListener(context, taskSnapshot -> {
                        emitter.onSuccess(uuid);
                    })
                    .addOnFailureListener(context, emitter::onError);
        });
    }

    @Override
    public Completable removeImage(String imageId) {
        return Completable.create(emitter -> {
            StorageReference imageRef = FirebaseStorage.getInstance().getReference(imageId);
            imageRef.delete()
                    .addOnSuccessListener(aVoid -> {
                        emitter.onComplete();
                    })
                    .addOnFailureListener(emitter::onError);
        });
    }
}
