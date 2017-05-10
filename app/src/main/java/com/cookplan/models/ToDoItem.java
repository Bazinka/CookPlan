package com.cookplan.models;

import java.io.Serializable;

/**
 * Created by DariaEfimova on 12.04.17.
 */

public class ToDoItem implements Serializable {

    private String id;
    public String userId;
    public String name;
    public String comment;
    public String categoryId;

    public ToDoItem() {
    }

    public ToDoItem(String userId, String name, String comment, String categoryId) {
        this.userId = userId;
        this.name = name;
        this.comment = comment;
        this.categoryId = categoryId;
    }

    public ToDoItem(String userId, String name, String categoryId) {
        this.userId = userId;
        this.name = name;
        this.categoryId = categoryId;
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

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }
}
