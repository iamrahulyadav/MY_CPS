package com.knwedu.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by knowalluser on 02-04-2018.
 */

public class AllMenuItemsModel implements Serializable {

    private ArrayList<String> categories = new ArrayList<>();

    private ArrayList<MenuCategory> menuCategories = new ArrayList<>();

    public ArrayList<String> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<String> categories) {
        this.categories = categories;
    }

    public ArrayList<MenuCategory> getMenuCategories() {
        return menuCategories;
    }

    public void setMenuCategories(ArrayList<MenuCategory> menuCategories) {
        this.menuCategories = menuCategories;
    }
}
