package com.blindchat.Notification;

import android.util.Log;


import com.blindchat.Utility.AppPref;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class MyFirebaseInstanceService extends FirebaseInstanceIdService {

    private static final String TAG="MyFirebaseInstanceServi";

    @Override
    public void onTokenRefresh() {

     String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        //FirebaseMessaging.getInstance().subscribeToTopic("all");
        Log.d(TAG, "Refreshed token: " + refreshedToken);

/* If you want to send messages to this application instance or manage this apps subscriptions on the server side, send the Instance ID token to your app server.*/

        sendRegistrationToServer(refreshedToken);
    }

    private void sendRegistrationToServer(String refreshedToken) {
        Log.d("TOKEN ", refreshedToken.toString());
        AppPref.getInstance().setTOKEN(refreshedToken);
    }
}
