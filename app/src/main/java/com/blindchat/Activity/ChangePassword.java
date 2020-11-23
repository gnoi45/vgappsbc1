package com.blindchat.Activity;

import android.os.Bundle;

import com.blindchat.R;


public class ChangePassword extends BaseActivity
{
    @Override
    public void initialize(Bundle save) {
        setTitle("Change Password");
    }

    @Override
    public int getActivityLayout() {
        return R.layout.dialog_change_password;
    }
}
