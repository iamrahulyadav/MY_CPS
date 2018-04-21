package com.knwedu.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.knwedu.calcuttapublicschool.R;
import com.knwedu.constant.Url;
import com.knwedu.firebasepush.Config;
import com.knwedu.model.Role;
import com.knwedu.model.RoleSelectionMenu;
import com.knwedu.model.UserClass;
import com.knwedu.util.AlertDialogCallBack;
import com.knwedu.util.Util;
import com.knwedu.volley.PostWithJsonWebTask;
import com.knwedu.volley.ServerResponseStringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by knowalluser on 13-03-2018.
 */

public class ActivitySetPassword extends AppCompatActivity {

    private EditText et_confirm_password, et_password;

    private Context mContext;

    private Button btn_set_pass;

    private String phoneNumber = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_password);

        mContext = ActivitySetPassword.this;
        et_confirm_password = (EditText) findViewById(R.id.et_confirm_password);
        et_password = (EditText) findViewById(R.id.et_password);
        btn_set_pass = (Button) findViewById(R.id.btn_set_pass);

        phoneNumber = getIntent().getStringExtra("phone");
        newPassEntry();
        passwordEntry();

    }

    public void onConfirmPasswordClick(View view) {
        HashMap<String, String> requestMap = new HashMap<>();
        requestMap.put("mobile_number", "" + phoneNumber.trim());
        requestMap.put("password", "" + et_password.getText().toString().trim());
        PostWithJsonWebTask.callPostWithJsonWebtask(ActivitySetPassword.this, Url.SET_PASSWORD_URL, requestMap, new ServerResponseStringCallback() {
            @Override
            public void onSuccess(String resultJsonObject) {
                {
                    try {
                        JSONObject jsonObject = new JSONObject(resultJsonObject);

                        if (jsonObject.optString("result").trim().equalsIgnoreCase("1")) {
                            Util.showCallBackMessageWithOkCallback(mContext, "" + jsonObject.optString("data"), new AlertDialogCallBack() {
                                @Override
                                public void onSubmit() {

                                    goToLogin();

                                }

                                @Override
                                public void onCancel() {

                                }
                            });
                        } else if (jsonObject.optString("result").trim().equalsIgnoreCase("0")) {

                            Util.showMessageWithOk(ActivitySetPassword.this, "" + jsonObject.optString("data"));
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void ErrorMsg(VolleyError error) {

            }
        }, true, Request.Method.POST);


    }

    private void newPassEntry() {
        et_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() < 4) {
                    Toast.makeText(mContext, "Password length too short.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });
    }

    private void passwordEntry() {
        et_confirm_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String confirmPassword = s.toString().trim();

                if (et_password.getText().toString().trim().length() >= 4 &&
                        (confirmPassword.trim().equalsIgnoreCase(et_password.getText().toString().trim()))) {
                    btn_set_pass.setVisibility(View.VISIBLE);
                } else {
                    btn_set_pass.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });
    }

    private void goToLogin() {
        try {
            SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
            String regId = pref.getString("regId", null);
            UserClass userClass = Util.fetchUserClass(mContext);
            if (userClass == null)
                userClass = new UserClass();
            userClass.setDeviceRegid(regId);
            Util.saveUserClass(mContext, userClass);
        } catch (Exception e) {
            e.printStackTrace();
        }

        HashMap<String, String> map = new HashMap<>();
        map.put("email", "" + phoneNumber.trim());
        map.put("password", "" + et_password.getText().toString().trim());
        map.put("devicetype", "android");
        map.put("devicetoken", "" + Util.fetchUserClass(mContext).getDeviceRegid());
        PostWithJsonWebTask.callPostWithJsonWebtask(ActivitySetPassword.this, Url.LOGIN_URL, map, new ServerResponseStringCallback() {
            @Override
            public void onSuccess(String resultJsonObject) {
                try {
                    JSONObject jsonObject = new JSONObject(resultJsonObject);
                    if (jsonObject.optString("result").trim().equalsIgnoreCase("1")) {

                        JSONArray jsonArray = jsonObject.optJSONArray("data");
                        ArrayList<Role> roles = new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject roleJsonObj = jsonArray.optJSONObject(i);
                            Role role = new Role();
                            role.setId(roleJsonObj.optString("id"));
                            role.setMobileNumber(roleJsonObj.optString("mobile_no"));
                            role.setName(roleJsonObj.optString("name"));
                            role.setUserTypeId(roleJsonObj.optString("user_type_id"));
                            role.setOrganization(roleJsonObj.optString("organization"));
                            role.setOrganizationId(roleJsonObj.optString("organization_id"));
                            role.setRole(roleJsonObj.optString("role"));
                            roles.add(role);

                        }
                        RoleSelectionMenu roleSelectionMenu = new RoleSelectionMenu();
                        roleSelectionMenu.setRoles(roles);
                        UserClass userClass = Util.fetchUserClass(ActivitySetPassword.this);
                        if (userClass == null)
                            userClass = new UserClass();

                        userClass.setPhone("" + phoneNumber.trim());
                        userClass.setPassword("" + et_password.getText().toString().trim());
                        Util.saveUserClass(ActivitySetPassword.this, userClass);

                        Util.saveRoles(ActivitySetPassword.this, roleSelectionMenu);

                        Intent intent = new Intent(ActivitySetPassword.this, ActivityMenu.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        finish();

                        startActivity(intent);
                        finish();
                    } else if (jsonObject.optString("result").trim().equalsIgnoreCase("0")) {
                        Util.showMessageWithOk(ActivitySetPassword.this, "" + jsonObject.optString("data"));
                    } else {
                        Util.showMessageWithOk(ActivitySetPassword.this, "Something went wrong! Please try again.");
                    }
                } catch (Exception e) {
                    Util.showMessageWithOk(ActivitySetPassword.this, "Something went wrong! Please try again.");
                }

            }

            @Override
            public void ErrorMsg(VolleyError error) {

            }
        }, true, Request.Method.POST);
    }

    public void onCallClick(View v) {

    }
}
