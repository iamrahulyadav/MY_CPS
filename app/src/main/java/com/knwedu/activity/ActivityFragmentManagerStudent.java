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
import com.knwedu.model.MenuItem;
import com.knwedu.ourschool.fragments.AisDailyDiaryListFragment;
import com.knwedu.ourschool.fragments.AisExamScheduleFragments;
import com.knwedu.ourschool.fragments.AisHelpFragment;
import com.knwedu.ourschool.fragments.AisSyllabus;
import com.knwedu.ourschool.fragments.AnnouncementListFragment;
import com.knwedu.ourschool.fragments.AssignmentListFragment;
import com.knwedu.ourschool.fragments.AttendanceViewFragment;
import com.knwedu.ourschool.fragments.BlogListFragment;
import com.knwedu.ourschool.fragments.BulletinListFragment;
import com.knwedu.ourschool.fragments.CampusFragment;
import com.knwedu.ourschool.fragments.CareerFragment;
import com.knwedu.ourschool.fragments.ClassFacilitiesListFragment;
import com.knwedu.ourschool.fragments.ClassFellowFragment;
import com.knwedu.ourschool.fragments.DailyDiaryStudentFragment;
import com.knwedu.ourschool.fragments.ExamsListFragment;
import com.knwedu.ourschool.fragments.HostelFragment;
import com.knwedu.ourschool.fragments.ManageFeesFragment;
import com.knwedu.ourschool.fragments.OrganizationListFragment;
import com.knwedu.ourschool.fragments.ProfileFragment;
import com.knwedu.ourschool.fragments.SchoolInfoFragment;
import com.knwedu.ourschool.fragments.SchoolSyllabusFragment;
import com.knwedu.ourschool.fragments.SpecialLectureFragment;
import com.knwedu.ourschool.fragments.StudentBehaviourListFragment;
import com.knwedu.ourschool.fragments.SubjectListFragment;
import com.knwedu.ourschool.fragments.TransportFragment;
import com.knwedu.ourschool.fragments.WebAccessFragment;
import com.knwedu.ourschool.fragments.WeeklyPlanListFragment;
import com.knwedu.ourschool.utils.Constants;
import com.knwedu.ourschool.utils.SchoolAppUtils;


/**
 * Created by knowalladmin on 09/02/18.
 */

public class ActivityFragmentManagerStudent extends AppCompatActivity {

    public static final String EXTRA_OBJC = "com.knwedu.school.EXTRA_OBJC";

    // give preparation animation activity transition
    public static void navigate(AppCompatActivity activity, View transitionImage, MenuItem obj) {
        Intent intent = new Intent(activity, ActivityFragmentManagerStudent.class);
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
        appType = SchoolAppUtils.GetSharedParameter(ActivityFragmentManagerStudent.this, Constants.APP_TYPE);
        // get extra object
        menuItem = (MenuItem) getIntent().getSerializableExtra(EXTRA_OBJC);
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

            case Constants.MENU_STUDENT_MY_PROFILE:
                fragment = new ProfileFragment();
                showMonthWeek.setVisibility(View.GONE);
                break;

            case Constants.MENU_STUDENT_ACADEMICS:
                fragment = new SubjectListFragment();
                showMonthWeek.setVisibility(View.GONE);
                break;

            case Constants.MENU_TEACHER_WEEKLY_PLAN:
                fragment = new WeeklyPlanListFragment();
                showMonthWeek.setVisibility(View.GONE);
                break;

          /*  case StaticConstant.ACADEMICS:
                fragment = new FragmentAcademics();
                break;*/

            case Constants.MENU_STUDENT_CLASS_FELLOW:
                fragment = new ClassFellowFragment();
                showMonthWeek.setVisibility(View.GONE);
               /* startActivity(new Intent(ActivityFragmentManagerParent.this, ActivityComposeMail.class));
                finish();*/
                break;

            case Constants.MENU_STUDENT_TIME_TABLE:
                fragment = new ClassFacilitiesListFragment();
                showMonthWeek.setVisibility(View.GONE);
                break;
            case Constants.MENU_STUDENT_ASSIGNMENT:
                fragment = new AssignmentListFragment(1);
                showMonthWeek.setVisibility(View.GONE);
                break;
            case Constants.MENU_STUDENT_QUIZ:
                fragment = new AssignmentListFragment(2);
                showMonthWeek.setVisibility(View.GONE);
                break;

            case Constants.MENU_STUDENT_ACTIVITY:
                fragment = new AssignmentListFragment(3);
                showMonthWeek.setVisibility(View.GONE);
                break;

            case Constants.MENU_STUDENT_EXAMS:
                if (appType.equals(Constants.APP_TYPE_COMMON_STANDARD)) {
                    fragment = new AisExamScheduleFragments();
                } else {
                    fragment = new ExamsListFragment();
                }
                // fragment = new AisExamScheduleFragments();
                showMonthWeek.setVisibility(View.GONE);
                break;

            case Constants.MENU_STUDENT_COURSEWORK:
                fragment = new AnnouncementListFragment(1);
                showMonthWeek.setVisibility(View.GONE);
                break;

            case Constants.MENU_STUDENT_ANNOUNCEMENT:
                fragment = new AnnouncementListFragment(2);
                showMonthWeek.setVisibility(View.GONE);
                break;

            case Constants.MENU_STUDENT_BULLETINS:
                fragment = new BulletinListFragment();
                break;

            case Constants.MENU_STUDENT_BEHAVIOUR:
                fragment = new StudentBehaviourListFragment();
                showMonthWeek.setVisibility(View.GONE);
                break;

            case Constants.MENU_STUDENT_ORGANIZATION:
                fragment = new OrganizationListFragment();
                showMonthWeek.setVisibility(View.GONE);
                break;
            case Constants.MENU_STUDENT_ATTENDANCE_INFO:
                fragment = new AttendanceViewFragment();
                showMonthWeek.setVisibility(View.GONE);
                break;

            case Constants.MENU_STUDENT_DIRECTORY:

                fragment = new SchoolInfoFragment();
                showMonthWeek.setVisibility(View.GONE);
              /*  if (menuItem.getRole().equalsIgnoreCase(StaticConstant.ROLE_TEACHER)) {
                    fragment = new FragmentAssignments();
                } else if (menuItem.getRole().equalsIgnoreCase(StaticConstant.ROLE_PARENT)) {
                    fragment = new FragmentAssignments();
                } else if (menuItem.getRole().equalsIgnoreCase(StaticConstant.ROLE_STUDENT)) {
                    fragment = new FragmentAssignments();
                }*/
                break;

            case Constants.MENU_STUDENT_BLOG:
                fragment = new BlogListFragment();
                showMonthWeek.setVisibility(View.GONE);
                break;
            case Constants.MENU_STUDENT_HELP:
                fragment = new AisHelpFragment();
                showMonthWeek.setVisibility(View.GONE);
                break;

            case Constants.MENU_STUDENT_TRANSPORT:
                fragment = new TransportFragment(0);
                showMonthWeek.setVisibility(View.GONE);
                break;

            case Constants.MENU_STUDENT_HOSTEL:
                fragment = new HostelFragment(0);
                showMonthWeek.setVisibility(View.GONE);
                break;

            case Constants.MENU_STUDENT_SPECIAL_CLASS:
                fragment = new SpecialLectureFragment();
                showMonthWeek.setVisibility(View.GONE);
                break;

            case Constants.SYLLABUS:
                if (appType.equals(Constants.APP_TYPE_COMMON_STANDARD)) {
                    fragment = new AisSyllabus();
                } else {
                    fragment = new SchoolSyllabusFragment(0);
                }
                // fragment = new AisSyllabus();
                showMonthWeek.setVisibility(View.GONE);
                break;

            case Constants.EVENT:
                fragment = new EventFragment(0);
                showMonthWeek.setVisibility(View.GONE);
                break;

            case Constants.MANAGE_FEES:
                fragment = new ManageFeesFragment(0);
                showMonthWeek.setVisibility(View.GONE);
                break;

            case Constants.CAMPUS:
                fragment = new CampusFragment();
                showMonthWeek.setVisibility(View.GONE);
                break;

            case Constants.MENU_WEBACCESS:
                fragment = new WebAccessFragment(0);
                showMonthWeek.setVisibility(View.GONE);
                break;

            case Constants.MENU_WEB_ACCESS:
                fragment = new WebAccessFragment(0);
                showMonthWeek.setVisibility(View.GONE);
                break;

            case Constants.MENU_CAREER:
                fragment = new CareerFragment(0);
                showMonthWeek.setVisibility(View.GONE);
                break;

            case Constants.MENU_DAILY_DIARY:
                if (appType.equals(Constants.APP_TYPE_COMMON_STANDARD)) {
                    fragment = new AisDailyDiaryListFragment();

                } else {
                    fragment = new DailyDiaryStudentFragment();
                }
                // fragment = new AisDailyDiaryListFragment();
                showMonthWeek.setVisibility(View.GONE);
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
