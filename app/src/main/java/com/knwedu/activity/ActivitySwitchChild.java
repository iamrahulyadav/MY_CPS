package com.knwedu.activity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.http.HttpResponseCache;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.knwedu.calcuttapublicschool.R;
import com.knwedu.ourschool.AdvertisementWebViewActivity;
import com.knwedu.ourschool.adapter.ParentChildrenAdapter;
import com.knwedu.ourschool.utils.Constants;
import com.knwedu.ourschool.utils.DataStructureFramwork;
import com.knwedu.ourschool.utils.JsonParser;
import com.knwedu.ourschool.utils.SchoolAppUtils;
import com.knwedu.ourschool.utils.Urls;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by knowalluser on 07-03-2018.
 */

public class ActivitySwitchChild extends AppCompatActivity {

    private ListView list;
    private ParentChildrenAdapter adapter;
    private ProgressDialog dialog;
    private Button report;
    private Context mContext;
    private ArrayList<DataStructureFramwork.StudentProfileInfo> studentProfiles;
    private String statusVal;

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
        setContentView(R.layout.fragment_list_child_profile);
        mContext = ActivitySwitchChild.this;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Switch Child");
        initialize();
    }

    private void initialize() {
        list = (ListView) findViewById(R.id.listview);

//		ImageView adImageView = (ImageView)view.findViewById(R.id.adImageView);
//		adImageView.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
////
//
//				startActivity(new Intent(getActivity(),
//						AdvertisementWebViewActivity.class));
//			}
//		});
//

        //-----------------for insurance-----------------------
        ImageView adImageView = (ImageView) findViewById(R.id.adImageView);

        if (Integer.parseInt(SchoolAppUtils.GetSharedParameter(mContext,
                Constants.USERTYPEID)) == 5 && (SchoolAppUtils.GetSharedParameter(mContext, Constants.INSURANCE_AVIALABLE).equalsIgnoreCase("1"))) {
            adImageView.setVisibility(View.VISIBLE);

        } else {
            adImageView.setVisibility(View.GONE);
        }
        adImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//				Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.AD_URL));
//				browserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//				browserIntent.setPackage("com.android.chrome");
//				try {
//					startActivity(browserIntent);
//				} catch (ActivityNotFoundException ex) {
//					// Chrome browser presumably not installed so allow user to choose instead
//					browserIntent.setPackage(null);
//					startActivity(browserIntent);
//				}
                startActivity(new Intent(mContext,
                        AdvertisementWebViewActivity.class));
            }
        });


        if (studentProfiles != null) {

            adapter = new ParentChildrenAdapter(mContext, studentProfiles);
            list.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            list.setOnItemClickListener(listener);
        } else {
            // String url = URLs.GetParentChildrenList +
            // "email=" +SchoolAppUtils.GetSharedParameter(mContext,
            // Constants.PUSERNAME) +
            // "&password=" +SchoolAppUtils.GetSharedParameter(mContext,
            // Constants.PPASSWORD);
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
            nameValuePairs.add(new BasicNameValuePair("user_id", SchoolAppUtils
                    .GetSharedParameter(mContext, Constants.USERID)));
            nameValuePairs.add(new BasicNameValuePair("user_type_id",
                    SchoolAppUtils.GetSharedParameter(mContext,
                            Constants.USERTYPEID)));
            nameValuePairs.add(new BasicNameValuePair("organization_id",
                    SchoolAppUtils.GetSharedParameter(mContext,
                            Constants.UORGANIZATIONID)));

            new GetChildrenAsyntask().execute(nameValuePairs);
        }
    }

    AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
            SchoolAppUtils.SetSharedParameter(mContext, Constants.SELECTED_CHILD_OBJECT, studentProfiles.get(arg2).object.toString());
            SchoolAppUtils.SetSharedParameter(mContext, Constants.CHILD_ID, studentProfiles.get(arg2).user_id);
            SchoolAppUtils.SetSharedParameter(mContext, Constants.SECTION_ID, studentProfiles.get(arg2).section_id);


            // ParentMainActivity.setUserImage(mContext);
            adapter.notifyDataSetChanged();

            for (int i = 0; i < arg0.getChildCount(); i++) {
                if (i == arg2) {
//					arg0.getChildAt(i).setBackgroundResource(
//							R.drawable.child_on);
                } else {
//					arg0.getChildAt(i).setBackgroundResource(
//							R.drawable.child_off);
                }
            }
            checkForPremimumService();

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

    private class GetChildrenAsyntask extends
            AsyncTask<List<NameValuePair>, Void, Boolean> {
        String error = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(mContext);
            dialog.setTitle(getResources().getString(R.string.children));
            dialog.setMessage(getResources().getString(R.string.please_wait));
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected Boolean doInBackground(List<NameValuePair>... params) {
            List<NameValuePair> nameValuePairs = params[0];
            JsonParser jParser = new JsonParser();
            JSONObject json = jParser.getJSONFromUrlnew(nameValuePairs,
                    Urls.api_parent_childrens);

            // Log parameters:
            Log.d("url extension", Urls.api_parent_childrens);
            String parameters = "";
            for (NameValuePair nvp : nameValuePairs) {
                parameters += nvp.getName() + "=" + nvp.getValue() + ",";
            }
            Log.d("Parameters: ", parameters);

            try {

                if (json != null) {
                    if (json.getString("result").equalsIgnoreCase("1")) {

                        JSONArray array = json.getJSONArray("data");
                        studentProfiles = new ArrayList<DataStructureFramwork.StudentProfileInfo>();
                        for (int i = 0; i < array.length(); i++) {
                            DataStructureFramwork.StudentProfileInfo studentProfile = new DataStructureFramwork.StudentProfileInfo(array.getJSONObject(i));
                            studentProfiles.add(studentProfile);
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
                e.printStackTrace();

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
                adapter = new ParentChildrenAdapter(mContext, studentProfiles);
                list.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                list.setOnItemClickListener(listener);
            } else {
                if (error.length() > 0) {
                    SchoolAppUtils.showDialog(mContext, "SwitchChild", error);
                } else {
                    SchoolAppUtils.showDialog(mContext, "SwitchChild", getResources().getString(R.string.error));
                }

            }
        }

    }

	/*OnHeadlineSelectedListener mCallback;

	// Container Activity must implement this interface
	public interface OnHeadlineSelectedListener {
		public void onArticleSelected(int position);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		// This makes sure that the container activity has implemented
		// the callback interface. If not, it throws an exception
		try {
			mCallback = (OnHeadlineSelectedListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnHeadlineSelectedListener");
		}
	}
*/

    public void checkForPremimumService() {
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
        nameValuePairs.add(new BasicNameValuePair("organization_id", SchoolAppUtils.GetSharedParameter(mContext, Constants.UORGANIZATIONID)));
        nameValuePairs.add(new BasicNameValuePair("student_id", SchoolAppUtils.GetSharedParameter(mContext, Constants.CHILD_ID)));

        new RequestPremiumServiceStatusAsyntask().execute(nameValuePairs);
    }

    private class RequestPremiumServiceStatusAsyntask extends AsyncTask<List<NameValuePair>, Void, Boolean> {
        String error = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(List<NameValuePair>... params) {
            List<NameValuePair> nameValuePairs = params[0];
            // Log parameters:
            Log.d("url extension: ", Urls.api_premium_service_status_check);
            String parameters = "";
            for (NameValuePair nvp : nameValuePairs) {
                parameters += nvp.getName() + "=" + nvp.getValue() + ",";
            }
            Log.d("Parameters: ", parameters);

            JsonParser jParser = new JsonParser();
            JSONObject json = jParser.getJSONFromUrlnew(nameValuePairs, Urls.api_premium_service_status_check);
            JSONArray dataArray = null;
            try {
                if (json != null) {
                    if (json.getString("result").equalsIgnoreCase("1")) {

                        try {
                            dataArray = json.getJSONArray("data");
                        } catch (Exception e) {
                            return true;
                        }
                        Log.d("Hellooo", dataArray.getJSONObject(0).getString("purchase_status").trim());
                        for (int i = 0; i < dataArray.length(); i++) {
                            statusVal = dataArray.getJSONObject(i).getString("purchase_status").trim();
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
            } catch (
                    JSONException e) {
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (result) {

                if (statusVal.equalsIgnoreCase("0")) {
                    //menuRightButton.setImageResource(R.drawable.swap);
                } else {
                    //  menuRightButton.setImageResource(R.drawable.graph_button_yes);

                }
            } else {
                if (error.length() > 0) {
                    SchoolAppUtils.showDialog(mContext, "SwitchChild", error);
                } else {
                    SchoolAppUtils.showDialog(mContext, "SwitchChild", getResources().getString(R.string.error));
                }

            }
        }
    }
}

