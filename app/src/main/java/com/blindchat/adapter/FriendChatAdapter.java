package com.blindchat.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;


import com.blindchat.Activity.VideoPreview;
import com.blindchat.R;
import com.blindchat.Utility.AppPref;
import com.blindchat.model.ChatModel;
import com.blindchat.model.Message;
import com.squareup.picasso.Picasso;

import java.util.List;

import static com.blindchat.Activity.ChatFriendActivity.fontsize;


public class FriendChatAdapter  extends RecyclerView.Adapter<FriendChatAdapter.MyViewHolder> {
    private List<ChatModel> MessageList;
    private Context context;

    public  class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView msg_right;
        public LinearLayout right;
        public LinearLayout left;
        public TextView msg_left;
        public ImageView image_right;
        public ImageView image_left;
        public ImageView video_right;
        public ImageView video_left;
        public TextView time_left;
        public TextView time_right;


        public MyViewHolder(View view) {
            super(view);

            msg_right = (TextView) view.findViewById(R.id.msg_right);
            right=(LinearLayout) view.findViewById(R.id.right);
            left=(LinearLayout) view.findViewById(R.id.left);
            msg_left=(TextView) view.findViewById(R.id.msg_left);
            image_right=(ImageView) view.findViewById(R.id.image_right);
            image_left=(ImageView) view.findViewById(R.id.image_left);
            video_right=(ImageView) view.findViewById(R.id.video_right);
            video_left=(ImageView) view.findViewById(R.id.video_left);
            time_left=(TextView) view.findViewById(R.id.time_left);
            time_right=(TextView) view.findViewById(R.id.time_right);


        }
    }
    public FriendChatAdapter(Context context, List<ChatModel>MessagesList) {
        this.context=context;
        this.MessageList = MessagesList;


    }

    @Override
    public int getItemCount() {
        return MessageList.size();
    }
    @Override
    public FriendChatAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item, parent, false);



        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final ChatModel m = MessageList.get(position);
        if(m.getFrom_id().equalsIgnoreCase(AppPref.getInstance().getUSERID())) {

            holder.left.setVisibility(View.GONE);
            holder.right.setVisibility(View.VISIBLE);
            holder.time_right.setText(m.getTimes());

            if(m.getType().equalsIgnoreCase("text"))
            {
                holder.msg_right.setText(m.getMsg());
                holder.msg_right.setTextSize(fontsize);
                holder.image_right.setVisibility(View.GONE);
                holder.msg_right.setVisibility(View.VISIBLE);
                holder.video_right.setVisibility(View.GONE);
                holder.right.setBackground(context.getResources().getDrawable(R.drawable.sender_chat_balloon));
                holder.time_right.setTextColor(context.getResources().getColor(R.color.white));
            }
            else if(m.getType().equalsIgnoreCase("image"))
            {
                Picasso.with(context).load("https://earnezy.in/blind/uploads/"+m.getMsg()).into(holder.image_right);
                holder.image_right.setVisibility(View.VISIBLE);
                holder.msg_right.setVisibility(View.GONE);
                holder.video_right.setVisibility(View.GONE);
                holder.right.setBackground(null);
                holder.time_right.setTextColor(context.getResources().getColor(R.color.black));
            }
            else if(m.getType().equalsIgnoreCase("video"))
            {
              //  Picasso.with(context).load("https://earnezy.in/blind/uploads/"+m.getMsg()).into(holder.image_right);
                holder.image_right.setVisibility(View.GONE);
                holder.msg_right.setVisibility(View.GONE);
                holder.video_right.setVisibility(View.VISIBLE);
                holder.video_right.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(context, VideoPreview.class);
                        intent.putExtra("video","https://earnezy.in/blind/uploads/"+m.getMsg());
                        context.startActivity(intent);
                    }
                });
                holder.right.setBackground(null);
                holder.time_right.setTextColor(context.getResources().getColor(R.color.black));
            }
        }
        else if(!m.getFrom_id().equalsIgnoreCase(AppPref.getInstance().getUSERID()))
        {
            //holder.msg_left.setText(m.getMsg());

            if(m.getType().equalsIgnoreCase("text"))
            {
                holder.msg_left.setText(m.getMsg());
                holder.msg_left.setTextSize(fontsize);
                holder.msg_left.setVisibility(View.VISIBLE);
                holder.image_left.setVisibility(View.GONE);
                holder.video_left.setVisibility(View.GONE);
                holder.left.setBackground(context.getResources().getDrawable(R.drawable.receiver_chat_balloon));
                holder.time_left.setTextColor(context.getResources().getColor(R.color.white));
            }
            else if(m.getType().equalsIgnoreCase("image"))
            {
                Picasso.with(context).load("https://earnezy.in/blind/uploads/"+m.getMsg()).into(holder.image_left);

                holder.msg_left.setVisibility(View.GONE);
                holder.image_left.setVisibility(View.VISIBLE);
                holder.video_left.setVisibility(View.GONE);
                holder.left.setBackground(null);
                holder.time_left.setTextColor(context.getResources().getColor(R.color.black));
            }

            else if(m.getType().equalsIgnoreCase("video"))
            {
                //Picasso.with(context).load("https://earnezy.in/blind/uploads/"+m.getMsg()).into(holder.image_left);

                holder.msg_left.setVisibility(View.GONE);
                holder.image_left.setVisibility(View.GONE);
                holder.video_left.setVisibility(View.VISIBLE);
                holder.video_left.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                            Intent intent=new Intent(context, VideoPreview.class);
                            intent.putExtra("video","https://earnezy.in/blind/uploads/"+m.getMsg());
                            context.startActivity(intent);
                    }
                });
                holder.left.setBackground(null);
                holder.time_left.setTextColor(context.getResources().getColor(R.color.black));
            }
            holder.left.setVisibility(View.VISIBLE);
            holder.right.setVisibility(View.GONE);
            holder.time_left.setText(m.getTimes());

        }
    }



}