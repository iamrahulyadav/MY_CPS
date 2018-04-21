package com.knwedu.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.knwedu.adapter.NotificationListAdapter;
import com.knwedu.calcuttapublicschool.R;
import com.knwedu.db.NotificationDB;
import com.knwedu.model.Notification;
import com.knwedu.util.AlertDialogCallBack;
import com.knwedu.util.Util;

import java.util.ArrayList;

/**
 * Created by knowalluser on 16-04-2018.
 */

public class ActivityNotificationCenter extends AppCompatActivity {

    private Context mContext;
    private ListView lv_notifications;

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
        setContentView(R.layout.activity_notification_center);
        mContext = ActivityNotificationCenter.this;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Notification Center");

        lv_notifications = (ListView) findViewById(R.id.lv_notifications);

        getNotificationsFromDB();
    }

    private void getNotificationsFromDB() {

        final ArrayList<Notification> notifications = new NotificationDB().getOfflineNotifications(mContext);
        if (notifications != null && notifications.size() > 0) {
            NotificationListAdapter notificationListAdapter = new NotificationListAdapter(mContext, notifications);
            lv_notifications.setAdapter(notificationListAdapter);
            lv_notifications.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent intent = new Intent(mContext, ActivityNotificationDetails.class);
                    intent.putExtra("notification", notifications.get(i));
                    startActivity(intent);
                }
            });
        } else {
            Util.showCallBackMessageWithOkCallback(mContext, "No Notification present at the moment.", new AlertDialogCallBack() {
                @Override
                public void onSubmit() {
                    onBackPressed();
                }

                @Override
                public void onCancel() {

                }
            });
        }
    }
}
