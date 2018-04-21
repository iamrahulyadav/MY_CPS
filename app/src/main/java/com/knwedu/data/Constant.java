package com.knwedu.data;

import android.content.Context;

import com.knwedu.calcuttapublicschool.R;
import com.knwedu.util.Util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Random;

@SuppressWarnings("ResourceType")
public class Constant {

    private static Random r = new Random();

    public static List<String> getParentCatgeory(Context ctx) {
        List<String> items = new ArrayList<>();
       /* String name_arr[] = Util.fetchMenuItems(ctx).getCategories();
        for (int i = 0; i < name_arr.length; i++) {
            items.add(name_arr[i]);
        }*/
        items = Util.fetchMenuItems(ctx).getCategories();
        return items;
    }

    public static List<String> getTeacherCatgeory(Context ctx) {
        List<String> items = new ArrayList<>();
       /* String name_arr[] = ctx.getResources().getStringArray(R.array.teacher_category);
        for (int i = 0; i < name_arr.length; i++) {
            items.add(name_arr[i]);
        }*/
        items = Util.fetchMenuItems(ctx).getCategories();
        return items;
    }

    public static List<String> getStudentCatgeory(Context ctx) {
        List<String> items = new ArrayList<>();
       /* String name_arr[] = ctx.getResources().getStringArray(R.array.student_category);
        for (int i = 0; i < name_arr.length; i++) {
            items.add(name_arr[i]);
        }*/
        items = Util.fetchMenuItems(ctx).getCategories();
        return items;
    }


    public static String formatTime(long time) {
        // income time
        Calendar date = Calendar.getInstance();
        date.setTimeInMillis(time);

        // current time
        Calendar curDate = Calendar.getInstance();
        curDate.setTimeInMillis(System.currentTimeMillis());

        SimpleDateFormat dateFormat = null;
        if (date.get(Calendar.YEAR) == curDate.get(Calendar.YEAR)) {
            if (date.get(Calendar.DAY_OF_YEAR) == curDate.get(Calendar.DAY_OF_YEAR)) {
                dateFormat = new SimpleDateFormat("h:mm a", Locale.US);
            } else {
                dateFormat = new SimpleDateFormat("MMM d", Locale.US);
            }
        } else {
            dateFormat = new SimpleDateFormat("MMM yyyy", Locale.US);
        }
        return dateFormat.format(time);
    }

    public static String getRandomDate(Context ctx) {
        String date_arr[] = ctx.getResources().getStringArray(R.array.general_date);
        return date_arr[getRandomIndex(0, date_arr.length - 1)];
    }

    private static int getRandomIndex(int min, int max) {
        return r.nextInt(max - min) + min;
    }

}
