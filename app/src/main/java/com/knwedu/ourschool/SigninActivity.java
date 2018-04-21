package com.knwedu.ourschool;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.firebase.iid.FirebaseInstanceId;
import com.knwedu.activity.ChangePassEnterOtpActivity;
import com.knwedu.activity.EnterMobileOtpActivity;
import com.knwedu.college.CollegeMainActivity;
import com.knwedu.college.CollegeParentMainActivity;
import com.knwedu.college.CollegeTeacherMainActivity;
import com.knwedu.college.adapter.CollegeListUserNameAdapter;
import com.knwedu.college.utils.CollegeAppUtils;
import com.knwedu.college.utils.CollegeConstants;
import com.knwedu.college.utils.CollegeDataStructureFramwork;
import com.knwedu.college.utils.CollegeDataStructureFramwork.CollegePermission;
import com.knwedu.college.utils.CollegeDataStructureFramwork.userType;
import com.knwedu.college.utils.CollegeJsonParser;
import com.knwedu.college.utils.CollegeUrls;
import com.knwedu.calcuttapublicschool.R;
import com.knwedu.constant.StaticConstant;
import com.knwedu.firebasepush.Config;
import com.knwedu.model.UserClass;
import com.knwedu.ourschool.db.DatabaseAdapter;
import com.knwedu.ourschool.db.SchoolApplication;
import com.knwedu.ourschool.model.InstituteData;
import com.knwedu.ourschool.utils.Constants;
import com.knwedu.ourschool.utils.DataStructureFramwork.Attendance;
import com.knwedu.ourschool.utils.DataStructureFramwork.Permission;
import com.knwedu.ourschool.utils.DataStructureFramwork.PermissionAdd;
import com.knwedu.ourschool.utils.DataStructureFramwork.Region;
import com.knwedu.ourschool.utils.DataStructureFramwork.Request;
import com.knwedu.ourschool.utils.DataStructureFramwork.StudentProfileInfo;
import com.knwedu.ourschool.utils.DataStructureFramwork.Subject;
import com.knwedu.ourschool.utils.DataStructureFramwork.TimeTable;
import com.knwedu.ourschool.utils.DataStructureFramwork.UserInfo;
import com.knwedu.ourschool.utils.JsonParser;
import com.knwedu.ourschool.utils.SchoolAppUtils;
import com.knwedu.ourschool.utils.Urls;
import com.knwedu.util.Util;
import com.knwedu.volley.PostWithJsonWebTask;
import com.knwedu.volley.ServerResponseStringCallback;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;


public class SigninActivity extends Activity {

    private EditText username, password, domain_view, code_verify;
    private TextView forgotPassword, resetOrganization, txt_signup, txtPrivacyPolicy, btn_next;
    private AutoCompleteTextView edt_subdomain;
    private Button signIn, signUp;
    private ProgressDialog dialog;
    private DatabaseAdapter mDatabase;
    private ArrayList<TimeTable> timeTable;
    private List<InstituteData> orgList = new ArrayList<InstituteData>();
    private List<Region> regionList = new ArrayList<Region>();
    ImageView home_information;
    private DisplayImageOptions options;
    ImageLoaderConfiguration config;
    File cacheDir;

    /*
     * For Google Cloud Messaging
     */
    public static final String EXTRA_MESSAGE = "message";
    public static final String PROPERTY_REG_ID = "registration_id";

    // Tag for log messages.
    static final String TAG = "GCMDemo";

    SharedPreferences prefs;
    String regid;
    private Spinner spnrInstType, spnrCountry, spnrInstitution;
    private CheckBox chk_domain;

    private ImageView image;
    private ArrayList<CollegeDataStructureFramwork.UserInfo> userInfoList;
    private ArrayList<CollegeDataStructureFramwork.userType> userTypeList;
    private Context context = this;

    private String curVersion = "0";
    private final int REQUEST_SIGNUP = 200;
    private int menu_val;

    //========================================
    private Context mContext;

    //========================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_signin);

        mContext = SigninActivity.this;

        mDatabase = ((SchoolApplication) getApplication()).getDatabase();
        // startService(new Intent(this, CollegeSyncronizeService.class));
        image = (ImageView) findViewById(R.id.image);
        username = (EditText) findViewById(R.id.username_edittxt);
        password = (EditText) findViewById(R.id.password_edittxt);
        forgotPassword = (TextView) findViewById(R.id.forgot_password);
        txtPrivacyPolicy = (TextView) findViewById(R.id.privacy_policy);
        resetOrganization = (TextView) findViewById(R.id.reset_org);
        btn_next = (TextView) findViewById(R.id.btn_next);
        signIn = (Button) findViewById(R.id.signin_btn);
        spnrInstType = (Spinner) findViewById(R.id.spinnerInstitutionType);
        spnrCountry = (Spinner) findViewById(R.id.spinnerCountry);
        spnrInstitution = (Spinner) findViewById(R.id.spinnerInstitution);
        chk_domain = (CheckBox) findViewById(R.id.chk_domain);
        edt_subdomain = (AutoCompleteTextView) findViewById(R.id.domain_edittxt);
        domain_view = (EditText) findViewById(R.id.domain_txt_view);
        code_verify = (EditText) findViewById(R.id.code_verify);
        home_information = (ImageView) findViewById(R.id.home_information);

        home_information.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(SigninActivity.this, HomeInformationActivity.class);
                myIntent.putExtra("callType", "home");
                startActivity(myIntent);
            }
        });

        cacheDir = StorageUtils.getCacheDirectory(this);
        config = new ImageLoaderConfiguration.Builder(this).memoryCacheExtraOptions(480, 800) // default = device screen dimensions
                .diskCacheExtraOptions(480, 800, null)
                .threadPriority(Thread.NORM_PRIORITY - 2) // default
                .tasksProcessingOrder(QueueProcessingType.FIFO) // default
                .memoryCache(new LruMemoryCache(2 * 1024 * 1024))
                .memoryCacheSize(2 * 1024 * 1024)
                .memoryCacheSizePercentage(13) // default
                .diskCache(new UnlimitedDiskCache(cacheDir)) // default
                .diskCacheFileCount(1000)
                .diskCacheFileNameGenerator(new HashCodeFileNameGenerator()) // default
                .imageDownloader(new BaseImageDownloader(this)) // default
                .defaultDisplayImageOptions(DisplayImageOptions.createSimple()) // default
                .writeDebugLogs()
                .build();
        ImageLoader.getInstance().init(config);

        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.progress_animation)
                .showImageForEmptyUri(R.drawable.app_icon)
                .showImageOnFail(R.drawable.app_icon)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();


        forgotPassword.setPaintFlags(forgotPassword.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        txtPrivacyPolicy.setPaintFlags(forgotPassword.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        resetOrganization.setPaintFlags(resetOrganization.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        txt_signup = (TextView) findViewById(R.id.txt_signup);
        signUp = (Button) findViewById(R.id.signup_btn);

        txt_signup.setVisibility(View.GONE);
        signUp.setVisibility(View.GONE);

        Bundle extras = getIntent().getExtras();
        if (null != extras) {
            menu_val = extras.getInt("menu_val");
        }

        //new SetRegionAsyn().execute();

        //=================================================
        UserClass userClass = Util.fetchUserClass(mContext);
        if (userClass != null && userClass.isOTPSignup()) {
            username.setText(userClass.getPhone());
            password.setText(userClass.getPassword());
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("ORGANIZATION", SchoolAppUtils.GetSharedParameter(mContext, Constants.UORGANIZATIONID));
        initialize();
    }


    private void initialize() {
        // User not selected Organization
        String vc = SchoolAppUtils.GetSharedParameter(mContext, Constants.VERIFIED_COUNTER);
        String tc = SchoolAppUtils.GetSharedParameter(mContext, Constants.TUTORIAL_COUNTER).trim();

        String organizationid = SchoolAppUtils.GetSharedParameter(mContext, Constants.UORGANIZATIONID).trim();
        Log.e("Tutorial Counter", "TC: " + tc);
        if (organizationid.equals("0")) {
            Log.e("initialize", "initialize");
            username.setVisibility(View.INVISIBLE);
            password.setVisibility(View.INVISIBLE);
            signIn.setVisibility(View.INVISIBLE);
            forgotPassword.setVisibility(View.INVISIBLE);
            edt_subdomain.setVisibility(View.VISIBLE);
            home_information.setVisibility(View.GONE);
            if (edt_subdomain.getText().toString().length() == 0) {
                btn_next.setVisibility(View.GONE);
            } else {
                btn_next.setVisibility(View.VISIBLE);
            }
            if (orgList.size() == 0) {
                spnrInstType.setVisibility(View.GONE);
                spnrCountry.setVisibility(View.GONE);
                spnrInstitution.setVisibility(View.GONE);
                // chk_domain.setVisibility(View.VISIBLE);
                edt_subdomain.setVisibility(View.VISIBLE);
                domain_view.setVisibility(View.GONE);


                domain_view.setVisibility(View.GONE);
                spnrInstType.setVisibility(View.GONE);
                spnrCountry.setVisibility(View.GONE);
                spnrInstitution.setVisibility(View.GONE);
                image.setImageResource(R.drawable.login_logo);

                txt_signup.setVisibility(View.GONE);
                signUp.setVisibility(View.INVISIBLE);

                // Request for all domain
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
                nameValuePairs.add(new BasicNameValuePair("type", "all"));

                new SubdomainAsync().execute(nameValuePairs);
            }
            resetOrganization.setVisibility(View.GONE);
            // chk_domain.setChecked(false);
            image.setImageResource(R.drawable.login_logo);

            txt_signup.setVisibility(View.GONE);
            signUp.setVisibility(View.GONE);
        }

        // User already selected Organization
        else {
            // display pre-stored user login id
            String storedUserId = SchoolAppUtils.GetSharedParameter(mContext, Constants.USER_LOGIN_ID);
            if (!storedUserId.equals("0")) {
                username.setText(storedUserId);
            }

            spnrInstType.setVisibility(View.GONE);
            spnrCountry.setVisibility(View.GONE);
            spnrInstitution.setVisibility(View.GONE);
            chk_domain.setVisibility(View.GONE);
            edt_subdomain.setVisibility(View.GONE);
            domain_view.setVisibility(View.GONE);
            signUp.setVisibility(View.VISIBLE);
            username.setVisibility(View.VISIBLE);
            password.setVisibility(View.VISIBLE);
            btn_next.setVisibility(View.GONE);
            home_information.setVisibility(View.VISIBLE);
            signIn.setVisibility(View.VISIBLE);
            resetOrganization.setVisibility(View.VISIBLE);

            String ais_logo_url = SchoolAppUtils.GetSharedParameter(mContext, Constants.AIS_APP_LOGO);

            ImageLoader.getInstance().displayImage(ais_logo_url, image, options);

        }


        forgotPassword.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                String organizationId = SchoolAppUtils.GetSharedParameter(mContext, Constants.UORGANIZATIONID);
                HashMap<String, String> requestMap = new HashMap<>();
                requestMap.put("organization_id", "" + organizationId.trim());
                startActivity(new Intent(mContext, ChangePassEnterOtpActivity.class));
               /* PostWithJsonWebTask.callPostWithJsonWebtask(SigninActivity.this, Urls.base_url + "mobile/showSignupType", requestMap, new ServerResponseStringCallback() {
                    @Override
                    public void onSuccess(String resultJsonObject) {
                        try {
                            JSONObject resultJson = new JSONObject(resultJsonObject);
                            if (resultJson.optString("result").trim().equalsIgnoreCase("1")) {
                                JSONObject dataJson = resultJson.optJSONObject("data");
                                if (dataJson.optString("signup_val").trim().equalsIgnoreCase("1")) {
                                    // OTP Change Pass
                                    startActivity(new Intent(mContext, ChangePassEnterOtpActivity.class));

                                } else if (dataJson.optString("signup_val").trim().equalsIgnoreCase("0")) {
                                    startActivity(new Intent(mContext, ForgotPasswordActivity.class));

                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void ErrorMsg(VolleyError error) {

                    }
                }, true, com.android.volley.Request.Method.POST);*/
            }
        });

        txtPrivacyPolicy.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, PrivacyPolicyActivity.class));
            }
        });

        signIn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                try {
                    SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
                    String regId = pref.getString("regId", null);
                    Log.e("FCMdevicetoken", "Reg ID: " + regId);

                    String refreshedToken = FirebaseInstanceId.getInstance().getToken();
                    Log.e("refreshedToken", "refreshedToken: " + refreshedToken);

                    if (regId == null && refreshedToken != null) {
                        SharedPreferences preff = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
                        SharedPreferences.Editor editor = preff.edit();
                        editor.putString("regId", refreshedToken);
                        editor.commit();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                String organizationId = SchoolAppUtils.GetSharedParameter(mContext, Constants.UORGANIZATIONID);
                HashMap<String, String> requestMap = new HashMap<>();
                requestMap.put("organization_id", "" + organizationId.trim());
                PostWithJsonWebTask.callPostWithJsonWebtask(SigninActivity.this, Urls.base_url + "mobile/showSignupType", requestMap, new ServerResponseStringCallback() {
                    @Override
                    public void onSuccess(String resultJsonObject) {
                        try {
                            JSONObject resultJson = new JSONObject(resultJsonObject);
                            UserClass userClass = Util.fetchUserClass(mContext);
                            if (userClass == null) {
                                userClass = new UserClass();

                            }
                            if (resultJson.optString("result").trim().equalsIgnoreCase("1")) {
                                JSONObject dataJson = resultJson.optJSONObject("data");
                                if (dataJson.optString("signup_val").trim().equalsIgnoreCase("1")) {
                                    // OTP Change Pass
                                    userClass.setOTPSignup(true);
                                    Util.saveUserClass(mContext, userClass);

                                } else if (dataJson.optString("signup_val").trim().equalsIgnoreCase("0")) {

                                    userClass.setOTPSignup(false);
                                    Util.saveUserClass(mContext, userClass);

                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void ErrorMsg(VolleyError error) {

                    }
                }, true, com.android.volley.Request.Method.POST);


                if (edt_subdomain.getText().toString().isEmpty() && chk_domain.isChecked()) {
                    Toast.makeText(mContext, R.string.please_enter_domain, Toast.LENGTH_SHORT).show();
                    return;
                }
                if (username.getText().toString().length() <= 0) {
                    Toast.makeText(mContext, R.string.enter_your_emailmobile, Toast.LENGTH_SHORT).show();
                    return;
                }
                if (password.getText().toString().length() <= 0) {
                    Toast.makeText(mContext, R.string.enter_your_password, Toast.LENGTH_SHORT).show();
                    return;
                }
                new retriveToken().execute();

            }
        });

        btn_next.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (SchoolAppUtils.GetSharedParameter(mContext, Constants.UORGANIZATIONID).equals("0")) {
                    Toast.makeText(mContext, R.string.please_select_institution, Toast.LENGTH_SHORT).show();
                } else {

                    //== Removed if section - disable tutorial with Got It.
                   /* if (SchoolAppUtils.GetSharedParameter(mContext, Constants.TUTORIAL_COUNTER).equals("0")) {

                        // Show Tutorial
                        Intent tu_int = new Intent(mContext, TutorialActivity.class);
                        startActivity(tu_int);

                    } else {*/
                    home_information.setVisibility(View.VISIBLE);
                    code_verify.setVisibility(View.GONE);
                    username.setVisibility(View.VISIBLE);
                    password.setVisibility(View.VISIBLE);
                    signIn.setVisibility(View.VISIBLE);
                    edt_subdomain.setVisibility(View.GONE);
                    forgotPassword.setVisibility(View.VISIBLE);
                    btn_next.setVisibility(View.GONE);
                    resetOrganization.setVisibility(View.VISIBLE);
                    signUp.setVisibility(View.VISIBLE);

                    String ais_logo_url = SchoolAppUtils.GetSharedParameter(mContext, Constants.AIS_APP_LOGO);
                    ImageLoader.getInstance().displayImage(ais_logo_url, image, options);
                    //  }
                }
            }
        });


        resetOrganization.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                SchoolAppUtils.SetSharedParameter(mContext, Constants.USER_LOGIN_ID, "0");
                username.setText("");
                edt_subdomain.setText("");

                SchoolAppUtils.SetSharedParameter(mContext, Constants.UORGANIZATIONID, "0");
                SchoolAppUtils.SetSharedParameter(mContext, Constants.APP_TYPE, Constants.APP_TYPE_EXCLUSIVE);
                SchoolAppUtils.SetSharedParameter(mContext, Constants.PASSWORD_PROTECTION, "0");
                SchoolAppUtils.SetSharedParameter(mContext, Constants.VERIFIED_COUNTER, "0");

                onResume();
            }
        });

        signUp.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                String organizationId = SchoolAppUtils.GetSharedParameter(mContext, Constants.UORGANIZATIONID);
                HashMap<String, String> requestMap = new HashMap<>();
                requestMap.put("organization_id", "" + organizationId.trim());

                PostWithJsonWebTask.callPostWithJsonWebtask(SigninActivity.this, Urls.base_url + "mobile/showSignupType", requestMap, new ServerResponseStringCallback() {
                    @Override
                    public void onSuccess(String resultJsonObject) {

                        try {

                            try {
                                SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
                                String regId = pref.getString("regId", null);
                                regid = regId;
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            try {
                                curVersion = getApplicationContext().getPackageManager().getPackageInfo(getApplicationInfo().packageName, 0).versionName;

                            } catch (NameNotFoundException e) {
                                e.printStackTrace();
                            }

                            JSONObject resultJson = new JSONObject(resultJsonObject);

                            if (resultJson.optString("result").trim().equalsIgnoreCase("1")) {
                                JSONObject dataJson = resultJson.optJSONObject("data");
                                if (dataJson.optString("signup_val").trim().equalsIgnoreCase("1")) {
                                    // OTP SignUp
                                    UserClass userClass = Util.fetchUserClass(mContext);
                                    if (userClass == null)
                                        userClass = new UserClass();
                                    userClass.setDeviceRegid(regid);
                                    userClass.setCurrentVersion(curVersion);
                                    userClass.setSignupType(StaticConstant.OTP_SIGNUP);
                                    Util.saveUserClass(mContext, userClass);
                                    startActivity(new Intent(mContext, EnterMobileOtpActivity.class));

                                } else if (dataJson.optString("signup_val").trim().equalsIgnoreCase("0")) {
                                    // Normal Signup
                                    UserClass userClass = Util.fetchUserClass(mContext);
                                    if (userClass == null)
                                        userClass = new UserClass();
                                    userClass.setSignupType(StaticConstant.NORMAL_SIGNUP);
                                    Util.saveUserClass(mContext, userClass);
                                    if (SchoolAppUtils.GetSharedParameter(mContext, Constants.APP_TYPE).equals(Constants.APP_TYPE_COMMON_STANDARD)) {
                                        Intent intent = new Intent(mContext, AisRegistrationActivity.class);
                                        intent.putExtra("devicetoken", regid);
                                        intent.putExtra("curversion", curVersion);
                                        startActivityForResult(intent, REQUEST_SIGNUP);
                                    } else {

                                        Intent in = new Intent(mContext, AimsRegistrationActivity.class);
                                        in.putExtra("devicetoken", regid);
                                        in.putExtra("curversion", curVersion);
                                        startActivity(in);

                                    }
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void ErrorMsg(VolleyError error) {

                    }
                }, true, com.android.volley.Request.Method.POST);

            }
        });

    }

    private void loadInstitutionNames() {
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
        nameValuePairs.add(new BasicNameValuePair("type", SchoolAppUtils.GetSharedParameter(mContext, Constants.INSTITUTION_TYPE)));
        nameValuePairs.add(new BasicNameValuePair("region", regionList.get(spnrCountry.getSelectedItemPosition() - 1).id));
        nameValuePairs.add(new BasicNameValuePair("country", spnrCountry.getSelectedItem().toString()));
    }

    @Override
    public void onStart() {
        super.onStart();
        EasyTracker.getInstance(this).activityStart(this);

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (dialog != null) {
            if (dialog.isShowing()) {
                dialog.dismiss();
                dialog = null;
            }
        }
        EasyTracker.getInstance(this).activityStart(this);
    }

    private class retriveToken extends AsyncTask<String, Void, Boolean> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(mContext);
            dialog.setTitle(getResources().getString(R.string.signing_in));
            dialog.setMessage(getResources().getString(R.string.please_wait));
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(false);

            dialog.show();

        }

        @Override
        protected Boolean doInBackground(String... params) {

            try {
                SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
                String regId = pref.getString("regId", null);
                regid = regId;
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (regid != null) {
                return true;
            }
            return false;

        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if (result) {

                try {
                    curVersion = getApplicationContext().getPackageManager().getPackageInfo(getApplicationInfo().packageName, 0).versionName;

                } catch (NameNotFoundException e) {
                    e.printStackTrace();
                }

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(4);
                nameValuePairs.add(new BasicNameValuePair("email", username.getText().toString().trim()));
                nameValuePairs.add(new BasicNameValuePair("username", username.getText().toString().trim()));
                nameValuePairs.add(new BasicNameValuePair("password", password.getText().toString().trim()));
                nameValuePairs.add(new BasicNameValuePair("devicetype", "android"));
                nameValuePairs.add(new BasicNameValuePair("organization_id", SchoolAppUtils.GetSharedParameter(mContext, Constants.UORGANIZATIONID)));
                nameValuePairs.add(new BasicNameValuePair("devicetoken", regid));
                nameValuePairs.add(new BasicNameValuePair("version_number", curVersion));

                new SignInAsyntask().execute(nameValuePairs);
            } else {

                if (dialog != null) {
                    dialog.dismiss();
                    dialog = null;
                }
                SchoolAppUtils.showDialog(mContext, getResources()
                        .getString(R.string.unknown_response), getResources()
                        .getString(R.string.please_check_internet_connection));
            }
        }
    }

    /**
     * This retrieves information for the selected user
     *
     * @author Sunit
     */
    private class SignInAsyntask extends AsyncTask<List<NameValuePair>, Void, Boolean> {
        UserInfo userInfo;

        String error = "";
        Permission permission;
        PermissionAdd permissionadd;
        Request requests;
        CollegePermission per;

        @Override
        protected Boolean doInBackground(List<NameValuePair>... params) {

            if (SchoolAppUtils.GetSharedParameter(mContext, Constants.INSTITUTION_TYPE).equals(Constants.INSTITUTION_TYPE_SCHOOL)) {
                List<NameValuePair> nameValuePairs = params[0];
                JsonParser jParser = new JsonParser(mContext);
                JSONObject json = jParser.getJSONFromUrlnew(nameValuePairs, Urls.api_login);

                // Log parameters:
                String parameters = "";
                for (NameValuePair nvp : nameValuePairs) {
                    parameters += nvp.getName() + "=" + nvp.getValue() + ",";
                }

                try {

                    if (json != null) {
                        if (json.getString("result").equalsIgnoreCase("1")) {
                            JSONObject object = json.getJSONObject("data");
                            String menu_tag;
                            String menu_title;
                            if (SchoolAppUtils.GetSharedParameter(mContext, Constants.APP_TYPE).equals(Constants.APP_TYPE_COMMON_STANDARD)) {
                                menu_tag = json.getString("menu_info");
                                menu_title = json.getString("menu_caption");
                            } else {
                                menu_tag = json.getString("menu_info") + "|webaccess";
                                menu_title = json.getString("menu_caption") + "|Web Access";
                            }

                            JSONObject permission_config = json.getJSONObject("activity_permission");
                            JSONObject request_config = json.getJSONObject("requests");
                            JSONObject permission_add = json.getJSONObject("permission");

                            JSONObject orgConfigJSONObj = json.getJSONObject("org_config");
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
                                userInfo = new UserInfo(object);
                                SchoolAppUtils.SetSharedParameter(mContext, Constants.USERTYPEID, userInfo.usertypeid);
                                SchoolAppUtils.SetSharedParameter(mContext, Constants.USERID, userInfo.userid);
                                SchoolAppUtils.SetSharedParameter(mContext, Constants.UALTEMAIL, userInfo.alt_email);

                                SchoolAppUtils.SetSharedParameter(mContext, Constants.ORGEMAIL, userInfo.org_email);

                                SchoolAppUtils.SetSharedParameter(mContext, Constants.UMOBILENO, userInfo.mobile_no);
                                SchoolAppUtils.SetSharedParameter(mContext, Constants.PASSWORD, password.getText().toString().trim());

                                SchoolAppUtils.SetSharedParameter(mContext, Constants.DEVICE_TOKEN, regid);


                            }
                            // Menu tag
                            if (menu_tag != null) {
                                SchoolAppUtils.SetSharedParameter(mContext, Constants.MENU_TAGS, menu_tag.toString());
                                SchoolAppUtils.SetSharedParameter(mContext, Constants.MENU_TITLES, menu_title.toString());
                            }

                            // Permissions
                            if (permission_config != null) {
                                permission = new Permission(permission_config);
                                SchoolAppUtils.SetSharedParameter(mContext, Constants.ASSIGNMENT_MARK_PERMISSION, permission.assignment_permission.toString());
                                SchoolAppUtils.SetSharedParameter(mContext, Constants.TEST_MARK_PERMISSION, permission.test_permission.toString());
                                SchoolAppUtils.SetSharedParameter(mContext, Constants.ACTIVITY_MARK_PERMISSION, permission.activity_permission.toString());
                            }

                            // Parent Request
                            if (request_config != null) {
                                requests = new Request(request_config);
                                SchoolAppUtils.SetSharedParameter(mContext, Constants.LEAVE_REQUEST, requests.leave_request.toString());
                                SchoolAppUtils.SetSharedParameter(mContext, Constants.REQUEST_FOR_BOOK, requests.request_for_book.toString());
                                SchoolAppUtils.SetSharedParameter(mContext, Constants.REQUEST_FOR_ID_CARD, requests.request_for_id.toString());
                                SchoolAppUtils.SetSharedParameter(mContext, Constants.REQUEST_FOR_UNIFORM, requests.request_for_uniform.toString());
                                SchoolAppUtils.SetSharedParameter(mContext, Constants.SPECIAL_REQUEST, requests.special_request.toString());
                            }

                            // Permission Add
                            if (permission_add != null) {
                                permissionadd = new PermissionAdd(permission_add);
                                SchoolAppUtils.SetSharedParameter(mContext, Constants.ASSIGNMENT_ADD_PERMISSION, permissionadd.assignment_add.toString());
                                SchoolAppUtils.SetSharedParameter(mContext, Constants.TEST_ADD_PERMISSION, permissionadd.test_add.toString());
                                SchoolAppUtils.SetSharedParameter(mContext, Constants.ACTIVITY_ADD_PERMISSION, permissionadd.activity_add.toString());
                                SchoolAppUtils.SetSharedParameter(mContext, Constants.ANNOUNCEMENT_ADD_PERMISSION, permissionadd.announcement_add.toString());
                                SchoolAppUtils.SetSharedParameter(mContext, Constants.COURSEWORK_ADD_PERMISSION, permissionadd.classwork_add.toString());
                                SchoolAppUtils.SetSharedParameter(mContext, Constants.LESSONS_ADD_PERMISSION, permissionadd.lessons_add.toString());
                                SchoolAppUtils.SetSharedParameter(mContext, Constants.ATTENDANCE_TYPE_PERMISSION, permissionadd.attendance_type.toString());
                            }

                            if (userInfo.usertypeid.equalsIgnoreCase(Constants.USERTYPE_PARENT)) {
                                JSONArray child_info_arry = json.getJSONArray("child_info");
                                JSONObject object_chield_info = child_info_arry.getJSONObject(0);
                                StudentProfileInfo selectedStudent = new StudentProfileInfo(object_chield_info);
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
                        error = getResources().getString(
                                R.string.unknown_response);
                    }

                } catch (JSONException e) {

                }
                return false;
            }

            // ---------------------College------------------------------
            else {
                List<NameValuePair> nameValuePairs = params[0];
                CollegeJsonParser jParser = new CollegeJsonParser();
                JSONObject json = jParser.getJSONFromUrlnew(nameValuePairs, CollegeUrls.api_login);

                // Log parameters:
                Log.d("url extension: ", CollegeUrls.api_login);
                String parameters = "";
                for (NameValuePair nvp : nameValuePairs) {
                    parameters += nvp.getName() + "=" + nvp.getValue() + ",";
                }
                Log.d("Parameters: ", parameters);
                userInfoList = new ArrayList<CollegeDataStructureFramwork.UserInfo>();
                userTypeList = new ArrayList<CollegeDataStructureFramwork.userType>();
                try {
                    if (json.getString("result").equalsIgnoreCase("1")) {
                        JSONArray jsonData = json.getJSONArray("data");
                        for (int i = 0; i < jsonData.length(); i++) {
                            CollegeDataStructureFramwork.UserInfo collegeUserInfo = new CollegeDataStructureFramwork.UserInfo(jsonData.getJSONObject(i));
                            CollegeAppUtils.SetSharedParameter(mContext, Constants.USERID, collegeUserInfo.id);
                            CollegeAppUtils.SetSharedParameter(context, "id", collegeUserInfo.id);
                            CollegeAppUtils.SetSharedParameter(context, "session_student_id", collegeUserInfo.session_student_id);
                            CollegeAppUtils.SetSharedParameter(context, Constants.CHILD_ID, collegeUserInfo.session_student_id);
                            CollegeAppUtils.SetSharedParameter(context, "student_name", collegeUserInfo.student_name);
                            CollegeAppUtils.SetSharedParameter(context, "student_class", collegeUserInfo.student_class);
                            CollegeAppUtils.SetSharedParameter(context, "student_image", collegeUserInfo.student_image);
                            CollegeAppUtils.SetSharedParameter(context, CollegeConstants.USERTYPEID, collegeUserInfo.usertypeid);
                            CollegeAppUtils.SetSharedParameter(context, CollegeConstants.UALTEMAIL, collegeUserInfo.alt_email);
                            CollegeAppUtils.SetSharedParameter(context, CollegeConstants.ORGEMAIL, collegeUserInfo.email);
                            CollegeAppUtils.SetSharedParameter(context, CollegeConstants.UMOBILENO, collegeUserInfo.mobile_no);
                            CollegeAppUtils.SetSharedParameter(context, CollegeConstants.USERTYPEID, collegeUserInfo.usertypeid);
                            userInfoList.add(collegeUserInfo);

                        }

                        JSONArray jsonUser = json.getJSONArray("user_types");
                        for (int j = 0; j < jsonUser.length(); j++) {
                            CollegeDataStructureFramwork.userType userTYpe = new userType(jsonUser.getJSONObject(j));
                            CollegeAppUtils.SetSharedParameter(context, "role_id", userTYpe.id);
                            userTypeList.add(userTYpe);
                        }
                        if (userTypeList.size() == 1) {
                            String menu_tag = json.getString("menu_info");
                            menu_tag.split("\\|");
                            String menu_title = json.getString("menu_caption");
                            JSONObject permission_add = json.getJSONObject("permissions");
                            if (jsonData != null) {
                                per = new CollegePermission(permission_add);
                            }
                            if (jsonData != null) {
                                if (menu_tag.contains(Constants.MENU_TEACHER_ASSIGNMENT)) {
                                    CollegeAppUtils.SetSharedParameter(mContext, CollegeConstants.COLLEGE_ASSIGNMENT_CREATE, per.assignment_create.toString());
                                    //Log.d("VALUE OF ASSIGNMENT CREATE",per.assignment_create.toString());

                                    CollegeAppUtils.SetSharedParameter(mContext, CollegeConstants.COLLEGE_ASSIGNMENT_MARK, per.assignment_mark.toString());

                                    // Log.d("VALUE OF ASSIGNMENT MARK",per.assignment_mark.toString());
                                    CollegeAppUtils.SetSharedParameter(mContext, CollegeConstants.COLLEGE_ASSIGNMENT_PUBLISH, per.assignment_publish.toString());

                                    // Log.d("VALUE OF ASSIGNMENT PUBLISH ",per.assignment_publish.toString());
                                }
                                if (menu_tag.contains(Constants.MENU_TEACHER_ANNOUNCEMENT)) {
                                    CollegeAppUtils.SetSharedParameter(mContext, CollegeConstants.COLLEGE_ANNOUNCEMENT_CREATE, per.announcement_create.toString());
                                }
                                if (menu_tag.contains(Constants.MENU_TEACHER_COURSE_WORK)) {
                                    CollegeAppUtils.SetSharedParameter(mContext, CollegeConstants.COLLEGE_CLASSWORK_CREATE, per.classwork_create.toString());
                                }
                                if (menu_tag.contains(Constants.MENU_TEACHER_QUIZ)) {
                                    CollegeAppUtils.SetSharedParameter(mContext, CollegeConstants.COLLEGE_INTERNAL_CREATE, per.test_create.toString());
                                    // marking & publish

                                    CollegeAppUtils.SetSharedParameter(mContext, CollegeConstants.COLLEGE_INTERNAL_MARK, per.test_mark.toString());

                                    Log.d("VALUE OF TEST MARK...", per.test_mark.toString());
                                    CollegeAppUtils.SetSharedParameter(mContext, CollegeConstants.COLLEGE_INTERNAL_PUBLISH, per.test_publish.toString());

                                    Log.d("VALUE OF TEST PUBLISH", per.test_publish.toString());
                                }

                                // attendance mark

                                if (!CollegeAppUtils.GetSharedParameter(context, CollegeConstants.USERTYPEID).equalsIgnoreCase(CollegeConstants.USERTYPE_PARENT)) {
                                    CollegeAppUtils.SetSharedParameter(mContext, CollegeConstants.COLLEGE_ATTENDANCE_MARK, per.attendance_mark.toString());

                                    // lessons edit
                                    if (per.lessons_edit != null) {
                                        CollegeAppUtils.SetSharedParameter(mContext, CollegeConstants.COLLEGE_LESSONS_EDIT, per.lessons_edit.toString());
                                    }

                                }

                                // marking & publish

                                if (!CollegeAppUtils.GetSharedParameter(context, CollegeConstants.USERTYPEID).equalsIgnoreCase(CollegeConstants.USERTYPE_TEACHER)) {
                                    if (per.request_create != null) {
                                        CollegeAppUtils.SetSharedParameter(mContext, CollegeConstants.COLLEGE_REQUEST_CREATE, per.request_create.toString());
                                    }

                                    if (per.request_delete != null) {
                                        CollegeAppUtils.SetSharedParameter(mContext, CollegeConstants.COLLEGE_REQUEST_DELETE, per.request_delete.toString());

                                    }

                                    if (per.request_edit != null) {
                                        CollegeAppUtils.SetSharedParameter(mContext, CollegeConstants.COLLEGE_REQUEST_EDIT, per.request_edit.toString());
                                    }
                                    CollegeAppUtils.SetSharedParameter(mContext, CollegeConstants.COLLEGE_REQUEST_MARK, per.request_mark.toString());

                                    if (per.request_publish != null) {
                                        CollegeAppUtils.SetSharedParameter(mContext, CollegeConstants.COLLEGE_REQUEST_PUBLISH, per.request_publish.toString());
                                    }
                                    CollegeAppUtils.SetSharedParameter(mContext, CollegeConstants.COLLEGE_REQUEST_VIEW, per.request_view.toString());
                                    // feedback create
                                    CollegeAppUtils.SetSharedParameter(context, CollegeConstants.COLLEGE_FEEDBACK_CREATE, per.feedback_create.toString());

                                }

                            }

                            if (menu_tag != null) {
                                Log.d("menu", menu_tag.toString());
                                CollegeAppUtils.SetSharedParameter(context, CollegeConstants.MENU_TAGS, menu_tag.toString());
                                CollegeAppUtils.SetSharedParameter(context, CollegeConstants.MENU_TITLES, menu_title.toString());
                            }
                        }
                        JSONObject jsonOrg = json.getJSONObject("org_config");
                        if (jsonOrg != null) {
                            CollegeDataStructureFramwork.UserInfo collegeUserInfo = new CollegeDataStructureFramwork.UserInfo(jsonOrg);
                            CollegeAppUtils.SetSharedParameter(context, CollegeConstants.ASSIGNMENT_MARKING, collegeUserInfo.assignment_marking);
                            Log.d("ASSIGNMENT MARKING", collegeUserInfo.assignment_marking);
                            CollegeAppUtils.SetSharedParameter(context, CollegeConstants.TEST_MARKING, collegeUserInfo.test_marking);
                            CollegeAppUtils.SetSharedParameter(context, CollegeConstants.CLASSWORK_MARKING, collegeUserInfo.classwork_marking);
                        }
                        return true;
                    } else {
                        try {
                            error = json.getString("data");
                        } catch (Exception e) {
                        }
                        return false;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {

            super.onPostExecute(result);

            if (result) {
                if (SchoolAppUtils.GetSharedParameter(mContext, Constants.INSTITUTION_TYPE).equals(Constants.INSTITUTION_TYPE_SCHOOL)) {
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
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);

                        if (dialog != null) {
                            dialog.dismiss();
                            dialog = null;
                        }
                        finish();
                    }
                }
                // College
                else {
                    if (userTypeList.size() > 1) {

                        if (dialog != null) {
                            dialog.dismiss();
                            dialog = null;
                        }
                        SchoolAppUtils.SetSharedParameter(mContext, Constants.USERID, "0");
                        showDialog();
                    } else {
                        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(0);
                        nameValuePairs.add(new BasicNameValuePair("id", CollegeAppUtils.GetSharedParameter(context, "id")));
                        Log.d("PARAMS OF TEACHER", "" + nameValuePairs);
                        new GetTimetableListAsyntaskCollege().execute(nameValuePairs);
                    }

                }
            } else {

                if (dialog != null) {
                    dialog.dismiss();
                    dialog = null;
                }
                if (error.length() > 0) {
                    SchoolAppUtils.showDialog(mContext, getResources().getString(R.string.sign_in), error);
                } else {
                    SchoolAppUtils.showDialog(mContext, getResources().getString(R.string.sign_in), getResources().getString(R.string.unknown_response));
                }
            }
        }
    }

    // OFFLINE DATABASE INITIALIZATION
    private class GetOfflineDataAsyntask extends AsyncTask<Void, Void, Boolean> {
        String error;
        private ArrayList<Subject> subjects;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            subjects = new ArrayList<Subject>();
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
                            Subject subject = new Subject(
                                    array.getJSONObject(i));

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

    private class GetListAsyntask extends
            AsyncTask<List<NameValuePair>, Void, Boolean> {
        String error;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(List<NameValuePair>... params) {
            timeTable = new ArrayList<TimeTable>();
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
                        timeTable = new ArrayList<TimeTable>();
                        for (int i = 0; i < array.length(); i++) {
                            TimeTable attendance = new TimeTable(array.getJSONObject(i));
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

    private class GetListAttendanceAsyntask extends
            AsyncTask<List<NameValuePair>, Void, Boolean> {
        String error;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(List<NameValuePair>... params) {
            timeTable = new ArrayList<TimeTable>();
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
                        timeTable = new ArrayList<TimeTable>();
                        for (int i = 0; i < array.length(); i++) {
                            TimeTable attendance = new TimeTable(array.getJSONObject(i));
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
        private ArrayList<Attendance> students;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Boolean doInBackground(List<NameValuePair>... params) {

            // Log parameters:
            students = new ArrayList<Attendance>();
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
                            Attendance subject = new Attendance(array.getJSONObject(i));
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

    class DownloadFile extends AsyncTask<String, Integer, Boolean> {
        ProgressBar progress;
        String strFolderName;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress = (ProgressBar) findViewById(R.id.progress);
            progress.setVisibility(View.VISIBLE);
            image.setVisibility(View.INVISIBLE);
            if (SchoolAppUtils.GetSharedParameter(mContext, Constants.APP_TYPE).equals(Constants.APP_TYPE_COMMON_STANDARD)) {
                txt_signup.setVisibility(View.GONE);
                signUp.setVisibility(View.VISIBLE);
            } else {
                txt_signup.setVisibility(View.GONE);
                signUp.setVisibility(View.GONE);
            }

        }

        @Override
        protected Boolean doInBackground(String... aurl) {
            int count;
            int count2;
            try {
                URL url = new URL((String) aurl[0]);
                URLConnection conexion = url.openConnection();
                conexion.connect();

                int lenghtOfFile = conexion.getContentLength();
                ContextWrapper cw = new ContextWrapper(getApplicationContext());
                // path to /data/data/yourapp/app_data/imageDir
                File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
                // Create imageDir
                File path = new File(directory, "applogo.png");

                InputStream input = new BufferedInputStream(url.openStream());
                OutputStream output = new FileOutputStream(path);
                byte data[] = new byte[1024];
                long total = 0;
                while ((count = input.read(data)) != -1) {
                    total += count;
                    publishProgress((int) (total * 100 / lenghtOfFile));
                    output.write(data, 0, count);
                }
                output.flush();
                output.close();
                input.close();

                // Download footer
                URL url2 = new URL((String) aurl[1]);
                URLConnection conexion2 = url2.openConnection();
                conexion2.connect();

                int lenghtOfFile2 = conexion2.getContentLength();
                File path2 = new File(directory, "footer_logo.png");

                InputStream input2 = new BufferedInputStream(url2.openStream());
                OutputStream output2 = new FileOutputStream(path2);
                byte data2[] = new byte[1024];
                long total2 = 0;
                while ((count2 = input2.read(data2)) != -1) {
                    total2 += count2;
                    publishProgress((int) (total2 * 100 / lenghtOfFile2));
                    output2.write(data2, 0, count2);
                }
                output2.flush();
                output2.close();
                input2.close();

            } catch (Exception e) {
                e.printStackTrace();
                return true;
            }
            return false;
        }

        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            progress.setVisibility(View.INVISIBLE);
            image.setVisibility(View.VISIBLE);
            loadImage();
        }
    }

    private void loadImage() {

        try {
            ContextWrapper cw = new ContextWrapper(getApplicationContext());

            File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
            // Create imageDir
            File path = new File(directory, "applogo.png");
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(path));
            if (b != null) {
                image.setImageBitmap(b);
            } else {
                image.setImageResource(R.drawable.login_logo);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void showDialog() {
        // Strings to Show In Dialog with Radio Buttons
        final Dialog dialog2 = new Dialog(context);
        dialog2.setCanceledOnTouchOutside(false);
        dialog2.setCancelable(false);
        dialog2.setOnCancelListener(new OnCancelListener() {

            @Override
            public void onCancel(DialogInterface arg0) {

                SchoolAppUtils.SetSharedParameter(mContext, Constants.USERID, "0");
            }
        });
        dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog2.setContentView(R.layout.custom_list_dialog);
        ListView listName = (ListView) dialog2.findViewById(R.id.listView1);

        CollegeListUserNameAdapter mAdapter = new CollegeListUserNameAdapter(context, userTypeList);
        listName.setAdapter(mAdapter);
        dialog2.show();
    }

    // All Institution names
    private class SubdomainAsync extends AsyncTask<List<NameValuePair>, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            orgList.clear();
            dialog = new ProgressDialog(mContext);
            dialog.setTitle(getResources().getString(
                    R.string.fetch_institutions));
            dialog.setMessage(getResources().getString(R.string.please_wait));
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected Boolean doInBackground(List<NameValuePair>... params) {

            List<NameValuePair> namevaluepair = params[0];

            JsonParser jParser = new JsonParser();
            JSONObject json = jParser.getJSONFromUrlfrist(namevaluepair, Urls.url_ais_institute_list);

            Log.d("url", Urls.url_ais_institute_list);
            if (json != null) {
                try {
                    if (json.getString("result").equalsIgnoreCase("1")) {
                        JSONArray array = null;
                        try {
                            array = json.getJSONArray("data");
                            Log.d("array alies", array.toString());
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }
                        if (array != null) {
                            for (int i = 0; i < array.length(); i++) {
                                try {
                                    JSONObject obj = array.getJSONObject(i);
                                    InstituteData orgSet = new InstituteData();
                                    orgSet.setBase_url(obj.getString("base_url"));
                                    orgSet.setOrganizaion_id(obj.getString("organizaion_id"));
                                    orgSet.setOrganization_name(obj.getString("organization_name"));
                                    orgSet.setApp_logo(obj.getString("app_logo"));
                                    orgSet.setType(obj.getString("type"));
                                    orgSet.setApp_type(obj.getString("app_type"));
                                    orgSet.setVerify_code(obj.getString("verify_code"));
                                    orgSet.setAllies_type(obj.getString("alias_type"));
                                    orgList.add(orgSet);

                                } catch (Exception e) {

                                }

                            }
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

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

            final ArrayList<String> mList = new ArrayList<String>();
            for (int i = 0; i < orgList.size(); i++) {
                mList.add(orgList.get(i).getOrganization_name());
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext, R.layout.simple_spinner_dropdown_item_custom_new, mList);

            edt_subdomain.setAdapter(adapter);
            edt_subdomain.setOnItemClickListener(new OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1,
                                        int arg2, long arg3) {

                    int index = mList.indexOf(edt_subdomain.getText().toString());
                    btn_next.setVisibility(View.VISIBLE);
                    code_verify.setVisibility(View.GONE);
                    Log.d("institutiontttt", orgList.get(index).getOrganization_name() + "\n" + orgList.get(index).getBase_url());

                    Log.d("aliestype", orgList.get(index).getAllies_type());
                    //domain_view.setText(orgList.get(index).getBase_url());
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(edt_subdomain.getWindowToken(), 0);
                    InstituteData m = orgList.get(index);
                    SchoolAppUtils.SetSharedParameter(mContext, Constants.COMMON_URL, m.getBase_url());
                    SchoolAppUtils.SetSharedParameter(mContext, Constants.UORGANIZATIONID, m.getOrganizaion_id());
                    SchoolAppUtils.SetSharedParameter(mContext, Constants.VERYFY_CODE, m.getVerify_code());
                    SchoolAppUtils.SetSharedParameter(mContext, Constants.INSTITUTION_TYPE, m.getType());
                    SchoolAppUtils.SetSharedParameter(mContext, Constants.INSTITUTION_NAME, m.getOrganization_name());
                    SchoolAppUtils.SetSharedParameter(mContext, Constants.APP_TYPE, m.getApp_type());

                    SchoolAppUtils.SetSharedParameter(mContext, Constants.Alies_type, m.getAllies_type());

                    SchoolAppUtils.SetSharedParameter(mContext, Constants.VERIFIED_COUNTER, "1");

                    SchoolAppUtils.SetSharedParameter(mContext, Constants.AIS_APP_LOGO, "https://kolkataschool.knwedu.com/images/" + m.getApp_logo());

                    SchoolAppUtils.SetSharedParameter(mContext, Constants.PUSH_TITLE, m.getOrganization_name());

                    new Urls(SchoolAppUtils.GetSharedParameter(mContext, Constants.COMMON_URL));

                    CollegeUrls.base_url = SchoolAppUtils.GetSharedParameter(mContext, Constants.COMMON_URL);

                }
            });
            edt_subdomain.addTextChangedListener(new TextWatcher() {

                @Override
                public void afterTextChanged(Editable arg0) {

                }

                @Override
                public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {


                }

                @Override
                public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                    if (arg0.toString().isEmpty()) {
                        domain_view.setText("");
                        SchoolAppUtils.SetSharedParameter(mContext, Constants.UORGANIZATIONID, "0");
                        SchoolAppUtils.SetSharedParameter(mContext, Constants.INSTITUTION_NAME, "School Name");
                        SchoolAppUtils.SetSharedParameter(mContext, Constants.APP_TYPE, Constants.APP_TYPE_EXCLUSIVE);
                        SchoolAppUtils.SetSharedParameter(mContext, Constants.PASSWORD_PROTECTION, "0");
                        image.setImageResource(R.drawable.login_logo);

                        txt_signup.setVisibility(View.GONE);
                        signUp.setVisibility(View.GONE);

                    }
                }

            });

        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);

            if (requestCode == REQUEST_SIGNUP && resultCode == RESULT_FIRST_USER) {

                String username = data.getStringExtra("username");
                String password = data.getStringExtra("password");

                this.username.setText(username);
                this.password.setText(password);
                // new retriveToken().execute();

            }
        } catch (Exception ex) {
            Toast.makeText(mContext, ex.toString(), Toast.LENGTH_SHORT).show();
        }

    }

    private class GetTimetableListAsyntaskCollege extends AsyncTask<List<NameValuePair>, Void, Boolean> {
        String error;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(List<NameValuePair>... params) {
            timeTable = new ArrayList<TimeTable>();
            List<NameValuePair> nvp = params[0];
            JsonParser jParser = new JsonParser();
            JSONObject json = jParser.getJSONFromUrlnew(nvp, CollegeUrls.api_timetable_user);
            try {

                if (json != null) {
                    if (json.getString("result").equalsIgnoreCase("1")) {
                        JSONArray array;
                        try {
                            array = json.getJSONArray("data");
                        } catch (Exception e) {
                            return true;
                        }
                        timeTable = new ArrayList<TimeTable>();
                        for (int i = 0; i < array.length(); i++) {
                            TimeTable attendance = new TimeTable(array.getJSONObject(i));
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
            if (dialog != null) {
                dialog.dismiss();
                dialog = null;
            }
            if (CollegeAppUtils.GetSharedParameter(context, CollegeConstants.USERTYPEID).equalsIgnoreCase(CollegeConstants.USERTYPE_STUDENT)) {
                Intent incol = new Intent(context, CollegeMainActivity.class);
                incol.putExtra("fromlogin", "1");

                context.startActivity(incol);
                ((Activity) context).finish();
            } else if (CollegeAppUtils.GetSharedParameter(context, CollegeConstants.USERTYPEID).equalsIgnoreCase(CollegeConstants.USERTYPE_PARENT)) {
                context.startActivity(new Intent(context, CollegeParentMainActivity.class));
                ((Activity) context).finish();
            } else if (CollegeAppUtils.GetSharedParameter(context, CollegeConstants.USERTYPEID).equalsIgnoreCase(CollegeConstants.USERTYPE_TEACHER)) {
                context.startActivity(new Intent(context, CollegeTeacherMainActivity.class));
                ((Activity) context).finish();
            }

        }
    }
}
