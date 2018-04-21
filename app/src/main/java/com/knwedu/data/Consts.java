package com.knwedu.data;

import android.content.Context;

import com.knwedu.calcuttapublicschool.R;
import com.knwedu.constant.StaticConstant;
import com.knwedu.model.AllMenuItemsModel;
import com.knwedu.model.MenuCategory;
import com.knwedu.model.MenuItem;
import com.knwedu.util.Util;

import java.util.ArrayList;

/**
 * Created by knowalladmin on 14/01/18.
 */

public class Consts {

    // Get Student Menu
    public static ArrayList<MenuItem> getStudentMenu(Context ctx, String category) {

        AllMenuItemsModel allMenuItemsModel = Util.fetchMenuItems(ctx);
        MenuCategory selectedCategory = new MenuCategory();

        if (allMenuItemsModel != null) {

            ArrayList<MenuCategory> menuCategories = allMenuItemsModel.getMenuCategories();
            for (MenuCategory menuCategory : menuCategories) {
                if (menuCategory.getCategoryName().equalsIgnoreCase(category)) {
                    selectedCategory = menuCategory;
                }
            }
            return selectedCategory.getSubCategories();
        } else {
            return new ArrayList<>();
        }

    }


    /**
     * Parent- Category Student
     */
    public static ArrayList<MenuItem> getParentStudentMenu() {

        ArrayList<MenuItem> menuItems = new ArrayList<>();

       /* MenuItem menuItem_header= new MenuItem();
        menuItem_header.setName("Student");
        menuItem_header.setDescription("Student");*/

        MenuItem menuItem_first = new MenuItem();
        menuItem_first.setName("Child Profile");
        menuItem_first.setDescription("Your Child's Personal Details Are Here");
        menuItem_first.setImage(R.drawable.ic_profile_icon);
        menuItem_first.setRole(StaticConstant.ROLE_PARENT);

        MenuItem menuItem_second = new MenuItem();
        menuItem_second.setName("Class Fellow");
        menuItem_second.setDescription("Your Child's Classmates Are Here");
        menuItem_second.setImage(R.drawable.ic_school_fellow);
        menuItem_second.setRole(StaticConstant.ROLE_PARENT);

        MenuItem menuItem_third = new MenuItem();
        menuItem_third.setName("Hostel");
        menuItem_third.setDescription("Find More About Hostel");
        menuItem_third.setImage(R.drawable.ic_hostel);
        menuItem_third.setRole(StaticConstant.ROLE_PARENT);

        MenuItem menuItem_fourth = new MenuItem();
        menuItem_fourth.setName("Behaviour");
        menuItem_fourth.setDescription("Child's Behaviour Is Recorded Here");
        menuItem_fourth.setImage(R.drawable.ic_behaviour);
        menuItem_fourth.setRole(StaticConstant.ROLE_PARENT);

        MenuItem menuItem_fifth = new MenuItem();
        menuItem_fifth.setName("Organization");
        menuItem_fifth.setDescription("Keep Yourself Updated Of Recent Activities");
        menuItem_fifth.setImage(R.drawable.ic_organization);
        menuItem_fifth.setRole(StaticConstant.ROLE_PARENT);

        /*MenuItem menuItem_sixth= new MenuItem();
        menuItem_sixth.setName("Child Profile");
        menuItem_sixth.setDescription("Child Profile");*/

        //  menuItems.add(menuItem_header);
        menuItems.add(menuItem_first);
        menuItems.add(menuItem_second);
        menuItems.add(menuItem_fourth);
        menuItems.add(menuItem_third);
        menuItems.add(menuItem_fifth);
        // menuItems.add(menuItem_sixth);

        return menuItems;
    }

    /**
     * Parent- Category Academics
     */
    public static ArrayList<MenuItem> getAcedemicsMenu() {

        ArrayList<MenuItem> menuItems = new ArrayList<>();

       /* MenuItem menuItem_header = new MenuItem();
        menuItem_header.setName("Academics");
        menuItem_header.setDescription("Academics");*/

        MenuItem menuItem_first = new MenuItem();
        menuItem_first.setName("Subjects");
        menuItem_first.setDescription("List Of Subjects Your Child Is Studying");
        menuItem_first.setImage(R.drawable.ic_subjects);
        menuItem_first.setRole(StaticConstant.ROLE_PARENT);

        MenuItem menuItem_second = new MenuItem();
        menuItem_second.setName("Time Table");
        menuItem_second.setDescription("Your Child's Daily Routine");
        menuItem_second.setImage(R.drawable.ic_timetable);
        menuItem_second.setRole(StaticConstant.ROLE_PARENT);

        MenuItem menuItem_third = new MenuItem();
        menuItem_third.setName("Exams");
        menuItem_third.setDescription("Exam Schedule, Report Cards Are Here");
        menuItem_third.setImage(R.drawable.ic_exams);
        menuItem_third.setRole(StaticConstant.ROLE_PARENT);

        MenuItem menuItem_fourth = new MenuItem();
        menuItem_fourth.setName("Special Class");
        menuItem_fourth.setDescription("Get To Know Of Special Class");
        menuItem_fourth.setImage(R.drawable.ic_special_class);
        menuItem_fourth.setRole(StaticConstant.ROLE_PARENT);

        MenuItem menuItem_fifth = new MenuItem();
        menuItem_fifth.setName("Syllabus");
        menuItem_fifth.setDescription("Subject Syllabus Can Be Found Here");
        menuItem_fifth.setImage(R.drawable.ic_syllabus);
        menuItem_fifth.setRole(StaticConstant.ROLE_PARENT);

        MenuItem menuItem_sixth = new MenuItem();
        menuItem_sixth.setName("Lesson Plan");
        menuItem_sixth.setDescription("Follow Updated Lesson Plan");
        menuItem_sixth.setImage(R.drawable.ic_lesson_plan);
        menuItem_sixth.setRole(StaticConstant.ROLE_PARENT);

        // menuItems.add(menuItem_header);
        menuItems.add(menuItem_first);
        menuItems.add(menuItem_second);
        menuItems.add(menuItem_third);
        menuItems.add(menuItem_fourth);
        menuItems.add(menuItem_fifth);
        menuItems.add(menuItem_sixth);

        return menuItems;
    }

    /**
     * Parent- Category Daily Activity
     */
    public static ArrayList<MenuItem> getDailyActivityMenu() {

        ArrayList<MenuItem> menuItems = new ArrayList<>();

       /* MenuItem menuItem_header = new MenuItem();
        menuItem_header.setName("Daily Activity");
        menuItem_header.setDescription("Daily Activity");*/

        MenuItem menuItem_first = new MenuItem();
        menuItem_first.setName("Attendance Info");
        menuItem_first.setDescription("Keep Track Of Your Child's Attendance");
        menuItem_first.setImage(R.drawable.ic_attendance);
        menuItem_first.setRole(StaticConstant.ROLE_PARENT);

        MenuItem menuItem_second = new MenuItem();
        menuItem_second.setName("Class Announcement");
        menuItem_second.setDescription("Teacher Announcements Can Be Found here");
        menuItem_second.setImage(R.drawable.ic_announcement);
        menuItem_second.setRole(StaticConstant.ROLE_PARENT);

        MenuItem menuItem_third = new MenuItem();
        menuItem_third.setName("Assignment");
        menuItem_third.setDescription("Daily Assignments Are Listed and Graded Here");
        menuItem_third.setImage(R.drawable.ic_assignment);
        menuItem_third.setRole(StaticConstant.ROLE_PARENT);

        MenuItem menuItem_fourth = new MenuItem();
        menuItem_fourth.setName("Test");
        menuItem_fourth.setDescription("Tests Are Listed and Graded Here");
        menuItem_fourth.setImage(R.drawable.ic_test);
        menuItem_fourth.setRole(StaticConstant.ROLE_PARENT);

        MenuItem menuItem_fifth = new MenuItem();
        menuItem_fifth.setName("Classwork");
        menuItem_fifth.setDescription("Find Out What Has Been Done In Class");
        menuItem_fifth.setImage(R.drawable.ic_class_work);
        menuItem_fifth.setRole(StaticConstant.ROLE_PARENT);

        MenuItem menuItem_sixth = new MenuItem();
        menuItem_sixth.setName("Bus Tracking");
        menuItem_sixth.setDescription("Follow The Bus");
        menuItem_sixth.setImage(R.drawable.ic_bus);
        menuItem_sixth.setRole(StaticConstant.ROLE_PARENT);

        MenuItem menuItem_seventh = new MenuItem();
        menuItem_seventh.setName("Activity");
        menuItem_seventh.setDescription("Activities Are Listed And Graded Here");
        menuItem_seventh.setImage(R.drawable.ic_activity);
        menuItem_seventh.setRole(StaticConstant.ROLE_PARENT);

        MenuItem menuItem_eighth = new MenuItem();
        menuItem_eighth.setName("Library");
        menuItem_eighth.setDescription("Access Library Resources From Here");
        menuItem_eighth.setImage(R.drawable.ic_library);
        menuItem_eighth.setRole(StaticConstant.ROLE_PARENT);

        //menuItems.add(menuItem_header);
        menuItems.add(menuItem_first);
        menuItems.add(menuItem_second);
        menuItems.add(menuItem_third);
        menuItems.add(menuItem_fourth);
        menuItems.add(menuItem_fifth);
        menuItems.add(menuItem_sixth);
        menuItems.add(menuItem_seventh);
        menuItems.add(menuItem_eighth);

        return menuItems;
    }

    /**
     * Parent- Category Communication
     */
    public static ArrayList<MenuItem> getCommunicationMenu() {

        ArrayList<MenuItem> menuItems = new ArrayList<>();

        /*MenuItem menuItem_header = new MenuItem();
        menuItem_header.setName("Communication");
        menuItem_header.setDescription("Communication");*/

        MenuItem menuItem_first = new MenuItem();
        menuItem_first.setName("Bulletin");
        menuItem_first.setDescription("Receive Insitution Class & Personal Notices");
        menuItem_first.setImage(R.drawable.ic_bulletins);
        menuItem_first.setRole(StaticConstant.ROLE_PARENT);

        MenuItem menuItem_second = new MenuItem();
        menuItem_second.setName("Self Request");
        menuItem_second.setDescription("Send Request To School");
        menuItem_second.setImage(R.drawable.ic_self_reuest);
        menuItem_second.setRole(StaticConstant.ROLE_PARENT);

        MenuItem menuItem_third = new MenuItem();
        menuItem_third.setName("School Request");
        menuItem_third.setDescription("Attend To Request From School");
        menuItem_third.setImage(R.drawable.ic_school_reuest);
        menuItem_third.setRole(StaticConstant.ROLE_PARENT);

        MenuItem menuItem_fourth = new MenuItem();
        menuItem_fourth.setName("Event Calender");
        menuItem_fourth.setDescription("Find Out About All Annual Events");
        menuItem_fourth.setImage(R.drawable.ic_event_calendar);
        menuItem_fourth.setRole(StaticConstant.ROLE_PARENT);

        /*MenuItem menuItem_fifth = new MenuItem();
        menuItem_fifth.setName("Feedback");
        menuItem_fifth.setDescription("Feedback");*/

        MenuItem menuItem_sixth = new MenuItem();
        menuItem_sixth.setName("Daily Diary");
        menuItem_sixth.setDescription("Access Daily Dairy With Option to Comment");
        menuItem_sixth.setImage(R.drawable.ic_daily_diary);
        menuItem_sixth.setRole(StaticConstant.ROLE_PARENT);

        MenuItem menuItem_seventh = new MenuItem();
        menuItem_seventh.setName("Feedback");
        menuItem_seventh.setDescription("Provide Your Valuable Feedback");
        menuItem_seventh.setImage(R.drawable.ic_feedback);
        menuItem_seventh.setRole(StaticConstant.ROLE_PARENT);

        MenuItem menuItem_eighth = new MenuItem();
        menuItem_eighth.setName("Blog");
        menuItem_eighth.setDescription("Join Institution Blog With Your Views");
        menuItem_eighth.setImage(R.drawable.ic_blog);
        menuItem_eighth.setRole(StaticConstant.ROLE_PARENT);

        MenuItem menuItem_ninth = new MenuItem();
        menuItem_ninth.setName("Directory");
        menuItem_ninth.setDescription("Access Institution Directory");
        menuItem_ninth.setImage(R.drawable.ic_directory);
        menuItem_ninth.setRole(StaticConstant.ROLE_PARENT);

        //menuItems.add(menuItem_header);
        menuItems.add(menuItem_first);
        menuItems.add(menuItem_second);
        menuItems.add(menuItem_third);
        menuItems.add(menuItem_fourth);
        //menuItems.add(menuItem_fifth);
        menuItems.add(menuItem_sixth);
        menuItems.add(menuItem_seventh);
        menuItems.add(menuItem_eighth);
        menuItems.add(menuItem_ninth);

        return menuItems;
    }

    /**
     * Parent- Category Profile
     */
    public static ArrayList<MenuItem> getParentMenu(Context ctx, String category) {

        // ArrayList<MenuItem> menuItems = new ArrayList<>();

       /* MenuItem menuItem_header = new MenuItem();
        menuItem_header.setName("Profile");
        menuItem_header.setDescription("Profile");*/

        AllMenuItemsModel allMenuItemsModel = Util.fetchMenuItems(ctx);
        MenuCategory selectedCategory = new MenuCategory();

        if (allMenuItemsModel != null) {

            ArrayList<MenuCategory> menuCategories = allMenuItemsModel.getMenuCategories();
            for (MenuCategory menuCategory : menuCategories) {
                //room contains an element of rooms
                if (menuCategory.getCategoryName().equalsIgnoreCase(category)) {
                    selectedCategory = menuCategory;
                }
            }
            return selectedCategory.getSubCategories();


        } else {
            return new ArrayList<>();
        }

      /*  MenuItem menuItem_first = new MenuItem();
        menuItem_first.setName("My Profile");
        menuItem_first.setDescription("Your Personal Details Are Here");
        menuItem_first.setImage(R.drawable.ic_profile_icon);
        menuItem_first.setRole(StaticConstant.ROLE_PARENT);

        MenuItem menuItem_second = new MenuItem();
        menuItem_second.setName("Help");
        menuItem_second.setDescription("Ping us for Technical Support");
        menuItem_second.setImage(R.drawable.ic_help);
        menuItem_second.setRole(StaticConstant.ROLE_PARENT);

        MenuItem menuItem_third = new MenuItem();
        menuItem_third.setName("Fees");
        menuItem_third.setDescription("Pay Fees & Receive Invoices  ");
        menuItem_third.setImage(R.drawable.ic_rupee);
        menuItem_third.setRole(StaticConstant.ROLE_PARENT);

        MenuItem menuItem_fourth = new MenuItem();
        menuItem_fourth.setName("Student Access");
        menuItem_fourth.setDescription("Allow Your Child To Use The App");
        menuItem_fourth.setImage(R.drawable.ic_access);
        menuItem_fourth.setRole(StaticConstant.ROLE_PARENT);

        MenuItem menuItem_fifth = new MenuItem();
        menuItem_fifth.setName("Web Access");
        menuItem_fifth.setDescription("Access The App On Your Computer");
        menuItem_fifth.setImage(R.drawable.ic_access);
        menuItem_fifth.setRole(StaticConstant.ROLE_PARENT);

        MenuItem menuItem_sixth = new MenuItem();
        menuItem_sixth.setName("Tutorial");
        menuItem_sixth.setDescription("Learn How To Use The App");
        menuItem_sixth.setImage(R.drawable.ic_tutorial);
        menuItem_sixth.setRole(StaticConstant.ROLE_PARENT);

        // menuItems.add(menuItem_header);
        menuItems.add(menuItem_first);
        menuItems.add(menuItem_third);
        menuItems.add(menuItem_second);

        menuItems.add(menuItem_fourth);
        menuItems.add(menuItem_fifth);
        menuItems.add(menuItem_sixth);
*/
    }

    //==============TEACHER ================

    /**
     * Teacher- Category Student
     */
    public static ArrayList<MenuItem> getStudentTeacherMenu() {

        ArrayList<MenuItem> menuItems = new ArrayList<>();

        MenuItem menuItem_first = new MenuItem();
        menuItem_first.setName("Class Student");
        menuItem_first.setDescription("List Of Students You Are Teaching");
        menuItem_first.setImage(R.drawable.ic_student);
        menuItem_first.setRole(StaticConstant.ROLE_TEACHER);

        MenuItem menuItem_second = new MenuItem();
        menuItem_second.setName("Behaviour");
        menuItem_second.setDescription("Mark Behavior For Your Students");
        menuItem_second.setImage(R.drawable.ic_behaviour);
        menuItem_second.setRole(StaticConstant.ROLE_TEACHER);

        MenuItem menuItem_third = new MenuItem();
        menuItem_third.setName("Organization");
        menuItem_third.setDescription("Update Activities");
        menuItem_third.setImage(R.drawable.ic_organization);
        menuItem_third.setRole(StaticConstant.ROLE_TEACHER);

        MenuItem menuItem_fourth = new MenuItem();
        menuItem_fourth.setName("Library");
        menuItem_fourth.setDescription("Access Library Resources");
        menuItem_fourth.setImage(R.drawable.ic_library);
        menuItem_fourth.setRole(StaticConstant.ROLE_TEACHER);

      /*  MenuItem menuItem_fifth = new MenuItem();
        menuItem_fifth.setName("Special Class");
        menuItem_fifth.setDescription("Special Class");

        MenuItem menuItem_sixth = new MenuItem();
        menuItem_sixth.setName("Syllabus");
        menuItem_sixth.setDescription("Syllabus");*/

        //  menuItems.add(menuItem_header);
        menuItems.add(menuItem_first);
        menuItems.add(menuItem_second);
        menuItems.add(menuItem_third);
        menuItems.add(menuItem_fourth);
        //  menuItems.add(menuItem_fifth);
        //  menuItems.add(menuItem_sixth);

        return menuItems;
    }

    /**
     * Teacher- Category Academics
     */
    public static ArrayList<MenuItem> getAcedemicsTeacherMenu() {

        ArrayList<MenuItem> menuItems = new ArrayList<>();

        /*MenuItem menuItem_header = new MenuItem();
        menuItem_header.setName("Academics");
        menuItem_header.setDescription("Academics");*/

        MenuItem menuItem_first = new MenuItem();
        menuItem_first.setName("Subjects");
        menuItem_first.setDescription("List Of Subjects You Are Teaching");
        menuItem_first.setImage(R.drawable.ic_subjects);
        menuItem_first.setRole(StaticConstant.ROLE_TEACHER);

        MenuItem menuItem_second = new MenuItem();
        menuItem_second.setName("Time Table");
        menuItem_second.setDescription("Your Daily Routine Is Here");
        menuItem_second.setImage(R.drawable.ic_timetable);
        menuItem_second.setRole(StaticConstant.ROLE_TEACHER);

        MenuItem menuItem_third = new MenuItem();
        menuItem_third.setName("Exams");
        menuItem_third.setDescription("Exam Schedule");
        menuItem_third.setImage(R.drawable.ic_exams);
        menuItem_third.setRole(StaticConstant.ROLE_TEACHER);

        MenuItem menuItem_fourth = new MenuItem();
        menuItem_fourth.setName("Special Class");
        menuItem_fourth.setDescription("Get To Know Of Special Class");
        menuItem_fourth.setImage(R.drawable.ic_special_class);
        menuItem_fourth.setRole(StaticConstant.ROLE_TEACHER);

        MenuItem menuItem_fifth = new MenuItem();
        menuItem_fifth.setName("Syllabus");
        menuItem_fifth.setDescription("Follow Subject Syllabus");
        menuItem_fifth.setImage(R.drawable.ic_syllabus);
        menuItem_fifth.setRole(StaticConstant.ROLE_TEACHER);

        MenuItem menuItem_sixth = new MenuItem();
        menuItem_sixth.setName("Lesson Plan");
        menuItem_sixth.setDescription("Update Lesson Plan ");
        menuItem_sixth.setImage(R.drawable.ic_lesson_plan);
        menuItem_sixth.setRole(StaticConstant.ROLE_TEACHER);

        //  menuItems.add(menuItem_header);
        menuItems.add(menuItem_first);
        menuItems.add(menuItem_second);
        menuItems.add(menuItem_third);
        menuItems.add(menuItem_fourth);
        menuItems.add(menuItem_fifth);
        menuItems.add(menuItem_sixth);

        return menuItems;
    }

    /**
     * Teacher- Category Daily Activity
     */
    public static ArrayList<MenuItem> getDailyActivityTeacherMenu() {

        ArrayList<MenuItem> menuItems = new ArrayList<>();

        /*MenuItem menuItem_header = new MenuItem();
        menuItem_header.setName("Daily Activity");
        menuItem_header.setDescription("Daily Activity");*/

        MenuItem menuItem_first = new MenuItem();
        menuItem_first.setName("Attendance Info");
        menuItem_first.setDescription("Mark & Follow Student Attendance");
        menuItem_first.setImage(R.drawable.ic_attendance);
        menuItem_first.setRole(StaticConstant.ROLE_TEACHER);

        MenuItem menuItem_second = new MenuItem();
        menuItem_second.setName("Class Announcement");
        menuItem_second.setDescription("Create Annoucemnts For Your Students");
        menuItem_second.setImage(R.drawable.ic_announcement);
        menuItem_second.setRole(StaticConstant.ROLE_TEACHER);

        MenuItem menuItem_third = new MenuItem();
        menuItem_third.setName("Assignment");
        menuItem_third.setDescription("Create Mark Publish Assignment");
        menuItem_third.setImage(R.drawable.ic_assignment);
        menuItem_third.setRole(StaticConstant.ROLE_TEACHER);

        MenuItem menuItem_fourth = new MenuItem();
        menuItem_fourth.setName("Test");
        menuItem_fourth.setDescription("Create Mark Publish Test");
        menuItem_fourth.setImage(R.drawable.ic_test);
        menuItem_fourth.setRole(StaticConstant.ROLE_TEACHER);

        MenuItem menuItem_fifth = new MenuItem();
        menuItem_fifth.setName("Classwork");
        menuItem_fifth.setDescription("Update Daily Classwork");
        menuItem_fifth.setImage(R.drawable.ic_class_work);
        menuItem_fifth.setRole(StaticConstant.ROLE_TEACHER);


        MenuItem menuItem_sixth = new MenuItem();
        menuItem_sixth.setName("Activity");
        menuItem_sixth.setDescription("Create Mark Publish Activity");
        menuItem_sixth.setImage(R.drawable.ic_activity);
        menuItem_sixth.setRole(StaticConstant.ROLE_TEACHER);

        //  menuItems.add(menuItem_header);
        menuItems.add(menuItem_first);
        menuItems.add(menuItem_second);
        menuItems.add(menuItem_third);
        menuItems.add(menuItem_fourth);
        menuItems.add(menuItem_fifth);
        menuItems.add(menuItem_sixth);

        return menuItems;
    }

    /**
     * Teacher- Category Communication
     */
    public static ArrayList<MenuItem> getCommunicationTeacherMenu() {

        ArrayList<MenuItem> menuItems = new ArrayList<>();

       /* MenuItem menuItem_header = new MenuItem();
        menuItem_header.setName("Communication");
        menuItem_header.setDescription("Communication");*/

        MenuItem menuItem_first = new MenuItem();
        menuItem_first.setName("Bulletin");
        menuItem_first.setDescription("Receive Institution & Personal Notices");
        menuItem_first.setImage(R.drawable.ic_bulletins);
        menuItem_first.setRole(StaticConstant.ROLE_TEACHER);

        MenuItem menuItem_second = new MenuItem();
        menuItem_second.setName("Event Calender");
        menuItem_second.setDescription("Find Out About All Annual Events");
        menuItem_second.setImage(R.drawable.ic_event_calendar);
        menuItem_second.setRole(StaticConstant.ROLE_TEACHER);

        MenuItem menuItem_third = new MenuItem();
        menuItem_third.setName("Daily Diary");
        menuItem_third.setDescription("Update Daily Dairy For Courses Taught");
        menuItem_third.setImage(R.drawable.ic_daily_diary);
        menuItem_third.setRole(StaticConstant.ROLE_TEACHER);


        MenuItem menuItem_eighth = new MenuItem();
        menuItem_eighth.setName("Blog");
        menuItem_eighth.setDescription("Join Institution Blog With Your Views");
        menuItem_eighth.setImage(R.drawable.ic_blog);
        menuItem_eighth.setRole(StaticConstant.ROLE_TEACHER);

        MenuItem menuItem_ninth = new MenuItem();
        menuItem_ninth.setName("Directory");
        menuItem_ninth.setDescription("Access Institution Directory");
        menuItem_ninth.setImage(R.drawable.ic_directory);
        menuItem_ninth.setRole(StaticConstant.ROLE_TEACHER);

        //menuItems.add(menuItem_header);
        menuItems.add(menuItem_first);
        menuItems.add(menuItem_second);
        menuItems.add(menuItem_third);
        menuItems.add(menuItem_eighth);
        menuItems.add(menuItem_ninth);


        return menuItems;
    }

    /**
     * Teacher- Category Profile
     */
    public static ArrayList<MenuItem> getTeacherProfileMenu() {

        ArrayList<MenuItem> menuItems = new ArrayList<>();

      /*  MenuItem menuItem_header = new MenuItem();
        menuItem_header.setName("Profile");
        menuItem_header.setDescription("Profile");*/

        MenuItem menuItem_first = new MenuItem();
        menuItem_first.setName("My Profile");
        menuItem_first.setDescription("Your Personal Details Are Here");
        menuItem_first.setImage(R.drawable.ic_profile_icon);
        menuItem_first.setRole(StaticConstant.ROLE_TEACHER);

        MenuItem menuItem_second = new MenuItem();
        menuItem_second.setName("Help");
        menuItem_second.setDescription("Ping us for Technical Support");
        menuItem_second.setImage(R.drawable.ic_help);
        menuItem_second.setRole(StaticConstant.ROLE_TEACHER);

        MenuItem menuItem_third = new MenuItem();
        menuItem_third.setName("Web Access");
        menuItem_third.setDescription("Access The App On Your Computer");
        menuItem_third.setImage(R.drawable.ic_access);
        menuItem_third.setRole(StaticConstant.ROLE_TEACHER);

        MenuItem menuItem_fourth = new MenuItem();
        menuItem_fourth.setName("Tutorial");
        menuItem_fourth.setDescription("Learn How To Use The App");
        menuItem_fourth.setImage(R.drawable.ic_tutorial);
        menuItem_fourth.setRole(StaticConstant.ROLE_TEACHER);

         /*MenuItem menuItem_fifth= new MenuItem();
        menuItem_fifth.setName("Organization");
        menuItem_fifth.setDescription("Organization");

        MenuItem menuItem_sixth= new MenuItem();
        menuItem_sixth.setName("Child Profile");
        menuItem_sixth.setDescription("Child Profile");*/

        // menuItems.add(menuItem_header);
        menuItems.add(menuItem_first);
        menuItems.add(menuItem_second);
        menuItems.add(menuItem_third);
        menuItems.add(menuItem_fourth);
        /*menuItems.add(menuItem_fifth);
        menuItems.add(menuItem_sixth);*/


        return menuItems;
    }

    public static ArrayList<MenuItem> getTeacherMenu(Context ctx, String category) {

        AllMenuItemsModel allMenuItemsModel = Util.fetchMenuItems(ctx);
        MenuCategory selectedCategory = new MenuCategory();

        if (allMenuItemsModel != null) {

            ArrayList<MenuCategory> menuCategories = allMenuItemsModel.getMenuCategories();
            for (MenuCategory menuCategory : menuCategories) {
                //room contains an element of rooms
                if (menuCategory.getCategoryName().equalsIgnoreCase(category)) {
                    selectedCategory = menuCategory;
                }
            }
            return selectedCategory.getSubCategories();


        } else {
            return new ArrayList<>();
        }

    }

    //======== STUDENT ==============

    /**
     * Student- Category Academics
     */
    public static ArrayList<MenuItem> getAcedemicsStudentMenu() {

        ArrayList<MenuItem> menuItems = new ArrayList<>();

      /*  MenuItem menuItem_header = new MenuItem();
        menuItem_header.setName("Academics");
        menuItem_header.setDescription("Academics");*/

        MenuItem menuItem_first = new MenuItem();
        menuItem_first.setName("Subjects");
        menuItem_first.setDescription("List Of Subjects You Are Studying");
        menuItem_first.setImage(R.drawable.ic_subjects);
        menuItem_first.setRole(StaticConstant.ROLE_STUDENT);

        MenuItem menuItem_second = new MenuItem();
        menuItem_second.setName("Time Table");
        menuItem_second.setDescription("Your Daily Routine Is Here");
        menuItem_second.setImage(R.drawable.ic_timetable);
        menuItem_second.setRole(StaticConstant.ROLE_STUDENT);

        MenuItem menuItem_third = new MenuItem();
        menuItem_third.setName("Exams");
        menuItem_third.setDescription("Exam Schedule, Syllabus, Report Cards");
        menuItem_third.setImage(R.drawable.ic_exams);
        menuItem_third.setRole(StaticConstant.ROLE_STUDENT);

        MenuItem menuItem_fourth = new MenuItem();
        menuItem_fourth.setName("Special Class");
        menuItem_fourth.setDescription("Follow Special Class As Planned");
        menuItem_fourth.setImage(R.drawable.ic_special_class);
        menuItem_fourth.setRole(StaticConstant.ROLE_STUDENT);

        MenuItem menuItem_fifth = new MenuItem();
        menuItem_fifth.setName("Syllabus");
        menuItem_fifth.setDescription("Follow Subject Syllabus");
        menuItem_fifth.setImage(R.drawable.ic_syllabus);
        menuItem_fifth.setRole(StaticConstant.ROLE_STUDENT);

        MenuItem menuItem_sixth = new MenuItem();
        menuItem_sixth.setName("Lesson Plan");
        menuItem_sixth.setDescription("Follow Updated Lesson Plan");
        menuItem_sixth.setImage(R.drawable.ic_lesson_plan);
        menuItem_sixth.setRole(StaticConstant.ROLE_STUDENT);

        // menuItems.add(menuItem_header);
        menuItems.add(menuItem_first);
        menuItems.add(menuItem_second);
        menuItems.add(menuItem_third);
        menuItems.add(menuItem_fourth);
        menuItems.add(menuItem_fifth);
        menuItems.add(menuItem_sixth);

        return menuItems;
    }

    /**
     * Student- Category Academics
     */
    public static ArrayList<MenuItem> getDailyActivityStudentMenu() {

        ArrayList<MenuItem> menuItems = new ArrayList<>();

      /*  MenuItem menuItem_header = new MenuItem();
        menuItem_header.setName("Daily Activity");
        menuItem_header.setDescription("Daily Activity");*/

        MenuItem menuItem_first = new MenuItem();
        menuItem_first.setName("Attendance Info");
        menuItem_first.setDescription("Keep Track Of Your Attendance ");
        menuItem_first.setImage(R.drawable.ic_attendance);
        menuItem_first.setRole(StaticConstant.ROLE_STUDENT);

        MenuItem menuItem_second = new MenuItem();
        menuItem_second.setName("Class Announcement");
        menuItem_second.setDescription("Receive Teacher Announcements");
        menuItem_second.setImage(R.drawable.ic_announcement);
        menuItem_second.setRole(StaticConstant.ROLE_STUDENT);

        MenuItem menuItem_third = new MenuItem();
        menuItem_third.setName("Assignment");
        menuItem_third.setDescription("Daily Assignments Are Listed and Graded Here");
        menuItem_third.setImage(R.drawable.ic_assignment);
        menuItem_third.setRole(StaticConstant.ROLE_STUDENT);

        MenuItem menuItem_fourth = new MenuItem();
        menuItem_fourth.setName("Test");
        menuItem_fourth.setDescription("Tests Are Listed and Graded Here");
        menuItem_fourth.setImage(R.drawable.ic_test);
        menuItem_fourth.setRole(StaticConstant.ROLE_STUDENT);

        MenuItem menuItem_fifth = new MenuItem();
        menuItem_fifth.setName("Classwork");
        menuItem_fifth.setDescription("Find Out What Has Been Done In Class");
        menuItem_fifth.setImage(R.drawable.ic_class_work);
        menuItem_fifth.setRole(StaticConstant.ROLE_STUDENT);

        MenuItem menuItem_sixth = new MenuItem();
        menuItem_sixth.setName("Bus Tracking");
        menuItem_sixth.setDescription("Follow The Bus");
        menuItem_sixth.setImage(R.drawable.ic_bus);
        menuItem_sixth.setRole(StaticConstant.ROLE_STUDENT);

        MenuItem menuItem_seventh = new MenuItem();
        menuItem_seventh.setName("Activity");
        menuItem_seventh.setDescription("Activities Are Listed And Graded Here");
        menuItem_seventh.setImage(R.drawable.ic_activity);
        menuItem_seventh.setRole(StaticConstant.ROLE_STUDENT);

        MenuItem menuItem_eighth = new MenuItem();
        menuItem_eighth.setName("Library");
        menuItem_eighth.setDescription("Access Library Resources From Here");
        menuItem_eighth.setImage(R.drawable.ic_library);
        menuItem_eighth.setRole(StaticConstant.ROLE_STUDENT);

        // menuItems.add(menuItem_header);
        menuItems.add(menuItem_first);
        menuItems.add(menuItem_second);
        menuItems.add(menuItem_third);
        menuItems.add(menuItem_fourth);
        menuItems.add(menuItem_fifth);
        menuItems.add(menuItem_sixth);
        menuItems.add(menuItem_seventh);
        menuItems.add(menuItem_eighth);

        return menuItems;
    }

    /**
     * Student- Category Communication
     */
    public static ArrayList<MenuItem> getCommunicationStudentMenu() {

        ArrayList<MenuItem> menuItems = new ArrayList<>();

        MenuItem menuItem_first = new MenuItem();
        menuItem_first.setName("Bulletin");
        menuItem_first.setDescription("Receive Insitution, Class & Personal Notices");
        menuItem_first.setImage(R.drawable.ic_bulletins);
        menuItem_first.setRole(StaticConstant.ROLE_STUDENT);

       /* MenuItem menuItem_second = new MenuItem();
        menuItem_second.setName("Self Request");
        menuItem_second.setDescription("Find Out About All Annual Events");
        menuItem_second.setImage(R.drawable.ic_self_reuest);

        MenuItem menuItem_third = new MenuItem();
        menuItem_third.setName("School Request");
        menuItem_third.setDescription("School Request");
        menuItem_third.setImage(R.drawable.ic_school_reuest);*/


        MenuItem menuItem_seventh = new MenuItem();
        menuItem_seventh.setName("Event Calender");
        menuItem_seventh.setDescription("Find Out About All Annual Events");
        menuItem_seventh.setImage(R.drawable.ic_event_calendar);
        menuItem_seventh.setRole(StaticConstant.ROLE_STUDENT);

        MenuItem menuItem_eighth = new MenuItem();
        menuItem_eighth.setName("Daily Diary");
        menuItem_eighth.setDescription("Access Daily Dairy");
        menuItem_eighth.setImage(R.drawable.ic_daily_diary);
        menuItem_eighth.setRole(StaticConstant.ROLE_STUDENT);
/*
        MenuItem menuItem_ninth = new MenuItem();
        menuItem_ninth.setName("Feedback");
        menuItem_ninth.setDescription("Feedback");
        menuItem_ninth.setImage(R.drawable.ic_feedback);*/

        MenuItem menuItem_tenth = new MenuItem();
        menuItem_tenth.setName("Blog");
        menuItem_tenth.setDescription("Join Institution Blog With Your Views");
        menuItem_tenth.setImage(R.drawable.ic_blog);
        menuItem_tenth.setRole(StaticConstant.ROLE_STUDENT);

        MenuItem menuItem_eleventh = new MenuItem();
        menuItem_eleventh.setName("Directory");
        menuItem_eleventh.setDescription("Access Institution Directory");
        menuItem_eleventh.setImage(R.drawable.ic_directory);
        menuItem_eleventh.setRole(StaticConstant.ROLE_STUDENT);

        //menuItems.add(menuItem_header);
        menuItems.add(menuItem_first);
      /* menuItems.add(menuItem_second);
        menuItems.add(menuItem_third);*/
        menuItems.add(menuItem_seventh);
        menuItems.add(menuItem_eighth);
        //menuItems.add(menuItem_ninth);
        menuItems.add(menuItem_tenth);
        menuItems.add(menuItem_eleventh);

        return menuItems;
    }

    /**
     * Student- Category Student
     */
    public static ArrayList<MenuItem> getStudentStudentMenu() {

        ArrayList<MenuItem> menuItems = new ArrayList<>();

        MenuItem menuItem_first = new MenuItem();
        menuItem_first.setName("Class Fellow");
        menuItem_first.setDescription("Your Classmates Are Here");
        menuItem_first.setImage(R.drawable.ic_school_fellow);
        menuItem_first.setRole(StaticConstant.ROLE_STUDENT);

        MenuItem menuItem_second = new MenuItem();
        menuItem_second.setName("Behaviour");
        menuItem_second.setDescription("Your Behaviour Record Is Here");
        menuItem_second.setImage(R.drawable.ic_behaviour);
        menuItem_second.setRole(StaticConstant.ROLE_STUDENT);

        MenuItem menuItem_third = new MenuItem();
        menuItem_third.setName("Hostel");
        menuItem_third.setDescription("Find More About Hostel");
        menuItem_third.setImage(R.drawable.ic_hostel);
        menuItem_third.setRole(StaticConstant.ROLE_STUDENT);

        MenuItem menuItem_seventh = new MenuItem();
        menuItem_seventh.setName("Organization");
        menuItem_seventh.setDescription("Keep Yourself Updated Of Recent Actvities");
        menuItem_seventh.setImage(R.drawable.ic_organization);
        menuItem_seventh.setRole(StaticConstant.ROLE_STUDENT);

        MenuItem menuItem_eighth = new MenuItem();
        menuItem_eighth.setName("Campus Activity");
        menuItem_eighth.setDescription("Follow Campus Buzz");
        menuItem_eighth.setImage(R.drawable.ic_campus);
        menuItem_eighth.setRole(StaticConstant.ROLE_STUDENT);

        MenuItem menuItem_ninth = new MenuItem();
        menuItem_ninth.setName("Career");
        menuItem_ninth.setDescription("Career Guidance from Here");
        menuItem_ninth.setImage(R.drawable.ic_career);
        menuItem_ninth.setRole(StaticConstant.ROLE_STUDENT);

        menuItems.add(menuItem_first);
        menuItems.add(menuItem_second);
        menuItems.add(menuItem_third);
        menuItems.add(menuItem_seventh);
        menuItems.add(menuItem_eighth);
        menuItems.add(menuItem_ninth);

        return menuItems;
    }

    /**
     * Student- Category Profile
     */
    public static ArrayList<MenuItem> getProfileStudentMenu() {

        ArrayList<MenuItem> menuItems = new ArrayList<>();

       /* MenuItem menuItem_header = new MenuItem();
        menuItem_header.setName("Profile");
        menuItem_header.setDescription("Profile");
*/
        MenuItem menuItem_first = new MenuItem();
        menuItem_first.setName("My Profile");
        menuItem_first.setDescription("Your Personal Details Are Here");
        menuItem_first.setImage(R.drawable.ic_profile_icon);
        menuItem_first.setRole(StaticConstant.ROLE_STUDENT);
/*
        MenuItem menuItem_second = new MenuItem();
        menuItem_second.setName("Fees");
        menuItem_second.setDescription("Fees");
        menuItem_second.setImage(R.drawable.ic_rupee);*/

        MenuItem menuItem_third = new MenuItem();
        menuItem_third.setName("Help");
        menuItem_third.setDescription("Ping us for Technical Support");
        menuItem_third.setImage(R.drawable.ic_help);
        menuItem_third.setRole(StaticConstant.ROLE_STUDENT);

        MenuItem menuItem_fourth = new MenuItem();
        menuItem_fourth.setName("Web Access");
        menuItem_fourth.setDescription("Access The App On Your Computer");
        menuItem_fourth.setImage(R.drawable.ic_access);
        menuItem_fourth.setRole(StaticConstant.ROLE_STUDENT);

        MenuItem menuItem_fifth = new MenuItem();
        menuItem_fifth.setName("Tutorials");
        menuItem_fifth.setDescription("Learn How To Use The App");
        menuItem_fifth.setImage(R.drawable.ic_tutorial);
        menuItem_fifth.setRole(StaticConstant.ROLE_STUDENT);

        /* MenuItem menuItem_sixth= new MenuItem();
        menuItem_sixth.setName("Child Profile");
        menuItem_sixth.setDescription("Child Profile");
*/
        //menuItems.add(menuItem_header);
        menuItems.add(menuItem_first);
        //  menuItems.add(menuItem_second);
        menuItems.add(menuItem_third);
        menuItems.add(menuItem_fourth);
        menuItems.add(menuItem_fifth);
       /*  menuItems.add(menuItem_sixth);*/

        return menuItems;
    }
}
