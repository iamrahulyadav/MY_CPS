package com.knwedu.ourschool;
import android.app.Activity;
import android.view.View;
import android.widget.Button;

import android.app.Dialog;
import android.os.Bundle;
import android.view.Window;

import com.knwedu.calcuttapublicschool.R;


/**
 * Created by nosyworld on 10/05/17.
 */

public class showPopupNew extends Dialog implements
        android.view.View.OnClickListener {

    public Activity c;
    public Dialog d;
    public Button yes, no;

    public showPopupNew(Activity a) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.popup_layout);
        //yes = (Button) findViewById(R.id.btn_yes);
        no = (Button) findViewById(R.id.btndismissxml);
        //yes.setOnClickListener(this);
        no.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            /*case R.id.btn_yes:
                c.finish();
                break;*/
            case R.id.btndismissxml:
                dismiss();

                break;
            default:
                break;
        }
        dismiss();
    }
}

