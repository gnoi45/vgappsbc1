package com.blindchat.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.blindchat.R;
import com.blindchat.Utility.ApiConsumer;
import com.blindchat.Utility.ApiResponse;
import com.blindchat.Utility.AppPref;
import com.blindchat.Utility.AppUrl;

import org.json.JSONObject;


public class EditProfile extends BaseActivity
{
    EditText edt_name, edt_email, edt_mobile;
    Button update_profile;
    @Override
    public void initialize(Bundle save) {
        setTitle("Edit Profile");
        edt_name = findViewById(R.id.edt_name);
        edt_email = findViewById(R.id.edt_email);
        edt_mobile = findViewById(R.id.edt_mobile);
        update_profile = findViewById(R.id.btn_update_profile);

        edt_name.setText(AppPref.getInstance().getNAME());
        edt_email.setText(AppPref.getInstance().getEMAIL());
        edt_mobile.setText(AppPref.getInstance().getMOBILE());

        update_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editDetails();
            }
        });


    }

    private void editDetails() {
        if (edt_name.getText().toString().equalsIgnoreCase("")) {
            edt_name.setError("Please Enter Name");
            return;
        }
        try {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("name=").append(edt_name.getText().toString());
            stringBuilder.append("&id=").append(AppPref.getInstance().getUSERID());

            String content = stringBuilder.toString();

            ApiConsumer apiConsumer = new ApiConsumer(EditProfile.this, AppUrl.EDIT_PROFILE, 1, content, true, "loading ...", new ApiResponse() {
                @Override
                public void getApiResponse(String responseData, int serviceCounter) {
                    try {
                        Log.d("responseData", responseData);
                        JSONObject jsonObject = new JSONObject(responseData);

                        String msg = jsonObject.getString("message");
                        AppPref.getInstance().setNAME(edt_name.getText().toString());
                        toastMessage(msg);
                        finish();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            apiConsumer.execute();
        } catch (Exception e) {
            toastMessage("Some Error Occurred, Please Try again");
            e.printStackTrace();
        }
    }

    private void alert()
    {
        AlertDialog.Builder adb=new AlertDialog.Builder(this);
        adb.setTitle("Success");
        adb.setMessage("Profile Updated Successfully");
        adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        adb.show();
    }

    @Override
    public int getActivityLayout() {
        return R.layout.dialog_editprofile;
    }

}
