package com.cookplan.upload_image;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.cookplan.BaseActivity;
import com.cookplan.R;
import com.cookplan.utils.GridSpacingItemDecoration;
import com.cookplan.utils.PermissionUtils;
import com.cookplan.utils.Utils;

import java.util.ArrayList;

public class UploadImageActivity extends BaseActivity implements UploadImageView,
        ActivityCompat.OnRequestPermissionsResultCallback {

    public static final String IMAGE_URL_KEY = "IMAGE_URL_KEY";

    private static final int CHOOSE_PHOTO_REQUEST_CODE = 17;
    private static final int RC_IMAGE_CAMERA_PERMS = 18;
    private static final int TAKE_PHOTO_REQUEST_CODE = 19;
    private static final int RC_IMAGE_CHOOSE_PHOTO_PERMS = 20;

    private UploadImagesRecyclerViewAdapter adapter;
    private UploadImagePresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_image);
        setNavigationArrow();
        setTitle(getString(R.string.edit_image_list_title));

        ArrayList<String> urls = getIntent().getStringArrayListExtra(IMAGE_URL_KEY);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.image_list_recycler);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, Utils.dpToPx(this, 16), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new UploadImagesRecyclerViewAdapter(
                urls,
                new UploadImagesRecyclerViewAdapter.UploadImagesEventListener() {
                    @Override
                    public void onAddImageEvent() {
                        new AlertDialog.Builder(UploadImageActivity.this, R.style.AppCompatAlertDialogStyle)
                                .setTitle(R.string.attention_title)
                                .setMessage("Откуда брать фотографии?")
                                .setPositiveButton("Камера", (dialog, which) -> {
                                    startCameraWithPermCheck();
                                })
                                .setNeutralButton(android.R.string.cancel, null)
                                .setNegativeButton("Память", (dialog, which) -> {
                                                       choosePhoto();
                                                   }
                                )
                                .show();
                    }

                    @Override
                    public void onDeleteImage(String url) {
                        if (presenter != null) {
                            presenter.removePhoto(url);
                        }
                    }
                }, this);
        recyclerView.setAdapter(adapter);
        presenter = new UploadImagePresenterImpl(this, this);
    }

    protected void choosePhoto() {
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);

        String[] perm = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE};
        if (ContextCompat.checkSelfPermission(this, perm[0]) != PackageManager.PERMISSION_GRANTED) {
            PermissionUtils.requestPermissions(this, RC_IMAGE_CHOOSE_PHOTO_PERMS, perm);
            return;
        }

        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, CHOOSE_PHOTO_REQUEST_CODE);
    }

    private void startCameraWithPermCheck() {
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);

        String[] permission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA};
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!PermissionUtils.isPermissionsGranted(this, permission)) {
                PermissionUtils.requestPermissions(this, RC_IMAGE_CAMERA_PERMS, permission);
            } else {
                startCameraActivity();
            }
        }
    }

    /**
     * to get high resolution image from camera
     */
    private void startCameraActivity() {
        String[] permission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA};
        if (PermissionUtils.isPermissionsGranted(this, permission)) {
            if (presenter != null) {
                Uri outputFileUri = presenter.getOutputImagePath();
                if (outputFileUri != null) {
                    final Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);

                    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                        startActivityForResult(takePictureIntent, TAKE_PHOTO_REQUEST_CODE);
                    }
                } else {
                    setError(getString(R.string.error_generate_image_path));
                }
            }
        } else {
            setError(getString(R.string.permission_denied));
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent data) {
        if (resultCode == Activity.RESULT_OK && presenter != null) {
            Uri selectedImage = null;
            if (requestCode == TAKE_PHOTO_REQUEST_CODE) {
                selectedImage = presenter.getOutputImagePath();
            } else if (requestCode == CHOOSE_PHOTO_REQUEST_CODE) {
                selectedImage = data.getData();
            } else {
                Toast.makeText(this, "ERROR: Image was not obtained.", Toast.LENGTH_SHORT).show();
            }
            if (selectedImage != null) {
                ProgressBar progressBar = (ProgressBar) findViewById(R.id.progress_bar);
                progressBar.setVisibility(View.VISIBLE);
                presenter.uploadPhoto(selectedImage);
            }
        } else {
            Toast.makeText(this, "No image chosen", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case RC_IMAGE_CAMERA_PERMS: {
                boolean permGranded = true;
                for (int grantResult : grantResults) {
                    if (grantResult != PackageManager.PERMISSION_GRANTED) {
                        permGranded = false;
                    }
                }
                if (grantResults.length == 2 && permGranded) {
                    startCameraActivity();
                } else {
                    setError(getString(R.string.permission_denied));
                }
                break;
            }
            case RC_IMAGE_CHOOSE_PHOTO_PERMS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    choosePhoto();
                } else {
                    setError("Permission denied, We're sorry, you can't choose the picture");
                }
                return;
            }
        }
    }

    @Override
    public void setError(String error) {
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.GONE);
        View mainView = findViewById(R.id.main_view);
        if (mainView != null) {
            Snackbar.make(mainView, error, Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    public void setImageSaved(String url) {
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.GONE);
        if (adapter != null) {
            adapter.addImage(url);
        }
    }

    @Override
    public void setImageRemoved(String imageId) {
        if (adapter != null) {
            adapter.removeImage(imageId);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu _menu) {
        getMenuInflater().inflate(R.menu.done_menu, _menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.app_bar_done) {
            ArrayList<String> imagesList = new ArrayList<>(adapter.getValues());
            Intent intent = new Intent();
            intent.putStringArrayListExtra(IMAGE_URL_KEY, imagesList);
            setResult(RESULT_OK, intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
