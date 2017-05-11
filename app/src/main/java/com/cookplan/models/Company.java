package com.cookplan.models;

import android.support.annotation.NonNull;

import org.joda.time.LocalDateTime;

import java.io.Serializable;
import java.util.List;

/**
 * Created by DariaEfimova on 17.10.16.
 */

public class Company implements Serializable {

    private String id;
    public String userId;
    public String name;
    public String comment;

    private LocalDateTime jodaDateCreated;
    public long dateCreatedMillisek;

    public double latitude;
    public double longitude;

    private List<String> photoList;

    public Company() {
        jodaDateCreated = LocalDateTime.now();
        dateCreatedMillisek = jodaDateCreated.getMillisOfSecond();
    }

    public Company(String userId, String name, String comment, double latitude, double longitude) {
        super();
        this.userId = userId;
        this.name = name;
        this.comment = comment;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public String getComment() {
        return comment;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public long getDateCreatedMillisek() {
        return dateCreatedMillisek;
    }

    public List<String> getPhotoList() {
        return photoList;
    }

    public void setPhotoList(List<String> photoList) {
        this.photoList = photoList;
    }

    public String getUserId() {
        return userId;
    }
}
