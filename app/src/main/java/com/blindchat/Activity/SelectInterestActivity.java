package com.blindchat.Activity;

import android.os.Bundle;

import com.blindchat.R;

public class SelectInterestActivity extends BaseActivity
{
    @Override
    public void initialize(Bundle save) {
        setTitle("Select Interest");
    }

    @Override
    public int getActivityLayout() {
        return R.layout.activity_select_interest;
    }
}
