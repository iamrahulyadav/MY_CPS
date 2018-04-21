package com.knwedu.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.knwedu.calcuttapublicschool.R;
import com.knwedu.model.Notification;

import java.util.ArrayList;

/**
 * Created by knowalluser on 29-03-2018.
 */

public class NotificationListAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<Notification> notifications = new ArrayList<>();


    public NotificationListAdapter(Context mContext, ArrayList<Notification> notifications) {
        this.mContext = mContext;
        mInflater = LayoutInflater.from(mContext);
        this.notifications = notifications;
    }


    @Override
    public int getCount() {
        return notifications.size();
    }

    @Override
    public Object getItem(int position) {
        return notifications.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View hView = convertView;
        if (convertView == null) {
            hView = mInflater.inflate(R.layout.listview_item_notification, null);
            ViewHolder holder = new ViewHolder();
            holder.tv_title = (TextView) hView.findViewById(R.id.tv_title);
            holder.tv_message = (TextView) hView.findViewById(R.id.tv_message);
            // holder.iv_profile = (ImageView) hView.findViewById(R.id.iv_profile);
            hView.setTag(holder);
        }

        ViewHolder holder = (ViewHolder) hView.getTag();
        holder.tv_title.setText("" + notifications.get(position).getTitle());
        holder.tv_message.setText("" + notifications.get(position).getMessage());

        return hView;
    }

    class ViewHolder {
        TextView tv_title, tv_message;
        //ImageView iv_profile;

    }
}

