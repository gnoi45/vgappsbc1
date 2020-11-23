package com.blindchat.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.blindchat.R;
import com.blindchat.Utility.AppPref;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.mukesh.OtpView;

public class LockPassword extends AppCompatActivity {

    EditText pwd;
    FloatingActionButton floatingActionButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock_password);
        pwd = (EditText) findViewById(R.id.password);
        floatingActionButton = (FloatingActionButton) findViewById(R.id.fab);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pwd.getText().toString().isEmpty()){
                    Toast.makeText(LockPassword.this, "Passcode is empty", Toast.LENGTH_SHORT).show();
                }
                else if (pwd.getText().toString().length() != 4){
                    Toast.makeText(LockPassword.this, "Passcode can contain only 4 digits", Toast.LENGTH_SHORT).show();
                }
                else{
                    String password = pwd.getText().toString();
                    AppPref.getInstance().setPASSCODE(password);
//                    Intent intent = new Intent(LockPassword.this, LockActivity.class);
//                    intent.putExtra("passcode", password);
//                    startActivity(intent);
                    finish();
                }
            }
        });
    }
}