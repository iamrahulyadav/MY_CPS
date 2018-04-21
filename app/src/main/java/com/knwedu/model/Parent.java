package com.knwedu.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by knowalladmin on 27/11/17.
 */

public class Parent implements Serializable {

public String parentName= "";
public String parentID="";
public String parentMobile= "";
public ArrayList<Student> students= new ArrayList<>();
}
