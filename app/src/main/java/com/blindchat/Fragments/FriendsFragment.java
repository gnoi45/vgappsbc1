package com.blindchat.Fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.blindchat.Activity.ChatActivity;
import com.blindchat.Activity.ChatFriendActivity;
import com.blindchat.R;
import com.blindchat.Utility.ApiConsumer;
import com.blindchat.Utility.ApiResponse;
import com.blindchat.Utility.AppPref;
import com.blindchat.Utility.AppUrl;
import com.blindchat.adapter.SingleAdapter;
import com.blindchat.model.FriendModel;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class FriendsFragment extends Fragment implements View.OnClickListener, SingleAdapter.ReturnView{


    Geocoder geocoder;
    List<Address> addresses;
    private static final int REQUEST_INT = 101;

    private ListView listView;
    private List<FriendModel> friendModels=new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friends, container, false);

        geocoder = new Geocoder(getContext(), Locale.getDefault());

        listView=(ListView) view.findViewById(R.id.listview);

        AdView adView = (AdView) view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);


        loaddata();
        loaddata1();
        //getLocation();
        return view;
    }

    private void loaddata()
    {
        StringBuilder stringBuilder=new StringBuilder();
        stringBuilder.append("user_id=").append(AppPref.getInstance().getUSERID());

        String content=stringBuilder.toString();
        ApiConsumer apiConsumer=new ApiConsumer(getActivity(), AppUrl.FRIEND_LIST, 0, content, true, "Loading ...", new ApiResponse() {
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
                        String token=jsonObject.getString("token");
                        String freied=jsonObject.getString("friend_id");
                        String status=jsonObject.getString("status");

                        friendModels.add(new FriendModel(id,name,freied,status,token));
                    }

                    listView.setAdapter(new SingleAdapter(getActivity(),R.layout.friend_single_item,friendModels,FriendsFragment.this,0));
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
        apiConsumer.execute();
    }


    private void loaddata1()
    {
        StringBuilder stringBuilder=new StringBuilder();
        stringBuilder.append("user_id=").append(AppPref.getInstance().getUSERID());

        String content=stringBuilder.toString();
        ApiConsumer apiConsumer=new ApiConsumer(getActivity(), AppUrl.FRI_LIST, 0, content, true, "Loading ...", new ApiResponse() {
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
                        String token=jsonObject.getString("token");
                        String freied=jsonObject.getString("user_id");
                        String status=jsonObject.getString("status");

                        friendModels.add(new FriendModel(id,name,freied,status,token));
                    }

                    listView.setAdapter(new SingleAdapter(getActivity(),R.layout.friend_single_item,friendModels,FriendsFragment.this,0));
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
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ll1:
              //  sendToActivity(ChatFriendActivity.class);
                break;
        }
    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_INT);
        } else {
            getCurrentLocation();
        }
    }

    private void getCurrentLocation() {
        final LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);


        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.getFusedLocationProviderClient(getContext()).requestLocationUpdates(locationRequest, new LocationCallback() {

            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                LocationServices.getFusedLocationProviderClient(getContext())
                        .removeLocationUpdates(this);
                if (locationResult != null && locationResult.getLocations().size() > 0) {
                    int latestLocationIndex = locationResult.getLocations().size() - 1;
                    double latitude = locationResult.getLocations().get(latestLocationIndex).getLatitude();
                    double longitude = locationResult.getLocations().get(latestLocationIndex).getLongitude();
                    try {
                        addresses = geocoder.getFromLocation(latitude, longitude, 1);
                        String city = addresses.get(0).getLocality();
                        String state = addresses.get(0).getAdminArea();
                        String country = addresses.get(0).getCountryName();
                        StringBuilder builder = new StringBuilder();
                        builder.append("City is : ").append(city).append(" , state is : ").append(state).append(" and country is : ").append(country);
                        String result = builder.toString();
                        Toast.makeText(getContext(), result, Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, Looper.myLooper());
    }

    private void sendToActivity(Class className) {
        Intent intent = new Intent(getContext(), className);
        startActivity(intent);
    }

    @Override
    public void getAdapterView(View view, List objects, int position, int from) {
        final TextView friend_name=(TextView) view.findViewById(R.id.friend_name);
        final FriendModel friendModel=(FriendModel) objects.get(position);
        friend_name.setText(friendModel.getFriendname());

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),ChatFriendActivity.class);
                intent.putExtra("id",friendModel.getFriendid());
                intent.putExtra("t_id",friendModel.getId());
                intent.putExtra("name",friendModel.getFriendname());
                intent.putExtra("status",friendModel.getStatus());
                intent.putExtra("token",friendModel.getToken());
                startActivity(intent);
            }
        });
    }
}
