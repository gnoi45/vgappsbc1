package com.blindchat.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


//import com.razorpay.Checkout;
//import com.razorpay.PaymentResultListener;

import com.blindchat.R;
import com.blindchat.Utility.AppPref;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

public class BcCredits extends BaseActivity implements View.OnClickListener, PaymentResultListener
{
    private TextView balance;
    private TextView txt_1500;
    private TextView txt_2000;
    private TextView txt_2500;

    private EditText edt_wallet;

    private Button btn_add_money;

    @Override
    public void initialize(Bundle save) {
        setTitle("Add Credits");
        balance=(TextView) findViewById(R.id.balance);
        edt_wallet=(EditText) findViewById(R.id.edt_wallet);

        btn_add_money=(Button) findViewById(R.id.btn_add_money);

        txt_1500=(TextView) findViewById(R.id.txt_1500);
        txt_2000=(TextView) findViewById(R.id.txt_2000);
        txt_2500=(TextView) findViewById(R.id.txt_2500);






        txt_2500.setOnClickListener(this);
        txt_1500.setOnClickListener(this);
        txt_2000.setOnClickListener(this);

        btn_add_money.setOnClickListener(this);

        balance.setText(AppPref.getInstance().getPOINTS()+" BC");

      //  Checkout.preload(getApplicationContext());

    }

    @Override
    public int getActivityLayout() {
        return R.layout.fragment_wallet;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txt_1500:
                edt_wallet.setText("1500");
                txt_1500.setBackground(getResources().getDrawable(R.drawable.rounded_corner_like));
                txt_1500.setTextColor(getResources().getColor(R.color.white));
                txt_2500.setTextColor(getResources().getColor(R.color.home_text_color));
                txt_2000.setTextColor(getResources().getColor(R.color.home_text_color));
                txt_2000.setBackground(getResources().getDrawable(R.drawable.rounded_corner_grey));
                txt_2500.setBackground(getResources().getDrawable(R.drawable.rounded_corner_grey));
                break;
            case R.id.txt_2000:
                edt_wallet.setText("2000");
                txt_1500.setTextColor(getResources().getColor(R.color.home_text_color));
                txt_2500.setTextColor(getResources().getColor(R.color.home_text_color));
                txt_2000.setTextColor(getResources().getColor(R.color.white));
                txt_1500.setBackground(getResources().getDrawable(R.drawable.rounded_corner_grey));
                txt_2000.setBackground(getResources().getDrawable(R.drawable.rounded_corner_like));
                txt_2500.setBackground(getResources().getDrawable(R.drawable.rounded_corner_grey));
                break;
            case R.id.txt_2500:
                edt_wallet.setText("2500");
                txt_1500.setTextColor(getResources().getColor(R.color.home_text_color));
                txt_2500.setTextColor(getResources().getColor(R.color.white));
                txt_2000.setTextColor(getResources().getColor(R.color.home_text_color));
                txt_1500.setBackground(getResources().getDrawable(R.drawable.rounded_corner_grey));
                txt_2000.setBackground(getResources().getDrawable(R.drawable.rounded_corner_grey));
                txt_2500.setBackground(getResources().getDrawable(R.drawable.rounded_corner_like));
                break;
            case R.id.btn_add_money:
                startPayment();
                break;
        }
    }

    private void startPayment()
    {
        String money=edt_wallet.getText().toString();
        double dm=Double.parseDouble(money);
        startPayment(10,"123");
    }





  public void startPayment(double amount,String order_id) {
        Checkout checkout = new Checkout();

        final Activity activity = this;

        try {
            JSONObject options = new JSONObject();

            options.put("name", "Merchant Name");

            options.put("description", "Reference No. #123456");
            options.put("currency", "INR");


            options.put("amount", "100");

            checkout.open(activity, options);
        } catch(Exception e) {
            Log.e("TAG", "Error in starting Razorpay Checkout", e);
        }
    }
    @Override
    public void onPaymentSuccess(String s) {
        toastMessage("Success");
    }

    @Override
    public void onPaymentError(int i, String s) {
        toastMessage(s);
    }

}
