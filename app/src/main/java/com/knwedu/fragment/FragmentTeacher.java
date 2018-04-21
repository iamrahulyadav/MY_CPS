package com.knwedu.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.knwedu.activity.ActivityFragmentManagerTeacher;
import com.knwedu.activity.ActivityMain;
import com.knwedu.activity.ActivityTutorailBehaviour;
import com.knwedu.activity.ActivityTutorialAnnouncement;
import com.knwedu.activity.ActivityTutorialAssignment;
import com.knwedu.activity.ActivityTutorialAttendance;
import com.knwedu.activity.ActivityTutorialClassWork;
import com.knwedu.activity.ActivityTutorialDailyDiary;
import com.knwedu.activity.ActivityTutorialTest;
import com.knwedu.adapter.AdapterListWithHeader;
import com.knwedu.calcuttapublicschool.R;
import com.knwedu.data.Constant;
import com.knwedu.data.Consts;
import com.knwedu.model.MenuItem;
import com.knwedu.ourschool.utils.Constants;

import java.util.List;

public class FragmentTeacher extends Fragment {

    private View root_view;
    private TabLayout tabLayout;
    private RecyclerView recyclerView;
    private AdapterListWithHeader mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root_view = inflater.inflate(R.layout.fragment_base_view, null);


        recyclerView = (RecyclerView) root_view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // prepare tab layout
        initTabLayout();

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                displayListNews(getSubCategoriesByCategory(tab.getText().toString()));
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        return root_view;
    }

    @Override
    public void onResume() {
        mAdapter.notifyDataSetChanged();
        super.onResume();
    }

    private void displayListNews(List<MenuItem> items) {
        mAdapter = new AdapterListWithHeader(getActivity(), items.get(0), items);
        recyclerView.setAdapter(mAdapter);
       /* mAdapter.setOnItemClickListener(new AdapterListWithHeader().OnItemClickListener() {
            @Override
            public void onItemClick(View v, MenuItem obj, int position) {
                ActivityNewsDetails.navigate((ActivityMain)getActivity(), v.findViewById(R.id.image), obj);
            }
        });*/
        mAdapter.setOnItemClickListener(new AdapterListWithHeader.OnItemClickListener() {
            @Override
            public void onItemClick(View view, MenuItem obj, int position) {
                // Toast.makeText(getActivity(), "" + obj.getName() + "Click", Toast.LENGTH_SHORT).show();
                /*if (obj.getName().equalsIgnoreCase("Assignment")) {
                    Intent intent = new Intent(getActivity(), ActivityAssignment.class);
                    intent.putExtra("menuItem", obj);
                    startActivity(intent);
                    return;
                } else if (obj.getName().equalsIgnoreCase("Test")) {
                    Intent intent = new Intent(getActivity(), ActivityTest.class);
                    intent.putExtra("menuItem", obj);
                    startActivity(intent);
                    return;
                } else if (obj.getName().equalsIgnoreCase("Classwork")) {
                    Intent intent = new Intent(getActivity(), ActivityClasswork.class);
                    intent.putExtra("menuItem", obj);
                    startActivity(intent);
                    return;
                } else if (obj.getName().equalsIgnoreCase("Class Announcement")) {
                    Intent intent = new Intent(getActivity(), ActivityAnnouncement.class);
                    intent.putExtra("menuItem", obj);
                    startActivity(intent);
                    return;
                }*/
                ActivityFragmentManagerTeacher.navigate((ActivityMain) getActivity(), view.findViewById(R.id.photo_content), obj);
            }
        });
        mAdapter.setOnTutorialClickListener(new AdapterListWithHeader.OnTutorialClickListener() {
            @Override
            public void onTutorialClick(View view, MenuItem obj, int position) {
                switch (obj.getName()) {
                    case Constants.MENU_TEACHER_ASSIGNMENT:
                        Intent assignmentIntent = new Intent(getActivity(), ActivityTutorialAssignment.class);
                        startActivity(assignmentIntent);
                        break;
                    case Constants.MENU_TEACHER_QUIZ:
                        Intent testIntent = new Intent(getActivity(), ActivityTutorialTest.class);
                        startActivity(testIntent);
                        break;
                    case Constants.MENU_TEACHER_COURSE_WORK:
                        Intent classworkIntent = new Intent(getActivity(), ActivityTutorialClassWork.class);
                        startActivity(classworkIntent);
                        break;
                    case Constants.MENU_TEACHER_ANNOUNCEMENT:
                        Intent announcementIntent = new Intent(getActivity(), ActivityTutorialAnnouncement.class);
                        startActivity(announcementIntent);
                        break;
                    case Constants.MENU_TEACHER_ATTENDANCE:
                        Intent attendancIntent = new Intent(getActivity(), ActivityTutorialAttendance.class);
                        startActivity(attendancIntent);
                        break;
                    case Constants.MENU_STUDENT_BEHAVIOUR:
                        Intent behaviourIntent = new Intent(getActivity(), ActivityTutorailBehaviour.class);
                        startActivity(behaviourIntent);
                        break;
                    case Constants.MENU_DAILY_DIARY:
                        Intent dailyDiaryIntent = new Intent(getActivity(), ActivityTutorialDailyDiary.class);
                        startActivity(dailyDiaryIntent);
                        break;
                }
            }
        });
    }

    private void initTabLayout() {
        tabLayout = (TabLayout) root_view.findViewById(R.id.tabs);
        List<String> items_tab = Constant.getTeacherCatgeory(getActivity());
        tabLayout.addTab(tabLayout.newTab().setText(items_tab.get(0)), true);

        // display first news
        displayListNews(getSubCategoriesByCategory(items_tab.get(0)));

        for (int i = 1; i < items_tab.size(); i++) {
            tabLayout.addTab(tabLayout.newTab().setText(items_tab.get(i)));
        }
    }

    private List<MenuItem> getSubCategoriesByCategory(String category) {
      /* if (category.equalsIgnoreCase("PROFILE")) {
            return Consts.getTeacherMenu();
        } else if (category.equalsIgnoreCase("STUDENT")) {
            return Consts.getStudentTeacherMenu();
        } else if (category.equalsIgnoreCase("ACADEMICS")) {
            return Consts.getAcedemicsTeacherMenu();
        } else if (category.equalsIgnoreCase("DAILY ACTIVITY")) {
            return Consts.getDailyActivityTeacherMenu();
        } else if (category.equalsIgnoreCase("COMMUNICATION")) {
            return Consts.getCommunicationTeacherMenu();
        }*/
        return Consts.getParentMenu(getActivity(), category);
    }


}
