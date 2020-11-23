package com.blindchat;

import android.app.Application;

public class BlindChatApplications extends Application {
    private static BlindChatApplications instance;


    public void onCreate()
    {
        super.onCreate();
        instance = this;
    }

    public static BlindChatApplications getInstance() {
        if (instance == null) {
            instance = new BlindChatApplications();
        }
        return instance;
    }

}
