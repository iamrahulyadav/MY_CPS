package com.knwedu.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by knowalluser on 02-04-2018.
 */

public class MenuCategory implements Serializable {

    private String categoryName = "";

    private ArrayList<MenuItem> subCategories = new ArrayList<>();


    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public ArrayList<MenuItem> getSubCategories() {
        return subCategories;
    }

    public void setSubCategories(ArrayList<MenuItem> subCategories) {
        this.subCategories = subCategories;
    }
}
