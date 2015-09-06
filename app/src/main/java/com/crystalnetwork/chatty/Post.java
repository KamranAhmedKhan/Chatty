package com.crystalnetwork.chatty;

import android.util.Base64;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Post {
    String name;
    String image;
    String msg;
    String time;
    String date;

    public Post() {
    }

    public Post(String name,String image,String msg){
        this.name = name;
        this.image = image;
        this.msg = msg;
        this.time = " - "+Time();
        this.date = Date();
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public String getMsg() {
        return msg;
    }

    public String getTime() {
        return time;
    }

    public String getDate() {
        return date;
    }

    public Post(String name, String image, String msg, String time, String date){
        this.name = name;
        this.image = image;
        this.msg = msg;
        this.time = time;
        this.date = date;

    }



    private static String Time(){
        DateFormat df = new SimpleDateFormat("HH:mm:ss");
        Date dateobj = new Date();
        return df.format(dateobj);
    }

    private static String Date(){
        DateFormat df = new SimpleDateFormat("dd/MM/yy");
        Date dateobj = new Date();
        return df.format(dateobj);
    }
    public static Post testPost = new Post("CHATTY","abc","Wellcome to CHATTY\nThis Message will not appears on your Followers Timeline");
}
