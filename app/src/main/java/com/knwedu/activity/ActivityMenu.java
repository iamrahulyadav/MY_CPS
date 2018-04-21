package com.knwedu.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.knwedu.adapter.RoleListAdapter;
import com.knwedu.calcuttapublicschool.R;
import com.knwedu.constant.StaticConstant;
import com.knwedu.constant.Url;
import com.knwedu.firebasepush.Config;
import com.knwedu.model.AllMenuItemsModel;
import com.knwedu.model.MenuCategory;
import com.knwedu.model.Role;
import com.knwedu.model.UserClass;
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

/**
 * Created by ritwik.rai on 16/01/18.
 */

public class ActivityMenu extends AppCompatActivity {

    private Context mContext;
    private CardView cv_parent, cv_teacher, cv_student;
    private ListView lv_user_role;
    public static final String FLAG_MENU_EXTRA = "putExtraMenuFlag";

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        // getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Select Role");
        mContext = ActivityMenu.this;
        lv_user_role = (ListView) findViewById(R.id.lv_user_role);
        cv_parent = (CardView) findViewById(R.id.cv_parent);
        cv_teacher = (CardView) findViewById(R.id.cv_teacher);
        cv_student = (CardView) findViewById(R.id.cv_student);

        cv_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ActivityMain.class);
                intent.putExtra("role", "parent");
                startActivity(intent);
                finish();
            }
        });
        cv_teacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ActivityMain.class);
                intent.putExtra("role", "teacher");
                startActivity(intent);
                finish();
            }
        });
        cv_student.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ActivityMain.class);
                intent.putExtra("role", "student");
                startActivity(intent);
                finish();
            }
        });

        loadRoleList();

    }

    private void loadRoleList() {

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

                    PostWithJsonWebTask.callPostWithJsonWebtask(ActivityMenu.this, Url.GET_MENU, map, new ServerResponseStringCallback() {
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

                                    UserClass userClass = Util.fetchUserClass(mContext);
                                    if (userClass == null)
                                        userClass = new UserClass();

                                    userClass.setUserId(roles.get(position).getId());
                                    userClass.setName(roles.get(position).getName());
                                    userClass.setPhone(roles.get(position).getMobileNumber());
                                    userClass.setUserTypeId(roles.get(position).getUserTypeId());
                                    userClass.setOrganization(roles.get(position).getOrganization());
                                    userClass.setOrganiZationId(roles.get(position).getOrganizationId());
                                    userClass.setRole(roles.get(position).getRole());
                                    userClass.setEmail(userEmail);
                                    // SAVE USER CLASS
                                    Util.saveUserClass(mContext, userClass);
                                    // SAVE SELECTED ROLE
                                    Role selectedRole = roles.get(position);
                                    selectedRole.setJsonString(responseJson.toString());
                                    Util.saveSelectedRole(mContext, selectedRole);
                                    // SAVE SELECTED MENU CATEGORIES
                                    Util.saveSelectedMenuCategory(mContext, menuCategories);

                                    if (roles.get(position).getRole().trim().equalsIgnoreCase(StaticConstant.ROLE_PARENT)) {
                                        Intent intent = new Intent(mContext, ActivityMain.class);
                                        intent.putExtra(FLAG_MENU_EXTRA, menuCategories);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        intent.putExtra("role", "parent");
                                        startActivity(intent);
                                        finish();

                                    } else if (roles.get(position).getRole().trim().equalsIgnoreCase(StaticConstant.ROLE_TEACHER)) {
                                        Intent intent = new Intent(mContext, ActivityMain.class);
                                        intent.putExtra(FLAG_MENU_EXTRA, menuCategories);
                                        intent.putExtra("role", "teacher");
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                        finish();

                                    } else if (roles.get(position).getRole().trim().equalsIgnoreCase(StaticConstant.ROLE_STUDENT)) {
                                        Intent intent = new Intent(mContext, ActivityMain.class);
                                        intent.putExtra(FLAG_MENU_EXTRA, menuCategories);
                                        intent.putExtra("role", "student");
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                        finish();
                                    }

                                } else {
                                    Log.e("TAG", "LSE");
                                    Util.showMessageWithOk(ActivityMenu.this, "No Menu found for this role.");
                                }
                            } catch (Exception e) {
                                Log.e("TAG", "Exception: " + e.getLocalizedMessage());
                                e.printStackTrace();
                                Util.showMessageWithOk(ActivityMenu.this, "No Menu found for this role.");
                            }
                        }

                        @Override
                        public void ErrorMsg(VolleyError error) {

                        }
                    }, true, Request.Method.POST);
                }
            });
        } else {
            Util.showMessageWithOk(ActivityMenu.this, "Roles Cannot be fetched right now. Please try again.");
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
            JSONObject orgConfigJSONObj = responseJson.getJSONObject("org_config");
            SchoolAppUtils.SetSharedParameter(mContext, Constants.UORGANIZATIONID, role.getOrganizationId());
            Log.e("OrgID", "OrgID: " + role.getOrganizationId());

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
            /*SchoolAppUtils.SetSharedParameter(mContext, Constants.INSURANCE_AVIALABLE, "1");

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
        } catch (JSONException e) {
            Log.e("JSONException", "JSONException: " + e.getLocalizedMessage());
            e.printStackTrace();
        }
    }
}
