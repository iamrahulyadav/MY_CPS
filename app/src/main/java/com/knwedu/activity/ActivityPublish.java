package com.knwedu.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.knwedu.calcuttapublicschool.R;

/**
 * Created by knowalluser on 06-03-2018.
 */

public class ActivityPublish extends AppCompatActivity {
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
        setContentView(R.layout.fragment_publish);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setTitle("Publish");
        getSupportActionBar().setTitle("");
    }
}
