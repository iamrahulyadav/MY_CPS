package com.knwedu.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.knwedu.adapter.TutorialAttendanceAdapter;
import com.knwedu.adapter.TutorialSchoolReuestAdapter;
import com.knwedu.calcuttapublicschool.R;

/**
 * Created by knowalluser on 20-04-2018.
 */

public class ActivityTutorialAttendance extends AppCompatActivity {

    ViewPager myPager;
    private Context mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial_reuest);
        mContext = ActivityTutorialAttendance.this;
        getSupportActionBar().setTitle("Tutorial");
        TutorialAttendanceAdapter adapter = new TutorialAttendanceAdapter();
        myPager = (ViewPager) findViewById(R.id.viewpager_layout);

        myPager.setAdapter(adapter);
        myPager.setCurrentItem(0);


    }

    public void jumpToNextPage(View view) {

        myPager.setCurrentItem(myPager.getCurrentItem() + 1, true);
        //Toast.makeText(mContext, "Current Item: " + myPager.getCurrentItem(), Toast.LENGTH_SHORT).show();

    }

    public void jumpToPreviousPage(View view) {

        myPager.setCurrentItem(myPager.getCurrentItem() - 1, true);
    }

    public void onCloseClick(View view) {

        onBackPressed();
    }
}
