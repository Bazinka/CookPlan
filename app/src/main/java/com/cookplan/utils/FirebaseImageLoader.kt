package com.cookplan.utils

import android.util.Log

import com.bumptech.glide.Priority
import com.bumptech.glide.load.data.DataFetcher
import com.bumptech.glide.load.model.stream.StreamModelLoader
import com.google.android.gms.tasks.Tasks
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.StreamDownloadTask

import java.io.IOException
import java.io.InputStream

/**
 * ModelLoader implementation to download images from FirebaseStorage with Glide.
 *
 * Sample Usage:
 * <pre>
 * StorageReference ref = FirebaseStorage.getInstance().getReference().child("myimage");
 * ImageView iv = (ImageView) findViewById(R.id.my_image_view);
 *
 * Glide.with(this)
 * .using(new FirebaseImageLoader())
 * .load(ref)
 * .into(iv);
</pre> *
 */
class FirebaseImageLoader : StreamModelLoader<StorageReference> {

    override fun getResourceFetcher(model: StorageReference, width: Int, height: Int): DataFetcher<InputStream> {
        return FirebaseStorageFetcher(model)
    }

    private inner class FirebaseStorageFetcher internal constructor(private val mRef: StorageReference) : DataFetcher<InputStream> {
        private var mInputStream: InputStream? = null

        @Throws(Exception::class)
        override fun loadData(priority: Priority): InputStream? {
            mInputStream = Tasks.await<StreamDownloadTask.TaskSnapshot>(mRef.stream).stream

            return mInputStream
        }

        override fun cleanup() {
            // Close stream if possible
            if (mInputStream != null) {
                try {
                    mInputStream?.close()
                    mInputStream = null
                } catch (e: IOException) {
                    Log.w(TAG, "Could not close stream", e)
                }

            }
        }

        override fun getId(): String {
            return mRef.path
        }

        override fun cancel() {
            // Cancel task if possible
            if (mRef.stream.isInProgress) {
                mRef.stream.cancel()
            }
        }
    }

    companion object {

        private val TAG = "FirebaseImageLoader"
    }
}
