package com.knwedu.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.knwedu.calcuttapublicschool.R;
import com.knwedu.model.Bulletin;

import java.util.ArrayList;

/**
 * Created by knowalluser on 29-03-2018.
 */

public class AllBulletinsListAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<Bulletin> bulletins = new ArrayList<>();


    public AllBulletinsListAdapter(Context mContext, ArrayList<Bulletin> bulletins) {
        this.mContext = mContext;
        mInflater = LayoutInflater.from(mContext);
        this.bulletins = bulletins;
    }

    @Override
    public int getCount() {
        return bulletins.size();
    }

    @Override
    public Object getItem(int position) {
        return bulletins.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View hView = convertView;
        if (convertView == null) {
            hView = mInflater.inflate(R.layout.news_items, null);
            ViewHolder holder = new ViewHolder();
            holder.title = (TextView) hView.findViewById(R.id.title_txt);
            holder.description = (TextView) hView.findViewById(R.id.description_txt);
            holder.date_txt = (TextView) hView.findViewById(R.id.date_text);
            hView.setTag(holder);
        }

        ViewHolder holder = (ViewHolder) hView.getTag();
        /*SchoolAppUtils.setFont(mContext, holder.description);
        SchoolAppUtils.setFont(mContext, holder.title);*/
        if (bulletins.get(position).getClassName() != null) {
            holder.title.setText(bulletins.get(position).getClassName());
            holder.description.setText(bulletins.get(position).getTitle());
        } else {
            holder.title.setText(bulletins.get(position).getTitle());
            holder.description.setText(bulletins.get(position).getDescription());
        }
        holder.date_txt.setText(bulletins.get(position).getCreated_date());
        return hView;
    }


    class ViewHolder {
        TextView title, description, date_txt;

    }
}

