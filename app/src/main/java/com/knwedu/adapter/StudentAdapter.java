package com.knwedu.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.knwedu.calcuttapublicschool.R;
import com.knwedu.model.Student;

import java.util.ArrayList;

/**
 * Created by knowalladmin on 30/11/17.
 */

public class StudentAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<Student> students = new ArrayList<>();

    public StudentAdapter(Context mContext, ArrayList<Student> students) {
        Log.d("ListAdapter", "Size: " + students.size());
        System.out.println("ListAdapter Size: " + students.size());
        this.mContext = mContext;
        this.students = students;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return students.size();
    }

    @Override
    public Object getItem(int position) {
        return students.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View hView = convertView;
        if (convertView == null) {
            hView = mInflater.inflate(R.layout.listview_item_student, null);
            StudentAdapter.ViewHolder holder = new StudentAdapter.ViewHolder();
            holder.studentName = (TextView) hView.findViewById(R.id.tv_subjectName);
            holder.studentClass = (TextView) hView.findViewById(R.id.tv_class);
            holder.section = (TextView) hView.findViewById(R.id.tv_section);
            holder.studentMobile=(TextView) hView.findViewById(R.id.tv_studentPhone);
            hView.setTag(holder);
        }

        StudentAdapter.ViewHolder holder = (StudentAdapter.ViewHolder) hView.getTag();
        holder.studentName.setText(students.get(position).studentName);
        holder.studentClass.setText(students.get(position).studentClass);
        holder.section.setText(students.get(position).studentSection);
        holder.studentMobile.setText(students.get(position).studetMobile);
        Log.e("Student",""+ students.get(position).studentName);
        return hView;
    }

    class ViewHolder {
        TextView studentName;
        TextView studentMobile;
        TextView studentClass;
        TextView section;


    }
}

