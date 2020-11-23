package com.blindchat.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.blindchat.R;
import com.blindchat.Utility.ApiConsumer;
import com.blindchat.Utility.ApiResponse;
import com.blindchat.Utility.AppPref;
import com.blindchat.Utility.AppUrl;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    TextView newAccount;
    EditText email, phone;
    Button signIn;
    LoginButton loginButton;
    String first_name, last_name, email_id ,id,image_url;
    private CallbackManager callbackManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        newAccount = (TextView) findViewById(R.id.new_account);
        email = (EditText) findViewById(R.id.email);
        signIn = (Button) findViewById(R.id.signInButton);
        loginButton = (LoginButton) findViewById(R.id.facebookLogIn);
        phone = (EditText) findViewById(R.id.phone);
        callbackManager = CallbackManager.Factory.create();

        newAccount.setOnClickListener(this);
        signIn.setOnClickListener(this);
        loginButton.setReadPermissions(Arrays.asList("email", "public_profile"));
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Intent intent = new Intent(LoginActivity.this, Dashboard.class);
                intent.putExtra("first_name", first_name);
                intent.putExtra("last_name", last_name);
                intent.putExtra("email", email_id);
                intent.putExtra("image_url", image_url);
                startActivity(intent);
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });

    }

    private void loadUserProfile(AccessToken accessToken) {
        GraphRequest graphRequest = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                try {
                    first_name = object.getString("first_name");
                    last_name = object.getString("last_name");
                    email_id = object.getString("email");
                    id = object.getString("id");
                    image_url = "https://graph.facebook.com/" + id + "/picture?type=normal";

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "first_name,last_name,email_id");
        graphRequest.setParameters(parameters);
        graphRequest.executeAsync();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    AccessTokenTracker accessTokenTracker = new AccessTokenTracker() {
        @Override
        protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
            if (currentAccessToken == null){
                Toast.makeText(LoginActivity.this, "Logged out", Toast.LENGTH_SHORT).show();
            }
            else {
                loadUserProfile(currentAccessToken);
            }
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.new_account:
                sendToActivity(RegisterActivity.class);
                break;
            case R.id.signInButton:
                logIn();
                break;
        }
    }

    private void sendToActivity(Class className) {
        Intent intent = new Intent(this, className);
        startActivity(intent);
    }

    private void logIn() {
        if(email.getText().toString().equalsIgnoreCase(""))
        {
          Toast.makeText(this,"Please Enter Email or Mobile",Toast.LENGTH_LONG).show();
          return;
        }
        StringBuilder stringBuilder=new StringBuilder();
        stringBuilder.append("email=").append(email.getText().toString());
        stringBuilder.append("&token=").append(AppPref.getInstance().getTOKEN());

        String content=stringBuilder.toString();

        ApiConsumer apiConsumer=new ApiConsumer(this, AppUrl.LOGIN_URL, 0, content, true, "Loading ...", new ApiResponse() {
            @Override
            public void getApiResponse(String responseData, int serviceCounter) {
                try
                {
                    JSONObject jsonObject=new JSONObject(responseData);
                    String id=jsonObject.getString("id");
                    String name=jsonObject.getString("name");
                    String mobile=jsonObject.getString("mobile");
                    String email=jsonObject.getString("email");
                    String photo=jsonObject.getString("photo");
                    String points=jsonObject.getString("blind_credit");
                    AppPref.getInstance().setUSERID(id);
                    AppPref.getInstance().setEMAIL(email);
                    AppPref.getInstance().setMOBILE(mobile);
                    AppPref.getInstance().setNAME(name);
                    AppPref.getInstance().setUNIQID("BC000"+id);
                    AppPref.getInstance().setPHOTO(photo);
                    AppPref.getInstance().setPOINTS(points);
                    Intent intent = new Intent(LoginActivity.this, OtpActivity.class);
                    startActivity(intent);
                    finish();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
        apiConsumer.execute();
    }
}