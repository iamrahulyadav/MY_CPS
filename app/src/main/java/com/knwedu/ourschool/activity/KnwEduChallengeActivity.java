package com.knwedu.ourschool.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.knwedu.calcuttapublicschool.R;
import com.knwedu.util.AlertDialogCallBack;
import com.knwedu.util.Util;

/**
 * Created by knowalladmin on 15/12/17.
 */

public class KnwEduChallengeActivity extends AppCompatActivity {

    private WebView wv_mindhour_test;
    ProgressDialog mDialog;
    private Context mContext;

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
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.fragment_mindhour_test);
        mContext = KnwEduChallengeActivity.this;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("KnwEdu Challenge");
        //wv_mindhour_test.loadUrl("http://deferred-live.net/onlinetest/quizzes/physics-101-quiz/");
        //wv_mindhour_test.loadUrl("http://google.com");

        String challengeUrl = getIntent().getStringExtra("challengeUrl");

        mDialog = new ProgressDialog(mContext);
        mDialog.setCancelable(true);
        mDialog.setMessage("Loading! Please wait..");
        mDialog.setTitle("KnwEdu Challenge");

        wv_mindhour_test = (WebView) findViewById(R.id.wv_mindhour_test);
        wv_mindhour_test.getSettings().setDomStorageEnabled(true);
        wv_mindhour_test.getSettings().setSaveFormData(true);
        wv_mindhour_test.getSettings().setAllowContentAccess(true);
        wv_mindhour_test.getSettings().setAllowFileAccess(true);
        wv_mindhour_test.getSettings().setAllowFileAccessFromFileURLs(true);
        wv_mindhour_test.getSettings().setAllowUniversalAccessFromFileURLs(true);
        wv_mindhour_test.getSettings().setSupportZoom(true);
        wv_mindhour_test.getSettings().setJavaScriptEnabled(true);
        wv_mindhour_test.getSettings().setSupportMultipleWindows(true);
        wv_mindhour_test.setWebChromeClient(new WebChromeClient());
        wv_mindhour_test.setWebViewClient(new myWebClient());
        wv_mindhour_test.setClickable(true);
        // wv_mindhour_test.loadUrl("http://deferred-live.net/onlinetest/quizzes/physics-101-quiz/");
        //wv_mindhour_test.loadUrl("http://www.google.co.in");
        wv_mindhour_test.loadUrl("" + challengeUrl);

    }


    // ====================================
    // ===== WEB CLIENT ===================

    private class myWebClient extends WebViewClient {

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {

            Log.v("URL", "" + url);

            mDialog.show();
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            return super.shouldOverrideUrlLoading(view, url);

        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            mDialog.dismiss();
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            mDialog.dismiss();

        }
    }

    @Override
    public void onBackPressed() {

        Util.showCallBackMessageWithOkCancel(mContext, "Are you sure you want to exit?", new AlertDialogCallBack() {
            @Override
            public void onSubmit() {
                KnwEduChallengeActivity.super.onBackPressed();
            }

            @Override
            public void onCancel() {

            }
        }, "KnwEdu Challenge");

    }
}
