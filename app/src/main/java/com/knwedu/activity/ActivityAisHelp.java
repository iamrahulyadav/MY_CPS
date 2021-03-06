package com.knwedu.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.knwedu.calcuttapublicschool.R;
import com.knwedu.ourschool.utils.Constants;
import com.knwedu.ourschool.utils.JsonParser;
import com.knwedu.ourschool.utils.SchoolAppUtils;
import com.knwedu.ourschool.utils.Urls;
import com.knwedu.util.Util;
import com.knwedu.volley.PostWithJsonWebTask;
import com.knwedu.volley.ServerResponseStringCallback;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by ritwik.
 */

public class ActivityAisHelp extends AppCompatActivity {

    private TextView from, btn_send;
    private EditText subject, message;
    String mail_id;
    private Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_ais_help);
        mContext = ActivityAisHelp.this;
        initialize();
    }

    private void initialize() {
        from = (TextView) findViewById(R.id.mail_id);
        btn_send = (TextView) findViewById(R.id.btn_send);
        subject = (EditText) findViewById(R.id.txt_subject);
        message = (EditText) findViewById(R.id.txt_message);

        mail_id = SchoolAppUtils.GetSharedParameter(mContext, Constants.ORGEMAIL).trim();
        from.setText(mail_id);

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int errorCount = 0;
                if (message.getText().toString().isEmpty()) {
                    message.setError("Enter Subject");
                    message.requestFocus();
                    errorCount++;
                } else {
                    message.setError(null);
                }

                if (subject.getText().toString().isEmpty()) {
                    subject.setError("Enter Subject");
                    subject.requestFocus();
                    errorCount++;
                } else {
                    subject.setError(null);
                }


                if (errorCount == 0) {
                    String userEmalID = from.getText().toString().trim();
                    if (TextUtils.isEmpty(userEmalID) || userEmalID.equalsIgnoreCase("null")) {
                        showEditEmailDialog();
                        return;

                    }
                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                    nameValuePairs.add(new BasicNameValuePair("from_email", mail_id));
                    nameValuePairs.add(new BasicNameValuePair("user_id", SchoolAppUtils
                            .GetSharedParameter(mContext, Constants.USERID)));
                    nameValuePairs.add(new BasicNameValuePair("organization_id",
                            SchoolAppUtils.GetSharedParameter(mContext,
                                    Constants.UORGANIZATIONID)));
                    nameValuePairs.add(new BasicNameValuePair("subject", subject.getText().toString()));
                    nameValuePairs.add(new BasicNameValuePair("message", message.getText().toString()));


                    new SendHelpRequest().execute(nameValuePairs);
                }


            }
        });

        if (TextUtils.isEmpty(mail_id)) {
            showEditEmailDialog();
        }
    }


    private class SendHelpRequest extends
            AsyncTask<List<NameValuePair>, Void, Boolean> {
        ProgressDialog dialog;
        String error = "";
        String succ_messgae;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(mContext);
            //dialog.setTitle(getResources().getString(isSchool?R.string.fetch_classes: R.string.fetch_program));
            dialog.setMessage(getResources().getString(R.string.please_wait));
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected Boolean doInBackground(List<NameValuePair>... params) {

            List<NameValuePair> namevaluepair = params[0];


            JsonParser jParser = new JsonParser();
            JSONObject json = jParser.getJSONFromUrlfrist(
                    namevaluepair,
                    SchoolAppUtils.GetSharedParameter(
                            mContext, Constants.COMMON_URL)
                            + Urls.api_ais_help);

            try {

                if (json != null) {
                    if (json.getString("result").equalsIgnoreCase("1")) {
                        try {
                            succ_messgae = json.getString("data");
                        } catch (Exception e) {
                            error = "Error in Data";
                            return false;
                        }
                        return true;
                    } else {
                        try {
                            error = json.getString("data");
                        } catch (Exception e) {
                        }
                        return false;
                    }
                } else {
                    error = getResources().getString(R.string.unknown_response);
                }

            } catch (JSONException e) {
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {

            super.onPostExecute(result);
            if (dialog != null) {
                dialog.dismiss();
                dialog = null;
            }
            if (result) {
                SchoolAppUtils.showDialog(mContext, "Help", succ_messgae);
                subject.setText("");
                message.setText("");
            } else {
                if (error.length() > 0) {
                    SchoolAppUtils.showDialog(mContext, "Help", error);

                } else {
                    SchoolAppUtils.showDialog(mContext, "Help", getResources().getString(R.string.error));

                }
            }
        }
    }

    private void showEditEmailDialog() {

        final Dialog customDialog = new Dialog(mContext);

        customDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        customDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.dialog_edit_email, null);


        //final EditText et_previousEmail= (EditText) view.findViewById(R.id.et_previousEmail);
        final EditText et_newEmail = (EditText) view.findViewById(R.id.et_newEmail);
        final EditText et_confirmEmail = (EditText) view.findViewById(R.id.et_confirmEmail);
        final Button btn_confirm = (Button) view.findViewById(R.id.btn_confirm);
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //String previousEmail= et_previousEmail.getText().toString().trim();
                final String newEmail = et_newEmail.getText().toString().trim();
                //String confirmEmail = et_confirmEmail.getText().toString().trim();
                if (TextUtils.isEmpty(newEmail)) {
                    Toast.makeText(mContext, "Please enter an email id.", Toast.LENGTH_LONG).show();
                    return;
                } else if (!Util.isValidEmail(newEmail)) {

                    Toast.makeText(mContext, "Please enter a valid e-mail id.", Toast.LENGTH_LONG).show();
                    return;
                }
                String organizationId = SchoolAppUtils.GetSharedParameter(mContext, Constants.UORGANIZATIONID);
                String userId = SchoolAppUtils.GetSharedParameter(mContext, Constants.USERID);
                HashMap<String, String> requestMap = new HashMap<>();
                requestMap.put("organization_id", "" + organizationId.trim());
                requestMap.put("email", "" + newEmail);
                requestMap.put("user_id", "" + userId.trim());
                PostWithJsonWebTask.callPostWithJsonWebtask(ActivityAisHelp.this, Urls.base_url + "mobile/updateEmail", requestMap, new ServerResponseStringCallback() {
                    @Override
                    public void onSuccess(String resultJsonObject) {
                        Log.e("Response", "Response: " + resultJsonObject);
                        try {
                            final JSONObject jsonObject = new JSONObject(resultJsonObject);

                            if (jsonObject.optString("result").trim().equalsIgnoreCase("1")) {

                                Toast.makeText(mContext, "Email updated successfully", Toast.LENGTH_LONG).show();
                                from.setText(newEmail);
                                mail_id = newEmail;
                                SchoolAppUtils.SetSharedParameter(mContext, Constants.ORGEMAIL, newEmail);
                                customDialog.dismiss();

                            } else if (jsonObject.optString("result").trim().equalsIgnoreCase("0")) {

                                Toast.makeText(mContext, "Email id already exists.", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(mContext, "Something went wrong! Please try again.", Toast.LENGTH_LONG).show();
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
        });

        customDialog.setCancelable(true);
        customDialog.setContentView(view);
        customDialog.setCanceledOnTouchOutside(false);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(customDialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        customDialog.show();
        customDialog.getWindow().setAttributes(lp);

    }


}
