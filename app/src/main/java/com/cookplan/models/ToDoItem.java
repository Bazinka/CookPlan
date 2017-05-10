package com.cookplan.models;

import java.io.Serializable;

/**
 * Created by DariaEfimova on 12.04.17.
 */

public class ToDoItem implements Serializable {

    private String id;
    private String userId;
    private String name;
    private String comment;

    public ToDoItem() {
    }

    public ToDoItem(String userId, String name, String comment) {
        this.userId = userId;
        this.name = name;
        this.comment = comment;
    }

    public ToDoItem(String userId, String name) {
        this.userId = userId;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
