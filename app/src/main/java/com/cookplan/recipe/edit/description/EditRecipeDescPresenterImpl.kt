package com.cookplan.recipe.edit.description

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Rect
import android.net.Uri
import android.os.Environment
import android.support.v4.content.FileProvider
import com.cookplan.R
import com.cookplan.utils.Utils
import com.googlecode.tesseract.android.TessBaseAPI
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

/**
 * Created by DariaEfimova on 16.03.17.
 */

class EditRecipeDescPresenterImpl(private val mainView: EditRecipeDescView?, private val context: Context) : EditRecipeDescPresenter {

    private var tessBaseApi: TessBaseAPI? = null

    override fun getOutputImagePath(): Uri? {
        var outputUri: Uri? = null
        try {
            val file = File(context.applicationContext.getExternalFilesDir(
                    android.os.Environment.DIRECTORY_PICTURES).absolutePath + File.separator + "ocr.jpg")
            outputUri = FileProvider.getUriForFile(context,
                    context.applicationContext.packageName + ".provider",
                    file)
        } catch (e: Exception) {
            e.printStackTrace()
            mainView?.setErrorToSnackBar(context.getString(R.string.text_wasnt_recognize))
        } finally {
            return outputUri
        }
    }

    /**
     * Prepare directory on external storage
     *
     * @param path
     * @throws Exception
     */
    private fun prepareDirectory(path: String) {

        val dir = File(path)
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                Utils.log(tag, "ERROR: Creation of directory $path failed, check does Android Manifest have permission to write to external storage.")
            }
        } else {
            Utils.log(tag, "Created directory " + path)
        }
    }


    private fun prepareTesseract() {
        try {
            prepareDirectory(DATA_PATH + TESSDATA)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        copyTessDataFiles(TESSDATA)
    }

    /**
     * Copy tessdata files (located on assets/tessdata) to destination directory
     *
     * @param path - name of directory with .traineddata files
     */
    private fun copyTessDataFiles(path: String) {
        try {
            val fileList = context.assets.list(path)

            for (fileName in fileList) {

                // open file within the assets folder
                // if it is not already there copy it to the sdcard
                val pathToDataFile = DATA_PATH + path + "/" + fileName
                if (!File(pathToDataFile).exists()) {

                    val input = context.assets.open(path + "/" + fileName)

                    val out = FileOutputStream(pathToDataFile)

                    // Transfer bytes from in to out
                    val buf = ByteArray(1024)
                    var len: Int = input.read(buf)
                    while (len > 0) {
                        out.write(buf, 0, len)
                        len = input.read(buf)
                    }
                    input.close()
                    out.close()

                    Utils.log(tag, "Copied " + fileName + "to tessdata")
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
            mainView?.setErrorToSnackBar(context.getString(R.string.unable_to_copy_files) + e.toString())
        }

    }


    /**
     * don't run this code in main thread - it stops UI thread. Create AsyncTask instead.
     * http://developer.android.com/intl/ru/reference/android/os/AsyncTask.html
     *
     * @param imgUri
     */
    private fun startOCR(imgUri: Uri?, language: String) {
        try {
            val options = BitmapFactory.Options()
            options.inSampleSize = 4 // 1 - means max size. 4 - means maxsize/4 size. Don't use value <4, because you need more memory in the heap to store your data.
            val inStream = context.contentResolver.openInputStream(imgUri)
            val bitmap = BitmapFactory.decodeStream(inStream, Rect(0, 0, 0, 0), options)

            val result = extractText(bitmap, language)

            mainView?.setAsyncTextResult(result)

        } catch (e: Exception) {
            e.printStackTrace()
            mainView?.setAsyncErrorToSnackBar(context.getString(R.string.text_wasnt_recognize))
        }

    }

    private fun extractText(bitmap: Bitmap, language: String): String {
        try {
            tessBaseApi = TessBaseAPI()
        } catch (e: Exception) {
            if (tessBaseApi == null) {
                Utils.log(tag, "TessBaseAPI is null. TessFactory not returning tess object.")
            }
            e.printStackTrace()
            mainView?.setErrorToSnackBar(context.getString(R.string.text_wasnt_recognize))
        }

        tessBaseApi?.pageSegMode = TessBaseAPI.PageSegMode.PSM_AUTO_OSD
        tessBaseApi?.init(DATA_PATH, language)

        //blackList Example
        tessBaseApi?.setVariable(TessBaseAPI.VAR_CHAR_BLACKLIST, "!@#$%^&*_+=-[]}{" + "'\"\\|~`")

        Utils.log(tag, "Training file loaded")
        tessBaseApi?.setImage(bitmap)
        var extractedText = "empty result"
        try {
            extractedText = tessBaseApi?.utF8Text ?: extractedText
        } catch (e: Exception) {
            e.printStackTrace()
            Utils.log(tag, "Error in recognizing text.")
        }

        tessBaseApi?.end()
        return extractedText
    }

    override fun doOCR(language: String) {
        Thread {
            prepareTesseract()
            startOCR(getOutputImagePath(), language)
        }.start()
    }

    companion object {
        private val tag = EditRecipeDescPresenterImpl::class.java.simpleName
        private val DATA_PATH = Environment.getExternalStorageDirectory().toString() + "/CookPlanImages/"
        private val TESSDATA = "tessdata"
    }
}
