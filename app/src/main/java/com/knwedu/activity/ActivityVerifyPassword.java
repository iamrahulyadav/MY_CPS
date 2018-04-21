package com.knwedu.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.knwedu.calcuttapublicschool.R;
import com.knwedu.constant.Url;
import com.knwedu.firebasepush.Config;
import com.knwedu.model.Role;
import com.knwedu.model.RoleSelectionMenu;
import com.knwedu.model.UserClass;
import com.knwedu.util.Util;
import com.knwedu.volley.PostWithJsonWebTask;
import com.knwedu.volley.ServerResponseStringCallback;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by knowalluser on 29-03-2018.
 */

public class ActivityVerifyPassword extends AppCompatActivity {

    private EditText et_password;
    private String phoneNuumber = "";
    private Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_password);
        mContext = ActivityVerifyPassword.this;
        et_password = (EditText) findViewById(R.id.et_password);
        phoneNuumber = getIntent().getStringExtra("phone");

    }

    public void onSubmitPasswordClick(View v) {
        String password = et_password.getText().toString().trim();

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
        if (TextUtils.isEmpty(password)) {
            Util.showMessageWithOk(ActivityVerifyPassword.this, "Please enter your password.");
        } else if (password.length() < 4) {
            Util.showMessageWithOk(ActivityVerifyPassword.this, "Please enter the correct password.");
        } else {
            HashMap<String, String> map = new HashMap<>();
            map.put("email", "" + phoneNuumber.trim());
            map.put("password", "" + password);
            map.put("devicetype", "android");
            map.put("devicetoken", "" + Util.fetchUserClass(mContext).getDeviceRegid());
            PostWithJsonWebTask.callPostWithJsonWebtask(ActivityVerifyPassword.this, Url.LOGIN_URL, map, new ServerResponseStringCallback() {
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
                            UserClass userClass = Util.fetchUserClass(ActivityVerifyPassword.this);
                            if (userClass == null)
                                userClass = new UserClass();

                            userClass.setPhone("" + phoneNuumber.trim());
                            userClass.setPassword("" + et_password.getText().toString().trim());
                            Util.saveUserClass(ActivityVerifyPassword.this, userClass);

                            Util.saveRoles(ActivityVerifyPassword.this, roleSelectionMenu);

                            Intent intent = new Intent(ActivityVerifyPassword.this, ActivityMenu.class);

                            startActivity(intent);
                            finish();
                        } else if (jsonObject.optString("result").trim().equalsIgnoreCase("0")) {
                            Util.showMessageWithOk(ActivityVerifyPassword.this, "" + jsonObject.optString("data"));
                        } else {
                            Util.showMessageWithOk(ActivityVerifyPassword.this, "Something went wrong! Please try again.");
                        }
                    } catch (Exception e) {
                        Util.showMessageWithOk(ActivityVerifyPassword.this, "Something went wrong! Please try again.");
                    }

                }

                @Override
                public void ErrorMsg(VolleyError error) {

                }
            }, true, Request.Method.POST);
        }
    }

    public void onCallClick(View v) {
        Intent callIntent = new Intent(Intent.ACTION_DIAL);
        callIntent.setData(Uri.parse("tel:" + "9831412425"));//change the number
        startActivity(callIntent);

    }

    public void onForgotPasswordClick(View v) {
        final Dialog customDialog = new Dialog(mContext);

        customDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //customDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        customDialog.setCancelable(true);
        LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.dialog_forgot_password, null);
        final EditText et_phoneNumber = (EditText) view.findViewById(R.id.et_phoneNumber);
        Button btn_submitPhone = (Button) view.findViewById(R.id.btn_submitPhone);
        btn_submitPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String phoneNumber = et_phoneNumber.getText().toString().trim();
                if (TextUtils.isEmpty(phoneNumber)) {
                    Toast.makeText(mContext, "Please enter your phone number", Toast.LENGTH_LONG).show();
                    return;
                } else if (phoneNumber.length() != 10) {

                    Toast.makeText(mContext, "Please enter correct phone number", Toast.LENGTH_LONG).show();
                    return;
                } else {
                    HashMap<String, String> map = new HashMap<>();
                    map.put("mobile_number", "" + phoneNumber);
                    PostWithJsonWebTask.callPostWithJsonWebtask(ActivityVerifyPassword.this, Url.FORGOT_PASSWORD_URL, map, new ServerResponseStringCallback() {
                        @Override
                        public void onSuccess(String resultJsonObject) {
                            try {
                                JSONObject resultJSON = new JSONObject(resultJsonObject);
                                if (resultJSON.optString("result").trim().equalsIgnoreCase("1")) {
                                    Intent intent = new Intent(mContext, ActivityForgotPassword.class);
                                    intent.putExtra("phone", "" + phoneNumber);
                                    startActivity(intent);
                                    customDialog.dismiss();
                                } else if (resultJSON.optString("result").trim().equalsIgnoreCase("0")) {
                                    Util.showMessageWithOk(ActivityVerifyPassword.this, "" + resultJSON.optString("data").trim());
                                    customDialog.dismiss();
                                } else {
                                    Util.showMessageWithOk(ActivityVerifyPassword.this, "Something went wrong. Please try again.");
                                    customDialog.dismiss();
                                }
                            } catch (Exception e) {

                            }
                        }

                        @Override
                        public void ErrorMsg(VolleyError error) {

                        }
                    }, true, Request.Method.POST);
                }
            }
        });
        customDialog.setContentView(view);
        customDialog.setCanceledOnTouchOutside(true);
        // Start AlertDialog
        customDialog.show();

    }

}
