package com.knwedu.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.knwedu.adapter.TutorialReuestAdapter;
import com.knwedu.calcuttapublicschool.R;

/**
 * Created by ritwik.rai on 28/11/17.
 */

public class ActivityTutorailReuest extends AppCompatActivity {

    ViewPager myPager;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial_reuest);
        mContext = ActivityTutorailReuest.this;
        getSupportActionBar().setTitle("Tutorial");
        TutorialReuestAdapter adapter = new TutorialReuestAdapter();
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
