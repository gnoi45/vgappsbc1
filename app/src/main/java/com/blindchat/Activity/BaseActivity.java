package com.blindchat.Activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.blindchat.R;


/**
 * Created by GOKUL on 31-08-2017.
 */
public abstract class BaseActivity extends AppCompatActivity {

    public void onCreate(Bundle save) {
        super.onCreate(save);
        setContentView(getActivityLayout());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initialize(save);
    }

    public abstract void initialize(Bundle save);

    public abstract int getActivityLayout();

    public void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void onBackPressed() {
        super.onBackPressed();
       // overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
    }

    public boolean onCreateOptionsMenu(Menu menu)
    {
        super.onCreateOptionsMenu(menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem menuItem)
    {
        super.onOptionsItemSelected(menuItem);
        switch (menuItem.getItemId())
        {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return true;
    }

    public void alertMessage(String title, String message) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setPositiveButton("OK", null);
        alertDialog.show();
    }

    public void sendToActivity(Class className) {
        Intent intent = new Intent(this, className);
        startActivity(intent);
        //overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
    }

    public void sendToActivity(Class className, int flags) {
        Intent intent = new Intent(this, className);
        intent.setFlags(flags);
        startActivity(intent);
       // overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
    }

    public void sendToActivity(Class className, String[] strings,int flags) {
        Intent intent = new Intent(this, className);
        for (int i = 0; i < strings.length; i++) {
            String s1 = strings[i];
            String s2[] = s1.split(";");
            intent.putExtra(s2[0], s2[1]);
        }
        intent.setFlags(flags);
        startActivity(intent);
       // overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
    }

    public void sendToActivity(Class className, String[] strings) {
        Intent intent = new Intent(this, className);
        for (int i = 0; i < strings.length; i++) {
            String s1 = strings[i];
            String s2[] = s1.split(";");
            intent.putExtra(s2[0], s2[1]);
        }
        startActivity(intent);
        //overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
    }

    public void setFragment(Fragment fragment, int id) {
        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        t.replace(id, fragment);
        t.commit();
    }
}
