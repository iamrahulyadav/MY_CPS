package com.knwedu.ourschool;

import com.google.analytics.tracking.android.EasyTracker;
import com.knwedu.college.utils.CollegeConstants;
import com.knwedu.calcuttapublicschool.R;
import com.knwedu.ourschool.utils.DownloadTask;
import com.knwedu.ourschool.utils.SchoolAppUtils;
import com.knwedu.ourschool.utils.Urls;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.net.http.HttpResponseCache;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class SyllabusDetailsActivity extends AppCompatActivity {

    private ProgressDialog dialog;
    private TextView header;
    private String page_title = "";
    private String Url;

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
        setContentView(R.layout.syllabus_details_view);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Syllabus");
        page_title = getIntent().getStringExtra(CollegeConstants.PAGE_TITLE);
        header = (TextView) findViewById(R.id.header_text);
        Button btnGotDoc = (Button) findViewById(R.id.download_btn);
        if (!getIntent().getStringExtra("sub_attachment").isEmpty()) {
            btnGotDoc.setVisibility(View.VISIBLE);
        } else {
            btnGotDoc.setVisibility(View.GONE);
        }

        btnGotDoc.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String syllabus = "syllabus";
                /**
                 * https://knweducollege.knwedu.com/mobile/download/2097/9/Syllabus
                 * CollegeUrls.api_get_doc = mobile/download
                 * id = 2097 (user_id)
                 * syllabus_id = getIntent().getStringExtra("syl_id")
                 * syllabus_name = Syllabus
                 */
                Url = Urls.base_url
                        + "/mobile/downloadSyllabus/"
                        + getIntent().getStringExtra("sub_id");
                System.out.println("URL>>>>>>" + Url);
                showGetDocDialog();
            }
        });

        header.setText(page_title);
        initialize();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    ;

    private void showGetDocDialog() {
        new AlertDialog.Builder(SyllabusDetailsActivity.this)
                .setTitle("Select option")
                .setPositiveButton("View Document",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                // continue with view
                                SchoolAppUtils
                                        .imagePdfViewDocument(
                                                SyllabusDetailsActivity.this,
                                                Url, getIntent()
                                                        .getStringExtra("sub_attachment"));
                            }
                        })
                .setNegativeButton("Download",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                // download
                                final DownloadTask downloadTask = new DownloadTask(
                                        SyllabusDetailsActivity.this,
                                        getIntent()
                                                .getStringExtra("sub_attachment"));
                                downloadTask.execute(Url);

                            }
                        }).setIcon(android.R.drawable.ic_dialog_info).show();
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

        TextView textViewProgram = (TextView) findViewById(R.id.textViewTitle);
        textViewProgram.setText(getIntent().getStringExtra("sub_title"));
        TextView textViewSession = (TextView) findViewById(R.id.textViewDesc);
        textViewSession.setText(getIntent().getStringExtra("sub_desc"));
    }

}
