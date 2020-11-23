package com.blindchat.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.blindchat.R;
import com.blindchat.Utility.AppPref;
import com.hanks.passcodeview.PasscodeView;

public class LockActivity extends AppCompatActivity {

    PasscodeView passcodeView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock);
        passcodeView = findViewById(R.id.passcodeView);
        passcodeView.setPasscodeLength(4);
        Intent intent = getIntent();
        String password = AppPref.getInstance().getPASSCODE();
//        Toast.makeText(LockActivity.this, password, Toast.LENGTH_SHORT).show();
        if (password != null) {
            passcodeView.setLocalPasscode(password)
                    .setListener(new PasscodeView.PasscodeViewListener() {
                        @Override
                        public void onFail() {
                            Toast.makeText(LockActivity.this, "PIN is incorrect", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onSuccess(String number) {
                            Toast.makeText(LockActivity.this, "PIN is Correct", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LockActivity.this, Dashboard.class);
                            startActivity(intent);
                            finish();
                        }
                    });
        }
    }
}