package com.knwedu.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by knowalluser on 29-03-2018.
 */

public class RoleSelectionMenu implements Serializable {


    private ArrayList<Role> roles = new ArrayList<>();

    public ArrayList<Role> getRoles() {
        return roles;
    }

    public void setRoles(ArrayList<Role> roles) {
        this.roles = roles;
    }
}
