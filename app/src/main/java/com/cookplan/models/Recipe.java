package com.cookplan.models;

import com.cookplan.utils.DatabaseConstants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.PropertyName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by DariaEfimova on 16.03.17.
 */

public class Recipe implements Serializable {

    private String id;
    private String name;
    private String desc;
    private List<String> imageUrls;
    private String userId;
    private String userName;
    private List<Long> cookingDateList;

    public Recipe() {
        cookingDateList = new ArrayList<>();
    }

    public Recipe(String id, String name, String desc, List<String> imageUrls, String userId, List<Long> cookingDateList) {
        this();
        this.id = id;
        this.name = name;
        this.desc = desc;
        this.imageUrls = imageUrls;
        this.userId = userId;
        this.cookingDateList = new ArrayList<>();
        if (cookingDateList != null) {
            this.cookingDateList.addAll(cookingDateList);
        }
    }

    public Recipe(String name, String desc, List<String> imageUrls) {
        this();
        this.name = name;
        this.desc = desc;
        this.imageUrls = imageUrls;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    public void setCookingDate(List<Long> cookingDateList) {
        this.cookingDateList = new ArrayList<>(cookingDateList);
    }

    public void addCookingDate(long cookingDate) {
        if (cookingDateList == null) {
            this.cookingDateList = new ArrayList<>();
        }
        cookingDateList.add(cookingDate);
    }

    public void clearCookingDateList() {
        if (cookingDateList != null && !cookingDateList.isEmpty()) {
            cookingDateList.clear();
        }
    }

    public List<Long> getCookingDate() {
        return cookingDateList;
    }

    public String getDesc() {
        return desc;
    }

    public RecipeDB getRecipeDB() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        return new RecipeDB(auth.getCurrentUser().getUid(), name, desc, imageUrls, cookingDateList);
    }

    public static Recipe getRecipeFromDBObject(DataSnapshot itemSnapshot) {
        RecipeDB object = itemSnapshot.getValue(RecipeDB.class);
        Recipe recipe = new Recipe(itemSnapshot.getKey(), object.getName(),
                                   object.getDesc(), object.getImageUrls(),
                                   object.getUserId(), object.getCookingDate());
        return recipe;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public ArrayList<String> getImageUrlArrayList() {
        if (imageUrls != null) {
            return new ArrayList<>(imageUrls);
        } else {
            return new ArrayList<>();
        }
    }

    public static class RecipeDB {


        public String id;

        @PropertyName(DatabaseConstants.DATABASE_NAME_FIELD)
        public String name;

        @PropertyName(DatabaseConstants.DATABASE_DESCRIPTION_FIELD)
        public String desc;

        @PropertyName(DatabaseConstants.DATABASE_USER_ID_FIELD)
        public String userId;

        @PropertyName(DatabaseConstants.DATABASE_IMAGE_URL_LIST_FIELD)
        public List<String> imageUrls;

        @PropertyName(DatabaseConstants.DATABASE_COOKING_DATE_LIST_FIELD)
        public List<Long> cookingDateList;


        public RecipeDB() {
        }

        public RecipeDB(String userId, String name, String desc, List<String> imageUrls, List<Long> cookingDate) {
            this.name = name;
            this.desc = desc;
            this.userId = userId;
            this.imageUrls = imageUrls;
            this.cookingDateList = cookingDateList;
        }

        public String getName() {
            return name;
        }

        public String getDesc() {
            return desc;
        }

        public String getId() {
            return id;
        }

        public String getUserId() {
            return userId;
        }

        public List<String> getImageUrls() {
            return imageUrls;
        }

        public List<Long> getCookingDate() {
            return cookingDateList;
        }
    }

}
