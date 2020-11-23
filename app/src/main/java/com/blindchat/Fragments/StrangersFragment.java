package com.blindchat.Fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.blindchat.Activity.ChatActivity;
import com.blindchat.Activity.ChatFriendActivity;
import com.blindchat.R;
import com.blindchat.Utility.ApiConsumer;
import com.blindchat.Utility.ApiResponse;
import com.blindchat.Utility.AppPref;
import com.blindchat.Utility.AppUrl;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

import org.json.JSONObject;

public class StrangersFragment extends Fragment {

    Button connect;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_strangers, container, false);
//        AdView adView = view.findViewById(R.id.adView);
//
//        adView.setAdSize(AdSize.BANNER);
//
//        adView.setAdUnitId("ca-app-pub-3940256099942544/6300978111");

        AdView adView = (AdView) view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
        connect = (Button) view.findViewById(R.id.connect);
        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                final ProgressDialog progressDialog=new ProgressDialog(getActivity());
//                progressDialog.setTitle("Please Wait ....");
//                progressDialog.setMessage("Searching for stranger for you ...");
//                progressDialog.show();
//                Handler handler = new Handler();
//                handler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        progressDialog.dismiss();
//
//                    }
//                }, 1000);


                //goLive();

                Intent intent = new Intent(getContext(), ChatActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }



}
