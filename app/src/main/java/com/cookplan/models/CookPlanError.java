package com.cookplan.models;

import android.support.v4.content.ContextCompat;

import com.google.firebase.database.DatabaseError;

/**
 * Created by DariaEfimova on 24.04.17.
 */

public class CookPlanError extends Exception{
    private String message;
    private int code;


    public CookPlanError(String message) {
        this.message = message;
    }

    public CookPlanError(DatabaseError databaseError) {
        message = databaseError.getMessage();
        code = databaseError.getCode();
    }

    public String getMessage() {
        return message;
    }

    public int getCode() {
        return code;
    }
}
