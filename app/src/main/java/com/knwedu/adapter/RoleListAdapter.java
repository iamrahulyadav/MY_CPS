package com.knwedu.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.knwedu.calcuttapublicschool.R;
import com.knwedu.model.Role;
import com.knwedu.util.Util;

import java.util.ArrayList;

/**
 * Created by knowalluser on 29-03-2018.
 */

public class RoleListAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<Role> roles = new ArrayList<>();


    public RoleListAdapter(Context mContext, ArrayList<Role> roles) {
        this.mContext = mContext;
        mInflater = LayoutInflater.from(mContext);
        this.roles = roles;
        fetchRoles(mContext);
    }

    private void fetchRoles(Context mContext) {
        roles = Util.fetchRoles(mContext).getRoles();
    }

    @Override
    public int getCount() {
        return roles.size();
    }

    @Override
    public Object getItem(int position) {
        return roles.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View hView = convertView;
        if (convertView == null) {
            hView = mInflater.inflate(R.layout.item_user_role, null);
            ViewHolder holder = new ViewHolder();
            holder.tv_roleName = (TextView) hView.findViewById(R.id.tv_roleName);
            holder.tv_role_description = (TextView) hView.findViewById(R.id.tv_role_description);
            holder.iv_profile = (ImageView) hView.findViewById(R.id.iv_profile);
            hView.setTag(holder);
        }

        ViewHolder holder = (ViewHolder) hView.getTag();
        holder.tv_roleName.setText("Enter as " + roles.get(position).getRole() + " in " + roles.get(position).getOrganization());
        if (roles.get(position).getRole().equalsIgnoreCase("parent")) {
            holder.iv_profile.setImageResource(R.drawable.ic_parent);
            holder.tv_role_description.setText("Tap here to enter as a Parent in " + roles.get(position).getOrganization());
        } else if (roles.get(position).getRole().equalsIgnoreCase("teacher")) {
            holder.iv_profile.setImageResource(R.drawable.ic_teacher);
            holder.tv_role_description.setText("Tap here to enter as a Teacher in " + roles.get(position).getOrganization());
        } else if (roles.get(position).getRole().equalsIgnoreCase("student")) {
            holder.iv_profile.setImageResource(R.drawable.ic_student);
            holder.tv_role_description.setText("Tap here to enter as a Student in " + roles.get(position).getOrganization());
        }

        return hView;
    }


    class ViewHolder {
        TextView tv_roleName, tv_role_description;
        ImageView iv_profile;

    }
}

