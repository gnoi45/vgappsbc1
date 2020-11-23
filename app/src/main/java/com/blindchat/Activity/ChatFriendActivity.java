package com.blindchat.Activity;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blindchat.R;
import com.blindchat.Utility.ApiConfig;
import com.blindchat.Utility.ApiConsumer;
import com.blindchat.Utility.ApiResponse;
import com.blindchat.Utility.AppConfig;
import com.blindchat.Utility.AppPref;
import com.blindchat.Utility.AppUrl;
import com.blindchat.Utility.ServerResponse;
import com.blindchat.adapter.ChatBoxAdapter;
import com.blindchat.adapter.FriendChatAdapter;
import com.blindchat.model.ChatModel;
import com.blindchat.model.Message;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;




public class ChatFriendActivity extends AppCompatActivity {



    public static int fontsize=15;

    ImageView emoji;
    EmojiconEditText emojiconEditText;
    View rootView;
    EmojIconActions emojiIcon;
    ImageView send;
    RecyclerView listview;
    FriendChatAdapter chatBoxAdapter;

    String to_id="";
    String to_name="";
    String t_id="";
    String token="";

    String type="";

    String mediaPath="";

    private ArrayList<ChatModel> chatModelArrayList=new ArrayList<>();

    private ImageView attachment_option;

    private Socket socket;
    private RelativeLayout rel_bot;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_chat);
        Toolbar toolbar=(Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        rel_bot=(RelativeLayout) findViewById(R.id.rel_bot);
        savedInstanceState=getIntent().getExtras();
        if(savedInstanceState!=null) {
            TextView title = (TextView) toolbar.findViewById(R.id.title);
            title.setText(savedInstanceState.getString("name"));
            to_name=savedInstanceState.getString("name");
            to_id=savedInstanceState.getString("id");
            t_id=savedInstanceState.getString("t_id");
            String status=savedInstanceState.getString("status");
            token=savedInstanceState.getString("token");
            if(status.equalsIgnoreCase("2"))
            {
                rel_bot.setVisibility(View.GONE);
            }
            else if(status.equalsIgnoreCase("1"))
                rel_bot.setVisibility(View.VISIBLE);
        }

        rootView = findViewById(R.id.rootView);
        listview= findViewById(R.id.listview);
        attachment_option=findViewById(R.id.attachment_option);
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


        try {
            socket = IO.socket(AppUrl.MAIN_URL);
            socket.connect();
            socket.emit("join", AppPref.getInstance().getUNIQID());
        } catch (URISyntaxException e) {
            e.printStackTrace();

        }

        attachment_option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAttachment();
            }
        });


        listview = (RecyclerView) findViewById(R.id.listview);


        send=(ImageView) findViewById(R.id.send);





        getMsg();

        // message send action
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //retrieve the nickname and the message content and fire the event messagedetection
                if(!emojiconEditText.getText().toString().isEmpty()){


                    Calendar calendar= Calendar.getInstance();

                    int YEAR=calendar.get(Calendar.YEAR);
                    int MONTH=calendar.get(Calendar.MONTH);
                    int DATE=calendar.get(Calendar.DATE);

                    int HOUR=calendar.get(calendar.HOUR_OF_DAY);
                    int MIN=calendar.get(Calendar.MINUTE);

                    String timetype=" AM";
                    int newhour=0;
                    if(HOUR>12)
                    {
                        newhour=24-HOUR;
                        timetype=" PM";
                    }
                    else if(HOUR<12)
                    {
                        newhour=HOUR;
                        timetype=" AM";
                    }

                    String time=DATE+"-"+MONTH+"-"+YEAR+" "+newhour+":"+MIN+timetype;

                    socket.emit("messagedetection", AppPref.getInstance().getUSERID()+";"+to_id,"text;"+emojiconEditText.getText().toString()+";"+time);

                    sendmsg(emojiconEditText.getText().toString(),"text",time);
                    emojiconEditText.setText("");
                }


            }
        });

        //implementing socket listeners
        socket.on("userjoinedthechat", new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String data = (String) args[0];

                        Toast.makeText(ChatFriendActivity.this,data,Toast.LENGTH_SHORT).show();

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

                        Toast.makeText(ChatFriendActivity.this,data,Toast.LENGTH_SHORT).show();
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

                            String y[]=nickname.split(";");


                            if(y!=null && y.length>1 && !y[0].equalsIgnoreCase(AppPref.getInstance().getUSERID()) && !y[1].equalsIgnoreCase(AppPref.getInstance().getUSERID()))
                            {

                            }
                            else {
                                String x[] = message.split(";");

                                String mm = "";
                                for (int i = 1; i < x.length-1; i++) {
                                    mm += x[i];
                                }


                                // make instance of message

                                //Message m = new Message(message,"",nickname);
                                ChatModel chatModel = null;
                                if (y[0].equalsIgnoreCase(AppPref.getInstance().getUSERID())) {
                                    chatModel = new ChatModel("", AppPref.getInstance().getUSERID(), to_id, mm, x[0], x[x.length-1]);
                                } else
                                    chatModel = new ChatModel("", to_id, AppPref.getInstance().getUSERID(), mm, x[0], x[x.length-1]);

                                //add the message to the messageList

                                chatModelArrayList.add(0,chatModel);
                                //chatModelArrayList.add(chatModel);

                                // add the new updated list to the dapter


                                // notify the adapter to update the recycler view

                                chatBoxAdapter.notifyDataSetChanged();

                                //set the adapter for the recycler view

                                listview.setAdapter(chatBoxAdapter);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                });
            }
        });
    }


    private void uploadFile(final String type) {

        final ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Uploading");
        progressDialog.setMessage("Uploading ...");
        progressDialog.show();
            File file = new File(mediaPath);

            RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), file);
            MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
            RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), file.getName());

            ApiConfig getResponse = AppConfig.getRetrofit().create(ApiConfig.class);
            Call<ServerResponse> call = getResponse.uploadFile(fileToUpload, filename);
            call.enqueue(new Callback<ServerResponse>() {
                @Override
                public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                    progressDialog.dismiss();
                    ServerResponse serverResponse = response.body();
                    if (serverResponse != null) {
                        if (serverResponse.getSuccess()) {

                            //Toast.makeText(getApplicationContext(), serverResponse.getMessage(), Toast.LENGTH_SHORT).show();


                            Calendar calendar= Calendar.getInstance();

                            int YEAR=calendar.get(Calendar.YEAR);
                            int MONTH=calendar.get(Calendar.MONTH);
                            int DATE=calendar.get(Calendar.DATE);

                            int HOUR=calendar.get(calendar.HOUR_OF_DAY);
                            int MIN=calendar.get(Calendar.MINUTE);

                            String timetype=" AM";
                            int newhour=0;
                            if(HOUR>12)
                            {
                                newhour=24-HOUR;
                                timetype=" PM";
                            }
                            else if(HOUR<12)
                            {
                                newhour=HOUR;
                                timetype=" AM";
                            }

                            String time=DATE+"-"+MONTH+"-"+YEAR+" "+newhour+":"+MIN+timetype;

                            socket.emit("messagedetection", AppPref.getInstance().getUSERID()+";"+to_id,type+";"+serverResponse.getMessage()+";"+time);

                            sendmsg(serverResponse.getMessage(),""+type,time);
                        } else {
                            Toast.makeText(getApplicationContext(), serverResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(ChatFriendActivity.this,"Some Error Occurred",Toast.LENGTH_LONG).show();
                        assert serverResponse != null;
                        if(serverResponse!=null)
                            Log.v("Response", serverResponse.toString());
                    }
                }

                @Override
                public void onFailure(Call<ServerResponse> call, Throwable t) {
                    Log.v("failed",t.getMessage());
                }
            });
        }


    private void openAttachment()
    {
        final String x[]={"Image","Audio","Video"};
        AlertDialog.Builder adb=new AlertDialog.Builder(this);


        adb.setTitle("Select");
        adb.setSingleChoiceItems(x, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                Toast.makeText(ChatFriendActivity.this,x[which],Toast.LENGTH_LONG).show();
                if(x[which].equalsIgnoreCase("Image"))
                pickimage();
                else if(x[which].equalsIgnoreCase("Video"))
                    pickvideo();
                dialog.dismiss();
            }
        });
        adb.show();
    }

    private void pickimage()
    {
        type="image";
        if (ActivityCompat.checkSelfPermission(ChatFriendActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(ChatFriendActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
        }
        else{
            Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            galleryIntent.setType("image/*");
            startActivityForResult(galleryIntent, 0);
        }
    }

    private void pickvideo()
    {
        type="video";
        if (ActivityCompat.checkSelfPermission(ChatFriendActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(ChatFriendActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
        }
        else{
            Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            galleryIntent.setType("video/*");
            startActivityForResult(galleryIntent, 1);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            // When an Image is picked
            if (requestCode == 0 && resultCode == RESULT_OK && null != data) {

                // Get the Image from data
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                assert cursor != null;
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                mediaPath = cursor.getString(columnIndex);

//                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//                Bitmap bitmap = decodeFile(new File(mediaPath),600,200);
//                FileOutputStream out = new FileOutputStream(mediaPath);
//                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                // Set the Image in ImageView for Previewing the Media
//                imgView.setImageBitmap(BitmapFactory.decodeFile(mediaPath));
           //     imgView.setImageBitmap(bitmap);
                cursor.close();

                uploadFile("image");
            } // When an Video is picked
            else if (requestCode == 1 && resultCode == RESULT_OK && null != data) {

                // Get the Video from data
                Uri selectedVideo = data.getData();
                String[] filePathColumn = {MediaStore.Video.Media.DATA};

                Cursor cursor = getContentResolver().query(selectedVideo, filePathColumn, null, null, null);
                assert cursor != null;
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);

                mediaPath = cursor.getString(columnIndex);


                File videoFile = new File(mediaPath);
                float lengthInBytes = videoFile.length();
                float lengthInKB = lengthInBytes / 1024;
                float lengthInMB = lengthInKB / 1024;

                if (lengthInMB > 15.0 ){
                    Toast.makeText(ChatFriendActivity.this, "Video size should be less than 15 MB", Toast.LENGTH_SHORT).show();
                }
                else {
                    // Set the Video Thumb in ImageView Previewing the Media
                    //imgView.setImageBitmap(getThumbnailPathForLocalFile(ImageUploadDemo.this, selectedVideo));
                    cursor.close();
                }

                uploadFile("video");
            } else {
                Toast.makeText(this, "You haven't picked Image/Video", Toast.LENGTH_LONG).show();
                //imgView.setVisibility(View.INVISIBLE);
            }
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
        }

    }

    public static Bitmap decodeFile(File f,int WIDTH,int HIGHT){
        try {
            //Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f),null,o);

            //The new size we want to scale to
            final int REQUIRED_WIDTH=WIDTH;
            final int REQUIRED_HIGHT=HIGHT;
            //Find the correct scale value. It should be the power of 2.
            int scale=1;
            while(o.outWidth/scale/2>=REQUIRED_WIDTH && o.outHeight/scale/2>=REQUIRED_HIGHT)
                scale*=2;

            //Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize=scale;
            return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
        } catch (FileNotFoundException e) {}
        return null;
    }


    private void getMsg()
    {
        StringBuilder stringBuilder=new StringBuilder();
        stringBuilder.append("from_id=").append(AppPref.getInstance().getUSERID());
        stringBuilder.append("&to_id=").append(to_id);


        String content=stringBuilder.toString();

        ApiConsumer apiConsumer=new ApiConsumer(this, AppUrl.GET_MSG, 0, content, true, "Loading ...", new ApiResponse() {
            @Override
            public void getApiResponse(String responseData, int serviceCounter) {
                try
                {
                    JSONArray js=new JSONArray(responseData);
                    for(int i=0;i<js.length();i++) {
                        JSONObject jsonObject = js.getJSONObject(i);
                        String id = jsonObject.getString("id");
                        String from_id=jsonObject.getString("from_id");
                        String t_id=jsonObject.getString("to_id");
                        String msg=jsonObject.getString("msg");
                        String type=jsonObject.getString("type");
                        String times=jsonObject.getString("times");


                        chatModelArrayList.add(new ChatModel(id,from_id,t_id,msg,type,times));
                    }

                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

                chatBoxAdapter = new FriendChatAdapter(ChatFriendActivity.this,chatModelArrayList);
                final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ChatFriendActivity.this);
                linearLayoutManager.setReverseLayout(true);
                listview.setLayoutManager(linearLayoutManager);
                listview.setItemAnimator(new DefaultItemAnimator());
                listview.setAdapter(chatBoxAdapter);
            }
        });
        apiConsumer.execute();
    }


    private void sendmsg(String msg,String type,String time)
    {


        StringBuilder stringBuilder=new StringBuilder();
        stringBuilder.append("from_id=").append(AppPref.getInstance().getUSERID());
        stringBuilder.append("&to_id=").append(to_id);
        stringBuilder.append("&msg=").append(msg);
        stringBuilder.append("&type=").append(type);
        stringBuilder.append("&times=").append(time);
        stringBuilder.append("&token=").append(token);


        String content=stringBuilder.toString();

        ApiConsumer apiConsumer=new ApiConsumer(this, AppUrl.SEND_MSG, 0, content, false, "Loading ...", new ApiResponse() {
            @Override
            public void getApiResponse(String responseData, int serviceCounter) {
                try
                {
                    JSONObject jsonObject=new JSONObject(responseData);
                    String id=jsonObject.getString("id");
                    Toast.makeText(ChatFriendActivity.this,"Msg Sent",Toast.LENGTH_LONG).show();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
        apiConsumer.execute();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.friend_top_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_block:
                displayAlert();
                break;
            case R.id.menu_disconnect:
                displaySeekBar();
                break;
            default:
                onBackPressed();
        }
        return true;
    }

    private void displayAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Block");
        builder.setMessage("Are you sure you want to block "+to_name+"? you will never be able to chat with them again");
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
                stringBuilder.append("id=").append(t_id);

                String content=stringBuilder.toString();

                ApiConsumer apiConsumer=new ApiConsumer(ChatFriendActivity.this, AppUrl.BLOCK_FRIEND, 0, content, true, "loading ...", new ApiResponse() {
                    @Override
                    public void getApiResponse(String responseData, int serviceCounter) {
                        try {
                            Log.d("responsedata",responseData);
                            JSONObject jsonObject=new JSONObject(responseData);
                            String id=jsonObject.getString("message");
                            Toast.makeText(ChatFriendActivity.this,"You have blocked " + to_name+ " successfully.",Toast.LENGTH_LONG).show();
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


    private void displaySeekBar() {
        AlertDialog.Builder ADB = new AlertDialog.Builder(ChatFriendActivity.this);
        ADB.setTitle("Set Font Size");
        SeekBar seekbar = new SeekBar(ChatFriendActivity.this);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            seekbar.setMin(15);
//        }
        seekbar.setMax(40);
        seekbar.setKeyProgressIncrement(1);
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            @Override
            public void onProgressChanged(SeekBar sb2, int intger, boolean id)
            {
                int MIN = 15;
                if (intger < MIN) {
                    fontsize = MIN;
                } else {
                    fontsize = intger;
                }
                chatBoxAdapter.notifyDataSetChanged();

            }

            @Override
            public void onStartTrackingTouch(SeekBar sb)
            {

            }

            @Override
            public void onStopTrackingTouch(SeekBar sb)
            {
                sb.setProgress(fontsize);
            }
        });
        ADB.setView(seekbar);
        ADB.show();
    }
}