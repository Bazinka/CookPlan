package com.cookplan.models;

import java.io.Serializable;

/**
 * Created by DariaEfimova on 12.04.17.
 */

public class ToDoCategory implements Serializable {

    private String id;
    public String userId;
    public String name;
    public ToDoCategoryColor color;

    public ToDoCategory() {
    }

    public ToDoCategory(String userId, String name, ToDoCategoryColor color) {
        this.userId = userId;
        this.name = name;
        this.color = color;
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

    public ToDoCategoryColor getColor() {
        return color;
    }

    public void setColor(ToDoCategoryColor category) {
        this.color = category;
    }
}
