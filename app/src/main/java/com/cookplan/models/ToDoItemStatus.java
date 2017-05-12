package com.cookplan.models;

/**
 * Created by DariaEfimova on 12.05.17.
 */

public enum ToDoItemStatus {
    HAVE_DONE(0), NEED_TO_DO(1);

    private int id;

    ToDoItemStatus(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static ToDoItemStatus getToDoItemStatusName(String name) {
        if (name.equals(HAVE_DONE.name())) {
            return HAVE_DONE;
        }
        if (name.equals(NEED_TO_DO.name())) {
            return NEED_TO_DO;
        }
        return null;
    }
}
