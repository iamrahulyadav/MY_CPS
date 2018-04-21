package com.knwedu.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.knwedu.com.knwedu.calendar.EventFragment;
import com.knwedu.calcuttapublicschool.R;
import com.knwedu.constant.StaticConstant;
import com.knwedu.model.MenuItem;
import com.knwedu.ourschool.fragments.AimsManageFeesFragment;
import com.knwedu.ourschool.fragments.AisDailyDiaryListFragment;
import com.knwedu.ourschool.fragments.AisExamScheduleFragments;
import com.knwedu.ourschool.fragments.AisHelpFragment;
import com.knwedu.ourschool.fragments.AisSyllabus;
import com.knwedu.ourschool.fragments.AisTransportFragment;
import com.knwedu.ourschool.fragments.AnnouncementListFragment;
import com.knwedu.ourschool.fragments.AssignmentListFragment;
import com.knwedu.ourschool.fragments.AttendanceViewFragment;
import com.knwedu.ourschool.fragments.BlogListFragment;
import com.knwedu.ourschool.fragments.BulletinListFragment;
import com.knwedu.ourschool.fragments.CampusFragment;
import com.knwedu.ourschool.fragments.CareerFragment;
import com.knwedu.ourschool.fragments.ClassFacilitiesListFragment;
import com.knwedu.ourschool.fragments.ClassFellowFragment;
import com.knwedu.ourschool.fragments.DailyDiaryParentFragment;
import com.knwedu.ourschool.fragments.ExamsListFragment;
import com.knwedu.ourschool.fragments.HostelFragment;
import com.knwedu.ourschool.fragments.OrganizationListFragment;
import com.knwedu.ourschool.fragments.ParentFeedbackFragment;
import com.knwedu.ourschool.fragments.ParentProfileFragment;
import com.knwedu.ourschool.fragments.ParentRequestFragment;
import com.knwedu.ourschool.fragments.ProfileFragment;
import com.knwedu.ourschool.fragments.RequestToParentFragment;
import com.knwedu.ourschool.fragments.SchoolInfoFragment;
import com.knwedu.ourschool.fragments.SchoolSyllabusFragment;
import com.knwedu.ourschool.fragments.SpecialLectureFragment;
import com.knwedu.ourschool.fragments.StudentBehaviourListFragment;
import com.knwedu.ourschool.fragments.SubjectListFragment;
import com.knwedu.ourschool.fragments.TransportFragment;
import com.knwedu.ourschool.fragments.WebAccessFragment;
import com.knwedu.ourschool.utils.Constants;
import com.knwedu.ourschool.utils.SchoolAppUtils;


/**
 * Created by knowalladmin on 09/02/18.
 */

public class ActivityFragmentManagerParent extends AppCompatActivity {

    public static final String EXTRA_OBJC = "com.knwedu.school.EXTRA_OBJC";

    // give preparation animation activity transition
    public static void navigate(AppCompatActivity activity, View transitionImage, MenuItem obj) {
        Intent intent = new Intent(activity, ActivityFragmentManagerParent.class);
        intent.putExtra(EXTRA_OBJC, obj);
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, transitionImage, EXTRA_OBJC);
        ActivityCompat.startActivity(activity, intent, options.toBundle());
    }

    //private Toolbar toolbar;
    private ActionBar actionBar;

    private View parent_view;

    // Extra Object
    private MenuItem menuItem;

    public static Button showMonthWeek;
    public static FloatingActionButton btnAdd;
    private String appType;


    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_frag);
        parent_view = findViewById(R.id.frame_content);
        showMonthWeek = (Button) findViewById(R.id.show_monthly_weekly);
        showMonthWeek.setVisibility(View.GONE);
        btnAdd = (FloatingActionButton) findViewById(R.id.add_assignment);
        btnAdd.setVisibility(View.GONE);
        // get extra object
        menuItem = (MenuItem) getIntent().getSerializableExtra(EXTRA_OBJC);
        appType = SchoolAppUtils.GetSharedParameter(ActivityFragmentManagerParent.this, Constants.APP_TYPE);
        // Log.e("TAG", "Name: " + menuItem.getName());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("" + menuItem.getCaption());
        // initToolbar();
        initFragment();
    }

    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    private void initFragment() {
        Fragment fragment = null;
        switch (menuItem.getName()) {

            case StaticConstant.PROFILE:
                fragment = new ParentProfileFragment();
                showMonthWeek.setVisibility(View.GONE);
                btnAdd.setVisibility(View.GONE);
                break;

            case StaticConstant.CHILDPROFILE:
                fragment = new ProfileFragment();
                showMonthWeek.setVisibility(View.GONE);
                btnAdd.setVisibility(View.GONE);
                break;

            case StaticConstant.CLASSFELLOW:
                fragment = new ClassFellowFragment();
                showMonthWeek.setVisibility(View.GONE);
                btnAdd.setVisibility(View.GONE);
                break;

          /*  case StaticConstant.ACADEMICS:
                fragment = new FragmentAcademics();
                break;*/

            case StaticConstant.HELP:
                fragment = new AisHelpFragment();
                showMonthWeek.setVisibility(View.GONE);
                btnAdd.setVisibility(View.GONE);
               /* startActivity(new Intent(ActivityFragmentManagerParent.this, ActivityComposeMail.class));
                finish();*/
                break;

            case StaticConstant.HOSTEL:
                fragment = new HostelFragment(1);
                showMonthWeek.setVisibility(View.GONE);
                btnAdd.setVisibility(View.GONE);
                break;
            case StaticConstant.BEHAVIOUR:
                fragment = new StudentBehaviourListFragment();
                showMonthWeek.setVisibility(View.GONE);
                btnAdd.setVisibility(View.GONE);
                break;
            case StaticConstant.SYLLABUS:
                if (appType.equals(Constants.APP_TYPE_COMMON_STANDARD)) {
                    fragment = new AisSyllabus();
                } else {

                    fragment = new SchoolSyllabusFragment(1);
                }
                //fragment = new AisSyllabus();
                showMonthWeek.setVisibility(View.GONE);
                btnAdd.setVisibility(View.GONE);
                break;

            case StaticConstant.SUBJECT:
                fragment = new SubjectListFragment();
                showMonthWeek.setVisibility(View.GONE);
                btnAdd.setVisibility(View.GONE);
                break;

            case StaticConstant.EVENTCALENDAR:
                fragment = new EventFragment(0);
                showMonthWeek.setVisibility(View.GONE);
                btnAdd.setVisibility(View.GONE);
                break;

            case StaticConstant.TIMETABLE:
                fragment = new ClassFacilitiesListFragment();
                showMonthWeek.setVisibility(View.GONE);
                btnAdd.setVisibility(View.GONE);
                break;

            case StaticConstant.EXAM:
                if (appType.equals(Constants.APP_TYPE_COMMON_STANDARD)) {
                    fragment = new AisExamScheduleFragments();
                } else {
                    fragment = new ExamsListFragment();
                }
                //fragment = new AisExamScheduleFragments();
                showMonthWeek.setVisibility(View.GONE);
                btnAdd.setVisibility(View.GONE);
                break;

            case StaticConstant.FEE:
                if (appType.equals(Constants.APP_TYPE_COMMON_STANDARD)) {
                    fragment = new AimsManageFeesFragment(1);
                } else {
                    //fragmentManager.beginTransaction().replace(R.id.content_frame, new ManageFeesFragment(1)).commit();
                    fragment = new AimsManageFeesFragment(1);
                }
                // fragment = new AimsManageFeesFragment(1);
                showMonthWeek.setVisibility(View.GONE);
                btnAdd.setVisibility(View.GONE);
                break;

            case StaticConstant.SPECIALCLASS:
                fragment = new SpecialLectureFragment();
                showMonthWeek.setVisibility(View.GONE);
                btnAdd.setVisibility(View.GONE);
                break;

            case StaticConstant.ATTENDANCE:
                fragment = new AttendanceViewFragment();
                showMonthWeek.setVisibility(View.GONE);
                btnAdd.setVisibility(View.GONE);
                break;
            case StaticConstant.DIRECTORY:
                fragment = new SchoolInfoFragment();
                showMonthWeek.setVisibility(View.GONE);
                btnAdd.setVisibility(View.GONE);
                break;

            case StaticConstant.ASSIGNMENT:

                fragment = new AssignmentListFragment(1);
                showMonthWeek.setVisibility(View.VISIBLE);
                showMonthWeek.setText(R.string.all);
                btnAdd.setVisibility(View.GONE);
              /*  if (menuItem.getRole().equalsIgnoreCase(StaticConstant.ROLE_TEACHER)) {
                    fragment = new FragmentAssignments();
                } else if (menuItem.getRole().equalsIgnoreCase(StaticConstant.ROLE_PARENT)) {
                    fragment = new FragmentAssignments();
                } else if (menuItem.getRole().equalsIgnoreCase(StaticConstant.ROLE_STUDENT)) {
                    fragment = new FragmentAssignments();
                }*/
                break;

            case StaticConstant.TEST:
                fragment = new AssignmentListFragment(2);
                showMonthWeek.setVisibility(View.VISIBLE);
                showMonthWeek.setText(R.string.all);
                btnAdd.setVisibility(View.GONE);
                break;
            case StaticConstant.ACTIVITY:
                fragment = new AssignmentListFragment(3);
                showMonthWeek.setVisibility(View.VISIBLE);
                showMonthWeek.setText(R.string.all);
                btnAdd.setVisibility(View.GONE);
                break;

            case StaticConstant.CLASSWORK:
                fragment = new AnnouncementListFragment(1);
                showMonthWeek.setVisibility(View.GONE);
                btnAdd.setVisibility(View.GONE);
                break;

            case StaticConstant.ANNOUNCEMENT:
                fragment = new AnnouncementListFragment(2);
                showMonthWeek.setVisibility(View.GONE);
                btnAdd.setVisibility(View.GONE);
                break;

            case StaticConstant.BULLETIN:
                fragment = new BulletinListFragment();
                showMonthWeek.setVisibility(View.GONE);
                btnAdd.setVisibility(View.GONE);
                break;

            case StaticConstant.BLOG:
                fragment = new BlogListFragment();
                showMonthWeek.setVisibility(View.GONE);
                btnAdd.setVisibility(View.GONE);
                break;

            case StaticConstant.DAILY_DIARY:
                // -------------------Daily Diary---------------------
                if (appType.equals(Constants.APP_TYPE_COMMON_STANDARD)) {
                    fragment = new AisDailyDiaryListFragment();

                } else {
                    fragment = new DailyDiaryParentFragment();
                }
                // fragment = new AisDailyDiaryListFragment();
                showMonthWeek.setVisibility(View.GONE);
                btnAdd.setVisibility(View.GONE);
                break;

            case StaticConstant.SCHOOLREQUEST:
                fragment = new RequestToParentFragment();
                showMonthWeek.setVisibility(View.GONE);
                btnAdd.setVisibility(View.GONE);
                break;

            case StaticConstant.REQUESTS:
                fragment = new ParentRequestFragment();
                showMonthWeek.setVisibility(View.GONE);
                btnAdd.setVisibility(View.GONE);
                break;

            case StaticConstant.FEEDBACK:
                fragment = new ParentFeedbackFragment();
                showMonthWeek.setVisibility(View.GONE);
                btnAdd.setVisibility(View.GONE);
                break;

            case StaticConstant.CAREER:
                fragment = new CareerFragment(1);
                showMonthWeek.setVisibility(View.GONE);
                btnAdd.setVisibility(View.GONE);
                break;

            case StaticConstant.ORGANIZATION:
                fragment = new OrganizationListFragment();
                showMonthWeek.setVisibility(View.GONE);
                btnAdd.setVisibility(View.GONE);
                break;

            case StaticConstant.STUDENTACCESS:
                // fragment = new FragmentStudentAccess();
                showMonthWeek.setVisibility(View.GONE);
                btnAdd.setVisibility(View.GONE);
                break;
            case StaticConstant.WEBACCESS:
                fragment = new WebAccessFragment(0);
                showMonthWeek.setVisibility(View.GONE);
                btnAdd.setVisibility(View.GONE);
                break;
            case StaticConstant.BUSTRACKING:
                if (appType.equals(Constants.APP_TYPE_COMMON_STANDARD)) {

                    fragment = new AisTransportFragment();
                } else {
                    if (SchoolAppUtils.GetSharedParameter(ActivityFragmentManagerParent.this, Constants.NEWTRANSPORT_TYPE).equals("Y")) {
                        fragment = new AisTransportFragment();
                    } else {
                        fragment = new TransportFragment(1);
                    }
                }
                // fragment = new AisTransportFragment();
                showMonthWeek.setVisibility(View.GONE);
                btnAdd.setVisibility(View.GONE);
                break;
            case StaticConstant.CAMPUS:
                fragment = new CampusFragment();
                showMonthWeek.setVisibility(View.GONE);
                btnAdd.setVisibility(View.GONE);
                break;


        }
        /*FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content, fragment).commit();*/
        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frame_content, fragment);
            fragmentTransaction.commit();
        }

    }

    private void initToolbar() {
        //toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle("");
    }
}
