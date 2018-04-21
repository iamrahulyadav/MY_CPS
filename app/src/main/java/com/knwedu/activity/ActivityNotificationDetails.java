package com.knwedu.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.knwedu.calcuttapublicschool.R;
import com.knwedu.model.Notification;

/**
 * Created by knowalluser on 16-04-2018.
 */

public class ActivityNotificationDetails extends AppCompatActivity {

    private TextView tv_role, tv_date, tv_title, tv_message;
    private Notification notification;

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
        setContentView(R.layout.activity_notification_details);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Notification Center");
        tv_role = (TextView) findViewById(R.id.tv_role);
        tv_date = (TextView) findViewById(R.id.tv_date);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_message = (TextView) findViewById(R.id.tv_message);
        notification = (Notification) getIntent().getSerializableExtra("notification");
        if (notification != null) {
            updateUI(notification);
        } else {

        }

    }

    private void updateUI(Notification notification) {
        if (notification.getRole() != null && !notification.getRole().equalsIgnoreCase("null"))
            tv_role.setText(notification.getRole());
        if (notification.getTimestamp() != null && !notification.getTimestamp().equalsIgnoreCase("null"))
            tv_date.setText(notification.getTimestamp());
        tv_message.setText(notification.getMessage());

        tv_title.setText(notification.getTitle());
    }
}
