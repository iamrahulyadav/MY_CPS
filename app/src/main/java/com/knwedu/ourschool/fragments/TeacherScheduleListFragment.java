package com.knwedu.ourschool.fragments;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.http.HttpResponseCache;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.knwedu.calcuttapublicschool.R;
import com.knwedu.ourschool.TeacherScheduleActivity;
import com.knwedu.ourschool.adapter.TeacherAdapter;
import com.knwedu.ourschool.db.DatabaseAdapter;
import com.knwedu.ourschool.db.SchoolApplication;
import com.knwedu.ourschool.utils.Constants;
import com.knwedu.ourschool.utils.DataStructureFramwork.Subject;
import com.knwedu.ourschool.utils.JsonParser;
import com.knwedu.ourschool.utils.SchoolAppUtils;
import com.knwedu.ourschool.utils.Urls;

public class TeacherScheduleListFragment extends Fragment {
    private ListView list;
    private TeacherAdapter adapter;
    private View view;
    private ProgressDialog dialog;
    private ArrayList<Subject> subjects;
    public DatabaseAdapter mDatabase;
    private TextView textEmpty;
    private boolean internetAvailable = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mDatabase = ((SchoolApplication) getActivity().getApplication())
                .getDatabase();
        view = inflater.inflate(R.layout.fragment_teacher_assignment_list,
                container, false);
        SchoolAppUtils.loadAppLogo(getActivity(),
                (ImageButton) view.findViewById(R.id.app_logo));
        list = (ListView) view.findViewById(R.id.listview);
        textEmpty = (TextView) view.findViewById(R.id.textEmpty);
        subjects = new ArrayList<Subject>();
        if (SchoolAppUtils.isOnline(getActivity())) {
            internetAvailable = true;
            initialize();
        } else {
            view.findViewById(R.id.txt_offline).setVisibility(View.VISIBLE);
            textEmpty.setText("No data available");
            textEmpty.setVisibility(View.VISIBLE);
            //new OfflineSubjectDetailsAsync().execute();
        }
        return view;
    }

    private void initialize() {
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
        nameValuePairs.add(new BasicNameValuePair("user_type_id",
                SchoolAppUtils.GetSharedParameter(getActivity(),
                        Constants.USERTYPEID)));
        nameValuePairs.add(new BasicNameValuePair("organization_id",
                SchoolAppUtils.GetSharedParameter(getActivity(),
                        Constants.UORGANIZATIONID)));
        nameValuePairs.add(new BasicNameValuePair("user_id", SchoolAppUtils
                .GetSharedParameter(getActivity(), Constants.USERID)));
        new GetAssignmentAsyntask().execute(nameValuePairs);
    }

    OnItemClickListener listener = new OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                long arg3) {
            Intent intent = new Intent(getActivity(),
                    TeacherScheduleActivity.class);
            if (internetAvailable) {
                intent.putExtra(Constants.TSCHEDULEID,
                        subjects.get(arg2).object.toString());
            } else {
                intent.putExtra(Constants.OFFLINE_SECTION_ID,
                        subjects.get(arg2).section_id);
                intent.putExtra(Constants.OFFLINE_SUBJECT_ID,
                        subjects.get(arg2).id);
            }
            intent.putExtra(Constants.PAGE_TITLE, "Lesson");
            getActivity().startActivity(intent);

        }
    };

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @SuppressLint("NewApi")
    @Override
    public void onStop() {
        super.onStop();
        if (dialog != null) {
            if (dialog.isShowing()) {
                dialog.dismiss();
                dialog = null;
            }
        }
        HttpResponseCache cache = HttpResponseCache.getInstalled();
        if (cache != null) {
            cache.flush();
        }
    }

    private class GetAssignmentAsyntask extends
            AsyncTask<List<NameValuePair>, Void, Boolean> {
        String error = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(getActivity());
            dialog.setTitle(getActivity().getTitle().toString());
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
                    Urls.api_activity_get_subjects);
            try {

                if (json != null) {
                    if (json.getString("result").equalsIgnoreCase("1")) {

                        JSONArray array = json.getJSONArray("data");
                        subjects.clear();
                        for (int i = 0; i < array.length(); i++) {
                            Subject assignment = new Subject(
                                    array.getJSONObject(i));
                            subjects.add(assignment);
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
                if (subjects != null) {
                    mDatabase.open();
                    // Delete all data in database
                    mDatabase.deleteAllSubjects();
                    String temp = null;
                    for (int i = 0; i < subjects.size(); i++) {
                        if (i == 0) {
                            subjects.get(0).check = true;
                            temp = subjects.get(0).classs + " "
                                    + subjects.get(0).section_name;
                        } else {
                            if (!temp.equalsIgnoreCase(subjects.get(i).classs
                                    + " " + subjects.get(i).section_name)) {
                                subjects.get(i).check = true;
                                temp = subjects.get(i).classs + " "
                                        + subjects.get(i).section_name;
                            }
                        }
                        Log.d("Insert: ", "Inserting ..");
                        mDatabase.addSubjects(subjects.get(i));
                    }
                    mDatabase.close();
                    if (subjects.size() > 0) {
                        textEmpty.setVisibility(View.GONE);
                        adapter = new TeacherAdapter(getActivity(), subjects,
                                true);
                        list.setAdapter(adapter);
                        list.setOnItemClickListener(listener);
                    } else {
                        textEmpty.setText("No data available");
                        textEmpty.setVisibility(View.VISIBLE);
                    }
                }
            } else {
                if (error.length() > 0) {
                    textEmpty.setText(error);

                } else {
                    textEmpty.setText(getResources().getString(R.string.error));
                }
                textEmpty.setVisibility(View.VISIBLE);

            }

        }

    }

    class OfflineSubjectDetailsAsync extends AsyncTask<String, Subject, Void> {

        protected void onPreExecute() {
            super.onPreExecute();
            subjects.clear();
        }

        protected Void doInBackground(String... params) {

            mDatabase.open();
            subjects = mDatabase.getAllSubject();
            mDatabase.close();

            return null;
        }

        @Override
        protected void onProgressUpdate(Subject... values) {
            super.onProgressUpdate(values);
        }

        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (subjects != null) {
                String temp = null;
                for (int i = 0; i < subjects.size(); i++) {
                    if (i == 0) {
                        subjects.get(0).check = true;
                        temp = subjects.get(0).classs + " "
                                + subjects.get(0).section_name;
                    } else {
                        if (!temp.equalsIgnoreCase(subjects.get(i).classs + " "
                                + subjects.get(i).section_name)) {
                            subjects.get(i).check = true;
                            temp = subjects.get(i).classs + " "
                                    + subjects.get(i).section_name;
                        }
                    }
                }
                if (subjects.size() > 0) {
                    textEmpty.setVisibility(View.GONE);
                    adapter = new TeacherAdapter(getActivity(), subjects, true);
                    list.setAdapter(adapter);
                    list.setOnItemClickListener(listener);
                } else {
                    textEmpty.setText("No data available");
                    textEmpty.setVisibility(View.VISIBLE);
                }
            }

        }
    }
}
