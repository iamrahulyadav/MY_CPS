package com.knwedu.firebasepush;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.knwedu.calcuttapublicschool.R;
import com.knwedu.db.DBConstants;
import com.knwedu.db.NotificationDB;
import com.knwedu.ourschool.SplashActivity;
import com.knwedu.ourschool.utils.Constants;
import com.knwedu.ourschool.utils.SchoolAppUtils;

import org.json.JSONException;
import org.json.JSONObject;

import me.leolin.shortcutbadger.ShortcutBadger;

/**
 *
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService implements DBConstants {

    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();
    public static int notificationCount;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.e(TAG, "From: " + remoteMessage.getFrom());
        if (remoteMessage == null)
            return;

        Log.e(TAG, "Data Payload: " + remoteMessage.getData().toString());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.e(TAG, "Data Payload: " + remoteMessage.getData().toString());

            try {
                JSONObject json = new JSONObject(remoteMessage.getData().toString());
                Log.e("JSON", "JSON: " + json);
                handleDataMessage(json);
            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }
        }
    }

    private void handleDataMessage(JSONObject json) {
        Log.e(TAG, "push json: " + json.toString());

        try {
            JSONObject data = json.getJSONObject("data");

            String title = data.getString("title");
            String message = data.getString("message");
            String menu = data.getString("menu");
            String timestamp = data.getString("timestamp");
            String organizationId = data.getString("organization_id");
            String userTypeId = data.getString("user_type_id");
            String module_name = data.getString("module_name");

            Log.e(TAG, "title: " + title);
            Log.e(TAG, "message: " + message);
            Log.e(TAG, "menu: " + menu);

            generateNotificationnew(this, message.trim(), title.trim(), menu, timestamp.trim(),
                    organizationId.trim(), userTypeId.trim(), module_name.trim());
        } catch (JSONException e) {
            Log.e(TAG, "Json Exception: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
    }


    private static void generateNotificationnew(Context context, String message, String title, String menu,
                                                String timestamp, String organizationId, String userTypeId, String module_name) {
        Intent notificationIntent = null;
        //Remove Badge
        ShortcutBadger.removeCount(context);
        Log.e("Received_Val", Integer.parseInt(menu) + "");

        if (SchoolAppUtils.GetSharedBoolParameter(context, Constants.UISSIGNIN) == true) {
            notificationIntent = new Intent(context, SplashActivity.class);
        } else if (SchoolAppUtils.GetSharedBoolParameter(context, Constants.ISPARENTSIGNIN) == true) {
            notificationIntent = new Intent(context, SplashActivity.class);
        } else if (SchoolAppUtils.GetSharedBoolParameter(context, Constants.ISSIGNIN) == true) {
            notificationIntent = new Intent(context, SplashActivity.class);
        } else {
            notificationIntent = new Intent(context, SplashActivity.class);
        }
        notificationIntent.putExtra("menu_val", Integer.parseInt(menu));
        try {

            int icon = R.drawable.app_icon;
            long when = System.currentTimeMillis();
            Notification notification = new Notification(R.drawable.app_icon, message, when);
            notification.color = 0x008000;

            NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            RemoteViews contentView = new RemoteViews(context.getPackageName(), R.layout.custom_notification);
            contentView.setImageViewResource(R.id.image, R.drawable.app_icon);
            contentView.setTextViewText(R.id.title, title);
            contentView.setTextViewText(R.id.text, message);
            notification.contentView = contentView;

            // Intent notificationIntent = new Intent(this, MainActivity.class);
            PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            notification.contentIntent = contentIntent;

            notification.flags |= Notification.FLAG_AUTO_CANCEL; // Do not clear the notification
            notification.defaults |= Notification.DEFAULT_LIGHTS; // LED
            notification.defaults |= Notification.DEFAULT_VIBRATE; // Vibration
            notification.defaults |= Notification.DEFAULT_SOUND; // Sound

            notification.number = notificationCount++;

            int notification_id = (int) System.currentTimeMillis();
            mNotificationManager.notify(notification_id, notification);

            ContentValues cv = new ContentValues();
            cv.put(NOTIFICATION_TITLE, title);
            cv.put(NOTIFICATION_MESSAGE, message);
            if (userTypeId.equalsIgnoreCase("5")) {
                cv.put(ROLE, "Parent");
            } else if (userTypeId.equalsIgnoreCase("3")) {
                cv.put(ROLE, "Teacher");
            } else if (userTypeId.equalsIgnoreCase("4")) {
                cv.put(ROLE, "Student");
            }
            cv.put(MODULE, "" + module_name);
            cv.put(ORGANIZATIONID, "" + organizationId);
            cv.put(TIMESTAMP, "" + timestamp);
            new NotificationDB().saveHistoryData(context, cv);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
