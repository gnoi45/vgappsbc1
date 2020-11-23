package com.blindchat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

public class SingleAdapter extends ArrayAdapter {
    int resource;
    int from;
    ReturnView returnView;
    List objects;

    public interface ReturnView {
        void getAdapterView(View view, List objects, int position, int from);
    }


    public SingleAdapter(Context context, int resource, List objects, ReturnView returnView, int from) {
        super(context, resource, objects);
        this.resource = resource;
        this.returnView = returnView;
        this.from = from;
        this.objects = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Object user = getItem(position);
        convertView = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(resource, parent, false);
        }

        returnView.getAdapterView(convertView, objects, position, from);

        return convertView;
    }

}
