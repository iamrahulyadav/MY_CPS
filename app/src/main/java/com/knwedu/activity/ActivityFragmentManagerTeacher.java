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
import com.knwedu.ourschool.fragments.AisExamScheduleFragments;
import com.knwedu.ourschool.fragments.AisHelpFragment;
import com.knwedu.ourschool.fragments.AisSyllabus;
import com.knwedu.ourschool.fragments.AisTeacherSUbjectList;
import com.knwedu.ourschool.fragments.BehaviourListFragment;
import com.knwedu.ourschool.fragments.BlogListFragment;
import com.knwedu.ourschool.fragments.BulletinListFragment;
import com.knwedu.ourschool.fragments.CampusFragment;
import com.knwedu.ourschool.fragments.CareerFragment;
import com.knwedu.ourschool.fragments.DailyDiaryClassListFragment;
import com.knwedu.ourschool.fragments.ExamsListFragment;
import com.knwedu.ourschool.fragments.SchoolInfoFragment;
import com.knwedu.ourschool.fragments.SchoolSyllabusFragment;
import com.knwedu.ourschool.fragments.SpecialLectureFragment;
import com.knwedu.ourschool.fragments.SubjectListFragment;
import com.knwedu.ourschool.fragments.TeacherActivityListFragment;
import com.knwedu.ourschool.fragments.TeacherAnnouncementListFragment;
import com.knwedu.ourschool.fragments.TeacherAssignmentListFragment;
import com.knwedu.ourschool.fragments.TeacherClassWorkListFragment;
import com.knwedu.ourschool.fragments.TeacherProfileFragment;
import com.knwedu.ourschool.fragments.TeacherScheduleListFragment;
import com.knwedu.ourschool.fragments.TeacherSubjectAttendanceListFragment;
import com.knwedu.ourschool.fragments.TeacherTestListFragment;
import com.knwedu.ourschool.fragments.TeacherTimeTableFragment;
import com.knwedu.ourschool.fragments.TeacherWeeklyPlanListFragment;
import com.knwedu.ourschool.fragments.WebAccessFragment;
import com.knwedu.ourschool.utils.Constants;
import com.knwedu.ourschool.utils.SchoolAppUtils;


/**
 * Created by knowalladmin on 09/02/18.
 */

public class ActivityFragmentManagerTeacher extends AppCompatActivity {

    public static final String EXTRA_OBJC = "com.knwedu.school.EXTRA_OBJC";

    // give preparation animation activity transition
    public static void navigate(AppCompatActivity activity, View transitionImage, MenuItem obj) {
        Intent intent = new Intent(activity, ActivityFragmentManagerTeacher.class);
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
        appType = SchoolAppUtils.GetSharedParameter(ActivityFragmentManagerTeacher.this, Constants.APP_TYPE);
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

            case Constants.MENU_TEACHER_MY_PROFILE:
                fragment = new TeacherProfileFragment();
                showMonthWeek.setVisibility(View.GONE);
                break;

            case Constants.MENU_TEACHER_ACADEMICS:
                fragment = new SubjectListFragment();
                showMonthWeek.setVisibility(View.GONE);
                break;

            case Constants.MENU_TEACHER_ATTENDANCE:
                fragment = new TeacherSubjectAttendanceListFragment();
                showMonthWeek.setVisibility(View.GONE);
                break;

          /*  case StaticConstant.ACADEMICS:
                fragment = new FragmentAcademics();
                break;*/

            case Constants.MENU_TEACHER_ASSIGNMENT:
                fragment = new TeacherAssignmentListFragment();
                showMonthWeek.setVisibility(View.GONE);
               /* startActivity(new Intent(ActivityFragmentManagerParent.this, ActivityComposeMail.class));
                finish();*/
                break;

            case Constants.MENU_TEACHER_QUIZ:
                fragment = new TeacherTestListFragment();
                showMonthWeek.setVisibility(View.GONE);
                break;

            case Constants.MENU_TEACHER_ACTIVITY:
                fragment = new TeacherActivityListFragment();
                showMonthWeek.setVisibility(View.GONE);
                break;

            case Constants.MENU_TEACHER_ANNOUNCEMENT:
                fragment = new TeacherAnnouncementListFragment();
                showMonthWeek.setVisibility(View.GONE);
                break;

            case Constants.MENU_STUDENT_BEHAVIOUR:
                fragment = new BehaviourListFragment();
                showMonthWeek.setVisibility(View.GONE);
                break;

            case Constants.MENU_TEACHER_COURSE_WORK:
                fragment = new TeacherClassWorkListFragment();
                showMonthWeek.setVisibility(View.GONE);
                break;

            case Constants.MENU_TEACHER_LESSON_PLAN:
                fragment = new TeacherScheduleListFragment();
                showMonthWeek.setVisibility(View.GONE);
                break;

            case Constants.MENU_TEACHER_WEEKLY_PLAN:
                fragment = new TeacherWeeklyPlanListFragment();
                showMonthWeek.setVisibility(View.GONE);
                break;

            case Constants.MENU_TEACHER_BULLETIN:
                fragment = new BulletinListFragment();
                showMonthWeek.setVisibility(View.GONE);
                break;

            case Constants.MENU_TEACHER_EXAMS:
                if (appType.equals(Constants.APP_TYPE_COMMON_STANDARD)) {
                    fragment = new AisExamScheduleFragments();
                } else {
                    fragment = new ExamsListFragment();
                }
                //fragment = new AisExamScheduleFragments();
                showMonthWeek.setVisibility(View.GONE);
                break;

            case Constants.MENU_TEACHER_TIME_TABLE:
                fragment = new TeacherTimeTableFragment();
                showMonthWeek.setVisibility(View.GONE);
                break;

            case Constants.MENU_TEACHER_DIRECTORY:
                fragment = new SchoolInfoFragment();
                showMonthWeek.setVisibility(View.GONE);
                break;

            case Constants.MENU_TEACHER_BLOG:
                fragment = new BlogListFragment();
                showMonthWeek.setVisibility(View.GONE);
                break;

            case Constants.MENU_TEACHER_SPECIAL_CLASS:
                fragment = new SpecialLectureFragment();
                showMonthWeek.setVisibility(View.GONE);
                break;

            case Constants.SYLLABUS:
                if (appType.equals(Constants.APP_TYPE_COMMON_STANDARD)) {
                    fragment = new AisSyllabus();
                } else {
                    fragment = new SchoolSyllabusFragment(1);
                }
                //fragment = new AisSyllabus();
                showMonthWeek.setVisibility(View.GONE);
                break;

            case Constants.EVENT:
                fragment = new EventFragment(0);
                showMonthWeek.setVisibility(View.GONE);
                break;

            case Constants.MENU_TEACHER_HELP:
                fragment = new AisHelpFragment();
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
                    fragment = new AisTeacherSUbjectList();
                } else {
                    fragment = new DailyDiaryClassListFragment();
                }
                // fragment = new DailyDiaryClassListFragment();
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
