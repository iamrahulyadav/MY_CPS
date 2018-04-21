package com.knwedu.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.knwedu.calcuttapublicschool.R;
import com.knwedu.ourschool.SigninActivity;
import com.knwedu.ourschool.utils.Constants;
import com.knwedu.ourschool.utils.SchoolAppUtils;
import com.knwedu.ourschool.utils.Urls;
import com.knwedu.util.AlertDialogCallBack;
import com.knwedu.util.Util;
import com.knwedu.volley.PostWithJsonWebTask;
import com.knwedu.volley.ServerResponseStringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by knowalladmin on 07/12/17.
 */

public class ChangePasswordFromOTPActivity extends ActionBarActivity {


    private EditText et_newPassword;
    private EditText et_confirmPassword;
    private Context mContext;
    String phoneNumber = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password_otp);
        mContext = ChangePasswordFromOTPActivity.this;

        et_newPassword = (EditText) findViewById(R.id.et_newPassword);
        et_confirmPassword = (EditText) findViewById(R.id.et_confirmPassword);

        phoneNumber = getIntent().getStringExtra("phone");
    }

    public void onSubmitCLick(View view) {

        String newPassword = et_newPassword.getText().toString().trim();
        String confirmPassword = et_confirmPassword.getText().toString().trim();

        if (TextUtils.isEmpty(newPassword) || TextUtils.isEmpty(confirmPassword)) {
            Toast.makeText(mContext, "Please fill all fields.", Toast.LENGTH_SHORT).show();
            return;
        } else if (newPassword.length() < 4) {
            Toast.makeText(mContext, "Password length is too short.", Toast.LENGTH_SHORT).show();
            return;
        } else if (!newPassword.equalsIgnoreCase(confirmPassword)) {
            Toast.makeText(mContext, "Passwords do not match.", Toast.LENGTH_SHORT).show();
            return;
        }

        String organizationId = SchoolAppUtils.GetSharedParameter(mContext, Constants.UORGANIZATIONID);
        HashMap<String, String> requestMap = new HashMap<>();
        requestMap.put("mobile_number", ""+phoneNumber);
        requestMap.put("password", ""+newPassword);
        requestMap.put("organization_id", ""+organizationId.trim());

        PostWithJsonWebTask.callPostWithJsonWebtask(ChangePasswordFromOTPActivity.this, Urls.base_url + "mobile/resetPassword", requestMap, new ServerResponseStringCallback() {
            @Override
            public void onSuccess(String resultJsonObject) {
                try{
                    JSONObject jsonObject = new JSONObject(resultJsonObject);

                    if (jsonObject.optString("result").trim().equalsIgnoreCase("1")) {
                        Util.showCallBackMessageWithOkCallback(mContext, ""+jsonObject.optString("data"), new AlertDialogCallBack() {
                            @Override
                            public void onSubmit() {

                                Intent intent= new Intent(mContext, SigninActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }

                            @Override
                            public void onCancel() {

                            }
                        });
                    }
                    else  if (jsonObject.optString("result").trim().equalsIgnoreCase("0")) {

                        Util.showMessageWithOk(ChangePasswordFromOTPActivity.this,""+jsonObject.optString("data"));
                    }

                }catch(JSONException e)
                {
                    e.printStackTrace();
                }

            }

            @Override
            public void ErrorMsg(VolleyError error) {

            }
        }, true, Request.Method.POST);

    }
}
