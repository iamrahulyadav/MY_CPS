package com.knwedu.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.knwedu.calcuttapublicschool.R;
import com.knwedu.constant.Url;
import com.knwedu.util.AlertDialogCallBack;
import com.knwedu.util.Util;
import com.knwedu.volley.PostWithJsonWebTask;
import com.knwedu.volley.ServerResponseStringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by knowalluser on 17-04-2018.
 */

public class ActivityUpdatePassword extends AppCompatActivity {

    private EditText et_confirm_password, et_password;

    private Context mContext;

    private Button btn_set_pass;

    private String phoneNumber = "";

    // private TextView tv_pass_msg;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_password);
        mContext = ActivityUpdatePassword.this;
        et_confirm_password = (EditText) findViewById(R.id.et_confirm_password);
        et_password = (EditText) findViewById(R.id.et_password);
        btn_set_pass = (Button) findViewById(R.id.btn_set_pass);
        // tv_pass_msg = (TextView) findViewById(R.id.tv_pass_msg);
        //tv_pass_msg.setText("");
        phoneNumber = getIntent().getStringExtra("phone");
        newPassEntry();
        passwordEntry();
    }

    public void onConfirmPasswordClick(View view) {
        HashMap<String, String> requestMap = new HashMap<>();
        requestMap.put("mobile_number", "" + phoneNumber.trim());
        requestMap.put("password", "" + et_password.getText().toString().trim());
        PostWithJsonWebTask.callPostWithJsonWebtask(ActivityUpdatePassword.this, Url.UPDATE_PASSWORD_URL, requestMap, new ServerResponseStringCallback() {
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

                            Util.showMessageWithOk(ActivityUpdatePassword.this, "" + jsonObject.optString("data"));
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

    private void goToLogin() {
        Intent intent = new Intent(mContext, ActivityVerifyPhoneNumber.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
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

    public void onCallClick(View v) {
        Intent callIntent = new Intent(Intent.ACTION_DIAL);
        callIntent.setData(Uri.parse("tel:" + "9831412425"));//change the number
        startActivity(callIntent);
    }
}
