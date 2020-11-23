package com.blindchat.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;


import com.blindchat.R;
import com.blindchat.Utility.AppPref;
import com.blindchat.model.Message;

import java.util.List;


public class ChatBoxAdapter  extends RecyclerView.Adapter<ChatBoxAdapter.MyViewHolder> {
    private List<Message> MessageList;

    public  class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView msg_right;
        public LinearLayout right;
        public LinearLayout left;
        public TextView msg_left;
        public TextView time_right;
        public TextView time_left;

        public MyViewHolder(View view) {
            super(view);

            msg_right = (TextView) view.findViewById(R.id.msg_right);
            right=(LinearLayout) view.findViewById(R.id.right);
            left=(LinearLayout) view.findViewById(R.id.left);
            msg_left=(TextView) view.findViewById(R.id.msg_left);
            time_right=(TextView) view.findViewById(R.id.time_right);
            time_left=(TextView) view.findViewById(R.id.time_left);


        }
    }
    public ChatBoxAdapter(List<Message>MessagesList) {
        this.MessageList = MessagesList;


    }

    @Override
    public int getItemCount() {
        return MessageList.size();
    }
    @Override
    public ChatBoxAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.stranger_item, parent, false);



        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final Message m = MessageList.get(position);
        if(m.getUser().equalsIgnoreCase(AppPref.getInstance().getUNIQID())) {
            holder.msg_right.setText(m.getText());
            holder.time_right.setText(m.getTime());
            holder.left.setVisibility(View.GONE);
            holder.right.setVisibility(View.VISIBLE);
        }
        else if(!m.getUser().equalsIgnoreCase(AppPref.getInstance().getUNIQID()))
        {
            holder.msg_left.setText(m.getText());
            holder.time_left.setText(m.getTime());
            holder.left.setVisibility(View.VISIBLE);
            holder.right.setVisibility(View.GONE);
        }
    }



}