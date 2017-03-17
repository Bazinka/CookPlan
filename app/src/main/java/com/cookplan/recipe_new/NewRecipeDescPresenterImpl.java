package com.cookplan.recipe_new;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.content.FileProvider;

import com.cookplan.R;
import com.cookplan.utils.Utils;
import com.googlecode.tesseract.android.TessBaseAPI;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by DariaEfimova on 16.03.17.
 */

public class NewRecipeDescPresenterImpl implements NewRecipeDescPresenter {
    private static final String tag = NewRecipeDescPresenterImpl.class.getSimpleName();
    private static final String DATA_PATH = Environment.getExternalStorageDirectory().toString() + "/CookPlanImages/";
    private static final String TESSDATA = "tessdata";


    private NewRecipeDescView mainView;
    private Context context;
    private TessBaseAPI tessBaseApi;

    public NewRecipeDescPresenterImpl(NewRecipeDescView mainView, Context context) {
        this.mainView = mainView;
        this.context = context;
    }

    @Override
    public Uri getOutputImagePath() {
        Uri outputFileUri = null;
        try {
            File file = new File(context.getApplicationContext().getExternalFilesDir(
                    android.os.Environment.DIRECTORY_PICTURES).getAbsolutePath() + File.separator + "ocr.jpg");
            outputFileUri = FileProvider.getUriForFile(context,
                    context.getApplicationContext().getPackageName() + ".provider",
                    file);
        } catch (Exception e) {
            e.printStackTrace();
            if (mainView != null) {
                mainView.setErrorToSnackBar(context.getString(R.string.text_wasnt_recognize));
            }
        }
        return outputFileUri;
    }

    /**
     * Prepare directory on external storage
     *
     * @param path
     * @throws Exception
     */
    private void prepareDirectory(String path) {

        File dir = new File(path);
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                Utils.log(tag, "ERROR: Creation of directory " + path + " failed, check does Android Manifest have permission to write to external storage.");
            }
        } else {
            Utils.log(tag, "Created directory " + path);
        }
    }


    private void prepareTesseract() {
        try {
            prepareDirectory(DATA_PATH + TESSDATA);
        } catch (Exception e) {
            e.printStackTrace();
        }

        copyTessDataFiles(TESSDATA);
    }

    /**
     * Copy tessdata files (located on assets/tessdata) to destination directory
     *
     * @param path - name of directory with .traineddata files
     */
    private void copyTessDataFiles(String path) {
        try {
            String fileList[] = context.getAssets().list(path);

            for (String fileName : fileList) {

                // open file within the assets folder
                // if it is not already there copy it to the sdcard
                String pathToDataFile = DATA_PATH + path + "/" + fileName;
                if (!(new File(pathToDataFile)).exists()) {

                    InputStream in = context.getAssets().open(path + "/" + fileName);

                    OutputStream out = new FileOutputStream(pathToDataFile);

                    // Transfer bytes from in to out
                    byte[] buf = new byte[1024];
                    int len;

                    while ((len = in.read(buf)) > 0) {
                        out.write(buf, 0, len);
                    }
                    in.close();
                    out.close();

                    Utils.log(tag, "Copied " + fileName + "to tessdata");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            if (mainView != null) {
                mainView.setErrorToast(context.getString(R.string.unable_to_copy_files) + e.toString());
            }
        }
    }


    /**
     * don't run this code in main thread - it stops UI thread. Create AsyncTask instead.
     * http://developer.android.com/intl/ru/reference/android/os/AsyncTask.html
     *
     * @param imgUri
     */
    private void startOCR(Uri imgUri, String language) {
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 4; // 1 - means max size. 4 - means maxsize/4 size. Don't use value <4, because you need more memory in the heap to store your data.
            InputStream inStream = context.getContentResolver().openInputStream(imgUri);
            Bitmap bitmap = BitmapFactory.decodeStream(inStream, new Rect(0, 0, 0, 0), options);

            String result = extractText(bitmap, language);

            if (mainView != null) {
                mainView.setAsyncTextResult(result);
            }

        } catch (Exception e) {
            e.printStackTrace();
            if (mainView != null) {
                mainView.setAsyncErrorToSnackBar(context.getString(R.string.text_wasnt_recognize));
            }
        }
    }

    private String extractText(Bitmap bitmap, String language) {
        try {
            tessBaseApi = new TessBaseAPI();
        } catch (Exception e) {
            if (tessBaseApi == null) {
                Utils.log(tag, "TessBaseAPI is null. TessFactory not returning tess object.");
            }
            e.printStackTrace();
            if (mainView != null) {
                mainView.setErrorToSnackBar(context.getString(R.string.text_wasnt_recognize));
            }
        }
        tessBaseApi.setPageSegMode(TessBaseAPI.PageSegMode.PSM_AUTO_OSD);
        tessBaseApi.init(DATA_PATH, language);

        //blackList Example
        tessBaseApi.setVariable(TessBaseAPI.VAR_CHAR_BLACKLIST, "!@#$%^&*_+=-[]}{" +
                "'\"\\|~`");

        Utils.log(tag, "Training file loaded");
        tessBaseApi.setImage(bitmap);
        String extractedText = "empty result";
        try {
            extractedText = tessBaseApi.getUTF8Text();
        } catch (Exception e) {
            e.printStackTrace();
            Utils.log(tag, "Error in recognizing text.");
        }
        tessBaseApi.end();
        return extractedText;
    }

    @Override
    public void doOCR(String language) {
        new Thread(() -> {
            prepareTesseract();
            startOCR(getOutputImagePath(), language);
        }).start();
    }
}
