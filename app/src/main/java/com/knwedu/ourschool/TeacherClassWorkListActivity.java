package com.knwedu.ourschool;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.http.HttpResponseCache;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.google.analytics.tracking.android.EasyTracker;
import com.knwedu.calcuttapublicschool.R;
import com.knwedu.ourschool.adapter.AnnouncementAdapter;
import com.knwedu.ourschool.db.DatabaseAdapter;
import com.knwedu.ourschool.db.SchoolApplication;
import com.knwedu.ourschool.utils.Constants;
import com.knwedu.ourschool.utils.DataStructureFramwork.Announcement;
import com.knwedu.ourschool.utils.DataStructureFramwork.Subject;
import com.knwedu.ourschool.utils.JsonParser;
import com.knwedu.ourschool.utils.SchoolAppUtils;
import com.knwedu.ourschool.utils.Urls;

public class TeacherClassWorkListActivity extends AppCompatActivity {
    private ArrayList<Announcement> classworks;
    private ProgressDialog dialog;
    private TextView header;
    private ListView listView;
    private AnnouncementAdapter adapter;
    ImageButton addAssignment;
    private Subject subject;
    private Button monthlyWeekly;
    private TextView textEmpty;
    public DatabaseAdapter mDatabase;
    private boolean internetAvailable = false;
    private String page_title = "";

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_assignment_list);
        page_title = getIntent().getStringExtra(Constants.PAGE_TITLE);
        SchoolAppUtils.loadAppLogo(TeacherClassWorkListActivity.this, (ImageButton) findViewById(R.id.app_logo));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setTitle("Publish");
        getSupportActionBar().setTitle("" + page_title);
        mDatabase = ((SchoolApplication) getApplication()).getDatabase();
        textEmpty = (TextView) findViewById(R.id.textEmpty);
        listView = (ListView) findViewById(R.id.listview);
        addAssignment = (ImageButton) findViewById(R.id.add_assignment);
        if ((SchoolAppUtils.GetSharedParameter(
                TeacherClassWorkListActivity.this,
                Constants.COURSEWORK_ADD_PERMISSION)).equalsIgnoreCase("0")) {
            addAssignment.setVisibility(View.INVISIBLE);
        }

        header = (TextView) findViewById(R.id.header_text);
        header.setVisibility(View.INVISIBLE);
        monthlyWeekly = (Button) findViewById(R.id.monthly_weekly_btn);
        monthlyWeekly.setVisibility(View.GONE);
        classworks = new ArrayList<Announcement>();
        internetAvailable = getIntent().getBooleanExtra(Constants.IS_ONLINE,
                false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (internetAvailable) {
            initialize();
        } else {
            findViewById(R.id.txt_offline).setVisibility(View.VISIBLE);
            monthlyWeekly.setVisibility(View.GONE);
            new OfflineSubjectListenerAsync().execute();
        }

    }

    ;

    @Override
    public void onStart() {
        super.onStart();
        Log.d("Google Analytics", "Tracking Start");
        EasyTracker.getInstance(this).activityStart(this);

    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("Google Analytics", "Tracking Stop");
        EasyTracker.getInstance(this).activityStop(this);
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
        HttpResponseCache cache = HttpResponseCache.getInstalled();
        if (cache != null) {
            cache.flush();
        }
    }

    private void initialize() {
        handleMonthlyWeekly();
        if (getIntent().getExtras() != null) {
            String temp = getIntent().getExtras().getString(
                    Constants.TANNOUNCEMENT);
            if (temp != null) {
                JSONObject object = null;
                try {
                    object = new JSONObject(temp);
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                if (object != null) {
                    subject = new Subject(object);
                }
            }
        }
        if (subject != null) {
            loadData();
        }
        addAssignment.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(TeacherClassWorkListActivity.this,
                        TeacherClassWorkActivity.class);
                intent.putExtra(Constants.IS_ONLINE, internetAvailable);
                intent.putExtra(Constants.TSUBJECT, subject.object.toString());
                intent.putExtra(Constants.PAGE_TITLE, page_title);
                startActivity(intent);
            }
        });

    }

    private void loadData() {

        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(5);
        nameValuePairs.add(new BasicNameValuePair("user_type_id",
                SchoolAppUtils.GetSharedParameter(getApplicationContext(),
                        Constants.USERTYPEID)));
        nameValuePairs.add(new BasicNameValuePair("organization_id",
                SchoolAppUtils.GetSharedParameter(getApplicationContext(),
                        Constants.UORGANIZATIONID)));
        nameValuePairs
                .add(new BasicNameValuePair("user_id", SchoolAppUtils
                        .GetSharedParameter(getApplicationContext(),
                                Constants.USERID)));
        if (monthlyWeekly.getText().equals(R.string.weekly)) {
            nameValuePairs.add(new BasicNameValuePair("list_type", "1"));
        } else {

            nameValuePairs.add(new BasicNameValuePair("list_type", "2"));
        }
        nameValuePairs.add(new BasicNameValuePair("type", "1"));
        nameValuePairs.add(new BasicNameValuePair("sub_id", subject.id));
        new GetAssignmentsAsyntask().execute(nameValuePairs);

    }

    private void handleMonthlyWeekly() {
        if (SchoolAppUtils.GetSharedBoolParameter(
                TeacherClassWorkListActivity.this,
                Constants.TEACHERMONTHLYWEEKLYCLASSWORK)) {
            monthlyWeekly.setText(R.string.weekly);
        } else {
            monthlyWeekly.setText(R.string.monthly);
        }
        monthlyWeekly.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (SchoolAppUtils.GetSharedBoolParameter(
                        TeacherClassWorkListActivity.this,
                        Constants.TEACHERMONTHLYWEEKLYCLASSWORK)) {
                    SchoolAppUtils.SetSharedBoolParameter(
                            TeacherClassWorkListActivity.this,
                            Constants.TEACHERMONTHLYWEEKLYCLASSWORK, false);
                    monthlyWeekly.setText(R.string.monthly);
                    loadData();
                } else {
                    SchoolAppUtils.SetSharedBoolParameter(
                            TeacherClassWorkListActivity.this,
                            Constants.TEACHERMONTHLYWEEKLYCLASSWORK, true);
                    monthlyWeekly.setText(R.string.weekly);
                    loadData();
                }
            }
        });
    }

    OnItemClickListener itemClickListener = new OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                long arg3) {
            Intent intent = new Intent(TeacherClassWorkListActivity.this,
                    ClassWorkActivity.class);
            intent.putExtra(Constants.IS_ONLINE, internetAvailable);
            intent.putExtra(Constants.PAGE_TITLE, page_title);
            if (internetAvailable) {
                intent.putExtra(Constants.ANNOUNCEMENT,
                        classworks.get(arg2).object.toString());
            } else {

                intent.putExtra(Constants.OFFLINE_ANNOUNCEMENT_ROWID,
                        classworks.get(arg2).row_id);
            }
            startActivity(intent);
        }
    };


    private class GetAssignmentsAsyntask extends
            AsyncTask<List<NameValuePair>, Void, Boolean> {
        String error = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(TeacherClassWorkListActivity.this);
            dialog.setTitle(page_title);
            dialog.setMessage(getResources().getString(R.string.please_wait));
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected Boolean doInBackground(List<NameValuePair>... params) {
            List<NameValuePair> nvp = params[0];
            JsonParser jParser = new JsonParser();
            JSONObject json = jParser.getJSONFromUrlnew(nvp,
                    Urls.api_announcement_view);
            try {

                if (json != null) {
                    if (json.getString("result").equalsIgnoreCase("1")) {
                        JSONArray array = json.getJSONArray("data");
                        classworks = new ArrayList<Announcement>();
                        for (int i = 0; i < array.length(); i++) {
                            Announcement assignment = new Announcement(
                                    array.getJSONObject(i));
                            classworks.add(assignment);
                        }

                        return true;
                    } else {
                        try {
                            error = json.getString("data");
                        } catch (Exception e) {
                        }
                        return false;
                    }
                } else {
                    error = getResources().getString(R.string.unknown_response);
                }
            } catch (JSONException e) {
                error = "Connection Failure";
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {

            super.onPostExecute(result);
            if (dialog != null) {
                dialog.dismiss();
                dialog = null;
            }

            if (result) {
                if (classworks != null) {
                    String temp = null;
                    for (int i = 0; i < classworks.size(); i++) {

                        classworks.get(i).sub_name = subject.sub_name;
                        if (i == 0) {
                            classworks.get(0).check = true;
                            temp = classworks.get(0).date;
                        } else {
                            if (!temp.equalsIgnoreCase(classworks.get(i).date)) {
                                classworks.get(i).check = true;
                                temp = classworks.get(i).date;
                            }
                        }
                    }
                    if (classworks.size() > 0) {
                        textEmpty.setVisibility(View.GONE);
                        adapter = new AnnouncementAdapter(
                                TeacherClassWorkListActivity.this, classworks, 2, "", "Class Work");
                        listView.setAdapter(adapter);
                        listView.setOnItemClickListener(itemClickListener);
                    } else {
                        textEmpty.setText("No data available");
                        textEmpty.setVisibility(View.VISIBLE);
                    }

                }
            } else {
                textEmpty.setText(error.toString());
                textEmpty.setVisibility(View.VISIBLE);
            }

        }

    }

    class OfflineSubjectListenerAsync extends AsyncTask<String, Subject, Void> {

        protected void onPreExecute() {
            super.onPreExecute();
            classworks.clear();
        }

        protected Void doInBackground(String... params) {
            final String subject_id = getIntent().getExtras().getString(
                    Constants.OFFLINE_SUBJECT_ID);
            mDatabase.open();
            subject = mDatabase.getSubject(subject_id);
            classworks = mDatabase.getAllAnnouncements(1, subject_id);
            mDatabase.close();
            addAssignment.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    Intent intent = new Intent(
                            TeacherClassWorkListActivity.this,
                            TeacherClassWorkActivity.class);
                    intent.putExtra(Constants.IS_ONLINE, internetAvailable);
                    intent.putExtra(Constants.OFFLINE_SUBJECT_ID, subject_id);
                    intent.putExtra(Constants.PAGE_TITLE, page_title);
                    startActivity(intent);
                }
            });

            return null;
        }

        @Override
        protected void onProgressUpdate(Subject... values) {
            super.onProgressUpdate(values);
        }

        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            if (classworks != null) {
                String temp = null;
                for (int i = 0; i < classworks.size(); i++) {
                    classworks.get(i).sub_name = subject.sub_name;
                    if (i == 0) {
                        classworks.get(0).check = true;
                        temp = classworks.get(0).date;
                    } else {
                        if (!temp.equalsIgnoreCase(classworks.get(i).date)) {
                            classworks.get(i).check = true;
                            temp = classworks.get(i).date;
                        }
                    }
                }
                if (classworks.size() > 0) {
                    textEmpty.setVisibility(View.GONE);
                    adapter = new AnnouncementAdapter(
                            TeacherClassWorkListActivity.this, classworks, 2, "");
                    listView.setAdapter(adapter);
                    listView.setOnItemClickListener(itemClickListener);
                } else {
                    textEmpty
                            .setText("No data available in Offline mode");
                    textEmpty.setVisibility(View.VISIBLE);
                }
            }
        }
    }
}