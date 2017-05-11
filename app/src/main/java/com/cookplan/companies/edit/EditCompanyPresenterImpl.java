package com.cookplan.companies.edit;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.cookplan.R;
import com.cookplan.models.Company;
import com.cookplan.utils.DatabaseConstants;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.joda.time.LocalDateTime;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

/**
 * Created by DariaEfimova on 19.10.16.
 */

public class EditCompanyPresenterImpl implements EditCompanyPresenter {

    private EditCompanyView mainView;
    private Context context;

    private DatabaseReference mRef;
    private DatabaseReference mNewPointRef;

    private List<StorageReference> photoListPreferences;

    public EditCompanyPresenterImpl(Context contex, EditCompanyView mainView) {
        this.context = contex;
        this.mainView = mainView;
        mRef = FirebaseDatabase.getInstance().getReference();
        mNewPointRef = mRef.child(DatabaseConstants.DATABASE_POINT_TABLE);
        photoListPreferences = new ArrayList<>();
    }

    @Override
    public String getDefaultName() {
        LocalDateTime currentDate = new LocalDateTime();
        String defaultName = context.getString(R.string.default_place_name)
                + " " + currentDate.dayOfWeek().getAsText(Locale.getDefault())
                + ", " + currentDate.toString("dd.MM.yyyy HH:mm");
        if (context instanceof AppCompatActivity) {
            String name = ((AppCompatActivity) context).getIntent().getStringExtra(
                    EditCompanyActivity.PLACE_NAME_KEY);
            if (name != null) {
                defaultName = name;
            }
        }
        return defaultName;
    }

    @Override
    public void savePoint(String name, String comments, double latitude, double longitude) {
        if (mNewPointRef != null
                && FirebaseAuth.getInstance() != null
                && FirebaseAuth.getInstance().getCurrentUser() != null
                && name != null
                && !name.isEmpty()) {
            String id = FirebaseAuth.getInstance().getCurrentUser().getUid();
            Company company = new Company(id, name, comments, latitude, longitude);
            if (photoListPreferences != null && !photoListPreferences.isEmpty()) {
                List<String> photoUrlsList = new ArrayList<>();
                for (StorageReference pref : photoListPreferences) {
                    pref.getDownloadUrl().addOnSuccessListener(uri -> {
                        photoUrlsList.add(uri.toString());
                        if (photoUrlsList.size() == photoListPreferences.size()) {
                            company.setPhotoList(photoUrlsList);
                            saveCompany(company);
                        }
                    }).addOnFailureListener(exception -> Toast.makeText(context, R.string.save_point_error, Toast.LENGTH_LONG).show());

                }
            } else {
                saveCompany(company);
            }
        }
    }

    private void saveCompany(Company company) {
        mNewPointRef.push().setValue(company, (databaseError, reference) -> {
            if (databaseError != null) {
                Log.e("AddPointPresenterImpl", "Failed to write message", databaseError.toException());
            } else {
                mainView.setSuccessSavePoint();
            }
        });
    }

    @Override
    public void uploadPhoto(Uri uri) {
        Toast.makeText(context, "Uploading...", Toast.LENGTH_SHORT).show();
        if (mainView != null) {
            mainView.setProgressBarVisability(View.VISIBLE);
        }
        // Upload to Firebase Storage
        String uuid = UUID.randomUUID().toString();
        StorageReference imageRef = FirebaseStorage.getInstance().getReference(uuid);
        imageRef.putFile(uri)
                .addOnSuccessListener(mainView.getActivity(), new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Log.d("AddPoint", "uploadPhoto:onSuccess:" +
                                taskSnapshot.getMetadata().getReference().getPath());
                        Toast.makeText(context, "Image uploaded",
                                       Toast.LENGTH_SHORT).show();
                        photoListPreferences.add(imageRef);
                        if (mainView != null) {
                            mainView.setSuccessAddPhoto(photoListPreferences);
                        }
                    }
                })
                .addOnFailureListener(mainView.getActivity(), new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if (mainView != null) {
                            mainView.setProgressBarVisability(View.GONE);
                        }
                        Log.w("AddPoint", "uploadPhoto:onError", e);
                        Toast.makeText(context, "Upload failed",
                                       Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void removePhotos() {
        for (StorageReference pref : photoListPreferences) {
            pref.delete();
        }

    }

}
