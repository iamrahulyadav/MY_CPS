package com.knwedu.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.knwedu.adapter.StudentAdapter;
import com.knwedu.adapter.SubjectAdapter;
import com.knwedu.calcuttapublicschool.R;
import com.knwedu.model.Parent;
import com.knwedu.model.Student;
import com.knwedu.model.Teacher;
import com.knwedu.model.UserClass;
import com.knwedu.ourschool.utils.Constants;
import com.knwedu.ourschool.utils.SchoolAppUtils;
import com.knwedu.ourschool.utils.Urls;
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
 * Created by knowalladmin on 21/11/17.
 */

public class ChangePassVerifyOTPActivity extends ActionBarActivity {

    private Context mContext;
    private EditText et_otp;
    private UserClass userClass;
    private String phoneNumber;
    private String otp;
    SmsVerifyCatcher smsVerifyCatcher;
    private TextView tv_timer;
    private TextView tv_waiting_msg;
    private Button btn_resendOtp;
    private LinearLayout ll_waitingOtp;
    private TextView tv_timeoutMessage;
    private CountDownTimer mCountDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redeem_otp);
        mContext = ChangePassVerifyOTPActivity.this;

        et_otp = (EditText) findViewById(R.id.et_otp);
        userClass = Util.fetchUserClass(mContext);
        phoneNumber = getIntent().getStringExtra("phoneNumber").trim();
        tv_timer = (TextView) findViewById(R.id.tv_timer);
        tv_waiting_msg = (TextView) findViewById(R.id.tv_waiting_msg);
        btn_resendOtp = (Button) findViewById(R.id.btn_resendOtp);
        ll_waitingOtp = (LinearLayout) findViewById(R.id.ll_waitingOtp);
        tv_timeoutMessage = (TextView) findViewById(R.id.tv_timeoutMessage);

        otp = getIntent().getStringExtra("otp").trim();
        Log.e("phoneNumber", "phoneNumber " + phoneNumber);
        Log.e("otp", "otp " + otp);
        smsVerifyCatcher = new SmsVerifyCatcher(this, new OnSmsCatchListener<String>() {
            @Override
            public void onSmsCatch(String message) {
                String code = parseCode(message);//Parse verification code
                et_otp.setText(code);//set code in edit text
                mCountDownTimer.cancel();
                tv_waiting_msg.setText("OTP received.");
                tv_timer.setText("");
            }
        });

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

    public void onConfirmOTPClicked(View v) {

        String otp = et_otp.getText().toString().trim();

        if (TextUtils.isEmpty(otp)) {
            Util.showMessageWithOk(ChangePassVerifyOTPActivity.this, "Please enter the OTP received.");
            return;
        } else if (otp.length() < 6) {
            Util.showMessageWithOk(ChangePassVerifyOTPActivity.this, "Please enter the correct OTP.");
            return;
        } else if (!this.otp.trim().equalsIgnoreCase(otp)) {
            Util.showMessageWithOk(ChangePassVerifyOTPActivity.this, "OTP mismatch! Please check.");
            return;
        }
        String organizationId = SchoolAppUtils.GetSharedParameter(mContext, Constants.UORGANIZATIONID);
        HashMap<String, String> requestMap = new HashMap<>();
        requestMap.put("mobile_number", "" + phoneNumber);
        requestMap.put("otp", "" + otp);
        requestMap.put("organization_id",""+organizationId.trim());

        PostWithJsonWebTask.callPostWithJsonWebtask(ChangePassVerifyOTPActivity.this, Urls.base_url + "mobile/verifyOtp", requestMap,
                new ServerResponseStringCallback() {
                    @Override
                    public void onSuccess(String resultJsonObject) {
                        try {
                            JSONObject jsonObject = new JSONObject(resultJsonObject);

                            if (jsonObject.optString("result").trim().equalsIgnoreCase("1")) {
                                Toast.makeText(ChangePassVerifyOTPActivity.this,"OTP Verified",Toast.LENGTH_SHORT).show();
                                    Intent intent= new Intent(mContext,ChangePasswordFromOTPActivity.class);
                                    intent.putExtra("phone",""+phoneNumber);
                                    startActivity(intent);
                                    finish();


                            } else if (jsonObject.optString("result").trim().equalsIgnoreCase("0")) {

                                Toast.makeText(ChangePassVerifyOTPActivity.this,"Wrong OTP.",Toast.LENGTH_SHORT).show();
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

    public void onResendOTPClicked(View v) {

        String organizationId = SchoolAppUtils.GetSharedParameter(mContext, Constants.UORGANIZATIONID);

        HashMap<String, String> requestMap = new HashMap<>();
        requestMap.put("mobile_number", "" + phoneNumber);
        requestMap.put("organization_id", "" + organizationId.trim());
        PostWithJsonWebTask.callPostWithJsonWebtask(ChangePassVerifyOTPActivity.this, Urls.base_url + "mobile/otp_generator", requestMap, new ServerResponseStringCallback() {
            @Override
            public void onSuccess(String resultJsonObject) {
                Log.e("Response", "Response: " + resultJsonObject);
                try {
                    final JSONObject jsonObject = new JSONObject(resultJsonObject);

                    if (jsonObject.optString("result").trim().equalsIgnoreCase("1")) {

                        UserClass userClass = Util.fetchUserClass(mContext);
                        userClass.setSentOtp(jsonObject.optString("otp"));
                        Util.saveUserClass(mContext, userClass);

                        String newOtp = jsonObject.optString("otp");
                        otp = newOtp;
                        runCountdownTimer();

                    } else {

                        Util.showMessageWithOk(ChangePassVerifyOTPActivity.this, "Sorry! Your mobile number is not registered.");
                    }

                } catch (JSONException e) {

                }
            }

            @Override
            public void ErrorMsg(VolleyError error) {


            }
        }, true, Request.Method.POST);

    }

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
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        smsVerifyCatcher.onRequestPermissionsResult(requestCode, permissions, grantResults);
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

    private void showTeacherDetailsDialog(Teacher teacher) {
        final Dialog dialog = new Dialog(mContext);
        dialog.setContentView(R.layout.dialog_otp_details);
        TextView tv_userType = (TextView) dialog.findViewById(R.id.tv_userType);
        TextView tv_Name = (TextView) dialog.findViewById(R.id.tv_Name);
        TextView tv_mobile = (TextView) dialog.findViewById(R.id.tv_mobile);
        TextView tv_class = (TextView) dialog.findViewById(R.id.tv_class);
        TextView tv_section = (TextView) dialog.findViewById(R.id.tv_section);

        ListView lv_subDetails = (ListView) dialog.findViewById(R.id.lv_subDetails);

        tv_userType.setText("Teacher");
        tv_Name.setText(teacher.teacherName);
        tv_mobile.setText(teacher.teacherMobileNo);
        tv_class.setVisibility(View.GONE);
        tv_section.setVisibility(View.GONE);

        SubjectAdapter adapter = new SubjectAdapter(mContext, teacher.subjects);
        lv_subDetails.setAdapter(adapter);

        Button btnOk = (Button) dialog.findViewById(R.id.btn_ok);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                gotoChangePassword();

            }
        });
        dialog.show();
    }

    private void showParentDetailsDialog(Parent parent) {
        final Dialog dialog = new Dialog(mContext);
        dialog.setContentView(R.layout.dialog_otp_details);
        Button btnOk = (Button) dialog.findViewById(R.id.btn_ok);
        TextView tv_userType = (TextView) dialog.findViewById(R.id.tv_userType);
        TextView tv_Name = (TextView) dialog.findViewById(R.id.tv_Name);
        TextView tv_mobile = (TextView) dialog.findViewById(R.id.tv_mobile);
        TextView tv_class = (TextView) dialog.findViewById(R.id.tv_class);
        TextView tv_section = (TextView) dialog.findViewById(R.id.tv_section);

        ListView lv_subDetails = (ListView) dialog.findViewById(R.id.lv_subDetails);

        tv_userType.setText("Parent");
        tv_Name.setText(parent.parentName);
        tv_mobile.setText(parent.parentMobile);

        tv_class.setVisibility(View.GONE);
        tv_section.setVisibility(View.GONE);

        StudentAdapter adapter = new StudentAdapter(mContext, parent.students);
        lv_subDetails.setAdapter(adapter);

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                gotoChangePassword();
            }
        });
        dialog.show();
    }

    private void showStudentDetailsDialog(Student student) {
        final Dialog dialog = new Dialog(mContext);
        dialog.setContentView(R.layout.dialog_otp_details);
        Button btnOk = (Button) dialog.findViewById(R.id.btn_ok);
        TextView tv_userType = (TextView) dialog.findViewById(R.id.tv_userType);
        TextView tv_Name = (TextView) dialog.findViewById(R.id.tv_Name);
        TextView tv_mobile = (TextView) dialog.findViewById(R.id.tv_mobile);
        TextView tv_class = (TextView) dialog.findViewById(R.id.tv_class);
        TextView tv_section = (TextView) dialog.findViewById(R.id.tv_section);

        ListView lv_subDetails = (ListView) dialog.findViewById(R.id.lv_subDetails);
        lv_subDetails.setVisibility(View.GONE);

        tv_userType.setText("Student");
        tv_Name.setText(student.studentName);
        tv_mobile.setText(student.studetMobile);
        tv_class.setText(student.studentClass);
        tv_section.setText(student.studentSection);

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                gotoChangePassword();
            }
        });
        dialog.show();
    }

    private void gotoChangePassword(){
        Intent intent= new Intent(mContext,ChangePasswordOTPActivity.class);
        intent.putExtra("phone",phoneNumber);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
