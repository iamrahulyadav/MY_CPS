package com.knwedu.ourschool;

import com.knwedu.calcuttapublicschool.R;


import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

public class PaymentFeesDetailsActivity extends Activity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.reminder_details_activity);



    }

    /**
     * Calling
     * https://kolkataschool.knwedu.com/mobile/feePaymentGenerateChecksumMobile
     * by passing the following parameters
     */


}