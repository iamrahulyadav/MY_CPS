package com.knwedu.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.knwedu.adapter.AllBulletinsListAdapter;
import com.knwedu.calcuttapublicschool.R;
import com.knwedu.constant.Url;
import com.knwedu.model.Bulletin;
import com.knwedu.model.Role;
import com.knwedu.ourschool.NewsActivity;
import com.knwedu.ourschool.utils.Constants;
import com.knwedu.ourschool.utils.SchoolAppUtils;
import com.knwedu.util.AlertDialogCallBack;
import com.knwedu.util.Util;
import com.knwedu.volley.PostWithJsonWebTask;
import com.knwedu.volley.ServerResponseStringCallback;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by ritwik on 09-04-2018.
 */

public class ActivityAllBulletins extends AppCompatActivity {
    private Context mContext;
    private Role role;
    private ListView lv_bulletins;

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_bulletin);
        mContext = ActivityAllBulletins.this;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Bulletins");
        role = (Role) getIntent().getSerializableExtra("role");
        lv_bulletins = (ListView) findViewById(R.id.lv_bulletins);
        getBulletins();
    }

    private void getBulletins() {

        HashMap<String, String> map = new HashMap<>();
        map.put("user_id", "" + SchoolAppUtils.GetSharedParameter(mContext, Constants.USERID));
        map.put("user_type_id", "" + SchoolAppUtils.GetSharedParameter(mContext, Constants.USERTYPEID));
        map.put("organization_id", "" + SchoolAppUtils.GetSharedParameter(mContext, Constants.UORGANIZATIONID));
        if (!(role.getRole().equalsIgnoreCase("teacher")))
            map.put("child_id", "" + SchoolAppUtils.GetSharedParameter(mContext, Constants.CHILD_ID));

        PostWithJsonWebTask.callPostWithJsonWebtask(ActivityAllBulletins.this, Url.GET_ALL_BULLETINS_URL, map, new ServerResponseStringCallback() {
            @Override
            public void onSuccess(String resultJsonObject) {

                try {

                    JSONObject jsonObject = new JSONObject(resultJsonObject);
                    if (jsonObject.optString("result").trim().equalsIgnoreCase("1")) {

                        JSONArray bulletinJSONArray = jsonObject.optJSONArray("data");

                        final ArrayList<Bulletin> bulletins = new ArrayList<>();
                        for (int i = 0; i < bulletinJSONArray.length(); i++) {
                            JSONObject object = bulletinJSONArray.optJSONObject(i);
                            Bulletin bulletin = new Bulletin();
                            bulletin.setId(object.optString("id"));
                            bulletin.setClass_id(object.optString("class_id"));
                            bulletin.setSection_id(object.optString("section_id"));
                            bulletin.setStudent_id(object.optString("student_id"));
                            bulletin.setTeacher_id(object.optString("teacher_id"));
                            bulletin.setTitle(object.optString("title"));
                            bulletin.setDescription(object.optString("description"));
                            bulletin.setCreated_by(object.optString("created_by"));
                            bulletin.setUser_type(object.optString("user_type"));
                            bulletin.setDoc(object.optString("doc"));
                            bulletin.setFile_name(object.optString("file_name"));
                            bulletin.setCreated_date(object.optString("created_date"));
                            bulletin.setActive_session_id(object.optString("active_session_id"));
                            bulletin.setOrganisation_id(object.optString("organisation_id"));
                            bulletin.setSend_status(object.optString("send_status"));
                            bulletin.setIs_delete(object.optString("is_delete"));
                            bulletin.setFname(object.optString("fname"));
                            bulletin.setClassName(object.optString("class"));
                            bulletin.setSection_name(object.optString("section_name"));
                            bulletin.setObject(object.toString());
                            bulletins.add(bulletin);
                        }
                        if (bulletins.size() > 0) {
                            AllBulletinsListAdapter allBulletinsListAdapter = new AllBulletinsListAdapter(mContext, bulletins);
                            lv_bulletins.setAdapter(allBulletinsListAdapter);
                            lv_bulletins.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                                    Intent intent = new Intent(mContext, NewsActivity.class);
                                    intent.putExtra(Constants.NEWS, bulletins.get(position).getObject().trim());
                                    intent.putExtra("type", SchoolAppUtils.GetSharedParameter(mContext, Constants.USERTYPEID));
                                    intent.putExtra(Constants.PAGE_TITLE, "Bulletin");
                                    startActivity(intent);
                                }
                            });

                        } else {
                            Util.showCallBackMessageWithOkCallback(mContext, "No data found.", new AlertDialogCallBack() {
                                @Override
                                public void onSubmit() {
                                    onBackPressed();
                                }

                                @Override
                                public void onCancel() {

                                }
                            });
                        }

                    } else if (jsonObject.optString("result").trim().equalsIgnoreCase("0")) {
                        Util.showMessageWithOk(ActivityAllBulletins.this, "" + jsonObject.optString("data"));
                    } else {
                        Util.showMessageWithOk(ActivityAllBulletins.this, "Something went wrong! Please try again.");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("Exception", "" + e.getLocalizedMessage());
                    Util.showMessageWithOk(ActivityAllBulletins.this, "Something went wrong! Please try again.");
                }

            }

            @Override
            public void ErrorMsg(VolleyError error) {

            }
        }, true, Request.Method.POST);
    }
}
