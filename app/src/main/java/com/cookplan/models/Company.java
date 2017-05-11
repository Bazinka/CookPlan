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
    private String name;
    private String comments;

    private LocalDateTime jodaDateCreated;
    private long dateCreatedMillisek;

    private double latityde;
    private double longitude;

    private List<String> photoList;

    public Company() {
        jodaDateCreated = LocalDateTime.now();
        dateCreatedMillisek = jodaDateCreated.getMillisOfSecond();
    }

    public Company(String id, String name, String comments, double latityde, double longitude) {
        super();
        this.id = id;
        this.name = name;
        this.comments = comments;
        this.latityde = latityde;
        this.longitude = longitude;
    }

    @NonNull
    public String getId() {
        return id;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public String getComments() {
        return comments;
    }

    public double getLatityde() {
        return latityde;
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
}
