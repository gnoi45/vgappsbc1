package com.blindchat.Utility;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.blindchat.R;


public class ApiConsumer extends AsyncTask<String, String, String> {

    private Activity activity;
    private String content;
    private Dialog cProgressDialog;
    private String url;
    private ApiResponse apiResponse;
    private int serviceCounter;
    private String message = "Processing...";
    private boolean isShowProgress = true;
    private String responseData;


    public ApiConsumer(Activity activity, String url, int serviceCounter, String content, boolean isShowProgress, String message, ApiResponse apiResponse) {
        this.activity = activity;
        this.content = content;
        this.url = url;
        this.serviceCounter = serviceCounter;
        this.isShowProgress = isShowProgress;
        this.message = message;
        this.apiResponse = apiResponse;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (isShowProgress) {
            cProgressDialog = new Dialog(activity);
            cProgressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            cProgressDialog.setContentView(R.layout.loader);
            cProgressDialog.setCanceledOnTouchOutside(false);
            cProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();

            Window window = cProgressDialog.getWindow();
            lp.copyFrom(window.getAttributes());
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(lp);
            cProgressDialog.show();
        }
    }


    @Override
    protected String doInBackground(String... params) {
        if (!isNetworkAvailable(activity)) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(activity,"No Internet Connecgtions", Toast.LENGTH_LONG).show();
                }
            });
        } else {
            responseData = WebService.Web_FetchData(url, content);
        }
        return responseData;
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        if (isShowProgress)
            cProgressDialog.dismiss();
        apiResponse.getApiResponse(result, serviceCounter);
    }
}
