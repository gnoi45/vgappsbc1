package com.blindchat.Activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blindchat.R;
import com.blindchat.Utility.ApiConsumer;
import com.blindchat.Utility.ApiResponse;
import com.blindchat.Utility.AppPref;
import com.blindchat.Utility.AppUrl;
import com.blindchat.adapter.ChatBoxAdapter;
import com.blindchat.model.Message;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;




public class ChatActivity extends AppCompatActivity {

    public List<Message> MessageList;
    ImageView emoji;
    EmojiconEditText emojiconEditText;
    View rootView;
    EmojIconActions emojiIcon;
    ImageView send;
    RecyclerView listview;
    ChatBoxAdapter chatBoxAdapter;

    private String to_id = "";

    private Socket socket;
    private ProgressDialog progressDialog;
    private TextView stranger_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Toolbar toolbar=(Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        stranger_id=toolbar.findViewById(R.id.stranger_id);
        rootView = findViewById(R.id.rootView);
        listview = findViewById(R.id.listview);
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


        MessageList = new ArrayList<>();
        listview = (RecyclerView) findViewById(R.id.listview);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        listview.setLayoutManager(linearLayoutManager);
        listview.setItemAnimator(new DefaultItemAnimator());

        send = (ImageView) findViewById(R.id.send);

        MessageList = new ArrayList<>();

        goLive();




        // message send action
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //retrieve the nickname and the message content and fire the event messagedetection
                if (!emojiconEditText.getText().toString().isEmpty()) {
                    Calendar calendar= Calendar.getInstance();
                    int HOUR=calendar.get(Calendar.HOUR_OF_DAY);
                    int MINUTE=calendar.get(Calendar.MINUTE);
                    String xx=HOUR+" : "+MINUTE;
                    socket.emit("messagedetection", AppPref.getInstance().getUNIQID(), xx+";"+emojiconEditText.getText().toString());

                    emojiconEditText.setText("");
                }


            }
        });


    }

    private void deletefriend()
    {
        StringBuilder stringBuilder=new StringBuilder();
        stringBuilder.append("user_id=").append(AppPref.getInstance().getUSERID());


        String content=stringBuilder.toString();

        ApiConsumer apiConsumer=new ApiConsumer(this, AppUrl.DELETE_STRANGER, 0, content, false, "Loading ...", new ApiResponse() {
            @Override
            public void getApiResponse(String responseData, int serviceCounter) {
                try
                {
                   Log.d("responsedata",responseData);
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
    public void onStop()
    {
        deletefriend();
        super.onStop();
    }


    private void goLive()
    {
        progressDialog= new ProgressDialog(this);
        progressDialog.setTitle("Searching ");
        progressDialog.setMessage("Searching for Right Stranger for you, pls be patient.... ");
        progressDialog.show();

        StringBuilder stringBuilder=new StringBuilder();
        stringBuilder.append("user_id=").append(AppPref.getInstance().getUSERID());


        String content=stringBuilder.toString();

        ApiConsumer apiConsumer=new ApiConsumer(this, AppUrl.GO_LIVE, 0, content, false, "Loading ...", new ApiResponse() {
            @Override
            public void getApiResponse(String responseData, int serviceCounter) {
                try
                {
                    JSONObject jsonObject=new JSONObject(responseData);
                    String id=jsonObject.getString("id");
                    //Toast.makeText(getActivity(),"Added",Toast.LENGTH_LONG).show();
                    getConnect();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
        apiConsumer.execute();

    }


    int count=0;
    private void getConnect()
    {

        StringBuilder stringBuilder=new StringBuilder();
        stringBuilder.append("user_id=").append(AppPref.getInstance().getUSERID());


        String content=stringBuilder.toString();

        ApiConsumer apiConsumer=new ApiConsumer(this, AppUrl.GET_CONNECT, 0, content, false, "Loading ...", new ApiResponse() {
            @Override
            public void getApiResponse(String responseData, int serviceCounter) {
                try
                {

                    JSONObject jsonObject=new JSONObject(responseData);
                    String user_id=jsonObject.getString("user_id");
                    //Toast.makeText(getActivity(),"Added",Toast.LENGTH_LONG).show();
                    to_id=user_id;

                    stranger_id.setText("BC0000"+to_id);
                    progressDialog.dismiss();

                    allowsocket();

                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    count++;
                    getConnect();
                }
            }
        });
        apiConsumer.execute();

    }

    private void allowsocket()
    {
        try {
            socket = IO.socket(AppUrl.MAIN_URL);
            socket.connect();
            socket.emit("join", AppPref.getInstance().getUNIQID());
        } catch (URISyntaxException e) {
            e.printStackTrace();

        }


        //implementing socket listeners
        socket.on("userjoinedthechat", new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String data = (String) args[0];

                        Toast.makeText(ChatActivity.this, data, Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });
        socket.on("userdisconnect", new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String data = (String) args[0];

                        deletefriend();
                        Toast.makeText(ChatActivity.this, data, Toast.LENGTH_SHORT).show();
                        finish();

                    }
                });
            }
        });
        socket.on("message", new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        JSONObject data = (JSONObject) args[0];
                        try {
                            //extract data from fired event

                            String nickname = data.getString("senderNickname");
                            String message = data.getString("message");

                            String x[]=message.split(";");

                            String zz="";
                            for(int i=1;i<x.length;i++)
                            {
                                zz+=x[i];
                            }

                            // make instance of message

                            Message m = new Message(zz, x[0], nickname);


                            //add the message to the messageList

                            MessageList.add(0,m);

                            // add the new updated list to the dapter
                            chatBoxAdapter = new ChatBoxAdapter(MessageList);

                            // notify the adapter to update the recycler view

                            chatBoxAdapter.notifyDataSetChanged();

                            //set the adapter for the recycler view

                            listview.setAdapter(chatBoxAdapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                });
            }
        });
    }

    private void getErrorMsg()
    {
        deletefriend();
        AlertDialog.Builder adb=new AlertDialog.Builder(this);
        adb.setTitle("Not Found");
        adb.setCancelable(false);
        adb.setMessage("No Stranger is found online currently, pls try after sometime");
        adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        adb.setNegativeButton("TRY AGAIN", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                goLive();
            }
        });
        adb.show();
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
        builder.setTitle("Disconnect User");
        builder.setMessage("Are you sure you want to disconnect from Chat?");
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton("Disconnect", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                socket.disconnect();
                finish();
            }
        });
        builder.show();
    }

    private void displayAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Friend");
        builder.setMessage("Are you sure you want to add this person as Friend");
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                StringBuilder stringBuilder=new StringBuilder();
                stringBuilder.append("user_id=").append(AppPref.getInstance().getUSERID());
                stringBuilder.append("&friend_id=").append(to_id);

                String content=stringBuilder.toString();

                ApiConsumer apiConsumer=new ApiConsumer(ChatActivity.this, AppUrl.ADD_FRIEND, 0, content, true, "loading ...", new ApiResponse() {
                    @Override
                    public void getApiResponse(String responseData, int serviceCounter) {
                        try {
                            Log.d("responsedata",responseData);
                            JSONObject jsonObject=new JSONObject(responseData);
                            String id=jsonObject.getString("id");
                            Toast.makeText(ChatActivity.this,"Friend Added SuccessFully",Toast.LENGTH_LONG).show();
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
        });
        builder.show();
    }
}