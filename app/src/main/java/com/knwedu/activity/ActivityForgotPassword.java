package com.knwedu.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.knwedu.calcuttapublicschool.R;
import com.knwedu.constant.Url;
import com.knwedu.util.Util;
import com.knwedu.volley.PostWithJsonWebTask;
import com.knwedu.volley.ServerResponseStringCallback;
import com.stfalcon.smsverifycatcher.OnSmsCatchListener;
import com.stfalcon.smsverifycatcher.SmsVerifyCatcher;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by knowalluser on 17-04-2018.
 */

public class ActivityForgotPassword extends AppCompatActivity {

    private Context mContext;
    SmsVerifyCatcher smsVerifyCatcher;
    private LinearLayout llVerifyOtp;
    private Button btn_confirmOtp;
    private EditText editTextOTP;
    private CountDownTimer mCountDownTimer;
    private TextView tv_waiting_msg, tv_timer, tv_timeoutMessage;
    private LinearLayout ll_waitingOtp;
    private Button btn_resendOtp;
    private String phoneNumber;

    @Override
    protected void onStart() {
        super.onStart();
        smsVerifyCatcher.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        smsVerifyCatcher.onStop();
    }

    /**
     * need for Android 6 real time permissions
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        smsVerifyCatcher.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_otp);
        mContext = ActivityForgotPassword.this;

        llVerifyOtp = (LinearLayout) findViewById(R.id.llVerifyOtp);
        btn_confirmOtp = (Button) findViewById(R.id.btn_confirmOtp);
        editTextOTP = (EditText) findViewById(R.id.editTextOTP);
        tv_waiting_msg = (TextView) findViewById(R.id.tv_waiting_msg);
        tv_timer = (TextView) findViewById(R.id.tv_timer);
        tv_timeoutMessage = (TextView) findViewById(R.id.tv_timeoutMessage);
        ll_waitingOtp = (LinearLayout) findViewById(R.id.ll_waitingOtp);
        btn_resendOtp = (Button) findViewById(R.id.btn_resendOtp);
        smsVerifyCatcher = new SmsVerifyCatcher(this, new OnSmsCatchListener<String>() {
            @Override
            public void onSmsCatch(String message) {
                String code = parseCode(message);//Parse verification code
                editTextOTP.setText(code);//set code in edit text
                mCountDownTimer.cancel();
                tv_waiting_msg.setText("OTP received.");
                tv_timer.setText("");
            }
        });
        phoneNumber = getIntent().getStringExtra("phone");
        otpEntry();
        runCountdownTimer();
    }

    private void runCountdownTimer() {

        tv_timeoutMessage.setVisibility(View.GONE);
        ll_waitingOtp.setVisibility(View.VISIBLE);
        btn_resendOtp.setVisibility(View.INVISIBLE);
        tv_timer.setText("2:00 min");
        tv_waiting_msg.setText("Waiting for OTP.");

        mCountDownTimer = new CountDownTimer(120000, 1000) {

            public void onTick(long millisUntilFinished) {
                tv_timer.setText((millisUntilFinished / 60000) + ":" + (millisUntilFinished % 60000 / 1000) + " min");
            }

            public void onFinish() {
                tv_timer.setText("0:00 min");
                ll_waitingOtp.setVisibility(View.GONE);
                tv_timeoutMessage.setVisibility(View.VISIBLE);
                btn_resendOtp.setVisibility(View.VISIBLE);
            }


        }.start();
    }

    /**
     * Parse verification code
     *
     * @param message sms message
     * @return only six numbers from message string
     */
    private String parseCode(String message) {
        Pattern p = Pattern.compile("\\b\\d{6}\\b");
        Matcher m = p.matcher(message);
        String code = "";
        while (m.find()) {
            code = m.group(0);
        }
        return code;
    }

    private void otpEntry() {
        editTextOTP.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 6) {
                    btn_confirmOtp.setVisibility(View.VISIBLE);
                } else {
                    btn_confirmOtp.setVisibility(View.INVISIBLE);
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

    public void onConfirmOtpClick(View view) {
        String otp = editTextOTP.getText().toString().trim();

        if (TextUtils.isEmpty(otp)) {
            Util.showMessageWithOk(ActivityForgotPassword.this, "Please enter the OTP received.");
            return;
        } else if (otp.length() < 6) {
            Util.showMessageWithOk(ActivityForgotPassword.this, "Please enter the correct OTP.");
            return;
        }

        HashMap<String, String> requestMap = new HashMap<>();
        requestMap.put("mobile_number", "" + phoneNumber);
        requestMap.put("otp", "" + editTextOTP.getText().toString().trim());
        PostWithJsonWebTask.callPostWithJsonWebtask(ActivityForgotPassword.this, Url.VERIFY_OTP_NEW, requestMap,
                new ServerResponseStringCallback() {
                    @Override
                    public void onSuccess(String resultJsonObject) {
                        try {
                            JSONObject jsonObject = new JSONObject(resultJsonObject);

                            if (jsonObject.optString("result").trim().equalsIgnoreCase("1")) {
                                Toast.makeText(mContext, "OTP Verified", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(mContext, ActivityUpdatePassword.class);
                                intent.putExtra("phone", "" + phoneNumber);
                                startActivity(intent);
                                finish();


                            } else if (jsonObject.optString("result").trim().equalsIgnoreCase("0")) {

                                Toast.makeText(mContext, "Wrong OTP.", Toast.LENGTH_SHORT).show();
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

    public void onResendOTPClicked(View view) {
        onSendOtpClick();
    }

    private void onSendOtpClick() {
        HashMap<String, String> map = new HashMap<>();
        map.put("mobile_no", "" + phoneNumber);
        PostWithJsonWebTask.callPostWithJsonWebtask(ActivityForgotPassword.this, Url.FORGOT_PASSWORD_URL, map, new ServerResponseStringCallback() {
            @Override
            public void onSuccess(String resultJsonObject) {
                try {
                    JSONObject resultJSON = new JSONObject(resultJsonObject);
                    if (resultJSON.optString("result").trim().equalsIgnoreCase("1")) {

                    } else if (resultJSON.optString("result").trim().equalsIgnoreCase("0")) {
                        Util.showMessageWithOk(ActivityForgotPassword.this, "" + resultJSON.optString("data").trim());
                    } else {
                        Util.showMessageWithOk(ActivityForgotPassword.this, "Something went wrong. Please try again.");
                    }
                } catch (Exception e) {

                }
            }

            @Override
            public void ErrorMsg(VolleyError error) {

            }
        }, true, Request.Method.POST);
    }

    public void onCallClick(View view) {
        Toast.makeText(mContext, "Click will invoke call.", Toast.LENGTH_SHORT).show();
    }
}
