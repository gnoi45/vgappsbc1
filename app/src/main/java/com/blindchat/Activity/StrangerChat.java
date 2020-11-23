package com.blindchat.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;


import com.blindchat.R;

import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;

public class StrangerChat extends AppCompatActivity {

    ImageView emoji;
    EmojiconEditText emojiconEditText;
    View rootView;
    EmojIconActions emojiIcon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stranger_chat);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        rootView = findViewById(R.id.rootView);
        emoji = (ImageView) findViewById(R.id.emoji);
        emojiconEditText = (EmojiconEditText) findViewById(R.id.message);
        emojiIcon = new EmojIconActions(this, rootView, emojiconEditText, emoji);
        emojiIcon.ShowEmojIcon();
        emojiIcon.setKeyboardListener(new EmojIconActions.KeyboardListener() {
            @Override
            public void onKeyboardOpen() {
                Log.d("keyboardOpen", "open");
            }

            @Override
            public void onKeyboardClose() {
                Log.d("keyboardClose", "close");
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.stranger_top_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_block:
                dialogDisplay();
                break;
            case R.id.menu_add:
                displayAlert();
                break;
            default:
                onBackPressed();
        }
        return true;
    }

    private void dialogDisplay() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Block User");
        builder.setMessage("Are you sure you want to block?");
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton("Block", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }

    private void displayAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add User");
        builder.setMessage("Are you sure you want to add as a friend?");
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }
}