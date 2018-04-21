package com.knwedu.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.knwedu.adapter.RoleListAdapter;
import com.knwedu.calcuttapublicschool.R;
import com.knwedu.constant.RoleID;
import com.knwedu.constant.StaticConstant;
import com.knwedu.constant.Url;
import com.knwedu.data.Tools;
import com.knwedu.firebasepush.Config;
import com.knwedu.fragment.FragmentParent;
import com.knwedu.fragment.FragmentStudent;
import com.knwedu.fragment.FragmentTeacher;
import com.knwedu.model.AllMenuItemsModel;
import com.knwedu.model.MenuCategory;
import com.knwedu.model.Role;
import com.knwedu.model.UserClass;
import com.knwedu.ourschool.PrivacyPolicyActivity;
import com.knwedu.ourschool.activity.KnwEduChallengeActivity;
import com.knwedu.ourschool.utils.Constants;
import com.knwedu.ourschool.utils.DataStructureFramwork;
import com.knwedu.ourschool.utils.SchoolAppUtils;
import com.knwedu.util.Util;
import com.knwedu.volley.PostWithJsonWebTask;
import com.knwedu.volley.ServerResponseStringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ActivityMain extends AppCompatActivity {

    private Toolbar toolbar;
    private ActionBar actionBar;
    private NavigationView navigationView;
    private View parent_view;
    private String role;
    private Context mContext;
    private ArrayList<MenuCategory> menuCategories = new ArrayList<>();
    private Dialog customDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_new);
        parent_view = findViewById(android.R.id.content);

        mContext = ActivityMain.this;

        initToolbar();
        initDrawerMenu();
        // GET MENU CATEGORIES AND SUB CATEGORIES
        //  menuCategories = (ArrayList<MenuCategory>) getIntent().getSerializableExtra(ActivityMenu.FLAG_MENU_EXTRA);
        menuCategories = Util.fetchSelectedMenuCategory(mContext);


        //role = getIntent().getStringExtra("role");
        role = Util.fetchSelectedRole(mContext).getRole();
        try {
            parseOtherData(new JSONObject(Util.fetchSelectedRole(mContext).getJsonString()), Util.fetchSelectedRole(mContext));
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("TAG", "Exception: " + e.getLocalizedMessage());
        }
        if (role.equalsIgnoreCase("parent")) {
            displayFragment(RoleID.ID_PARENT, "");
            // fetchMenu(RoleID.ID_PARENT);
        } else if (role.equalsIgnoreCase("teacher")) {
            displayFragment(RoleID.ID_TEACHER, "");
            //  fetchMenu(RoleID.ID_TEACHER);
        } else if (role.equalsIgnoreCase("student")) {
            displayFragment(RoleID.ID_STUDENT, "");
            // fetchMenu(RoleID.ID_STUDENT);
        } else {
            displayFragment(RoleID.ID_PARENT, "");
            //fetchMenu(RoleID.ID_PARENT);
        }
        // for system bar in lollipop
        Tools.systemBarLolipop(this);

    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
    }

    private void initDrawerMenu() {
        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            public void onDrawerOpened(View drawerView) {
                hideKeyboard();
                updateSavedCounter(navigationView);
                super.onDrawerOpened(drawerView);
            }
        };
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItem.setChecked(true);
                displayFragment(menuItem.getItemId(), "");
                drawer.closeDrawers();
                return true;
            }
        });
    }

    private void displayFragment(int id, String title) {
        actionBar.setDisplayShowCustomEnabled(false);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(title);
        Fragment fragment = null;
        switch (id) {
            case RoleID.ID_PARENT:
                fragment = new FragmentParent();
                break;
            case RoleID.ID_TEACHER:
                fragment = new FragmentTeacher();
                break;
            case RoleID.ID_STUDENT:
                fragment = new FragmentStudent();
                break;
            case R.id.nav_switch_role:
                customDialog = new Dialog(ActivityMain.this);

                customDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                customDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                customDialog.setCancelable(true);
                LayoutInflater layoutInflater = (LayoutInflater) ActivityMain.this.getSystemService(LAYOUT_INFLATER_SERVICE);
                View view = layoutInflater.inflate(R.layout.dialog_role_menu, null);

                ListView lv_user_role = view.findViewById(R.id.lv_user_role);
                loadRoleList(lv_user_role);
                customDialog.setContentView(view);
                customDialog.setCanceledOnTouchOutside(true);
                // Start AlertDialog
                customDialog.show();
                break;

            case R.id.nav_challenge:
                Intent intent = new Intent(ActivityMain.this, KnwEduChallengeActivity.class);
                intent.putExtra("challengeUrl", "https://www.knwedu.com/challenge");
                startActivity(intent);
                break;

            case R.id.nav_not_center:
                Intent notificationIntent = new Intent(ActivityMain.this, ActivityNotificationCenter.class);
                startActivity(notificationIntent);
                break;

            case R.id.nav_legal_version:
                Intent legalversionIntent = new Intent(ActivityMain.this, PrivacyPolicyActivity.class);
                startActivity(legalversionIntent);
                break;

            case R.id.nav_help_tutorials:
                Intent helpIntent = new Intent(ActivityMain.this, ActivityAisHelp.class);
                startActivity(helpIntent);
                break;

        }
        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frame_content, fragment);
            fragmentTransaction.commit();
        }
    }

    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void switchFragment(int role) {
        actionBar.setDisplayShowCustomEnabled(false);
        actionBar.setDisplayShowTitleEnabled(true);

        Fragment fragment = null;
        if (role == RoleID.ID_PARENT) {
            actionBar.setTitle("");
            invalidateOptionsMenu();
            fragment = new FragmentParent();
        } else if (role == RoleID.ID_TEACHER) {
            actionBar.setTitle("");
            invalidateOptionsMenu();
            fragment = new FragmentTeacher();
        } else if (role == RoleID.ID_STUDENT) {
            actionBar.setTitle("");
            invalidateOptionsMenu();
            fragment = new FragmentStudent();
        }
        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frame_content, fragment);
            fragmentTransaction.commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_bulletins) {
            //Snackbar.make(parent_view, " Bulletins", Snackbar.LENGTH_SHORT).show();
            Intent intent = new Intent(mContext, ActivityBulletin.class);
            intent.putExtra("role", Util.fetchSelectedRole(mContext));
            startActivity(intent);
        } /*else if (item.getItemId() == R.id.action_publish) {
            //startActivity(new Intent(mContext, ActivityPublish.class));
        } */ else if (item.getItemId() == R.id.action_switch_child) {
            startActivity(new Intent(mContext, ActivitySwitchChild.class));
        } /*else if (item.getItemId() == R.id.action_bus_tracking) {
            //  Snackbar.make(parent_view, " Bus Tracking", Snackbar.LENGTH_SHORT).show();
            Intent intent = new Intent(mContext, ActivityBusTracking.class);
            startActivity(intent);
        }*/ /*else if (item.getItemId() == R.id.action_fees) {
            Intent intent = new Intent(mContext, ActivityFees.class);
            intent.putExtra("type", 1);
            startActivity(intent);
        } */else if (item.getItemId() == R.id.action_synchronize) {
            Intent intent = new Intent(mContext, ActivitySynchronize.class);
            startActivity(intent);
        }
        actionBar.setDisplayShowCustomEnabled(false);
        actionBar.setDisplayShowTitleEnabled(true);

        Fragment fragment = null;

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frame_content, fragment);
            fragmentTransaction.commit();
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        if (role.trim().equalsIgnoreCase("student"))
            getMenuInflater().inflate(R.menu.menu_activity_main_student, menu);
        else if (role.trim().equalsIgnoreCase("parent"))
            getMenuInflater().inflate(R.menu.menu_activity_main_parent, menu);
        else if (role.trim().equalsIgnoreCase("teacher"))
            getMenuInflater().inflate(R.menu.menu_activity_main_teacher, menu);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    private void updateSavedCounter(NavigationView nav) {
       /* TextView view = (TextView) nav.getMenu().findItem(itemId).getActionView().findViewById(R.id.counter);
        view.setText(String.valueOf(count));*/
        TextView tv_userName = (TextView) nav.getHeaderView(0).findViewById(R.id.tv_userName);
        TextView tv_userRole = (TextView) nav.getHeaderView(0).findViewById(R.id.tv_userRole);
        UserClass userClass = Util.fetchUserClass(mContext);
        if (userClass != null) {
            tv_userName.setText("" + userClass.getName());
        } else {
            tv_userName.setText("");
        }
        tv_userRole.setText("" + role);
    }

    @Override
    public void onBackPressed() {
    }


    public ArrayList<MenuCategory> getMenues() {
        return menuCategories;
    }


    private void loadRoleList(ListView lv_user_role) {

        final ArrayList<Role> roles = Util.fetchRoles(mContext).getRoles();
        if (roles != null && roles.size() > 0) {

            RoleListAdapter roleListAdapter = new RoleListAdapter(mContext, roles);
            lv_user_role.setAdapter(roleListAdapter);
            lv_user_role.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                    HashMap<String, String> map = new HashMap<>();
                    map.put("user_id", "" + roles.get(position).getId());
                    map.put("user_type_id", "" + roles.get(position).getUserTypeId());
                    map.put("organization_id", "" + roles.get(position).getOrganizationId());

                    PostWithJsonWebTask.callPostWithJsonWebtask(ActivityMain.this, Url.GET_MENU, map, new ServerResponseStringCallback() {
                        @Override
                        public void onSuccess(String resultJsonObject) {
                            Log.e("Response", "" + resultJsonObject);
                            try {
                                JSONObject responseJson = new JSONObject(resultJsonObject);
                                parseOtherData(responseJson, roles.get(position));
                                JSONArray menuNewJsonArray = responseJson.optJSONArray("new_menu");
                                ArrayList<MenuCategory> menuCategories = new ArrayList<>();
                                ArrayList<String> menuCatTitle = new ArrayList<>();
                                for (int i = 0; i < menuNewJsonArray.length(); i++) {
                                    JSONObject menuCategoryJsonObj = menuNewJsonArray.optJSONObject(i);
                                    MenuCategory menuCategory = new MenuCategory();
                                    menuCategory.setCategoryName(menuCategoryJsonObj.optString("Category"));

                                    JSONArray menuSubCatArray = menuCategoryJsonObj.optJSONArray("subcategory");
                                    ArrayList<com.knwedu.model.MenuItem> menuItems = new ArrayList<>();

                                    if (menuSubCatArray != null) {
                                        menuCatTitle.add(menuCategoryJsonObj.optString("Category"));
                                        for (int j = 0; j < menuSubCatArray.length(); j++) {
                                            JSONObject menuSubCatJSonObj = menuSubCatArray.optJSONObject(j);

                                            com.knwedu.model.MenuItem menuItem = new com.knwedu.model.MenuItem();
                                            menuItem.setName("" + menuSubCatJSonObj.optString("name"));
                                            menuItem.setDescription("" + menuSubCatJSonObj.optString("description"));
                                            menuItem.setCaption("" + menuSubCatJSonObj.optString("caption"));
                                            menuItem.setRole(roles.get(position).getRole());
                                            switch (menuSubCatJSonObj.optString("name")) {
                                                case StaticConstant.PROFILE:
                                                    menuItem.setImage(R.drawable.ic_profile_icon);
                                                    break;
                                                case StaticConstant.HELP:
                                                    menuItem.setImage(R.drawable.ic_help);
                                                    break;

                                                case StaticConstant.STUDENTACCESS:
                                                    menuItem.setImage(R.drawable.ic_access);
                                                    break;
                                                case StaticConstant.WEBACCESS:
                                                    menuItem.setImage(R.drawable.ic_access);
                                                    break;
                                                case StaticConstant.TUTORIAL:
                                                    menuItem.setImage(R.drawable.ic_tutorial);
                                                    break;
                                                case StaticConstant.CHILDPROFILE:
                                                    menuItem.setImage(R.drawable.ic_profile_icon);
                                                    break;
                                                case StaticConstant.BEHAVIOUR:
                                                    menuItem.setImage(R.drawable.ic_behaviour);
                                                    break;
                                                case StaticConstant.CLASSFELLOW:
                                                    menuItem.setImage(R.drawable.ic_school_fellow);
                                                    break;
                                                case StaticConstant.SUBJECT:
                                                    menuItem.setImage(R.drawable.ic_subjects);
                                                    break;
                                                case StaticConstant.EXAM:
                                                    menuItem.setImage(R.drawable.ic_exams);
                                                    break;
                                                case StaticConstant.TIMETABLE:
                                                    menuItem.setImage(R.drawable.ic_timetable);
                                                    break;
                                                case StaticConstant.SPECIALCLASS:
                                                    menuItem.setImage(R.drawable.ic_special_class);
                                                    break;
                                                case StaticConstant.SYLLABUS:
                                                    menuItem.setImage(R.drawable.ic_syllabus);
                                                    break;
                                                case StaticConstant.ATTENDANCE:
                                                    menuItem.setImage(R.drawable.ic_attendance);
                                                    break;
                                                case StaticConstant.TEST:
                                                    menuItem.setImage(R.drawable.ic_test);
                                                    break;
                                                case StaticConstant.ACTIVITY:
                                                    menuItem.setImage(R.drawable.ic_activity);
                                                    break;
                                                case StaticConstant.ASSIGNMENT:
                                                    menuItem.setImage(R.drawable.ic_assignment);
                                                    break;
                                                case StaticConstant.CLASSWORK:
                                                    menuItem.setImage(R.drawable.ic_class_work);
                                                    break;
                                                case StaticConstant.ANNOUNCEMENT:
                                                    menuItem.setImage(R.drawable.ic_announcement);
                                                    break;
                                                case StaticConstant.BULLETIN:
                                                    menuItem.setImage(R.drawable.ic_bulletins);
                                                    break;
                                                case StaticConstant.DIRECTORY:
                                                    menuItem.setImage(R.drawable.ic_directory);
                                                    break;
                                                case StaticConstant.BLOG:
                                                    menuItem.setImage(R.drawable.ic_blog);
                                                    break;
                                                case StaticConstant.DAILY_DIARY:
                                                    menuItem.setImage(R.drawable.ic_daily_diary);
                                                    break;
                                                case StaticConstant.EVENTCALENDAR:
                                                    menuItem.setImage(R.drawable.ic_event_calendar);
                                                    break;
                                                case StaticConstant.FEE:
                                                    menuItem.setImage(R.drawable.ic_rupee);
                                                    break;
                                                case StaticConstant.BUSTRACKING:
                                                    menuItem.setImage(R.drawable.ic_bus);
                                                    break;
                                                case StaticConstant.REQUESTS:
                                                    menuItem.setImage(R.drawable.ic_school_reuest);
                                                    break;
                                                case StaticConstant.SCHOOLREQUEST:
                                                    menuItem.setImage(R.drawable.ic_self_reuest);
                                                    break;
                                                case StaticConstant.FEEDBACK:
                                                    menuItem.setImage(R.drawable.ic_feedback);
                                                    break;
                                                case StaticConstant.HOSTEL:
                                                    menuItem.setImage(R.drawable.ic_hostel);
                                                    break;

                                            }

                                            menuItems.add(menuItem);
                                        }

                                        menuCategory.setSubCategories(menuItems);
                                        menuCategories.add(menuCategory);
                                    } else {
                                        // No SubCategories- so ignore this category

                                    }

                                }
                                if (menuCategories.size() > 0) {
                                    AllMenuItemsModel allMenuItemsModel = new AllMenuItemsModel();
                                    allMenuItemsModel.setCategories(menuCatTitle);
                                    allMenuItemsModel.setMenuCategories(menuCategories);

                                    Util.saveMenuItems(mContext, allMenuItemsModel);

                                    UserClass userClass = new UserClass();
                                    userClass.setUserId(roles.get(position).getId());
                                    userClass.setName(roles.get(position).getName());
                                    userClass.setPhone(roles.get(position).getMobileNumber());
                                    userClass.setUserTypeId(roles.get(position).getUserTypeId());
                                    userClass.setOrganization(roles.get(position).getOrganization());
                                    userClass.setOrganiZationId(roles.get(position).getOrganizationId());
                                    userClass.setRole(roles.get(position).getRole());
                                    userClass.setEmail(userEmail);
                                    //SAVE USER CLASS
                                    Util.saveUserClass(mContext, userClass);
                                    // SAVE SELECTED ROLE
                                    Role selectedRole = new Role();
                                    selectedRole = roles.get(position);
                                    selectedRole.setJsonString(responseJson.toString());
                                    Util.saveSelectedRole(mContext, selectedRole);
                                    // SAVE SELECTED MENU CATEGORIES
                                    Util.saveSelectedMenuCategory(mContext, menuCategories);

                                    if (roles.get(position).getRole().trim().equalsIgnoreCase(StaticConstant.ROLE_PARENT)) {
                                        switchFragment(RoleID.ID_PARENT);
                                        role = "parent";
                                        customDialog.dismiss();

                                    } else if (roles.get(position).getRole().trim().equalsIgnoreCase(StaticConstant.ROLE_TEACHER)) {
                                        switchFragment(RoleID.ID_TEACHER);
                                        role = "teacher";
                                        customDialog.dismiss();

                                    } else if (roles.get(position).getRole().trim().equalsIgnoreCase(StaticConstant.ROLE_STUDENT)) {
                                        switchFragment(RoleID.ID_STUDENT);
                                        role = "student";
                                        customDialog.dismiss();
                                    }

                                } else {
                                    Log.e("TAG", "LSE");
                                    Util.showMessageWithOk(ActivityMain.this, "No Menu found for this role.");
                                }
                            } catch (Exception e) {
                                Log.e("TAG", "Exception: " + e.getLocalizedMessage());
                                e.printStackTrace();
                                Util.showMessageWithOk(ActivityMain.this, "No Menu found for this role.");
                            }
                        }

                        @Override
                        public void ErrorMsg(VolleyError error) {

                        }
                    }, true, Request.Method.POST);
                }
            });
        } else {
            Util.showMessageWithOk(ActivityMain.this, "Roles Cannot be fetched right now. Please try again.");
        }
    }

    private String userEmail = "";
    // OTHER JSON DATA
    private DataStructureFramwork.Permission permission;
    private DataStructureFramwork.PermissionAdd permissionadd;
    private DataStructureFramwork.Request requests;

    private void parseOtherData(JSONObject responseJson, Role role) {
        try {
            SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
            String regId = pref.getString("regId", null);
            // regid = regId;
            JSONObject object = responseJson;
            String menu_tag;
            String menu_title;

            //TODO COMMENTED
          /*  if (SchoolAppUtils.GetSharedParameter(mContext, Constants.APP_TYPE).equals(Constants.APP_TYPE_COMMON_STANDARD)) {
                menu_tag = responseJson.getString("menu_info");
                menu_title = responseJson.getString("menu_caption");
            } else {
                menu_tag = responseJson.getString("menu_info") + "|webaccess";
                menu_title = responseJson.getString("menu_caption") + "|Web Access";
            }*/

            // LINE ADDED
            menu_tag = responseJson.getString("menu_info");
            menu_title = responseJson.getString("menu_caption");
            String email = responseJson.optString("email");
            Log.e("Email", "Mal: " + email);
            userEmail = email;
            SchoolAppUtils.SetSharedParameter(mContext, Constants.ORGEMAIL, "" + email);
            SchoolAppUtils.SetSharedParameter(mContext, Constants.APP_TYPE, Constants.APP_TYPE_EXCLUSIVE);
            SchoolAppUtils.SetSharedParameter(mContext, Constants.UORGANIZATIONID, role.getOrganizationId());

            //=====================================================================

            JSONObject permission_config = responseJson.getJSONObject("activity_permission");
            try {
                JSONObject request_config = responseJson.getJSONObject("requests");
                // Parent Request
                if (request_config != null) {
                    requests = new DataStructureFramwork.Request(request_config);
                    SchoolAppUtils.SetSharedParameter(mContext, Constants.LEAVE_REQUEST, requests.leave_request.toString());
                    SchoolAppUtils.SetSharedParameter(mContext, Constants.REQUEST_FOR_BOOK, requests.request_for_book.toString());
                    SchoolAppUtils.SetSharedParameter(mContext, Constants.REQUEST_FOR_ID_CARD, requests.request_for_id.toString());
                    SchoolAppUtils.SetSharedParameter(mContext, Constants.REQUEST_FOR_UNIFORM, requests.request_for_uniform.toString());
                    SchoolAppUtils.SetSharedParameter(mContext, Constants.SPECIAL_REQUEST, requests.special_request.toString());


                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            JSONObject permission_add = responseJson.getJSONObject("permission");
            //TODO COMMENTED
            SchoolAppUtils.SetSharedParameter(mContext, Constants.UORGANIZATIONID, role.getOrganizationId());
            Log.e("OrgID", "OrgID: " + role.getOrganizationId());
            JSONObject orgConfigJSONObj = responseJson.getJSONObject("org_config");
            if (orgConfigJSONObj != null) {
                //--------------------------------INSURANCE and premium--------------//
                try {
                    String isInsuranceAvailable = orgConfigJSONObj.getString("is_insurance").trim();
                    String isPremiumServiceAvailable = orgConfigJSONObj.getString("is_premium_service").trim();
                    String transportType = orgConfigJSONObj.getString("transport_mobile_based").trim();
                    SchoolAppUtils.SetSharedParameter(mContext, Constants.INSURANCE_AVIALABLE, "" + isInsuranceAvailable);

                    SchoolAppUtils.SetSharedParameter(mContext, Constants.PREMIUM_SERVICE_AVAILABLE, "" + isPremiumServiceAvailable);

                    SchoolAppUtils.SetSharedParameter(mContext, Constants.NEWTRANSPORT_TYPE, "" + transportType);

                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }

            // LINES ADDED
           /* SchoolAppUtils.SetSharedParameter(mContext, Constants.INSURANCE_AVIALABLE, "1");

            SchoolAppUtils.SetSharedParameter(mContext, Constants.PREMIUM_SERVICE_AVAILABLE, "1");

            SchoolAppUtils.SetSharedParameter(mContext, Constants.NEWTRANSPORT_TYPE, "Y");*/
            //==================================================================

            if (object != null) {
                // user details
                //userInfo = new DataStructureFramwork.UserInfo(object);
                SchoolAppUtils.SetSharedParameter(mContext, Constants.USERTYPEID, role.getUserTypeId());
                SchoolAppUtils.SetSharedParameter(mContext, Constants.USERID, role.getId());
                // TODO GET EMAIL HERE INSTEAD OF EMPTY STRING
                SchoolAppUtils.SetSharedParameter(mContext, Constants.UALTEMAIL, "");
                // TODO GET EMAIL HERE INSTEAD OF EMPTY STRING
                SchoolAppUtils.SetSharedParameter(mContext, Constants.ORGEMAIL, "");

                SchoolAppUtils.SetSharedParameter(mContext, Constants.UMOBILENO, role.getMobileNumber());
                SchoolAppUtils.SetSharedParameter(mContext, Constants.PASSWORD, Util.fetchUserClass(mContext).getPassword());

                SchoolAppUtils.SetSharedParameter(mContext, Constants.DEVICE_TOKEN, regId);


            }
            // Menu tag
            if (menu_tag != null) {
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
            }

            if (role.getUserTypeId().equalsIgnoreCase(Constants.USERTYPE_PARENT)) {
                //TODO GET CHILD INFO
                JSONArray child_info_arry = responseJson.getJSONArray("child_info");
                JSONObject object_chield_info = child_info_arry.getJSONObject(0);
                DataStructureFramwork.StudentProfileInfo selectedStudent = new DataStructureFramwork.StudentProfileInfo(object_chield_info);
                SchoolAppUtils.SetSharedParameter(mContext, Constants.CHILD_ID, selectedStudent.user_id);
                SchoolAppUtils.SetSharedParameter(mContext, Constants.CHILD_NAME, selectedStudent.fullname);
                SchoolAppUtils.SetSharedParameter(mContext, Constants.STUDENT_CLASS_SECTION, selectedStudent.class_section);
                SchoolAppUtils.SetSharedParameter(mContext, Constants.SELECTED_CHILD_OBJECT, object_chield_info.toString());
                SchoolAppUtils.SetSharedParameter(mContext, Constants.SECTION_ID, selectedStudent.section_id);

            } else if (role.getUserTypeId().equalsIgnoreCase(Constants.USERTYPE_STUDENT)) {
                SchoolAppUtils.SetSharedParameter(mContext, Constants.CHILD_ID, role.getId());

            }
            String sideNav = responseJson.optString("side_menu");
            updateUI(sideNav);
        } catch (JSONException e) {
            Log.e("JSONException", "JSONException: " + e.getLocalizedMessage());
            e.printStackTrace();
        }
    }

    private void updateUI(String sideNav) {

        Menu nav_Menu = navigationView.getMenu();
        /*if (sideNav.contains("home")) {
            nav_Menu.findItem(R.id.nav_home).setVisible(true);
        } else {
            nav_Menu.findItem(R.id.nav_home).setVisible(false);
        }*/
        /*if (sideNav.contains("Switch Role")) {
            nav_Menu.findItem(R.id.nav_switch_role).setVisible(true);
        } else {
            nav_Menu.findItem(R.id.nav_switch_role).setVisible(false);
        }*/
        /*if (sideNav.contains("KnwEduChallenge")) {
            nav_Menu.findItem(R.id.nav_challenge).setVisible(true);
        } else {
            nav_Menu.findItem(R.id.nav_challenge).setVisible(false);
        }*/
        nav_Menu.findItem(R.id.nav_challenge).setVisible(false);
        /*if (sideNav.contains("Premium Service")) {
            nav_Menu.findItem(R.id.nav_premium_service).setVisible(true);
        } else {
            nav_Menu.findItem(R.id.nav_premium_service).setVisible(false);
        }*/
        nav_Menu.findItem(R.id.nav_premium_service).setVisible(false);
        /*if (sideNav.contains("Notification Center")) {
            nav_Menu.findItem(R.id.nav_not_center).setVisible(true);
        } else {
            nav_Menu.findItem(R.id.nav_not_center).setVisible(false);
        }*/
        /*if (sideNav.contains("Legal & Version")) {
            nav_Menu.findItem(R.id.nav_legal_version).setVisible(true);
        } else {
            nav_Menu.findItem(R.id.nav_legal_version).setVisible(false);
        }*/
        /*if (sideNav.contains("Settings")) {
            nav_Menu.findItem(R.id.nav_setting).setVisible(true);
        } else {
            nav_Menu.findItem(R.id.nav_setting).setVisible(false);
        }*/
        nav_Menu.findItem(R.id.nav_setting).setVisible(false);
       /* if (sideNav.contains("Help & Tutorials")) {
            nav_Menu.findItem(R.id.nav_help_tutorials).setVisible(true);
        } else {
            nav_Menu.findItem(R.id.nav_help_tutorials).setVisible(false);
        }*/
       /* if (sideNav.contains("")) {
            nav_Menu.findItem(R.id.nav_testing).setVisible(true);
        } else {
            nav_Menu.findItem(R.id.nav_testing).setVisible(false);
        }
        */
        nav_Menu.findItem(R.id.nav_testing).setVisible(false);
    }
}
