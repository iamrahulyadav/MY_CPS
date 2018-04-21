package com.knwedu.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.knwedu.calcuttapublicschool.R;
import com.knwedu.model.Subject;

import java.util.ArrayList;

/**
 * Created by knowalladmin on 27/11/17.
 */

public class SubjectAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<Subject>subjects = new ArrayList<>();

    public SubjectAdapter(Context mContext, ArrayList<Subject> subjects) {
        Log.d("ListAdapter", "Size: " + subjects.size());
        System.out.println("ListAdapter Size: " + subjects.size());
        this.mContext = mContext;
        this.subjects = subjects;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return subjects.size();
    }

    @Override
    public Object getItem(int position) {
        return subjects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View hView = convertView;
        if (convertView == null) {
            hView = mInflater.inflate(R.layout.listview_item_subject, null);
            ViewHolder holder = new ViewHolder();
            holder.subjectName = (TextView) hView.findViewById(R.id.tv_subjectName);
            holder.classGrade = (TextView) hView.findViewById(R.id.tv_class);
            holder.section = (TextView) hView.findViewById(R.id.tv_section);
            hView.setTag(holder);
        }

        ViewHolder holder = (ViewHolder) hView.getTag();
        holder.subjectName.setText(subjects.get(position).subjectName);
        holder.classGrade.setText(subjects.get(position).classGrade);
        holder.section.setText(subjects.get(position).sectionName);
        Log.e("Subject",""+subjects.get(position).subjectName);
        return hView;
    }

    class ViewHolder {
        TextView subjectName;
        TextView classGrade;
        TextView section;


    }
}
