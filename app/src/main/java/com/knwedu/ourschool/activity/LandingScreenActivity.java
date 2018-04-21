package com.knwedu.ourschool.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.knwedu.calcuttapublicschool.R;
import com.knwedu.constant.Consts;
import com.knwedu.model.UserClass;
import com.knwedu.ourschool.SigninActivity;
import com.knwedu.util.Util;
import com.knwedu.volley.PostWithJsonWebTask;
import com.knwedu.volley.ServerResponseStringCallback;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by knowalladmin on 05/01/18.
 */

public class LandingScreenActivity extends Activity {

    private static String challengeUrl = "";
    private Button btn_knwedu_challenge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_screen);
        btn_knwedu_challenge = (Button) findViewById(R.id.btn_knwedu_challenge);

        PostWithJsonWebTask.callPostWithJsonWebtask(LandingScreenActivity.this, Consts.GET_LANDING_PAGE_URL, new HashMap<String, String>(), new ServerResponseStringCallback() {
            @Override
            public void onSuccess(String resultJsonObject) {
                Log.e("OnSuccess", "Result: " + resultJsonObject);
                try {
                    JSONObject jsonObject = new JSONObject(resultJsonObject);
                    if (jsonObject.optString("result").trim().equalsIgnoreCase("1")) {
                        challengeUrl = jsonObject.optString("data").trim();
                        if (challengeUrl != null && challengeUrl.length() > 0) {


                            showChallengeButton();
                        } else {
                            hideChallengeButton();
                        }
                    } else {
                        hideChallengeButton();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    hideChallengeButton();
                }
            }

            @Override
            public void ErrorMsg(VolleyError error) {

            }
        }, true, Request.Method.POST);

    }

    public void onConnectInstituteClick(View v) {
        startActivity(new Intent(LandingScreenActivity.this, SigninActivity.class));

    }

    public void onKnwEduChallengeClick(View v) {
        /*Intent intent = new Intent(LandingScreenActivity.this, KnwEduChallengeActivity.class);
        intent.putExtra("challengeUrl", "" + challengeUrl);
        startActivity(intent);*/

        String url = "" + challengeUrl;
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);

    }

    private void hideChallengeButton() {
        UserClass userClass = Util.fetchUserClass(LandingScreenActivity.this);
        if (userClass == null) {
            userClass = new UserClass();
        }
        userClass.challengeUrl = "";
        Util.saveUserClass(LandingScreenActivity.this, userClass);
        btn_knwedu_challenge.setVisibility(View.GONE);
    }

    private void showChallengeButton() {
        UserClass userClass = Util.fetchUserClass(LandingScreenActivity.this);
        if (userClass == null) {
            userClass = new UserClass();
        }
        userClass.challengeUrl = challengeUrl;
        Util.saveUserClass(LandingScreenActivity.this, userClass);
        btn_knwedu_challenge.setVisibility(View.VISIBLE);
    }
}
