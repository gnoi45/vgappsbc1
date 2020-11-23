package com.blindchat.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;


import com.blindchat.R;
import java.io.File;

public class VideoPreview extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setTitle("Video view");
        getSupportActionBar().hide();
        setContentView(getActivityLayout());
        initialize(savedInstanceState);
    }

    @Override
    public void initialize(Bundle save) {
        VideoView videoView =(VideoView)findViewById(R.id.videoView1);

        //Creating MediaController
        MediaController mediaController= new MediaController(this);
        mediaController.setAnchorView(videoView);

        //specify the location of media file
        save=getIntent().getExtras();
        String url=save.getString("video");
        Uri uri=Uri.parse(url);

        //Setting MediaController and URI, then starting the videoView
        videoView.setMediaController(mediaController);
        videoView.setVideoURI(uri);
        videoView.requestFocus();
        videoView.start();
    }

    @Override
    public int getActivityLayout() {
        return R.layout.video_preview;
    }


}