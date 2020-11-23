package com.blindchat.Activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.blindchat.R;
import com.blindchat.Utility.AppPref;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;


/**
 * Created by Gokul on 10-02-2018.
 */

public class SplashActivity extends AppCompatActivity {
    public void onCreate(Bundle save) {
        super.onCreate(save);
        setContentView(R.layout.activity_splash);
//        getSupportActionBar().hide();
        // Font path
//        String fontPath = "JMH Belicosa.otf";
//        TextView txtGhost = (TextView) findViewById(R.id.title);
//        Typeface tf = Typeface.createFromAsset(getAssets(), fontPath);
//        txtGhost.setTypeface(tf);
        FirebaseMessaging.getInstance().setAutoInitEnabled(true);


        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w("error", "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();

                        // Log and toast
                        //String msg = getString(R.string.msg_token_fmt, token);
                        Log.d("REFERN TOKEn", token);
                        AppPref.getInstance().setTOKEN(token);
//                        Toast.makeText(SplashActivity.this, token, Toast.LENGTH_SHORT).show();
                    }
                });

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(AppPref.getInstance().getUSERID().equalsIgnoreCase("")) {
                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
                else
                {
                    if(!AppPref.getInstance().getPASSCODE().equalsIgnoreCase(""))
                    {
                        Intent intent = new Intent(SplashActivity.this, LockActivity.class);
                        startActivity(intent);
                    }
                    else {
                        Intent intent = new Intent(SplashActivity.this, Dashboard.class);
                        startActivity(intent);
                    }
                }
                finish();
            }
        }, 2000);

    }
}
