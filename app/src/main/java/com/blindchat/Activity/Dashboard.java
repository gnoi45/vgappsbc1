package com.blindchat.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.blindchat.Activity.ui.main.SectionsPagerAdapter;
import com.blindchat.R;
import com.blindchat.Utility.AppPref;
import com.blindchat.Utility.AppUrl;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.google.android.material.tabs.TabLayout;

import java.net.URISyntaxException;


public class Dashboard extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        Toolbar toolbar=(Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);


        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);


        String Nickname= AppPref.getInstance().getNAME();
        //connect you socket client to the server


    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item1:
                Intent intent1=new Intent(this,EarnPoint.class);
                startActivity(intent1);
                break;
            case R.id.item2:
                Intent intent=new Intent(this,ProfileActivity.class);
                startActivity(intent);
                break;
            case R.id.item3:
//                Intent intent2=new Intent(this,LockPassword.class);
//                startActivity(intent2);

                Intent intent2=new Intent(this,FriendsList.class);
                startActivity(intent2);

                break;
            case R.id.item4:
                Intent intent3=new Intent(this,BcCredits.class);
                startActivity(intent3);
                break;
            default:
                onBackPressed();
        }
        return true;
    }
}