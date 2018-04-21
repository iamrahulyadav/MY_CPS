package com.knwedu.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by knowalladmin on 21/11/17.
 */

public class Teacher implements Serializable{

    public String id;

    public String teacherName;

    public String teacherMobileNo;

    public ArrayList<Subject> subjects= new ArrayList<>();


}
