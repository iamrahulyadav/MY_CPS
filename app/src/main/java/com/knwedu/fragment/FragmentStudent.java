package com.knwedu.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.knwedu.activity.ActivityFragmentManagerStudent;
import com.knwedu.activity.ActivityMain;
import com.knwedu.adapter.AdapterListWithHeader;
import com.knwedu.calcuttapublicschool.R;
import com.knwedu.data.Constant;
import com.knwedu.data.Consts;
import com.knwedu.model.MenuItem;

import java.util.List;

public class FragmentStudent extends Fragment {

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
                //Toast.makeText(getActivity(), "" + obj.getName() + "Click", Toast.LENGTH_SHORT).show();
                  ActivityFragmentManagerStudent.navigate((ActivityMain) getActivity(), view.findViewById(R.id.photo_content), obj);
            }
        });
    }

    private void initTabLayout() {
        tabLayout = (TabLayout) root_view.findViewById(R.id.tabs);
        List<String> items_tab = Constant.getStudentCatgeory(getActivity());
        tabLayout.addTab(tabLayout.newTab().setText(items_tab.get(0)), true);

        // display first news
        displayListNews(getSubCategoriesByCategory(items_tab.get(0)));

        for (int i = 1; i < items_tab.size(); i++) {
            tabLayout.addTab(tabLayout.newTab().setText(items_tab.get(i)));
        }
    }

    private List<MenuItem> getSubCategoriesByCategory(String category) {
      /*  if (category.equalsIgnoreCase("ACADEMICS")) {
            return Consts.getAcedemicsStudentMenu();
        } else if (category.equalsIgnoreCase("STUDENT")) {
            return Consts.getStudentStudentMenu();
        } else if (category.equalsIgnoreCase("DAILY ACTIVITY")) {
            return Consts.getDailyActivityStudentMenu();
        } else if (category.equalsIgnoreCase("COMMUNICATION")) {
            return Consts.getCommunicationStudentMenu();
        } else if (category.equalsIgnoreCase("PROFILE")) {
            return Consts.getProfileStudentMenu();
        }
        return new ArrayList<>();*/
        return Consts.getStudentMenu(getActivity(), category);
    }
}
