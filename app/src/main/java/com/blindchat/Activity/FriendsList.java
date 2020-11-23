package com.blindchat.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.blindchat.R;
import com.blindchat.Utility.ApiConsumer;
import com.blindchat.Utility.ApiResponse;
import com.blindchat.Utility.AppPref;
import com.blindchat.Utility.AppUrl;
import com.blindchat.adapter.SingleAdapter;
import com.blindchat.model.FriendModel;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class FriendsList extends BaseActivity implements SingleAdapter.ReturnView
{

    private ListView friends_list;
    private ArrayList<FriendModel> friendModels=new ArrayList<>();

    @Override
    public void initialize(Bundle save) {
        setTitle("Friend Request");
        friends_list=(ListView) findViewById(R.id.friends_list);
        loaddata();


        AdView adView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
      //  toastMessage("hello");
    }

    @Override
    public int getActivityLayout() {
        return R.layout.activity_friends_list;
    }

    private void loaddata()
    {
        StringBuilder stringBuilder=new StringBuilder();
        stringBuilder.append("user_id=").append(AppPref.getInstance().getUSERID());

        String content=stringBuilder.toString();
        ApiConsumer apiConsumer=new ApiConsumer(this, AppUrl.FRIEND_REQ, 0, content, true, "Loading ...", new ApiResponse() {
            @Override
            public void getApiResponse(String responseData, int serviceCounter) {
                try {
                    Log.d("resposnedata",responseData);
                    JSONArray jsonArray=new JSONArray(responseData);
                    for(int i=0;i<jsonArray.length();i++)
                    {
                        JSONObject jsonObject=jsonArray.getJSONObject(i);
                        String id=jsonObject.getString("id");
                        String name=jsonObject.getString("name");
                        String freied=jsonObject.getString("user_id");

                        friendModels.add(new FriendModel(id,name,freied));
                    }

                    friends_list.setAdapter(new SingleAdapter(FriendsList.this,R.layout.friend_single_item,friendModels, FriendsList.this,0));
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
        apiConsumer.execute();
    }

    @Override
    public void getAdapterView(View view, List objects, int position, int from) {
        final TextView friend_name=(TextView) view.findViewById(R.id.friend_name);
        final FriendModel friendModel=(FriendModel) objects.get(position);
        friend_name.setText("BC0000"+friendModel.getFriendid());

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeafriend(friend_name.getText().toString(),friendModel.getId());
            }
        });
    }

    private void makeafriend(String name,final String id)
    {
        AlertDialog.Builder adb=new AlertDialog.Builder(this);
        adb.setTitle("Make a friend");
        adb.setMessage("Are you sure you want to make "+name+" your friend?");
        adb.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                confirmFriend(id);
            }
        });
        adb.setNegativeButton("NO",null);
        adb.show();
    }

    private void confirmFriend(String id)
    {
        StringBuilder stringBuilder=new StringBuilder();
        stringBuilder.append("id=").append(id);

        String content=stringBuilder.toString();
        ApiConsumer apiConsumer=new ApiConsumer(this, AppUrl.CONFIRM_FRIEND, 0, content, true, "Loading ...", new ApiResponse() {
            @Override
            public void getApiResponse(String responseData, int serviceCounter) {
                try {
                    Log.d("resposnedata",responseData);
                    JSONObject jsonObject=new JSONObject(responseData);
                    String message=jsonObject.getString("message");
                    toastMessage(message);
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