package com.knwedu.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.knwedu.calcuttapublicschool.R;
import com.knwedu.ourschool.utils.Constants;
import com.knwedu.ourschool.utils.SchoolAppUtils;
import com.knwedu.ourschool.utils.Urls;
import com.knwedu.util.Util;
import com.knwedu.volley.PostWithJsonWebTask;
import com.knwedu.volley.ServerResponseStringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by ritwik.rai on 20/11/17.
 */

public class ChangePassEnterOtpActivity extends ActionBarActivity {

    private Context mContext;

    private EditText etPhoneNumber;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_mobile_otp);
        mContext = ChangePassEnterOtpActivity.this;

        etPhoneNumber = (EditText) findViewById(R.id.et_phoneNumber);

    }

    public void onSendOTPClicked(View v) {
        String phoneNumber = etPhoneNumber.getText().toString().trim();

        if (TextUtils.isEmpty(phoneNumber)) {
            Util.showMessageWithOk(ChangePassEnterOtpActivity.this, "Please enter a correct phone number.");
            return;
        } else if (phoneNumber.length() < 10) {

            Util.showMessageWithOk(ChangePassEnterOtpActivity.this, "Please enter a correct phone number.");
            return;
        }

        String organizationId = SchoolAppUtils.GetSharedParameter(mContext, Constants.UORGANIZATIONID);
        //Toast.makeText(mContext,""+ Urls.base_url,Toast.LENGTH_SHORT).show();
        HashMap<String, String> requestMap = new HashMap<>();
        requestMap.put("mobile_number", "" + phoneNumber);
        requestMap.put("organization_id", "" + organizationId.trim());

        PostWithJsonWebTask.callPostWithJsonWebtask(ChangePassEnterOtpActivity.this, Urls.base_url + "mobile/genereateOtp", requestMap, new ServerResponseStringCallback() {
            @Override
            public void onSuccess(String resultJsonObject) {
                Log.e("Response", "Response: " + resultJsonObject);
                try {
                    final JSONObject jsonObject = new JSONObject(resultJsonObject);

                    if (jsonObject.optString("result").trim().equalsIgnoreCase("1")) {

                        /*UserClass userClass = Util.fetchUserClass(mContext);
                        userClass.setSentOtp(jsonObject.optString("otp"));
                        Util.saveUserClass(mContext, userClass);*/

                        String otp = jsonObject.optString("otp");
                        Intent intent = new Intent(mContext, ChangePassVerifyOTPActivity.class);
                        intent.putExtra("phoneNumber", "" + etPhoneNumber.getText().toString().trim());
                        intent.putExtra("otp", otp);
                        startActivity(intent);

                    } else {

                        Util.showMessageWithOk(ChangePassEnterOtpActivity.this, "Sorry! Your mobile number is not registered.");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void ErrorMsg(VolleyError error) {


            }
        }, true, Request.Method.POST);


    }

}
