package com.knwedu.ourschool;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.TextView;

import com.knwedu.calcuttapublicschool.R;
import com.knwedu.ourschool.utils.SchoolAppUtils;
import com.knwedu.ourschool.utils.Urls;

public class PrivacyPolicyActivity extends AppCompatActivity {
    int touchCount = 0;
    private WebView webView;
    ProgressDialog mProgressDialog;

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
        setContentView(R.layout.activity_privacy_policy);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Privacy Policy");
        initialize();
    }

    private void initialize() {
        mProgressDialog = new ProgressDialog(this);
        TextView header_text = (TextView) findViewById(R.id.header_text);
        header_text.setVisibility(View.GONE);
        //header_text.setText("Privacy Policy");
        webView = (WebView) findViewById(R.id.webView_to_show);
        webView.getSettings().setJavaScriptEnabled(true);

        if (SchoolAppUtils.isOnline(this)) {
            webView.loadUrl(Urls.api_privacy_policy);
        } else {
            SchoolAppUtils.showDialog(this,
                    getResources().getString(R.string.privacy), "No Network Connectivity.");
        }
        webView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                touchCount += 1;
                return false;
            }
        });
        webView.setWebViewClient(new WebViewClient() {

            public void onPageFinished(WebView view, String url) {
                mProgressDialog.dismiss();
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                mProgressDialog.setMessage("Loading..");
                mProgressDialog.show();
            }
        });

    }

}

