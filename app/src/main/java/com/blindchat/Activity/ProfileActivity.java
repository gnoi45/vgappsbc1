package com.blindchat.Activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blindchat.R;
import com.blindchat.Utility.AppPref;
import com.squareup.picasso.Picasso;

import java.util.concurrent.locks.Lock;


public class ProfileActivity extends BaseActivity implements View.OnClickListener
{
    @Override
    public void initialize(Bundle save) {
        setTitle("Profile");

        TextView name=(TextView) findViewById(R.id.name);
        TextView email=(TextView) findViewById(R.id.email);

        name.setText(AppPref.getInstance().getNAME());
        email.setText(AppPref.getInstance().getEMAIL());

        TextView edit_profile=(TextView) findViewById(R.id.edit_profile);
        TextView change_password=(TextView) findViewById(R.id.change_password);
        TextView logout=(TextView) findViewById(R.id.logout);
        TextView digital_lock=(TextView) findViewById(R.id.digital_lock);

        edit_profile.setOnClickListener(this);
        change_password.setOnClickListener(this);
        digital_lock.setOnClickListener(this);
        logout.setOnClickListener(this);

        ImageView imageView=(ImageView) findViewById(R.id.photo);
        if(!AppPref.getInstance().getPHOTO().equalsIgnoreCase(""))
        {
            Picasso.with(this).load("https://earnezy.in/blind/uploads/"+AppPref.getInstance().getPHOTO()).into(imageView);
        }
    }

    @Override
    public int getActivityLayout() {
        return R.layout.fragment_profile;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.edit_profile:
                sendToActivity(EditPrefernces.class);
                break;
            case R.id.logout:
                logout();
                break;
            case R.id.change_password:
                sendToActivity(ChangePassword.class);
                break;
            case R.id.digital_lock:
                sendToActivity(LockPassword.class);
                break;
        }
    }

    private void logout()
    {
        AlertDialog.Builder adb=new AlertDialog.Builder(this);
        adb.setTitle("Logout");
        adb.setMessage("Are you sure you want to logout?");
        adb.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                AppPref.getInstance().setUSERID("");
                AppPref.getInstance().setNAME("");
                AppPref.getInstance().setMOBILE("");
                AppPref.getInstance().setEMAIL("");
                AppPref.getInstance().setPHOTO("");
                AppPref.getInstance().setPASSCODE("");
                sendToActivity(LoginActivity.class, Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            }
        });
        adb.setNegativeButton("NO",null);
        adb.show();
    }
}
