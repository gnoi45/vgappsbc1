package com.blindchat.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.blindchat.R;
import com.mukesh.OtpView;

public class OtpActivity extends AppCompatActivity implements View.OnClickListener
{
    private OtpView otp_view;

    private Button validate_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        otp_view=(OtpView) findViewById(R.id.otp_view);

        validate_btn=(Button) findViewById(R.id.validate_btn);
        validate_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.validate_btn:
                    Intent intent = new Intent(this, Dashboard.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                break;
        }
    }
}