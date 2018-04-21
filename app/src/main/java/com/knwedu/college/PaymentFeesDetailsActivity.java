package com.knwedu.college;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

import com.knwedu.calcuttapublicschool.R;

public class PaymentFeesDetailsActivity extends Activity {

    /*private TableLayout table_layout;

    private TextView tvName, tvClass, tvDueDate, lblCommission, lblConvenience, lblTotalPaid;

    private ProgressDialog dialog;

    private TextView header;

    private String page_title = "";

    private String Url;

    private int type;

    private ArrayList<PaymentReminderDeatils> paymentReminder = new ArrayList<PaymentReminderDeatils>();

    private ArrayList<GatewayDetails> gatewayDetails = new ArrayList<GatewayDetails>();

    private ArrayList<PayUMoneyDetails> paymoneydetails = new ArrayList<PayUMoneyDetails>();

    private ArrayList<PayTMDetails> paytmdetails = new ArrayList<PayTMDetails>();

    private ArrayList<PayUMoneyResponse> payumoneyresponses = new ArrayList<PayUMoneyResponse>();

    private ArrayList<Bitmap> bitmaps = new ArrayList<Bitmap>();

    private ListView reminderList;

    private PaymentReminderAdapter adapter;

    private TableLayout t1;

    private String fees_total, fine_total, total_amount, scholarship_amount;

    private String payt_STATUS;

    private String checksum_hash;

    private String reminderId;

    private String orderId;

    private LinearLayout llPaymentGateways;

    private HashMap<String, String> params = new HashMap<>();

    private static final String TAG = "PayUMoneySDK ";

    private RadioGroup rg;

    private Button btnPayment;

    private int payment_type;

    private Intent payUMoneyResponse;

    private PayTMResponse payTMResponse = new PayTMResponse();

    private int class_id;
    */

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

    private void requetsForChecksumForPayTM() {


    }

    private void requetsForChecksumForPayUMoney(String id) {




    }

    private void requetsForPayTMDetails() {



    }

    private void sendingPayumoneyResponse(int paymentType) {


    }

    /**
     * Urls.api_payment_generate_checksum =
     * "mobile/feePaymentGenerateChecksumMobile"
     *
     * @author Rajhrita
     */



    /**
     * Urls.api_payment_generate_checksum_payumoney =
     * "mobile/getpayumoney"
     *
     * @author Rajhrita
     */



}