package com.knwedu.model;

import com.knwedu.constant.StaticConstant;

import java.io.Serializable;
import java.util.HashMap;

public class UserClass implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private String name = "";
    private String userId = "";
    private String userTypeId="";
    private String databaseId = "";
    private String phone = "";
    private String organiZationId="";
    private String organization="";
    private String role="";
    private String email = "";
    private String status = "";
    private String userName = "";
    private String password = "";
    private boolean isRemember = false;
    private boolean isLoggedin = false;
    private boolean isOTPSignup = false;
    private String sentOtp = "";
    private String deviceRegid = "";
    private String currentVersion = "";
    public String challengeUrl = "";
    private String signupType = StaticConstant.NORMAL_SIGNUP;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserTypeId() {
        return userTypeId;
    }

    public void setUserTypeId(String userTypeId) {
        this.userTypeId = userTypeId;
    }

    public String getDatabaseId() {
        return databaseId;
    }

    public void setDatabaseId(String databaseId) {
        this.databaseId = databaseId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getOrganiZationId() {
        return organiZationId;
    }

    public void setOrganiZationId(String organiZationId) {
        this.organiZationId = organiZationId;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean getIsRemember() {
        return isRemember;
    }

    public void setIsRemember(boolean isRemember) {
        this.isRemember = isRemember;
    }

    public boolean getIsLoggedin() {
        return isLoggedin;
    }

    public void setIsLoggedin(boolean isLoggedin) {
        this.isLoggedin = isLoggedin;
    }

    public boolean isOTPSignup() {
        return isOTPSignup;
    }

    public void setOTPSignup(boolean OTPSignup) {
        isOTPSignup = OTPSignup;
    }

    public String getSentOtp() {
        return sentOtp;
    }

    public void setSentOtp(String sentOtp) {
        this.sentOtp = sentOtp;
    }


    public String getDeviceRegid() {
        return deviceRegid;
    }

    public void setDeviceRegid(String deviceRegid) {
        this.deviceRegid = deviceRegid;
    }

    public String getCurrentVersion() {
        return currentVersion;
    }

    public void setCurrentVersion(String currentVersion) {
        this.currentVersion = currentVersion;
    }

    public String getSignupType() {
        return signupType;
    }

    public void setSignupType(String signupType) {
        this.signupType = signupType;
    }
}
