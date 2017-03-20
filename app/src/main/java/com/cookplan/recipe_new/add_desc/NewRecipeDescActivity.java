package com.cookplan.recipe_new.add_desc;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cookplan.BaseActivity;
import com.cookplan.R;
import com.cookplan.utils.PermissionUtils;
import com.cookplan.utils.Utils;

public class NewRecipeDescActivity extends BaseActivity implements ActivityCompat.OnRequestPermissionsResultCallback,
        NewRecipeDescView {

    private static final int PHOTO_REQUEST_CODE = 101;
    private static final int RC_IMAGE_PERMS = 102;
    private static final String[] permission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA};

    private ProgressDialog mProgressDialog;
    private NewRecipeDescPresenter presenter;

    private String language = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_load);
        setNavigationArrow();
        setTitle(getString(R.string.add_recipe_second_screen_title));

        Button captureImg = (Button) findViewById(R.id.ocr_desc_btn);
        if (captureImg != null) {
            captureImg.setOnClickListener(view -> {
                new AlertDialog.Builder(this)
                        .setMessage(R.string.choose_recipe_language)
                        .setPositiveButton(R.string.english_lan_title, (dialog, which) -> {
                            language = "eng";
                            startCameraActivity();
                        })
                        .setNegativeButton(R.string.russian_lan_title,
                                (dialog, which) -> {
                                    language = "rus";
                                    startCameraActivity();
                                })
                        .show();

            });
        }
        presenter = new NewRecipeDescPresenterImpl(this, this);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!PermissionUtils.isPermissionsGranted(this, permission)) {
                PermissionUtils.requestPermissions(this, RC_IMAGE_PERMS, permission);
                return;
            }
        }
    }


    /**
     * to get high resolution image from camera
     */
    private void startCameraActivity() {
        if (PermissionUtils.isPermissionsGranted(this, permission)) {
            if (presenter != null) {
                Uri outputFileUri = presenter.getOutputImagePath();
                if (outputFileUri != null) {
                    final Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);

                    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                        startActivityForResult(takePictureIntent, PHOTO_REQUEST_CODE);
                    }
                } else {
                    setErrorToast(getString(R.string.error_generate_image_path));
                }
            }
        } else {
            setErrorToSnackBar(getString(R.string.permission_denied));
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent data) {
        //making photo
        if (requestCode == PHOTO_REQUEST_CODE && resultCode == Activity.RESULT_OK
                && presenter != null
                && language != null) {
            if (mProgressDialog == null) {
                mProgressDialog = ProgressDialog.show(this, getString(R.string.processing_title),
                        getString(R.string.processing_ocr_message), true);
            } else {
                mProgressDialog.show();
            }
            presenter.doOCR(language);
        } else {
            Toast.makeText(this, "ERROR: Image was not obtained.", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case RC_IMAGE_PERMS: {

                boolean permGranded = true;
                for (int grantResult : grantResults) {
                    if (grantResult != PackageManager.PERMISSION_GRANTED) {
                        permGranded = false;
                    }
                }

                if (grantResults.length == permission.length && permGranded) {
                    startCameraActivity();
                } else {
                    setErrorToSnackBar(getString(R.string.permission_denied));
                }
            }
        }
    }

    @Override
    public void setErrorToSnackBar(String error) {
        Snackbar.make(findViewById(R.id.root_view), error, Snackbar.LENGTH_LONG).show();
        Utils.log(NewRecipeDescActivity.class.getSimpleName(), error);
    }

    @Override
    public void setErrorToast(String error) {
        Toast.makeText(this, error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void setAsyncTextResult(String result) {
        runOnUiThread(() -> {
            if (result != null && !result.equals("")) {
                EditText textView = (EditText) findViewById(R.id.process_edit_text);
                if (textView != null) {
                    textView.setText(result);
                }
            }
            if (mProgressDialog != null) {
                mProgressDialog.dismiss();
            }
        });
    }

    @Override
    public void setAsyncErrorToSnackBar(String error) {
        runOnUiThread(() -> {
            Snackbar.make(findViewById(R.id.root_view), error, Snackbar.LENGTH_LONG).show();
            Utils.log(NewRecipeDescActivity.class.getSimpleName(), error);
            if (mProgressDialog != null) {
                mProgressDialog.dismiss();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu _menu) {
        getMenuInflater().inflate(R.menu.add_recipe_next_menu, _menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.app_bar_next) {
            //TODO:saving data
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}