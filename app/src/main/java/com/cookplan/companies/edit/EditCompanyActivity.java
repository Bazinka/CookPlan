package com.cookplan.companies.edit;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cookplan.BaseActivity;
import com.cookplan.R;
import com.cookplan.utils.FirebaseImageLoader;
import com.cookplan.utils.PermissionUtils;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class EditCompanyActivity extends BaseActivity implements EditCompanyView {

    private static final int RC_CHOOSE_PHOTO = 101;
    private static final int RC_IMAGE_PERMS = 102;

    public static final String PLACE_LATITUDE_KEY = "PLACE_LATITUDE_KEY";
    public static final String PLACE_LONGITUDE_KEY = "PLACE_LONGITUDE_KEY";
    public static final String PLACE_NAME_KEY = "PLACE_NAME_KEY";

    private EditCompanyPresenter presenter;

    private ProgressBar progressView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_new_point);
        setNavigationArrow();

        progressView = (ProgressBar) findViewById(R.id.add_point_progress);
        presenter = new EditCompanyPresenterImpl(this, this);

        String defaultName = presenter.getDefaultName();
        EditText nameEditText = (EditText) findViewById(R.id.name_place_edit_view);
        if (nameEditText != null) {
            nameEditText.setText(defaultName);
        }

        Button addPhotoButton = (Button) findViewById(R.id.save_point_add_photo_button);
        addPhotoButton.setOnClickListener(v -> {
            choosePhoto();
        });
        Button saveButton = (Button) findViewById(R.id.save_place_button);
        saveButton.setOnClickListener(v -> {
            String name = null;
            if (nameEditText != null) {
                name = nameEditText.getText().toString();
            }
            double latitude = getIntent().getDoubleExtra(PLACE_LATITUDE_KEY, -1);
            double longitude = getIntent().getDoubleExtra(PLACE_LONGITUDE_KEY, -1);
            EditText commentsEditText = (EditText) findViewById(R.id.comments_edit_text);
            String comments = commentsEditText.getText().toString();

            if (presenter != null) {
                presenter.savePoint(name, comments, latitude, longitude);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_CHOOSE_PHOTO) {
            if (resultCode == RESULT_OK) {
                Uri selectedImage = data.getData();
                presenter.uploadPhoto(selectedImage);
            } else {
                Toast.makeText(this, "No image chosen", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case RC_IMAGE_PERMS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    choosePhoto();

                } else {
                    setErrorToast("Permission denied, We're sorry, you can't choose the picture");
                }
                return;
            }
        }
    }

    protected void choosePhoto() {
        String[] perm = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE};
        if (ContextCompat.checkSelfPermission(getActivity(), perm[0]) != PackageManager.PERMISSION_GRANTED) {
            PermissionUtils.requestPermissions(this, RC_IMAGE_PERMS, perm);
            return;
        }

        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, RC_CHOOSE_PHOTO);

    }

    @Override
    public void setErrorToast(String error) {
        Toast.makeText(this, error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void setSuccessSavePoint() {
        finish();
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public void setSuccessAddPhoto(List<StorageReference> listImageRef) {
        setProgressBarVisability(View.GONE);
        findViewById(R.id.save_point_photo_title).setVisibility(View.VISIBLE);
        LinearLayout ll = (LinearLayout) findViewById(R.id.save_point_photo_layout);
        ll.setVisibility(View.VISIBLE);
        ll.removeAllViews();
        for (StorageReference pref : listImageRef) {
            ImageView ii = new ImageView(this);
            ii.setPadding(10, 0, 10, 0);
            Glide.with(this)
                    .using(new FirebaseImageLoader())
                    .load(pref).placeholder(R.drawable.logo)
                    .centerCrop()
                    .crossFade()
                    .into(ii);
            ll.addView(ii);
        }
        ll.invalidate();
    }

    @Override
    public void setProgressBarVisability(int visability) {
        if (progressView != null) {
            progressView.setVisibility(visability);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (presenter != null) {
            presenter.removePhotos();
        }
    }
}
