package com.knwedu.ourschool;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;

import com.knwedu.calcuttapublicschool.R;
import com.knwedu.ourschool.utils.Constants;
import com.knwedu.ourschool.utils.DownloadTask;
import com.knwedu.ourschool.utils.SchoolAppUtils;
import com.knwedu.ourschool.utils.Urls;

/**
 * Created by ddasgupta on 3/17/2017.
 */

public class ShowAttachmentActivity extends Activity {
    private String file_id, file_name,type,downloadUrl;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_attachment_view);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            file_id = extras.getString("id");
            file_name = extras.getString("file_name");
            type = extras.getString("type");
            Log.d("file_name>",file_name);


        }

        new AlertDialog.Builder(
                this)
                .setTitle("Select option")
                .setPositiveButton("View Document",
                        new DialogInterface.OnClickListener() {
                            public void onClick(
                                    DialogInterface dialog,
                                    int which) {
                                // continue with view

                                Log.d("type>",type);
                                if(type.equals("1")){

                                    downloadUrl = SchoolAppUtils.GetSharedParameter(ShowAttachmentActivity.this,
                                            Constants.COMMON_URL ) + Urls.api_ais_syllabus_downlaod + "/" + file_id;
                                    Log.d("downloadUrldoc",downloadUrl);
                                }else{
                                    downloadUrl = SchoolAppUtils.GetSharedParameter(ShowAttachmentActivity.this,
                                            Constants.COMMON_URL ) + Urls.api_ais_exam_download + "/" + file_id;
                                    Log.d("downloadUrldoc",downloadUrl);
                                }



                                SchoolAppUtils
                                        .imagePdfViewDocument(
                                                ShowAttachmentActivity.this,
                                                downloadUrl,file_name);
                            }
                        })
                .setNegativeButton("Download",
                        new DialogInterface.OnClickListener() {
                            public void onClick(
                                    DialogInterface dialog,
                                    int which) {
                                // download
                                if(type.equals("1")){
                                    downloadUrl = SchoolAppUtils.GetSharedParameter(ShowAttachmentActivity.this,
                                            Constants.COMMON_URL ) + Urls.api_ais_syllabus_downlaod + "/" + file_id;
                                    Log.d("downloadUrl",downloadUrl);
                                }else{
                                    downloadUrl = SchoolAppUtils.GetSharedParameter(ShowAttachmentActivity.this,
                                            Constants.COMMON_URL ) + Urls.api_ais_exam_download + "/" + file_id;
                                }
                                final DownloadTask downloadTask = new DownloadTask(ShowAttachmentActivity.this, file_name);
                                downloadTask.execute(downloadUrl);
                                Log.d("downloadUrl",downloadUrl);

                            }
                        })
                .setIcon(android.R.drawable.ic_dialog_info).show();
    }
}
