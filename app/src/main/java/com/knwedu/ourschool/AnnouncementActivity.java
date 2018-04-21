package com.knwedu.ourschool;


import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.analytics.tracking.android.EasyTracker;
import com.knwedu.calcuttapublicschool.R;
import com.knwedu.ourschool.db.DatabaseAdapter;
import com.knwedu.ourschool.db.SchoolApplication;
import com.knwedu.ourschool.utils.Constants;
import com.knwedu.ourschool.utils.DataStructureFramwork.Announcement;
import com.knwedu.ourschool.utils.DownloadTask;
import com.knwedu.ourschool.utils.SchoolAppUtils;
import com.knwedu.ourschool.utils.Urls;


public class AnnouncementActivity extends AppCompatActivity {
    private TextView title, date, description, header;
    private Announcement announcement;
    private Button getDocBtn;
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
        setContentView(R.layout.activity_announcement);
        mDatabase = ((SchoolApplication) getApplication()).getDatabase();
        internetAvailable = getIntent().getBooleanExtra(Constants.IS_ONLINE, true);
        page_title = getIntent().getStringExtra(Constants.PAGE_TITLE);
        SchoolAppUtils.loadAppLogo(AnnouncementActivity.this, (ImageButton) findViewById(R.id.app_logo));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("" + page_title);
        initialize();
    }

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
    }

    private void initialize() {
        title = (TextView) findViewById(R.id.title_txt);
        date = (TextView) findViewById(R.id.assignment_due_txt);
        description = (TextView) findViewById(R.id.assignment_txt);
        header = (TextView) findViewById(R.id.header_txt);
        header.setVisibility(View.GONE);
        getDocBtn = (Button) findViewById(R.id.download_btn);
        if ((SchoolAppUtils.GetSharedParameter(AnnouncementActivity.this,
                Constants.USERTYPEID).equalsIgnoreCase(
                Constants.USERTYPE_STUDENT)) || SchoolAppUtils.GetSharedParameter(AnnouncementActivity.this,
                Constants.USERTYPEID).equalsIgnoreCase(
                Constants.USERTYPE_PARENT)) {
            if (getIntent().getExtras() != null) {
                //if (internetAvailable) {
                String temp = getIntent().getExtras().getString(
                        Constants.ANNOUNCEMENT);
                if (temp != null) {
                    JSONObject object = null;
                    try {
                        object = new JSONObject(temp);
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    if (object != null) {
                        announcement = new Announcement(object);
                    }
                }
            /*}else{
                int row_id = getIntent().getIntExtra(Constants.OFFLINE_ANNOUNCEMENT_ROWID, 0);
				mDatabase.open();
				announcement = mDatabase.getAnnouncement(row_id, 2);
				mDatabase.close();
			}*/
            }
        } else {
            if (internetAvailable) {
                String temp = getIntent().getExtras().getString(
                        Constants.ANNOUNCEMENT);
                if (temp != null) {
                    JSONObject object = null;
                    try {
                        object = new JSONObject(temp);
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    if (object != null) {
                        announcement = new Announcement(object);
                    }
                }
            } else {
                int row_id = getIntent().getIntExtra(Constants.OFFLINE_ANNOUNCEMENT_ROWID, 0);
                mDatabase.open();
                announcement = mDatabase.getAnnouncement(row_id, 2);
                mDatabase.close();
            }
        }

        if (announcement != null) {
            title.setText(announcement.title);
            date.setText(announcement.date.replace("-", "/"));
            description.setText(announcement.description);
            if (!announcement.file_name.equals("null")
                    && !announcement.attachment.equals("null") && internetAvailable) {
                getDocBtn.setVisibility(View.VISIBLE);
                getDocBtn.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        new AlertDialog.Builder(
                                AnnouncementActivity.this)
                                .setTitle("Select option")
                                .setPositiveButton("View Document",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(
                                                    DialogInterface dialog,
                                                    int which) {
                                                // continue with view
                                                SchoolAppUtils
                                                        .imagePdfViewDocument(
                                                                AnnouncementActivity.this,
                                                                Urls.api_get_announcement_classwork_doc
                                                                        + announcement.id, announcement.file_name);
                                            }
                                        })
                                .setNegativeButton("Download",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(
                                                    DialogInterface dialog,
                                                    int which) {
                                                // download

                                                final DownloadTask downloadTask = new DownloadTask(AnnouncementActivity.this, announcement.file_name);
                                                downloadTask.execute(Urls.api_get_announcement_classwork_doc + announcement.id);

												

												/*Intent i = new Intent(
                                                        Intent.ACTION_VIEW);
												i.setData(Uri
														.parse(Urls.api_get_announcement_classwork_doc
																+ announcement.id));
												startActivity(i);*/

                                            }
                                        })
                                .setIcon(android.R.drawable.ic_dialog_info).show();


                        ;
                    }
                });
            } else {
                getDocBtn.setVisibility(View.GONE);
            }
			/*if (announcement.file_name != null) {
				if (announcement.attachment.length() > 0) {
					getDocBtn.setVisibility(View.VISIBLE);
					getDocBtn.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							Intent i = new Intent(Intent.ACTION_VIEW);
							i.setData(Uri.parse(Urls.api_get_announcement_classwork_doc
									+ announcement.id));
							startActivity(i);
						}
					});
				} 
				else
				{
					getDocBtn.setVisibility(View.GONE);
				}
			}*/

        }
    }
}
