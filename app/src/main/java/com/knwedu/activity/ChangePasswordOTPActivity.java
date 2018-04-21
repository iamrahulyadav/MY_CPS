package com.knwedu.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.knwedu.college.utils.CollegeDataStructureFramwork;
import com.knwedu.calcuttapublicschool.R;
import com.knwedu.model.UserClass;
import com.knwedu.ourschool.MainActivity;
import com.knwedu.ourschool.ParentMainActivity;
import com.knwedu.ourschool.TeacherMainActivity;
import com.knwedu.ourschool.db.DatabaseAdapter;
import com.knwedu.ourschool.db.SchoolApplication;
import com.knwedu.ourschool.utils.Constants;
import com.knwedu.ourschool.utils.DataStructureFramwork;
import com.knwedu.ourschool.utils.JsonParser;
import com.knwedu.ourschool.utils.SchoolAppUtils;
import com.knwedu.ourschool.utils.Urls;
import com.knwedu.util.AlertDialogCallBack;
import com.knwedu.util.Util;
import com.knwedu.volley.PostWithJsonWebTask;
import com.knwedu.volley.ServerResponseStringCallback;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by knowalladmin on 30/11/17.
 */

public class ChangePasswordOTPActivity extends ActionBarActivity {

    private Context mContext;
    private EditText etNewPassword, etConfirmPassword;
    private String phoneNumber = "";
    UserClass userClass;
    private DatabaseAdapter mDatabase;
    ProgressDialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changepassword);
        mContext = ChangePasswordOTPActivity.this;
        etNewPassword = (EditText) findViewById(R.id.etNewPassword);
        etConfirmPassword = (EditText) findViewById(R.id.etConfirmPassword);

        mDatabase = ((SchoolApplication) getApplication()).getDatabase();
        phoneNumber = getIntent().getStringExtra("phone");
        userClass = Util.fetchUserClass(mContext);
        if (userClass == null) {
            userClass = new UserClass();
        }
    }

    public void onConfirmClick(View view) {

        final String password = etNewPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();

        if (TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword)) {
            Toast.makeText(mContext, "Please fill all the fields.", Toast.LENGTH_SHORT).show();
            return;
        } else if (password.length() < 4) {
            Toast.makeText(mContext, "Password length is too short.", Toast.LENGTH_SHORT).show();
            return;
        } else if (!password.equalsIgnoreCase(confirmPassword)) {
            Toast.makeText(mContext, "Passwords do not match.", Toast.LENGTH_SHORT).show();
            return;

        }
        String organizationId = SchoolAppUtils.GetSharedParameter(mContext, Constants.UORGANIZATIONID);
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("mobile_number", "" + phoneNumber);
        hashMap.put("password", "" + password);
        hashMap.put("organization_id", "" + organizationId);
        PostWithJsonWebTask.callPostWithJsonWebtask(ChangePasswordOTPActivity.this,
                Urls.base_url + "mobile/resetPassword", hashMap, new ServerResponseStringCallback() {
                    @Override
                    public void onSuccess(String resultJsonObject) {

                        try {
                            JSONObject resultJson = new JSONObject(resultJsonObject);
                            if (resultJson.optString("result").trim().equalsIgnoreCase("1")) {
                                Util.showCallBackMessageWithOkCallback(mContext, "" + resultJson.optString("data"), new AlertDialogCallBack() {
                                    @Override
                                    public void onSubmit() {
                                        UserClass userClass = Util.fetchUserClass(mContext);
                                        if (userClass == null) {
                                            userClass = new UserClass();

                                        }
                                        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(4);
                                        nameValuePairs.add(new BasicNameValuePair("email", phoneNumber));
                                        nameValuePairs.add(new BasicNameValuePair("username", phoneNumber));
                                        nameValuePairs.add(new BasicNameValuePair("password", password));
                                        nameValuePairs.add(new BasicNameValuePair("devicetype", "android"));
                                        nameValuePairs.add(new BasicNameValuePair("organization_id", SchoolAppUtils.GetSharedParameter(mContext, Constants.UORGANIZATIONID)));
                                        nameValuePairs.add(new BasicNameValuePair("devicetoken", userClass.getDeviceRegid()));
                                        nameValuePairs.add(new BasicNameValuePair("version_number", userClass.getCurrentVersion()));

                                        new SignInAsyntask().execute(nameValuePairs);


                                        //=============================================================//

                                        userClass.setOTPSignup(true);
                                        userClass.setPhone(phoneNumber);
                                        userClass.setPassword(password);
                                        Util.saveUserClass(mContext, userClass);

                                    }

                                    @Override
                                    public void onCancel() {

                                    }
                                });

                            } else if (resultJson.optString("result").trim().equalsIgnoreCase("0")) {
                                Util.showMessageWithOk(ChangePasswordOTPActivity.this, "" + resultJson.optString("data"));
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

    private int menu_val;

    private class SignInAsyntask extends AsyncTask<List<NameValuePair>, Void, Boolean> {
        DataStructureFramwork.UserInfo userInfo;
        String error = "";
        DataStructureFramwork.Permission permission;
        DataStructureFramwork.PermissionAdd permissionadd;
        DataStructureFramwork.Request requests;
        CollegeDataStructureFramwork.CollegePermission per;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(mContext);
            //dialog.setTitle(getResources().getString(isSchool?R.string.fetch_classes: R.string.fetch_program));
            dialog.setMessage("Signing in! Please wait...");
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(false);
            dialog.show();

        }

        @Override
        protected Boolean doInBackground(List<NameValuePair>... params) {


            List<NameValuePair> nameValuePairs = params[0];
            JsonParser jParser = new JsonParser(mContext);
            JSONObject json = jParser.getJSONFromUrlnew(nameValuePairs, Urls.api_login);

            // Log parameters:
            Log.d("url extension: ", Urls.api_login);
            String parameters = "";
            for (NameValuePair nvp : nameValuePairs) {
                parameters += nvp.getName() + "=" + nvp.getValue() + ",";
            }
            Log.d("Parameters: ", parameters);

            try {

                if (json != null) {
                    if (json.getString("result").equalsIgnoreCase("1")) {
                        JSONObject object = json.getJSONObject("data");
                        String menu_tag;
                        String menu_title;
                        if (SchoolAppUtils.GetSharedParameter(mContext, Constants.APP_TYPE).equals(
                                Constants.APP_TYPE_COMMON_STANDARD)) {
                            menu_tag = json.getString("menu_info");
                            menu_title = json.getString("menu_caption");
                        } else {
                            menu_tag = json.getString("menu_info") + "|webaccess";
                            menu_title = json.getString("menu_caption") + "|Web Access";
                        }

                        //notification counter
                        // String menu_Noticounter=json.getString("menu_counter")+"|web access";
                        JSONObject permission_config = json.getJSONObject("activity_permission");
                        JSONObject request_config = json.getJSONObject("requests");
                        JSONObject permission_add = json.getJSONObject("permission");


                        JSONObject orgConfigJSONObj = json.getJSONObject("org_config");
                        //if(orgConfigJSONObj != null){
                        if (orgConfigJSONObj != null) {
                            //--------------------------------INSURANCE and premium--------------//
                            try {
                                String isInsuranceAvailable = orgConfigJSONObj.getString("is_insurance").trim();
                                String isPremiumServiceAvailable = orgConfigJSONObj.getString("is_premium_service").trim();
                                String transportType = orgConfigJSONObj.getString("transport_mobile_based").trim();
                                SchoolAppUtils.SetSharedParameter(mContext, Constants.INSURANCE_AVIALABLE, isInsuranceAvailable);

                                SchoolAppUtils.SetSharedParameter(mContext, Constants.PREMIUM_SERVICE_AVAILABLE, isPremiumServiceAvailable);

                                SchoolAppUtils.SetSharedParameter(mContext, Constants.NEWTRANSPORT_TYPE, transportType);


                            } catch (Exception e1) {
                                e1.printStackTrace();
                            }
                        }
                        if (object != null) {
                            // user details
                            userInfo = new DataStructureFramwork.UserInfo(object);
                            SchoolAppUtils.SetSharedParameter(mContext, Constants.USERTYPEID, userInfo.usertypeid);
                            SchoolAppUtils.SetSharedParameter(mContext, Constants.USERID, userInfo.userid);
                            SchoolAppUtils.SetSharedParameter(mContext, Constants.UALTEMAIL, userInfo.alt_email);

                            SchoolAppUtils.SetSharedParameter(mContext, Constants.ORGEMAIL, userInfo.org_email);

                            SchoolAppUtils.SetSharedParameter(mContext, Constants.UMOBILENO, userInfo.mobile_no);
                            SchoolAppUtils.SetSharedParameter(mContext, Constants.PASSWORD, etNewPassword.getText().toString().trim());

                            SchoolAppUtils.SetSharedParameter(mContext, Constants.DEVICE_TOKEN, userClass.getDeviceRegid());


                        }
                        // Menu tag
                        if (menu_tag != null) {
                            Log.d("menu", menu_tag.toString());
                            SchoolAppUtils.SetSharedParameter(mContext, Constants.MENU_TAGS, menu_tag.toString());
                            SchoolAppUtils.SetSharedParameter(mContext, Constants.MENU_TITLES, menu_title.toString());
                        }

                        // Permissions
                        if (permission_config != null) {
                            permission = new DataStructureFramwork.Permission(permission_config);
                            SchoolAppUtils.SetSharedParameter(mContext, Constants.ASSIGNMENT_MARK_PERMISSION, permission.assignment_permission.toString());
                            SchoolAppUtils.SetSharedParameter(mContext, Constants.TEST_MARK_PERMISSION, permission.test_permission.toString());
                            SchoolAppUtils.SetSharedParameter(mContext, Constants.ACTIVITY_MARK_PERMISSION, permission.activity_permission.toString());
                        }

                        // Parent Request
                        if (request_config != null) {
                            requests = new DataStructureFramwork.Request(request_config);
                            SchoolAppUtils.SetSharedParameter(mContext, Constants.LEAVE_REQUEST, requests.leave_request.toString());
                            SchoolAppUtils.SetSharedParameter(mContext, Constants.REQUEST_FOR_BOOK, requests.request_for_book.toString());
                            SchoolAppUtils.SetSharedParameter(mContext, Constants.REQUEST_FOR_ID_CARD, requests.request_for_id.toString());
                            SchoolAppUtils.SetSharedParameter(mContext, Constants.REQUEST_FOR_UNIFORM, requests.request_for_uniform.toString());
                            SchoolAppUtils.SetSharedParameter(mContext, Constants.SPECIAL_REQUEST, requests.special_request.toString());
                        }

                        // Permission Add
                        if (permission_add != null) {
                            permissionadd = new DataStructureFramwork.PermissionAdd(permission_add);
                            SchoolAppUtils.SetSharedParameter(mContext, Constants.ASSIGNMENT_ADD_PERMISSION, permissionadd.assignment_add.toString());
                            SchoolAppUtils.SetSharedParameter(mContext, Constants.TEST_ADD_PERMISSION, permissionadd.test_add.toString());
                            SchoolAppUtils.SetSharedParameter(mContext, Constants.ACTIVITY_ADD_PERMISSION, permissionadd.activity_add.toString());
                            SchoolAppUtils.SetSharedParameter(mContext, Constants.ANNOUNCEMENT_ADD_PERMISSION, permissionadd.announcement_add.toString());
                            SchoolAppUtils.SetSharedParameter(mContext, Constants.COURSEWORK_ADD_PERMISSION, permissionadd.classwork_add.toString());
                            SchoolAppUtils.SetSharedParameter(mContext, Constants.LESSONS_ADD_PERMISSION, permissionadd.lessons_add.toString());
                            SchoolAppUtils.SetSharedParameter(mContext, Constants.ATTENDANCE_TYPE_PERMISSION, permissionadd.attendance_type.toString());
                            Log.d("assignment", permissionadd.attendance_type.toString());
                        }

                        if (userInfo.usertypeid.equalsIgnoreCase(Constants.USERTYPE_PARENT)) {

                            JSONArray child_info_arry = json.getJSONArray("child_info");
                            JSONObject object_chield_info = child_info_arry.getJSONObject(0);
                            DataStructureFramwork.StudentProfileInfo selectedStudent = new DataStructureFramwork.StudentProfileInfo(object_chield_info);
                            SchoolAppUtils.SetSharedParameter(mContext, Constants.CHILD_ID, selectedStudent.user_id);
                            SchoolAppUtils.SetSharedParameter(mContext, Constants.CHILD_NAME, selectedStudent.fullname);
                            SchoolAppUtils.SetSharedParameter(mContext, Constants.STUDENT_CLASS_SECTION, selectedStudent.class_section);
                            SchoolAppUtils.SetSharedParameter(mContext, Constants.SELECTED_CHILD_OBJECT, object_chield_info.toString());
                            SchoolAppUtils.SetSharedParameter(mContext, Constants.SECTION_ID, selectedStudent.section_id);

                        } else if (userInfo.usertypeid.equalsIgnoreCase(Constants.USERTYPE_STUDENT)) {
                            SchoolAppUtils.SetSharedParameter(mContext, Constants.CHILD_ID, userInfo.userid);
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

            if (result) {
                if (!SchoolAppUtils.GetSharedParameter(mContext, Constants.USERTYPEID).equalsIgnoreCase(Constants.USERTYPE_PARENT)) {

                    SchoolAppUtils.SetSharedParameter(mContext, Constants.USERINFO, userInfo.toString());

                    if (SchoolAppUtils.GetSharedParameter(mContext, Constants.USERTYPEID).equalsIgnoreCase(Constants.USERTYPE_STUDENT)) {
                        SchoolAppUtils.SetSharedBoolParameter(mContext, Constants.UISSIGNIN, true);
                        Intent ints = new Intent(mContext, MainActivity.class);
                        ints.putExtra("fromlogin", "1");
                        ints.putExtra("menu_val", menu_val);
                        ints.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(ints);
                        if (dialog != null) {
                            dialog.dismiss();
                            dialog = null;
                        }
                        finish();
                    } else if (SchoolAppUtils.GetSharedParameter(mContext, Constants.USERTYPEID).equalsIgnoreCase(Constants.USERTYPE_TEACHER)) {
                        SchoolAppUtils.SetSharedBoolParameter(mContext, Constants.ISSIGNIN, true);

                        // Download data for offline access
                        new GetOfflineDataAsyntask().execute();
                    }
                } else {
                    SchoolAppUtils.SetSharedBoolParameter(mContext, Constants.ISSIGNIN, true);
                    SchoolAppUtils.SetSharedBoolParameter(mContext, Constants.ISPARENTSIGNIN, true);

                    Intent intent = new Intent(mContext, ParentMainActivity.class);
                    intent.putExtra("menu_val", menu_val);
                    intent.putExtra("prompt", "1");
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);

                    if (dialog != null) {
                        dialog.dismiss();
                        dialog = null;
                    }
                    finish();
                }

            }

        }

        private class GetOfflineDataAsyntask extends AsyncTask<Void, Void, Boolean> {
            String error;
            private ArrayList<DataStructureFramwork.Subject> subjects;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                subjects = new ArrayList<DataStructureFramwork.Subject>();
                subjects.clear();
            }

            @Override
            protected Boolean doInBackground(Void... params) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
                nameValuePairs.add(new BasicNameValuePair("user_type_id", SchoolAppUtils.GetSharedParameter(mContext, Constants.USERTYPEID)));
                nameValuePairs.add(new BasicNameValuePair("organization_id", SchoolAppUtils.GetSharedParameter(mContext, Constants.UORGANIZATIONID)));
                nameValuePairs.add(new BasicNameValuePair("user_id", SchoolAppUtils.GetSharedParameter(mContext, Constants.USERID)));
                // Log parameters:
                Log.d("url extension: ", Urls.api_activity_get_subjects);
                String parameters = "";
                for (NameValuePair nvp : nameValuePairs) {
                    parameters += nvp.getName() + "=" + nvp.getValue() + ",";
                }
                Log.d("Parameters: ", parameters);

                JsonParser jParser = new JsonParser();
                JSONObject json = jParser.getJSONFromUrlnew(nameValuePairs, Urls.api_activity_get_subjects);
                try {

                    if (json != null) {
                        if (json.getString("result").equalsIgnoreCase("1")) {

                            JSONArray array;
                            try {
                                array = json.getJSONArray("data");
                            } catch (Exception e) {
                                return true;
                            }
                            for (int i = 0; i < array.length(); i++) {
                                DataStructureFramwork.Subject subject = new DataStructureFramwork.Subject(array.getJSONObject(i));

                                subjects.add(subject);
                            }

                            return true;
                        } else {
                            try {
                                error = json.getString("data");
                            } catch (Exception e) {
                            }
                            return false;
                        }
                    }

                } catch (JSONException e) {

                }
                return false;
            }

            @Override
            protected void onPostExecute(Boolean result) {

                super.onPostExecute(result);
                if (subjects != null) {

                    mDatabase.open();
                    // Delete all data in database
                    mDatabase.deleteAllSubjects();
                    for (int i = 0; i < subjects.size(); i++) {
                        Log.d("Insert: ", "Inserting .." + i);
                        mDatabase.addSubjects(subjects.get(i));
                    }
                    mDatabase.close();

                }

                // Go to Teacher Main Page
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
                nameValuePairs.add(new BasicNameValuePair("user_type_id", SchoolAppUtils.GetSharedParameter(mContext, Constants.USERTYPEID)));
                nameValuePairs.add(new BasicNameValuePair("organization_id", SchoolAppUtils.GetSharedParameter(mContext, Constants.UORGANIZATIONID)));
                nameValuePairs.add(new BasicNameValuePair("user_id", SchoolAppUtils.GetSharedParameter(mContext, Constants.USERID)));
                new GetListAsyntask().execute(nameValuePairs);
                // Go to Teacher Main Page

            }

        }

    }

    private ArrayList<DataStructureFramwork.TimeTable> timeTable;

    private class GetListAsyntask extends AsyncTask<List<NameValuePair>, Void, Boolean> {
        String error;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(List<NameValuePair>... params) {
            timeTable = new ArrayList<DataStructureFramwork.TimeTable>();
            List<NameValuePair> nvp = params[0];
            JsonParser jParser = new JsonParser();
            JSONObject json = jParser.getJSONFromUrlnew(nvp, Urls.api_timetable_user);
            try {

                if (json != null) {
                    if (json.getString("result").equalsIgnoreCase("1")) {
                        JSONArray array;
                        try {
                            array = json.getJSONArray("data");
                        } catch (Exception e) {
                            return true;
                        }
                        timeTable = new ArrayList<DataStructureFramwork.TimeTable>();
                        for (int i = 0; i < array.length(); i++) {
                            DataStructureFramwork.TimeTable attendance = new DataStructureFramwork.TimeTable(array.getJSONObject(i));
                            timeTable.add(attendance);
                        }

                        return true;
                    } else {
                        try {
                            error = json.getString("data");
                        } catch (Exception e) {
                        }
                        return false;
                    }
                }

            } catch (JSONException e) {

            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {

            super.onPostExecute(result);
            if (timeTable != null) {
                if (timeTable.size() > 0) {
                    mDatabase.open();
                    // Delete all data in database
                    mDatabase.deleteAllTimetable();
                    for (int i = 0; i < timeTable.size(); i++) {
                        Log.d("Insert: ", "Inserting ..");
                        mDatabase.addTimetable(timeTable.get(i));
                    }
                    mDatabase.close();

                }
            }

            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
            nameValuePairs.add(new BasicNameValuePair("user_type_id", SchoolAppUtils.GetSharedParameter(mContext, Constants.USERTYPEID)));
            nameValuePairs.add(new BasicNameValuePair("organization_id", SchoolAppUtils.GetSharedParameter(mContext, Constants.UORGANIZATIONID)));
            nameValuePairs.add(new BasicNameValuePair("user_id", SchoolAppUtils.GetSharedParameter(mContext, Constants.USERID)));
            new GetListAttendanceAsyntask().execute(nameValuePairs);

        }
    }

    private class GetListAttendanceAsyntask extends AsyncTask<List<NameValuePair>, Void, Boolean> {
        String error;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(List<NameValuePair>... params) {
            timeTable = new ArrayList<DataStructureFramwork.TimeTable>();
            List<NameValuePair> nvp = params[0];
            JsonParser jParser = new JsonParser();
            JSONObject json = jParser.getJSONFromUrlnew(nvp, Urls.api_teacher_offline_attendance_user);
            try {

                if (json != null) {
                    if (json.getString("result").equalsIgnoreCase("1")) {
                        JSONArray array;
                        try {
                            array = json.getJSONArray("data");
                        } catch (Exception e) {
                            return true;
                        }
                        timeTable = new ArrayList<DataStructureFramwork.TimeTable>();
                        for (int i = 0; i < array.length(); i++) {
                            DataStructureFramwork.TimeTable attendance = new DataStructureFramwork.TimeTable(array.getJSONObject(i));
                            timeTable.add(attendance);
                        }

                        return true;
                    } else {
                        try {
                            error = json.getString("data");
                        } catch (Exception e) {
                        }
                        return false;
                    }
                }

            } catch (JSONException e) {

            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {

            super.onPostExecute(result);
			/*
			 * if (dialog != null) { dialog.dismiss(); dialog = null; }
			 */
            if (timeTable != null) {
                if (timeTable.size() > 0) {
                    mDatabase.open();
                    // Delete all data in database
                    mDatabase.deleteAllofflineattendance();
                    for (int i = 0; i < timeTable.size(); i++) {
                        Log.d("Insert: ", "Inserting ..");
                        mDatabase.addOfflineteacherattendance(timeTable.get(i));
                    }
                    mDatabase.close();

                }
            }

            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
            nameValuePairs.add(new BasicNameValuePair("user_type_id", SchoolAppUtils.GetSharedParameter(mContext, Constants.USERTYPEID)));
            nameValuePairs.add(new BasicNameValuePair("organization_id", SchoolAppUtils.GetSharedParameter(mContext, Constants.UORGANIZATIONID)));
            nameValuePairs.add(new BasicNameValuePair("user_id", SchoolAppUtils.GetSharedParameter(mContext, Constants.USERID)));
            new GetOfflineDataStudentAsyntask().execute(nameValuePairs);

        }
    }

    // OFFLINE DATABASE INITIALIZATION
    private class GetOfflineDataStudentAsyntask extends AsyncTask<List<NameValuePair>, Void, Boolean> {
        String error;
        private ArrayList<DataStructureFramwork.Attendance> students;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Boolean doInBackground(List<NameValuePair>... params) {

            // Log parameters:
            students = new ArrayList<DataStructureFramwork.Attendance>();
            List<NameValuePair> nameValuePairs = params[0];
            Log.d("url extension: ", Urls.api_students_info);
            String parameters = "";
            for (NameValuePair nvp : nameValuePairs) {
                parameters += nvp.getName() + "=" + nvp.getValue() + ",";
            }
            Log.d("Parameters: ", parameters);

            JsonParser jParser = new JsonParser();
            JSONObject json = jParser.getJSONFromUrlnew(nameValuePairs, Urls.api_students_info);
            try {

                if (json != null) {
                    if (json.getString("result").equalsIgnoreCase("1")) {

                        JSONArray array;
                        try {
                            array = json.getJSONArray("data");
                        } catch (Exception e) {
                            return true;
                        }
                        for (int i = 0; i < array.length(); i++) {
                            DataStructureFramwork.Attendance subject = new DataStructureFramwork.Attendance(array.getJSONObject(i));
                            students.add(subject);
                        }

                        return true;
                    } else {
                        try {
                            error = json.getString("data");
                        } catch (Exception e) {
                        }
                        return false;
                    }
                }

            } catch (JSONException e) {

            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {

            super.onPostExecute(result);

            if (students != null) {

                mDatabase.open();
                // Delete all data in database
                mDatabase.deleteAllStudents();
                for (int i = 0; i < students.size(); i++) {
                    Log.d("Insert: ", "Inserting .." + i);
                    mDatabase.addStudents(students.get(i));
                }
                mDatabase.close();
                Intent intent = new Intent(mContext, TeacherMainActivity.class);
                intent.putExtra("menu_val", menu_val);
                intent.putExtra("prompt", "1");

                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

                if (dialog != null) {
                    dialog.dismiss();
                    dialog = null;
                }
                finish();

            }

        }

    }
}

