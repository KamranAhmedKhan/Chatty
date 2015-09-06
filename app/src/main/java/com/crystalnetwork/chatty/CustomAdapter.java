package com.crystalnetwork.chatty;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class CustomAdapter extends ArrayAdapter {

    ImageView post_imageView;
    Button share_Button;
    TextView post_name;
    TextView post_msg;
    TextView post_time;
    TextView post_date;
    LayoutInflater layoutInflater;
    public CustomAdapter(Activity activity,ArrayList<Post> list){
        super(activity,0,list);
         layoutInflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public View getView(final int position,View convertView, ViewGroup parent){

        if(convertView==null) {
            convertView = layoutInflater.inflate(R.layout.custom_posts_list, null);
        }

        share_Button = (Button)convertView.findViewById(R.id.share_button);
        post_imageView = (ImageView)convertView.findViewById(R.id.post_imageView);
        post_name = (TextView)convertView.findViewById(R.id.post_name);
        post_msg = (TextView)convertView.findViewById(R.id.post_msg);
        post_time = (TextView)convertView.findViewById(R.id.post_time);
        post_date = (TextView)convertView.findViewById(R.id.post_date);

        Post p = (Post)getItem(position);
        post_name.setText(p.name);
        post_msg.setText(p.msg);
        post_time.setText(p.time);
        post_date.setText(p.date);

        return convertView;
    }
}





