package com.knwedu.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.knwedu.activity.ActivityFragmentManagerParent;
import com.knwedu.activity.ActivityMain;
import com.knwedu.activity.ActivityTutorailReuest;
import com.knwedu.activity.ActivityTutorailSchoolReuest;
import com.knwedu.adapter.AdapterListWithHeader;
import com.knwedu.calcuttapublicschool.R;
import com.knwedu.constant.MenuHeader;
import com.knwedu.constant.StaticConstant;
import com.knwedu.data.Constant;
import com.knwedu.data.Consts;
import com.knwedu.model.MenuItem;

import java.util.List;

public class FragmentParent extends Fragment {

    private View root_view;
    private TabLayout tabLayout;
    private RecyclerView recyclerView;
    private AdapterListWithHeader mAdapter;
    private Context mContext;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root_view = inflater.inflate(R.layout.fragment_base_view, null);
        mContext = getActivity();

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
                // Toast.makeText(mContext, "CLick: " + obj.getName().toString(), Toast.LENGTH_SHORT).show();
                ActivityFragmentManagerParent.navigate((ActivityMain) getActivity(), view.findViewById(R.id.photo_content), obj);
            }
        });
        mAdapter.setOnTutorialClickListener(new AdapterListWithHeader.OnTutorialClickListener() {
            @Override
            public void onTutorialClick(View view, MenuItem obj, int position) {
                Log.e("Menu Name", "Name: " + obj.getName());
                switch (obj.getName()) {

                    case StaticConstant.REQUESTS:
                        Intent intent = new Intent(mContext, ActivityTutorailReuest.class);
                        startActivity(intent);
                        break;
                    case StaticConstant.SCHOOLREQUEST:
                        Intent intentSchoolReuest = new Intent(mContext, ActivityTutorailSchoolReuest.class);
                        startActivity(intentSchoolReuest);
                        break;
                }
            }
        });
    }

    private void initTabLayout() {

        tabLayout = (TabLayout) root_view.findViewById(R.id.tabs);
        List<String> items_tab = Constant.getParentCatgeory(getActivity());
        tabLayout.addTab(tabLayout.newTab().setText(items_tab.get(0)), true);

        // display first news
        displayListNews(getSubCategoriesByCategory(items_tab.get(0)));

        for (int i = 1; i < items_tab.size(); i++) {
            tabLayout.addTab(tabLayout.newTab().setText(items_tab.get(i)));
        }
    }

    private List<MenuItem> getSubCategoriesByCategory(String category) {


        //  ArrayList<String> allCategoriesName = Util.fetchMenuItems(mContext).getCategories();
      /*  for (int i = 0; i < allCategoriesName.size(); i++) {

        }*/


       /* if (category.equalsIgnoreCase("PROFILE")) {
            return Consts.getParentMenu(mContext, "");
        } else if (category.equalsIgnoreCase("STUDENT")) {
            return Consts.getParentStudentMenu();
        } else if (category.equalsIgnoreCase("ACADEMICS")) {
            return Consts.getAcedemicsMenu();
        } else if (category.equalsIgnoreCase("DAILY ACTIVITY")) {
            return Consts.getDailyActivityMenu();
        } else if (category.equalsIgnoreCase("COMMUNICATION")) {
            return Consts.getCommunicationMenu();
        }*/
        return Consts.getParentMenu(mContext, category);
    }


}
