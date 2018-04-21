package com.knwedu.activity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Window;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.knwedu.calcuttapublicschool.R;
import com.knwedu.college.utils.CollegeUrls;
import com.knwedu.constant.Url;
import com.knwedu.ourschool.AisSignInActivity;
import com.knwedu.ourschool.MainActivity;
import com.knwedu.ourschool.ParentMainActivity;
import com.knwedu.ourschool.TeacherMainActivity;
import com.knwedu.ourschool.activity.LandingScreenActivity;
import com.knwedu.ourschool.db.DatabaseAdapter;
import com.knwedu.ourschool.db.SchoolApplication;
import com.knwedu.ourschool.utils.Constants;
import com.knwedu.ourschool.utils.DataStructureFramwork;
import com.knwedu.ourschool.utils.JsonParser;
import com.knwedu.ourschool.utils.SchoolAppUtils;
import com.knwedu.ourschool.utils.Urls;
import com.knwedu.util.Util;
import com.knwedu.view.KenBurnsView;
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
 * Created by knowalladmin on 16/01/18.
 */

public class ActivitySplash extends Activity {
    private Context mContext;
    private DatabaseAdapter mDatabase;
    private int menu_val;
    String curVersion;
    private ProgressDialog dialog;
    private Handler mHandler;
    private ServiceWaitThread mThread;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE); //Removing ActionBar
        setContentView(R.layout.activity_splash_new);
        mContext = ActivitySplash.this;

        new Urls(Url.BASE_URL);
        mDatabase = ((SchoolApplication) getApplication()).getDatabase();

        // ==========Handle update from school to common app=============
        if (SchoolAppUtils.GetSharedParameter(mContext, Constants.INSTITUTION_TYPE).equals("0")) {
            SchoolAppUtils.SetSharedParameter(mContext, Constants.USERID, "0");
            SchoolAppUtils.SetSharedParameter(mContext, Constants.UORGANIZATIONID, "0");

        }
        Bundle extras = getIntent().getExtras();
        if (null != extras) {
            menu_val = extras.getInt("menu_val");
        }
        curVersion = "0";
        try {
            curVersion = getApplicationContext().getPackageManager().getPackageInfo(getApplicationInfo().packageName, 0).versionName;
            Log.d("Current Version", curVersion + getApplicationInfo().packageName.toString());
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        new SplashTimerTask().execute();
    }

    /**
     * The Splash Timer. duration ---> 2345ms
     *
     * @params {@link AsyncTask}
     */
    private class SplashTimerTask extends AsyncTask<Void, Void, Void> {

        private ProgressDialog mProgressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(ActivitySplash.this);
            mProgressDialog.setCancelable(false);
            mProgressDialog.setMessage("Loading! Please wait...");
            // mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            try {
                Thread.sleep(2345);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if (mProgressDialog.isShowing())
                mProgressDialog.dismiss();
            checkLoginParameters();
        }
    }

    private void checkLoginParameters() {
        //No Internet
        if (!SchoolAppUtils.isOnline(mContext)) {
            // Check already Logged in or not
            if (Util.fetchSelectedMenuCategory(mContext) != null) {
                // Already Logged In
                goToMainActivity();

            } else {
                SchoolAppUtils.showDialog(mContext, "Alert", "No internet Connectivity");
            }
        }
        // Internet Available
        else {
            // ----------------Current version check-----------------
            //new VersionCheckAsync().execute();
            checkAppVersion();
        }

    }

    private void checkAppVersion() {
        HashMap<String, String> map = new HashMap<>();
        map.put("app_name", "cps");
        map.put("type", "android");
        PostWithJsonWebTask.callPostWithJsonWebtask(ActivitySplash.this, Url.GET_APP_VERSION_URL, map, new ServerResponseStringCallback() {
            @Override
            public void onSuccess(String resultJsonObject) {
                try {
                    JSONObject json = new JSONObject(resultJsonObject);
                    if (json.optString("result").trim().equalsIgnoreCase("1")) {
                            String playstore_version = json.getString("data");
                            Log.d("Playstore version", playstore_version);
                            Version a = new Version(curVersion);
                            Version b = new Version(playstore_version);
                            Log.d("Version Check", "Current: " + a.get() + " Latest:" + b.get() + " Compare:" + a.compareTo(b));
                            if (a.compareTo(b) >= 0) {
                                // Redirect user to menu, or login
                                // Already Logged in
                                if (Util.fetchSelectedMenuCategory(mContext) != null && Util.fetchRoles(mContext) != null) {
                                    goToMainActivity();
                                }
                                // Not Logged in
                                else {
                                    // Go to Sign in page
                                    goToLoginActivity();
                                }

                            } else {
                                Intent i = new Intent(android.content.Intent.ACTION_VIEW);
                                i.setData(Uri.parse("https://play.google.com/store/apps/details?id=" + getApplicationInfo().packageName));
                                startActivity(Intent.createChooser(i, "Update KnwEdu CPS using.."));
                            }

                        } else if (json.optString("result").trim().equalsIgnoreCase("0")) {
                            Util.showMessageWithOk(ActivitySplash.this, "" + json.optString("data"));
                        } else {
                            Util.showMessageWithOk(ActivitySplash.this, "Please check your internet and try again.");
                        }

                } catch (JSONException e) {
                    Util.showMessageWithOk(ActivitySplash.this, "Please check your internet and try again.");
                }
            }

            @Override
            public void ErrorMsg(VolleyError error) {

            }
        }, true, Request.Method.POST);
    }

    /* private class VersionCheckAsync extends AsyncTask<Void, Void, Boolean> {
         String error;
         String playstore_version;

         @Override
         protected void onPreExecute() {
             super.onPreExecute();
             playstore_version = "0";
         }

         @Override
         protected Boolean doInBackground(Void... params) {

             // JSONObject json = jParser.getJSONFromUrlfrist(nameValuePairs, Urls.url_playstore_version_common);
             try {

                 if (json != null) {
                     if (json.getString("status").equalsIgnoreCase("1")) {

                         playstore_version = json.getString("data");
                         Log.d("Playstore version", playstore_version);

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
             if (result) {
                 Version a = new Version(curVersion);
                 Version b = new Version(playstore_version);
                 Log.d("Version Check", "Current: " + a.get() + " Latest:" + b.get() + " Compare:" + a.compareTo(b));
                 if (a.compareTo(b) >= 0) {
                     // Redirect user to menu, or login
                     // Already Logged in
                     if (Util.fetchSelectedMenuCategory(mContext) != null && Util.fetchRoles(mContext) != null) {
                         goToMainActivity();
                     }
                     // Not Logged in
                     else {
                         // Go to Sign in page
                         goToLoginActivity();
                     }

                 } else {
                     Intent i = new Intent(android.content.Intent.ACTION_VIEW);
                     i.setData(Uri.parse("https://play.google.com/store/apps/details?id=" + getApplicationInfo().packageName));
                     startActivity(Intent.createChooser(i, "Update KnwEdu CPS using.."));
                 }
             } else {
                 if (dialog != null) {
                     dialog.dismiss();
                     dialog = null;
                 }
                 if (error != null) {
                     if (error.length() > 0) {
                         SchoolAppUtils.showDialog(mContext, getResources().getString(R.string.unknown_response), error);
                     } else {
                         SchoolAppUtils.showDialog(mContext, getResources().getString(R.string.unknown_response), getResources().getString(R.string.please_check_internet_connection));
                     }
                 } else {
                     SchoolAppUtils.showDialog(mContext, getResources().getString(R.string.unknown_response), getResources().getString(R.string.please_check_internet_connection));
                 }

             }
         }
     }
 */
    public class Version implements Comparable<Version> {

        private String version;

        public final String get() {
            return this.version;
        }

        public Version(String version) {
            if (version == null)
                throw new IllegalArgumentException("Version can not be null");
            if (!version.matches("[0-9]+(\\.[0-9]+)*"))
                throw new IllegalArgumentException("Invalid version format");
            this.version = version;
        }

        @Override
        public int compareTo(Version that) {
            if (that == null)
                return 1;
            String[] thisParts = this.get().split("\\.");
            String[] thatParts = that.get().split("\\.");
            int length = Math.max(thisParts.length, thatParts.length);
            for (int i = 0; i < length; i++) {
                int thisPart = i < thisParts.length ? Integer.parseInt(thisParts[i]) : 0;
                int thatPart = i < thatParts.length ? Integer.parseInt(thatParts[i]) : 0;
                if (thisPart < thatPart)
                    return -1;
                if (thisPart > thatPart)
                    return 1;
            }
            return 0;
        }

        @Override
        public boolean equals(Object that) {
            if (this == that)
                return true;
            if (that == null)
                return false;
            if (this.getClass() != that.getClass())
                return false;
            return this.compareTo((Version) that) == 0;
        }

    }

    private void goToMainActivity() {

        Log.e("TAG", "goToMainActivity");
        SchoolAppUtils.SetSharedParameter(mContext, Constants.COMMON_URL, Url.BASE_URL);
        Intent intent = new Intent(ActivitySplash.this, ActivityMain.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();

    }

    private void goToLoginActivity() {
        Log.e("TAG", "goToLoginActivity");
        SchoolAppUtils.SetSharedParameter(mContext, Constants.COMMON_URL, Url.BASE_URL);
        Intent intent = new Intent(ActivitySplash.this, ActivityVerifyPhoneNumber.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }


    class PasswordCheck extends AsyncTask<List<NameValuePair>, Void, Boolean> {
        ProgressDialog dialog;
        String pass_check;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);

            if (pass_check != null) {
                if (pass_check.equalsIgnoreCase("match")) {

                    // Check for Latest version
                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(5);
                    nameValuePairs.add(new BasicNameValuePair("user_type_id", SchoolAppUtils.GetSharedParameter(getApplicationContext(), Constants.USERTYPEID)));
                    nameValuePairs.add(new BasicNameValuePair("organization_id", SchoolAppUtils.GetSharedParameter(getApplicationContext(), Constants.UORGANIZATIONID)));
                    nameValuePairs.add(new BasicNameValuePair("user_id", SchoolAppUtils.GetSharedParameter(getApplicationContext(), Constants.USERID)));
                    nameValuePairs.add(new BasicNameValuePair("devicetype", "android"));
                    nameValuePairs.add(new BasicNameValuePair("version_number", curVersion));
                    nameValuePairs.add(new BasicNameValuePair("app_type", SchoolAppUtils.GetSharedParameter(getApplicationContext(), Constants.APP_TYPE)));
                    new UploadAsyntask().execute(nameValuePairs);
                } else {

                    if (dialog != null) {
                        dialog.dismiss();
                        dialog = null;
                    }
                    startActivity(new Intent(mContext, LandingScreenActivity.class));

                }
            }
        }

        @Override
        protected Boolean doInBackground(List<NameValuePair>... params) {

            List<NameValuePair> nameValuePairs = params[0];
            JsonParser jParser = new JsonParser();
            JSONObject json = jParser.getJSONFromUrlnew(nameValuePairs, Urls.api_password);
            if (json != null) {

                try {
                    if (json.getString("result").equalsIgnoreCase("1")) {
                        pass_check = "match";
                    } else {
                        pass_check = "missmatch";
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    return true;
                }
            }
            return false;
        }
    }

    private class ServiceWaitThread extends Thread {
        @Override
        public void run() {
            try {
                sleep(3000);
            } catch (InterruptedException e) {
                throw new RuntimeException(
                        "waiting thread sleep() has been interrupted");
            }

            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    onServiceReady();
                }
            });
            mThread = null;
        }
    }

    protected void onServiceReady() {
       /* startActivity(new Intent(mContext, LandingScreenActivity.class));
        finish();*/
    }

    private class UploadAsyntask extends AsyncTask<List<NameValuePair>, Void, Boolean> {
        String error;
        DataStructureFramwork.Permission permission;
        DataStructureFramwork.PermissionAdd permissionadd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(mContext);
            dialog.setTitle(getResources().getString(R.string.please_wait));
            dialog.setMessage(getResources().getString(R.string.please_wait));
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(false);
            //dialog.show();
        }

        @Override
        protected Boolean doInBackground(List<NameValuePair>... params) {
            List<NameValuePair> namevaluepair = params[0];
            JsonParser jParser = new JsonParser();
            JSONObject json = jParser.getJSONFromUrlnew(namevaluepair, Urls.api_splash_version);
            String menu_tag;
            String menu_title;
            String transport_type;
            try {

                if (json != null) {
                    if (json.getString("result").equalsIgnoreCase("1")) {
                        if (SchoolAppUtils.GetSharedParameter(mContext, Constants.APP_TYPE).equals(Constants.APP_TYPE_COMMON_STANDARD)) {
                            menu_tag = json.getString("menu_info");
                            menu_title = json.getString("menu_caption");
                        } else {
                            menu_tag = json.getString("menu_info") + "|webaccess";
                            menu_title = json.getString("menu_caption") + "|Web Access";
                        }

                        transport_type = json.getString("transport_mobile_based");

                        SchoolAppUtils.SetSharedParameter(mContext, Constants.NEWTRANSPORT_TYPE, transport_type);

                        if (menu_tag != null) {
                            Log.d("menu", menu_tag.toString());
                            SchoolAppUtils.SetSharedParameter(mContext, Constants.MENU_TAGS, menu_tag.toString());
                            SchoolAppUtils.SetSharedParameter(mContext, Constants.MENU_TITLES, menu_title.toString());
                        }

                        JSONObject permission_config = json.getJSONObject("activity_permission");
                        permission = new DataStructureFramwork.Permission(permission_config);
                        JSONObject permission_add = json.getJSONObject("permission");
                        permissionadd = new DataStructureFramwork.PermissionAdd(permission_add);

                        SchoolAppUtils.SetSharedParameter(mContext, Constants.ASSIGNMENT_MARK_PERMISSION, permission.assignment_permission.toString());
                        SchoolAppUtils.SetSharedParameter(mContext, Constants.TEST_MARK_PERMISSION, permission.test_permission.toString());
                        SchoolAppUtils.SetSharedParameter(mContext, Constants.ACTIVITY_MARK_PERMISSION, permission.activity_permission.toString());
                        SchoolAppUtils.SetSharedParameter(mContext, Constants.ASSIGNMENT_ADD_PERMISSION, permissionadd.assignment_add.toString());
                        SchoolAppUtils.SetSharedParameter(mContext, Constants.TEST_ADD_PERMISSION, permissionadd.test_add.toString());
                        SchoolAppUtils.SetSharedParameter(mContext, Constants.ACTIVITY_ADD_PERMISSION, permissionadd.activity_add.toString());
                        SchoolAppUtils.SetSharedParameter(mContext, Constants.ANNOUNCEMENT_ADD_PERMISSION, permissionadd.announcement_add.toString());
                        SchoolAppUtils.SetSharedParameter(mContext, Constants.COURSEWORK_ADD_PERMISSION, permissionadd.classwork_add.toString());
                        SchoolAppUtils.SetSharedParameter(mContext, Constants.LESSONS_ADD_PERMISSION, permissionadd.lessons_add.toString());
                        SchoolAppUtils.SetSharedParameter(mContext, Constants.ATTENDANCE_TYPE_PERMISSION, permissionadd.attendance_type.toString());
                        Log.d("assignment", permissionadd.attendance_type.toString());

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

            if (result) {

                if (SchoolAppUtils.GetSharedBoolParameter(mContext, Constants.ISSIGNIN) || (SchoolAppUtils.GetSharedBoolParameter(mContext, Constants.UISSIGNIN))) {
                    if (SchoolAppUtils.GetSharedBoolParameter(mContext, Constants.ISPARENTSIGNIN)) {
                        Intent intent = new Intent(mContext, ParentMainActivity.class);
                        intent.putExtra("menu_val", menu_val);
                        startActivity(intent);
                        finish();
                        return;
                    }
                    if (SchoolAppUtils.GetSharedBoolParameter(mContext, Constants.UISSIGNIN)) {
                        Intent intent = new Intent(mContext, MainActivity.class);
                        intent.putExtra("menu_val", menu_val);
                        startActivity(intent);
                        finish();
                        return;
                    }

                    if (SchoolAppUtils.GetSharedParameter(mContext, Constants.USERTYPEID).equalsIgnoreCase("4")) {
                        if (dialog != null) {
                            dialog.dismiss();
                            dialog = null;
                        }
                        Intent intent = new Intent(mContext, MainActivity.class);
                        intent.putExtra("menu_val", menu_val);
                        startActivity(intent);
                        finish();
                        return;
                    } else if (SchoolAppUtils.GetSharedParameter(mContext, Constants.USERTYPEID).equalsIgnoreCase("3")) {

                        // Download data for offline access
                        new GetOfflineDataAsyntask().execute();
                        return;
                    } else if (SchoolAppUtils.GetSharedParameter(mContext, Constants.USERTYPEID).equalsIgnoreCase("5")) {
                        if (dialog != null) {
                            dialog.dismiss();
                            dialog = null;
                        }
                        Intent intent = new Intent(mContext, ParentMainActivity.class);
                        intent.putExtra("menu_val", menu_val);
                        startActivity(intent);
                        finish();
                        return;
                    }
                } else {
                    mHandler = new Handler();
                    mThread = new ServiceWaitThread();
                    mThread.start();
                }

            } else {

                if (dialog != null) {
                    dialog.dismiss();
                    dialog = null;
                }
                if (error != null) {
                    if (error.length() > 0) {
                        SchoolAppUtils.showDialog(mContext, getResources().getString(R.string.unknown_response), error);
                    } else {
                        SchoolAppUtils.showDialog(mContext, getResources().getString(R.string.unknown_response),
                                getResources().getString(R.string.please_check_internet_connection));
                    }
                } else {
                    SchoolAppUtils.showDialog(mContext, getResources().getString(R.string.unknown_response),
                            getResources().getString(R.string.please_check_internet_connection));
                }

            }
        }
    }

    // OFFLINE DATABASE INITIALIZATION
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
            JSONObject json = jParser.getJSONFromUrlnew(nameValuePairs,
                    Urls.api_activity_get_subjects);
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
                    Log.d("Insert: ", "Inserting Subject .." + i);
                    mDatabase.addSubjects(subjects.get(i));
                }
                mDatabase.close();

            }
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
            nameValuePairs.add(new BasicNameValuePair("user_type_id", SchoolAppUtils.GetSharedParameter(mContext, Constants.USERTYPEID)));
            nameValuePairs.add(new BasicNameValuePair("organization_id", SchoolAppUtils.GetSharedParameter(mContext, Constants.UORGANIZATIONID)));
            nameValuePairs.add(new BasicNameValuePair("user_id", SchoolAppUtils.GetSharedParameter(mContext, Constants.USERID)));
            new GetListAsyntask().execute(nameValuePairs);
            // Go to Teacher Main Page

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
            /*
             * if (dialog != null) { // dialog.dismiss(); dialog = null; }
			 */
            if (timeTable != null) {
                if (timeTable.size() > 0) {
                    mDatabase.open();
                    // Delete all data in database
                    mDatabase.deleteAllTimetable();
                    for (int i = 0; i < timeTable.size(); i++) {
                        Log.d("Insert: ", "Inserting Time Table.." + i);
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
                        Log.d("Insert: ", "Inserting Attendance..");
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
            if (dialog != null) {
                dialog.dismiss();
                dialog = null;
            }

            if (students != null) {

                mDatabase.open();
                // Delete all data in database
                mDatabase.deleteAllStudents();
                for (int i = 0; i < students.size(); i++) {
                    Log.d("Insert: ", "Inserting Students.." + i);
                    mDatabase.addStudents(students.get(i));
                }
                mDatabase.close();
                Intent intent = new Intent(mContext, TeacherMainActivity.class);
                intent.putExtra("menu_val", menu_val);
                startActivity(intent);
                finish();

            }

        }

    }


}
